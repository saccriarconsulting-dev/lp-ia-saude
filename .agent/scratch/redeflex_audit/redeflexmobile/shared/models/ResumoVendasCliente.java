package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ResumoVendasCliente {
    @SerializedName("IDCliente") private Integer idCliente;
    @SerializedName("Produto") private String produto;
    @SerializedName("Quantidade") private Integer quantidade;
    @SerializedName("ValorFace") private double valorface;
    @SerializedName("ValorTotal") private double valortotal;
    @SerializedName("DataVenda") private Date datavenda;
    @SerializedName("Tipo") private String tipo;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorface() {
        return valorface;
    }

    public void setValorface(double valorface) {
        this.valorface = valorface;
    }

    public double getValortotal() {
        return valortotal;
    }

    public void setValortotal(double valortotal) {
        this.valortotal = valortotal;
    }

    public Date getDatavenda() {
        return datavenda;
    }

    public void setDatavenda(Date datavenda) {
        this.datavenda = datavenda;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
