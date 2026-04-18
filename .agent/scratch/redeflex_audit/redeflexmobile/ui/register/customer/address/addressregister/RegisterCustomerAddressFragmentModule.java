package com.axys.redeflexmobile.ui.register.customer.address.addressregister;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Bruno Pimentel on 21/11/18.
 */

@Module
public class RegisterCustomerAddressFragmentModule {

    @Provides
    RegisterCustomerAddressPresenter providePresenter(RegisterCustomerAddressFragment view,
                                                      SchedulerProvider schedulerProvider,
                                                      ExceptionUtils exceptionUtils,
                                                      CustomerRegisterManager customerRegisterManager) {
        return new RegisterCustomerAddressPresenterImpl(view, schedulerProvider, exceptionUtils,
                customerRegisterManager);
    }
}
