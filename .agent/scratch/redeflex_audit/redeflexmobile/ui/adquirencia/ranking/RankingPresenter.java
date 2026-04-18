package com.axys.redeflexmobile.ui.adquirencia.ranking;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Bruno Pimentel on 23/01/19.
 */
interface RankingPresenter extends BasePresenter<RankingView> {

    void getRankings();
}
