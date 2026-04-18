package com.axys.redeflexmobile.ui.register.customer.commercial.pos;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 18/02/19.
 */

@Module
public class RegisterCustomerCommercialPosFragmentModule {

    @Provides
    RegisterCustomerCommercialPosPresenter providePresenter(RegisterCustomerCommercialPosFragment view,
                                                            SchedulerProvider schedulerProvider,
                                                            ExceptionUtils exceptionUtils,
                                                            CustomerRegisterManager customerRegisterManager) {
        return new RegisterCustomerCommercialPosPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                customerRegisterManager);
    }
}
