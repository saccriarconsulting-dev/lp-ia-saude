package com.axys.redeflexmobile.ui.venda.pedido.produto;

import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

public interface PedidoVendaProdutoView extends BaseView {

    void popularListaPesquisa(List<Produto> produtos);

    void mostrarMensagem(String mensagem);

    void enviarProduto(Produto produto);
}
