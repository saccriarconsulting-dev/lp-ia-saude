package com.axys.redeflexmobile.shared.models.solicitacaotroca;

import com.google.gson.annotations.SerializedName;

public class SolicitacaoTrocaMotivo {

    @SerializedName("IdServer") private int idServer;
    @SerializedName("MotivoId") private String motivoId;
    @SerializedName("Descricao") private String descricao;
    @SerializedName("Ativo") private boolean ativo;

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public String getMotivoId() {
        return motivoId;
    }

    public void setMotivoId(String motivoId) {
        this.motivoId = motivoId;
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
