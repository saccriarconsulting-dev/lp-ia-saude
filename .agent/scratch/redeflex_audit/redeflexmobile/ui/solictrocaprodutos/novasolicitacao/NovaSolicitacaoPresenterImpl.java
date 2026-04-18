package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

public class NovaSolicitacaoPresenterImpl extends BasePresenterImpl<NovaSolicitacaoView> implements
        NovaSolicitacaoPresenter {

    private NovaSolicitacaoManager novaSolicitacaoManager;
    private Cliente clienteSelecionado;
    private List<Cliente> clientes;
    private List<SolicitacaoTrocaDetalhes> produtos;

    NovaSolicitacaoPresenterImpl(NovaSolicitacaoView view,
                                 SchedulerProvider schedulerProvider,
                                 ExceptionUtils exceptionUtils,
                                 NovaSolicitacaoManager novaSolicitacaoManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.novaSolicitacaoManager = novaSolicitacaoManager;
    }

    @Override
    public List<SolicitacaoTrocaDetalhes> getProdutos() {
        return produtos;
    }

    @Override
    public void setProdutos(List<SolicitacaoTrocaDetalhes> produtos) {
        this.produtos = produtos;
    }

    @Override
    public void setCliente(int index) {
        try {
            this.clienteSelecionado = clientes.get(index);
        } catch (NullPointerException e) {
            this.clienteSelecionado = null;
        } catch (IndexOutOfBoundsException e) {
            this.clienteSelecionado = null;
        }
    }

    @Override
    public ArrayList<Cliente> obterClientes() {
        this.clientes = novaSolicitacaoManager.obterClientes();
        return (ArrayList<Cliente>) this.clientes;
    }

    @Override
    public void removerProduto(String produtoCodigo) {
        List<SolicitacaoTrocaDetalhes> temp = Stream.ofNullable(produtos)
                .filter(value -> value.getProdutoCodigo().equals(produtoCodigo))
                .toList();
        produtos.removeAll(temp);
        getView().carregarListagem(produtos);
    }

    @Override
    public boolean possoVoltar() {
        return this.clienteSelecionado == null && this.produtos == null;
    }

    @Override
    public void salvarSolicitacao() {
        if (this.clienteSelecionado == null) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_nova_solicitacao_cliente_vazio));
            return;
        }

        if (this.produtos == null || this.produtos.isEmpty()) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_nova_solicitacao_produto_vazio));
            return;
        }

        SolicitacaoTroca solicitacaoTroca = new SolicitacaoTroca();
        solicitacaoTroca.setDataSolicitacao(new Date());
        solicitacaoTroca.setVendedorId(novaSolicitacaoManager.obterVendedorId());
        solicitacaoTroca.setClienteId(clienteSelecionado.getId());
        solicitacaoTroca.setClienteNome(clienteSelecionado.getNomeFantasia());
        solicitacaoTroca.setProdutos(produtos);

        addDisposable(novaSolicitacaoManager.iniciarSolicitacao(solicitacaoTroca)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doOnTerminate(() -> getView().hideLoading())
                .subscribe(() -> {
                            this.clienteSelecionado = null;
                            this.produtos = null;
                            getView().onBackPressed();
                        },
                        throwable -> getView().exibirErro(throwable.getMessage())));
    }
}
