package com.axys.redeflexmobile.shared.models;

import java.io.Serializable;

public class SolicitacaoPidRetorno implements Serializable {
    private int idSolicitacaoPid;
    private boolean sucesso;
    private String mensagem;
    private String status;

    public SolicitacaoPidRetorno() {
    }

    public int getIdSolicitacaoPid() {
        return idSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(int idSolicitacaoPid) {
        this.idSolicitacaoPid = idSolicitacaoPid;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
