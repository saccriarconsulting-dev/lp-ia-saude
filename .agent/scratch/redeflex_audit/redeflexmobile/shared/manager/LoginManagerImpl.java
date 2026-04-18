package com.axys.redeflexmobile.shared.manager;

import android.content.Context;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.login.Login;
import com.axys.redeflexmobile.shared.models.login.LoginRequest;
import com.axys.redeflexmobile.shared.models.login.LoginResponse;
import com.axys.redeflexmobile.shared.services.LoginService;
import com.axys.redeflexmobile.shared.util.SharedPreferenceEncrypted;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;

import io.reactivex.Single;

public class LoginManagerImpl implements LoginManager {

    private final LoginService loginService;
    private final Context context;
    private final SharedPreferenceEncrypted sharedPreferenceEncrypted;
    private final DBColaborador dbColaborador;

    public LoginManagerImpl(LoginService loginService, Context context,
                            SharedPreferenceEncrypted sharedPreferenceEncrypted,
                            DBColaborador dbColaborador) {
        this.loginService = loginService;
        this.context = context;
        this.sharedPreferenceEncrypted = sharedPreferenceEncrypted;
        this.dbColaborador = dbColaborador;
    }


    @Override
    public Single<LoginResponse> login(LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @Override
    public Single<LoginResponse> loginOffline(String user, String password) {
        return Single.defer(() -> {
            Login login = sharedPreferenceEncrypted.getSimpleLogin();

            if (StringUtils.isEmpty(login.getUsername()) || StringUtils.isEmpty(login.getPassword()))
                return Single.error(new IllegalAccessException("Não existe usuário salvo no seu dispositivo, tente fazer o login online."));

            if (!login.getUsername().equals(user) || !login.getPassword().equals(password))
                return Single.error(new IllegalAccessException("Usuário e/ou senha inválidos."));

            return Single.just(new LoginResponse());
        });
    }

    @Override
    public Single<Colaborador> fetchUser(String imei) {
        return loginService.fetchUser(imei)
                .flatMap(user -> {
                    dbColaborador.addOrUpdate(user);
                    return Single.just(user);
                });
    }

    @Override
    public Single<Colaborador> fetchUser() {
        return Single.defer(() -> {
            Colaborador colaborador = dbColaborador.get();
            if (colaborador == null)
                return Single.error(new IllegalStateException("Colaborador não existe"));

            return Single.just(colaborador);
        });
    }

    @Nullable
    @Override
    public Colaborador fetchLocalUser() {
        return dbColaborador.get();
    }

    @Override
    public void deleteDatabase() {
        Utilidades.deletarTudo(context);
    }
}
