package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SolicitacaoPidAnexo implements Serializable {
    @SerializedName("IdAppMobile") private Integer Id;
    private Integer IdSolicitacaoPid;
    private String Tipo;
    private String Anexo;
    private String NomeArquivo;
    private String TipoArquivo;
    private double Latitude;
    private double Longitude;
    private double Precisao;
    private Integer Sync;
    private String Tamanhoarquivo;
    private String Imagem;

    public SolicitacaoPidAnexo() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getIdSolicitacaoPid() {
        return IdSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(Integer idSolicitacaoPid) {
        IdSolicitacaoPid = idSolicitacaoPid;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getAnexo() {
        return Anexo;
    }

    public void setAnexo(String anexo) {
        Anexo = anexo;
    }

    public String getNomeArquivo() {
        return NomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        NomeArquivo = nomeArquivo;
    }

    public String getTipoArquivo() {
        return TipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        TipoArquivo = tipoArquivo;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getPrecisao() {
        return Precisao;
    }

    public void setPrecisao(double precisao) {
        Precisao = precisao;
    }

    public Integer getSync() {
        return Sync;
    }

    public void setSync(Integer sync) {
        Sync = sync;
    }

    public String getTamanhoarquivo() {
        return Tamanhoarquivo;
    }

    public void setTamanhoarquivo(String tamanhoarquivo) {
        Tamanhoarquivo = tamanhoarquivo;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }
}
