package com.axys.redeflexmobile.ui.adquirencia.visit.quality;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Rogério Massa on 04/01/19.
 */

public interface VisitProspectQualityPresenter extends BasePresenter<VisitProspectQualityView> {

    List<VisitProspectQualityQuestion> getQuestionList();

    void getQuestions();

    void saveQualitySelected(List<VisitProspectQualityQuestion> answers, List<VisitProspectQualityQuestion> questions);
}
