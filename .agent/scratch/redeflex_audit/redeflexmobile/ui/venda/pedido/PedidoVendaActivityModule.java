package com.axys.redeflexmobile.ui.venda.pedido;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VendaManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class PedidoVendaActivityModule {

    @Provides
    public PedidoVendaPresenter providePresenter(PedidoVendaActivity activity,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 VendaManager vendaManager) {
        return new PedidoVendaPresenterImpl(
                activity,
                schedulerProvider,
                exceptionUtils,
                vendaManager
        );
    }

    @Provides
    public PedidoVendaAdapter provideAdapter() {
        return new PedidoVendaAdapter();
    }
}
