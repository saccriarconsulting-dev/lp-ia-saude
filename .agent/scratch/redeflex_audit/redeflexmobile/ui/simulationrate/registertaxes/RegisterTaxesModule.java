package com.axys.redeflexmobile.ui.simulationrate.registertaxes;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.manager.registerrate.RegistrationSimulationFeeManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lucas Marciano on 28/04/2020
 */
@Module
public class RegisterTaxesModule {

    @Provides
    RegisterTaxesPresenter providerPresenter(RegisterTaxesActivity view,
                                             SchedulerProvider schedulerProvider,
                                             ExceptionUtils exceptionUtils,
                                             ProspectingClientAcquisitionManager prospectManager,
                                             RegistrationSimulationFeeManager feeManager,
                                             CustomerRegisterManager customerRegisterManager,
                                             FlagsBankManager flagsBankManager
    ) {
        return new RegisterTaxesPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                prospectManager,
                feeManager,
                customerRegisterManager,
                flagsBankManager
        );
    }
}
