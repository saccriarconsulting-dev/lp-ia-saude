package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joao.viana on 11/01/2017.
 */

public class AuditagemCliente {
    private String idProduto;
    private int quantidade;
    private String idCliente;
    private int id;
    private String produto;
    private ArrayList<CodBarra> listaCodigos;
    private boolean confirmada;
    private Date data;
    private String versaoApp;
    private String codigoBarra;
    private String codigoBarraFinal;
    private int idCodigoBarra;
    private boolean importado;

    public int getIdCodigoBarra() {
        return idCodigoBarra;
    }

    public void setIdCodigoBarra(int idCodigoBarra) {
        this.idCodigoBarra = idCodigoBarra;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCodigoBarraFinal() {
        return codigoBarraFinal;
    }

    public void setCodigoBarraFinal(String codigoBarraFinal) {
        this.codigoBarraFinal = codigoBarraFinal;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }

    public ArrayList<CodBarra> getListaCodigos() {
        return listaCodigos;
    }

    public void setListaCodigos(ArrayList<CodBarra> listaCodigos) {
        this.listaCodigos = listaCodigos;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isImportado() {
        return importado;
    }

    public void setImportado(boolean importado) {
        this.importado = importado;
    }
}