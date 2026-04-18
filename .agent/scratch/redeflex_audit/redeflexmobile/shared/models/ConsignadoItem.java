package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsignadoItem implements Serializable {
    @SerializedName("IdAppMobileItem") private int Id;
    private int IdConsignado;
    private int IdServer;
    private String IdProduto;
    private int Qtd;
    private double ValorUnit;
    private double ValorTotalItem;
    private String NomeProduto;
    private int QtdVendido;
    private int QtdAuditado;
    private int QtdDevolvido;
    public static final long  serialVersionUID = 100L;

    @SerializedName("codigobarras") private ArrayList<ConsignadoItemCodBarra> ListaCodigoBarra;
    private ArrayList<CodBarra> ListaType_CodBarra;

    public ConsignadoItem() {
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

    public int getIdServer() {
        return IdServer;
    }

    public void setIdServer(int idServer) {
        IdServer = idServer;
    }

    public String getIdProduto() {
        return IdProduto;
    }

    public void setIdProduto(String idProduto) {
        IdProduto = idProduto;
    }

    public int getQtd() {
        return Qtd;
    }

    public void setQtd(int qtd) {
        Qtd = qtd;
    }

    public double getValorUnit() {
        return ValorUnit;
    }

    public void setValorUnit(double valorUnit) {
        ValorUnit = valorUnit;
    }

    public double getValorTotalItem() {
        return ValorTotalItem;
    }

    public void setValorTotalItem(double valorTotalItem) {
        ValorTotalItem = valorTotalItem;
    }

    public String getNomeProduto() {
        return NomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        NomeProduto = nomeProduto;
    }

    public int getQtdVendido() {
        return QtdVendido;
    }

    public void setQtdVendido(int qtdVendido) {
        QtdVendido = qtdVendido;
    }

    public int getQtdAuditado() {
        return QtdAuditado;
    }

    public void setQtdAuditado(int qtdAuditado) {
        QtdAuditado = qtdAuditado;
    }

    public int getQtdDevolvido() {
        return QtdDevolvido;
    }

    public void setQtdDevolvido(int qtdDevolvido) {
        QtdDevolvido = qtdDevolvido;
    }

    public ArrayList<ConsignadoItemCodBarra> getListaCodigoBarra() {
        return ListaCodigoBarra;
    }

    public void setListaCodigoBarra(ArrayList<ConsignadoItemCodBarra> listaCodigoBarra) {
        ListaCodigoBarra = listaCodigoBarra;
    }

    public ArrayList<CodBarra> getListaType_CodBarra() {
        return ListaType_CodBarra;
    }

    public void setListaType_CodBarra(ArrayList<CodBarra> listaType_CodBarra) {
        ListaType_CodBarra = listaType_CodBarra;
    }
}
