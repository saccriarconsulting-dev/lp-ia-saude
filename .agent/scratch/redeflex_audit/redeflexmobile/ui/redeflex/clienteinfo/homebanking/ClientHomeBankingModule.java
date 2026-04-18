package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientHomeBankingManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author lucasmarciano on 01/07/20
 */
@Module
public
class ClientHomeBankingModule {

    @Provides
    ClientHomeBankingPresenter providerPresenter(ClientHomeBankingActivity view,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 ClientHomeBankingManager clientHomeBankingManager) {
        return new ClientHomeBankingPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                clientHomeBankingManager
        );
    }

    @Provides
    ClientHomeBankingAdapter provideAdapter() {
        return new ClientHomeBankingAdapter();
    }
}
