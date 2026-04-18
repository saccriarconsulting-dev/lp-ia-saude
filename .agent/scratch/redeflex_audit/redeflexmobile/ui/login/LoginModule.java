package com.axys.redeflexmobile.ui.login;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.LoginManager;
import com.axys.redeflexmobile.shared.manager.ProvideString;
import com.axys.redeflexmobile.shared.util.CheckForInternetProvider;
import com.axys.redeflexmobile.shared.util.DeviceUniqueProvider;
import com.axys.redeflexmobile.shared.util.SharedPreferenceEncrypted;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    LoginPresenter providePresenter(LoginActivity activity, SchedulerProvider schedulerProvider,
                                    ExceptionUtils exceptionUtils, LoginManager loginManager,
                                    SharedPreferenceEncrypted sharedPreferenceEncrypted,
                                    ProvideString provideString,
                                    CheckForInternetProvider checkForInternetProvider,
                                    DeviceUniqueProvider deviceUniqueProvider) {
        return new LoginPresenterImpl(
                activity,
                schedulerProvider,
                exceptionUtils,
                loginManager,
                sharedPreferenceEncrypted,
                provideString,
                checkForInternetProvider,
                deviceUniqueProvider
        );
    }
}
