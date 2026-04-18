package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joao.viana on 17/10/2017.
 */

public class HeardCodBarra {
    private int idVendaItem;
    private int idVenda;
    private String idProduto;
    private String idProdutoSelecionado;
    private String nomeProduto;
    private int quantidade;
    private Date dataVenda;
    private double valorUN;
    private ArrayList<CodBarra> listaItens;
    private int qtdCombo;
    private ItemVendaCombo itemVendaCombo;

    public ItemVendaCombo getItemVendaCombo() {
        return itemVendaCombo;
    }

    public void setItemVendaCombo(ItemVendaCombo itemVendaCombo) {
        this.itemVendaCombo = itemVendaCombo;
    }

    public int getQtdCombo() {
        return qtdCombo;
    }

    public void setQtdCombo(int qtdCombo) {
        this.qtdCombo = qtdCombo;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public double getValorUN() {
        return valorUN;
    }

    public void setValorUN(double valorUN) {
        this.valorUN = valorUN;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getIdVendaItem() {
        return idVendaItem;
    }

    public void setIdVendaItem(int idVendaItem) {
        this.idVendaItem = idVendaItem;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdProdutoSelecionado() {
        return idProdutoSelecionado;
    }

    public void setIdProdutoSelecionado(String idProdutoSelecionado) {
        this.idProdutoSelecionado = idProdutoSelecionado;
    }

    public ArrayList<CodBarra> getListaItens() {
        return listaItens;
    }

    public void setListaItens(ArrayList<CodBarra> listaItens) {
        this.listaItens = listaItens;
    }
}