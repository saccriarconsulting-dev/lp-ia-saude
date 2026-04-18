package com.axys.redeflexmobile.shared.models.responses;

/**
 * Created by joao.viana on 04/11/2016.
 */

public class AnexoRetorno {
    private String tipo;
    private String situacao;
    private String id;
    private String idCadastro;
    private String retorno;
    private String cpfCnpj;

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCadastro() {
        return idCadastro;
    }

    public void setIdCadastro(String idCadastro) {
        this.idCadastro = idCadastro;
    }
}