package com.axys.redeflexmobile.ui.venda.pedido;

public interface EventoPedidoVendaAdapter {

    void onTouch(String produtoId, String itemVendaId, int quantidade,
                 int quantidadeLido, boolean produtoCombo, String produtoComboId,
                 int quantidadeCombo, int quantidadeComboLido);
}
