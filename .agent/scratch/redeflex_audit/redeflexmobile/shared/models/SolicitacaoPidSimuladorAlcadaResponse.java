package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SolicitacaoPidSimuladorAlcadaResponse {
    private boolean dentroDaAlcada;
    private String mensagem;
    private List<SolicitacaoPidSimuladorTaxasRecomendadasResponse> taxasRecomendadas;

    public SolicitacaoPidSimuladorAlcadaResponse() {
    }

    public boolean isDentroDaAlcada() {
        return dentroDaAlcada;
    }

    public void setDentroDaAlcada(boolean dentroDaAlcada) {
        this.dentroDaAlcada = dentroDaAlcada;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<SolicitacaoPidSimuladorTaxasRecomendadasResponse> getTaxasRecomendadas() {
        return taxasRecomendadas;
    }

    public void setTaxasRecomendadas(List<SolicitacaoPidSimuladorTaxasRecomendadasResponse> taxasRecomendadas) {
        this.taxasRecomendadas = taxasRecomendadas;
    }
}
