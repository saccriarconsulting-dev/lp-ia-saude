package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;

/**
 * Created by joao.viana on 01/09/2017.
 */

public class DevolucaoEnvio extends Devolucao {
    private String idVendedor;
    private ArrayList<DevolucaoItens> listItens;

    public ArrayList<DevolucaoItens> getListItens() {
        return listItens;
    }

    public void setListItens(ArrayList<DevolucaoItens> listItens) {
        this.listItens = listItens;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }
}