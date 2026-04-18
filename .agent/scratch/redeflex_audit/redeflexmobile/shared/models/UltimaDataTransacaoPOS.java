package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 06/11/18.
 */

public class UltimaDataTransacaoPOS {

    @SerializedName("Id") private Integer id;
    @SerializedName("UltimaTransacaoPOSId") private Integer ultimaTransacaoPOSId;
    @SerializedName("IdTerminal") private String idTerminal;
    @SerializedName("IdCliente") private Integer idCliente;
    @SerializedName("DataTransacao") private Date dataTransacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        id = id;
    }

    public Integer getUltimaTransacaoPOSId() {
        return ultimaTransacaoPOSId;
    }

    public void setUltimaTransacaoPOSId(Integer ultimaTransacaoPOSId) {
        this.ultimaTransacaoPOSId = ultimaTransacaoPOSId;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
}
