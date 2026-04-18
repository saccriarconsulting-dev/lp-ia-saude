package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class ValorPorFormaPagto {

    @SerializedName("aVista")
    private double aVista;

    @SerializedName("aPrazo")
    private double aPrazo;

    @SerializedName("pix")
    private double pix;

    @SerializedName("cartao")
    private double cartao;

    public double getAVista() {
        return aVista;
    }

    public void setAVista(double aVista) {
        this.aVista = aVista;
    }

    public double getAPrazo() {
        return aPrazo;
    }

    public void setAPrazo(double aPrazo) {
        this.aPrazo = aPrazo;
    }

    public double getPix() {
        return pix;
    }

    public void setPix(double pix) {
        this.pix = pix;
    }

    public double getCartao() {
        return cartao;
    }

    public void setCartao(double cartao) {
        this.cartao = cartao;
    }

    public static ValorPorFormaPagto fromJson(String json) {
        if (json == null || json.trim().isEmpty()) return null;
        return new com.google.gson.Gson()
                .fromJson(json, ValorPorFormaPagto.class);
    }
}