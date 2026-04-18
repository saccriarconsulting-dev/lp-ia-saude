package com.axys.redeflexmobile.shared.models.responses;

import java.util.List;

/**
 * @author Lucas Marciano on 27/02/2020
 */
public class Pendencia {
    private String id;
    private String pendenciaId;
    private String descricao;
    private boolean ativo;

    private List<PendenciaMotivo> motivos;

    public Pendencia() {
    }

    public Pendencia(String id, String pendenciaId, String descricao, boolean ativo) {
        this.id = id;
        this.pendenciaId = pendenciaId;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPendenciaId() {
        return pendenciaId;
    }

    public void setPendenciaId(String pendenciaId) {
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

    public List<PendenciaMotivo> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<PendenciaMotivo> motivos) {
        this.motivos = motivos;
    }
}
