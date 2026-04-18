package com.axys.redeflexmobile.shared.models.migracao;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class MotiveMigrationSub {
    private int id;
    private String descricao;
    private boolean ativo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
