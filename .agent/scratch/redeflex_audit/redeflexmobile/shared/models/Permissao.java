package com.axys.redeflexmobile.shared.models;

/**
 * Created by Desenvolvimento on 23/06/2016.
 */

public class Permissao {

    private String id;
    private String idApp;
    private String ativo;

    public Permissao() {
    }

    public Permissao(String idApp) {
        this.idApp = idApp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdApp() {
        return idApp;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
}