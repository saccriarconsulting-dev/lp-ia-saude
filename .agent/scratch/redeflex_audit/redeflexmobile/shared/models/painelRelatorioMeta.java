package com.axys.redeflexmobile.shared.models;

/**
 * Created by Desenvolvimento on 20/05/2016.
 */
public class painelRelatorioMeta {
    private String texto;
    private int meta;
    private int realizado;
    private int idoperadora;
    private boolean isIndicador;
    private boolean isHeader;
    private double tendencia;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    public boolean isIndicador() {
        return isIndicador;
    }

    public void setIndicador(boolean indicador) {
        isIndicador = indicador;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public int getIdoperadora() {
        return idoperadora;
    }

    public void setIdoperadora(int idoperadora) {
        this.idoperadora = idoperadora;
    }

    public double getTendencia() {
        return tendencia;
    }

    public void setTendencia(double tendencia) {
        this.tendencia = tendencia;
    }
}