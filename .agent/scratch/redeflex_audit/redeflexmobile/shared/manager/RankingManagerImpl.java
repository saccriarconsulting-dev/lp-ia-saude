package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.services.network.RankingService;

import java.util.List;

import io.reactivex.Single;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Rogério Massa on 22/11/18.
 */

public class RankingManagerImpl implements RankingManager {

    private RankingService rankingService;
    private DBColaborador dbColaborador;

    public RankingManagerImpl(RankingService rankingService, DBColaborador dbColaborador) {
        this.rankingService = rankingService;
        this.dbColaborador = dbColaborador;
    }

    @Override
    public Single<List<Ranking>> fetchRankings() {
        return Single.just(dbColaborador.get()).flatMap(salesman ->
                rankingService.getRanking(String.valueOf(salesman.getId()), EMPTY_STRING));
    }
}
