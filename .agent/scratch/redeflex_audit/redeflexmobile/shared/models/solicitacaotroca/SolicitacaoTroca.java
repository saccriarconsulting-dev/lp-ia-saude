package com.axys.redeflexmobile.shared.models.solicitacaotroca;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SolicitacaoTroca {

    @SerializedName(value = "IdServer", alternate = {"SolicitacaoTrocaId"}) private int idServer;
    @SerializedName(value = "IdAppMobile") private int idApp;
    @SerializedName(value = "DataSolicitacao") private Date dataSolicitacao;
    @SerializedName(value = "VendedorId") private int vendedorId;
    @SerializedName(value = "IdCliente") private String clienteId;
    @SerializedName(value = "Itens", alternate = {"TrocaDetalhes"}) private List<SolicitacaoTrocaDetalhes> produtos;
    private String clienteNome;

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public List<SolicitacaoTrocaDetalhes> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<SolicitacaoTrocaDetalhes> produtos) {
        this.produtos = produtos;
    }
}
