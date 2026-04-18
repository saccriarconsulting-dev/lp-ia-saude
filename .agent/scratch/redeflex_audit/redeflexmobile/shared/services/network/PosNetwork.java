package com.axys.redeflexmobile.shared.services.network;

import com.axys.redeflexmobile.shared.models.PosRequest;
import com.axys.redeflexmobile.shared.models.PosResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PosNetwork {

    @POST("BaixaPOS")
    Single<PosResponse> baixaPos(@Body PosRequest posRequest);
}
