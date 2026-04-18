package com.axys.redeflexmobile.shared.models;

public class CartaoPontoRequest {
    private int idMobile;
    private int idVendedor;
    private String data;
    private double latitude;
    private double longitude;
    private double precisao;

    public CartaoPontoRequest() {
    }

    public CartaoPontoRequest(int idMobile, int idVendedor, String data, double latitude, double longitude, double precisao) {
        this.idMobile = idMobile;
        this.idVendedor = idVendedor;
        this.data = data;
        this.latitude = latitude;
        this.longitude = longitude;
        this.precisao = precisao;
    }

    public int getIdMobile() {
        return idMobile;
    }

    public void setIdMobile(int idMobile) {
        this.idMobile = idMobile;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
}
