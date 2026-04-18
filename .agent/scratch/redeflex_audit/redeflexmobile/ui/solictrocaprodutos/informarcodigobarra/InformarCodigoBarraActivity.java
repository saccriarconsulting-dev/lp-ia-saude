package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog.SolicitarTrocaListener;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraPresenterImpl.CB_ANTIGOS;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraPresenterImpl.CB_NOVOS;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao.NovaSolicitacaoActivity.EXTRA_SOLICITACAO_TROCA_ID;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_INFORMAR_COD_BARRA_COD_BARRA;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_INFORMAR_COD_BARRA_COD_BARRA_ID;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_INFORMAR_COD_BARRA_COD_BARRA_LIST;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_INFORMAR_COD_BARRA_REQUEST_CODE;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity.EXTRA_PRODUTO_ID;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemPresenterImpl.DEFAULT_INT_VALUE;


/**
 * Created by Rogério Massa on 08/10/18.
 */

public class InformarCodigoBarraActivity extends BaseActivity implements InformarCodigoBarraView {

    public static final int TAG_INFORMAR_CODIGO_BARRAS = 8;
    public static final int BUTTON_TOUCH_DURATION = 1000;

    @Inject InformarCodigoBarraAdapter adapter;
    @Inject InformarCodigoBarraPresenter presenter;

    @BindView(R.id.informar_codigo_barra_et_cliente_nome) EditText etClienteNome;
    @BindView(R.id.informar_codigo_barra_tv_produto_nome) TextView tvProdutoNome;
    @BindView(R.id.informar_codigo_barra_tv_quantidade_antigo) TextView tvQuantidadeAntigo;
    @BindView(R.id.informar_codigo_barra_tv_quantidade_novo) TextView tvQuantidadeNovo;
    @BindView(R.id.informar_codigo_barra_iv_codigo_barra) ImageView ivCodigoBarra;
    @BindView(R.id.informar_codigo_barra_rv_produtos) RecyclerView rvProdutos;
    @BindView(R.id.informar_codigo_barra_bt_salvar) Button btSalvar;

    private String produtoId;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_informar_codigo_barra;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.solicitacao_troca_informar_cod_barras_titulo));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }

        disposables.add(
                RxView.clicks(btSalvar)
                        .throttleFirst(BUTTON_TOUCH_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> salvarClick(), Timber::e)
        );

        disposables.add(
                RxView.clicks(ivCodigoBarra)
                        .throttleFirst(BUTTON_TOUCH_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> presenter.realizarLeitura(false), Timber::e)
        );

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setNestedScrollingEnabled(false);
        rvProdutos.setAdapter(adapter);

        this.produtoId = bundle.getString(EXTRA_PRODUTO_ID);
        presenter.setProdutoId(produtoId);
        presenter.obterSolicitacao(bundle.getInt(EXTRA_SOLICITACAO_TROCA_ID));
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
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
    public void popularTela(SolicitacaoTroca solicitacao) {
        etClienteNome.setText(solicitacao.getClienteNome());

        SolicitacaoTrocaDetalhes produto = presenter.obterProduto();
        tvProdutoNome.setText(produto.getProdutoNome());

        atualizarQuantidades();
        popularListagem();
    }

    @Override
    public void popularListagem() {
        adapter.setProdutos(presenter.obterProdutoCodBarras());
        presenter.obterQuantidadeAntigosNovos();
        atualizarQuantidades();
    }

    @Override
    public void exibirErro(String mensagem, SolicitarTrocaListener callback) {
        SolicitacaoTrocaDialog.newInstance(mensagem,
                getString(android.R.string.ok),
                callback
        ).show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void abrirSelecionarProdutosBipagem(int codBarraId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INFORMAR_COD_BARRA_REQUEST_CODE, TAG_INFORMAR_CODIGO_BARRAS);
        bundle.putString(EXTRA_PRODUTO_ID, produtoId);

        bundle.putInt(EXTRA_INFORMAR_COD_BARRA_COD_BARRA_ID, codBarraId);
        bundle.putString(EXTRA_INFORMAR_COD_BARRA_COD_BARRA_LIST, new Gson().toJson(presenter.obterProdutoCodBarras()));

        Utilidades.openNewActivityForResult(this,
                SelecionarProdutosBipagemActivity.class, TAG_INFORMAR_CODIGO_BARRAS, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == TAG_INFORMAR_CODIGO_BARRAS) {
            presenter.inserirCodigoBarra(
                    data.getIntExtra(EXTRA_INFORMAR_COD_BARRA_COD_BARRA_ID, DEFAULT_INT_VALUE),
                    data.getStringExtra(EXTRA_INFORMAR_COD_BARRA_COD_BARRA));
        }
    }

    private void salvarClick() {
        if (!presenter.houveAlteracao()) {
            SolicitacaoTrocaDialog.newInstance(
                    getString(R.string.solicitacao_troca_informar_cod_barras_dialog_erro_salvar),
                    getString(android.R.string.ok),
                    null
            ).show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
            return;
        }

        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_informar_cod_barras_dialog_salvar),
                getString(R.string.solicitacao_troca_informar_cod_barras_dialog_bt_positivo),
                getString(R.string.solicitacao_troca_dialog_bt_nao),
                () -> presenter.salvar(),
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    private void atualizarQuantidades() {
        HashMap<String, Integer> quantidades = presenter.obterQuantidadeAntigosNovos();
        tvQuantidadeAntigo.setText(getString(R.string.solicitacao_troca_informar_cod_barras_tv_antigos, quantidades.get(CB_ANTIGOS)));
        tvQuantidadeNovo.setText(getString(R.string.solicitacao_troca_informar_cod_barras_tv_novos, quantidades.get(CB_NOVOS)));
    }
}
