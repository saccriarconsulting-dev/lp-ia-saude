package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class SugestaoVenda {

    @SerializedName("id") private long id;
    @SerializedName("idCliente") private long idCliente;
    @SerializedName("Vd") private int vendaDia;
    @SerializedName("Em") private int estoqueMinimo;
    @SerializedName("Es") private int estoqueSeguranca;
    @SerializedName("grupoProdutoSAP") private int grupoProduto;
    @SerializedName("operadora") private String operadora;
    @SerializedName("operadoraId") private int idOperadora;
    @SerializedName("estoqueIdeal") private int estoqueIdeal;
    @SerializedName("situacaoCliente") private String situacaocliente;
    @SerializedName("descricaoGrupo") private String descricaogrupo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public int getVendaDia() {
        return vendaDia;
    }

    public void setVendaDia(int vendaDia) {
        this.vendaDia = vendaDia;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getEstoqueSeguranca() {
        return estoqueSeguranca;
    }

    public void setEstoqueSeguranca(int estoqueSeguranca) {
        this.estoqueSeguranca = estoqueSeguranca;
    }

    public int getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(int grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public int getIdOperadora() {
        return idOperadora;
    }

    public void setIdOperadora(int idOperadora) {
        this.idOperadora = idOperadora;
    }

    public int getEstoqueIdeal() {
        return estoqueIdeal;
    }

    public void setEstoqueIdeal(int estoqueIdeal) {
        this.estoqueIdeal = estoqueIdeal;
    }

    public String getSituacaocliente() {
        return situacaocliente;
    }

    public void setSituacaocliente(String situacaocliente) {
        this.situacaocliente = situacaocliente;
    }
    public String getDescricaogrupo() {
        return descricaogrupo;
    }

    public void setDescricaogrupo(String descricaogrupo) {
        this.descricaogrupo = descricaogrupo;
    }

}
