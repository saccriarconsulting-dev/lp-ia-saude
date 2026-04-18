package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 10/10/2017.
 */

public class EstruturaProd {
    private int id;
    private String itemPai;
    private String itemFilho;
    private int qtd;
    private double proporcao;
    private boolean ativo;

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public double getProporcao() {
        return proporcao;
    }

    public void setProporcao(double proporcao) {
        this.proporcao = proporcao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemPai() {
        return itemPai;
    }

    public void setItemPai(String itemPai) {
        this.itemPai = itemPai;
    }

    public String getItemFilho() {
        return itemFilho;
    }

    public void setItemFilho(String itemFilho) {
        this.itemFilho = itemFilho;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}