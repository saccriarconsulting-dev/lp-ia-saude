package com.axys.redeflexmobile.shared.network.api;

import com.axys.redeflexmobile.shared.bd.Operadora;
import com.axys.redeflexmobile.shared.models.Adquirencia;
import com.axys.redeflexmobile.shared.models.AnexoChamado;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.models.AudOpe;
import com.axys.redeflexmobile.shared.models.Bancos;
import com.axys.redeflexmobile.shared.models.CadastroVendedorPOS;
import com.axys.redeflexmobile.shared.models.CallReason;
import com.axys.redeflexmobile.shared.models.CampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.models.CartaoPontoRequest;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Cnae;
import com.axys.redeflexmobile.shared.models.Cobranca;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComandoExecutar;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.FaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.models.FaixaPatrimonial;
import com.axys.redeflexmobile.shared.models.FaixaSalarial;
import com.axys.redeflexmobile.shared.models.Filial;
import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.models.GenericResponse;
import com.axys.redeflexmobile.shared.models.HorarioNotificacao;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.IccidOperadora;
import com.axys.redeflexmobile.shared.models.InformacaoCorban;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.Interacoes;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.ListaDataPrazo;
import com.axys.redeflexmobile.shared.models.Mensagem;
import com.axys.redeflexmobile.shared.models.MobileCodBarra;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.Motivo;
import com.axys.redeflexmobile.shared.models.OrdemServico;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;
import com.axys.redeflexmobile.shared.models.RelatorioSupervisorVendedor;
import com.axys.redeflexmobile.shared.models.Remessa;
import com.axys.redeflexmobile.shared.models.RemessaAnexo;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RetornoItemVenda;
import com.axys.redeflexmobile.shared.models.RotaMobile;
import com.axys.redeflexmobile.shared.models.SenhaCliente;
import com.axys.redeflexmobile.shared.models.SenhaVenda;
import com.axys.redeflexmobile.shared.models.SituacaoSolicitacao;
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRetorno;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidStatusRetorno;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.models.TaxasAdquirencia;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.models.TipoLogradouro;
import com.axys.redeflexmobile.shared.models.TokenCliente;
import com.axys.redeflexmobile.shared.models.UltimaDataTransacaoPOS;
import com.axys.redeflexmobile.shared.models.VendaMobile;
import com.axys.redeflexmobile.shared.models.Vendedor;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCatalog;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterExemption;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.requests.AnexoRequestBody;
import com.axys.redeflexmobile.shared.models.requests.AtendimentoRequest;
import com.axys.redeflexmobile.shared.models.requests.CadastroPOSRequest;
import com.axys.redeflexmobile.shared.models.requests.ClienteCadastroRequest;
import com.axys.redeflexmobile.shared.models.requests.CustomerRegisterConfirm;
import com.axys.redeflexmobile.shared.models.requests.LoginRequest;
import com.axys.redeflexmobile.shared.models.responses.AnexoRetorno;
import com.axys.redeflexmobile.shared.models.responses.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.responses.ConsignadoLimiteCliente;
import com.axys.redeflexmobile.shared.models.responses.Departamento;
import com.axys.redeflexmobile.shared.models.responses.InformacoesChip;
import com.axys.redeflexmobile.shared.models.responses.LoginResponse;
import com.axys.redeflexmobile.shared.models.responses.Pendencia;
import com.axys.redeflexmobile.shared.models.responses.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.responses.PendenciaMotivo;
import com.axys.redeflexmobile.shared.models.responses.Permissao;
import com.axys.redeflexmobile.shared.models.responses.Profissoes;
import com.axys.redeflexmobile.shared.models.responses.ProjetoTrade;
import com.axys.redeflexmobile.shared.models.responses.ProjetoTradeItens;
import com.axys.redeflexmobile.shared.models.responses.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.models.responses.RelatorioMeta;
import com.axys.redeflexmobile.shared.models.responses.RetCadCliente;
import com.axys.redeflexmobile.shared.models.responses.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.responses.Segmento;
import com.axys.redeflexmobile.shared.models.responses.SyncResponse;
import com.axys.redeflexmobile.shared.models.responses.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.services.URLs;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Central Retrofit service interface with unified paths and DTO packages.
 */
public interface ApiService {

    @POST
    Call<Void> sync(@Url String url,
                    @Body Map<String, Object> payload);

    // Authentication
    @POST("auth/login")
    Single<LoginResponse> login(@Body LoginRequest body);

    // Full sync
    @GET("sync/full")
    Single<SyncResponse> syncFull(
            @Query("imei")    String imei,
            @Query("version") String appVersion
    );

    // OS endpoints
    @GET(URLs.OS + "/{id}")
    Call<Integer> syncVisualizacao(
            @Path("id")   long   id,
            @Query("data") String dataIso,
            @Query("campo") int    campo
    );

    @POST(URLs.OS)
    Call<Integer> syncAtendimento(@Body AtendimentoRequest body);

    // Customer registration
    @POST(URLs.CADASTRO_CLIENTE)
    Call<String> postCadastroCliente(@Body ClienteCadastroRequest request);

    @POST(URLs.CADASTRO_ADQUIRENCIA)
    Call<String> postCadastroAdquirencia(@Body ClienteCadastroRequest request);

    @POST(URLs.CONFIRMA_CADASTRO_CLIENTE)
    Call<String> postConfirmarCadastro(@Body CustomerRegisterConfirm body);

    @GET(URLs.CADASTRO_CLIENTE)
    Call<List<RetCadCliente>> getListaClientes(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")   String tipoCarga
    );

    // Customer attachments
    @POST(URLs.CADASTRO_CLIENTE_ANEXO)
    Call<String> postCadastroAnexo(@Body AnexoRequestBody body);

    @GET(URLs.CADASTRO_CLIENTE_ANEXO)
    Call<List<AnexoRetorno>> getListaAnexosVendedor(
            @Query("idVendedor") String idVendedor
    );

    @GET(URLs.CADASTRO_CLIENTE_ANEXO)
    Call<ResponseBody> downloadAnexo(
            @Query("idAnexo") int idAnexo
    );

    // POS registration
    @GET(URLs.CADASTRO_CLIENTE_POS)
    Call<List<ClienteCadastroPOS>> getListaPOS(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")   String tipoCarga
    );

    @POST(URLs.CADASTRO_CLIENTE_POS)
    Call<String> postCadastroPOS(@Body CadastroPOSRequest body);

    // Simple sync (partial)
    @GET("sync")
    Single<SyncResponse> sync(
            @Query("imei")    String imei,
            @Query("version") String version
    );

    // Chip identification
    @GET(URLs.IDENTIFICAR_CHIP)
    Call<InformacoesChip> identifyChip(
            @Query("barcodeChip") String barcodeChip
    );

    // Various GETs
    @GET(URLs.URL_CONSIGNADOLIMITECLIENTE)
    Call<List<ConsignadoLimiteCliente>> getConsignadoLimiteCliente(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")    int    tipoCarga
    );

    @GET(URLs.DEPARTAMENTO)
    Call<List<Departamento>> getDepartamentos(
            @NonNull @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")             int    tipoCarga
    );

    @GET(URLs.URL_PENDENCIA)
    Call<List<Pendencia>> getPendencias(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.URL_PENDENCIA_MOTIVO)
    Call<List<PendenciaMotivo>> getPendenciasMotivo(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.URL_PENDENCIA_CLIENTE)
    Call<List<PendenciaCliente>> getPendenciasCliente(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.PERGUNTAS_QUALIDADE)
    Call<List<VisitProspectQualityQuestion>> getPerguntasQualidade(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.PERMISSAO)
    Call<List<Permissao>> getPermissoes(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.PRAZO_NEGOCIACAO)
    Call<List<PrazoNegociacao>> getPrazosNegociacao(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.PROFISSOES)
    Call<Profissoes[]> getProfissoes(
            @NonNull @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")             int    tipoCarga
    );

    @GET(URLs.PROJETO_TRADE)
    Call<ProjetoTrade[]> getProjetoTrade(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.PROJETO_TRADE_ITENS)
    Call<ProjetoTradeItens[]> getProjetoTradeItens(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.PROSPECT)
    Call<RouteClientProspect[]> getProspect(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    @GET(URLs.URL_CADASTRO_MIGRACAO_SUB)
    Call<RegisterMigrationSub[]> getRegisterMigrationSub(
            @Query("idVendedor") String idVendedor,
            @Query("tipoCarga")  int    tipoCarga
    );

    // RelatorioMeta endpoints
    @GET(URLs.RELATORIO_META)
    Call<RelatorioMeta[]> getRelatorioMeta(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.RELATORIO_META)
    Call<Integer> syncRelatorioMeta(
            @Query("idVendedor")  int idVendedor,
            @Query("idIndicador") int idIndicador,
            @Query("idOperadora") int idOperadora
    );

    // Segmento endpoints
    @GET(URLs.SEGMENTO)
    Call<Segmento[]> getSegmento(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.SEGMENTO)
    Call<Integer> syncSegmento(
            @Query("idVendedor") int idVendedor,
            @Query("idSegmento") int idSegmento
    );

    @GET(URLs.SENHA_CLIENTE)
    Call<SenhaCliente[]> getSenhaCliente(
            @Query("idVendedor")  int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.SENHA_CLIENTE)
    Call<Integer> syncSenhaCliente(
            @Query("idServer")    String csvIds,
            @Query("idVendedor")  int    idVendedor
    );
    @POST(URLs.REENVIA_SENHA_CLIENTE)
    Call<GenericResponse> reenviaSenhaCliente(
            @Body ReenviaSenhaCliente request
    );
    @GET(URLs.SENHA_MASTERS + "/{idColaborador}")
    Call<RequestModel> getSenhaMasters(
            @Path("idColaborador") String idColaborador
    );

    @GET(URLs.SOLICITACAO_MERC)
    Call<SolicitacaoMercadoria[]> getSolicitacoes(
            @Query("idVendedor") int idVendedor,
            @Query("isReSync")   int isReSync
    );

    @GET(URLs.SOLICITACAO_MERC)
    Call<Integer> setIdAppModule(
            @Query("idSolicitacao") int   idSolicitacao,
            @Query("idAppModule")   long  idAppModule
    );

    @GET(URLs.SITUACAO_SOLICITACAO)
    Call<SituacaoSolicitacao[]> getSituacoesByIdServer(
            @Query("idSolicitacao") int idSolicitacao
    );

    @POST(URLs.SOLICITACAO_MERC)
    Call<String> postSolicitacao(
            @Body SolicitacaoMercadoria solicitacao
    );

    @POST(URLs.URL_SOLICITACAOPID)
    Call<SolicitacaoPidRetorno> postSolicitacaoPid(
            @Body SolicitacaoPid solicitacao
    );

    @POST(URLs.URL_SOLICITACAOPID_STATUS)
    Call<SolicitacaoPidStatusRetorno> getStatusSolicitacaoPid(
            @Body List<Integer> serverIds
    );

    @POST(URLs.URL_SOLICITACAOPRECODIFERENCIADO)
    Call<SolicitacaoPrecoDiferenciado> postSolicitacaoPrecoDiferenciado(
            @Body SolicitacaoPrecoDiferenciado request
    );

    @GET(URLs.URL_SOLICITACAOPRECODIFERENCIADO)
    Call<SolicitacaoPrecoDiferenciado[]> getSolPrecoDiferenciado(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.URL_SOLICITACAOPRECODIFERENCIADO)
    Call<Integer> syncSolPrecoDiferenciado(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.SOLICITACAO_TROCA_MOTIVO)
    Call<SolicitacaoTrocaMotivo[]> getSolicitacaoTrocaMotivo(
            @Query("idVendedor")  int idVendedor,
            @Query("tipoRecarga") int tipoRecarga
    );

    @GET(URLs.SOLICITACAO_TROCA_MOTIVO)
    Call<Integer> syncSolicitacaoTrocaMotivo(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.SOLICITACAO_TROCA)
    Call<ResponseBody> getSolicitacaoTrocaRaw(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.SOLICITACAO_TROCA)
    Call<Integer> syncSolicitacaoTroca(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @PUT(URLs.SOLICITACAO_TROCA)
    Call<Integer> postSolicitacaoTroca(
            @Body SolicitacaoTroca request
    );

    @GET(URLs.SUGESTAO_VENDA)
    Call<SugestaoVenda[]> getSugestaoVendaRaw(
            @Query("idVendedor") int    idVendedor,
            @Query("tipoCarga")   String tipoCarga
    );

    @GET(URLs.SUGESTAO_VENDA)
    Call<Integer> syncSugestaoVenda(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.TAXASADQUIRENCIA)
    Call<TaxasAdquirencia[]> getTaxasAdquirencia(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.TAXASADQUIRENCIA)
    Call<Integer> syncTaxasAdquirencia(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.TAXASADQUIRENCIAPF)
    Call<ResponseBody> getTaxasAdquirenciaPFRaw(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.TAXASADQUIRENCIAPF)
    Call<Integer> syncTaxasAdquirenciaPF(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.URL_TIPO_CONTA)
    Call<TipoConta[]> getTipoConta(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.URL_TIPO_CONTA)
    Call<Integer> syncTipoConta(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.URL_TIPO_LOGRADOURO)
    Call<TipoLogradouro[]> getTipoLogradouro(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.URL_TIPO_LOGRADOURO)
    Call<Integer> syncTipoLogradouro(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.TOKEN)
    Call<TokenCliente[]> getTokenCliente(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.TOKEN)
    Call<Integer> syncTokenCliente(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @GET(URLs.VENDA_MOBILE)
    Call<RetornoItemVenda[]> getBoletosGerados(
            @Query("idVendedor") int idVendedor
    );

    @GET(URLs.VENDA_MOBILE)
    Call<Integer> syncBoletosGerados(
            @Query("idServer")   String csvIds,
            @Query("idVendedor") int    idVendedor
    );

    @POST(URLs.VENDA_MOBILE)
    Call<String> postVendaMobile(
            @Body VendaMobile vendaMobile
    );

    @POST(URLs.VENDA_MOBILE_BARRA)
    Call<String> postVendaMobileCodBarra(
            @Body MobileCodBarra mobileCodBarra
    );

    @GET(URLs.RESOLVE_DATA_PRAZO)
    Call<ListaDataPrazo[]> getResolveDataPrazo(
            @Query("idVendedor") int idVendedor
    );

    @POST(URLs.RESOLVE_DATA_PRAZO)
    Call<String> postResolveDataPrazo(
            @Body List<VendaMobile> listaVendaMobile
    );

    @POST(URLs.VENDA_SENHA_SENHA)
    Call<String> postVendaSenha(@Body SenhaVenda senhaVenda);

    @GET(URLs.VENDA_SENHA)
    Call<SenhaVenda[]> getVendaSenha(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.VENDA_SENHA)
    Call<Integer> syncVendaSenha(
            @Query("idVendedor") int idVendedor,
            @Query("idServer")    String idServerCsv
    );

    @GET(URLs.VENDEDORES_SUPERVISOR + "/idSupervisor")
    Call<Vendedor[]> getVendedoresSupervisor(
            @Query("idSupervisor") int idSupervisor
    );

    @POST(URLs.ENVIO_SKY_TA)
    Call<Void> enviarComprovante(@Body RequestBody body);

    @POST(URLs.VISITA_ADQUIRENCIA)
    Call<String> enviarVisitaProspect(@Body RequestBody body);

    @POST(URLs.VISITA_ADQUIRENCIA_ANEXO)
    Call<Void> enviarVisitaProspectAnexo(@Body RequestBody body);

    @POST(URLs.VISITA_ADQUIRENCIA_QUALIDADE)
    Call<Void> enviarVisitaProspectQualidade(@Body RequestBody body);

    @POST(URLs.NOVA_TELEMETRIA)
    Call<String> enviarTelemetria(@Body RequestBody body);

    @POST(URLs.LOCALIZACAO_CLIENTE)
    Call<String> enviarLocalizacaoCliente(@Body RequestBody body);

    @POST(URLs.UTIL)
    Call<String> enviarPendencias(@Body RequestBody body);

    @POST(URLs.URL_CADASTRO_MIGRACAO_SUB)
    Call<String> sendRegisterMigrationSub(@Body RequestBody body);

    @POST(URLs.CONFIRMA_MIGRACAO_CLIENTE)
    Call<String> confirmRegisterMigrationSub(@Body RequestBody body);

    @POST(URLs.VISITA)
    Call<String> enviarVisita(@Body RequestBody body);

    @GET
    Call<String> getDynamic(@Url String url);

    @POST(URLs.PROSPECT_CLIENT_ACQUISITION)
    Call<String> sendProspectClientAcquisition(@Body RequestBody body);

    @POST(URLs.URL_PENDENCIA_CLIENTE)
    Call<String> sendPendenciaCliente(@Body RequestBody body);

    @GET(URLs.TAXA_MDR)
    Call<TaxaMdr[]> getTaxas(
            @NonNull @Query("idVendedor") Integer idVendedor,
            @NonNull @Query("tipoCarga") Integer tipoCarga
    );

    @POST(URLs.CLIENTE_ALTERACAO)
    Call<SyncResponse> atualizarCliente(@Body AtualizaCliente cliente);

    @GET(URLs.AUDITORIA_OPERADORA)
    Call<AudOpe[]> getAudOperadora(
            @Query("idVendedor") int vendedorId,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.AUDITORIA_OPERADORA)
    Call<Void> setSyncAudOp(
            @Query("idVendedor") int vendedorId,
            @Query("idAuditagemOperadora") int auditId
    );

    @GET(URLs.BANCOS)
    Call<List<Bancos>> getBancos(
            @Query("idVendedor") int vendedorId,
            @Query("tipoCarga")  int tipoCarga
    );
    @GET(URLs.BANCOS)
    Call<Void> setSyncBank(
            @Query("idVendedor") int vendedorId,
            @Query("idBanco")    int bancoId
    );

    @GET(URLs.RELATORIO_ADQUIRENCIA)
    Call<List<Adquirencia>> getRelatorio(@Query("idVendedor") String id);

    @GET(URLs.RELATORIO_ADQUIRENCIA)
    Call<RequestModel> getDados(@Query("idVendedor") String id);

    @GET(URLs.CADASTRO_VENDEDOR_POS)
    Call<CadastroVendedorPOS[]> getCadastroVendedorPOS(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.CADASTRO_VENDEDOR_POS)
    Call<Integer> setSyncCadastroVendedorPOS(
            @Query("idVendedor") int idVendedor
    );

    @GET(URLs.URL_CALL_RESPONSE)
    Call<CallReason[]> getCallReason(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.URL_CAMPANHAMERCHANCLAROMATERIAL)
    Call<List<CampanhaMerchanClaroMaterial>> getMaterials(
            @Query("idVendedor") int vendedorId,
            @Query("tipoCarga")   int tipoCarga
    );

    @POST(URLs.SUPORTE)
    Call<String> enviarSolicitacao(@Body Map<String,Object> payload);

    @POST(URLs.REGISTRO_PONTO)
    Call<String> sendCartaoPonto(@Body CartaoPontoRequest request);

    @GET(URLs.CATALOGO_RFMA)
    Call<VisitProspectCatalog[]> getCatalogs();

    @GET(URLs.CHAMADOS)
    Call<Chamado[]> getChamados(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.CHAMADOS)
    Call<Integer> syncChamado(
            @Query("idChamado")   int chamadoId,
            @Query("idVendedor")  int vendedorId
    );

    @GET(URLs.INTERACOES)
    Call<Interacoes[]> getInteracoes(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.INTERACOES)
    Call<Integer> syncInteracao(
            @Query("idInteracao") int interacaoId,
            @Query("idVendedor")  int vendedorId
    );

    @GET(URLs.ANEXO_CHAMADO)
    Call<AnexoChamado[]> getAnexos(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.ANEXO_CHAMADO)
    Call<Integer> syncAnexo(
            @Query("idAnexo")    int anexoId,
            @Query("idVendedor") int vendedorId
    );

    @POST(URLs.INTERACOES)
    Call<Void> postInteracao(
            @Body Interacoes interacoes
    );

    @POST(URLs.CHAMADOS)
    Call<Chamado> postChamado(
            @Body Chamado chamado
    );

    @POST(URLs.ANEXO_CHAMADO)
    Call<Void> postAnexo(
            @Body AnexoChamado anexoChamado
    );

    @GET(URLs.CHAMADOS)
    Call<Integer> updateStatus(
            @Query("idChamado") int idChamado,
            @Query("situacao")    int situacao,
            @Query("dataAgendamento") String dataAgendamento
    );

    @POST(URLs.ANEXO_CHAMADO)
    Call<Void> postAnexoRaw(@Body RequestBody rawJson);

    @GET(URLs.CNAE)
    Call<Cnae[]> getCnae(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.COBRANCA)
    Call<Cobranca[]> getCobranca(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.COLABORADOR)
    Call<Colaborador> getColaborador(
            @Query("imei") String imei
    );

    @GET(URLs.DATA_HORA_SERVIDOR)
    Call<Date> getServerDate();

    @GET(URLs.COMANDO)
    Call<ComandoExecutar[]> getComandos(@Query("idVendedor") int idVendedor);

    @POST(URLs.COMANDO)
    Call<Void> postComando(@androidx.annotation.NonNull @Body JsonObject payload);

    @GET(URLs.PRECO_DIFERENCIADO)
    Call<PrecoDiferenciado[]> getPrecoDiferenciado(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")  int tipoCarga
    );

    @GET(URLs.PRECO_DIFERENCIADO)
    Call<Integer> syncPreco(
            @Query("idServer") int idServer
    );

    @GET(URLs.PRECO_CLIENTE)
    Call<Integer> updatePrecoVenda(
            @Query("idServer")    int idServer,
            @Query("idVenda")     int idVenda,
            @Query("idVendaItem") int idVendaItem,
            @Query("quantidade")  int quantidade
    );

    @PUT(URLs.URL_CONSIGNADO)
    Call<Consignado> putConsignado(@androidx.annotation.NonNull @Body Consignado consignado);

    @GET(URLs.URL_CONSIGNADO)
    Call<Consignado[]> getConsignado(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.FORMA_PAGAMENTO)
    Call<FormaPagamento[]> getFormaPagamentos(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.FILIAL)
    Call<Filial[]> getFilial(
            @Query("idVendedor") @NonNull int idVendedor,
            @Query("tipoCarga")   @NonNull int tipoCarga
    );

    @GET(URLs.ISENCAO)
    Call<CustomerRegisterExemption[]> getPrazoIsencao(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.URL_INFORMACAOCORBAN)
    Call<InformacaoCorban[]> getInformacaoCorban(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.LIMITE_CLIENTE)
    Call<LimiteCliente[]> getLimite(
            @Query("idVendedor") @NonNull int idVendedor,
            @Query("tipoCarga")   @NonNull int tipoCarga
    );

    @GET(URLs.MENSAGEM)
    Call<Mensagem[]> getMensagens(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor
    );

    @GET(URLs.MENSAGEM + "/{id}")
    Call<Integer> setVisualizacao(
            @Path("id") @androidx.annotation.NonNull int id,
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("dataVisualizacao") @androidx.annotation.NonNull String dataVisualizacao
    );

    @GET(URLs.MODELO_POS)
    Call<ModeloPOS[]> getModelos(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.URL_MOTIVO_MIGRACAO_SUB_GET)
    Call<MotiveMigrationSub[]> getSubs(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.MOTIVO_ADQUIRENCIA)
    Call<VisitProspectCancelReason[]> getMotivosAdquirencia(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.MOTIVO)
    Call<Motivo[]> getMotivos(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")  @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.OPERADORA)
    Call<Operadora[]> getOperadoras(
            @Query("idVendedor") @NonNull int idVendedor,
            @Query("tipoCarga")  @NonNull int tipoCarga
    );

    @GET(URLs.OS)
    Call<OrdemServico[]> getOs(
            @Query("idVendedor") @NonNull int idVendedor,
            @Query("tipoCarga")  @NonNull int tipoCarga
    );

    @GET(URLs.ULTIMA_DATA_TRANSACAO_POS)
    Call<UltimaDataTransacaoPOS[]> getUltimaTransacaoPOS(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.INFORMACOES_GERAIS_POS)
    Call<InformacaoGeralPOS[]> getInformacoesGeraisPOS(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.VENDEDORES_SUPERVISOR + "/{idColaborador}")
    Call<RequestModel> getVendedores(@Path("idColaborador") @androidx.annotation.NonNull String idColaborador);

    @POST(URLs.VENDAS_RESUMO)
    Call<RequestModel> postResumoVenda(@androidx.annotation.NonNull @Body JsonObject payload);

    @POST(URLs.VENDAS_VENDEDOR)
    Call<RequestModel> postVendasVendedor(@androidx.annotation.NonNull @Body RelatorioSupervisorVendedor relatorio);

    @GET(URLs.REMESSA)
    Call<Remessa[]> getRemessa(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @PUT(URLs.REMESSA)
    Call<Remessa> putRemessa(@androidx.annotation.NonNull @Body Remessa remessa);

    @GET(URLs.REMESSA_ANEXO)
    Call<RemessaAnexo[]> getAnexo(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @PUT(URLs.REMESSA_ANEXO)
    Call<Void> putRemessaAnexo(@androidx.annotation.NonNull @Body JsonObject payload);

    @GET(URLs.ROTA_ADQUIRENCIA)
    Call<RoutesProspect[]> getRotaAdquirencia(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.ROTA)
    Call<RotaMobile[]> getRota(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.URL_CLIENT_MDR_TAX)
    Call<ClientTaxMdr[]> fetchClientTaxMdr(
            @Query("idVendedor") long idVendedor,
            @Query("tipoCarga") int tipoCarga
    );

    @GET(URLs.URL_CLIENT_DOMICILIO_BANCARIO)
    Call<ClientHomeBanking[]> fetchClientHomeBanking(
            @Query("idVendedor") long idVendedor,
            @Query("tipoCarga") int tipoCarga
    );

    @GET(URLs.FAIXA_FATURAMENTOMENSAL)
    Call<List<FaixaDeFaturamentoMensal>> getFaixasFaturamento(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.FAIXA_PATRIMONIAL)
    Call<List<FaixaPatrimonial>> getFaixasPatrimonial(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.FAIXA_SALARIAL)
    Call<List<FaixaSalarial>> getFaixasSalarial(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @POST(URLs.DEVOLUCAO)
    Call<String> postDevolucao(@NonNull @Body JsonObject payload);

    @GET(URLs.ESTOQUE)
    Call<Produto[]> getProdutos(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @POST(URLs.AUDITAGEMESTOQUE)
    Call<String> postAuditagemEstoque(@androidx.annotation.NonNull @Body JsonObject payload);

    @POST(URLs.AUDITAGEM_CLIENTE)
    Call<String> postAuditagemCliente(@androidx.annotation.NonNull @Body JsonObject payload);

    @GET(URLs.PRODUTO)
    Call<EstruturaProd[]> getEstrutura(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.JORNADA)
    Call<HorarioNotificacao[]> getHorarios(
            @Query("idVendedor") int idVendedor,
            @Query("tipoCarga")   int tipoCarga
    );

    @GET(URLs.URL_ICCIOPERADORA)
    Call<IccidOperadora[]> getIccidOperadora(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.IDENTIFICAR_CHIP)
    Call<Iccid[]> getIccid(
            @Query("idVendedor") @androidx.annotation.NonNull int idVendedor,
            @Query("tipoCarga")   @androidx.annotation.NonNull int tipoCarga
    );

    @GET(URLs.URL_FLAGS)
    Call<FlagsBank[]> fetchFlagsBank(
            @Query("idVendedor") long idVendedor,
            @Query("tipoCarga") int tipoCarga
    );
}
