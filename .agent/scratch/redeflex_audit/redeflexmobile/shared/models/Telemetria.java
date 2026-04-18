package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.shared.util.Util_IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Desenvolvimento on 10/05/2016.
 */
public class Telemetria {
    private int id;
    private int idVendedor;
    private String modeloAparelho;
    private String versaoOs;
    private String versaoApp;
    private String imei;
    private int bateria;
    private String tipoInternet;
    private double latitude;
    private double longitude;
    private double precisao;
    private Date dataGps;
    private int qtdVisitaPend;
    private int qtdVendaPend;
    private int qtdVendaItensPend;
    private int qtdCodBarraPend;
    private int qtdCadCliPend;
    private int qtdDocCliPend;
    private int qtdLocCliPend;
    private int qtdRemessaPend;
    private int qtdItemRemessaPend;

    public int getQtdCadCliPend() {
        return qtdCadCliPend;
    }

    public void setQtdCadCliPend(int qtdCadCliPend) {
        this.qtdCadCliPend = qtdCadCliPend;
    }

    public int getQtdDocCliPend() {
        return qtdDocCliPend;
    }

    public void setQtdDocCliPend(int qtdDocCliPend) {
        this.qtdDocCliPend = qtdDocCliPend;
    }

    public int getQtdLocCliPend() {
        return qtdLocCliPend;
    }

    public void setQtdLocCliPend(int qtdLocCliPend) {
        this.qtdLocCliPend = qtdLocCliPend;
    }

    public int getQtdRemessaPend() {
        return qtdRemessaPend;
    }

    public void setQtdRemessaPend(int qtdRemessaPend) {
        this.qtdRemessaPend = qtdRemessaPend;
    }

    public int getQtdItemRemessaPend() {
        return qtdItemRemessaPend;
    }

    public void setQtdItemRemessaPend(int qtdItemRemessaPend) {
        this.qtdItemRemessaPend = qtdItemRemessaPend;
    }

    public int getQtdVendaPend() {
        return qtdVendaPend;
    }

    public void setQtdVendaPend(int qtdVendaPend) {
        this.qtdVendaPend = qtdVendaPend;
    }

    public int getQtdVisitaPend() {
        return qtdVisitaPend;
    }

    public void setQtdVisitaPend(int qtdVisitaPend) {
        this.qtdVisitaPend = qtdVisitaPend;
    }

    public int getQtdVendaItensPend() {
        return qtdVendaItensPend;
    }

    public void setQtdVendaItensPend(int qtdVendaItensPend) {
        this.qtdVendaItensPend = qtdVendaItensPend;
    }

    public int getQtdCodBarraPend() {
        return qtdCodBarraPend;
    }

    public void setQtdCodBarraPend(int qtdCodBarraPend) {
        this.qtdCodBarraPend = qtdCodBarraPend;
    }

    @Override
    public String toString() {
        JSONObject main = new JSONObject();
        try {
            main.put("idVendedor", getIdVendedor());
            main.put("modeloAparelho", getModeloAparelho());
            main.put("versaoOs", getVersaoOs());
            main.put("versaoApp", getVersaoApp());
            main.put("imei", getImei());
            main.put("bateria", getBateria());
            main.put("tipoInternet", getTipoInternet());
            main.put("latitude", getLatitude());
            main.put("longitude", getLongitude());
            main.put("precisao", getPrecisao());
            main.put("dataGps", Util_IO.dateTimeToString(getDataGps(), "yyyy-MM-dd HH:mm:ss"));
            main.put("qtdVisitaPend", getQtdVisitaPend());
            main.put("qtdVendaPend", getQtdVendaPend());
            main.put("qtdVendaItensPend", getQtdVendaItensPend());
            main.put("qtdCodBarraPend", getQtdCodBarraPend());
            return main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getModeloAparelho() {
        return modeloAparelho;
    }

    public void setModeloAparelho(String modeloAparelho) {
        this.modeloAparelho = modeloAparelho;
    }

    public String getVersaoOs() {
        return versaoOs;
    }

    public void setVersaoOs(String versaoOs) {
        this.versaoOs = versaoOs;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
    }

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public String getTipoInternet() {
        return tipoInternet;
    }

    public void setTipoInternet(String tipoInternet) {
        this.tipoInternet = tipoInternet;
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

    public Date getDataGps() {
        return dataGps;
    }

    public void setDataGps(Date dataGps) {
        this.dataGps = dataGps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}