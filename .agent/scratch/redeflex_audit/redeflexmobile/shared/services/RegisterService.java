package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.models.CartaoConsultaRequest;
import com.axys.redeflexmobile.shared.models.CartaoConsultaResponse;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.ConsultaPessoaFisica;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.PostConsultaCPF;
import com.axys.redeflexmobile.shared.models.QrCodeConsultaRequest;
import com.axys.redeflexmobile.shared.models.QrCodeConsultaResponse;
import com.axys.redeflexmobile.shared.models.QrCodeGerarRequest;
import com.axys.redeflexmobile.shared.models.QrCodeGerarResponse;
import com.axys.redeflexmobile.shared.models.TaxaMdrRequest;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @author Bruno Pimentel on 24/01/19.
 */
public interface RegisterService {

    @GET("ReceitaConsulta")
    Single<ConsultaReceita> queryCnpj(@Query("pCnpj") String cnpj);

    @GET("ConsultaCep")
    Single<List<Cep>> getAddress(@Query("value") String postalCode);

    @GET("ReceitaConsulta")
    Call<ConsultaReceita> retornaCliente(@Query("pCnpjCpf") String pCnpjCpf);
    
    @GET("ReceitaConsulta")
    Call<ConsultaReceita> retornaClienteReceita(@Query("pCnpjCpf") String pCnpjCpf);
    @POST("CadastroClienteProcessarCpfCnpj")
    Single<ConsultaPessoaFisica> getPessoaFisica(@Body PostConsultaCPF postConsultaCPF);
    @GET("CadastroAdquirencia")
    Single<CustomerRegister> getDadosCliente(@Query("pCpfCnpj") String pCpfCnpj);

    // Endpoint PIX - Cartão
    @POST("QrCodeGerar")
    Call<QrCodeGerarResponse> postGerarQrCode(@Body QrCodeGerarRequest json);

    @POST("QrCodeConsulta")
    Call<QrCodeConsultaResponse> getConsultaQrCode(@Body QrCodeConsultaRequest json);

    @PUT("QrCodeCancelar")
    Call<QrCodeConsultaResponse> putCancelarQrCode(@Body QrCodeConsultaRequest json);

    @POST("CartaoConsultaPagto")
    Call<CartaoConsultaResponse> getConsultaPagtoCartao(@Body CartaoConsultaRequest json);
    @POST("GetTaxaMdr")
    Call<TaxaMdr> getGetTaxaMdr(@Body TaxaMdrRequest json);
}
