package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by Desenvolvimento on 02/05/2016.
 */
public class ComandoExecutado {
    private String id;
    private String mensagem;
    private Date dataExecucao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(Date dataExecucao) {
        this.dataExecucao = dataExecucao;
    }
}