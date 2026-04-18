package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.models.PendenciaCliente;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaClienteService {

    @GET("PendenciaCliente")
    Single<List<PendenciaCliente>> getAll(@Query("idVendedor") String vendedorId, @Query("tipoCarga") String carga);

    @POST(URLs.URL_PENDENCIA_CLIENTE)
    Single<PendenciaCliente> save(@Body PendenciaCliente pendenciaCliente);
}
