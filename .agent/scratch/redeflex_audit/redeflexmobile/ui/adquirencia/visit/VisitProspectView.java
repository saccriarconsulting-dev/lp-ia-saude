package com.axys.redeflexmobile.ui.adquirencia.visit;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;
import com.axys.redeflexmobile.shared.util.IGPSTracker;

import java.util.List;

/**
 * @author Rogério Massa on 29/11/18.
 */

public interface VisitProspectView extends BaseActivityView {

    void setToolbarTitle(String title);

    void takePicture();

    void initializeFlow();

    Colaborador getSalesman();

    VisitProspect getVisit();

    RouteClientProspect getCustomer();

    Integer getCustomerId();

    String getObservation();

    void setObservation(String observation);

    void cancelVisit(VisitProspectCancelReason reason, String observation);

    void saveQualityVisit(List<VisitProspectQualityQuestion> answers, List<VisitProspectQualityQuestion> questions);

    void finishVisit(Integer flowFlag);

    VisitProspectAttachment getVisitProspectAttachment();

    IGPSTracker getGpsTracker();
}
