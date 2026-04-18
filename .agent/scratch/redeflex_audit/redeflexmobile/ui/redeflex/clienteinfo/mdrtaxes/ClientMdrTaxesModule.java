package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientTaxMdrManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author lucasmarciano on 30/06/20
 */
@Module
public class ClientMdrTaxesModule {

    @Provides
    ClientMdrTaxesPresenter providerPresenter(ClientMdrTaxesActivity view,
                                              SchedulerProvider schedulerProvider,
                                              ExceptionUtils exceptionUtils,
                                              ClientTaxMdrManager clientTaxMdrManager,
                                              FlagsBankManager flagsBankManager) {
        return new ClientMdrTaxesPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                clientTaxMdrManager,
                flagsBankManager
        );
    }
}
