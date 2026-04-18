package com.axys.redeflexmobile.ui.login;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

public interface LoginView extends BaseActivityView {

    void showError(String message);

    void fillUser(String user);

    void openMainActivity(int kindSync);

    void updateApp();

    void enableService();

    void disableService();

    void registerCrashlytics(Colaborador user);
}
