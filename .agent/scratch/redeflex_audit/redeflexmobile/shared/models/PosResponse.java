package com.axys.redeflexmobile.shared.models;

public class PosResponse {

    private boolean sucesso;
    private String tituloErro;
    private String descricaoErro;
    private String codigoErro;

    public boolean isSucesso() {
        return sucesso;
    }

    public String getTituloErro() {
        return tituloErro;
    }

    public String getDescricaoErro() {
        return descricaoErro;
    }
}
