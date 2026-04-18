package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Optional;

public class SolicitacaoPidTaxaMDR  implements Serializable {
    @SerializedName("IdMobile")
    private Integer Id;
    @SerializedName("IdSolicitacaoPidMobile")
    private Integer IdSolicitacaoPid;
    private Integer BandeiraTipoId;
    private Double TaxaDebito;
    private Double TaxaCredito;
    private Double TaxaCredito6x;
    private Double TaxaCredito12x;
    private Double TaxaRavAutomatica;
    private Double TaxaRavEventual;
    private Double TaxaDebitoConcorrente;
    private Double TaxaCreditoConcorrente;
    private Double TaxaCredito6xConcorrente;
    private Double TaxaCredito12xConcorrente;
    private Double TaxaRavAutomaticaConcorrente;
    private Double TaxaRavEventualConcorrente;
    private Double TaxaDebitoContraproposta;
    private Double TaxaCreditoContraproposta;
    private Double TaxaCredito6xContraproposta;
    private Double TaxaCredito12xContraproposta;
    private Double TaxaRavAutomaticaContraproposta;
    private Double TaxaRavEventualContraproposta;
    private Double TaxaSimuladorTPVDebito;
    private Double TaxaSimuladorTPVCredito;
    private Double TaxaSimuladorTPVCredito6x;
    private Double TaxaSimuladorTPVCredito12x;
    private Double TaxaSimuladorIntercambioDebito;
    private Double TaxaSimuladorIntercambioCredito;
    private Double TaxaSimuladorIntercambioCredito6x;
    private Double TaxaSimuladorIntercambioCredito12x;
    private Double TaxaSimuladorNetMDRDebito;
    private Double TaxaSimuladorNetMDRCredito;
    private Double TaxaSimuladorNetMDRCredito6x;
    private Double TaxaSimuladorNetMDRCredito12x;

    private Double TaxaDebitoRecomendada;
    private Double TaxaCreditoRecomendada;
    private Double TaxaCredito6xRecomendada;
    private Double TaxaCredito12xRecomendada;

    public SolicitacaoPidTaxaMDR() {
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

    public Optional<Double> getTaxaCredito12x() {
        return Optional.ofNullable(TaxaCredito12x);
    }

    public void setTaxaCredito12x(Double taxaCredito12x) {
        TaxaCredito12x = taxaCredito12x;
    }

    public Optional<Double> getTaxaRavAutomatica() {
        return Optional.ofNullable(TaxaRavAutomatica);
    }

    public void setTaxaRavAutomatica(double taxaRavAutomatica) {
        TaxaRavAutomatica = taxaRavAutomatica;
    }

    public Optional<Double> getTaxaRavEventual() {
        return Optional.ofNullable(TaxaRavEventual);
    }

    public void setTaxaRavEventual(double taxaRavEventual) {
        TaxaRavEventual = taxaRavEventual;
    }

    public Optional<Double> getTaxaDebitoConcorrente() {
        return Optional.ofNullable(TaxaDebitoConcorrente);
    }

    public void setTaxaDebitoConcorrente(Double taxaDebitoConcorrente) {
        TaxaDebitoConcorrente = taxaDebitoConcorrente;
    }

    public Optional<Double> getTaxaCreditoConcorrente() {
        return Optional.ofNullable(TaxaCreditoConcorrente);
    }

    public void setTaxaCreditoConcorrente(Double taxaCreditoConcorrente) {
        TaxaCreditoConcorrente = taxaCreditoConcorrente;
    }

    public Optional<Double> getTaxaCredito6xConcorrente() {
        return Optional.ofNullable(TaxaCredito6xConcorrente);
    }

    public void setTaxaCredito6xConcorrente(Double taxaCredito6xConcorrente) {
        TaxaCredito6xConcorrente = taxaCredito6xConcorrente;
    }

    public Optional<Double> getTaxaCredito12xConcorrente() {
        return Optional.ofNullable(TaxaCredito12xConcorrente);
    }

    public void setTaxaCredito12xConcorrente(Double taxaCredito12xConcorrente) {
        TaxaCredito12xConcorrente = taxaCredito12xConcorrente;
    }

    public Optional<Double> getTaxaRavAutomaticaConcorrente() {
        return Optional.ofNullable(TaxaRavAutomaticaConcorrente);
    }

    public void setTaxaRavAutomaticaConcorrente(double taxaRavAutomaticaConcorrente) {
        TaxaRavAutomaticaConcorrente = taxaRavAutomaticaConcorrente;
    }

    public Optional<Double> getTaxaRavEventualConcorrente() {
        return Optional.ofNullable(TaxaRavEventualConcorrente);
    }

    public void setTaxaRavEventualConcorrente(double taxaRavEventualConcorrente) {
        TaxaRavEventualConcorrente = taxaRavEventualConcorrente;
    }

    public Optional<Double> getTaxaDebitoContraproposta() {
        return Optional.ofNullable(TaxaDebitoContraproposta);
    }

    public void setTaxaDebitoContraproposta(Double taxaDebitoContraproposta) {
        TaxaDebitoContraproposta = taxaDebitoContraproposta;
    }

    public Optional<Double> getTaxaCreditoContraproposta() {
        return Optional.ofNullable(TaxaCreditoContraproposta);
    }

    public void setTaxaCreditoContraproposta(Double taxaCreditoContraproposta) {
        TaxaCreditoContraproposta = taxaCreditoContraproposta;
    }

    public Optional<Double> getTaxaCredito6xContraproposta() {
        return Optional.ofNullable(TaxaCredito6xContraproposta);
    }

    public void setTaxaCredito6xContraproposta(Double taxaCredito6xContraproposta) {
        TaxaCredito6xContraproposta = taxaCredito6xContraproposta;
    }

    public Optional<Double> getTaxaCredito12xContraproposta() {
        return Optional.ofNullable(TaxaCredito12xContraproposta);
    }

    public void setTaxaCredito12xContraproposta(Double taxaCredito12xContraproposta) {
        TaxaCredito12xContraproposta = taxaCredito12xContraproposta;
    }

    public Optional<Double> getTaxaRavAutomaticaContraproposta() {
        return Optional.ofNullable(TaxaRavAutomaticaContraproposta);
    }

    public void setTaxaRavAutomaticaContraproposta(double taxaRavAutomaticaContraproposta) {
        TaxaRavAutomaticaContraproposta = taxaRavAutomaticaContraproposta;
    }

    public Optional<Double> getTaxaRavEventualContraproposta() {
        return Optional.ofNullable(TaxaRavEventualContraproposta);
    }

    public void setTaxaRavEventualContraproposta(double taxaRavEventualContraproposta) {
        TaxaRavEventualContraproposta = taxaRavEventualContraproposta;
    }

    public Optional<Double> getTaxaSimuladorTPVDebito() {
        return Optional.ofNullable(TaxaSimuladorTPVDebito);
    }

    public void setTaxaSimuladorTPVDebito(double taxaSimuladorTPVDebito) {
        TaxaSimuladorTPVDebito = taxaSimuladorTPVDebito;
    }

    public Optional<Double> getTaxaSimuladorTPVCredito() {
        return Optional.ofNullable(TaxaSimuladorTPVCredito);
    }

    public void setTaxaSimuladorTPVCredito(double taxaSimuladorTPVCredito) {
        TaxaSimuladorTPVCredito = taxaSimuladorTPVCredito;
    }

    public Optional<Double> getTaxaSimuladorTPVCredito6x() {
        return Optional.ofNullable(TaxaSimuladorTPVCredito6x);
    }

    public void setTaxaSimuladorTPVCredito6x(double taxaSimuladorTPVCredito6x) {
        TaxaSimuladorTPVCredito6x = taxaSimuladorTPVCredito6x;
    }

    public Optional<Double> getTaxaSimuladorTPVCredito12x() {
        return Optional.ofNullable(TaxaSimuladorTPVCredito12x);
    }

    public void setTaxaSimuladorTPVCredito12x(double taxaSimuladorTPVCredito12x) {
        TaxaSimuladorTPVCredito12x = taxaSimuladorTPVCredito12x;
    }

    public Optional<Double> getTaxaSimuladorIntercambioDebito() {
        return Optional.ofNullable(TaxaSimuladorIntercambioDebito);
    }

    public void setTaxaSimuladorIntercambioDebito(double taxaSimuladorIntercambioDebito) {
        TaxaSimuladorIntercambioDebito = taxaSimuladorIntercambioDebito;
    }

    public Optional<Double> getTaxaSimuladorIntercambioCredito() {
        return Optional.ofNullable(TaxaSimuladorIntercambioCredito);
    }

    public void setTaxaSimuladorIntercambioCredito(double taxaSimuladorIntercambioCredito) {
        TaxaSimuladorIntercambioCredito = taxaSimuladorIntercambioCredito;
    }

    public Optional<Double> getTaxaSimuladorIntercambioCredito6x() {
        return Optional.ofNullable(TaxaSimuladorIntercambioCredito6x);
    }

    public void setTaxaSimuladorIntercambioCredito6x(double taxaSimuladorIntercambioCredito6x) {
        TaxaSimuladorIntercambioCredito6x = taxaSimuladorIntercambioCredito6x;
    }

    public Optional<Double> getTaxaSimuladorIntercambioCredito12x() {
        return Optional.ofNullable(TaxaSimuladorIntercambioCredito12x);
    }

    public void setTaxaSimuladorIntercambioCredito12x(double taxaSimuladorIntercambioCredito12x) {
        TaxaSimuladorIntercambioCredito12x = taxaSimuladorIntercambioCredito12x;
    }

    public Optional<Double> getTaxaSimuladorNetMDRDebito() {
        return Optional.ofNullable(TaxaSimuladorNetMDRDebito);
    }

    public void setTaxaSimuladorNetMDRDebito(double taxaSimuladorNetMDRDebito) {
        TaxaSimuladorNetMDRDebito = taxaSimuladorNetMDRDebito;
    }

    public Optional<Double> getTaxaSimuladorNetMDRCredito() {
        return Optional.ofNullable(TaxaSimuladorNetMDRCredito);
    }

    public void setTaxaSimuladorNetMDRCredito(double taxaSimuladorNetMDRCredito) {
        TaxaSimuladorNetMDRCredito = taxaSimuladorNetMDRCredito;
    }

    public Optional<Double> getTaxaSimuladorNetMDRCredito6x() {
        return Optional.ofNullable(TaxaSimuladorNetMDRCredito6x);
    }

    public void setTaxaSimuladorNetMDRCredito6x(double taxaSimuladorNetMDRCredito6x) {
        TaxaSimuladorNetMDRCredito6x = taxaSimuladorNetMDRCredito6x;
    }

    public Optional<Double> getTaxaSimuladorNetMDRCredito12x() {
        return Optional.ofNullable(TaxaSimuladorNetMDRCredito12x);
    }

    public void setTaxaSimuladorNetMDRCredito12x(double taxaSimuladorNetMDRCredito12x) {
        TaxaSimuladorNetMDRCredito12x = taxaSimuladorNetMDRCredito12x;
    }

    public Optional<Double> getTaxaDebitoRecomendada() {
        return Optional.ofNullable(TaxaDebitoRecomendada);
    }

    public void setTaxaDebitoRecomendada(double taxaDebitoRecomendada) {
        TaxaDebitoRecomendada = taxaDebitoRecomendada;
    }

    public Optional<Double> getTaxaCreditoRecomendada() {
        return Optional.ofNullable(TaxaCreditoRecomendada);
    }

    public void setTaxaCreditoRecomendada(double taxaCreditoRecomendada) {
        TaxaCreditoRecomendada = taxaCreditoRecomendada;
    }

    public Optional<Double> getTaxaCredito6xRecomendada() {
        return Optional.ofNullable(TaxaCredito6xRecomendada);
    }

    public void setTaxaCredito6xRecomendada(double taxaCredito6xRecomendada) {
        TaxaCredito6xRecomendada = taxaCredito6xRecomendada;
    }

    public Optional<Double> getTaxaCredito12xRecomendada() {
        return Optional.ofNullable(TaxaCredito12xRecomendada);
    }

    public void setTaxaCredito12xRecomendada(double taxaCredito12xRecomendada) {
        TaxaCredito12xRecomendada = taxaCredito12xRecomendada;
    }
}
