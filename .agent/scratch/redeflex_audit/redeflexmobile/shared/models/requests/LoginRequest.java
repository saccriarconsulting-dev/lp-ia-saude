package com.axys.redeflexmobile.shared.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * DTO para requisição de login (POST /Authentication).
 * Contém o token Base64(user:pass) e o UUID do dispositivo.
 */
public class LoginRequest {

    @SerializedName("credencial")
    private final String token;

    @SerializedName("uuid")
    private final String uuid;

    public LoginRequest(String token, String uuid) {
        this.token = token;
        this.uuid = uuid;
    }

    /** @return token de autenticação no formato Base64(user:pass) */
    public String getToken() {
        return token;
    }

    /** @return identificador único do dispositivo */
    public String getUuid() {
        return uuid;
    }
}
