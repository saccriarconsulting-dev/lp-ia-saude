package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 30/05/2017.
 */

public class RetCodBarra {
    private boolean inclusaoOK;
    private String mensagem;
    private int quantidade;
    private CodBarra codBarra;

    public CodBarra getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(CodBarra codBarra) {
        this.codBarra = codBarra;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isInclusaoOK() {
        return inclusaoOK;
    }

    public void setInclusaoOK(boolean inclusaoOK) {
        this.inclusaoOK = inclusaoOK;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}