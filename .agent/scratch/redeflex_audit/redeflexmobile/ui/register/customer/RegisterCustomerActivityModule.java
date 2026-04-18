package com.axys.redeflexmobile.ui.register.customer;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Bruno Pimentel on 20/11/18.
 */

@Module
public class RegisterCustomerActivityModule {

    @Provides
    RegisterCustomerPresenter providePresenter(RegisterCustomerActivity view,
                                               SchedulerProvider schedulerProvider,
                                               ExceptionUtils exceptionUtils,
                                               CustomerRegisterManager customerRegisterManager,
                                               ProspectingClientAcquisitionManager prospectingClientAcquisitionManager) {
        return new RegisterCustomerPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                customerRegisterManager,
                prospectingClientAcquisitionManager);
    }
}
