package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoVisualizacaoTipo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Rogério Massa on 09/10/18.
 */

public class ListagemSolicitacaoTrocaManagerImpl implements ListagemSolicitacaoTrocaManager {

    private DBEstoque dbEstoque;
    private DBSolicitacaoTroca dbSolicitacaoTroca;

    public ListagemSolicitacaoTrocaManagerImpl(DBEstoque dbEstoque,
                                               DBSolicitacaoTroca dbSolicitacaoTroca) {
        this.dbEstoque = dbEstoque;
        this.dbSolicitacaoTroca = dbSolicitacaoTroca;
    }

    @Override
    public Single<List<ListagemSolicitacaoExibicao>> carregarSolicitacoes(List<EnumStatusTrocaProduto> filtros) {
        return dbSolicitacaoTroca.obterSolicitacoes()
                .flatMap(solicitacoes -> {

                    List<String> datas = Stream.ofNullable(solicitacoes)
                            .map(solicitacao -> Util_IO.dateToStringBr(solicitacao.getDataSolicitacao()))
                            .distinct()
                            .sorted(String::compareTo)
                            .toList();

                    List<ListagemSolicitacaoExibicao> dados = new ArrayList<>();
                    Stream.ofNullable(datas).forEach(data -> Stream.ofNullable(solicitacoes).forEach(solicitacao -> {
                        if (data.equals(Util_IO.dateToStringBr(solicitacao.getDataSolicitacao()))) {
                            List<ListagemSolicitacaoExibicao> produtos = Stream.ofNullable(solicitacao.getProdutos())
                                    .filter(produto -> {
                                        if (filtros == null) return true;
                                        return filtros.contains(EnumStatusTrocaProduto.obterEnum(produto.getStatusId()))
                                                || filtros.contains(EnumStatusTrocaProduto.APROVADO)
                                                && produto.getStatusId() > EnumStatusTrocaProduto.APROVADO.valor;
                                    })
                                    .map(produto -> new ListagemSolicitacaoExibicao(produto.getSolicitacaoTrocaId(),
                                            solicitacao.getClienteId(),
                                            solicitacao.getClienteNome(),
                                            produto.getProdutoCodigo(),
                                            produto.getProdutoNome(),
                                            produto.getQtde(),
                                            produto.getQtdeTrocado(),
                                            produto.getStatusId(),
                                            solicitacao.getDataSolicitacao(),
                                            produto.getAlteracaoData(),
                                            data))
                                    .toList();

                            if (!produtos.isEmpty()) {
                                if (Stream.ofNullable(dados)
                                        .filter(item -> Util_IO.dateToStringBr(item.dataSolicitacao).equals(data)
                                                && item.tipo == ListagemSolicitacaoVisualizacaoTipo.DATA)
                                        .findFirst()
                                        .orElse(null) == null) {
                                    dados.add(new ListagemSolicitacaoExibicao(data, solicitacao.getDataSolicitacao()));
                                }

                                dados.add(new ListagemSolicitacaoExibicao(solicitacao.getIdApp(), data, solicitacao.getClienteNome()));
                                dados.addAll(produtos);
                            }
                        }
                    }));

                    return Single.just(dados);
                });
    }

    @Override
    public Completable cancelarSolicitacao(ListagemSolicitacaoExibicao solicitacao) {
        return dbSolicitacaoTroca.cancelarSolicitacaoPorProduto(solicitacao.solicitacaoId, solicitacao.produtoId);
    }

    @Override
    public Produto obterProduto(String produtoId) {
        return dbEstoque.getProdutoById(produtoId);
    }

    @Override
    public Completable trocarProdutos(ListagemSolicitacaoExibicao solicitacao) {
        Produto produto = dbEstoque.getProdutoById(solicitacao.produtoId);
        if (produto != null && solicitacao.produtoQuantidadeTrocada > produto.getEstoqueAtual()) {
            return Completable.error(new Throwable("Quantidade digitada é superior à quantidade em estoque."));
        }
        return dbSolicitacaoTroca.trocarProdutosSemBipagem(solicitacao.solicitacaoId,
                solicitacao.produtoId, solicitacao.produtoQuantidadeTrocada);
    }
}
