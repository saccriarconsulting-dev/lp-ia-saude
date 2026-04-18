package com.axys.redeflexmobile.shared.util.exception;

import com.google.gson.annotations.SerializedName;

/**
 * Created by capptan on 29/06/17.
 */

@SuppressWarnings("all")
public class MessageException {

    @SerializedName(value = "title", alternate = {"tituloErro"}) private String title;
    @SerializedName(value = "message", alternate = {"descricaoErro"}) private String message;
    private int messageResId;
    private String code;

    public MessageException() {
    }

    public MessageException(String code, int messageResId) {
        this.code = code;
        this.messageResId = messageResId;
    }

    public MessageException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageException(String code, String message, String title) {
        this.title = title;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageResId() {
        return messageResId;
    }

    public void setMessageResId(int messageResId) {
        this.messageResId = messageResId;
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
}
