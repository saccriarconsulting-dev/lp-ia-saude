package com.axys.redeflexmobile.shared.models;

import java.util.HashMap;
import java.util.Map;


public final class PrecoFormaCache {

    private static final Map<String, Double> valorPadraoPorProduto = new HashMap<>();
    private static final Map<String, ValorPorFormaPagto> valorPorFormaPorProduto = new HashMap<>();
    private static final Map<String, String> vendeAvistaPorProduto = new HashMap<>();

    private PrecoFormaCache() {}

    public static void put(Produto p) {
        if (p == null || p.getId() == null) return;

        if (p.getValorPorFormaPagto() != null) {
            valorPorFormaPorProduto.put(p.getId(), p.getValorPorFormaPagto());
        }

        if (p.getVendaAvista() != null) {
            vendeAvistaPorProduto.put(p.getId(), p.getVendaAvista());
        }
    }

    public static void putPrecoVenda(String idProduto, double valorVenda) {
        if (idProduto == null) return;
        if (valorVenda > 0.0) {
            valorPadraoPorProduto.put(idProduto, valorVenda);
        }
    }

    public static ValorPorFormaPagto getValorPorForma(String idProduto) {
        return valorPorFormaPorProduto.get(idProduto);
    }

    public static void clear() {
        valorPadraoPorProduto.clear();
        valorPorFormaPorProduto.clear();
        vendeAvistaPorProduto.clear();
    }
}