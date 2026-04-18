package com.axys.redeflexmobile.ui.adquirencia.visit.route;

import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApi;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.IGPSTracker;

import java.util.List;

/**
 * @author Rogério Massa on 29/11/18.
 */

public interface VisitProspectRouteView extends BaseView {

    void onInitializeAttendance();

    void fillRoutes(List<GoogleMapsApi> routes);

    void openCustomerRegisterDialog(String customerId);

    IGPSTracker getGpsTracker();

    boolean validacaoDataAparelhoAdquirencia();

    void validacaoOsSemAgendamento();

    void validacaoNaoEstaNasImediacoes();
}
