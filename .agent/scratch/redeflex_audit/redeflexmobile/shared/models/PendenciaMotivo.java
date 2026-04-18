package com.axys.redeflexmobile.shared.models;

/**
 * @author Lucas Marciano on 27/02/2020
 */
public class PendenciaMotivo {
    private String id;
    private int pendenciaId;
    private int pendenciaMotivoId;
    private String descricao;
    private boolean ativo;

    public PendenciaMotivo(String id, int pendenciaId, int pendenciaMotivoId, String descricao, boolean ativo) {
        this.id = id;
        this.pendenciaId = pendenciaId;
        this.pendenciaMotivoId = pendenciaMotivoId;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public PendenciaMotivo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPendenciaMotivoId() {
        return pendenciaMotivoId;
    }

    public void setPendenciaMotivoId(int pendenciaMotivoId) {
        this.pendenciaMotivoId = pendenciaMotivoId;
    }

    public int getPendenciaId() {
        return pendenciaId;
    }

    public void setPendenciaId(int pendenciaId) {
        this.pendenciaId = pendenciaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
