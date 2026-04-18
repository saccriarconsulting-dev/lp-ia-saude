package com.axys.redeflexmobile.shared.models.responses;

/**
 * Created by joao.viana on 06/10/2016.
 */

public class ProjetoTradeItens {
    private String id;
    private String idProjeto;
    private String idProduto;
    private String descProduto;
    private int qtd;
    private Double preco;
    private String situacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(String idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescProduto() {
        return descProduto;
    }

    public void setDescProduto(String descProduto) {
        this.descProduto = descProduto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}