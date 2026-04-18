package com.axys.redeflexmobile.shared.models;

import java.util.Optional;

public class SolicitacaoPidSimuladorTaxaBandeiraRequest {
    private int IdBandeiraTipo;
    private Double TaxaDebito;
    private Double TaxaCredito;
    private Double TaxaCredito6x;
    private Double TaxaCredito12x;

    public SolicitacaoPidSimuladorTaxaBandeiraRequest() {
    }

    public int getIdBandeiraTipo() {
        return IdBandeiraTipo;
    }

    public void setIdBandeiraTipo(int idBandeiraTipo) {
        IdBandeiraTipo = idBandeiraTipo;
    }

    public Optional<Double> getTaxaDebito() {
        return Optional.ofNullable(TaxaDebito);
    }

    public void setTaxaDebito(Double taxaDebito) {
        TaxaDebito = taxaDebito;
    }

    public Optional<Double> getTaxaCredito() {
        return Optional.ofNullable(TaxaCredito);
    }

    public void setTaxaCredito(Double taxaCredito) {
        TaxaCredito = taxaCredito;
    }

    public Optional<Double> getTaxaCredito6x() {
        return Optional.ofNullable(TaxaCredito6x);
    }

    public void setTaxaCredito6x(Double taxaCredito6x) {
        TaxaCredito6x = taxaCredito6x;
    }

    public Optional<Double> getTaxaCredito12x()
    {
        return Optional.ofNullable(TaxaCredito12x);
    }

    public void setTaxaCredito12x(Double taxaCredito12x) {
        TaxaCredito12x = taxaCredito12x;
    }
}
