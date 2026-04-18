package com.axys.redeflexmobile.shared.models;

import androidx.annotation.NonNull;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;

/**
 * Created by joao.viana on 29/08/2016.
 */
public class Segmento implements ICustomSpinnerDialogModel, GenericaDialog.GenericaItem {
    private String id;
    private String codigo;
    private String descricao;
    private String situacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        return Integer.valueOf(codigo);
    }

    @Override
    public String getDescriptionValue() {
        return descricao;
    }

    @NonNull
    @Override
    public String toString() {
        return getDescricao();
    }
}