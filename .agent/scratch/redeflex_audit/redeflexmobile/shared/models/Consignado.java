package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Consignado {
    @SerializedName("IdAppMobile") private int Id;
    private int IdVendedor;
    private String TipoConsignado;
    private int IdCliente;
    private int IdServer;
    private int IdVisita;
    private Date DataEmissao;
    private double ValorTotal;
    private Date DataSync;
    private double  Latitude;
    private double Longitude;
    private double Precisao;
    private String VersaoApp;
    private int IdConsignadoRefer;
    private String Status;
    private List<ConsignadoItem> itens;

    public Consignado() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        IdVendedor = idVendedor;
    }

    public String getTipoConsignado() {
        return TipoConsignado;
    }

    public void setTipoConsignado(String tipoConsignado) {
        TipoConsignado = tipoConsignado;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public int getIdServer() {
        return IdServer;
    }

    public void setIdServer(int idServer) {
        IdServer = idServer;
    }

    public int getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(int idVisita) {
        IdVisita = idVisita;
    }

    public Date getDataEmissao() {
        return DataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        DataEmissao = dataEmissao;
    }

    public double getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(double valorTotal) {
        ValorTotal = valorTotal;
    }

    public Date getDataSync() {
        return DataSync;
    }

    public void setDataSync(Date dataSync) {
        DataSync = dataSync;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getPrecisao() {
        return Precisao;
    }

    public void setPrecisao(double precisao) {
        Precisao = precisao;
    }

    public String getVersaoApp() {
        return VersaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        VersaoApp = versaoApp;
    }

    public int getIdConsignadoRefer() {
        return IdConsignadoRefer;
    }

    public void setIdConsignadoRefer(int idConsignadoRefer) {
        IdConsignadoRefer = idConsignadoRefer;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<ConsignadoItem> getItens() {
        return itens;
    }

    public void setItens(List<ConsignadoItem> itens) {
        this.itens = itens;
    }
}
