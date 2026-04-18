package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author Rogério Massa on 06/11/18.
 */

public class UltimaVendaCliente {

    @SerializedName(value = "IdCliente", alternate = {"idCliente"}) private Integer idCliente;
    @SerializedName(value = "IdProduto", alternate = {"idProduto"}) private String idProduto;
    @SerializedName(value = "NomeProduto", alternate = {"nomeProduto"}) private String nomeProduto;
    @SerializedName(value = "Operadora", alternate = {"operadora"}) private String operadora;
    @SerializedName(value = "Quantidade", alternate = {"quantidade"}) private int quantidade;
    @SerializedName(value = "Data", alternate = {"data"}) private Date data;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
