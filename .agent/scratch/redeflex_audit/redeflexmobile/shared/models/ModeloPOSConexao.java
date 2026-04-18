package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

public class ModeloPOSConexao implements ICustomSpinnerDialogModel {

    @SerializedName("TipoConexaoId") private Integer id;
    @SerializedName("TipoConexaoDescricao") private String description;
    /* 0 - não mostra campos / 1 - mostra campos */
    @SerializedName("ObrigaOperadora") private boolean carrier;
    @SerializedName("ObrigaMetragemCabo") private boolean cableLength;
    private int idModelPos;

    public ModeloPOSConexao() {
    }

    public ModeloPOSConexao(Integer id, int idModelPos, String description) {
        this.id = id;
        this.idModelPos = idModelPos;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdModelPos() {
        return idModelPos;
    }

    public void setIdModelPos(int idModelPos) {
        this.idModelPos = idModelPos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getIdValue() {
        return getId();
    }

    @Override
    public String getDescriptionValue() {
        return getDescription();
    }

    public boolean isCarrier() {
        return carrier;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
    }

    public boolean isCableLength() {
        return cableLength;
    }

    public void setCableLength(boolean cableLength) {
        this.cableLength = cableLength;
    }
}
