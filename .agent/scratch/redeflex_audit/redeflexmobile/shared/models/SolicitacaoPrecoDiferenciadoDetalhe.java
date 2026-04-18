package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class SolicitacaoPrecoDiferenciadoDetalhe {
    @SerializedName("IdAppMobile") private int Id;
    private int IdSolicitacao;
    private int IdServer;
    private int DDD;
    private int IdCliente;
    private String CodigoClienteSGV;
    private String ItemCode;
    private int Quantidade;
    private double PrecoVenda;
    private double PrecoDiferenciado;
    private int TipoPagamentoId;
    private int StatusId;
    private int IdVendedor;
    private String MotivoRecusada;
    private String Aplicada;
    private String Justificativa;

    public SolicitacaoPrecoDiferenciadoDetalhe() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdSolicitacao() {
        return IdSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        IdSolicitacao = idSolicitacao;
    }

    public int getIdServer() {
        return IdServer;
    }

    public void setIdServer(int idServer) {
        IdServer = idServer;
    }

    public int getDDD() {
        return DDD;
    }

    public void setDDD(int DDD) {
        this.DDD = DDD;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public String getCodigoClienteSGV() {
        return CodigoClienteSGV;
    }

    public void setCodigoClienteSGV(String codigoClienteSGV) {
        CodigoClienteSGV = codigoClienteSGV;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        Quantidade = quantidade;
    }

    public double getPrecoVenda() {
        return PrecoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        PrecoVenda = precoVenda;
    }

    public double getPrecoDiferenciado() {
        return PrecoDiferenciado;
    }

    public void setPrecoDiferenciado(double precoDiferenciado) {
        PrecoDiferenciado = precoDiferenciado;
    }

    public int getTipoPagamentoId() {
        return TipoPagamentoId;
    }

    public void setTipoPagamentoId(int tipoPagamentoId) {
        TipoPagamentoId = tipoPagamentoId;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public int getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        IdVendedor = idVendedor;
    }

    public String getMotivoRecusada() {
        return MotivoRecusada;
    }

    public void setMotivoRecusada(String motivoRecusada) {
        MotivoRecusada = motivoRecusada;
    }

    public String getAplicada() {
        return Aplicada;
    }

    public void setAplicada(String aplicada) {
        Aplicada = aplicada;
    }

    public String getJustificativa() {
        return Justificativa;
    }

    public void setJustificativa(String justificativa) {
        Justificativa = justificativa;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }
}
