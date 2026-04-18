package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.models.Pendencia;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaService {

    @GET("Pendencia")
    Single<List<Pendencia>> getAll(@Query("idVendedor") String vendedorId, @Query("tipoCarga") String carga);

    @POST(URLs.URL_PENDENCIA)
    Single<Pendencia> save(@Body Pendencia pendencia);
}
