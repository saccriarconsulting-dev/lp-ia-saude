package com.axys.redeflexmobile.shared.models;

public class CartaoConsultaRequest {
    private int ClienteId;
    private int ColaboradorId;
    private double Valor;
    private int AppMobileId;
    private int VisitaId;
    private String VersaoApp;
    private String ChaveNSU;

    public CartaoConsultaRequest() {
    }

    public int getClienteId() {
        return ClienteId;
    }

    public void setClienteId(int clienteId) {
        ClienteId = clienteId;
    }

    public int getColaboradorId() {
        return ColaboradorId;
    }

    public void setColaboradorId(int colaboradorId) {
        ColaboradorId = colaboradorId;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public int getAppMobileId() {
        return AppMobileId;
    }

    public void setAppMobileId(int appMobileId) {
        AppMobileId = appMobileId;
    }

    public int getVisitaId() {
        return VisitaId;
    }

    public void setVisitaId(int visitaId) {
        VisitaId = visitaId;
    }

    public String getVersaoApp() {
        return VersaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        VersaoApp = versaoApp;
    }

    public String getChaveNSU() {
        return ChaveNSU;
    }

    public void setChaveNSU(String chaveNSU) {
        ChaveNSU = chaveNSU;
    }
}
