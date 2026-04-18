package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import android.content.Context;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rogério Massa on 01/10/2018.
 */

@Module
public class SelecionarProdutosModule {

    @Provides
    SelecionarProdutosPresenter providePresenter(SelecionarProdutosActivity view,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 SelecionarProdutosManager selecionarProdutosManager) {
        return new SelecionarProdutosPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                selecionarProdutosManager
        );
    }

    @Provides
    SelecionarProdutosAdapter provideAdapter(Context context, SelecionarProdutosActivity view) {
        return new SelecionarProdutosAdapter(context, view);
    }

}
