package com.axys.redeflexmobile.shared.models.adquirencia;


import com.google.gson.annotations.SerializedName;

public class GenericResponse {
    @SerializedName(value = "code", alternate = {"codigoErro"}) private String code;
    @SerializedName(value = "message", alternate = {"descricaoErro", "Message"}) private String message;
    @SerializedName(value = "title", alternate = {"tituloErro"}) private String title;

    public GenericResponse() {
    }

    public GenericResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public GenericResponse(String code, String message, String title) {
        this.code = code;
        this.message = message;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }
}
