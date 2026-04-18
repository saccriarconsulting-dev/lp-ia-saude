package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 14/07/2017.
 */

public class Preco {
    private String idPreco;
    private Double valor;
    private int quantidade;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdPreco() {
        return idPreco;
    }

    public void setIdPreco(String idPreco) {
        this.idPreco = idPreco;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}