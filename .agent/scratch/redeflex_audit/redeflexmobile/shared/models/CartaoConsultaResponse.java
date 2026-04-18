package com.axys.redeflexmobile.shared.models;

public class CartaoConsultaResponse {
    private int ClienteId;
    private double Valor;
    private int Status;
    private String Mensagem;

    public CartaoConsultaResponse() {
    }

    public int getClienteId() {
        return ClienteId;
    }

    public void setClienteId(int clienteId) {
        ClienteId = clienteId;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public String getMensagem() {
        return Mensagem;
    }

    public void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
