package com.axys.redeflexmobile.ui.clientemigracao.register.proposal;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.PeriodNegotiationManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author lucasmarciano on 06/04/20
 */

@Module
public class RegisterMigrationProposalModule {

    @Provides
    RegisterMigrationProposalPresenter providePresenter(RegisterMigrationProposalFragment view,
                                                        SchedulerProvider schedulerProvider,
                                                        ExceptionUtils exceptionUtils,
                                                        PeriodNegotiationManager periodNegotiationManager) {
        return new RegisterMigrationProposalPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                periodNegotiationManager);
    }
}
