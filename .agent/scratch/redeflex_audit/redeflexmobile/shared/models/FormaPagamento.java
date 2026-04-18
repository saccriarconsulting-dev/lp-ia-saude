package com.axys.redeflexmobile.shared.models;

/**
 * Created by Desenvolvimento on 06/07/2016.
 */
public class FormaPagamento {
    private int id;
    private String descricao;
    private String ativo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
}