package com.axys.redeflexmobile.shared.models.responses;

/**
 * Created by Desenvolvimento on 23/05/2016.
 */
public class RelatorioMeta {
    private Integer idIndicador;
    private String indicador;
    private Integer idOperadora;
    private String Operadora;
    private int meta;
    private int realizado;
    private String ativo;
    private double tendencia;

    public Integer getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(Integer idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public Integer getIdOperadora() {
        return idOperadora;
    }

    public void setIdOperadora(Integer idOperadora) {
        this.idOperadora = idOperadora;
    }

    public String getOperadora() {
        return Operadora;
    }

    public void setOperadora(String operadora) {
        Operadora = operadora;
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

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public double getTendencia() {
        return tendencia;
    }

    public void setTendencia(double tendencia) {
        this.tendencia = tendencia;
    }
}