package com.axys.redeflexmobile.ui.redeflex;

import com.axys.redeflexmobile.shared.models.ItemVendaCombo;

import java.util.ArrayList;
import java.util.List;

public final class VendaMemoria {
    private static final ArrayList<ItemVendaCombo> listaItensEstatica = new ArrayList<>();

    public static void setLista(List<ItemVendaCombo> itens) {
        listaItensEstatica.clear();
        if (itens != null) {
            listaItensEstatica.addAll(itens);
        }
    }

    public static ArrayList<ItemVendaCombo> getLista() {
        return new ArrayList<>(listaItensEstatica);
    }

    public static void clear() {
        listaItensEstatica.clear();
    }
}