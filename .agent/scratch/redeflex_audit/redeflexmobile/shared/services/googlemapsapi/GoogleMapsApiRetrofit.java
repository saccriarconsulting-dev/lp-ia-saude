package com.axys.redeflexmobile.shared.services.googlemapsapi;

import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Rogério Massa on 31/01/19.
 */

public class GoogleMapsApiRetrofit {

    private static final String IMAGE_URL = "https://maps.googleapis.com/maps/api/";
    private static final int DEFAULT_TIMEOUT = 60;

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(IMAGE_URL)
                .client(getHeader())
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGsonInstance()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getHeader() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(urlParser())
                .build();
    }

    private Interceptor urlParser() {
        return chain -> {
            Request request = chain.request();
            String string = request.url().toString();
            string = string.replaceAll("%3D", "=");
            string = string.replaceAll("%2C", ",");
            Request newRequest = new Request.Builder()
                    .url(string)
                    .build();
            return chain.proceed(newRequest);
        };
    }
}
