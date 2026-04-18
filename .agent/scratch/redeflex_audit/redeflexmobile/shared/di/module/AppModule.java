package com.axys.redeflexmobile.shared.di.module;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBCredencial;
import com.axys.redeflexmobile.shared.manager.ProvideString;
import com.axys.redeflexmobile.shared.manager.ProvideStringImpl;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiRetrofit;
import com.axys.redeflexmobile.shared.util.CheckForInternetProvider;
import com.axys.redeflexmobile.shared.util.CheckForInternetProviderImpl;
import com.axys.redeflexmobile.shared.util.CipherTransformation;
import com.axys.redeflexmobile.shared.util.CipherWrapper;
import com.axys.redeflexmobile.shared.util.CipherWrapperImpl;
import com.axys.redeflexmobile.shared.util.DeviceUniqueProvider;
import com.axys.redeflexmobile.shared.util.DeviceUniqueProviderImpl;
import com.axys.redeflexmobile.shared.util.KeyStoreWrapper;
import com.axys.redeflexmobile.shared.util.KeyStoreWrapperImpl;
import com.axys.redeflexmobile.shared.util.SharedPreferenceEncrypted;
import com.axys.redeflexmobile.shared.util.SharedPreferenceEncryptedImpl;
import com.axys.redeflexmobile.shared.util.exception.AppExceptionUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.RedeflexApplication;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author diego on 05/10/17.
 */

@Module
public class AppModule {

    private static final int DEFAULT_TIMEOUT = 60;

    @NonNull
    public static OkHttpClient getHeader() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }

    @Provides
    public static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_SERVICE_URL)
                .client(getHeader())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat(Config.FormatDateTimeStringBancoJson)
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public static GoogleMapsApiRetrofit provideGoogleMapsRetrofit() {
        return new GoogleMapsApiRetrofit();
    }

    @Provides
    @Singleton
    Context provideContext(RedeflexApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    ExceptionUtils provideException(Retrofit retrofit, Context context) {
        return new AppExceptionUtils(retrofit, context);
    }

    @Provides
    SchedulerProvider provideScheduler() {
        return new AppSchedulerProvider();
    }

    @Provides
    ProvideString provideString(Context context) {
        return new ProvideStringImpl(context);
    }

    @Provides
    CipherWrapper provideCipherWrapper() {
        return new CipherWrapperImpl(CipherTransformation.TRANSFORMATION_PADDING);
    }

    @Provides
    KeyStoreWrapper provideKeyStoreWrapper(Context context) {
        return new KeyStoreWrapperImpl(context);
    }

    @Provides
    SharedPreferenceEncrypted provideSharedPreferenceEncrypted(CipherWrapper cipherWrapper,
                                                               KeyStoreWrapper keyStoreWrapper,
                                                               DBCredencial dbCredencial
    ) {
        return new SharedPreferenceEncryptedImpl(
                cipherWrapper,
                keyStoreWrapper,
                dbCredencial
        );
    }

    @Provides
    CheckForInternetProvider provideCheckForInternetProvider(RedeflexApplication application) {
        return new CheckForInternetProviderImpl((ConnectivityManager) application.getSystemService(
                Context.CONNECTIVITY_SERVICE
        ));
    }

    @Provides
    DeviceUniqueProvider provideDeviceUnique(Context context) {
        return new DeviceUniqueProviderImpl(context);
    }
}

