package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse {
    @SerializedName("idBandeiraTipo") private int IdBandeiraTipo;
    @SerializedName("naturezaOperacao") private String NaturezaOperacao;
    @SerializedName("parcelas") private int Parcelas;
    private double taxaCredito;

    public SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse() {
    }

    public int getIdBandeiraTipo() {
        return IdBandeiraTipo;
    }

    public void setIdBandeiraTipo(int idBandeiraTipo) {
        IdBandeiraTipo = idBandeiraTipo;
    }

    public String getNaturezaOperacao() {
        return NaturezaOperacao;
    }

    public void setNaturezaOperacao(String naturezaOperacao) {
        NaturezaOperacao = naturezaOperacao;
    }

    public int getParcelas() {
        return Parcelas;
    }

    public void setParcelas(int parcelas) {
        Parcelas = parcelas;
    }

    public double getTaxaCredito() {
        return taxaCredito;
    }

    public void setTaxaCredito(double taxaCredito) {
        this.taxaCredito = taxaCredito;
    }
}
