package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;

/**
 * Created by joao.viana on 31/08/2017.
 */

public class DevolucaoItens {
    private int id;
    private int idDevolucao;
    private String idProduto;
    private int quantidade;
    private ArrayList<DevolucaoICCID> listICCID;

    public ArrayList<DevolucaoICCID> getListICCID() {
        return listICCID;
    }

    public void setListICCID(ArrayList<DevolucaoICCID> listICCID) {
        this.listICCID = listICCID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDevolucao() {
        return idDevolucao;
    }

    public void setIdDevolucao(int idDevolucao) {
        this.idDevolucao = idDevolucao;
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
}