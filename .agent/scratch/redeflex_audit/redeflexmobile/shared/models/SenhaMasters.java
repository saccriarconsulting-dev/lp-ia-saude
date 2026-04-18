package com.axys.redeflexmobile.shared.models;

/**
 * Created by Vitor Herrmann - Capptan on 07/08/2018.
 */

public class SenhaMasters {

    private String senha;
    private String dataUltimaAtualizacao;

    public SenhaMasters() {
    }

    public SenhaMasters(String senha, String dataUltimaAtualizacao) {
        this.senha = senha;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(String dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}
