package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 09/05/2017.
 */

public class Cadastro {
    public static final String CADASTRO_ALTERADO = "S";
    private boolean exibeSenha;
    private String alterado;

    public boolean isExibeSenha() {
        return exibeSenha;
    }

    public void setExibeSenha(boolean exibeSenha) {
        this.exibeSenha = exibeSenha;
    }

    public String getAlterado() {
        return alterado;
    }

    public void setAlterado(String alterado) {
        this.alterado = alterado;
    }
}