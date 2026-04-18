package com.axys.redeflexmobile.ui.clientevalidacao;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ClienteModule {

    @Provides
    ClientePresenter provideClientePresenter(ClienteActivity view,
                                             SchedulerProvider schedulerProvider,
                                             ExceptionUtils exceptionUtils,
                                             RegisterMigrationSubManager registerMigrationSubManager,
                                             MotiveMigrationSubManager motiveMigrationSubManager,
                                             DBColaborador dbColaborador) {
        return new ClientePresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                registerMigrationSubManager,
                motiveMigrationSubManager,
                dbColaborador
        );
    }
}
