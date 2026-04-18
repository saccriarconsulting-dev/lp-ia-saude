package com.axys.redeflexmobile.ui.register.customer.personal;

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
public class RegisterCustomerPersonalFragmentModule {

    @Provides
    RegisterCustomerPersonalPresenter providePresenter(RegisterCustomerPersonalFragment view,
                                                       SchedulerProvider schedulerProvider,
                                                       ExceptionUtils exceptionUtils,
                                                       CustomerRegisterManager customerRegisterManager,
                                                       ProspectingClientAcquisitionManager prospectingClientAcquisitionManager) {
        return new RegisterCustomerPersonalPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                customerRegisterManager,
                prospectingClientAcquisitionManager);
    }
}
