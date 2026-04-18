package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 31/08/2017.
 */

public class DevolucaoICCID {
    private int id;
    private String iccidEntrada;
    private String iccidSaida;

    public String getIccidEntrada() {
        return iccidEntrada;
    }

    public void setIccidEntrada(String iccidEntrada) {
        this.iccidEntrada = iccidEntrada;
    }

    public String getIccidSaida() {
        return iccidSaida;
    }

    public void setIccidSaida(String iccidSaida) {
        this.iccidSaida = iccidSaida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}