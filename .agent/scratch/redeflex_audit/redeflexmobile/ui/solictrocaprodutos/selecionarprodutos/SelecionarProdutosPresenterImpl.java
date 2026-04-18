package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 01/10/2018.
 */

public class SelecionarProdutosPresenterImpl extends BasePresenterImpl<SelecionarProdutosView>
        implements SelecionarProdutosPresenter {

    private SelecionarProdutosManager selecionarProdutosManager;
    private List<Produto> produtosListagem;
    private List<SolicitacaoTrocaDetalhes> produtos;

    SelecionarProdutosPresenterImpl(SelecionarProdutosView view,
                                    SchedulerProvider schedulerProvider,
                                    ExceptionUtils exceptionUtils,
                                    SelecionarProdutosManager selecionarProdutosManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.selecionarProdutosManager = selecionarProdutosManager;
        this.produtos = new ArrayList<>();
    }

    @Override
    public List<Produto> getProdutosListagem() {
        return produtosListagem;
    }

    @Override
    public String getProdutos() {
        return produtos == null || produtos.isEmpty() ? null : new Gson().toJson(produtos);
    }

    @Override
    public void setProdutos(List<SolicitacaoTrocaDetalhes> produtos) {
        this.produtos = produtos;
        getView().carregarListagem(this.produtos);
    }

    @Override
    public String getProdutosCodBarras(Produto produto) {
        if (produtos == null || produtos.isEmpty()) return null;
        SolicitacaoTrocaDetalhes item = Stream.ofNullable(produtos).filter(value ->
                value.getProdutoCodigo().equals(produto.getId())).findFirst().orElse(null);
        if (item == null) return null;
        return new Gson().toJson(item.getIccids());
    }

    @Override
    public void setProduto(SolicitacaoTrocaDetalhes produto) {
        if (this.produtos == null) this.produtos = new ArrayList<>();

        SolicitacaoTrocaDetalhes validador = Stream.ofNullable(produtos)
                .filter(value -> value.getProdutoCodigo().equals(produto.getProdutoCodigo()))
                .findFirst()
                .orElse(null);

        if (validador == null) {
            this.produtos.add(produto);
            getView().carregarListagem(this.produtos);
            return;
        }

        Stream.ofNullable(produtos).forEachIndexed((index, solicitacaoTrocaDetalhes) -> {
            if (solicitacaoTrocaDetalhes.getProdutoCodigo().equals(produto.getProdutoCodigo())) {
                produtos.set(index, produto);
            }
        });
        getView().carregarListagem(this.produtos);
    }

    @Override
    public ArrayList<Produto> obterProdutosSelecao() {
        this.produtosListagem = selecionarProdutosManager.obterProdutosSelecaoTroca();
        return (ArrayList<Produto>) this.produtosListagem;
    }

    @Override
    public void removerProduto(String produtoCodigo) {
        SolicitacaoTrocaDetalhes produto = Stream.ofNullable(produtos)
                .filter(value -> value.getProdutoCodigo().equals(produtoCodigo))
                .findFirst()
                .orElse(null);
        if (produto != null) {
            produtos.remove(produto);
            getView().carregarListagem(produtos);
        }
    }

    @Override
    public void adicionarProdutoSemBipagem(Produto produto, String qtde) {

        SolicitacaoTrocaDetalhes validador = Stream.ofNullable(produtos).filter(value ->
                value.getProdutoCodigo().equals(produto.getId())).findFirst().orElse(null);
        if (validador != null) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_selecionar_produto_dialog_produto_adicionado));
            return;
        }

        if (Util_IO.isNullOrEmpty(qtde)) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_selecionar_produto_dialog_informe_quantidade));
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.valueOf(qtde);
        } catch (NumberFormatException e) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_selecionar_produto_dialog_quantidade_invalida));
            return;
        }

        if (quantidade == 0) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_selecionar_produto_dialog_quantidade_invalida));
            return;
        }
        SolicitacaoTrocaDetalhes solicitacaoTrocaDetalhes = new SolicitacaoTrocaDetalhes();
        solicitacaoTrocaDetalhes.setProdutoCodigo(produto.getId());
        solicitacaoTrocaDetalhes.setProdutoNome(produto.getNome());
        solicitacaoTrocaDetalhes.setQtde(quantidade);
        solicitacaoTrocaDetalhes.setValorUnitario(produto.getValor());
        solicitacaoTrocaDetalhes.setValorTotal(quantidade * solicitacaoTrocaDetalhes.getValorUnitario());
        solicitarMotivo(solicitacaoTrocaDetalhes);
    }

    @Override
    public void solicitarMotivo(SolicitacaoTrocaDetalhes produto) {
        getView().exibirMotivos(selecionarProdutosManager.obterMotivos(), produto);
    }
}
