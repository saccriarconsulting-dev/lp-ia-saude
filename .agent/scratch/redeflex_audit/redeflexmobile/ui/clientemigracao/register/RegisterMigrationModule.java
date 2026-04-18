package com.axys.redeflexmobile.ui.clientemigracao.register;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubTaxManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author lucasmarciano on 02/04/20
 */
@Module
public class RegisterMigrationModule {

    @Provides
    RegisterMigrationPresenter providePresenter(RegisterMigrationActivity view,
                                                SchedulerProvider schedulerProvider,
                                                ExceptionUtils exceptionUtils,
                                                RegisterMigrationSubManager registerMigrationSubManager,
                                                RegisterMigrationSubTaxManager registerMigrationSubTaxManager
    ) {
        return new RegisterMigrationPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                registerMigrationSubManager,
                registerMigrationSubTaxManager
        );
    }
}
