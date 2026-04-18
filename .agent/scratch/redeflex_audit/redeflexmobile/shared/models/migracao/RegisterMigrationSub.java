package com.axys.redeflexmobile.shared.models.migracao;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * @author Lucas Marciano on 24/03/2020
 */
public class RegisterMigrationSub {
    @SerializedName("idAppMobile") private Integer id;
    private int idServer;
    private Integer idVendedor;
    private Integer idCliente;
    private String email;
    @SerializedName("telCelular") private String telefoneCelular;
    private Integer tipoContaId;
    private Integer idBanco;
    private String agencia;
    private String digitoAgencia;
    private String conta;
    private String digitoConta;
    private String versaoApp;
    private Integer idMcc;
    private double faturamentoMedioPrevisto;
    private Integer idPrazoNegociacao;
    private Boolean antecipacao;
    private Integer idMotivoRecusa;
    private String observacaoRecusa;
    private double latitude;
    private double longitude;
    private double precisao;
    @JsonExclude private boolean sync;
    private String dataCadastro;
    private String token;
    @JsonExclude private Integer confirmado;
    @Expose(deserialize = false) @SerializedName("taxas")
    private List<RegisterMigrationSubTax> taxesList;
    @JsonExclude private String createdAt;
    private Integer situacao;
    private String retorno;
    @JsonExclude private boolean antecipacaoAutomatica;

    private EnumRegisterSexo Sexo;
    private String RG;
    private Integer IdProfissao;
    private Integer IdRendaMensal;
    private Integer IdPatrimonio;
    private double FaturamentoBruto;
    private int PercVendaCartao;
    private int PercFaturamentoEcommerce;
    private int PercFaturamento;
    private String EntregaPosCompra;
    private int PrazoEntrega;
    private int PercEntregaImediata;
    private int PercEntregaPosterior;
    private Date DataFundacaoPF;

    private String TipoMigracao;

    @Expose(deserialize = false) @SerializedName("horarioFuncionamento")
    private List<CadastroMigracaoSubHorario> horarioFuncionamento = new ArrayList<CadastroMigracaoSubHorario>();

    public RegisterMigrationSub() {
    }

    public RegisterMigrationSub(RegisterMigrationSub registerMigrationSub) {
        this.id = registerMigrationSub.getId();
        this.idVendedor = registerMigrationSub.getIdVendedor();
        this.idCliente = registerMigrationSub.getIdCliente();
        this.email = registerMigrationSub.getEmail();
        this.telefoneCelular = registerMigrationSub.getTelefoneCelular();
        this.tipoContaId = registerMigrationSub.getTipoConta();
        this.idBanco = registerMigrationSub.getIdBanco();
        this.agencia = registerMigrationSub.getAgencia();
        this.digitoAgencia = registerMigrationSub.getDigitoAgencia();
        this.conta = registerMigrationSub.getConta();
        this.digitoConta = registerMigrationSub.getDigitoConta();
        this.versaoApp = registerMigrationSub.getVersaoApp();
        this.idMcc = registerMigrationSub.getIdMcc();
        this.faturamentoMedioPrevisto = registerMigrationSub.getFaturamentoMedioPrevisto();
        this.idPrazoNegociacao = registerMigrationSub.getIdPrazoNegociacao();
        this.antecipacao = registerMigrationSub.isAntecipacao();
        this.idMotivoRecusa = registerMigrationSub.getIdMotivoRecusa();
        this.observacaoRecusa = registerMigrationSub.getObservacaoRecusa();
        this.latitude = registerMigrationSub.getLatitude();
        this.longitude = registerMigrationSub.getLongitude();
        this.precisao = registerMigrationSub.getPrecisao();
        this.sync = registerMigrationSub.isSync();
        this.dataCadastro = registerMigrationSub.getDataCadastro();
        this.confirmado = registerMigrationSub.getConfirmado();
        this.token = registerMigrationSub.getToken();
        this.taxesList = registerMigrationSub.getTaxesList();
        this.createdAt = registerMigrationSub.getCreatedAt();
        this.situacao = registerMigrationSub.getSituacao();
        this.retorno = registerMigrationSub.getRetorno();
        this.antecipacaoAutomatica = registerMigrationSub.isAntecipacaoAutomatica();
        this.horarioFuncionamento = registerMigrationSub.getHorarioFuncionamento();
        this.Sexo = registerMigrationSub.getSexo();
        this.RG = registerMigrationSub.getRG();
        this.IdProfissao = registerMigrationSub.getIdProfissao();
        this.IdRendaMensal = registerMigrationSub.getIdRendaMensal();
        this.IdPatrimonio = registerMigrationSub.getIdPatrimonio();
        this.FaturamentoBruto = registerMigrationSub.getFaturamentoBruto();
        this.PercVendaCartao = registerMigrationSub.getPercVendaCartao();
        this.PercFaturamentoEcommerce = registerMigrationSub.getPercFaturamentoEcommerce();
        this.PercFaturamento = registerMigrationSub.getPercFaturamento();
        this.EntregaPosCompra = registerMigrationSub.getEntregaPosCompra();
        this.PrazoEntrega = registerMigrationSub.getPrazoEntrega();
        this.PercEntregaImediata = registerMigrationSub.getPercEntregaImediata();
        this.PercEntregaPosterior = registerMigrationSub.getPercEntregaPosterior();
        this.DataFundacaoPF = registerMigrationSub.getDataFundacaoPF();
        this.TipoMigracao = registerMigrationSub.getTipoMigracao();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public Integer getTipoConta() {
        return tipoContaId;
    }

    public void setTipoConta(Integer tipoContaId) {
        this.tipoContaId = tipoContaId;
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getDigitoAgencia() {
        return digitoAgencia;
    }

    public void setDigitoAgencia(String digitoAgencia) {
        this.digitoAgencia = digitoAgencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDigitoConta() {
        return digitoConta;
    }

    public void setDigitoConta(String digitoConta) {
        this.digitoConta = digitoConta;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
    }

    public Integer getIdMcc() {
        return idMcc;
    }

    public void setIdMcc(Integer idMcc) {
        this.idMcc = idMcc;
    }

    public double getFaturamentoMedioPrevisto() {
        return faturamentoMedioPrevisto;
    }

    public void setFaturamentoMedioPrevisto(double faturamentoMedioPrevisto) {
        this.faturamentoMedioPrevisto = faturamentoMedioPrevisto;
    }

    public Integer getIdPrazoNegociacao() {
        return idPrazoNegociacao;
    }

    public void setIdPrazoNegociacao(Integer idPrazoNegociacao) {
        this.idPrazoNegociacao = idPrazoNegociacao;
    }

    public Boolean isAntecipacao() {
        return antecipacao;
    }

    public void setAntecipacao(Boolean antecipacao) {
        this.antecipacao = antecipacao;
    }

    public Integer getIdMotivoRecusa() {
        return idMotivoRecusa;
    }

    public void setIdMotivoRecusa(Integer idMotivoRecusa) {
        this.idMotivoRecusa = idMotivoRecusa;
    }

    public String getObservacaoRecusa() {
        return observacaoRecusa;
    }

    public void setObservacaoRecusa(String observacaoRecusa) {
        this.observacaoRecusa = observacaoRecusa;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrecisao() {
        return precisao;
    }

    public void setPrecisao(double precisao) {
        this.precisao = precisao;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Integer confirmado) {
        this.confirmado = confirmado;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<RegisterMigrationSubTax> getTaxesList() {
        return taxesList;
    }

    public void setTaxesList(List<RegisterMigrationSubTax> taxesList) {
        this.taxesList = taxesList;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    //TODO: Tabela cliente
    public boolean isAntecipacaoAutomatica() {
        return antecipacaoAutomatica;
    }

    //TODO: Tabela cliente
    public void setAntecipacaoAutomatica(boolean antecipacaoAutomatica) {
        this.antecipacaoAutomatica = antecipacaoAutomatica;
    }

    public List<CadastroMigracaoSubHorario> getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(List<CadastroMigracaoSubHorario> horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public EnumRegisterSexo getSexo() {
        return Sexo;
    }

    public void setSexo(EnumRegisterSexo sexo) {
        this.Sexo = sexo;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public Integer getIdProfissao() {
        return IdProfissao;
    }

    public void setIdProfissao(Integer idProfissao) {
        this.IdProfissao = idProfissao;
    }

    public Integer getIdRendaMensal() {
        return IdRendaMensal;
    }

    public void setIdRendaMensal(Integer idRendaMensal) {
        this.IdRendaMensal = idRendaMensal;
    }

    public Integer getIdPatrimonio() {
        return IdPatrimonio;
    }

    public void setIdPatrimonio(Integer idPatrimonio) {
        this.IdPatrimonio = idPatrimonio;
    }

    public double getFaturamentoBruto() {
        return FaturamentoBruto;
    }

    public void setFaturamentoBruto(double faturamentoBruto) {
        this.FaturamentoBruto = faturamentoBruto;
    }

    public Integer getPercVendaCartao() {
        return PercVendaCartao;
    }

    public void setPercVendaCartao(Integer percVendaCartao) {
        this.PercVendaCartao = percVendaCartao;
    }

    public Integer getPercFaturamentoEcommerce() {
        return PercFaturamentoEcommerce;
    }

    public void setPercFaturamentoEcommerce(Integer percFaturamentoEcommerce) {
        this.PercFaturamentoEcommerce = percFaturamentoEcommerce;
    }

    public Integer getPercFaturamento() {
        return PercFaturamento;
    }

    public void setPercFaturamento(Integer percFaturamento) {
        this.PercFaturamento = percFaturamento;
    }

    public String getEntregaPosCompra() {
        return EntregaPosCompra;
    }

    public void setEntregaPosCompra(String entregaPosCompra) {
        this.EntregaPosCompra = entregaPosCompra;
    }

    public Integer getPrazoEntrega() {
        return PrazoEntrega;
    }

    public void setPrazoEntrega(Integer prazoEntrega) {
        this.PrazoEntrega = prazoEntrega;
    }

    public Integer getPercEntregaImediata() {
        return PercEntregaImediata;
    }

    public void setPercEntregaImediata(Integer percEntregaImediata) {
        this.PercEntregaImediata = percEntregaImediata;
    }

    public Integer getPercEntregaPosterior() {
        return PercEntregaPosterior;
    }

    public void setPercEntregaPosterior(Integer percEntregaPosterior) {
        this.PercEntregaPosterior = percEntregaPosterior;
    }

    public Date getDataFundacaoPF() {
        return DataFundacaoPF;
    }

    public void setDataFundacaoPF(Date dataFundacaoPF) {
        this.DataFundacaoPF = dataFundacaoPF;
    }

    public String getTipoMigracao() {
        return TipoMigracao;
    }

    public void setTipoMigracao(String tipoMigracao) {
        this.TipoMigracao = tipoMigracao;
    }
}
