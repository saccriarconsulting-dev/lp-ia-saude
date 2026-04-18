package com.axys.redeflexmobile.ui.redeflex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.BarcodeAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

public class ProdAudClienteActivity extends AppCompatActivity {

    public static final int BUTTON_WAIT_DURATION = 2200;

    Produto produto;
    EditText txtCodigo;
    ListView listView;
    ArrayList<CodBarra> itensCod;
    int QtdBipagem;
    ImageButton btnScan, btnAddCodigoBarra;
    CodBarra linha;
    BarcodeAdapter itemsAdapter;
    Cliente cliente;
    CodBarra newCodBarra;
    ProdutoCombo produtoCombo;
    DBEstoque dbEstoque;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_produto_auditagem);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String idProduto = null;
        String idCliente = null;
        Bundle args = getIntent().getExtras();
        if (args != null) {
            idProduto = args.getString(Config.CodigoProduto);
            idCliente = args.getString(Config.CodigoCliente);
        }

        if (Util_IO.isNullOrEmpty(idProduto)) {
            Mensagens.produtoNaoEncontrado(ProdAudClienteActivity.this);
            return;
        }

        if (Util_IO.isNullOrEmpty(idCliente)) {
            Mensagens.clienteNaoEncontrado(ProdAudClienteActivity.this, true);
            return;
        }

        criarObjetos(idProduto, idCliente);
        criarEventos();
        abrirCamera();
    }

    private void criarObjetos(String idProduto, String idCliente) {
        try {
            dbEstoque = new DBEstoque(ProdAudClienteActivity.this);
            produto = dbEstoque.getProdutoById(idProduto);
            newCodBarra = null;
            if (produto == null) {
                Mensagens.produtoNaoEncontrado(ProdAudClienteActivity.this);
                return;
            }

            cliente = new DBCliente(this).getById(idCliente);
            if (cliente == null) {
                Mensagens.clienteNaoEncontrado(ProdAudClienteActivity.this, true);
                return;
            }


            QtdBipagem = 0;
            produtoCombo = new ProdutoCombo();

            if (produto.getQtdCombo() > 0)
                (findViewById(R.id.combo)).setVisibility(View.VISIBLE);

            txtCodigo = findViewById(R.id.txtCodigoBarras);
            listView = findViewById(R.id.listView);
            itensCod = new ArrayList<>();
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(produto.getId() + " - " + produto.getNome());
            btnScan = findViewById(R.id.button);
            btnAddCodigoBarra = findViewById(R.id.buttonBuscar);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdAudClienteActivity.this, ex.getMessage(), true);
        }
    }

    private void criarEventos() {
        btnScan.setOnClickListener((view) -> {
            abrirCamera();
        });

        btnAddCodigoBarra.setOnClickListener(view -> {
            verificaInclusaoCodBarra(txtCodigo.getText().toString());
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int posicao = position;
                if (itensCod != null && itensCod.size() > 0) {
                    Alerta alerta = new Alerta(ProdAudClienteActivity.this, getResources().getString(R.string.app_name), "Deseja realmente excluir?");
                    alerta.showConfirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itensCod.remove(posicao);
                            atualizarlista();
                        }
                    }, null);
                }
            }
        });

        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            Alerta alerta = new Alerta(ProdAudClienteActivity.this, getResources().getString(R.string.app_name), "Os dados não foram salvos. Deseja continuar sem salvar?");
                            alerta.showConfirm((dialog, which) -> {
                                onBackPressed();
                            }, null);
                        }, Timber::e)
        );
    }

    private void abrirCamera() {
        try {
            txtCodigo.setText("");
            CodeReader.openCodeReader(this, IntentIntegrator.CODE_128);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdAudClienteActivity.this, ex.getMessage(), false);
        }
    }

    protected void onResume() {
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    private void verificaInclusaoCodBarra(final String pCodBarra) {
        if (Util_IO.isNullOrEmpty(pCodBarra)) {
            Utilidades.retornaMensagem(ProdAudClienteActivity.this, "Informe o código", false);
            return;
        }

        Alerta alerta = new Alerta(this, pCodBarra, "Deseja incluir esse código de barra?");
        alerta.showConfirm((dialog, which) -> {
            try {
                if (produto != null) {
                    if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !pCodBarra.startsWith(produto.getIniciaCodBarra())) {
                        Alerta alerta1 = new Alerta(ProdAudClienteActivity.this, pCodBarra, "Código de Barra deve começar com: " + produto.getIniciaCodBarra());
                        alerta1.show((dialog1, which1) -> {
                            QtdBipagem = 0;
                            abrirCamera();
                        });
                        return;
                    }

                    if (produto.getQtdCodBarra() > 0 && pCodBarra.trim().length() != produto.getQtdCodBarra()) {
                        new Alerta(ProdAudClienteActivity.this, pCodBarra, "Código de Barra deve conter " + produto.getQtdCodBarra() + " caracteres")
                                .show((dialog1, which1) -> {
                                    QtdBipagem = 0;
                                    abrirCamera();
                                });
                        return;
                    }
                }

                if (QtdBipagem == 1) {
                    SetCodBarra(pCodBarra, false);
                    if (verificaLinhaDuplicada()) return;

                    if (!CodigoBarra.validaQuantidadeRange(linha)) {
                        new Alerta(ProdAudClienteActivity.this, "Erro", "Favor conferir o ICCID inicial e final")
                                .show((dialog1, which1) -> {
                                    QtdBipagem = 0;
                                    abrirCamera();
                                });
                        return;
                    }

                    itensCod.add(linha);
                    QtdBipagem = 0;
                    atualizarlista();
                    abrirCamera();
                    return;
                }

                Alerta alerta1 = new Alerta(ProdAudClienteActivity.this, pCodBarra, "Código de barra unitário?");
                alerta1.showConfirm((dialog2, which2) -> {
                    SetCodBarra(pCodBarra, true);
                    if (verificaLinhaDuplicada())
                        return;

                    itensCod.add(linha);
                    atualizarlista();
                    abrirCamera();
                }, (dialog2, which2) -> {
                    SetCodBarra(pCodBarra, false);
                    abrirCamera();
                });
            } catch (Exception ex) {
                Mensagens.mensagemErro(ProdAudClienteActivity.this, ex.getMessage(), false);
            }
        }, (dialog, which) -> {
            abrirCamera();
        });
    }

    private void atualizarlista() {
        if (produto.getQtdCombo() > 0) {
            produtoCombo = CodigoBarra.retornaCombo(produto.getQtdCombo(), itensCod, UsoCodBarra.AUDITAGEM_CLIENTE);
            ((TextView) findViewById(R.id.txtQtdBipado)).setText(String.valueOf(produtoCombo.getQtdTotal()));
            if (produtoCombo.getQtdFalta() == 0)
                (findViewById(R.id.falta)).setVisibility(View.GONE);
            else
                (findViewById(R.id.falta)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txtQtdFaltaBipar)).setText(String.valueOf(produtoCombo.getQtdFalta()));
        }
        itemsAdapter = new BarcodeAdapter(ProdAudClienteActivity.this, R.layout.item_codigobarra_range, itensCod, UsoCodBarra.AUDITAGEM_CLIENTE);
        listView.setAdapter(itemsAdapter);
        txtCodigo.setText("");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                runOnUiThread(() -> getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE));
                new Handler().postDelayed(() -> {
                    runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
                    verificaInclusaoCodBarra(result.getContents());
                }, 1000);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdAudClienteActivity.this, ex.getMessage(), false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solic_merc, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_salvar:
                salvar();
                break;
            case R.id.action_limpar:
                itensCod = new ArrayList<>();
                atualizarlista();
                break;
            case R.id.action_addproduto:
                abrirCamera();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        try {
            int quantidadebipada;
            if (produto.getQtdCombo() > 0)
                quantidadebipada = produtoCombo.getQtdTotal();
            else
                quantidadebipada = CodigoBarra.quantidadeBipada(itensCod, UsoCodBarra.AUDITAGEM_CLIENTE);

            if (produto.getQtdCombo() > 0 && produtoCombo.getQtdFalta() > 0) {
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name),
                        "Está faltando escanear " + produtoCombo.getQtdFalta() + " " + ((produtoCombo.getQtdFalta() == 1) ? "código" : "codigos") + " de barra, Verifique!");
                alerta.show();
                return;
            }

            if (quantidadebipada <= 0) {
                Alerta alerta = new Alerta(ProdAudClienteActivity.this, getResources().getString(R.string.app_name), "Nenhum produto foi selecionado, Verifique!");
                alerta.show();
                return;
            }

            AuditagemCliente auditagem = new AuditagemCliente();
            auditagem.setIdProduto(produto.getId());
            auditagem.setQuantidade(quantidadebipada);
            auditagem.setIdCliente(cliente.getId());
            auditagem.setListaCodigos(itensCod);
            dbEstoque.addAuditagemCliente(auditagem);

            setResult(RESULT_OK);
            finish();
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdAudClienteActivity.this, ex.getMessage(), false);
        }
    }

    private void SetCodBarra(String pCodBarra, Boolean pIsIndividual) {
        CodBarra codAdd = new CodBarra();
        codAdd.setIndividual(pIsIndividual);
        codAdd.setIdProduto(produto.getId());

        if (pIsIndividual)
            codAdd.setCodBarraInicial(pCodBarra);
        else if (QtdBipagem == 0) {
            codAdd.setCodBarraInicial(pCodBarra);
            QtdBipagem = 1;
        } else {
            linha.setCodBarraFinal(pCodBarra);
            return;
        }

        linha = codAdd;
    }

    private Boolean verificaLinhaDuplicada() {
        ArrayList<CodBarra> itensCodValidacao = new ArrayList<>();
        itensCodValidacao.addAll(itensCod);

        ArrayList<AuditagemCliente> auditagemRealizadas = dbEstoque.getAuditagensCliente(cliente.getId());
        for (AuditagemCliente auditagemCliente : auditagemRealizadas) {
            itensCodValidacao.addAll(auditagemCliente.getListaCodigos());
        }

        if (CodigoBarra.verificaICCIDDuplicado(linha, itensCodValidacao)
                || (!linha.getIndividual() && QtdBipagem == 1
                && Long.parseLong(linha.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE)) < 2)) {
            Alerta alerta = new Alerta(ProdAudClienteActivity.this, "Informação", "Item já adicionado");
            alerta.show((dialog, which) -> {
                linha = null;
                QtdBipagem = 0;
                abrirCamera();
            });
            return true;
        }

        if (!linha.getIndividual()) {
            Long codigoBarraInicialSemDigito = Long.parseLong(CodigoBarra.retornaICCIDSemDigito(linha.getCodBarraInicial()));
            Long digitoCodigoBarraInicial = Long.valueOf(CodigoBarra.retornaDigitoVerificadorICCID(linha.getCodBarraInicial()));

            Long codigoBarraFinalSemDigito = Long.parseLong(CodigoBarra.retornaICCIDSemDigito(linha.getCodBarraFinal()));
            Long digitoCodigoBarraFinal = Long.valueOf(CodigoBarra.retornaDigitoVerificadorICCID(linha.getCodBarraFinal()));

            if (codigoBarraInicialSemDigito >= codigoBarraFinalSemDigito && digitoCodigoBarraInicial >= digitoCodigoBarraFinal) {
                Alerta alerta = new Alerta(ProdAudClienteActivity.this, "Informação", "Item não é sequencial");
                alerta.show((dialog, which) -> {
                    linha = null;
                    QtdBipagem = 0;
                    abrirCamera();
                });
                return true;
            }
        }


        return false;
    }
}