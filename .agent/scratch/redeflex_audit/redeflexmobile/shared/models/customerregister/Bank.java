package com.axys.redeflexmobile.shared.models.customerregister;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 16/11/18.
 */

public class Bank implements ICustomSpinnerDialogModel {

    @SerializedName("id") private Integer id;
    @SerializedName("descricao") private String description;
    @SerializedName("situacao") private String situation;

    public Bank() {
    }

    public Bank(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return description;
    }
}
