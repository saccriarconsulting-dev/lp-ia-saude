package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class RemessaLista {
    private String id_capa;
    private String numero_capa;
    private Date datageracao_capa;
    private Date dataconfirmacao_capa;
    private int situacao_capa;
    private String localArquivo;
    private int situacaoArquivo;

    public int getSituacaoArquivo() {
        return situacaoArquivo;
    }

    public void setSituacaoArquivo(int situacaoArquivo) {
        this.situacaoArquivo = situacaoArquivo;
    }

    private String idItem_item;
    private String itemCodeSAP_item;
    private int qtdRemessa_item;
    private int qtdInformada_item;
    private Date dataConfirmacao_item;
    private String itemDescricao;

    public String getLocalArquivo() {
        return localArquivo;
    }

    public void setLocalArquivo(String localArquivo) {
        this.localArquivo = localArquivo;
    }

    public String getItemDescricao() {
        return itemDescricao;
    }

    public void setItemDescricao(String itemDescricao) {
        this.itemDescricao = itemDescricao;
    }

    public String getId_capa() {
        return id_capa;
    }

    public void setId_capa(String id_capa) {
        this.id_capa = id_capa;
    }

    public String getNumero_capa() {
        return numero_capa;
    }

    public void setNumero_capa(String numero_capa) {
        this.numero_capa = numero_capa;
    }

    public Date getDatageracao_capa() {
        return datageracao_capa;
    }

    public void setDatageracao_capa(Date datageracao_capa) {
        this.datageracao_capa = datageracao_capa;
    }

    public Date getDataconfirmacao_capa() {
        return dataconfirmacao_capa;
    }

    public void setDataconfirmacao_capa(Date dataconfirmacao_capa) {
        this.dataconfirmacao_capa = dataconfirmacao_capa;
    }

    public int getSituacao_capa() {
        return situacao_capa;
    }

    public void setSituacao_capa(int situacao_capa) {
        this.situacao_capa = situacao_capa;
    }

    public String getIdItem_item() {
        return idItem_item;
    }

    public void setIdItem_item(String idItem_item) {
        this.idItem_item = idItem_item;
    }

    public String getItemCodeSAP_item() {
        return itemCodeSAP_item;
    }

    public void setItemCodeSAP_item(String itemCodeSAP_item) {
        this.itemCodeSAP_item = itemCodeSAP_item;
    }

    public int getQtdRemessa_item() {
        return qtdRemessa_item;
    }

    public void setQtdRemessa_item(int qtdRemessa_item) {
        this.qtdRemessa_item = qtdRemessa_item;
    }

    public int getQtdInformada_item() {
        return qtdInformada_item;
    }

    public void setQtdInformada_item(int qtdInformada_item) {
        this.qtdInformada_item = qtdInformada_item;
    }

    public Date getDataConfirmacao_item() {
        return dataConfirmacao_item;
    }

    public void setDataConfirmacao_item(Date dataConfirmacao_item) {
        this.dataConfirmacao_item = dataConfirmacao_item;
    }
}