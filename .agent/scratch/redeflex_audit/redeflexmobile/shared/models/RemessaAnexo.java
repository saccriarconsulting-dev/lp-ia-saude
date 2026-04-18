package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 07/12/2016.
 */

public class RemessaAnexo {
    private String localArquivo;
    private String idRemessa;
    private String numeroRemessa;
    private String situacao;

    public String getNumeroRemessa() {
        return numeroRemessa;
    }

    public void setNumeroRemessa(String numeroRemessa) {
        this.numeroRemessa = numeroRemessa;
    }

    public String getIdRemessa() {
        return idRemessa;
    }

    public void setIdRemessa(String idRemessa) {
        this.idRemessa = idRemessa;
    }

    public String getLocalArquivo() {
        return localArquivo;
    }

    public void setLocalArquivo(String localArquivo) {
        this.localArquivo = localArquivo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}