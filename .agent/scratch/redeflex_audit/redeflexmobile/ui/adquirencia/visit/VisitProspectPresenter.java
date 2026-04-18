package com.axys.redeflexmobile.ui.adquirencia.visit;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Rogério Massa on 29/11/18.
 */

public interface VisitProspectPresenter extends BasePresenter<VisitProspectView> {

    Colaborador getSalesman();

    VisitProspect getVisitProspect();

    RouteClientProspect getVisitProspectCustomer();

    VisitProspectAttachment getVisitProspectAttachment();

    String getObservation();

    void setObservation(String observation);

    void getDataByCustomer(int clientId);

    void getDataByProspect(int clientId);

    void setRouteId(int routeId);

    void initializeVisit(VisitProspectAttachment visitProspectAttachment);

    void cancelVisit(VisitProspectCancelReason reason, String observation);

    void saveQualityVisit(List<VisitProspectQualityQuestion> answers, List<VisitProspectQualityQuestion> questions);

    void saveVisit();
}
