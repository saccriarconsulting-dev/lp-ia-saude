package com.axys.redeflexmobile.shared.models;

/**
 * Created by Desenvolvimento on 02/05/2016.
 */
public class ComandoExecutar {
    private String id;
    private String comando;
    private String idVendedor;
    private String basedados;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getBasedados() {
        return basedados;
    }

    public void setBasedados(String basedados) {
        this.basedados = basedados;
    }
}