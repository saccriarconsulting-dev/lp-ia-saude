package com.axys.redeflexmobile.ui.register.customer.dadosec;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterCustomerDadosECFragmentModule {
    @Provides
    RegisterCustomerDadosECPresenter providePresenter(RegisterCustomerDadosECFragment view,
                                                       SchedulerProvider schedulerProvider,
                                                       ExceptionUtils exceptionUtils,
                                                       CustomerRegisterManager manager) {
        return new RegisterCustomerDadosECPresenterImpl(view, schedulerProvider, exceptionUtils,
                manager);

    }

    @Provides
    RegisterCustomerDadosECHorarioFuncAdapter provideHorarioFuncAdapter(RegisterCustomerDadosECFragment view) {
        return new RegisterCustomerDadosECHorarioFuncAdapter(view);
    }
}
