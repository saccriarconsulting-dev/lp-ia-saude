package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 22/11/18.
 */

public interface RankingManager {

    Single<List<Ranking>> fetchRankings();
}
