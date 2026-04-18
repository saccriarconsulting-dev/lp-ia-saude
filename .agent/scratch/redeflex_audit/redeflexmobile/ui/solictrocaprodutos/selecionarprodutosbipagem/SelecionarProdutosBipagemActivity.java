package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.redeflex.ProdutoVendaActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemAdapter.SelecionarProdutosBipagemAdapterListener;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra.InformarCodigoBarraActivity.TAG_INFORMAR_CODIGO_BARRAS;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemPresenterImpl.DEFAULT_INT_VALUE;


public class SelecionarProdutosBipagemActivity extends BaseActivity implements
        SelecionarProdutosBipagemView, SelecionarProdutosBipagemAdapterListener {

    public static final String EXTRA_PRODUTO_ID = "ProdutoId";
    public static final String EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST = "Produtos";

    public static final String EXTRA_INFORMAR_COD_BARRA_REQUEST_CODE = "requestCode";
    public static final String EXTRA_INFORMAR_COD_BARRA_COD_BARRA_ID = "codBarraId";
    public static final String EXTRA_INFORMAR_COD_BARRA_COD_BARRA = "codBarra";
    public static final String EXTRA_INFORMAR_COD_BARRA_COD_BARRA_LIST = "ProdutosBipados";
    public static final int BUTTON_WAIT_DURATION = 1000;
    public static final int SEM_MOTIVO = 0;

    @Inject SelecionarProdutosBipagemPresenter presenter;
    @Inject SelecionarProdutosBipagemAdapter adapter;

    @BindView(R.id.txtCodigoBarras) EditText etCodBarras;
    @BindView(R.id.rvCodigoBarras) RecyclerView rvCodigoBarras;
    @BindView(R.id.btnAddCodigo) ImageButton btnAddCodigo;
    @BindView(R.id.btnLerCodigoBarra) LinearLayout btnLerCodigoBarra;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_selecionar_produtos_bipagem;
    }

    @Override
    protected void initialize() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || Util_IO.isNullOrEmpty(bundle.getString(EXTRA_PRODUTO_ID))) {
            onBackPressed();
            return;
        }

        rvCodigoBarras.setLayoutManager(new LinearLayoutManager(this));
        rvCodigoBarras.setAdapter(adapter);

        presenter.getProdutoById(bundle.getString(EXTRA_PRODUTO_ID));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(presenter.getProduto().getNome());
        }

        compositeDisposable.add(
                RxView.clicks(btnLerCodigoBarra)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> abrirCamera(), Timber::e)
        );
        compositeDisposable.add(
                RxView.clicks(btnAddCodigo)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> presenter.inserirCodigoBarra(etCodBarras.getText().toString().trim()), Timber::e)
        );

        Integer InformarCodBarraRequestCode = bundle.getInt(EXTRA_INFORMAR_COD_BARRA_REQUEST_CODE);
        presenter.setInformarCodBarrasActivity(InformarCodBarraRequestCode == TAG_INFORMAR_CODIGO_BARRAS);
        if (!presenter.isInformarCodBarrasActivity()) {
            presenter.inicializarListagem(bundle.getString(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST));
        } else {
            presenter.setInformarCodigoBarrasCodBarraId(bundle.getInt(EXTRA_INFORMAR_COD_BARRA_COD_BARRA_ID, DEFAULT_INT_VALUE));
            presenter.setInformarCodigoBarrasLista(bundle.getString(EXTRA_INFORMAR_COD_BARRA_COD_BARRA_LIST));
        }
        abrirCamera();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        compositeDisposable.add(
                RxMenuItem.clicks(item)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(menu -> {
                            int i = item.getItemId();

                            if (i == android.R.id.home) {
                                onBackPressed();
                            }
                            if (i == R.id.opcao_salvar) {
                                presenter.exibirMotivo();
                            }
                        }, Timber::e)
        );
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void exibirErro(String mensagem) {
        Alerta alerta = new Alerta(this, getString(R.string.solicitacao_troca_dialog_erro_titulo), mensagem);
        alerta.show((dialog, which) -> this.abrirCamera());
    }

    @Override
    public void onBackPressed() {
        if (presenter.obterProdutoSalvar() == null) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                presenter.inserirCodigoBarra(result.getContents());
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    @Override
    public void carregarListagem(List<SolicitacaoTrocaCodBarras> lista, boolean camera) {
        etCodBarras.setText("");
        etCodBarras.clearFocus();
        adapter.carregarListagem(lista);
        if (camera) this.abrirCamera();
    }

    @Override
    public void deletarItem(SolicitacaoTrocaCodBarras codBarras) {
        SolicitacaoTrocaDialog.newInstance(getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover_bt_positivo),
                getString(R.string.solicitacao_troca_produto_bipagem_dialog_remover_bt_negativo),
                () -> presenter.deletarCodBarra(codBarras.getIccidDe()),
                null)
                .show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void abrirCamera() {
        try {
            if (etCodBarras != null) etCodBarras.setText("");
            CodeReader.openCodeReader(SelecionarProdutosBipagemActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    @Override
    public void retornarInformarCodigoBarraActivity(Integer codBarraId, String codBarra) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_INFORMAR_COD_BARRA_COD_BARRA_ID, codBarraId);
        resultIntent.putExtra(EXTRA_INFORMAR_COD_BARRA_COD_BARRA, codBarra);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void exibirMotivos(List<SolicitacaoTrocaMotivo> motivos, SolicitacaoTrocaDetalhes detalhes) {
        SolicitacaoTrocaDialog.newInstance(
                getString(R.string.solicitacao_troca_selecionar_produto_dialog_motivo_bt_negativo),
                getString(R.string.solicitacao_troca_selecionar_produto_dialog_motivo_bt_positivo),
                motivos,
                null,
                this::salvarClick
        ).show(getSupportFragmentManager(), SolicitacaoTrocaDialog.class.getSimpleName());
    }

    @Override
    public void sairSemAlteracao() {
        salvarClick(SEM_MOTIVO);
    }

    private void salvarClick(int motivoId) {
        SolicitacaoTrocaDetalhes solicitacaoTrocaDetalhes = presenter.obterProdutoSalvar();
        if (solicitacaoTrocaDetalhes == null || presenter.isInformarCodBarrasActivity()) {
            setResult(Activity.RESULT_CANCELED, informarRemocao());
            finish();
            return;
        }

        solicitacaoTrocaDetalhes.setMotivoId(motivoId);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECIONAR_PRODUTO_PRODUTOS_LIST, new Gson().toJson(solicitacaoTrocaDetalhes));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private Intent informarRemocao() {
        if (!presenter.deveRemoverProduto()) {
            return null;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PRODUTO_ID, presenter.getProduto().getId());
        return resultIntent;
    }
}