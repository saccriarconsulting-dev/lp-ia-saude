package com.axys.redeflexmobile.ui.clientemigracao;

import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Lucas Marciano on 23/03/2020
 */
public interface ClientMigrationPresenter extends BasePresenter<ClientMigrationView> {

    /**
     * Load the list of the clients that will migrate.
     */
    void loadClientsMigration(int filtro);

    /**
     * Validate token value, when situation is 99
     *
     * @param clientMigrationResponse [ClientMigrationResponse]
     */
    void validateToken(String token, ClientMigrationResponse clientMigrationResponse);
}
