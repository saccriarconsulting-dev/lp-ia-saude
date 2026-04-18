package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class RelatorioSupervisorProduto {

    @SerializedName("IdOperadora") public long idOperadora;
    @SerializedName("Operadora") public String operadora;
    @SerializedName("Produto") public String produto;
    @SerializedName("Qtde") public long qtde;

    public RelatorioSupervisorProduto(long idOperadora, String operadora, String produto, long qtde) {
        this.idOperadora = idOperadora;
        this.operadora = operadora;
        this.produto = produto;
        this.qtde = qtde;
    }
}
