package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 09/02/2017.
 */

public class Iccid {
    private String itemCode;
    private String codigo;
    private String codigoSemVerificador;
    private boolean ativo;
    private String id;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoSemVerificador() {
        return codigoSemVerificador;
    }

    public void setCodigoSemVerificador(String codigoSemVerificador) {
        this.codigoSemVerificador = codigoSemVerificador;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}