package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 07/03/2017.
 */

public class Chamado {
    private int Id;
    private Integer ChamadoID;
    private Integer FilialID;
    private Integer DepartamentoID;
    private Integer SolicitanteID;
    private String Solicitante;
    private String Assunto;
    private int StatusID;
    private Date DataCadastro;
    private Date DataAlteracao;
    private Integer IdAppMobile;
    private Date DataAppMobile;
    private boolean Sync;
    private String Responsavel;
    private Integer ResponsavelID;
    private Integer IdCliente;
    private Date DataAgendamento;
    private String nomeFantasiaCliente;

    public Date getDataAgendamento() {
        return DataAgendamento;
    }

    public void setDataAgendamento(Date dataAgendamento) {
        DataAgendamento = dataAgendamento;
    }

    public Integer getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(Integer idCliente) {
        IdCliente = idCliente;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Integer getChamadoID() {
        return ChamadoID;
    }

    public void setChamadoID(Integer chamadoID) {
        ChamadoID = chamadoID;
    }

    public Integer getFilialID() {
        return FilialID;
    }

    public void setFilialID(Integer filialID) {
        FilialID = filialID;
    }

    public Integer getDepartamentoID() {
        return DepartamentoID;
    }

    public void setDepartamentoID(Integer departamentoID) {
        DepartamentoID = departamentoID;
    }

    public Integer getSolicitanteID() {
        return SolicitanteID;
    }

    public void setSolicitanteID(Integer solicitanteID) {
        SolicitanteID = solicitanteID;
    }

    public String getSolicitante() {
        return Solicitante;
    }

    public void setSolicitante(String solicitante) {
        Solicitante = solicitante;
    }

    public String getAssunto() {
        return Assunto;
    }

    public void setAssunto(String assunto) {
        Assunto = assunto;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public Date getDataCadastro() {
        return DataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        DataCadastro = dataCadastro;
    }

    public Date getDataAlteracao() {
        return DataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        DataAlteracao = dataAlteracao;
    }

    public Integer getIdAppMobile() {
        return IdAppMobile;
    }

    public void setIdAppMobile(Integer idAppMobile) {
        IdAppMobile = idAppMobile;
    }

    public Date getDataAppMobile() {
        return DataAppMobile;
    }

    public void setDataAppMobile(Date dataAppMobile) {
        DataAppMobile = dataAppMobile;
    }

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }

    public String getResponsavel() {
        return Responsavel;
    }

    public void setResponsavel(String responsavel) {
        Responsavel = responsavel;
    }

    public Integer getResponsavelID() {
        return ResponsavelID;
    }

    public void setResponsavelID(Integer responsavelID) {
        ResponsavelID = responsavelID;
    }

    public String getNomeFantasiaCliente() {
        return nomeFantasiaCliente;
    }

    public void setNomeFantasiaCliente(String nomeFantasiaCliente) {
        this.nomeFantasiaCliente = nomeFantasiaCliente;
    }

    public String retornaSituacao() {
        String situacao = "";
        switch (this.StatusID) {
            case 1:
                situacao = "Aguardando Atendimento";
                break;
            case 2:
                situacao = "Em Atendimento";
                break;
            case 3:
                situacao = "Fila";
                break;
            case 4:
                situacao = "Aguardando Solicitante";
                break;
            case 5:
                situacao = "Aguardando Terceiros";
                break;
            case 6:
                situacao = "Encerrado";
                break;
            case 7:
                situacao = "Cancelado";
                break;
            case 8:
                situacao = "Projeto";
                break;
        }

        return situacao;
    }
}