package com.axys.redeflexmobile.shared.services.migracao;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.services.URLs;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public interface RegisterMigrationSubService {

    @PUT(URLs.URL_CADASTRO_MIGRACAO_SUB)
    Single<RegisterMigrationSub> save(@Body RegisterMigrationSub clienteManager);
}
