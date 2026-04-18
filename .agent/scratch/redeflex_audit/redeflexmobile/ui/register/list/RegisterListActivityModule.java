package com.axys.redeflexmobile.ui.register.list;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 29/10/18.
 */

@Module
public class RegisterListActivityModule {

    @Provides
    RegisterListPresenter providePresenter(RegisterListActivity view,
                                           SchedulerProvider schedulerProvider,
                                           ExceptionUtils exceptionUtils,
                                           CustomerRegisterManager registerManager) {
        return new RegisterListPresenterImpl(view, schedulerProvider, exceptionUtils, registerManager);
    }

    @Provides
    RegisterListAdapter providerAdapter(RegisterListActivity view) {
        return new RegisterListAdapter(view);
    }
}
