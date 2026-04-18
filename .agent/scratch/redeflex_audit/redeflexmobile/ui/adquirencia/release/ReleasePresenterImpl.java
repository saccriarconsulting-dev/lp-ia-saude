package com.axys.redeflexmobile.ui.adquirencia.release;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.ReleaseManager;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class ReleasePresenterImpl extends BasePresenterImpl<ReleaseView> implements ReleasePresenter {

    private ReleaseManager releaseManager;

    public ReleasePresenterImpl(ReleaseView view,
                                SchedulerProvider schedulerProvider,
                                ExceptionUtils exceptionUtils,
                                ReleaseManager releaseManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.releaseManager = releaseManager;
    }

    @Override
    public void loadReleases(int clientId) {
        compositeDisposable.add(releaseManager.getReleases(clientId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(getView()::fillReleases, this::showError));
    }

    @Override
    public void reloadReleases(int clientId) {
        compositeDisposable.add(releaseManager.getReleases(clientId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showSwipeLoading())
                .doAfterTerminate(() -> getView().hideSwipeLoading())
                .subscribe(getView()::fillReleases, this::showError));
    }
}
