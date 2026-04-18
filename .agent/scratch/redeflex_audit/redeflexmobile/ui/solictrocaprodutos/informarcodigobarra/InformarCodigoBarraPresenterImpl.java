package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.List;

import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemPresenterImpl.DEFAULT_INT_VALUE;

/**
 * Created by Rogério Massa on 08/10/18.
 */

class InformarCodigoBarraPresenterImpl extends BasePresenterImpl<InformarCodigoBarraView>
        implements InformarCodigoBarraPresenter {

    static final String CB_ANTIGOS = "ANTIGOS";
    static final String CB_NOVOS = "NOVOS";

    private InformarCodigoBarraManager informarCodigoBarraManager;
    private String produtoId;
    private SolicitacaoTroca solicitacao;

    InformarCodigoBarraPresenterImpl(InformarCodigoBarraView view,
                                     SchedulerProvider schedulerProvider,
                                     ExceptionUtils exceptionUtils,
                                     InformarCodigoBarraManager informarCodigoBarraManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.informarCodigoBarraManager = informarCodigoBarraManager;
    }

    @Override
    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public void obterSolicitacao(int solicitacaoTrocaId) {
        addDisposable(informarCodigoBarraManager.obterSolicitacao(solicitacaoTrocaId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::obterSolicitacaoSucesso,
                        throwable -> getView().exibirErro(throwable.getMessage(), null)));
    }

    private void obterSolicitacaoSucesso(SolicitacaoTroca solicitacao) {
        this.solicitacao = solicitacao;
        getView().popularTela(solicitacao);
    }

    @Override
    public SolicitacaoTrocaDetalhes obterProduto() {
        return Stream.ofNullable(solicitacao.getProdutos())
                .filter(produtoDetalhes -> produtoDetalhes.getProdutoCodigo().equals(produtoId))
                .findFirst().orElse(null);
    }

    @Override
    public HashMap<String, Integer> obterQuantidadeAntigosNovos() {
        HashMap<String, Integer> valores = new HashMap<>();
        valores.put(CB_ANTIGOS, Stream.ofNullable(obterProdutoCodBarras())
                .toList()
                .size());
        valores.put(CB_NOVOS, Stream.ofNullable(obterProdutoCodBarras())
                .filter(codBarras -> !Util_IO.isNullOrEmpty(codBarras.getIccidPara()))
                .toList()
                .size());
        return valores;
    }

    @Override
    public List<SolicitacaoTrocaCodBarras> obterProdutoCodBarras() {
        return Stream.ofNullable(solicitacao.getProdutos())
                .filter(produtoDetalhes -> produtoDetalhes.getProdutoCodigo().equals(produtoId))
                .findFirst()
                .get()
                .getIccids();
    }

    @Override
    public void realizarLeitura(boolean salvar) {
        for (SolicitacaoTrocaCodBarras item : obterProdutoCodBarras()) {
            if (item.isBipadoAntigo() && !item.isBipadoNovo()) {
                getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_informar_novo),
                        () -> getView().abrirSelecionarProdutosBipagem(item.getIdApp()));
                return;
            }
        }

        for (SolicitacaoTrocaCodBarras item : obterProdutoCodBarras()) {
            if (!item.isBipadoAntigo()) {
                getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_informar_antigo),
                        () -> getView().abrirSelecionarProdutosBipagem(DEFAULT_INT_VALUE));
                return;
            }
        }
    }

    @Override
    public void inserirCodigoBarra(Integer codBarraId, String codBarra) {
        if (Util_IO.isNullOrEmpty(codBarra) || codBarraId == DEFAULT_INT_VALUE) return;

        Stream.ofNullable(obterProdutoCodBarras())
                .filter(value -> value.getIdApp() == codBarraId)
                .forEach(produtoCodBarra -> {
                    if (!produtoCodBarra.isBipadoAntigo()) {
                        produtoCodBarra.setBipadoAntigo(true);
                    } else if (!produtoCodBarra.isBipadoNovo()) {
                        produtoCodBarra.setIccidPara(codBarra);
                        produtoCodBarra.setBipadoNovo(true);
                    }
                });

        getView().popularListagem();
    }

    @Override
    public boolean houveAlteracao() {
        return Stream.ofNullable(obterProdutoCodBarras())
                .filter(SolicitacaoTrocaCodBarras::isBipadoNovo)
                .count() > 0;
    }

    @Override
    public void salvar() {
        SolicitacaoTrocaDetalhes produto = Stream.ofNullable(this.solicitacao.getProdutos())
                .filter(value -> value.getProdutoCodigo().equals(produtoId))
                .findFirst()
                .get();

        produto.setQtdeTrocado(Stream.ofNullable(obterProdutoCodBarras())
                .filter(SolicitacaoTrocaCodBarras::isBipadoNovo)
                .toList()
                .size());

        addDisposable(informarCodigoBarraManager.salvarTroca(this.solicitacao.getIdApp(), produto)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> getView().onBackPressed(),
                        throwable -> getView().exibirErro(throwable.getMessage(), null)));
    }
}
