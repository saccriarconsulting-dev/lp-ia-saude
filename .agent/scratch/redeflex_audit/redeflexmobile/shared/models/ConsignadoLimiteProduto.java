package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class ConsignadoLimiteProduto {
    private int Id;
    private int IdConsignadoLimiteCliente;
    @SerializedName("ProdutoSapId") private String IdProduto;
    private int Quantidade;

    public ConsignadoLimiteProduto() {
    }

    public ConsignadoLimiteProduto(int id, int idConsignadoLimiteCliente, String idProduto, int quantidade) {
        this.Id = id;
        this.IdConsignadoLimiteCliente = idConsignadoLimiteCliente;
        this.IdProduto = idProduto;
        this.Quantidade = quantidade;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getIdConsignadoLimiteCliente() {
        return IdConsignadoLimiteCliente;
    }

    public void setIdConsignadoLimiteCliente(int idConsignadoLimiteCliente) {
        this.IdConsignadoLimiteCliente = idConsignadoLimiteCliente;
    }

    public String getIdProduto() {
        return IdProduto;
    }

    public void setIdProduto(String idProduto) {
        this.IdProduto = idProduto;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.Quantidade = quantidade;
    }
}
