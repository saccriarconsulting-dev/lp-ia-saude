package com.axys.redeflexmobile.shared.models;

import androidx.annotation.NonNull;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

public class FaixaPatrimonial implements ICustomSpinnerDialogModel {
    private int Id;
    private String Descricao;
    private String Situacao;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getSituacao() {
        return Situacao;
    }

    public void setSituacao(String situacao) {
        Situacao = situacao;
    }

    @Override
    public Integer getIdValue() {
        return Id;
    }

    @Override
    public String getDescriptionValue() {
        return Descricao;
    }

    @NonNull
    @Override
    public String toString() {
        return getDescricao();
    }
}
