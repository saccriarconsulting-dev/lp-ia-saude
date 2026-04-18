package com.axys.redeflexmobile.ui.adquirencia.visit.route;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Rogério Massa on 29/11/18.
 */

public interface VisitProspectRoutePresenter extends BasePresenter<VisitProspectRouteView> {

    RouteClientProspect getCustomer();

    void setCustomer(RouteClientProspect customer);

    void setSalesman(Colaborador salesman);

    void initializeAttendance();

    void getRoutes(LatLng start, LatLng end);

    void updateCustomer(int customerId);
}
