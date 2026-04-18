package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SolicitacaoPrecoDiferenciado implements Serializable {
    @SerializedName("IdAppMobile") private int Id;
    private int IdServerSolicitacao;
    @SerializedName("UsuarioCriacaoexterno") private int IdVendedor;
    private String NomeSolicitante;
    private Date DataSolicitacao;
    private Date DataInicial;
    private Date DataFinal;
    private int SituacaoId;
    private int TipoId;
    private Date DataCriacao;
    private Date DataAtualizacao;
    private int Sync;
    private Date DataSincronizacao;

    private ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> ItensPrecoDif;

    public SolicitacaoPrecoDiferenciado() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdServerSolicitacao() {
        return IdServerSolicitacao;
    }

    public void setIdServerSolicitacao(int idServerSolicitacao) {
        IdServerSolicitacao = idServerSolicitacao;
    }

    public int getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        IdVendedor = idVendedor;
    }

    public Date getDataSolicitacao() {
        return DataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        DataSolicitacao = dataSolicitacao;
    }

    public Date getDataInicial() {
        return DataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        DataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return DataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        DataFinal = dataFinal;
    }

    public int getSituacaoId() {
        return SituacaoId;
    }

    public void setSituacaoId(int situacaoId) {
        SituacaoId = situacaoId;
    }

    public int getTipoId() {
        return TipoId;
    }

    public void setTipoId(int tipoId) {
        TipoId = tipoId;
    }

    public Date getDataCriacao() {
        return DataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        DataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return DataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        DataAtualizacao = dataAtualizacao;
    }

    public int getSync() {
        return Sync;
    }

    public void setSync(int sync) {
        Sync = sync;
    }

    public Date getDataSincronizacao() {
        return DataSincronizacao;
    }

    public void setDataSincronizacao(Date dataSincronizacao) {
        DataSincronizacao = dataSincronizacao;
    }

    public ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> getItensPrecoDif() {
        return ItensPrecoDif;
    }

    public void setItensPrecoDif(ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> itensPrecoDif) {
        ItensPrecoDif = itensPrecoDif;
    }

    public String getNomeSolicitante() {
        return NomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        NomeSolicitante = nomeSolicitante;
    }
}
