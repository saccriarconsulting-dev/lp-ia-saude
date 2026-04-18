package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class RelatorioSupervisorVendedor {

    @SerializedName("IdVendedor") public String idVendedor;
    @SerializedName("DataInicial") public String dataInicial;
    @SerializedName("DataFinal") public String dataFinal;
}
