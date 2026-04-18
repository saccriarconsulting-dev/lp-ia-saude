package com.axys.redeflexmobile.ui.adquirencia.visit.quality;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * @author Rogério Massa on 04/01/19.
 */

public interface VisitProspectQualityView extends BaseView {

    void fillReasons(List<VisitProspectQualityQuestion> reasons);

    void onConfirmSuccess(List<VisitProspectQualityQuestion> answers, List<VisitProspectQualityQuestion> questions);

    boolean validacaoDataAparelhoAdquirencia();
}
