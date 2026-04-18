package com.axys.redeflexmobile.ui.adquirencia.visit.cancel;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

/**
 * @author Rogério Massa on 04/01/19.
 */

public class VisitProspectCancelPresenterImpl extends BasePresenterImpl<VisitProspectCancelView>
        implements VisitProspectCancelPresenter {

    private VisitProspectManager visitProspectManager;

    public VisitProspectCancelPresenterImpl(VisitProspectCancelView view,
                                     SchedulerProvider schedulerProvider,
                                     ExceptionUtils exceptionUtils,
                                     VisitProspectManager visitProspectManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.visitProspectManager = visitProspectManager;
    }

    @Override
    public void getReasons() {
        compositeDisposable.add(visitProspectManager.getReasons()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(reasons -> getView().fillReasons(reasons), this::showError));
    }

    @Override
    public void saveReasonSelected(VisitProspectCancelReason reason, String observation) {
        if (reason == null) {
            showError(getView().getStringByResId(R.string.visit_prospect_cancel_error));
            return;
        }
        getView().onConfirmSuccess(reason, observation);
    }
}
