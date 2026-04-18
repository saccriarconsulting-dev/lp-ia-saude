package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.login.LoginRequest;
import com.axys.redeflexmobile.shared.models.login.LoginResponse;

import io.reactivex.Single;

public interface LoginManager {

    Single<LoginResponse> login(LoginRequest loginRequest);

    Single<LoginResponse> loginOffline(String user, String password);

    Single<Colaborador> fetchUser(String imei);

    Single<Colaborador> fetchUser();

    Colaborador fetchLocalUser();

    void deleteDatabase();
}
