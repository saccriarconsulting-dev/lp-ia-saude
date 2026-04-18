package com.axys.redeflexmobile.shared.models.venda;

import java.util.Date;

public class ProdutoMerchandising {
    private int id;
    private String nome;
    private int operadora;
    private Date data;

    public ProdutoMerchandising() {
    }

    public ProdutoMerchandising(int id, String nome, int operadora, Date data) {
        this.id = id;
        this.nome = nome;
        this.operadora = operadora;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getOperadora() {
        return operadora;
    }

    public void setOperadora(int operadora) {
        this.operadora = operadora;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
