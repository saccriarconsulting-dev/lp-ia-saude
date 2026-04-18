package com.axys.redeflexmobile.ui.adquirencia.visit.cancel;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * @author Rogério Massa on 04/01/19.
 */

public interface VisitProspectCancelView extends BaseView {

    void fillReasons(List<VisitProspectCancelReason> reasons);

    void onConfirmSuccess(VisitProspectCancelReason reason, String observation);
}
