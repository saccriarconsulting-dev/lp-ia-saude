package com.axys.redeflexmobile.shared.models.login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("uuid") String uuid;
    @SerializedName("credencial") String credential;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
