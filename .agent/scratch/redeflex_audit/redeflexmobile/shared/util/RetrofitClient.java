package com.axys.redeflexmobile.shared.util;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofit()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.REST_SERVICE_URL)
                    .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGsonInstance()))
                    .build();
        }
        return retrofit;
    }
}
