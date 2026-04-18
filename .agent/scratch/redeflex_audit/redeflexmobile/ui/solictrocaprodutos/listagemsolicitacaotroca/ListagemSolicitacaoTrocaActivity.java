package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog;
import com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoTrocaAdapterListener;
import com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoActivity.EXTRA_SOLICITACAO_TROCA_ID;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_PRODUTO_ID;

public class ListagemSolicitacaoTrocaActivity extends BaseActivity implements
        ListagemSolicitacaoTrocaView, ListagemSolicitacaoTrocaAdapterListener {

    public static final int BUTTON_WAIT_DURATION = 1000;
    @Inject ListagemSolicitacaoTrocaPresenter presenter;
    @Inject ListagemSolicitacaoTrocaAdapter adapter;

    @BindView(R.id.solicitacao_troca_ll_filtros) LinearLayout llFiltros;
    @BindView(R.id.solicitacao_troca_analise) CheckBox cbAnalise;
    @BindView(R.id.solicitacao_troca_aprovado) CheckBox cbAprovado;
    @BindView(R.id.solicitacao_troca_reprovados) CheckBox cbReprovado;
    @BindView(R.id.solicitacao_troca_rv_trocas) RecyclerView rvTroca;
    @BindView(R.id.solicitacao_troca_bt_nova_solicitacao) Button btNovaSolicitacao;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_solicitacao_troca;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.solicitacao_troca_toolbar));
        }

        cbAnalise.setOnCheckedChangeListener(this::eventoSelecaoFiltro);
        cbAprovado.setOnCheckedChangeListener(this::eventoSelecaoFiltro);
        cbReprovado.setOnCheckedChangeListener(this::eventoSelecaoFiltro);

        compositeDisposable.add(
                RxView.clicks(btNovaSolicitacao)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(
                                v -> Utilidades.openNewActivity(
                                        this,
                                        NovaSolicitacaoActivity.class,
                                        null,
                                        false
                                ), Timber::e
                        )
        );
        rvTroca.setLayoutManager(new LinearLayoutManager(this));
        rvTroca.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.carregarSolicitacoes();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void carregarListagem(List<ListagemSolicitacaoExibicao> solicitacoes) {
        adapter.setSolicitacoes(solicitacoes);
    }

    @Override
    public void exibirErro(String mensagem) {
        SolicitacaoTrocaDialog.newInstance(mensagem,
                getString(R.string.solicitacao_troca_dialog_bt_ok),
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void abrirInformarCodigoBarra(ListagemSolicitacaoExibicao solicitacao) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SOLICITACAO_TROCA_ID, solicitacao.solicitacaoId);
        bundle.putString(EXTRA_PRODUTO_ID, solicitacao.produtoId);
        Utilidades.openNewActivity(this, InformarCodigoBarraActivity.class, bundle, false);
    }

    @Override
    public void abrirInformarQtdDialog(ListagemSolicitacaoExibicao solicitacao) {
        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_dialog_confirmar_tv_texto,
                solicitacao.produtoQuantidade),
                solicitacao.produtoNome,
                qtdeTrocada -> {
                    solicitacao.produtoQuantidadeTrocada = qtdeTrocada;
                    presenter.trocarProdutos(solicitacao);
                },
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void exibirErroQuantidade() {
        String mensagem = getString(R.string.solicitacao_troca_listagem_erro_quantidade);
        exibirErro(mensagem);
    }

    @Override
    public void cancelarSolicitacao(ListagemSolicitacaoExibicao solicitacao) {
        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_dialog_cancelar_tv_texto),
                solicitacao.produtoNome,
                getString(R.string.solicitacao_troca_dialog_bt_sim),
                getString(R.string.solicitacao_troca_dialog_bt_nao),
                () -> presenter.cancelarSolicitacao(solicitacao),
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void confirmarSolicitacao(ListagemSolicitacaoExibicao solicitacao) {
        presenter.aceitarSolicitacao(solicitacao);
    }

    private void eventoSelecaoFiltro(CompoundButton compoundButton, boolean ativo) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.solicitacao_troca_analise:
                presenter.setFiltros(EnumStatusTrocaProduto.ANALISE, ativo);
                break;
            case R.id.solicitacao_troca_aprovado:
                presenter.setFiltros(EnumStatusTrocaProduto.APROVADO, ativo);
                break;
            case R.id.solicitacao_troca_reprovados:
                presenter.setFiltros(EnumStatusTrocaProduto.REPROVADO, ativo);
                break;
            default:
                break;
        }
    }
}
