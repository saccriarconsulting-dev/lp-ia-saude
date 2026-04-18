package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;

public interface ListagemSolicitacaoTrocaPresenter extends BasePresenter<ListagemSolicitacaoTrocaView> {

    void setFiltros(EnumStatusTrocaProduto filtro, boolean ativo);

    void carregarSolicitacoes();

    void cancelarSolicitacao(ListagemSolicitacaoExibicao solicitacao);

    void aceitarSolicitacao(ListagemSolicitacaoExibicao solicitacao);

    void trocarProdutos(ListagemSolicitacaoExibicao solicitacao);
}
