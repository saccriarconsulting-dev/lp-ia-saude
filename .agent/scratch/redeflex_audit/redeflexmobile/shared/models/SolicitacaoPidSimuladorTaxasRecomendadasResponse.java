package com.axys.redeflexmobile.shared.models;

public class SolicitacaoPidSimuladorTaxasRecomendadasResponse {
    private int idBandeiraTipo;
    private double taxaDebito;
    private double taxaCredito;
    private double taxaCredito6x;
    private double taxaCredito12x;

    public SolicitacaoPidSimuladorTaxasRecomendadasResponse() {
    }

    public int getIdBandeiraTipo() {
        return idBandeiraTipo;
    }

    public void setIdBandeiraTipo(int idBandeiraTipo) {
        this.idBandeiraTipo = idBandeiraTipo;
    }

    public double getTaxaDebito() {
        return taxaDebito;
    }

    public void setTaxaDebito(double taxaDebito) {
        this.taxaDebito = taxaDebito;
    }

    public double getTaxaCredito() {
        return taxaCredito;
    }

    public void setTaxaCredito(double taxaCredito) {
        this.taxaCredito = taxaCredito;
    }

    public double getTaxaCredito6x() {
        return taxaCredito6x;
    }

    public void setTaxaCredito6x(double taxaCredito6x) {
        this.taxaCredito6x = taxaCredito6x;
    }

    public double getTaxaCredito12x() {
        return taxaCredito12x;
    }

    public void setTaxaCredito12x(double taxaCredito12x) {
        this.taxaCredito12x = taxaCredito12x;
    }
}
