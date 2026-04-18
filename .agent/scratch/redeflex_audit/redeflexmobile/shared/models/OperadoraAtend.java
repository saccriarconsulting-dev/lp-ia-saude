package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 31/10/2017.
 */

public class OperadoraAtend {
    private String descricao;
    private boolean atendido;
    private Date data;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }
}