package com.axys.redeflexmobile.ui.login;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface LoginPresenter extends BasePresenter<LoginView> {

    void access(String user, String password);

    void loadData();

    void shouldValidateAppVersion();

    void checkIfNeedEnableService();

    void registerCrashlytics();
}
