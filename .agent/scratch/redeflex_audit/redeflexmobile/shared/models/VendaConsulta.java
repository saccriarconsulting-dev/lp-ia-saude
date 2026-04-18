package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joao.viana on 08/08/2016.
 */
public class VendaConsulta {
    private Date dataVenda;
    private Double valor;
    private List<VendaConsultaItem> list;

    public VendaConsulta() {
        this.list = new ArrayList<VendaConsultaItem>();
    }

    public List<VendaConsultaItem> getList() {
        return list;
    }

    public void setList(List<VendaConsultaItem> list) {
        this.list = list;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}