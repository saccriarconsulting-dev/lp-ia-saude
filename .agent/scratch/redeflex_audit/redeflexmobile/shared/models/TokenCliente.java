package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 21/09/2017.
 */

public class TokenCliente {
    private String id;
    private String idCliente;
    private String token;
    private Integer TipoToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTipoToken() {
        return TipoToken;
    }

    public void setTipoToken(Integer tipoToken) {
        TipoToken = tipoToken;
    }
}