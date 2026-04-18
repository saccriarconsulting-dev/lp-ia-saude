package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 19/08/2016.
 */
public class RetornoVenda {
    private int id;
    private int idVenda;
    private int idVendaItem;
    private int quantidade;
    private int idPreco;

    public int getIdPreco() {
        return idPreco;
    }

    public void setIdPreco(int idPreco) {
        this.idPreco = idPreco;
    }

    public int getIdVendaItem() {
        return idVendaItem;
    }

    public void setIdVendaItem(int idVendaItem) {
        this.idVendaItem = idVendaItem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }
}