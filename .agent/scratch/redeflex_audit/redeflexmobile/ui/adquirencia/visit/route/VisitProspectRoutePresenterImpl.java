package com.axys.redeflexmobile.ui.adquirencia.visit.route;

import android.location.Location;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApiManager;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.google.android.gms.maps.model.LatLng;

import static com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect.TYPE_CUSTOMER;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

/**
 * @author Rogério Massa on 29/11/18.
 */

public class VisitProspectRoutePresenterImpl extends BasePresenterImpl<VisitProspectRouteView> implements
        VisitProspectRoutePresenter {

    private static final String FLAG_POINT_FIRST = "point A";
    private static final String FLAG_POINT_SECOND = "point B";

    private VisitProspectManager visitProspectManager;
    private GoogleMapsApiManager googleMapsApiManager;
    private RouteClientProspect customer;
    private Colaborador salesman;

    public VisitProspectRoutePresenterImpl(VisitProspectRouteView view,
                                           SchedulerProvider schedulerProvider,
                                           ExceptionUtils exceptionUtils,
                                           VisitProspectManager visitProspectManager,
                                           GoogleMapsApiManager googleMapsApiManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.visitProspectManager = visitProspectManager;
        this.googleMapsApiManager = googleMapsApiManager;
    }

    @Override
    public RouteClientProspect getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(RouteClientProspect customer) {
        this.customer = customer;
    }

    @Override
    public void setSalesman(Colaborador salesman) {
        this.salesman = salesman;
    }

    @Override
    public void getRoutes(LatLng start, LatLng end) {
        compositeDisposable.add(googleMapsApiManager.getRoute(start, end)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(routes -> getView().fillRoutes(routes),
                        throwable -> getView().fillRoutes(null)));
    }

    @Override
    public void updateCustomer(int customerId) {
        compositeDisposable.add(visitProspectManager.getClient(customerId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::setCustomer, this::showError));
    }

    @Override
    public void initializeAttendance() {
        if (!getView().validacaoDataAparelhoAdquirencia()) {
            return;
        }

        if (visitProspectManager.verifyOSWithoutScheduling()) {
            getView().validacaoOsSemAgendamento();
            return;
        }

        IGPSTracker gpsTracker = getView().getGpsTracker();
        if (!gpsTracker.isGPSEnabled()) {
            gpsTracker.showSettingsAlert();
            return;
        }

        if (gpsTracker.isMockLocationON() || gpsTracker.areThereMockPermissionApps()) {
            gpsTracker.showMockAlert();
            return;
        }

        if (customer.getType() == TYPE_CUSTOMER && customer.isContactUpdate()) {
            getView().openCustomerRegisterDialog(String.valueOf(customer.getId()));
            return;
        }

        if (!validateDistance()) {
            getView().validacaoNaoEstaNasImediacoes();
            return;
        }

        getView().onInitializeAttendance();
    }

    private boolean validateDistance() {
        double latitude = customer.getLatitude();
        double longitude = customer.getLongitude();

        IGPSTracker gpsTracker = getView().getGpsTracker();
        Location locationA = new Location(FLAG_POINT_FIRST);
        locationA.setLatitude(gpsTracker.getLatitude());
        locationA.setLongitude(gpsTracker.getLongitude());

        Location locationB = new Location(FLAG_POINT_SECOND);
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);

        float distance = locationA.distanceTo(locationB);
        distance = gpsTracker.getPrecisao() == EMPTY_INT ? EMPTY_INT : Math.round(distance);

        if (latitude == EMPTY_INT || longitude == EMPTY_INT) {
            return true;
        }

        if (customer.getType() == TYPE_CUSTOMER
                && (salesman.getDistancia() > EMPTY_INT || customer.getCerca() > EMPTY_INT)
                && gpsTracker.getPrecisao() > EMPTY_INT
                && distance > (customer.getCerca() + gpsTracker.getPrecisao())) {
            return false;
        }

        return salesman.getDistancia() <= EMPTY_INT
                || !(distance > (salesman.getDistancia() + gpsTracker.getPrecisao()));
    }
}
