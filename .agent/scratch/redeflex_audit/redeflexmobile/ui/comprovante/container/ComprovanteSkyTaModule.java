package com.axys.redeflexmobile.ui.comprovante.container;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ComprovanteSkyTaManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ComprovanteSkyTaModule {

    @Provides
    ComprovanteSkyTaPresenter providePresenter(ComprovanteSkyTaActivity activity,
                                               SchedulerProvider schedulerProvider,
                                               ExceptionUtils exceptionUtils,
                                               ComprovanteSkyTaManager manager) {
        return new ComprovanteSkyTaPresenterImpl(
                activity,
                schedulerProvider,
                exceptionUtils,
                manager
        );
    }
}
