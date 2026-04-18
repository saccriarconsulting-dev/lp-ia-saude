package com.axys.redeflexmobile.shared.models.responses;

/**
 * Created by joao.viana on 10/03/2017.
 */

public class Departamento {
    private int Id;
    private String Descricao;
    private boolean Ativo;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public boolean isAtivo() {
        return Ativo;
    }

    public void setAtivo(boolean ativo) {
        Ativo = ativo;
    }
}