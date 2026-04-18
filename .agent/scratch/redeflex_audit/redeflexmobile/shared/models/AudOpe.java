package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joao.viana on 11/12/2017.
 */

public class AudOpe {

    @SerializedName("id") private int codigo;
    @SerializedName("idCliente") private String cliente;
    @SerializedName("idOperadora") private String operadora;
    @SerializedName("ativo") private boolean ativo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}