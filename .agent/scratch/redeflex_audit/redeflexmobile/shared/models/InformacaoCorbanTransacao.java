package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class InformacaoCorbanTransacao {
    private int idcliente;
    @SerializedName("AnoMes") private int anomes;
    @SerializedName("QtdTransacao") private int qtdtransacao;

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public int getAnomes() {
        return anomes;
    }

    public void setAnomes(int anomes) {
        this.anomes = anomes;
    }

    public int getQtdtransacao() {
        return qtdtransacao;
    }

    public void setQtdtransacao(int qtdtransacao) {
        this.qtdtransacao = qtdtransacao;
    }
}
