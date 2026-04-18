package com.axys.redeflexmobile.shared.services.migracao;

import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.services.URLs;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.axys.redeflexmobile.shared.services.URLs.URL_MOTIVO_MIGRACAO_SUB;
import static com.axys.redeflexmobile.shared.services.URLs.URL_MOTIVO_MIGRACAO_SUB_GET;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public interface MotiveMigrationSubService {

    @GET(URL_MOTIVO_MIGRACAO_SUB_GET)
    Single<List<MotiveMigrationSub>> getAll(@Query("idVendedor") String vendedorId, @Query("tipoCarga") String carga);

    @PUT(URL_MOTIVO_MIGRACAO_SUB)
    Single<MotiveMigrationSub> save(@Body MotiveMigrationSub motiveMigrationSub);
}
