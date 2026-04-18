package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joao.viana on 26/07/2016.
 */
public class SenhaVenda {
    @SerializedName("id")
    private int id;
    @SerializedName("senha")
    private String senha;
    @SerializedName("idVendedor")
    private String idVendedor;

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}