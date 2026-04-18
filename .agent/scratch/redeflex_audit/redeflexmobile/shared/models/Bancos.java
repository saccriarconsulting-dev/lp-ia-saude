package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class Bancos implements ICustomSpinnerDialogModel {
    private String id;
    private String idServer;
    private String descricao;
    private String situacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdServer() {
        return idServer;
    }

    public void setIdServer(String idServer) {
        this.idServer = idServer;
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
        return Integer.parseInt(getId());
    }

    @Override
    public String getDescriptionValue() {
        return getDescricao();
    }
}