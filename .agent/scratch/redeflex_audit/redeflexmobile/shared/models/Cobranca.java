package com.axys.redeflexmobile.shared.models;

import java.util.Date;

/**
 * Created by joao.viana on 13/07/2016.
 */
public class Cobranca {
    private Long Id;
    private Long NumBoleto;
    private String LinhaDigitavel;
    private Date DataVencimento;
    private Double Valor;
    private int Situacao;
    private int diaspendetens;

    public int getDiaspendetens() {
        return diaspendetens;
    }

    public void setDiaspendetens(int diaspendetens) {
        this.diaspendetens = diaspendetens;
    }

    public Long getNumBoleto() {
        return NumBoleto;
    }

    public void setNumBoleto(Long numBoleto) {
        NumBoleto = numBoleto;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLinhaDigitavel() {
        return LinhaDigitavel;
    }

    public void setLinhaDigitavel(String linhaDigitavel) {
        LinhaDigitavel = linhaDigitavel;
    }

    public Date getDataVencimento() {
        return DataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        DataVencimento = dataVencimento;
    }

    public Double getValor() {
        return Valor;
    }

    public void setValor(Double valor) {
        Valor = valor;
    }

    public int getSituacao() {
        return Situacao;
    }

    public void setSituacao(int situacao) {
        Situacao = situacao;
    }
}