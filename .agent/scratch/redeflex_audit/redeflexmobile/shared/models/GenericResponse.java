package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class GenericResponse {

    @SerializedName("sucesso") private boolean success;
    @SerializedName("codigoErro") private String code;
    @SerializedName("tituloErro") private String title;
    @SerializedName("descricaoErro") private String description;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
