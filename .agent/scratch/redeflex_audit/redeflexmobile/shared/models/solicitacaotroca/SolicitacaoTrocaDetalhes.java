package com.axys.redeflexmobile.shared.models.solicitacaotroca;

import com.axys.redeflexmobile.shared.util.Util_IO;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SolicitacaoTrocaDetalhes {

    @SerializedName(value = "IdServer", alternate = {"SolicitacaoTrocaDetalhesId"}) private int idServer;
    @SerializedName("IdAppMobile") private int idApp;
    @SerializedName("CodigoItem") private String produtoCodigo;
    @SerializedName("Qtde") private int qtde;
    @SerializedName(value = "QtdeTrocada", alternate = {"QuantidadeTrocada"}) private int qtdeTrocado;
    @SerializedName("ValorUN") private double valorUnitario;
    @SerializedName("ValorTotalItem") private double valorTotal;
    @SerializedName("StatusId") private int statusId;
    @SerializedName("DataAlteracao") private Date alteracaoData;
    @SerializedName("DataTroca") private Date dataTroca;
    @SerializedName("MotivoId") private int motivoId;
    @SerializedName(value = "CodBarras", alternate = {"TrocaCodBarras"}) private List<SolicitacaoTrocaCodBarras> iccids;
    private int solicitacaoTrocaId;
    private String produtoNome;
    private Date syncDate;
    private int sync;

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public int getSolicitacaoTrocaId() {
        return solicitacaoTrocaId;
    }

    public void setSolicitacaoTrocaId(int solicitacaoTrocaId) {
        this.solicitacaoTrocaId = solicitacaoTrocaId;
    }

    public String getProdutoCodigo() {
        return produtoCodigo;
    }

    public void setProdutoCodigo(String produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public int getQtdeTrocado() {
        return qtdeTrocado;
    }

    public void setQtdeTrocado(int qtdeTrocado) {
        this.qtdeTrocado = qtdeTrocado;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = Util_IO.converterValorParaDoisDecimaisDouble(valorUnitario);
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getMotivoId() {
        return motivoId;
    }

    public void setMotivoId(int motivoId) {
        this.motivoId = motivoId;
    }

    public Date getAlteracaoData() {
        if (dataTroca != null) {
            return dataTroca;
        }
        return alteracaoData;
    }

    public void setAlteracaoData(Date alteracaoData) {
        this.alteracaoData = alteracaoData;
    }

    public List<SolicitacaoTrocaCodBarras> getIccids() {
        return iccids;
    }

    public void setIccids(List<SolicitacaoTrocaCodBarras> iccids) {
        this.iccids = iccids;
    }

    public Date getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }
}
