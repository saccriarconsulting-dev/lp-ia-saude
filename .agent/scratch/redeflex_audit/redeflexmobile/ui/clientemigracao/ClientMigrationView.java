package com.axys.redeflexmobile.ui.clientemigracao;

import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * @author Lucas Marciano on 23/03/2020
 */
public interface ClientMigrationView extends BaseView {

    /**
     * Fill the adapter with the list of clients.
     *
     * @param list [List<Cliente>]
     */
    void fillAdapter(final List<ClientMigrationResponse> list);

    /**
     * Show message when token was valid
     */
    void showTokenValidationSuccess();

    /**
     * Show message when token wasn't valid
     */
    void showTokenValidationFailure(ClientMigrationResponse clientMigrationResponse);

    /**
     * Show a loading screen when data are load.
     */
    void showMainLoading();

    /**
     * Hide a loading screen when data are load
     */
    void hideMainLoading();
}
