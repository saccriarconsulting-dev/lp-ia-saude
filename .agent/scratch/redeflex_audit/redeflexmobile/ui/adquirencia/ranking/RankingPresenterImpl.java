package com.axys.redeflexmobile.ui.adquirencia.ranking;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.RankingManager;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

/**
 * @author Bruno Pimentel on 23/01/19.
 */
public class RankingPresenterImpl extends BasePresenterImpl<RankingView> implements RankingPresenter {

    private RankingManager rankingManager;

    public RankingPresenterImpl(
            RankingView view,
            SchedulerProvider schedulerProvider,
            ExceptionUtils exceptionUtils,
            RankingManager rankingManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.rankingManager = rankingManager;
    }

    @Override
    public void getRankings() {
        compositeDisposable.add(rankingManager.fetchRankings()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(rankings -> getView().onGetRankingSuccess(rankings), this::showError));
    }
}
