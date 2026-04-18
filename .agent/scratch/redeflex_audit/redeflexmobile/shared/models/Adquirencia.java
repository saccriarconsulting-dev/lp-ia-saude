package com.axys.redeflexmobile.shared.models;

/**
 * @author Vitor Herrmann on 03/01/19.
 */
public class Adquirencia {

    private long idIndicador;
    private String indicador;
    private int meta;
    private int realizado;
    private int falta;
    private String ativo;

    public Adquirencia() {
    }

    public Adquirencia(long idIndicador, String indicador, int meta, int realizado, int falta, String ativo) {
        this.idIndicador = idIndicador;
        this.indicador = indicador;
        this.meta = meta;
        this.realizado = realizado;
        this.falta = falta;
        this.ativo = ativo;
    }

    public long getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(long idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public int getRealizado() {
        return realizado;
    }

    public void setRealizado(int realizado) {
        this.realizado = realizado;
    }

    public int getFalta() {
        return falta;
    }

    public void setFalta(int falta) {
        this.falta = falta;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
}
