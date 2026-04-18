package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author lucasmarciano on 16/07/20
 */
public class CallReason {
    @SerializedName("id") private int id;
    @SerializedName("descricao") private String description;
    @SerializedName("ativo") private boolean active;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return description;
    }
}
