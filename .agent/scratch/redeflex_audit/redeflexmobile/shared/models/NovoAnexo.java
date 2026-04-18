package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 15/03/2017.
 */

public class NovoAnexo {
    private String nomeArquivo;
    private String localArquivo;

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getLocalArquivo() {
        return localArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void setLocalArquivo(String localArquivo) {
        this.localArquivo = localArquivo;
    }

    public String getTipoArquivo() {
        if (nomeArquivo.isEmpty())
            return "";

        if (nomeArquivo.toLowerCase().contains("jpg"))
            return "image/jpeg";
        else if (nomeArquivo.toLowerCase().contains("png"))
            return "image/png";
        else
            return "image";
    }
}