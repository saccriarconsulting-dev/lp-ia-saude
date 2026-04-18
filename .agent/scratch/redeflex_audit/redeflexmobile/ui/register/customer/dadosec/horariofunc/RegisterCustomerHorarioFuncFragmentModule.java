package com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterCustomerHorarioFuncFragmentModule {

    @Provides
    RegisterCustomerHorarioFuncPresenter providePresenter(RegisterCustomerHorarioFuncFragment view,
                                                      SchedulerProvider schedulerProvider,
                                                      ExceptionUtils exceptionUtils,
                                                      CustomerRegisterManager customerRegisterManager) {
        return new RegisterCustomerHorarioFuncPresenterImpl(view, schedulerProvider, exceptionUtils,
                customerRegisterManager);
    }
}
