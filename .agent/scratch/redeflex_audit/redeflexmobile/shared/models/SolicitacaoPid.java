package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class SolicitacaoPid implements Serializable {
    @SerializedName("IdAppMobile") private Integer Id;
    private Integer Id_Server;
    private String TipoPessoa;
    private String TipoCliente;
    private Integer IdVendedor;
    private double Latitude;
    private double Longitude;
    private double Precisao;
    private String CodigoSGV;
    private Integer CodigoAdquirencia;
    private String CpfCnpj;
    private String RazaoSocial;
    private String NomeFantasia;
    private String Observacao;
    private Integer MCCPrincipal;
    private Integer DDD;
    private Integer Rede;
    private Integer NDeLojas;
    @SerializedName("TpvTotalEC") private double FaturamentoPrevisto;
    private String NomeConcorrente;
    private Double DistribuicaoDebito;
    @SerializedName("DistribuicaoCreditoAVista") private Double DistribuicaoCredito;
    @SerializedName("DistribuicaoCred2a6") private Double DistribuicaoCredito6x;
    @SerializedName("DistribuicaoCred7a12") private Double DistribuicaoCredito12x;
    private String Origem;
    private Integer Renegociacao;
    private Integer Contraproposta;
    private Integer Reprecificada;
    private Integer Sincronizado;
    private Date DataSync;
    private Date DataCadastro;
    private Date DataAvaliacao;
    private Date DataEnvioTermo;
    private String Status;
    private Integer PropostaId;
    private String TipoTaxaRAV;

    @SerializedName("DadosDaRede") private ArrayList<SolicitacaoPidRede> listaRedes;
    @SerializedName("POS") private ArrayList<SolicitacaoPidPos> listaPOS;

    @SerializedName("TaxasMdr") private ArrayList<SolicitacaoPidTaxaMDR> listaTaxas;
    @SerializedName("TaxaRav") private SolicitacaoPidTaxaRAV taxaRAV;
    @SerializedName("Anexos") private ArrayList<SolicitacaoPidAnexo> Anexos;

    @SerializedName("Produto") private ArrayList<SolicitacaoPidProduto> Produto;

    private Double ValorTaxaRAV;
    private int Aluguel;
    private int ModDebito;
    private int ModCredito;
    private int ModCredito2a6;
    private int ModCredito7a12;

    public SolicitacaoPid() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getId_Server() {
        return Id_Server;
    }

    public void setId_Server(Integer id_Server) {
        Id_Server = id_Server;
    }

    public String getTipoPessoa() {
        return TipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        TipoPessoa = tipoPessoa;
    }

    public String getTipoCliente() {
        return TipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        TipoCliente = tipoCliente;
    }

    public Integer getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        IdVendedor = idVendedor;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getPrecisao() {
        return Precisao;
    }

    public void setPrecisao(double precisao) {
        Precisao = precisao;
    }

    public String getCodigoSGV() {
        return CodigoSGV;
    }

    public void setCodigoSGV(String codigoSGV) {
        CodigoSGV = codigoSGV;
    }

    public Integer getCodigoAdquirencia() {
        return CodigoAdquirencia;
    }

    public void setCodigoAdquirencia(Integer codigoAdquirencia) {
        CodigoAdquirencia = codigoAdquirencia;
    }

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public String getRazaoSocial() {
        return RazaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        RazaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return NomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        NomeFantasia = nomeFantasia;
    }

    public String getObservacao() {
        return Observacao;
    }

    public void setObservacao(String observacao) {
        Observacao = observacao;
    }

    public Integer getMCCPrincipal() {
        return MCCPrincipal;
    }

    public void setMCCPrincipal(Integer MCCPrincipal) {
        this.MCCPrincipal = MCCPrincipal;
    }

    public Integer getDDD() {
        return DDD;
    }

    public void setDDD(Integer DDD) {
        this.DDD = DDD;
    }

    public Integer getRede() {
        return Rede;
    }

    public void setRede(Integer rede) {
        Rede = rede;
    }

    public Integer getNDeLojas() {
        return NDeLojas;
    }

    public void setNDeLojas(Integer NDeLojas) {
        this.NDeLojas = NDeLojas;
    }

    public double getFaturamentoPrevisto() {
        return FaturamentoPrevisto;
    }

    public void setFaturamentoPrevisto(double faturamentoPrevisto) {
        FaturamentoPrevisto = faturamentoPrevisto;
    }

    public String getNomeConcorrente() {
        return NomeConcorrente;
    }

    public void setNomeConcorrente(String nomeConcorrente) {
        NomeConcorrente = nomeConcorrente;
    }

    public Optional<Double> getDistribuicaoDebito() {
        return Optional.ofNullable(DistribuicaoDebito);
    }

    public void setDistribuicaoDebito(Double distribuicaoDebito) {
        DistribuicaoDebito = distribuicaoDebito;
    }

    public Optional<Double> getDistribuicaoCredito() {
        return Optional.ofNullable(DistribuicaoCredito);
    }

    public void setDistribuicaoCredito(Double distribuicaoCredito) {
        DistribuicaoCredito = distribuicaoCredito;
    }

    public Optional<Double> getDistribuicaoCredito6x() {
        return Optional.ofNullable(DistribuicaoCredito6x);
    }

    public void setDistribuicaoCredito6x(Double distribuicaoCredito6x) {
        DistribuicaoCredito6x = distribuicaoCredito6x;
    }

    public Optional<Double> getDistribuicaoCredito12x() {
        return Optional.ofNullable(DistribuicaoCredito12x);
    }

    public void setDistribuicaoCredito12x(Double distribuicaoCredito12x) {
        DistribuicaoCredito12x = distribuicaoCredito12x;
    }

    public String getOrigem() {
        return Origem;
    }

    public void setOrigem(String origem) {
        Origem = origem;
    }

    public Integer getRenegociacao() {
        return Renegociacao;
    }

    public void setRenegociacao(Integer renegociacao) {
        Renegociacao = renegociacao;
    }

    public Integer getContraproposta() {
        return Contraproposta;
    }

    public void setContraproposta(Integer contraproposta) {
        Contraproposta = contraproposta;
    }

    public Integer getReprecificada() {
        return Reprecificada;
    }

    public void setReprecificada(Integer reprecificada) {
        Reprecificada = reprecificada;
    }

    public Integer getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        Sincronizado = sincronizado;
    }

    public Date getDataSync() {
        return DataSync;
    }

    public void setDataSync(Date dataSync) {
        DataSync = dataSync;
    }

    public Date getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        DataCadastro = dataCadastro;
    }

    public Date getDataAvaliacao() {
        return DataAvaliacao;
    }

    public void setDataAvaliacao(Date dataAvaliacao) {
        DataAvaliacao = dataAvaliacao;
    }

    public Date getDataEnvioTermo() {
        return DataEnvioTermo;
    }

    public void setDataEnvioTermo(Date dataEnvioTermo) {
        DataEnvioTermo = dataEnvioTermo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Integer getPropostaId() {
        return PropostaId;
    }

    public void setPropostaId(Integer propostaId) {
        PropostaId = propostaId;
    }

    public ArrayList<SolicitacaoPidRede> getListaRedes() {
        return listaRedes;
    }

    public void setListaRedes(ArrayList<SolicitacaoPidRede> listaRedes) {
        this.listaRedes = listaRedes;
    }

    public ArrayList<SolicitacaoPidPos> getListaPOS() {
        return listaPOS;
    }

    public void setListaPOS(ArrayList<SolicitacaoPidPos> listaPOS) {
        this.listaPOS = listaPOS;
    }

    public ArrayList<SolicitacaoPidTaxaMDR> getListaTaxas() {
        return listaTaxas;
    }

    public void setListaTaxas(ArrayList<SolicitacaoPidTaxaMDR> listaTaxas) {
        this.listaTaxas = listaTaxas;
    }

    public String getTipoTaxaRAV() {
        return TipoTaxaRAV;
    }

    public void setTipoTaxaRAV(String tipoTaxaRAV) {
        TipoTaxaRAV = tipoTaxaRAV;
    }

    public SolicitacaoPidTaxaRAV getTaxaRAV() {
        return taxaRAV;
    }

    public void setTaxaRAV(SolicitacaoPidTaxaRAV taxaRAV) {
        this.taxaRAV = taxaRAV;
    }

    public ArrayList<SolicitacaoPidAnexo> getAnexos() {
        return Anexos;
    }

    public void setAnexos(ArrayList<SolicitacaoPidAnexo> anexos) {
        Anexos = anexos;
    }

    public ArrayList<SolicitacaoPidProduto> getProduto() {
        return Produto;
    }

    public void setProduto(ArrayList<SolicitacaoPidProduto> produto) {
        Produto = produto;
    }

    public Double getValorTaxaRAV() {
        return ValorTaxaRAV;
    }

    public void setValorTaxaRAV(Double valorTaxaRAV) {
        ValorTaxaRAV = valorTaxaRAV;
    }

    public int getAluguel() {
        return Aluguel;
    }

    public void setAluguel(int aluguel) {
        Aluguel = aluguel;
    }

    public int getModDebito() {
        return ModDebito;
    }

    public void setModDebito(int modDebito) {
        ModDebito = modDebito;
    }

    public int getModCredito() {
        return ModCredito;
    }

    public void setModCredito(int modCredito) {
        ModCredito = modCredito;
    }

    public int getModCredito2a6() {
        return ModCredito2a6;
    }

    public void setModCredito2a6(int modCredito2a6) {
        ModCredito2a6 = modCredito2a6;
    }

    public int getModCredito7a12() {
        return ModCredito7a12;
    }

    public void setModCredito7a12(int modCredito7a12) {
        ModCredito7a12 = modCredito7a12;
    }
}