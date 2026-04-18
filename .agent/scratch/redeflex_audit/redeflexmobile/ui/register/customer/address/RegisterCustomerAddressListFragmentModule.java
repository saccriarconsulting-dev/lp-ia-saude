package com.axys.redeflexmobile.ui.register.customer.address;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 07/02/19.
 */

@Module
public class RegisterCustomerAddressListFragmentModule {

    @Provides
    RegisterCustomerAddressListPresenter providePresenter(RegisterCustomerAddressListFragment view,
                                                          SchedulerProvider schedulerProvider,
                                                          ExceptionUtils exceptionUtils) {
        return new RegisterCustomerAddressListPresenterImpl(view, schedulerProvider, exceptionUtils);
    }

    @Provides
    RegisterCustomerAddressListAdapter provideAdapter(RegisterCustomerAddressListFragment view) {
        return new RegisterCustomerAddressListAdapter(view);
    }
}
