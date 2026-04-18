package com.axys.redeflexmobile.ui.redeflex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.BarcodeAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.enums.EnumTipoAuditagem;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.RetCodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding2.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class ProdutoAuditagemActivity extends AppCompatActivity {

    @BindView(R.id.buttonBuscar)
    ImageButton btnAddCodigoBarra;
    @BindView(R.id.button)
    ImageButton scanBtn;
    @BindView(R.id.txtCodigoBarras)
    EditText txtCodigo;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.combo)
    LinearLayout llCombo;
    @BindView(R.id.falta)
    LinearLayout llFalta;
    @BindView(R.id.txtQtdBipado)
    TextView tvQtdeBipada;
    @BindView(R.id.txtQtdFaltaBipar)
    TextView tvFaltaBipar;
    @BindView(R.id.layoutbipagemproduto_txtQuantidade)
    TextView tvQuantidade;

    private int QtdBipagem;
    private DBEstoque dbEstoque;
    private Produto produto;
    private CodBarra linha;
    private ArrayList<CodBarra> itensCod;
    private ProdutoCombo produtoCombo;
    private DBIccid dbIccid;
    private ProgressDialog mDialog;
    private CompositeDisposable disposables = new CompositeDisposable();
    private String idAuditagem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_auditagem);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String idp = null;
        if (getIntent().hasExtra(Config.CodigoProduto)) {
            idp = getIntent().getStringExtra(Config.CodigoProduto);
        } else {
            Mensagens.produtoNaoEncontrado(ProdutoAuditagemActivity.this);
            return;
        }

        // Carrega o código da Auditagem para busca dos Dados no caso de edição
        if (getIntent().hasExtra("idAuditagem")) {
            idAuditagem = getIntent().getStringExtra("idAuditagem");
        }

        try {
            criarObjetos();
            tvQuantidade.setVisibility(View.VISIBLE);

            produto = dbEstoque.getProdutoById(idp);

            if (produto == null) {
                Mensagens.produtoNaoEncontrado(ProdutoAuditagemActivity.this);
                return;
            }

            itensCod = new ArrayList<>();
            QtdBipagem = 0;
            getSupportActionBar().setTitle(produto.getId() + " - " + produto.getNome());

            if (produto.getQtdCombo() > 0) {
                llCombo.setVisibility(View.VISIBLE);
            }

            criarEventos();

            // Carrega os dados caso seja alteração
            if (!Util_IO.isNullOrEmpty(idAuditagem)) {
                ArrayList<CodBarra> listaCodigoBarra = dbEstoque.getCodBarras(idAuditagem, produto.getGrupo());
                dbEstoque.deletarPistolagemNaoFinalizadas(EnumTipoAuditagem.AuditagemVendedor);
                for (int aa = 0; aa < listaCodigoBarra.size(); aa++) {
                    dbEstoque.addPistolagem(listaCodigoBarra.get(aa), produto.getId(), null, null);
                }
                atualizaLista();
            }

            abrirCamera();
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoAuditagemActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    protected void onResume() {
        atualizaLista();
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbEstoque.deletarPistolagemNaoFinalizadas(EnumTipoAuditagem.AuditagemVendedor);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solic_merc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Alerta alerta = new Alerta(ProdutoAuditagemActivity.this, getString(R.string.app_name), "Os dados não foram salvos. Deseja continuar sem salvar?");
                alerta.showConfirm((dialog, which) -> onBackPressed(), null);
                break;
            case R.id.action_salvar:
                salvar();
                break;
            case R.id.action_limpar:
                dbEstoque.deletarPistolagemNaoFinalizadas(EnumTipoAuditagem.AuditagemVendedor);
                atualizaLista();
                break;
            case R.id.action_addproduto:
                abrirCamera();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            runOnUiThread(() -> getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE));
            new Handler().postDelayed(() -> {
                runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
                verificaInclusaoCodBarra(result.getContents());
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    private void criarObjetos() {
        produtoCombo = new ProdutoCombo();
        dbEstoque = new DBEstoque(ProdutoAuditagemActivity.this);
        dbIccid = new DBIccid(this);
    }

    private void criarEventos() {
        Disposable adapterDispose = RxAdapterView.itemClickEvents(listView)
                .throttleFirst(NumberUtils.FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(adapterViewItemClickEvent -> {
                    final int posicao = adapterViewItemClickEvent.position();
                    if (itensCod != null && itensCod.size() > 0) {
                        Alerta alerta = new Alerta(ProdutoAuditagemActivity.this,
                                getString(R.string.app_name), "Deseja realmente excluir?");
                        alerta.showConfirm((dialog, which) -> {
                            if (posicao < itensCod.size()) {
                                CodBarra codBarra = itensCod.get(posicao);
                                dbEstoque.deletarPistolagemById(codBarra.getIdPistolagem());
                            }
                            atualizaLista();
                        }, null);
                    }
                }, Timber::e);
        disposables.add(adapterDispose);

        scanBtn.setOnClickListener(v -> abrirCamera());
        btnAddCodigoBarra.setOnClickListener(v -> verificaInclusaoCodBarra(txtCodigo.getText().toString()));
    }

    private void abrirCamera() {
        try {
            txtCodigo.setText("");
            CodeReader.openCodeReader(ProdutoAuditagemActivity.this, IntentIntegrator.CODE_128);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoAuditagemActivity.this, ex.getMessage(), false);
        }
    }

    private void showProgress() {
        this.runOnUiThread(() -> mDialog = ProgressDialog.show(this,
                getString(R.string.app_name),
                "Aguarde, validando range...",
                false,
                false)
        );
    }

    private void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private boolean validacaoInclusao(String codBarra) {
        if (Util_IO.isNullOrEmpty(codBarra)) {
            this.runOnUiThread(() -> {
                Utilidades.retornaMensagem(ProdutoAuditagemActivity.this, "Informe o código", false);
                abrirCamera();
            });
            return false;
        }

        Iccid iccid = dbIccid.getByCodigo(codBarra);
        if (iccid == null) {
            this.runOnUiThread(() -> {
                Alerta alerta = new Alerta(this, codBarra, "Código de barra não está disponível, Entrar em contato com o setor de logística!");
                alerta.show((dialog, which) -> abrirCamera());
            });
            return false;
        }

        if (!iccid.getItemCode().equals(this.produto.getId())) {
            this.runOnUiThread(() -> {
                Alerta alerta = new Alerta(this, codBarra, "Código de barra não é referente ao produto selecionado. Verifique!");
                alerta.show((dialog, which) -> abrirCamera());
            });
            return false;
        }

        if (this.produto == null) {
            Produto produto = dbEstoque.getProdutoById(iccid.getItemCode());
            if (produto == null) {
                this.runOnUiThread(() -> {
                    Alerta alerta = new Alerta(this, codBarra, iccid.getItemCode() + " - produto não está disponível, Entrar em contato com o setor de logística!");
                    alerta.show((dialog, which) -> abrirCamera());
                });
                return false;
            }
        }

        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !codBarra.startsWith(produto.getIniciaCodBarra())) {
            this.runOnUiThread(() -> Utilidades.retornaMensagem(ProdutoAuditagemActivity.this, "Código de Barra deve começar com: " + produto.getIniciaCodBarra(), false));
            return false;
        }

        if (produto.getQtdCodBarra() > 0 && codBarra.trim().length() != produto.getQtdCodBarra()) {
            this.runOnUiThread(() -> {
                Utilidades.retornaMensagem(ProdutoAuditagemActivity.this, "Código de Barra deve conter " + produto.getQtdCodBarra() + " caracteres", false);
                abrirCamera();
            });
            return false;
        }

        return true;
    }

    private void verificaInclusaoCodBarra(final String codBarra) {
        new Thread(() -> {
            if (!validacaoInclusao(codBarra)) {
                return;
            }

            this.runOnUiThread(() -> {
                hideProgress();
                Alerta alerta = new Alerta(ProdutoAuditagemActivity.this, codBarra, "Deseja incluir esse código de barra?");
                alerta.showConfirm((dialog, which) -> {
                    try {
                        if (QtdBipagem == 0) {
                            inserirCodigoBarraInicial(codBarra);
                        } else {
                            inserirCodigoBarraFinal(codBarra);
                        }
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(ProdutoAuditagemActivity.this, ex.getMessage(), false);
                    }
                }, (dialog, which) -> abrirCamera());
            });
        }).start();
    }

    private void inserirCodigoBarraInicial(String codBarra) {
        Alerta alerta1 = new Alerta(ProdutoAuditagemActivity.this, codBarra, "Código de barra unitário?");
        alerta1.showConfirm((dialog15, which15) -> new Thread(() -> {
                    RetCodBarra retCodBarra = CodigoBarra.validacao(codBarra, produto, QtdBipagem,
                            linha, itensCod, true, false, ProdutoAuditagemActivity.this);
                    QtdBipagem = retCodBarra.getQuantidade();

                    this.runOnUiThread(() -> {
                        if (!retCodBarra.isInclusaoOK()) {
                            Alerta alerta2 = new Alerta(ProdutoAuditagemActivity.this, "Informação", retCodBarra.getMensagem());
                            alerta2.show((dialog14, which14) -> abrirCamera());
                            return;
                        }

                        linha = retCodBarra.getCodBarra();
                        dbEstoque.addPistolagem(linha, produto.getId(), null, null);
                        atualizaLista();
                        abrirCamera();
                    });
                }).start(),

                (dialog13, which13) -> new Thread(() -> {
                    RetCodBarra retCodBarra = CodigoBarra.validacao(codBarra, produto, QtdBipagem,
                            linha, itensCod, false, false, ProdutoAuditagemActivity.this);
                    QtdBipagem = retCodBarra.getQuantidade();

                    this.runOnUiThread(() -> {
                        if (!retCodBarra.isInclusaoOK()) {
                            Alerta alerta2 = new Alerta(ProdutoAuditagemActivity.this, "Informação", retCodBarra.getMensagem());
                            alerta2.show((dialog12, which12) -> abrirCamera());
                            return;
                        }

                        linha = retCodBarra.getCodBarra();
                        abrirCamera();
                    });
                }).start());
    }

    private void inserirCodigoBarraFinal(String codBarra) {
        showProgress();
        linha.setCodBarraFinal(codBarra);
        if (!CodigoBarra.validaQuantidadeRange(linha)) {
            this.runOnUiThread(() -> {
                hideProgress();
                new Alerta(ProdutoAuditagemActivity.this, "Erro", "Favor conferir o ICCID inicial e final")
                        .show((dialog1, which1) -> {
                            QtdBipagem = 0;
                            abrirCamera();
                        });
            });
            return;
        }

        RetCodBarra retCodBarra = CodigoBarra.validacao(codBarra, produto, QtdBipagem, linha,
                itensCod, false, false, ProdutoAuditagemActivity.this);
        QtdBipagem = retCodBarra.getQuantidade();

        this.runOnUiThread(() -> {
            hideProgress();
            if (!retCodBarra.isInclusaoOK()) {
                Alerta alerta2 = new Alerta(ProdutoAuditagemActivity.this, "Informação", retCodBarra.getMensagem());
                alerta2.show((dialog1, which1) -> abrirCamera());
                return;
            }

            linha = retCodBarra.getCodBarra();
            dbEstoque.addPistolagem(linha, produto.getId(), null, null);
            atualizaLista();
            abrirCamera();
        });
    }

    private void atualizaLista() {
        try {
            itensCod = dbEstoque.getPistolagemNaoFinalizada(produto.getId());
            atualizaListaProdutoCombo();
            txtCodigo.setText("");
            listView.setAdapter(new BarcodeAdapter(ProdutoAuditagemActivity.this,
                    R.layout.item_codigobarra_range, itensCod, UsoCodBarra.GERAL));

            if (itensCod == null)
                tvQuantidade.setText("Quantidade Total: 0");
            else
                tvQuantidade.setText("Quantidade Total: " + CodigoBarra.quantidadeBipada(itensCod, UsoCodBarra.GERAL));

        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoAuditagemActivity.this, ex.getMessage(), false);
        }
    }

    private void atualizaListaProdutoCombo() {
        if (produto.getQtdCombo() <= 0) {
            llFalta.setVisibility(View.GONE);
            return;
        }

        produtoCombo = CodigoBarra.retornaCombo(produto.getQtdCombo(), itensCod, UsoCodBarra.GERAL);
        tvQtdeBipada.setText(String.valueOf(produtoCombo.getQtdTotal()));
        tvFaltaBipar.setText(String.valueOf(produtoCombo.getQtdFalta()));
        llFalta.setVisibility(produtoCombo.getQtdFalta() == 0 ? View.GONE : View.VISIBLE);
    }

    private void salvar() {
        try {
            atualizaLista();
            int quantidadebipada;
            int quantidadeBipadaAntiga = 0;

            if (produto.getQtdCombo() > 0)
                quantidadebipada = produtoCombo.getQtdTotal();
            else {
                quantidadebipada = CodigoBarra.quantidadeBipada(itensCod, UsoCodBarra.GERAL, this);
                quantidadeBipadaAntiga = CodigoBarra.quantidadeBipada(itensCod, UsoCodBarra.GERAL);
            }

            if (quantidadeBipadaAntiga > quantidadebipada) {
                Alerta alerta = new Alerta(this, getString(R.string.app_name), "Existem ICCIDs dentro do range que não estão no seu estoque. Serão contabilizados apenas os ICCIDs em estoque.");
                alerta.show((dialog, which) -> finalizarAuditagem(quantidadebipada));
            } else {
                finalizarAuditagem(quantidadebipada);
            }

        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoAuditagemActivity.this, ex.getMessage(), false);
        }
    }

    private AuditagemEstoque obterAuditagem(Produto produto, int qtdeInformada, List<CodBarra> codigosList) {
        AuditagemEstoque auditagemEstoque = new AuditagemEstoque();
        auditagemEstoque.setIdProduto(produto.getId());
        auditagemEstoque.setNomeProduto(produto.getNome());
        auditagemEstoque.setQtdeInformada(qtdeInformada);
        auditagemEstoque.setQtdeReal(produto.getEstoqueAtual());
        auditagemEstoque.setCodigosList(codigosList);
        return auditagemEstoque;
    }

    private void acrescentarProdutos() {
        List<Produto> produtos = dbEstoque.getProdutosComEstoque();
        List<AuditagemEstoque> auditagens = dbEstoque.getAuditagemEstoqueHoje();
        if (produtos.isEmpty() || !auditagens.isEmpty()) {
            return;
        }

        Stream.ofNullable(produtos)
                .filter(value -> !dbEstoque.isProdutoCombo(value.getId()))
                .filter(value -> !value.getId().equalsIgnoreCase(produto.getId()))
                .forEach(produto -> dbEstoque.addAuditagemEstoque(
                        obterAuditagem(produto, 0, new ArrayList<>())));
    }

    private void finalizarAuditagem(int quantidadebipada) {
        if (produto.getQtdCombo() > 0 && produtoCombo.getQtdFalta() > 0) {
            Mensagens.faltaEscanearCodBarra(ProdutoAuditagemActivity.this, produtoCombo.getQtdFalta());
            return;
        }

        if (quantidadebipada <= 0) {
            Mensagens.nenhumItemIncluido(ProdutoAuditagemActivity.this);
            return;
        }

        if (quantidadebipada > produto.getEstoqueAtual()) {
            quantidadebipada = produto.getEstoqueAtual();
        }

        acrescentarProdutos();

        // Remover os códigos Barras já adicionados antes da edição
        if (!Util_IO.isNullOrEmpty(idAuditagem)) {
            dbEstoque.removerIccidAuditagem(idAuditagem);
        }

        AuditagemEstoque auditagemEstoque = obterAuditagem(produto, quantidadebipada, itensCod);
        dbEstoque.addAuditagemEstoque(auditagemEstoque, ProdutoAuditagemActivity.this);
        dbEstoque.confirmaPistolagem(produto.getId());

        setResult(RESULT_OK);
        finish();
    }
}