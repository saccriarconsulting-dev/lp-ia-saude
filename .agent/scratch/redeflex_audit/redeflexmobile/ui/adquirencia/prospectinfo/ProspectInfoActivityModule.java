package com.axys.redeflexmobile.ui.adquirencia.prospectinfo;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ProspectInfoManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */

@Module
public class ProspectInfoActivityModule {

    @Provides
    ProspectInfoPresenter providePresenter(ProspectInfoActivity view,
                                           SchedulerProvider schedulerProvider,
                                           ExceptionUtils exceptionUtils,
                                           ProspectInfoManager prospectInfoManager) {
        return new ProspectInfoPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                prospectInfoManager);
    }
}
