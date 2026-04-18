package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 24/02/2017.
 */

public class OrdemServico {
    private int id;
    private int idTipo;
    private String descricaoTipo;
    private int idCliente;
    private String nomeCliente;
    private String obs;
    private Date data;
    private String dataAgend;
    private Date dataLimiteAtend;
    private int tempoSla;
    private int idClasse;
    private String classeSla;
    private String dataCancelamento;

    public String getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(String dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getDescricaoTipo() {
        return descricaoTipo;
    }

    public void setDescricaoTipo(String descricaoTipo) {
        this.descricaoTipo = descricaoTipo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataAgend() {
        return dataAgend;
    }

    public void setDataAgend(String dataAgend) {
        this.dataAgend = dataAgend;
    }

    public Date getDataLimiteAtend() {
        return dataLimiteAtend;
    }

    public void setDataLimiteAtend(Date dataLimiteAtend) {
        this.dataLimiteAtend = dataLimiteAtend;
    }

    public int getTempoSla() {
        return tempoSla;
    }

    public void setTempoSla(int tempoSla) {
        this.tempoSla = tempoSla;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public String getClasseSla() {
        return classeSla;
    }

    public void setClasseSla(String classeSla) {
        this.classeSla = classeSla;
    }
}