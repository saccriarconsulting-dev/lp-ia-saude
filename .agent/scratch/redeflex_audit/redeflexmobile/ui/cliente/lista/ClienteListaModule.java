package com.axys.redeflexmobile.ui.cliente.lista;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ClienteListaModule {

    @Provides
    public ClienteListaAdapter providePresenter() {
        return new ClienteListaAdapter();
    }

    @Provides
    ClienteListaPresenter providerPresenter(ClienteListaActivity view,
                                                   SchedulerProvider schedulerProvider,
                                                   ExceptionUtils exceptionUtils,
                                                   ClienteManager clienteManager) {
        return new ClienteListaPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                clienteManager
        );
    }
}
