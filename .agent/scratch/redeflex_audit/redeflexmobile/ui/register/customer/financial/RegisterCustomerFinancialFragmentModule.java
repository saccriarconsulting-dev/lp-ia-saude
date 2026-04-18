package com.axys.redeflexmobile.ui.register.customer.financial;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Bruno Pimentel on 22/11/18.
 */

@Module
public class RegisterCustomerFinancialFragmentModule {

    @Provides
    RegisterCustomerFinancialPresenter providePresenter(RegisterCustomerFinancialFragment view,
                                                        SchedulerProvider schedulerProvider,
                                                        ExceptionUtils exceptionUtils,
                                                        CustomerRegisterManager manager) {
        return new RegisterCustomerFinancialPresenterImpl(view, schedulerProvider, exceptionUtils,
                manager);

    }
}
