package com.axys.redeflexmobile.shared.models;

/**
 * Created by diego.lobo on 27/02/2018.
 */

public class TipoMaquina {
    private Integer TipoMaquinaId;
    private String Modelo;
    private Double ValorAluguelPadrao;

    public Integer getTipoMaquinaId() {
        return TipoMaquinaId;
    }

    public void setTipoMaquinaId(Integer tipoMaquinaId) {
        TipoMaquinaId = tipoMaquinaId;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public Double getValorAluguelPadrao() {
        return ValorAluguelPadrao;
    }

    public void setValorAluguelPadrao(Double valorAluguelPadrao) {
        ValorAluguelPadrao = valorAluguelPadrao;
    }
}