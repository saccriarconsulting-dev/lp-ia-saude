package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import android.content.Context;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rogério Massa on 15/10/18.
 */

@Module
public class SelecionarProdutosBipagemModule {

    @Provides
    SelecionarProdutosBipagemPresenter providePresenter(SelecionarProdutosBipagemActivity view,
                                                        SchedulerProvider schedulerProvider,
                                                        ExceptionUtils exceptionUtils,
                                                        SelecionarProdutosBipagemManager manager,
                                                        SelecionarProdutosManager selecionarProdutosManager) {
        return new SelecionarProdutosBipagemPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                manager,
                selecionarProdutosManager
        );
    }

    @Provides
    SelecionarProdutosBipagemAdapter provideAdapter(Context context, SelecionarProdutosBipagemActivity view) {
        return new SelecionarProdutosBipagemAdapter(context, view);
    }

}
