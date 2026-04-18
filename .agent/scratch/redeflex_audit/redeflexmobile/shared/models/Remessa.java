package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class Remessa {
    @SerializedName("id") private String id;
    @SerializedName("numero") private String numero;
    @SerializedName("datageracao") private Date datageracao;
    @SerializedName("dataconfirmacao") private Date dataconfirmacao;
    @SerializedName("situacao") private int situacao;
    @SerializedName("listaitem") private ArrayList<RemessaItem> listaitem;

    public String getId() {
        return id;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDatageracao() {
        return datageracao;
    }

    public void setDatageracao(Date datageracao) {
        this.datageracao = datageracao;
    }

    public ArrayList<RemessaItem> getListaitem() {
        return listaitem;
    }

    public void setListaitem(ArrayList<RemessaItem> listaitem) {
        this.listaitem = listaitem;
    }

    public Date getDataconfirmacao() {
        return dataconfirmacao;
    }

    public void setDataconfirmacao(Date dataconfirmacao) {
        this.dataconfirmacao = dataconfirmacao;
    }
}