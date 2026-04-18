package com.axys.redeflexmobile.shared.models;

public class PosRequest {

    private static final int TROCA = 0;
    private static final int REMOCAO = 1;

    private int idVendedor;
    private int idCliente;
    private String idTerminal;
    private int tipoProcesso;

    public PosRequest(int idVendedor, int idCliente, String idTerminal) {
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.idTerminal = idTerminal;
    }

    public void troca() {
        tipoProcesso = TROCA;
    }

    public void remocao() {
        tipoProcesso = REMOCAO;
    }
}
