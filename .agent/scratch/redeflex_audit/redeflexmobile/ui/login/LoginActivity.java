package com.axys.redeflexmobile.ui.login;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.BuildConfig;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.CartaoPontoService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;
import com.axys.redeflexmobile.ui.redeflex.MainActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class LoginActivity extends BaseActivity implements LoginView {

    private static final String KIND_OPERATION = "mTipoOperacao";
    private static final String FROM_LOGIN = "Login";
    private static final int DELAY_TO_NEXT_ACTION = 1200;

    private final CompositeDisposable disposables = new CompositeDisposable();
    @Inject LoginPresenter presenter;
    @BindView(R.id.login_et_password) AppCompatEditText etPassword;
    @BindView(R.id.login_et_user) TextInputEditText etUser;
    @BindView(R.id.login_bt_access) AppCompatButton btAccess;
    @BindView(R.id.login_cpl_loading) ComponentProgressLoading loading;
    @BindView(R.id.login_tvEsqueciSenha) TextView tvEsqueciSenha;
    private Intent serviceCartaoPonto;

    @Override
    protected int getContentView() {
        return R.layout.login_layout;
    }

    @Override
    protected void initialize() {
        createService();
        prepareScreen();
        startPresenter();
        startEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
        presenter.detach();
    }

    @Override
    public void showError(String message) {
        showOneButtonDialog(
                getString(R.string.app_titulo),
                message,
                null
        );
    }

    @Override
    public void fillUser(String user) {
        etUser.setText(user);
    }

    @Override
    public void openMainActivity(int kindSync) {
        presenter.checkIfNeedEnableService();

        Bundle bundle = new Bundle();
        bundle.putInt(FROM_LOGIN, 1);
        bundle.putInt(KIND_OPERATION, kindSync);
        Utilidades.openNewActivity(LoginActivity.this, MainActivity.class, bundle, true);
    }

    @Override
    public void updateApp() {
        Alerta alerta = new Alerta(LoginActivity.this,
                getString(R.string.app_name),
                getString(R.string.message_update_app));

        alerta.show((dialog, which) -> {
            try {
                Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
            } catch (ActivityNotFoundException e) {
                Timber.e(e);
                Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
            }
            finish();
        });
    }

    @Override
    public void enableService() {
        startService(serviceCartaoPonto);
    }

    @Override
    public void disableService() {
        stopService(serviceCartaoPonto);
    }

    @Override
    public void registerCrashlytics(Colaborador user) {
       /* String data = String.format(
                "%s - %s",
                user.getId(),
                user.getNome()
        );
        Crashlytics.setUserIdentifier(data);*/
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return loading;
    }

    private void startPresenter() {
        presenter.shouldValidateAppVersion();
        presenter.loadData();
        presenter.registerCrashlytics();
    }

    private void startEvents() {
        Disposable disposableButton = RxView.clicks(btAccess)
                .throttleFirst(DELAY_TO_NEXT_ACTION, TimeUnit.MILLISECONDS)
                .subscribe(view -> {
                    final String user = getText(etUser);
                    final String password = getText(etPassword);

                    presenter.access(user, password);
                }, Timber::e);
        disposables.add(disposableButton);

        tvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = BuildConfig.REST_SERVICE_INTRAFLEX + "Login/EsqueciSenha";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private String getText(AppCompatEditText editText) {
        if (editText.getText() != null) return editText.getText().toString();

        return StringUtils.EMPTY_STRING;
    }

    private void createService() {
        serviceCartaoPonto = new Intent(LoginActivity.this, CartaoPontoService.class);
    }

    private void prepareScreen() {
        getWindow().setBackgroundDrawableResource(R.drawable.ic_back_login1);
        tvEsqueciSenha.setPaintFlags(tvEsqueciSenha.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
