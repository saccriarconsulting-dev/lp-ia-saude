package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.services.network.RankingService;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 26/11/18.
 */

public class MapItemManagerImpl implements MapItemManager {

    private RankingService rankingService;
    private DBColaborador dbColaborador;

    public MapItemManagerImpl(RankingService rankingService,
                              DBColaborador dbColaborador) {
        this.rankingService = rankingService;
        this.dbColaborador = dbColaborador;
    }

    @Override
    public Single<List<Ranking>> getMapItems(EnumMapPeriod period) {
        return Single.just(dbColaborador.get()).flatMap(colaborador ->
                rankingService.getRanking(String.valueOf(colaborador.getId()), period.getValue()));
    }
}
