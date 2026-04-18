package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;

import java.util.List;

interface ListagemSolicitacaoTrocaView extends BaseView {

    void carregarListagem(List<ListagemSolicitacaoExibicao> solicitacoes);

    void exibirErro(String mensagem);

    void abrirInformarCodigoBarra(ListagemSolicitacaoExibicao solicitacao);

    void abrirInformarQtdDialog(ListagemSolicitacaoExibicao solicitacao);

    void exibirErroQuantidade();
}
