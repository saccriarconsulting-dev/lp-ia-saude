package com.axys.redeflexmobile.shared.models;

import java.util.Optional;

public class SolicitacaoPidTaxaRAV {
    private Double ConcorrenciaRavAutomatica;
    private Double NovaPropostaRavAutomatica;
    private Double ConcorrenciaRavEventual;
    private Double NovaPropostaRavEventual;

    public SolicitacaoPidTaxaRAV() {
    }

    public Optional<Double> getConcorrenciaRavAutomatica() {
        return Optional.ofNullable(ConcorrenciaRavAutomatica);
    }

    public void setConcorrenciaRavAutomatica(double concorrenciaRavAutomatica) {
        ConcorrenciaRavAutomatica = concorrenciaRavAutomatica;
    }

    public Optional<Double> getNovaPropostaRavAutomatica() {
        return Optional.ofNullable(NovaPropostaRavAutomatica);
    }

    public void setNovaPropostaRavAutomatica(Double novaPropostaRavAutomatica) {
        NovaPropostaRavAutomatica = novaPropostaRavAutomatica;
    }

    public Optional<Double> getConcorrenciaRavEventual() {
        return Optional.ofNullable(ConcorrenciaRavEventual);
    }

    public void setConcorrenciaRavEventual(Double concorrenciaRavEventual) {
        ConcorrenciaRavEventual = concorrenciaRavEventual;
    }

    public Optional<Double> getNovaPropostaRavEventual() {
        return Optional.ofNullable(NovaPropostaRavEventual);
    }

    public void setNovaPropostaRavEventual(Double novaPropostaRavEventual) {
        NovaPropostaRavEventual = novaPropostaRavEventual;
    }
}
