package com.axys.redeflexmobile.ui.clientemigracao;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import timber.log.Timber;

/**
 * @author Lucas Marciano on 23/03/2020
 */
public class ClientMigrationPresenterImpl extends BasePresenterImpl<ClientMigrationView> implements
        ClientMigrationPresenter {

    private final ClienteManager clienteManager;
    private final RegisterMigrationSubManager registerMigrationSubManager;

    public ClientMigrationPresenterImpl(ClientMigrationView view,
                                        SchedulerProvider schedulerProvider,
                                        ExceptionUtils exceptionUtils,
                                        ClienteManager clienteManager,
                                        RegisterMigrationSubManager registerMigrationSubManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.clienteManager = clienteManager;
        this.registerMigrationSubManager = registerMigrationSubManager;
    }

    @Override
    public void loadClientsMigration(int filtro) {
        if (filtro == 0) {
            compositeDisposable.add(clienteManager.obterClientesMigracao()
                    .subscribeOn(schedulerProvider.io())
                    .flatMap(registerMigrationSubManager::mountListClientMigrationResponse)
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe(d -> getView().showMainLoading())
                    .doOnTerminate(getView()::hideMainLoading)
                    .subscribe(getView()::fillAdapter, Timber::e));
        }
        else
        {
            compositeDisposable.add(clienteManager.obterClientesPendentesMigracao()
                    .subscribeOn(schedulerProvider.io())
                    .flatMap(registerMigrationSubManager::mountListClientMigrationResponse)
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe(d -> getView().showMainLoading())
                    .doOnTerminate(getView()::hideMainLoading)
                    .subscribe(getView()::fillAdapter, Timber::e));
        }

    }

    @Override
    public void validateToken(String token, ClientMigrationResponse clientMigrationResponse) {
        int clientId = Integer.parseInt(clientMigrationResponse.getClient().getId());

        compositeDisposable.add(registerMigrationSubManager.validateToken(clientId, token)
                .subscribeOn(schedulerProvider.io())
                .flatMapSingle(tokenValid -> registerMigrationSubManager.updateTokenConfirmation(clientId))
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .doOnTerminate(getView()::hideMainLoading)
                .subscribe(
                        response -> getView().showTokenValidationSuccess(),
                        error -> getView().showTokenValidationFailure(clientMigrationResponse)
                ));
    }
}
