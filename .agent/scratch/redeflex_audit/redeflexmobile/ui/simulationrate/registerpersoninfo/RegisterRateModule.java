package com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.PeriodNegotiationManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterRateModule {

    @Provides
    RegisterRatePresenter providePresenter(RegisterRateActivity view,
                                           SchedulerProvider schedulerProvider,
                                           ExceptionUtils exceptionUtils,
                                           PeriodNegotiationManager periodNegotiationManager,
                                           CustomerRegisterManager customerRegisterManager,
                                           ProspectingClientAcquisitionManager prospectingManager) {

        return new RegisterRatePresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                periodNegotiationManager,
                customerRegisterManager,
                prospectingManager);
    }

}
