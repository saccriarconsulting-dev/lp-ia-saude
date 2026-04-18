package com.axys.redeflexmobile.ui.adquirencia.visit.quality;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.List;

/**
 * @author Rogério Massa on 04/01/19.
 */

public class VisitProspectQualityPresenterImpl extends BasePresenterImpl<VisitProspectQualityView>
        implements VisitProspectQualityPresenter {

    private VisitProspectManager visitProspectManager;
    private List<VisitProspectQualityQuestion> questionList;

    public VisitProspectQualityPresenterImpl(VisitProspectQualityView view,
                                             SchedulerProvider schedulerProvider,
                                             ExceptionUtils exceptionUtils,
                                             VisitProspectManager visitProspectManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.visitProspectManager = visitProspectManager;
    }

    @Override
    public List<VisitProspectQualityQuestion> getQuestionList() {
        return questionList;
    }

    @Override
    public void getQuestions() {
        compositeDisposable.add(visitProspectManager.getQuality()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(questions -> {
                    this.questionList = questions;
                    getView().fillReasons(questions);
                }, this::showError));
    }

    @Override
    public void saveQualitySelected(List<VisitProspectQualityQuestion> answers,
                                    List<VisitProspectQualityQuestion> questions) {
        if (!getView().validacaoDataAparelhoAdquirencia()) {
            return;
        }

        if (answers == null || answers.size() < questionList.size()) {
            showError(getView().getStringByResId(R.string.visit_prospect_quality_error));
            return;
        }
        getView().onConfirmSuccess(answers, questions);
    }
}
