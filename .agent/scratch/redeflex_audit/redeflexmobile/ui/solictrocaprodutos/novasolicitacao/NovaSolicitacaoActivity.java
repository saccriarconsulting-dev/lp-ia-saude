package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosAdapter.SelecionarProdutosAdapterListener;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

public class NovaSolicitacaoActivity extends BaseActivity implements NovaSolicitacaoView,
        SelecionarProdutosAdapterListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_SOLICITACAO_TROCA_ID = "solicitacaoTrocaId";
    private static final int NOVA_SOLITICACAO_REQUEST = 11;
    public static final int BUTTON_WAIT_DURATION = 1000;
    @Inject NovaSolicitacaoPresenter presenter;
    @Inject NovaSolicitacaoAdapter adapter;

    @BindView(R.id.nova_solicitacao_ss_cliente) SearchableSpinner ssCliente;
    @BindView(R.id.nova_solicitacao_bt_salvar) Button btSalvar;
    @BindView(R.id.nova_solicitacao_bt_adicionar) Button btAdicionar;
    @BindView(R.id.nova_solicitacao_rv_adicionar) RecyclerView rvProdutos;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_nova_solicitacao_troca;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.solicitacao_troca_nova_solicitacao_titulo));
        }

        ssCliente.setAdapter(UtilAdapter.adapterCliente(this, presenter.obterClientes()));
        ssCliente.setOnItemSelectedListener(this);

        compositeDisposable.add(
                RxView.clicks(btSalvar)
                .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> presenter.salvarSolicitacao(), Timber::e)
        );

        compositeDisposable.add(
                RxView.clicks(btAdicionar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> abrirProdutos(), Timber::e)
        );

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (presenter.possoVoltar()) {
            super.onBackPressed();
            return;
        }
        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_nova_solicitacao_dialog_voltar),
                getString(R.string.solicitacao_troca_nova_solicitacao_dialog_voltar_bt_positivo),
                getString(R.string.solicitacao_troca_dialog_bt_nao),
                super::onBackPressed,
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.setCliente(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void carregarListagem(List<SolicitacaoTrocaDetalhes> produtos) {
        adapter.inserirProdutos(produtos);
    }

    @Override
    public void exibirErro(String mensagem) {
        SolicitacaoTrocaDialog.newInstance(mensagem,
                getString(R.string.solicitacao_troca_dialog_bt_ok),
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void abrirProdutos() {
        Bundle bundle = null;
        List<SolicitacaoTrocaDetalhes> produtos = presenter.getProdutos();
        if (produtos != null && !produtos.isEmpty()) {
            bundle = new Bundle();
            bundle.putString(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST, new Gson().toJson(produtos));
        }
        Utilidades.openNewActivityForResult(this,
                SelecionarProdutosActivity.class, NOVA_SOLITICACAO_REQUEST, bundle);
    }

    @Override
    public void removerProduto(SolicitacaoTrocaDetalhes produto) {
        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover_bt_positivo),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover_bt_negativo),
                () -> {
                    presenter.removerProduto(produto.getProdutoCodigo());
                    adapter.notifyDataSetChanged();
                },
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOVA_SOLITICACAO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            String retorno = data.getStringExtra(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST);
            if (retorno != null && !retorno.isEmpty()) {
                List<SolicitacaoTrocaDetalhes> produtos = new ArrayList<>(Arrays.asList(new Gson()
                        .fromJson(retorno, SolicitacaoTrocaDetalhes[].class)));
                presenter.setProdutos(produtos);
                this.carregarListagem(produtos);
            }
        }
    }
}
