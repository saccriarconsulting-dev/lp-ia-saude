package com.axys.redeflexmobile.shared.models;

/**
 * Created by joao.viana on 08/08/2016.
 */
public class VendaConsultaItem {
    private String cliente;
    private int id;
    private String horavenda;
    private Double valor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getHoravenda() {
        return horavenda;
    }

    public void setHoravenda(String horavenda) {
        this.horavenda = horavenda;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}