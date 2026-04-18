package com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterMigrationHorarioFuncModule {
    @Provides
    RegisterMigrationHorarioFuncPresenter providePresenter(RegisterMigrationHorarioFuncFragment view,
                                                           SchedulerProvider schedulerProvider,
                                                           ExceptionUtils exceptionUtils,
                                                           RegisterMigrationSubManager registerMigrationSubManager) {
        return new RegisterMigrationHorarioFuncPresenterImpl(view, schedulerProvider, exceptionUtils,
                registerMigrationSubManager);
    }
}
