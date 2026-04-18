package com.axys.redeflexmobile.ui.register.customer.commercial;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Bruno Pimentel on 22/11/18.
 */

@Module
public class RegisterCustomerCommercialFragmentModule {

    @Provides
    RegisterCustomerCommercialPresenter providePresenter(RegisterCustomerCommercialFragment view,
                                                         SchedulerProvider schedulerProvider,
                                                         ExceptionUtils exceptionUtils,
                                                         CustomerRegisterManager customerRegisterManager) {
        return new RegisterCustomerCommercialPresenterImpl(view,
                schedulerProvider,
                exceptionUtils,
                customerRegisterManager);
    }

    @Provides
    RegisterCustomerCommercialPosAdapter providePosAdapter(RegisterCustomerCommercialFragment view) {
        return new RegisterCustomerCommercialPosAdapter(view);
    }
}
