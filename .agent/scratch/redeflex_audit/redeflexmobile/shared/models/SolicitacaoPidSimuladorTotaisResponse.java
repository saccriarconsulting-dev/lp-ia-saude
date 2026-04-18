package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class SolicitacaoPidSimuladorTotaisResponse {
    @SerializedName("receitaTotal") private double ReceitaTotal;
    @SerializedName("impostoTotal") private double ImpostoTotal;
    @SerializedName("receitaTotalLiquida") private double ReceitaTotalLiquida;
    @SerializedName("rentabilidadeBruta") private double RentabilidadeBruta;
    @SerializedName("takeRate") private double TakeRate;

    public SolicitacaoPidSimuladorTotaisResponse() {
    }

    public double getReceitaTotal() {
        return ReceitaTotal;
    }

    public void setReceitaTotal(double receitaTotal) {
        ReceitaTotal = receitaTotal;
    }

    public double getImpostoTotal() {
        return ImpostoTotal;
    }

    public void setImpostoTotal(double impostoTotal) {
        ImpostoTotal = impostoTotal;
    }

    public double getReceitaTotalLiquida() {
        return ReceitaTotalLiquida;
    }

    public void setReceitaTotalLiquida(double receitaTotalLiquida) {
        ReceitaTotalLiquida = receitaTotalLiquida;
    }

    public double getRentabilidadeBruta() {
        return RentabilidadeBruta;
    }

    public void setRentabilidadeBruta(double rentabilidadeBruta) {
        RentabilidadeBruta = rentabilidadeBruta;
    }

    public double getTakeRate() {
        return TakeRate;
    }

    public void setTakeRate(double takeRate) {
        TakeRate = takeRate;
    }
}
