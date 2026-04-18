package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;

import java.util.ArrayList;
import java.util.List;

public class ListagemSolicitacaoTrocaPresenterImpl extends BasePresenterImpl<ListagemSolicitacaoTrocaView>
        implements ListagemSolicitacaoTrocaPresenter {

    private static final String NECESSITA_BIPAGEM = "S";

    private ListagemSolicitacaoTrocaManager listagemSolicitacaoTrocaManager;
    private List<EnumStatusTrocaProduto> filtros;

    ListagemSolicitacaoTrocaPresenterImpl(ListagemSolicitacaoTrocaView activity,
                                          SchedulerProvider schedulerProvider,
                                          ExceptionUtils exceptionUtils,
                                          ListagemSolicitacaoTrocaManager listagemSolicitacaoTrocaManager) {
        super(activity, schedulerProvider, exceptionUtils);
        this.listagemSolicitacaoTrocaManager = listagemSolicitacaoTrocaManager;
    }

    public void setFiltros(EnumStatusTrocaProduto filtro, boolean ativo) {
        if (filtros == null) filtros = new ArrayList<>();
        if (!ativo) this.filtros.remove(filtro);
        else this.filtros.add(filtro);
        if (filtros.isEmpty()) filtros = null;
        this.carregarSolicitacoes();
    }

    @Override
    public void carregarSolicitacoes() {
        addDisposable(listagemSolicitacaoTrocaManager.carregarSolicitacoes(filtros)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(solicitacoes -> getView().carregarListagem(solicitacoes),
                        throwable -> getView().exibirErro(throwable.getMessage())));
    }

    @Override
    public void cancelarSolicitacao(ListagemSolicitacaoExibicao solicitacao) {
        addDisposable(listagemSolicitacaoTrocaManager.cancelarSolicitacao(solicitacao)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::carregarSolicitacoes,
                        throwable -> getView().exibirErro(throwable.getMessage())));
    }

    @Override
    public void aceitarSolicitacao(ListagemSolicitacaoExibicao solicitacao) {
        Produto produto = listagemSolicitacaoTrocaManager.obterProduto(solicitacao.produtoId);
        if (NECESSITA_BIPAGEM.equals(produto.getBipagem())) {
            getView().abrirInformarCodigoBarra(solicitacao);
        } else {
            getView().abrirInformarQtdDialog(solicitacao);
        }
    }

    @Override
    public void trocarProdutos(ListagemSolicitacaoExibicao solicitacao) {
        if (solicitacao.produtoQuantidadeTrocada > solicitacao.produtoQuantidade) {
            getView().exibirErroQuantidade();
            return;
        }
        addDisposable(listagemSolicitacaoTrocaManager.trocarProdutos(solicitacao)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::carregarSolicitacoes,
                        throwable -> getView().exibirErro(throwable.getMessage())));
    }
}
