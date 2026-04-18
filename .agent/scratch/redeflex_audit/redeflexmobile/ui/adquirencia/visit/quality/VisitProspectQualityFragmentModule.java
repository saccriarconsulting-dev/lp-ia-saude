package com.axys.redeflexmobile.ui.adquirencia.visit.quality;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class VisitProspectQualityFragmentModule {

    @Provides
    VisitProspectQualityPresenter providePresenter(VisitProspectQualityFragment view,
                                                   SchedulerProvider schedulerProvider,
                                                   ExceptionUtils exceptionUtils,
                                                   VisitProspectManager visitProspectManager) {
        return new VisitProspectQualityPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                visitProspectManager);
    }
}
