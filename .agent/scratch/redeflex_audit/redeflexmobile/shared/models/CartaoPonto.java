package com.axys.redeflexmobile.shared.models;

import java.util.Date;

public class CartaoPonto {
    private int id;
    private Date horario;
    private double latitude;
    private double longitude;
    private double precisao;
    private boolean sync;

    public CartaoPonto() {
    }

    public CartaoPonto(int id, Date horario, double latitude, double longitude, double precisao, boolean sync) {
        this.id = id;
        this.horario = horario;
        this.latitude = latitude;
        this.longitude = longitude;
        this.precisao = precisao;
        this.sync = sync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
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

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
