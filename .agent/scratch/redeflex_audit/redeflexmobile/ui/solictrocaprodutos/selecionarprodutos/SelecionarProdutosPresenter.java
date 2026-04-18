package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 01/10/2018.
 */

public interface SelecionarProdutosPresenter {

    List<Produto> getProdutosListagem();

    String getProdutos();

    void setProdutos(List<SolicitacaoTrocaDetalhes> produtos);

    String getProdutosCodBarras(Produto produto);

    void setProduto(SolicitacaoTrocaDetalhes produto);

    ArrayList<Produto> obterProdutosSelecao();

    void removerProduto(String produtoCodigo);

    void adicionarProdutoSemBipagem(Produto produto, String qtde);

    void solicitarMotivo(SolicitacaoTrocaDetalhes produto);
}
