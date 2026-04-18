package com.axys.redeflexmobile.ui.adquirencia.routes;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.RoutesProspectManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class RoutesProspectActivityModule {

    @Provides
    RoutesProspectPresenter providePresenter(RoutesProspectActivity view,
                                             SchedulerProvider schedulerProvider,
                                             ExceptionUtils exceptionUtils,
                                             RoutesProspectManager routesProspectManager) {
        return new RoutesProspectPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                routesProspectManager);
    }
}
