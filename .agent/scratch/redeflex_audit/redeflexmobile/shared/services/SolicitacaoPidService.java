package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorRequest;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SolicitacaoPidService {
    @POST("GetSimulacaoPID")
    Call<SolicitacaoPidSimuladorResponse> simuladorPid(@Body SolicitacaoPidSimuladorRequest simuladorRequest);
}
