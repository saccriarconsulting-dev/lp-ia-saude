package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import android.content.Context;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rogério Massa on 08/10/18.
 */

@Module
public class InformarCodigoBarraModule {

    @Provides
    InformarCodigoBarraPresenter providePresenter(InformarCodigoBarraActivity view,
                                                  SchedulerProvider schedulerProvider,
                                                  ExceptionUtils exceptionUtils,
                                                  InformarCodigoBarraManager informarCodigoBarraManager) {
        return new InformarCodigoBarraPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                informarCodigoBarraManager
        );
    }

    @Provides
    InformarCodigoBarraAdapter provideAdapter(Context context) {
        return new InformarCodigoBarraAdapter(context);
    }

}
