package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class InformacaoCorban {
    @SerializedName("Id") private int id;
    @SerializedName("IdCliente") private int idcliente;
    @SerializedName("CodCorban") private int codigocorban;
    @SerializedName("Loja") private String loja;
    @SerializedName("Lod") private double lod;
    @SerializedName("Situacao") private String situacao;
    @SerializedName("Valor") private double valor;
    @SerializedName("Dias") private int dias;
    @SerializedName("DataAtualizacao") private Date dataatualizacao;
    @SerializedName("DataAtivacao") private Date dataativacao;
    @SerializedName("DataUltimaTransacao") private Date dataultimatransacao;
    @SerializedName("DadosTransacoes") private List<InformacaoCorbanTransacao> dadostransacoes;

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public int getCodigocorban() {
        return codigocorban;
    }

    public void setCodigocorban(int codigocorban) {
        this.codigocorban = codigocorban;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public double getLod() {
        return lod;
    }

    public void setLod(double lod) {
        this.lod = lod;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getDataatualizacao() {
        return dataatualizacao;
    }

    public void setDataatualizacao(Date dataatualizacao) {
        this.dataatualizacao = dataatualizacao;
    }

    public Date getDataativacao() {
        return dataativacao;
    }

    public void setDataativacao(Date dataativacao) {
        this.dataativacao = dataativacao;
    }

    public Date getDataultimatransacao() {
        return dataultimatransacao;
    }

    public void setDataultimatransacao(Date dataultimatransacao) {
        this.dataultimatransacao = dataultimatransacao;
    }

    public List<InformacaoCorbanTransacao> getDadostransacoes() {
        return dadostransacoes;
    }

    public void setDadostransacoes(List<InformacaoCorbanTransacao> dadostransacoes) {
        this.dadostransacoes = dadostransacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
