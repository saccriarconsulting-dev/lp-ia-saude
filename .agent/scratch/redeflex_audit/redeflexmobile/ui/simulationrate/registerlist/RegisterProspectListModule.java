package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lucas Marciano on 30/04/2020
 */
@Module
public class RegisterProspectListModule {

    @Provides
    RegisterProspectListPresenter providePresenter(RegisterProspectListActivity view,
                                                   SchedulerProvider schedulerProvider,
                                                   ExceptionUtils exceptionUtils,
                                                   ProspectingClientAcquisitionManager manager
    ) {

        return new RegisterProspectListPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                manager
        );
    }

    @Provides
    RegisterProspectListAdapter provideAdapter(RegisterProspectListActivity view) {
        return new RegisterProspectListAdapter(view);
    }
}
