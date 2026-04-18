package com.axys.redeflexmobile.shared.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SolicitacaoPidStatusRetorno implements Serializable {
    private boolean sucesso;
    private String mensagem;
    private ArrayList<SolicitacaoPidStatus> solicitacoes;

    public SolicitacaoPidStatusRetorno() {
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

    public ArrayList<SolicitacaoPidStatus> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(ArrayList<SolicitacaoPidStatus> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }
}
