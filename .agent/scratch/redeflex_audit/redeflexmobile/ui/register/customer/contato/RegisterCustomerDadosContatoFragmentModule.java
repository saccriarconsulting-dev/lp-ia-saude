package com.axys.redeflexmobile.ui.register.customer.contato;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterCustomerDadosContatoFragmentModule {
    @Provides
    RegisterCustomerDadosContatoPresenter providePresenter(RegisterCustomerDadosContatoFragment view,
                                                      SchedulerProvider schedulerProvider,
                                                      ExceptionUtils exceptionUtils,
                                                      CustomerRegisterManager manager) {
        return new RegisterCustomerDadosContatoPresenterImpl(view, schedulerProvider, exceptionUtils,
                manager);

    }

    @Provides
    RegisterCustomerDadosContatoAdapter provideDadosContatoAdapter(RegisterCustomerDadosContatoFragment view) {
        return new RegisterCustomerDadosContatoAdapter(view);
    }
}
