package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SolicitacaoPidSimuladorResponse {
    @SerializedName("idSolicitacaoPid") private int IdSolicitacaoPid;
    @SerializedName("sucesso") private boolean Sucesso;
    @SerializedName("mensagem") private String Mensagem;
    @SerializedName("simuladorTotais") private SolicitacaoPidSimuladorTotaisResponse SimuladorTotais;
    @SerializedName("creditoEfetivo") private List<SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse> CreditoEfetivo;

    private SolicitacaoPidSimuladorAlcadaResponse alcada;

    public SolicitacaoPidSimuladorResponse() {
    }

    public boolean isSucesso() {
        return Sucesso;
    }

    public void setSucesso(boolean sucesso) {
        Sucesso = sucesso;
    }

    public String getMensagem() {
        return Mensagem;
    }

    public void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }

    public SolicitacaoPidSimuladorTotaisResponse getSimuladorTotais() {
        return SimuladorTotais;
    }

    public void setSimuladorTotais(SolicitacaoPidSimuladorTotaisResponse simuladorTotais) {
        SimuladorTotais = simuladorTotais;
    }

    public List<SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse> getCreditoEfetivo() {
        return CreditoEfetivo;
    }

    public void setCreditoEfetivo(List<SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse> creditoEfetivo) {
        CreditoEfetivo = creditoEfetivo;
    }

    public SolicitacaoPidSimuladorAlcadaResponse getAlcada() {
        return alcada;
    }

    public void setAlcada(SolicitacaoPidSimuladorAlcadaResponse alcada) {
        this.alcada = alcada;
    }

    public int getIdSolicitacaoPid() {
        return IdSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(int idSolicitacaoPid) {
        IdSolicitacaoPid = idSolicitacaoPid;
    }
}
