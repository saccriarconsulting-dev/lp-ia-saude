package com.axys.redeflexmobile.shared.models;

public class TaxaMdrRequest {

    private int PessoaTipoId;
    private int TaxaMDRTipoPrazoNegociacaoId;
    private int TaxaMDRTipoNegociacaoId;
    private int TaxaMDRTipoClassificacaoId;
    private int BandeiraTipoId;
    private int Rav;
    private int MCC;
    private double Faturamento;
    private int EnviaTermoIncentivo;

    public TaxaMdrRequest() {
    }

    public int getPessoaTipoId() {
        return PessoaTipoId;
    }

    public void setPessoaTipoId(int pessoaTipoId) {
        PessoaTipoId = pessoaTipoId;
    }

    public int getTaxaMDRTipoPrazoNegociacaoId() {
        return TaxaMDRTipoPrazoNegociacaoId;
    }

    public void setTaxaMDRTipoPrazoNegociacaoId(int taxaMDRTipoPrazoNegociacaoId) {
        TaxaMDRTipoPrazoNegociacaoId = taxaMDRTipoPrazoNegociacaoId;
    }

    public int getTaxaMDRTipoNegociacaoId() {
        return TaxaMDRTipoNegociacaoId;
    }

    public void setTaxaMDRTipoNegociacaoId(int taxaMDRTipoNegociacaoId) {
        TaxaMDRTipoNegociacaoId = taxaMDRTipoNegociacaoId;
    }

    public int getTaxaMDRTipoClassificacaoId() {
        return TaxaMDRTipoClassificacaoId;
    }

    public void setTaxaMDRTipoClassificacaoId(int taxaMDRTipoClassificacaoId) {
        TaxaMDRTipoClassificacaoId = taxaMDRTipoClassificacaoId;
    }

    public int getBandeiraTipoId() {
        return BandeiraTipoId;
    }

    public void setBandeiraTipoId(int bandeiraTipoId) {
        BandeiraTipoId = bandeiraTipoId;
    }

    public int getRav() {
        return Rav;
    }

    public void setRav(int rav) {
        Rav = rav;
    }

    public int getMCC() {
        return MCC;
    }

    public void setMCC(int MCC) {
        this.MCC = MCC;
    }

    public double getFaturamento() {
        return Faturamento;
    }

    public void setFaturamento(double faturamento) {
        Faturamento = faturamento;
    }

    public int getEnviaTermoIncentivo() {
        return EnviaTermoIncentivo;
    }

    public void setEnviaTermoIncentivo(int enviaTermoIncentivo) {
        EnviaTermoIncentivo = enviaTermoIncentivo;
    }
}
