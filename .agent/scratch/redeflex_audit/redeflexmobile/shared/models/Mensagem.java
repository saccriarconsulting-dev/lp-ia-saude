package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by Desenvolvimento on 03/06/2016.
 */
public class Mensagem {
    private int id;
    private String texto;
    private Date data;
    private Date dataVisualizacao;
    private Boolean permiteDeletar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(Date dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public Boolean getPermiteDeletar() {
        return permiteDeletar;
    }

    public void setPermiteDeletar(Boolean permiteDeletar) {
        this.permiteDeletar = permiteDeletar;
    }
}