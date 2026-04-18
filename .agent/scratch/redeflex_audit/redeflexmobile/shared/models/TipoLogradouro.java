package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

public class TipoLogradouro implements ICustomSpinnerDialogModel {
    private int id;
    private String descricao;
    private String situacao;

    public TipoLogradouro()
    {

    }

    public TipoLogradouro(int id, String descricao, String situacao) {
        this.id = id;
        this.descricao = descricao;
        this.situacao = situacao;
    }

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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return descricao;
    }
}
