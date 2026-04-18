package com.axys.redeflexmobile.shared.models;

import java.util.Date;

public class ComprovanteSkyTa {

    public static final int NAO_SINCRONIZADO = 0;

    private long id;
    private long idColaborador;
    private long idCliente;
    private int tipo;
    private String anexo;
    private int sync;
    private int idCampanha;
    private Date data;
    private int idMaterial;
    private int interno;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(long idColaborador) {
        this.idColaborador = idColaborador;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(int idCampanha) {
        this.idCampanha = idCampanha;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public int getInterno() {
        return interno;
    }

    public void setInterno(int interno) {
        this.interno = interno;
    }
}
