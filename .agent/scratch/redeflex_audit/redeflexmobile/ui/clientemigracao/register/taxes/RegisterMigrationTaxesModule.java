package com.axys.redeflexmobile.ui.clientemigracao.register.taxes;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubTaxManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lucas Marciano on 07/04/2020
 */

@Module
public class RegisterMigrationTaxesModule {

    @Provides
    IGPSTracker provideGpsTracker(RegisterMigrationTaxesFragment fragment) {
        return new GPSTracker(fragment.getContext());
    }

    @Provides
    RegisterMigrationTaxesPresenter providePresenter(RegisterMigrationTaxesFragment view,
                                                     SchedulerProvider schedulerProvider,
                                                     ExceptionUtils exceptionUtils,
                                                     RegisterMigrationSubTaxManager registerMigrationSubTaxManager,
                                                     ClienteManager clienteManager,
                                                     RegisterMigrationSubManager registerMigrationSubManager,
                                                     MotiveMigrationSubManager motiveMigrationSubManager,
                                                     IGPSTracker gpsTracker,
                                                     DBColaborador dbColaborador,
                                                     FlagsBankManager flagsBankManager) {
        return new RegisterMigrationTaxesPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                registerMigrationSubTaxManager,
                clienteManager,
                registerMigrationSubManager,
                motiveMigrationSubManager,
                gpsTracker,
                dbColaborador,
                flagsBankManager
        );
    }
}
