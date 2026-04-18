package com.axys.redeflexmobile.ui.clientpendent;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.PendenciaClienteManager;
import com.axys.redeflexmobile.shared.manager.PendenciaManager;
import com.axys.redeflexmobile.shared.manager.PendenciaMotivoManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lucas Marciano on 28/02/2020
 */
@Module
public class ClientePendenteModule {

    @Provides
    public ClientePendenteAdapter providerAdapter() {
        return new ClientePendenteAdapter();
    }

    @Provides
    ClientePendentePresenter providerPresenter(ClientePendenteActivity view,
                                               SchedulerProvider schedulerProvider,
                                               ExceptionUtils exceptionUtils,
                                               ClienteManager clienteManager,
                                               PendenciaManager pendenciaManager,
                                               PendenciaClienteManager pendenciaClienteManager,
                                               PendenciaMotivoManager pendenciaMotivoManager) {
        return new ClientePendentePresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                clienteManager,
                pendenciaManager,
                pendenciaClienteManager,
                pendenciaMotivoManager
        );
    }
}
