package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReenviaSenhaCliente {

    @SerializedName("idVendedor") private int idVendedor;
    @SerializedName("idCliente") private int idCliente;
    @SerializedName("latitude") private Double latitude;
    @SerializedName("longitude") private Double longitude;
    @SerializedName("precisao") private Double precisao;
    @SerializedName("contatos") private List<String> contatos;

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrecisao() {
        return precisao;
    }

    public void setPrecisao(double precisao) {
        this.precisao = precisao;
    }

    public List<String> getContatos() {
        return contatos;
    }

    public void setContatos(List<String> contatos) {
        this.contatos = contatos;
    }
}
