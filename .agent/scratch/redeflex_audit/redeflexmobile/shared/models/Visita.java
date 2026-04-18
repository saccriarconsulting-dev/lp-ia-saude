package com.axys.redeflexmobile.shared.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Desenvolvimento on 01/07/2016.
 */
public class Visita implements Serializable {
    private int id;
    private int idServer;
    private String idVendedor;
    private String idClienteSGV;
    private String nomeFantasia;
    private int idMotivo;
    private Date dataFim;
    private Date dataInicio;
    private String idCliente;
    private String versaoApp;
    private double latitude;
    private double longitude;
    private double precisao;
    private String idClienteIntraFlex;
    private String idProjetoTrade;
    private String codigoExibirCliente;
    private double distancia;
    private int origem;

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getCodigoExibirCliente() {
        return codigoExibirCliente;
    }

    public void setCodigoExibirCliente(String codigoExibirCliente) {
        this.codigoExibirCliente = codigoExibirCliente;
    }

    public String getIdProjetoTrade() {
        return idProjetoTrade;
    }

    public void setIdProjetoTrade(String idProjetoTrade) {
        this.idProjetoTrade = idProjetoTrade;
    }

    public String getIdClienteIntraFlex() {
        return idClienteIntraFlex;
    }

    public void setIdClienteIntraFlex(String idClienteIntraFlex) {
        this.idClienteIntraFlex = idClienteIntraFlex;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdClienteSGV() {
        return idClienteSGV;
    }

    public void setIdClienteSGV(String idClienteSGV) {
        this.idClienteSGV = idClienteSGV;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public int getOrigem() {
        return origem;
    }

    public void setOrigem(int origem) {
        this.origem = origem;
    }
}