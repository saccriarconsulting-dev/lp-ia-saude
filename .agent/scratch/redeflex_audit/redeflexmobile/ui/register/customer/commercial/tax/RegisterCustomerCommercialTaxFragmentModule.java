package com.axys.redeflexmobile.ui.register.customer.commercial.tax;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 15/02/19.
 */

@Module
public class RegisterCustomerCommercialTaxFragmentModule {

    @Provides
    RegisterCustomerCommercialTaxPresenter providePresenter(RegisterCustomerCommercialTaxFragment view,
                                                            SchedulerProvider schedulerProvider,
                                                            ExceptionUtils exceptionUtils,
                                                            CustomerRegisterManager manager,
                                                            FlagsBankManager flagsBankManager) {
        return new RegisterCustomerCommercialTaxPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                manager,
                flagsBankManager);
    }
}
