package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 13/10/2017.
 */

public class RetBoleto {
    private boolean Erro;
    private String Mensagem;

    public boolean isErro() {
        return Erro;
    }

    public void setErro(boolean erro) {
        Erro = erro;
    }

    public String getMensagem() {
        return Mensagem;
    }

    public void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }
}