package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Rogério Massa on 09/10/18.
 */

public interface ListagemSolicitacaoTrocaManager {

    Single<List<ListagemSolicitacaoExibicao>> carregarSolicitacoes(List<EnumStatusTrocaProduto> filtros);

    Completable cancelarSolicitacao(ListagemSolicitacaoExibicao solicitacao);

    Produto obterProduto(String produtoId);

    Completable trocarProdutos(ListagemSolicitacaoExibicao solicitacao);
}
