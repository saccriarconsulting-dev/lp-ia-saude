package com.axys.redeflexmobile.ui.clientevalidacao;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import timber.log.Timber;

public class ClientePresenterImpl extends BasePresenterImpl<ClienteView>
        implements ClientePresenter {

    private final RegisterMigrationSubManager registerMigrationSubManager;
    private final MotiveMigrationSubManager motiveMigrationSubManager;
    private final DBColaborador dbColaborador;

    public ClientePresenterImpl(ClienteView view,
                                SchedulerProvider schedulerProvider,
                                ExceptionUtils exceptionUtils,
                                RegisterMigrationSubManager registerMigrationSubManager,
                                MotiveMigrationSubManager motiveMigrationSubManager,
                                DBColaborador dbColaborador) {
        super(view, schedulerProvider, exceptionUtils);
        this.registerMigrationSubManager = registerMigrationSubManager;
        this.motiveMigrationSubManager = motiveMigrationSubManager;
        this.dbColaborador = dbColaborador;
    }

    @Override
    public void loadMotivesCancelMigration() {
        compositeDisposable.add(motiveMigrationSubManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(getView()::callModalMotivesCancelMigration, Timber::e));
    }

    @Override
    public void saveMigrationCancel(RegisterMigrationSub registerMigrationSub) {
        registerMigrationSub.setIdVendedor(dbColaborador.get().getId());
        compositeDisposable.add(registerMigrationSubManager.create(registerMigrationSub)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(response ->
                                getView().showResponseMotivesCancelMigration(),
                        Timber::e)
        );
    }

    @Override
    public void checkRegisterMigrationAreCanceled(String clientId, String tipoMigracao) {
        compositeDisposable.add(registerMigrationSubManager.obterCadastroMigracaoPorClienteId(
                Integer.parseInt(clientId),tipoMigracao)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(getView()::registerAreCanceled,
                        e -> {
                            getView().registerAreCanceled(null);
                            Timber.e(e);
                        })
        );
    }
}
