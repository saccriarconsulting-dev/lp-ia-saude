package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class RemessaItem {
    @SerializedName("id") private String id;
    @SerializedName("itemCodeSAP") private String itemCodeSAP;
    @SerializedName("qtdRemessa") private int qtdRemessa;
    @SerializedName("qtdInformada") private int qtdInformada;
    @SerializedName("dataConfirmacao") private Date dataConfirmacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemCodeSAP() {
        return itemCodeSAP;
    }

    public void setItemCodeSAP(String itemCodeSAP) {
        this.itemCodeSAP = itemCodeSAP;
    }

    public int getQtdInformada() {
        return qtdInformada;
    }

    public void setQtdInformada(int qtdInformada) {
        this.qtdInformada = qtdInformada;
    }

    public int getQtdRemessa() {
        return qtdRemessa;
    }

    public void setQtdRemessa(int qtdRemessa) {
        this.qtdRemessa = qtdRemessa;
    }

    public Date getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(Date dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
}