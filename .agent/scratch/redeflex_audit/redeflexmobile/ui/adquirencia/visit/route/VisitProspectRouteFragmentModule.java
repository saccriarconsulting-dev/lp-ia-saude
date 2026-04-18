package com.axys.redeflexmobile.ui.adquirencia.visit.route;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class VisitProspectRouteFragmentModule {

    @Provides
    VisitProspectRoutePresenter providePresenter(VisitProspectRouteFragment view,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 VisitProspectManager visitProspectManager,
                                                 GoogleMapsApiManager googleMapsApiManager) {
        return new VisitProspectRoutePresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                visitProspectManager,
                googleMapsApiManager);
    }
}
