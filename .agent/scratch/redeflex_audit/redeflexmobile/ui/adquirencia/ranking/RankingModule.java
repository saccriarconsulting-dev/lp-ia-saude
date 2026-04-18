package com.axys.redeflexmobile.ui.adquirencia.ranking;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.RankingManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class RankingModule {

    @Provides
    RankingPresenter providePresenter(RankingActivity view,
                                      SchedulerProvider schedulerProvider,
                                      ExceptionUtils exceptionUtils,
                                      RankingManager rankingManager) {
        return new RankingPresenterImpl(view, schedulerProvider, exceptionUtils, rankingManager);
    }
}
