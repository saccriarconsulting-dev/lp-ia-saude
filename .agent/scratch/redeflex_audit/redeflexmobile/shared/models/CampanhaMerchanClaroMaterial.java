package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;

public class CampanhaMerchanClaroMaterial implements ICustomSpinnerDialogModel, GenericaDialog.GenericaItem {
    private String idMaterial;
    private String descricao;
    private int idCampanha;
    private int ativo;

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    @Override
    public String getId() {
        return idMaterial;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(int idCampanha) {
        this.idCampanha = idCampanha;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    @Override
    public Integer getIdValue() {
        return Integer.parseInt(getIdMaterial());
    }

    @Override
    public String getDescriptionValue() {
        return getDescricao();
    }

    public String toString() {
        return "ID: " + getIdMaterial() + " - " + getDescricao();
    }

    public String toValue() {
        return getIdMaterial();
    }
}
