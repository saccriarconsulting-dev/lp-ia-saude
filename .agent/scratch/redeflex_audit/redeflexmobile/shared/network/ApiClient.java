package com.axys.redeflexmobile.shared.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Singleton, thread-safe HTTP client for unified configuration.
 * Consolidates cache, interceptors, timeouts, call adapters and converter.
 */
public final class ApiClient {
    private static volatile ApiClient instance;
    private final Retrofit retrofit;
    private final NetworkErrorHandler errorHandler = new NetworkErrorHandler();

    // Timeouts
    private static final int CONNECT_TIMEOUT_S = 3;
    private static final int READ_TIMEOUT_S = 3;
    private static final int WRITE_TIMEOUT_S = 3;

    // Cache configuration
    private static final long CACHE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB
    private static final int MAX_AGE_SECONDS = 60;
    private static final int STALE_WHILE_REVALIDATE = 60 * 60;
    private static final int MAX_STALE_OFFLINE_SECONDS = 60 * 60 * 24 * 7;

    private ApiClient(@NonNull Context context) {
        Context appCtx = context.getApplicationContext();

        // Build cache for offline use
        Cache cache = new Cache(new File(appCtx.getCacheDir(), "http_cache"), CACHE_SIZE_BYTES);

        // Interceptor for offline caching
        Interceptor offlineInterceptor = chain -> {
            Request req = chain.request();
            ConnectivityManager cm = (ConnectivityManager)
                    appCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
            if (netInfo == null || !netInfo.isConnected()) {
                req = req.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale="
                                + MAX_STALE_OFFLINE_SECONDS)
                        .build();
            }
            return chain.proceed(req);
        };

        // Interceptor to apply network cache headers
        Interceptor networkInterceptor = chain -> {
            okhttp3.Response response = chain.proceed(chain.request());
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + MAX_AGE_SECONDS
                            + ", stale-while-revalidate=" + STALE_WHILE_REVALIDATE)
                    .build();
        };

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT_S, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT_S, TimeUnit.MINUTES)
                .writeTimeout(WRITE_TIMEOUT_S, TimeUnit.MINUTES);

                // .cache(cache);

//                .addInterceptor(new ConnectivityInterceptor(appCtx))
//                .addInterceptor(new Force200Interceptor())
//                .addInterceptor(offlineInterceptor)
//                .addInterceptor(new AuthInterceptor(
//                        appCtx,
//                        new DBColaborador(appCtx),
//                        new DBTokenCliente(appCtx)
//                ))
//                .addInterceptor(new HttpErrorInterceptor())
//                .addInterceptor(new RetryInterceptor())
//                .addInterceptor(new MetricsInterceptor());

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpBuilder.addInterceptor(logger);

        // httpBuilder.addNetworkInterceptor(networkInterceptor);

        OkHttpClient client = httpBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_SERVICE_URL)
                .client(client)
//                .addCallAdapterFactory(ResultCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGsonInstance()))
                .build();
    }

    /**
     * Returns the singleton instance, initializing if necessary.
     */
    @NonNull
    public static ApiClient getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient(context);
                }
            }
        }
        return instance;
    }

    /**
     * Creates and returns a Retrofit service implementation.
     */
    @NonNull
    public <S> S createService(@NonNull Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    /**
     * Executes a synchronous call and wraps the result in a Result<T>.
     */
    @NonNull
    public <T> Result<T> executeCall(@NonNull Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return Result.success(response.body());
            } else {
                return Result.error(errorHandler.apply(new HttpException(response)));
            }
        } catch (Throwable t) {
            return Result.error(errorHandler.apply(t));
        }
    }

    /**
     * Unified request method for both synchronous and reactive usage.
     * Clones the call to allow repeated execution.
     */
    @NonNull
    public <T> Result<T> request(@NonNull Call<T> call) {
        return executeCall(call.clone());
    }

    /**
     * Converts a call into a reactive Single<T>, propagating HTTP errors.
     */
    @NonNull
    public <T> Single<T> toSingle(@NonNull Call<T> call) {
        return Single.fromCallable(() -> {
            Response<T> resp = call.clone().execute();
            if (!resp.isSuccessful() || resp.body() == null) {
                throw new HttpException(resp);
            }
            return resp.body();
        });
    }

    /**
     * Provides direct access to the underlying Retrofit instance.
     */
    @NonNull
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
