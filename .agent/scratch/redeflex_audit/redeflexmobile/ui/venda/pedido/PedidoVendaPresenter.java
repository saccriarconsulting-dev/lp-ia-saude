package com.axys.redeflexmobile.ui.venda.pedido;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.Date;

public interface PedidoVendaPresenter extends BasePresenter<PedidoVendaView> {

    void informarVisita(int id);

    void carregarProdutos();

    void carregarCliente();

    void adicionarProdutoBase(Produto produto);

    void removerProduto(ItemVenda itemVenda);

    void lerIccid(String produtoId, String itemVendaId, int quantidade,
                  int quantidadeLido, boolean produtoCombo, String produtoComboId,
                  int quantidadeCombo, int quantidadeComboLido);

    Date dataInicioVisita();

    void finalizarPedido();

    void removerCodigoBarra(CodBarra codBarra, int posicao);

    void cancelarVenda();

    void importarProdutosSugestaoVenda();

    Cliente retornaCliente();
}
