package com.axys.redeflexmobile.ui.venda.pedido.produto;

import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import io.reactivex.Single;

public interface PedidoVendaProdutoPresenter extends BasePresenter<PedidoVendaProdutoView> {

    void carregarProdutos();

    void recuperarProduto(Produto produto);

    void validarProduto(String quantidade, Double precoVenda, String idCliente);

    Preco retornaPrecoDiferenciado(Produto produto, String idCliente);

    PrecoDiferenciado obterPrecoDiferenciado(String codigoPreco);
}
