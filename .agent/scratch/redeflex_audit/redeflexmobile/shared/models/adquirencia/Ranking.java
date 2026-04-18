package com.axys.redeflexmobile.shared.models.adquirencia;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rogério Massa on 22/11/18.
 */

public class Ranking {

    @SerializedName("idCliente") private int clientId;
    @SerializedName("latitude") private Double latitude;
    @SerializedName("longitude") private Double longitude;
    @SerializedName("nomeFantasia") private String fantasyName;
    @SerializedName("faturamentoRealizado") private Double revenue;
    @SerializedName("faturamentoEsperado") private Double expectedRevenue;
    @SerializedName("farol") private Double status;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public Integer getStatus() {
        return (int) Math.round(status);
    }

    public void setStatus(Double status) {
        this.status = status;
    }
}
