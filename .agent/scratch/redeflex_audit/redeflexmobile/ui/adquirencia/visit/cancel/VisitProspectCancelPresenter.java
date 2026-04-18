package com.axys.redeflexmobile.ui.adquirencia.visit.cancel;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Rogério Massa on 04/01/19.
 */

public interface VisitProspectCancelPresenter extends BasePresenter<VisitProspectCancelView> {

    void getReasons();

    void saveReasonSelected(VisitProspectCancelReason reason, String observation);
}
