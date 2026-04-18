package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joao.viana on 29/08/2016.
 */
public class Cep {
    @SerializedName("CodigoBairro") private String codigoBairro;
    @SerializedName("CodigoIBGE") private String codigoIBGE;
    @SerializedName("CodigoLocalidade") private String codigoLocalidade;
    @SerializedName("ComplementoLogradouro") private String complementoLogradouro;
    @SerializedName("DDD") private String ddd;
    @SerializedName("Latitude") private String latitude;
    @SerializedName("Longitude") private String longitude;
    @SerializedName("Mensagem") private String mensagem;
    @SerializedName("NivelCEP") private String nivelCEP;
    @SerializedName("NomeBairro") private String nomeBairro;
    @SerializedName("NomeLocalidade") private String nomeLocalidade;
    @SerializedName("NomeLogradouro") private String nomeLogradouro;
    @SerializedName("NomeMunicipio") private String nomeMunicipio;
    @SerializedName("TipoLogradouro") private String tipoLogradouro;
    @SerializedName("TipoLocalidade") private String tipoLocalidade;
    @SerializedName("UF") private String uf;

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public String getCodigoBairro() {
        return codigoBairro;
    }

    public void setCodigoBairro(String codigoBairro) {
        this.codigoBairro = codigoBairro;
    }

    public String getCodigoIBGE() {
        return codigoIBGE;
    }

    public void setCodigoIBGE(String codigoIBGE) {
        this.codigoIBGE = codigoIBGE;
    }

    public String getCodigoLocalidade() {
        return codigoLocalidade;
    }

    public void setCodigoLocalidade(String codigoLocalidade) {
        this.codigoLocalidade = codigoLocalidade;
    }

    public String getComplementoLogradouro() {
        return complementoLogradouro;
    }

    public void setComplementoLogradouro(String complementoLogradouro) {
        this.complementoLogradouro = complementoLogradouro;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNivelCEP() {
        return nivelCEP;
    }

    public void setNivelCEP(String nivelCEP) {
        this.nivelCEP = nivelCEP;
    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }

    public String getNomeLocalidade() {
        return nomeLocalidade;
    }

    public void setNomeLocalidade(String nomeLocalidade) {
        this.nomeLocalidade = nomeLocalidade;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public String getTipoLocalidade() {
        return tipoLocalidade;
    }

    public void setTipoLocalidade(String tipoLocalidade) {
        this.tipoLocalidade = tipoLocalidade;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}