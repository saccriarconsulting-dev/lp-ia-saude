package com.axys.redeflexmobile.ui.adquirencia.visit.catalog;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class VisitProspectCatalogFragmentModule {

    @Provides
    VisitProspectCatalogPresenter providePresenter(VisitProspectCatalogFragment view,
                                                   SchedulerProvider schedulerProvider,
                                                   ExceptionUtils exceptionUtils,
                                                   VisitProspectManager visitProspectManager) {
        return new VisitProspectCatalogPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                visitProspectManager);
    }
}
