package com.axys.redeflexmobile.ui.adquirencia.ranking;

import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author Rogério Massa on 02/01/19.
 */

public interface RankingView extends BaseActivityView {
    void onGetRankingSuccess(List<Ranking> rankings);
}
