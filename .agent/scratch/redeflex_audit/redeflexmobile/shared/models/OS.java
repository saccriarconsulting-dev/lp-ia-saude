package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by Desenvolvimento on 09/02/2016.
 */
public class OS extends OrdemServico {
    private Date dataVisualizacao;
    private Date dataAgendamento;
    private Date dataAtendimento;
    private boolean visualizacaoSync;
    private boolean agendamentoSync;
    private boolean atendimentoSync;
    private String obsVendedor;

    public String getObsVendedor() {
        return obsVendedor;
    }

    public boolean isVisualizacaoSync() {
        return visualizacaoSync;
    }

    public void setVisualizacaoSync(boolean visualizacaoSync) {
        this.visualizacaoSync = visualizacaoSync;
    }

    public void setObsVendedor(String obsVendedor) {
        this.obsVendedor = obsVendedor;
    }

    public Date getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(Date dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public Date getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(Date dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public boolean isAgendamentoSync() {
        return agendamentoSync;
    }

    public void setAgendamentoSync(boolean agendamentoSync) {
        this.agendamentoSync = agendamentoSync;
    }

    public boolean isAtendimentoSync() {
        return atendimentoSync;
    }

    public void setAtendimentoSync(boolean atendimentoSync) {
        this.atendimentoSync = atendimentoSync;
    }
}