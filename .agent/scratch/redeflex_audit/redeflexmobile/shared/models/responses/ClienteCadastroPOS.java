package com.axys.redeflexmobile.shared.models.responses;

import com.axys.redeflexmobile.shared.services.network.util.JsonExclude;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ClienteCadastroPOS {

    @SerializedName("Id") private Integer id;
    @SerializedName("IdAppMobile") private Integer idAppMobile;
    @JsonExclude private int idClienteCadastro;
    @SerializedName("TipoMaquinaId") private int idTipoMaquina;
    @SerializedName("IdTerminal") private String idTerminal;
    @SerializedName("ValorAluguel") private double valorAluguel;
    @SerializedName("DataSync") private Date dataSync;
    @SerializedName("CpfCnpjCliente") private String cpfCnpjCliente;
    @SerializedName("TipoConexaoId") private Integer tipoConexao;
    @SerializedName("Situacao") private int situacao /* 0: deletado, 1: ativo */;
    @JsonExclude private String posDescricao;
    @JsonExclude private String posModelo;

    @JsonExclude private Integer idOperadora;
    @JsonExclude private Integer metragemCabo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdAppMobile() {
        return idAppMobile;
    }

    public void setIdAppMobile(Integer idAppMobile) {
        this.idAppMobile = idAppMobile;
    }

    public int getIdClienteCadastro() {
        return idClienteCadastro;
    }

    public void setIdClienteCadastro(int idClienteCadastro) {
        this.idClienteCadastro = idClienteCadastro;
    }

    public int getIdTipoMaquina() {
        return idTipoMaquina;
    }

    public void setIdTipoMaquina(int idTipoMaquina) {
        this.idTipoMaquina = idTipoMaquina;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Date getDataSync() {
        return dataSync;
    }

    public void setDataSync(Date dataSync) {
        this.dataSync = dataSync;
    }

    public Integer getTipoConexao() {
        return tipoConexao;
    }

    public void setTipoConexao(Integer tipoConexao) {
        this.tipoConexao = tipoConexao;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public String getPosDescricao() {
        return posDescricao;
    }

    public void setPosDescricao(String posDescricao) {
        this.posDescricao = posDescricao;
    }

    public String getPosModelo() {
        return posModelo;
    }

    public void setPosModelo(String posModelo) {
        this.posModelo = posModelo;
    }

    public String getCpfCnpjCliente() {
        return cpfCnpjCliente;
    }

    public void setCpfCnpjCliente(String cpfCnpjCliente) {
        this.cpfCnpjCliente = cpfCnpjCliente;
    }

    public Integer getIdOperadora() {
        return idOperadora;
    }

    public void setIdOperadora(Integer idOperadora) {
        this.idOperadora = idOperadora;
    }

    public Integer getMetragemCabo() {
        return metragemCabo;
    }

    public void setMetragemCabo(Integer metragemCabo) {
        this.metragemCabo = metragemCabo;
    }
}
