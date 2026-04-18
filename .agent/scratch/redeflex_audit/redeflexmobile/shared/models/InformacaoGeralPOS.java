package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 06/11/18.
 */

public class InformacaoGeralPOS {

    @SerializedName("Id") private Integer id;
    @SerializedName("IdCliente") private Integer idCliente;
    @SerializedName("CodigoMasters") private String codigoMasters;
    @SerializedName("CodigoMuxx") private String codigoMuxx;
    @SerializedName("NumeroSerie") private String numeroSerie;
    @SerializedName("EntidadePontoCapturaId") private Integer entidadePontoCapturaId;
    @SerializedName("Adquirencia") private boolean adquirencia;
    @SerializedName("Recarga") private Integer recarga;
    @SerializedName("DataInstalacaoAdquirencia") private Date dataInstalacaoAdquirencia;
    @SerializedName("ValorAluguel") private Double valorAluguel;
    @SerializedName("DataUltimaTransacaoAdquirencia") private Date dataUltimaTransacaoAdquirencia;
    @SerializedName("DataUltimaVendaRecarga") private Date dataUltimaVendaRecarga;
    @SerializedName("ValorTransacionadoAdquirencia") private Double valorTransacionadoAdquirencia;
    @SerializedName("TerminalAlocadoSGV") private boolean terminalAlocadoSGV;
    @SerializedName("Pontuacao") private Double pontuacao;
    @SerializedName("ICCID") private String iccid;
    @SerializedName("SituacaoPontoCapturaId") private Integer situacaoPontoCapturaId;
    @SerializedName("SituacaoPontoCaptura") private Integer situacaoPontoCaptura;
    @SerializedName("Modelo") private String modelo;
    @SerializedName("Descricao") private String descricao;
    @SerializedName("DataCadastro") private Date dataCadastro;
    @SerializedName("VendedorInstalacao") private String vendedorInstalacao;
    @SerializedName("Ativo") private boolean ativo;
    @SerializedName("ValorTransMesAtual") private double valortransmesatual;
    @SerializedName("ValorTransMesAnterior") private double valortransmesanterior;
    @SerializedName("TransacionadoRecarga") private String transacionadorecarga;
    @SerializedName("ValorRecarga_Transacionado") private double valorrecarga_transacionado;
    @SerializedName("ValorRecarga_TransMesAtual") private double valorrecarga_transmesatual;
    @SerializedName("ValorRecarga_TransMesAnterior") private double valorrecarga_transmesanterior;

    @SerializedName("ValorPix") private double valorPix;
    @SerializedName("ValorPixTransMesAtual") private double valorPixTransMesAtual;
    @SerializedName("ValorPixTransMesAnterior") private double valorPixTransMesAnterior;

    private Date dataUltimaTransacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigoMasters() {
        return codigoMasters;
    }

    public void setCodigoMasters(String codigoMasters) {
        this.codigoMasters = codigoMasters;
    }

    public String getCodigoMuxx() {
        return codigoMuxx;
    }

    public void setCodigoMuxx(String codigoMuxx) {
        this.codigoMuxx = codigoMuxx;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Integer getEntidadePontoCapturaId() {
        return entidadePontoCapturaId;
    }

    public void setEntidadePontoCapturaId(Integer entidadePontoCapturaId) {
        this.entidadePontoCapturaId = entidadePontoCapturaId;
    }

    public boolean getAdquirencia() {
        return adquirencia;
    }

    public void setAdquirencia(boolean adquirencia) {
        this.adquirencia = adquirencia;
    }

    public Integer getRecarga() {
        return recarga;
    }

    public void setRecarga(Integer recarga) {
        this.recarga = recarga;
    }

    public Date getDataInstalacaoAdquirencia() {
        return dataInstalacaoAdquirencia;
    }

    public void setDataInstalacaoAdquirencia(Date dataInstalacaoAdquirencia) {
        this.dataInstalacaoAdquirencia = dataInstalacaoAdquirencia;
    }

    public Double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(Double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Date getDataUltimaTransacaoAdquirencia() {
        return dataUltimaTransacaoAdquirencia;
    }

    public void setDataUltimaTransacaoAdquirencia(Date dataUltimaTransacaoAdquirencia) {
        this.dataUltimaTransacaoAdquirencia = dataUltimaTransacaoAdquirencia;
    }

    public Date getDataUltimaVendaRecarga() {
        return dataUltimaVendaRecarga;
    }

    public void setDataUltimaVendaRecarga(Date dataUltimaVendaRecarga) {
        this.dataUltimaVendaRecarga = dataUltimaVendaRecarga;
    }

    public Double getValorTransacionadoAdquirencia() {
        return valorTransacionadoAdquirencia;
    }

    public void setValorTransacionadoAdquirencia(Double valorTransacionadoAdquirencia) {
        this.valorTransacionadoAdquirencia = valorTransacionadoAdquirencia;
    }

    public boolean getTerminalAlocadoSGV() {
        return terminalAlocadoSGV;
    }

    public void setTerminalAlocadoSGV(boolean terminalAlocadoSGV) {
        this.terminalAlocadoSGV = terminalAlocadoSGV;
    }

    public Double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public Integer getSituacaoPontoCapturaId() {
        return situacaoPontoCapturaId;
    }

    public void setSituacaoPontoCapturaId(Integer situacaoPontoCapturaId) {
        this.situacaoPontoCapturaId = situacaoPontoCapturaId;
    }

    public Integer getSituacaoPontoCaptura() {
        return situacaoPontoCaptura;
    }

    public void setSituacaoPontoCaptura(Integer situacaoPontoCaptura) {
        this.situacaoPontoCaptura = situacaoPontoCaptura;
    }

    public Date getDataUltimaTransacao() {
        return dataUltimaTransacao;
    }

    public void setDataUltimaTransacao(Date dataUltimaTransacao) {
        this.dataUltimaTransacao = dataUltimaTransacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getVendedorInstalacao() {
        return vendedorInstalacao;
    }

    public void setVendedorInstalacao(String vendedorInstalacao) {
        this.vendedorInstalacao = vendedorInstalacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public double getValortransmesatual() {
        return valortransmesatual;
    }

    public void setValortransmesatual(double valortransmesatual) {
        this.valortransmesatual = valortransmesatual;
    }

    public double getValortransmesanterior() {
        return valortransmesanterior;
    }

    public void setValortransmesanterior(double valortransmesanterior) {
        this.valortransmesanterior = valortransmesanterior;
    }

    public String getTransacionadorecarga() {
        return transacionadorecarga;
    }

    public void setTransacionadorecarga(String transacionadorecarga) {
        this.transacionadorecarga = transacionadorecarga;
    }

    public double getValorrecarga_transacionado() {
        return valorrecarga_transacionado;
    }

    public void setValorrecarga_transacionado(double valorrecarga_transacionado) {
        this.valorrecarga_transacionado = valorrecarga_transacionado;
    }

    public double getValorrecarga_transmesatual() {
        return valorrecarga_transmesatual;
    }

    public void setValorrecarga_transmesatual(double valorrecarga_transmesatual) {
        this.valorrecarga_transmesatual = valorrecarga_transmesatual;
    }

    public double getValorrecarga_transmesanterior() {
        return valorrecarga_transmesanterior;
    }

    public void setValorrecarga_transmesanterior(double valorrecarga_transmesanterior) {
        this.valorrecarga_transmesanterior = valorrecarga_transmesanterior;
    }

    public double getValorPix() {
        return valorPix;
    }

    public void setValorPix(double valorPix) {
        this.valorPix = valorPix;
    }

    public double getValorPixTransMesAtual() {
        return valorPixTransMesAtual;
    }

    public void setValorPixTransMesAtual(double valorPixTransMesAtual) {
        this.valorPixTransMesAtual = valorPixTransMesAtual;
    }

    public double getValorPixTransMesAnterior() {
        return valorPixTransMesAnterior;
    }

    public void setValorPixTransMesAnterior(double valorPixTransMesAnterior) {
        this.valorPixTransMesAnterior = valorPixTransMesAnterior;
    }
}
