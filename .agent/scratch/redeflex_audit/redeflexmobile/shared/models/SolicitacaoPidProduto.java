package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SolicitacaoPidProduto implements Serializable {

    @SerializedName("IdAppMobile") private Integer Id;
    @SerializedName("IdAppMobileSol") private Integer IdSolicitacaoPid;
    private Integer BandeiraTipoId;
    private Integer TaxaDebito;
    private Integer TaxaCredito;
    private Integer TaxaCredito6x;
    private Integer TaxaCredito12x;
    private Integer TaxaRavAutomatica;
    private Integer TaxaRavEventual;
    private Integer Aluguel;

    public SolicitacaoPidProduto() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getIdSolicitacaoPid() {
        return IdSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(Integer idSolicitacaoPid) {
        IdSolicitacaoPid = idSolicitacaoPid;
    }

    public Integer getBandeiraTipoId() {
        return BandeiraTipoId;
    }

    public void setBandeiraTipoId(Integer bandeiraTipoId) {
        BandeiraTipoId = bandeiraTipoId;
    }

    public Integer getTaxaDebito() {
        return TaxaDebito;
    }

    public void setTaxaDebito(Integer taxaDebito) {
        TaxaDebito = taxaDebito;
    }

    public Integer getTaxaCredito() {
        return TaxaCredito;
    }

    public void setTaxaCredito(Integer taxaCredito) {
        TaxaCredito = taxaCredito;
    }

    public Integer getTaxaCredito6x() {
        return TaxaCredito6x;
    }

    public void setTaxaCredito6x(Integer taxaCredito6x) {
        TaxaCredito6x = taxaCredito6x;
    }

    public Integer getTaxaCredito12x() {
        return TaxaCredito12x;
    }

    public void setTaxaCredito12x(Integer taxaCredito12x) {
        TaxaCredito12x = taxaCredito12x;
    }

    public Integer getTaxaRavAutomatica() {
        return TaxaRavAutomatica;
    }

    public void setTaxaRavAutomatica(Integer taxaRavAutomatica) {
        TaxaRavAutomatica = taxaRavAutomatica;
    }

    public Integer getTaxaRavEventual() {
        return TaxaRavEventual;
    }

    public void setTaxaRavEventual(Integer taxaRavEventual) {
        TaxaRavEventual = taxaRavEventual;
    }

    public Integer getAluguel() {
        return Aluguel;
    }

    public void setAluguel(Integer aluguel) {
        Aluguel = aluguel;
    }
}
