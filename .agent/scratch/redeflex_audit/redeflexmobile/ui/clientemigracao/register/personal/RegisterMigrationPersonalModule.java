package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lucas Marciano on 02/04/2020.
 */

@Module
public class RegisterMigrationPersonalModule {

    @Provides
    IGPSTracker provideGpsTracker(RegisterMigrationPersonalFragment fragment) {
        return new GPSTracker(fragment.getContext());
    }

    @Provides
    RegisterMigrationPersonalPresenter providePresenter(RegisterMigrationPersonalFragment view,
                                                        SchedulerProvider schedulerProvider,
                                                        ExceptionUtils exceptionUtils,
                                                        CustomerRegisterManager customerRegisterManager,
                                                        ClienteManager clienteManager,
                                                        DBColaborador dbColaborador,
                                                        DBTipoConta dbTipoConta,
                                                        MotiveMigrationSubManager motiveMigrationSubManager,
                                                        IGPSTracker gpsTracker,
                                                        RegisterMigrationSubManager registerMigrationSubManager) {
        return new RegisterMigrationPersonalPresenterImpl(
                view,
                schedulerProvider,
                exceptionUtils,
                customerRegisterManager,
                clienteManager,
                dbColaborador,
                dbTipoConta,
                motiveMigrationSubManager,
                gpsTracker,
                registerMigrationSubManager);
    }

    @Provides
    RegisterMigrationPersonalHorarioFuncAdapter provideHorarioFuncAdapter(RegisterMigrationPersonalFragment view) {
        return new RegisterMigrationPersonalHorarioFuncAdapter(view);
    }
}
