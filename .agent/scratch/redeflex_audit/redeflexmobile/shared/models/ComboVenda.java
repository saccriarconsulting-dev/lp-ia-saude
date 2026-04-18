package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 03/11/2017.
 */

public class ComboVenda {
    private String idProduto;
    private int quantidade;
    private int id;
    private long idItemVenda;
    private int idVenda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(long idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

}