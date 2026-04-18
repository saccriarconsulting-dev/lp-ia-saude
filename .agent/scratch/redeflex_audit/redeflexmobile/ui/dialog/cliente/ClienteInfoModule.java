package com.axys.redeflexmobile.ui.dialog.cliente;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ClienteInfoModule {

    @Provides
    ClienteInfoPresenter provideClienteInfoPresenter(ClienteInfoDialog view,
                                                     SchedulerProvider schedulerProvider,
                                                     ExceptionUtils exceptionUtils,
                                                     ClienteManager clienteManager) {
        return new ClienteInfoPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                clienteManager
        );
    }
}
