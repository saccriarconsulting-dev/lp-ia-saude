package com.axys.redeflexmobile.ui.adquirencia.release;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ReleaseManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 04/12/18.
 */

@Module
public class ReleaseActivityModule {

    @Provides
    ReleasePresenter providePresenter(ReleaseActivity view,
                                      SchedulerProvider schedulerProvider,
                                      ExceptionUtils exceptionUtils,
                                      ReleaseManager releaseManager) {
        return new ReleasePresenterImpl(view, schedulerProvider, exceptionUtils, releaseManager);
    }

    @Provides
    ReleaseAdapter provideAdapter(ReleaseActivity view) {
        return new ReleaseAdapter(view);
    }
}

