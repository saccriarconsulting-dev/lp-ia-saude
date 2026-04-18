package com.axys.redeflexmobile.ui.login;

import androidx.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.LoginManager;
import com.axys.redeflexmobile.shared.manager.ProvideString;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.login.Login;
import com.axys.redeflexmobile.shared.models.login.LoginRequest;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.CheckForInternetProvider;
import com.axys.redeflexmobile.shared.util.DeviceUniqueProvider;
import com.axys.redeflexmobile.shared.util.SharedPreferenceEncrypted;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.shared.util.exception.MessageException;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class LoginPresenterImpl extends BasePresenterImpl<LoginView> implements LoginPresenter {

    public static final String NO_UPDATE_APP = "-1";

    private static final int PARTIAL_SYNC = 1;
    private static final int FULL_SYNC = 0;
    private static final String USER_IN_QUEUE = "422";
    public static final String EMAIL_DOMAIN_REDEFLEX = "@redeflex.com.br";

    private final LoginManager loginManager;
    private final SharedPreferenceEncrypted sharedPreferenceEncrypted;
    private final ProvideString provideString;
    private final CheckForInternetProvider checkForInternetProvider;
    private final DeviceUniqueProvider deviceUniqueProvider;

    private String uniqueID;
    private int kindSync = PARTIAL_SYNC;

    public LoginPresenterImpl(LoginView view, SchedulerProvider schedulerProvider,
                              ExceptionUtils exceptionUtils, LoginManager loginManager,
                              SharedPreferenceEncrypted sharedPreferenceEncrypted,
                              ProvideString provideString,
                              CheckForInternetProvider checkForInternetProvider,
                              DeviceUniqueProvider deviceUniqueProvider) {
        super(view, schedulerProvider, exceptionUtils);
        this.loginManager = loginManager;
        this.sharedPreferenceEncrypted = sharedPreferenceEncrypted;
        this.provideString = provideString;
        this.checkForInternetProvider = checkForInternetProvider;
        this.deviceUniqueProvider = deviceUniqueProvider;
    }

    @Override
    public void access(String user, String password) {
        if (StringUtils.isEmpty(user)) {
            showError(provideString.getString(R.string.login_alert_user_invalid));
            return;
        }

        if (StringUtils.isEmpty(password)) {
            showError(provideString.getString(R.string.login_alert_password_invalid));
            return;
        }

        String modifiedUser = shouldCompleteEmail(user).toLowerCase();
        String credential = parseCredential(modifiedUser, password);
        String uniqueId = getUniqueId();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCredential(credential);
        loginRequest.setUuid(uniqueId);

        Disposable disposable = checkForInternetProvider.checkForInternet()
                .flatMap(hasInternet -> loginManager.fetchUser(uniqueId)
                        .flatMap(colaborador -> Single.just(hasInternet))
                        .onErrorResumeNext(throwable -> Single.just(hasInternet)))
                .flatMapCompletable(hasInternet ->
                        flatMapLogin(hasInternet, loginRequest, uniqueId, modifiedUser, password)
                )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(dispose -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(
                        () -> getView().openMainActivity(kindSync),
                        error -> handlerErrorLogin(error, uniqueId)
                );

        addDisposable(disposable);
    }

    @Override
    public void loadData() {
        addDisposable(
                sharedPreferenceEncrypted.getLogin()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .doOnSubscribe(dispose -> getView().showLoading())
                        .doFinally(() -> getView().hideLoading())
                        .subscribe(login -> {
                            getView().fillUser(login.getUsername());
                            uniqueID = login.getUuid();
                        }, Timber::e)
        );
    }

    @Override
    public void shouldValidateAppVersion() {
        addDisposable(
                loginManager.fetchUser()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(this::shouldUpdateApp, Timber::e)
        );
    }

    @Override
    public void checkIfNeedEnableService() {
        addDisposable(
                loginManager.fetchUser()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(this::shouldEnableService, Timber::e)
        );
    }

    @Override
    public void registerCrashlytics() {
        addDisposable(
                loginManager.fetchUser()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(user -> getView().registerCrashlytics(user), Timber::e)
        );
    }

    @NonNull
    private String parseCredential(final String user, final String password) {
        final String baseUser = Base64.encodeToString(user.getBytes(), Base64.NO_WRAP);
        final String basePassword = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);

        return Base64.encodeToString((baseUser + ":" + basePassword).getBytes(), Base64.NO_WRAP);
    }

    @NonNull
    private String getUniqueId() {
        if (StringUtils.isNotEmpty(uniqueID)) return uniqueID;

        return deviceUniqueProvider.getUniqueId();
    }

    private void saveUnique(String uniqueId) {
        addDisposable(
                sharedPreferenceEncrypted.getLogin()
                        .flatMapCompletable(login -> sharedPreferenceEncrypted.saveLogin(
                                new Login(uniqueId, login.getUsername(), login.getPassword())
                        ))
                        .onErrorResumeNext(throwable -> sharedPreferenceEncrypted.saveLogin(
                                new Login(uniqueId, null, null)
                        ))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> {
                        }, Timber::e)
        );
    }

    private void handlerErrorLogin(Throwable throwable, String uniqueId) {
        MessageException exception = exceptionUtils.getExceptionsMessage(throwable);
        String message = StringUtils.isNotEmpty(exception.getMessage())
                ? exception.getMessage()
                : provideString.getString(exception.getMessageResId());

        getView().showError(message);

        if (exception.getCode().equals(USER_IN_QUEUE)) {
            saveUnique(uniqueId);
            uniqueID = uniqueId;
        }
    }

    private void clearData() {
        loginManager.deleteDatabase();
    }

    private Completable flatMapLogin(boolean hasInternet, LoginRequest loginRequest,
                                     String uniqueId, String user, String password) {
        return Completable.defer(() -> {
            if (!hasInternet)
                return Completable.fromSingle(loginManager.loginOffline(user.toLowerCase(), password));

            return Completable.fromSingle(
                    loginManager.login(loginRequest)
                            .flatMapCompletable(loginResponse -> {
                                Login login = sharedPreferenceEncrypted.getSimpleLogin();

                                Colaborador savedUser = loginManager.fetchLocalUser();
                                String emailLocal = null;
                                if (savedUser != null) emailLocal = savedUser.getEmail();

                                kindSync = user.equals(emailLocal) && user.equals(login.getUsername())
                                        ? PARTIAL_SYNC
                                        : FULL_SYNC;

                                if (!user.equals(emailLocal)) {
                                    clearData();
                                }
                                shouldClearData(login, emailLocal, user);

                                return sharedPreferenceEncrypted.saveLogin(
                                        new Login(uniqueId, user, password)
                                );
                            })
                            .andThen(loginManager.fetchUser(uniqueId))
            );
        });
    }

    private String shouldCompleteEmail(String username) {
        if (!StringUtils.isEmailNotValid(username)) return username;

        return username + EMAIL_DOMAIN_REDEFLEX;
    }

    private void shouldClearData(Login login, String emailLocal, String typedEmail) {
        if (login == null || StringUtils.isEmpty(login.getUsername())) return;

        if (!typedEmail.equals(login.getUsername()) || !typedEmail.equals(emailLocal)) {
            clearData();
        }
    }

    private void shouldUpdateApp(Colaborador colaborador) {
        if (colaborador.getVersaoApp() == null
                || colaborador.getVersaoApp().equals(NO_UPDATE_APP)
                || colaborador.getVersaoApp().equals(BuildConfig.VERSION_NAME)) {
            return;
        }

        getView().updateApp();
    }

    private void shouldEnableService(Colaborador colaborador) {
        if (colaborador != null && colaborador.isCartaoPonto()) {
            getView().enableService();
            return;
        }

        getView().disableService();
    }
}
