package com.axys.redeflexmobile.shared.models;

import java.util.List;

public class SolicitacaoPidSimuladorRequest {
    private int IdSolicitacaoPid;
    private String TipoCliente;
    private String Mcc;
    private String CpfCnpj;
    private String TipoTaxaRav;
    private double TpvTotal;
    private double DistribuicaoDebitoProposta;
    private double DistribuicaoCreditoProposta;
    private double DistribuicaoCredito6xProposta;
    private double DistribuicaoCredito12xProposta;
    private double TaxaRav;
    private List<SolicitacaoPidSimuladorTaxaBandeiraRequest> TaxaBandeira;

    public SolicitacaoPidSimuladorRequest() {
    }

    public Integer getIdSolicitacaoPid() {
        return IdSolicitacaoPid;
    }

    public void setIdSolicitacaoPid(Integer idSolicitacaoPid) {
        IdSolicitacaoPid = idSolicitacaoPid;
    }

    public String getTipoCliente() {
        return TipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        TipoCliente = tipoCliente;
    }

    public String getMcc() {
        return Mcc;
    }

    public void setMcc(String mcc) {
        Mcc = mcc;
    }

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public double getTpvTotal() {
        return TpvTotal;
    }

    public void setTpvTotal(double tpvTotal) {
        TpvTotal = tpvTotal;
    }

    public double getDistribuicaoDebitoProposta() {
        return DistribuicaoDebitoProposta;
    }

    public void setDistribuicaoDebitoProposta(double distribuicaoDebitoProposta) {
        DistribuicaoDebitoProposta = distribuicaoDebitoProposta;
    }

    public double getDistribuicaoCreditoProposta() {
        return DistribuicaoCreditoProposta;
    }

    public void setDistribuicaoCreditoProposta(double distribuicaoCreditoProposta) {
        DistribuicaoCreditoProposta = distribuicaoCreditoProposta;
    }

    public double getDistribuicaoCredito6xProposta() {
        return DistribuicaoCredito6xProposta;
    }

    public void setDistribuicaoCredito6xProposta(double distribuicaoCredito6xProposta) {
        DistribuicaoCredito6xProposta = distribuicaoCredito6xProposta;
    }

    public double getDistribuicaoCredito12xProposta() {
        return DistribuicaoCredito12xProposta;
    }

    public void setDistribuicaoCredito12xProposta(double distribuicaoCredito12xProposta) {
        DistribuicaoCredito12xProposta = distribuicaoCredito12xProposta;
    }

    public double getTaxaRav() {
        return TaxaRav;
    }

    public void setTaxaRav(double taxaRav) {
        TaxaRav = taxaRav;
    }

    public List<SolicitacaoPidSimuladorTaxaBandeiraRequest> getTaxaBandeira() {
        return TaxaBandeira;
    }

    public void setTaxaBandeira(List<SolicitacaoPidSimuladorTaxaBandeiraRequest> taxaBandeira) {
        TaxaBandeira = taxaBandeira;
    }

    public String getTipoTaxaRav() {
        return TipoTaxaRav;
    }

    public void setTipoTaxaRav(String tipoTaxaRav) {
        TipoTaxaRav = tipoTaxaRav;
    }
}
