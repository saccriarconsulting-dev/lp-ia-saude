package com.axys.redeflexmobile.shared.models.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("code") int code;
    @SerializedName("message") String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
