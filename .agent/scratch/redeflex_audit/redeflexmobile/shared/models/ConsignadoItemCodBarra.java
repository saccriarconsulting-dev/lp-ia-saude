package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConsignadoItemCodBarra implements Serializable {

    @SerializedName("IdAppMobileBarra") private int Id;
    private int IdConsignado;
    private int IdConsignadoItem;
    private int IdServer;
    private String CodigoBarraIni;
    private String CodigoBarraFim;
    private int Qtd;

    public ConsignadoItemCodBarra() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdConsignado() {
        return IdConsignado;
    }

    public void setIdConsignado(int idConsignado) {
        IdConsignado = idConsignado;
    }

    public int getIdConsignadoItem() {
        return IdConsignadoItem;
    }

    public void setIdConsignadoItem(int idConsignadoItem) {
        IdConsignadoItem = idConsignadoItem;
    }

    public int getIdServer() {
        return IdServer;
    }

    public void setIdServer(int idServer) {
        IdServer = idServer;
    }

    public String getCodigoBarraIni() {
        return CodigoBarraIni;
    }

    public void setCodigoBarraIni(String codigoBarraIni) {
        CodigoBarraIni = codigoBarraIni;
    }

    public String getCodigoBarraFim() {
        return CodigoBarraFim;
    }

    public void setCodigoBarraFim(String codigoBarraFim) {
        CodigoBarraFim = codigoBarraFim;
    }

    public int getQtd() {
        return Qtd;
    }

    public void setQtd(int qtd) {
        Qtd = qtd;
    }
}
