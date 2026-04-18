package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.PosManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ClienteInfoPosListModule {

    @Provides
    public ClienteInfoPosPresenter providePresenter(ClienteInfoPosList view,
                                                    SchedulerProvider schedulerProvider,
                                                    ExceptionUtils exceptionUtils,
                                                    PosManager posManager) {
        return new ClienteInfoPosPresenterImpl(view, schedulerProvider, exceptionUtils, posManager);
    }

    @Provides
    public ClienteInfoPosListAdapter providerAdapter(ClienteInfoPosList activity) {
        return new ClienteInfoPosListAdapter(activity);
    }

    @Provides
    public ClienteInfoPosListLocalizacao provideValidadorLocalizacao() {
        return new ClienteInfoPosListLocalizacaoImpl();
    }
}
