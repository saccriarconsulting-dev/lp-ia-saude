package com.axys.redeflexmobile.ui.register.list;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import timber.log.Timber;

/**
 * @author Rogério Massa on 21/11/18.
 */

public class RegisterListPresenterImpl extends BasePresenterImpl<RegisterListView> implements
        RegisterListPresenter {

    private final CustomerRegisterManager registerManager;

    public RegisterListPresenterImpl(final RegisterListView view,
                                     final SchedulerProvider schedulerProvider,
                                     final ExceptionUtils exceptionUtils,
                                     final CustomerRegisterManager registerManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.registerManager = registerManager;
    }

    @Override
    public void getRegisters(String filter) {
        compositeDisposable.add(registerManager.getRegisters(filter)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoadingDelayed())
                .subscribe(registers -> getView().fillRegisterList(registers), this::showError));
    }

    @Override
    public void validateToOpenRelease(RegisterListItem register) {
        IGPSTracker gpsTracker = getView().getGpsTracker();
        if (!gpsTracker.isGPSEnabled()) {
            gpsTracker.showSettingsAlert();
            return;
        }

        if (gpsTracker.isMockLocationON() || gpsTracker.areThereMockPermissionApps()) {
            gpsTracker.showMockAlert();
            return;
        }

        if (!validateDistance(register)) {
            getView().validacaoNaoEstaNasImediacoes();
            return;
        }

        getView().openRelease(register);
    }

    @Override
    public void validateToken(final RegisterListItem item, final String token) {
        if (item.getToken().equals(token)) {
            compositeDisposable.add(registerManager.saveTokenConfirmation(item.getId())
                    .subscribe(getView()::showTokenValidationSuccess, Timber::e));
            return;
        }

        getView().showTokenValidationFailure(item);
    }

    private boolean validateDistance(RegisterListItem register) {
        double latitude = register.getLatitude();
        double longitude = register.getLongitude();

        if (latitude == EMPTY_INT || longitude == EMPTY_INT) {
            return true;
        }

        IGPSTracker gpsTracker = getView().getGpsTracker();
        float distance = gpsTracker.getDistanceToMyLocation(latitude, longitude);
        distance = gpsTracker.getPrecisao() == EMPTY_INT ? EMPTY_INT : Math.round(distance);

        Colaborador salesman = registerManager.getSalesmanDirect();
        return salesman.getDistancia() <= EMPTY_INT
                || !(distance > (salesman.getDistancia() + gpsTracker.getPrecisao()));
    }
}
