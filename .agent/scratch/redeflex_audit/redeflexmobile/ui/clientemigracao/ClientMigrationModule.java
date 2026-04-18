package com.axys.redeflexmobile.ui.clientemigracao;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lucas Marciano on 23/03/2020
 */
@Module
public class ClientMigrationModule {

    @Provides
    ClientMigrationPresenter providerPresenter(ClientMigrationActivity view,
                                               SchedulerProvider schedulerProvider,
                                               ExceptionUtils exceptionUtils,
                                               ClienteManager clienteManager,
                                               RegisterMigrationSubManager registerMigrationSubManager) {
        return new ClientMigrationPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                clienteManager,
                registerMigrationSubManager
        );
    }

    @Provides
    ClientMigrationAdapter provideAdapter(ClientMigrationActivity view) {
        return new ClientMigrationAdapter(view);
    }
}
