package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 08/08/2016.
 */
public class MobileCodBarra {
    private int id;
    private int idVendaItem;
    private String idVendedor;
    private int idVenda;
    private Date data;
    private String idCliente;
    private int idVisita;
    private String codigoBarra;
    private String codigoBarraFinal;
    private int quantidade;
    private String idProduto;

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVendaItem() {
        return idVendaItem;
    }

    public void setIdVendaItem(int idVendaItem) {
        this.idVendaItem = idVendaItem;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}