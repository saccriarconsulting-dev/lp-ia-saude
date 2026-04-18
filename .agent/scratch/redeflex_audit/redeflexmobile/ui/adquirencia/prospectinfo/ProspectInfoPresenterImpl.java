package com.axys.redeflexmobile.ui.adquirencia.prospectinfo;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ProspectInfoManager;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.shared.util.exception.MessageException;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */

public class ProspectInfoPresenterImpl extends BasePresenterImpl<ProspectInfoView>
        implements ProspectInfoPresenter {

    private ProspectInfoManager prospectInfoManager;

    public ProspectInfoPresenterImpl(ProspectInfoView view,
                                     SchedulerProvider schedulerProvider,
                                     ExceptionUtils exceptionUtils,
                                     ProspectInfoManager prospectInfoManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.prospectInfoManager = prospectInfoManager;
    }

    @Override
    public void getProspectInfo(int prospectId) {
        compositeDisposable.add(prospectInfoManager.getProspectInfo(prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(getView()::fillProspectInfo, this::getProspectInfoError));
    }

    private void getProspectInfoError(Throwable throwable) {
        MessageException messageException = exceptionUtils.getExceptionsMessage(throwable);
        getView().getProspectInfoError(messageException.getMessage() != null
                ? messageException.getMessage()
                : getView().getStringByResId(messageException.getMessageResId()));
    }
}
