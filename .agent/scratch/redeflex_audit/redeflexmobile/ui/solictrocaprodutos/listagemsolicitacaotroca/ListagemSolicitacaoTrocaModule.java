package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import android.content.Context;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ListagemSolicitacaoTrocaModule {

    @Provides
    ListagemSolicitacaoTrocaPresenter providePresenter(ListagemSolicitacaoTrocaActivity view,
                                                       SchedulerProvider schedulerProvider,
                                                       ExceptionUtils exceptionUtils,
                                                       ListagemSolicitacaoTrocaManager listagemSolicitacaoTrocaManager) {
        return new ListagemSolicitacaoTrocaPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                listagemSolicitacaoTrocaManager
        );
    }

    @Provides
    ListagemSolicitacaoTrocaAdapter provideAdapter(ListagemSolicitacaoTrocaActivity view, Context context) {
        return new ListagemSolicitacaoTrocaAdapter(context, view);
    }
}
