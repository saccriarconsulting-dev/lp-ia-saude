package com.axys.redeflexmobile.shared.models;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class TaxasAdquirencia implements ICustomSpinnerDialogModel {

    private String id;
    private String ramoAtividade;
    private String mcc;
    private String situacao;
    private double minDebito;
    private double minCreditoAVista;
    private double minAte6;
    private double minMaior6;
    private double tabDebito;
    private double tabCreditoAVista;
    private double tabAte6;
    private double tabMaior6;
    private double maxDebito;
    private double maxCreditoAVista;
    private double maxAte6;
    private double maxMaior6;
    private Integer tipo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRamoAtividade() {
        return ramoAtividade;
    }

    public void setRamoAtividade(String ramoAtividade) {
        this.ramoAtividade = ramoAtividade;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public double getMinDebito() {
        return minDebito;
    }

    public void setMinDebito(double minDebito) {
        this.minDebito = minDebito;
    }

    public double getMinCreditoAVista() {
        return minCreditoAVista;
    }

    public void setMinCreditoAVista(double minCreditoAVista) {
        this.minCreditoAVista = minCreditoAVista;
    }

    public double getMinAte6() {
        return minAte6;
    }

    public void setMinAte6(double minAte6) {
        this.minAte6 = minAte6;
    }

    public double getMinMaior6() {
        return minMaior6;
    }

    public void setMinMaior6(double minMaior6) {
        this.minMaior6 = minMaior6;
    }

    public double getTabDebito() {
        return tabDebito;
    }

    public void setTabDebito(double tabDebito) {
        this.tabDebito = tabDebito;
    }

    public double getTabCreditoAVista() {
        return tabCreditoAVista;
    }

    public void setTabCreditoAVista(double tabCreditoAVista) {
        this.tabCreditoAVista = tabCreditoAVista;
    }

    public double getTabAte6() {
        return tabAte6;
    }

    public void setTabAte6(double tabAte6) {
        this.tabAte6 = tabAte6;
    }

    public double getTabMaior6() {
        return tabMaior6;
    }

    public void setTabMaior6(double tabMaior6) {
        this.tabMaior6 = tabMaior6;
    }

    public double getMaxDebito() {
        return maxDebito;
    }

    public void setMaxDebito(double maxDebito) {
        this.maxDebito = maxDebito;
    }

    public double getMaxCreditoAVista() {
        return maxCreditoAVista;
    }

    public void setMaxCreditoAVista(double maxCreditoAVista) {
        this.maxCreditoAVista = maxCreditoAVista;
    }

    public double getMaxAte6() {
        return maxAte6;
    }

    public void setMaxAte6(double maxAte6) {
        this.maxAte6 = maxAte6;
    }

    public double getMaxMaior6() {
        return maxMaior6;
    }

    public void setMaxMaior6(double maxMaior6) {
        this.maxMaior6 = maxMaior6;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    @Override
    public Integer getIdValue() {
        return Integer.parseInt(mcc);
    }

    @Override
    public String getDescriptionValue() {
        return String.format("%s - %s", mcc, ramoAtividade);
    }
}