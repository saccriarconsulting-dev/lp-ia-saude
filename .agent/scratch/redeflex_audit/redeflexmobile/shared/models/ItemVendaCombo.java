package com.axys.redeflexmobile.shared.models;

import java.util.ArrayList;

/**
 * Created by joao.viana on 25/10/2017.
 */

public class ItemVendaCombo extends ItemVenda {
    private int qtdCombo;
    private ArrayList<ComboVenda> listItens;
    private boolean isViewing = false;
    public ArrayList<ComboVenda> getListItens() {
        return listItens;
    }

    public void setListItens(ArrayList<ComboVenda> listItens) {
        this.listItens = listItens;
    }

    public int getQtdCombo() {
        return qtdCombo;
    }

    public void setQtdCombo(int qtdCombo) {
        this.qtdCombo = qtdCombo;
    }

    public void changeViewing() {
        isViewing = !isViewing;
    }

    public boolean isViewing() {
        return isViewing;
    }
}