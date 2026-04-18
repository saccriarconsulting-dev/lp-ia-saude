package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import android.content.Context;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

@Module
public class NovaSolicitacaoModule {

    @Provides
    NovaSolicitacaoPresenter providePresenter(NovaSolicitacaoActivity view,
                                              SchedulerProvider schedulerProvider,
                                              ExceptionUtils exceptionUtils,
                                              NovaSolicitacaoManager novaSolicitacaoManager) {
        return new NovaSolicitacaoPresenterImpl(view, schedulerProvider, exceptionUtils, novaSolicitacaoManager);
    }

    @Provides
    NovaSolicitacaoAdapter provideAdapter(Context context, NovaSolicitacaoActivity view) {
        return new NovaSolicitacaoAdapter(context, view);
    }
}
