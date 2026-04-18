package com.axys.redeflexmobile.shared.models.venda;

public class Financeiro {
    private String quantidadeBoletos;
    private double valorInadimplencia;

    public Financeiro() {
    }

    public Financeiro(String quantidadeBoletos, double valorInadimplencia) {
        this.quantidadeBoletos = quantidadeBoletos;
        this.valorInadimplencia = valorInadimplencia;
    }

    public String getQuantidadeBoletos() {
        return quantidadeBoletos;
    }

    public void setQuantidadeBoletos(String quantidadeBoletos) {
        this.quantidadeBoletos = quantidadeBoletos;
    }

    public double getValorInadimplencia() {
        return valorInadimplencia;
    }

    public void setValorInadimplencia(double valorInadimplencia) {
        this.valorInadimplencia = valorInadimplencia;
    }
}
