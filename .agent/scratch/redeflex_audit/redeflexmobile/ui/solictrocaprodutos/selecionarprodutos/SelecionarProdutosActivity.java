package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosAdapter.SelecionarProdutosAdapterListener;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity;
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

import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_PRODUTO_ID;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST;

/**
 * Created by Rogério Massa on 01/10/2018.
 */

public class SelecionarProdutosActivity extends BaseActivity implements SelecionarProdutosView,
        SelecionarProdutosAdapterListener {

    public static final int SELECIONAR_PRODUTO_BIPAGEM = 10;
    public static final int BUTTON_WAIT_DURATION = 1000;
    private static final String EMPTY_STRING = "";
    private static final String NECESSITA_BIPAGEM = "S";
    @Inject SelecionarProdutosPresenter presenter;
    @Inject SelecionarProdutosAdapter adapter;

    @BindView(R.id.selecionar_produtos_ss_produto) SearchableSpinner ssProduto;
    @BindView(R.id.selecionar_produtos_rl_quantidade) RelativeLayout rlQuantidade;
    @BindView(R.id.selecionar_produtos_et_quantidade) EditText etQuantidade;
    @BindView(R.id.selecionar_produtos_bt_adicionar) Button btAdicionar;
    @BindView(R.id.selecionar_produtos_rv_produtos) RecyclerView rvProdutos;
    @BindView(R.id.selecionar_produtos_bt_salvar) Button btSalvar;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_selecionar_produtos_troca;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.solicitacao_troca_selecionar_produto_titulo));
        }

        ssProduto.setAdapter(UtilAdapter.adapterProduto(this, presenter.obterProdutosSelecao()));
        ssProduto.setOnItemSelectedListener(this.obterProdutoListener());

        etQuantidade.getBackground().mutate()
                .setColorFilter(getResources().getColor(R.color.cinza), PorterDuff.Mode.SRC_ATOP);

        compositeDisposable.add(
                RxView.clicks(btAdicionar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> adicionarClick(), Timber::e)
        );
        compositeDisposable.add(
                RxView.clicks(btSalvar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> salvarClick(), Timber::e)
        );

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setAdapter(adapter);

        this.carregarExtras();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
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
    public void carregarListagem(List<SolicitacaoTrocaDetalhes> produtos) {
        etQuantidade.setText("");
        etQuantidade.clearFocus();
        this.closeKeyboard();
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
    public void exibirMotivos(List<SolicitacaoTrocaMotivo> motivos, SolicitacaoTrocaDetalhes produto) {
        SolicitacaoTrocaDialog.newInstance(
                getString(R.string.solicitacao_troca_selecionar_produto_dialog_motivo_bt_negativo),
                getString(R.string.solicitacao_troca_selecionar_produto_dialog_motivo_bt_positivo),
                motivos,
                null,
                motivoId -> {
                    produto.setMotivoId(motivoId);
                    presenter.setProduto(produto);
                }).show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void removerProduto(SolicitacaoTrocaDetalhes produto) {
        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover_bt_positivo),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover_bt_negativo),
                () -> presenter.removerProduto(produto.getProdutoCodigo()),
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECIONAR_PRODUTO_BIPAGEM && resultCode == Activity.RESULT_OK && data != null) {
            String retorno = data.getStringExtra(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST);
            if (retorno != null && !retorno.isEmpty()) {
                presenter.setProduto(new Gson().fromJson(retorno, SolicitacaoTrocaDetalhes.class));
            }
        }

        if (requestCode == SELECIONAR_PRODUTO_BIPAGEM && resultCode == Activity.RESULT_CANCELED && data != null) {
            String produtoId = data.getStringExtra(EXTRA_PRODUTO_ID);
            presenter.removerProduto(produtoId);
        }
    }

    @Override
    public void onBackPressed() {
        if (presenter.getProdutos() == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_produto_bipagem_dialog_voltar),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_voltar_bt_positivo),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_voltar_bt_negativo),
                () -> {
                    setResult(RESULT_CANCELED);
                    finish();
                },
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    private void carregarExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String params = bundle.getString(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST);
            if (params != null && !params.isEmpty()) {
                List<SolicitacaoTrocaDetalhes> produtos = new ArrayList<>(Arrays.asList(new Gson()
                        .fromJson(params, SolicitacaoTrocaDetalhes[].class)));
                presenter.setProdutos(produtos);
            }
        }
    }

    private AdapterView.OnItemSelectedListener obterProdutoListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Produto produto = presenter.getProdutosListagem().get(position);
                if (produto == null) {
                    exibirErro(getString(R.string.solicitacao_troca_selecionar_produto_dialog_produto_vazio));
                    return;
                }

                etQuantidade.setText(EMPTY_STRING);
                if (NECESSITA_BIPAGEM.equalsIgnoreCase(produto.getBipagem())) {
                    rlQuantidade.setVisibility(View.GONE);
                } else {
                    rlQuantidade.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    private void adicionarClick() {
        if (!Validacoes.validacaoDataAparelho(this)) {
            return;
        }

        Produto produto = presenter.getProdutosListagem().get(ssProduto.getSelectedItemPosition());
        if (produto == null) {
            exibirErro(getString(R.string.solicitacao_troca_selecionar_produto_dialog_produto_selecione));
            return;
        }

        if (NECESSITA_BIPAGEM.equalsIgnoreCase(produto.getBipagem())) {
            adicionarProdutoComBipagem(produto);
        } else {
            presenter.adicionarProdutoSemBipagem(produto, etQuantidade.getText().toString().trim());
        }
    }

    private void adicionarProdutoComBipagem(Produto produto) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PRODUTO_ID, produto.getId());
        String produtos = presenter.getProdutosCodBarras(produto);
        if (produtos != null) bundle.putString(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST, produtos);
        Utilidades.openNewActivityForResult(this, SelecionarProdutosBipagemActivity.class, SELECIONAR_PRODUTO_BIPAGEM, bundle);
    }

    private void salvarClick() {
        String produtos = presenter.getProdutos();
        if (produtos == null || produtos.isEmpty()) {
            SolicitacaoTrocaDialog.newInstance(
                    getString(R.string.solicitacao_troca_dialog_erro_produto),
                    getString(R.string.solicitacao_troca_dialog_bt_ok),
                    null
            ).show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST, produtos);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
