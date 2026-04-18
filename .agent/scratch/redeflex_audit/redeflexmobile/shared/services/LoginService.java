package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.login.LoginRequest;
import com.axys.redeflexmobile.shared.models.login.LoginResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @POST("Authentication")
    Single<LoginResponse> login(@Body LoginRequest request);

    @GET("Colaborador")
    Single<Colaborador> fetchUser(@Query("imei") String imei);
}
