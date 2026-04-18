package com.axys.redeflexmobile.shared.services.network;

import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Bruno Pimentel on 22/01/19.
 */
public interface RankingService {

    @GET("farol")
    Single<List<Ranking>> getRanking(@Query("idVendedor") String clientId, @Query("Filtro") String filter);
}
