package com.axys.redeflexmobile.ui.clientemigracao.register;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubTaxManager;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import timber.log.Timber;

/**
 * @author lucasmarciano on 02/04/20
 */
class RegisterMigrationPresenterImpl extends BasePresenterImpl<RegisterMigrationView>
        implements RegisterMigrationPresenter {

    private final RegisterMigrationSubManager registerMigrationSubManager;
    private final RegisterMigrationSubTaxManager registerMigrationSubTaxManager;

    RegisterMigrationPresenterImpl(RegisterMigrationView view,
                                   SchedulerProvider schedulerProvider,
                                   ExceptionUtils exceptionUtils,
                                   RegisterMigrationSubManager registerMigrationSubManager,
                                   RegisterMigrationSubTaxManager registerMigrationSubTaxManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.registerMigrationSubManager = registerMigrationSubManager;
        this.registerMigrationSubTaxManager = registerMigrationSubTaxManager;
    }

    @Override
    public void saveMigration(@NotNull RegisterMigrationSub registerMigrationSub) {
        registerMigrationSub.setSituacao(EnumRegisterStatus.SCHEDULED.getValue());
        registerMigrationSub.setDataCadastro(Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        compositeDisposable.add(registerMigrationSubManager.create(registerMigrationSub)
                .subscribeOn(schedulerProvider.io())
                .flatMap(registerMigrationSubManager::createToken)
                .flatMap(registerMigrationSubTaxManager::createAll)
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(response -> getView().onSaveSuccess(), Timber::e));
    }
}
