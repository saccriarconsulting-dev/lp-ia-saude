package com.axys.redeflexmobile.shared.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 07/01/19.
 */

public class VisitProspectQualityQuestion {

    @SerializedName("id") private int id;
    @SerializedName("tipo") private int type;
    @SerializedName("descricao") private String description;
    @SerializedName("ativo") private boolean activate;

    public VisitProspectQualityQuestion(int id, int type, String description, boolean activate) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.activate = activate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
