package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class SolicitacaoPidPos implements Serializable {
    @Expose(serialize = false) private Integer Id;
    @Expose(serialize = false) private Integer IdSolicitacaoPid;
    private Integer TipoMaquinaId;
    private double ValorAluguel;
    private Integer TipoConexaoId;
    private Integer IdOperadora;
    private Integer MetragemCabo;
    private Integer Quantidade;
    private Integer Situacao;

    public SolicitacaoPidPos() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getIdSolicitacaoPid() {
        return IdSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(Integer idSolicitacaoPid) {
        IdSolicitacaoPid = idSolicitacaoPid;
    }

    public Integer getTipoMaquinaId() {
        return TipoMaquinaId;
    }

    public void setTipoMaquinaId(Integer tipoMaquinaId) {
        TipoMaquinaId = tipoMaquinaId;
    }

    public double getValorAluguel() {
        return ValorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        ValorAluguel = valorAluguel;
    }

    public Integer getTipoConexaoId() {
        return TipoConexaoId;
    }

    public void setTipoConexaoId(Integer tipoConexaoId) {
        TipoConexaoId = tipoConexaoId;
    }

    public Integer getIdOperadora() {
        return IdOperadora;
    }

    public void setIdOperadora(Integer idOperadora) {
        IdOperadora = idOperadora;
    }

    public Integer getMetragemCabo() {
        return MetragemCabo;
    }

    public void setMetragemCabo(Integer metragemCabo) {
        MetragemCabo = metragemCabo;
    }

    public Integer getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        Quantidade = quantidade;
    }

    public Integer getSituacao() {
        return Situacao;
    }

    public void setSituacao(Integer situacao) {
        Situacao = situacao;
    }
}
