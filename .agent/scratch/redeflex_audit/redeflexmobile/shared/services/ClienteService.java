package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.GenericResponse;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClienteService {

    @GET("ConsultaCep")
    Single<Cep[]> obterInformacoesCep(@Query("value") String cep);

    @POST(URLs.REENVIA_SENHA_CLIENTE)
    Single<GenericResponse> obterSenhaSgv(@Body ReenviaSenhaCliente reenviaSenhaCliente);
}
