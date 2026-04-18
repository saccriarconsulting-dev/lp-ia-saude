package com.axys.redeflexmobile.ui.venda.pedido;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

public interface PedidoVendaView extends BaseView {

    void popularAdapter(List<ItemVendaCombo> itens);

    void popularCliente(Cliente cliente);

    void atualizarAdicaoAdapter(ItemVendaCombo itemVenda);

    void lerIccid(int visitaId, String produtoId, String vendaItemId, int quantidade,
                  int quantidadeLido, boolean produtoCombo, String produtoComboId,
                  int quantidadeCombo, int quantidadeComboLido);

    void mostrarMensagem(String mensagem);

    void atualizarTotal(String total);

    void iniciarTimer();

    List<ItemVendaCombo> obterDadosAdapter();

    void atualizarAdapter(int posicao);

    void fechar();
}
