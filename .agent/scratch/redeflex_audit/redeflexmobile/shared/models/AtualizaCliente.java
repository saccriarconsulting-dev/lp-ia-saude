package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AtualizaCliente {

    @SerializedName("idCliente") private String id;
    @SerializedName("idVendedor") private String idVendedor;
    @SerializedName("nomeFantasia") private String nomeFantasia;
    @SerializedName("razaoSocial") private String razaoSocial;
    @SerializedName("idSegmentoSGV") private String segmento;
    @SerializedName("contato") private String nomeContato;
    @SerializedName("dddTelefone") private String dddTelefone;
    @SerializedName("telefone") private String telefone;
    @SerializedName("dddCelular") private String dddCelular;
    @SerializedName("celular") private String celular;
    @SerializedName("email") private String email;
    @SerializedName("tipoLogradouro") private String tipoLogradouro;
    @SerializedName("nomeLogradouro") private String nomeLogradouro;
    @SerializedName("numeroLogradouro") private String numeroLogradouro;
    @SerializedName("complementoLogradouro") private String complementoLogradouro;
    @SerializedName("pontoReferencia") private String pontoReferencia;
    @SerializedName("bairro") private String bairro;
    @SerializedName("cidade") private String cidade;
    @SerializedName("estado") private String estado;
    @SerializedName("cep") private String cep;
    @SerializedName("dataHora") private Date dataHora;
    private int sync;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getDddTelefone() {
        return dddTelefone;
    }

    public void setDddTelefone(String dddTelefone) {
        this.dddTelefone = dddTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDddCelular() {
        return dddCelular;
    }

    public void setDddCelular(String dddCelular) {
        this.dddCelular = dddCelular;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public String getNumeroLogradouro() {
        return numeroLogradouro;
    }

    public void setNumeroLogradouro(String numeroLogradouro) {
        this.numeroLogradouro = numeroLogradouro;
    }

    public String getComplementoLogradouro() {
        return complementoLogradouro;
    }

    public void setComplementoLogradouro(String complementoLogradouro) {
        this.complementoLogradouro = complementoLogradouro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    /**
     *  Retorna um valor do enum EnumAtualizaCliente
     *
     * @return EnumAtualizarCliente;
     */
    public int getStatus() {
        return status;
    }

    /**
     *  Passar um valor do EnumAtualizaCliente
     *
     * @param status EnumAtualizarCliente
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
