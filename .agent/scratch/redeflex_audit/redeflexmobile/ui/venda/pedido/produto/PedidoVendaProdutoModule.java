package com.axys.redeflexmobile.ui.venda.pedido.produto;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VendaManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class PedidoVendaProdutoModule {

    @Provides
    PedidoVendaProdutoPresenter providePresenter(PedidoVendaProdutoDialog dialog,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 VendaManager vendaManager) {
        return new PedidoVendaProdutoPresenterImpl(
                dialog,
                schedulerProvider,
                exceptionUtils,
                vendaManager
        );
    }
}
