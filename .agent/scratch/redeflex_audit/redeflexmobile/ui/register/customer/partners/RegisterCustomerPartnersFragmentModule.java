package com.axys.redeflexmobile.ui.register.customer.partners;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterCustomerPartnersFragmentModule {
    @Provides
    RegisterCustomerPartnersPresenter providePresenter(RegisterCustomerPartnersFragment view,
                                                        SchedulerProvider schedulerProvider,
                                                        ExceptionUtils exceptionUtils,
                                                        CustomerRegisterManager manager) {
        return new RegisterCustomerPartnersPresenterImpl(view, schedulerProvider, exceptionUtils,
                manager);

    }
}
