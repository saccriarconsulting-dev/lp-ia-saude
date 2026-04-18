package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 17/05/2017.
 */

public class RotaMobile {
    private String idCliente;
    private int diaSemana;
    private int ordem;
    private boolean incluir;
    private int id;
    private int idTipo;
    private int semana;

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public boolean isIncluir() {
        return incluir;
    }

    public void setIncluir(boolean incluir) {
        this.incluir = incluir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}