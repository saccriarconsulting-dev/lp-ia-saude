package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.BarcodeAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.enums.EnumTipoAuditagem;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.RetCodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ProdutoVendaActivity extends AppCompatActivity {
    ImageButton scanBtn, btnAddCodigoBarra;
    EditText txtCodigo;
    DBEstoque dbEstoque;
    Produto produto;
    DBVenda dbVenda;
    Venda venda;
    ArrayList<CodBarra> itensCod;
    ListView listView;
    CodBarra linha;
    int QtdBipagem;
    DBPreco dbPreco;
    PrecoDiferenciado precoDiferenciado;
    ProdutoCombo produtoCombo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_venda);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String idp = null, codigoPreco = "";
        int idVenda = 0, idPreco = 0;

        if (bundle != null) {
            idp = bundle.getString(Config.CodigoProdutoVenda);
            idVenda = bundle.getInt(Config.CodigoVenda);
            codigoPreco = bundle.getString(Config.CodigoPreco);
        }

        if (!Util_IO.isNullOrEmpty(codigoPreco))
            idPreco = Integer.parseInt(codigoPreco);

        if (idVenda == 0) {
            Mensagens.vendaNaoIniciada(ProdutoVendaActivity.this);
            return;
        }

        try {
            criarObjetos();
            venda = dbVenda.getVendabyId(idVenda);
            if (venda == null) {
                Mensagens.vendaNaoIniciada(ProdutoVendaActivity.this);
                return;
            }

            if (Util_IO.isNullOrEmpty(idp)) {
                Mensagens.produtoNaoEncontrado(ProdutoVendaActivity.this);
                return;
            }

            produto = dbEstoque.getProdutoById(idp);
            if (produto == null) {
                Mensagens.produtoNaoEncontrado(ProdutoVendaActivity.this);
                return;
            }

            precoDiferenciado = dbPreco.getPrecoById(String.valueOf(idPreco));
            getSupportActionBar().setTitle(produto.getId() + " - " + produto.getNome());

            if (produto.getQtdCombo() > 0)
                (findViewById(R.id.combo)).setVisibility(View.VISIBLE);

            atualizalista();
            abrirCamera();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoVendaActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        produtoCombo = new ProdutoCombo();
        scanBtn = (ImageButton) findViewById(R.id.button);
        dbVenda = new DBVenda(ProdutoVendaActivity.this);
        dbPreco = new DBPreco(ProdutoVendaActivity.this);
        dbEstoque = new DBEstoque(ProdutoVendaActivity.this);
        btnAddCodigoBarra = (ImageButton) findViewById(R.id.buttonBuscar);
        listView = (ListView) findViewById(R.id.listView);
        itensCod = new ArrayList<>();
        txtCodigo = (EditText) findViewById(R.id.txtCodigoBarras);
    }

    private void criarEventos() {
        scanBtn.setOnClickListener((view) -> {
            abrirCamera();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final int posicao = position;
            if (itensCod != null && itensCod.size() > 0) {
                Alerta alerta = new Alerta(ProdutoVendaActivity.this, getResources().getString(R.string.app_name), "Deseja realmente excluir?");
                alerta.showConfirm((dialog, which) -> {
                    CodBarra codBarra = itensCod.get(posicao);
                    dbEstoque.deletarPistolagemById(codBarra.getIdPistolagem());
                    atualizalista();
                }, null);
            }
        });

        btnAddCodigoBarra.setOnClickListener((view) -> {
            try {
                verificaInclusaoCodBarra(txtCodigo.getText().toString());
            } catch (Exception ex) {
                Mensagens.mensagemErro(ProdutoVendaActivity.this, ex.getMessage(), false);
            }
        });
    }

    private void abrirCamera() {
        try {
            if (txtCodigo != null) txtCodigo.setText("");
            CodeReader.openCodeReader((Activity) ProdutoVendaActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoVendaActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    protected void onResume() {
        SimpleDbHelper.INSTANCE.open(ProdutoVendaActivity.this);
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
        dbEstoque.deletarPistolagemNaoFinalizadas(EnumTipoAuditagem.Venda);
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
                Alerta alerta = new Alerta(ProdutoVendaActivity.this, getResources().getString(R.string.app_name), "Os dados não foram salvos. Deseja continuar sem salvar?");
                alerta.showConfirm((dialog, which) -> {
                    onBackPressed();
                }, null);
                return true;
            case R.id.action_salvar:
                salvar();
                break;
            case R.id.action_limpar:
                dbEstoque.deletarPistolagemNaoFinalizadas(EnumTipoAuditagem.Venda);
                atualizalista();
                break;
            case R.id.action_addproduto:
                abrirCamera();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        try {
            double valor = produto.getPrecovenda();
            int quantidadebipada;
            if (produto.getQtdCombo() > 0)
                quantidadebipada = produtoCombo.getQtdTotal();
            else
                quantidadebipada = CodigoBarra.quantidadeBipada(itensCod, UsoCodBarra.GERAL);

            if (produto.getQtdCombo() > 0 && produtoCombo.getQtdFalta() > 0) {
                Alerta alerta = new Alerta(ProdutoVendaActivity.this, getResources().getString(R.string.app_name),
                        "Está faltando escanear " + String.valueOf(produtoCombo.getQtdFalta()) + " " + ((produtoCombo.getQtdFalta() == 1) ? "código" : "codigos") + " de barra, Verifique!");
                alerta.show();
                return;
            }

            if (quantidadebipada <= 0) {
                Mensagens.nenhumItemIncluido(ProdutoVendaActivity.this);
                return;
            }

            if (precoDiferenciado != null)
                valor = precoDiferenciado.getValor();

            if (valor <= 0 && produto.getPermiteVendaSemValor().equalsIgnoreCase("N")) {
                Mensagens.produtoComValorZerado(ProdutoVendaActivity.this);
                return;
            }

            if (!dbEstoque.verificaEstoque(produto.getId(), quantidadebipada)) {
                Mensagens.semEstoque(ProdutoVendaActivity.this);
                return;
            }

            if (precoDiferenciado != null) {
                if (precoDiferenciado.getQtdPreco() > 0 && quantidadebipada > precoDiferenciado.getQtdPreco()) {
                    Mensagens.promocaoAteTantasUnds(ProdutoVendaActivity.this, precoDiferenciado.getQtdPreco());
                    return;
                } else {
                    produto.setQtde(quantidadebipada);
                    long idVendaItem = dbVenda.addItemVenda(venda, produto, itensCod, valor, null, String.valueOf(precoDiferenciado.getId()));
                    dbPreco.atualizaIdVenda(String.valueOf(precoDiferenciado.getId()), String.valueOf(venda.getId()), String.valueOf(idVendaItem), quantidadebipada);
                    dbPreco.atualizaQtdPreco(String.valueOf(precoDiferenciado.getId()), quantidadebipada);
                }
            } else {
                produto.setQtde(quantidadebipada);
                dbVenda.addItemVenda(venda, produto, itensCod, valor, null, null);
            }
            dbEstoque.atualizaEstoque(produto.getId(), true, quantidadebipada);
            dbEstoque.confirmaPistolagem(produto.getId(), venda.getIdCliente(), String.valueOf(venda.getId()));
            setResult(RESULT_OK);
            finish();
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoVendaActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {

            verificaInclusaoCodBarra(result.getContents());
        }
    }

    private void verificaInclusaoCodBarra(final String codBarra) {
        if (Util_IO.isNullOrEmpty(codBarra)) {
            Utilidades.retornaMensagem(ProdutoVendaActivity.this, "Código de barra não informado, Verifique", false);
            return;
        }
        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !codBarra.startsWith(produto.getIniciaCodBarra())) {
            Utilidades.retornaMensagem(ProdutoVendaActivity.this, "Código de Barra deve começar com: " + produto.getIniciaCodBarra(), false);
            return;
        } else if (produto.getQtdCodBarra() > 0 && codBarra.trim().length() != produto.getQtdCodBarra()) {
            Utilidades.retornaMensagem(ProdutoVendaActivity.this, "Código de Barra deve conter " + String.valueOf(produto.getQtdCodBarra()) + " caracteres", false);
            abrirCamera();
            return;
        }
        Alerta alerta = new Alerta(ProdutoVendaActivity.this, codBarra, "Deseja incluir esse código de barra?");
        alerta.showConfirm((dialog, which) -> {
            try {
                if (QtdBipagem == 0) {
                    Alerta alerta1 = new Alerta(ProdutoVendaActivity.this, codBarra, "Código de barra unitário?");
                    alerta1.showConfirm((dialog1, which1) -> {
                        RetCodBarra retCodBarra = CodigoBarra.validacao(codBarra, produto, QtdBipagem, linha, itensCod, true, true, ProdutoVendaActivity.this);
                        if (!retCodBarra.isInclusaoOK()) {
                            Alerta alerta2 = new Alerta(ProdutoVendaActivity.this, "Informação", retCodBarra.getMensagem());
                            alerta2.show((dialog2, which2) -> {
                                abrirCamera();
                            });
                        } else {
                            QtdBipagem = retCodBarra.getQuantidade();
                            linha = retCodBarra.getCodBarra();
                            dbEstoque.addPistolagem(linha, produto.getId(), venda.getIdCliente(), String.valueOf(venda.getId()));
                            atualizalista();
                            abrirCamera();
                        }
                    }, (dialog2, which2) -> {
                        RetCodBarra retCodBarra = CodigoBarra.validacao(codBarra, produto, QtdBipagem, linha, itensCod, false, true, ProdutoVendaActivity.this);
                        linha = retCodBarra.getCodBarra();
                        QtdBipagem = retCodBarra.getQuantidade();
                        abrirCamera();
                    });
                } else {
                    RetCodBarra retCodBarra = CodigoBarra.validacao(codBarra, produto, QtdBipagem, linha, itensCod, false, true, ProdutoVendaActivity.this);
                    if (!retCodBarra.isInclusaoOK()) {
                        Alerta alerta2 = new Alerta(ProdutoVendaActivity.this, "Informação", retCodBarra.getMensagem());
                        alerta2.show((dialog1, which1) -> {
                            abrirCamera();
                        });
                    } else {
                        QtdBipagem = retCodBarra.getQuantidade();
                        linha = retCodBarra.getCodBarra();
                        dbEstoque.addPistolagem(linha, produto.getId(), venda.getIdCliente(), String.valueOf(venda.getId()));
                        atualizalista();
                        abrirCamera();
                    }
                }
            } catch (Exception ex) {
                Mensagens.mensagemErro(ProdutoVendaActivity.this, ex.getMessage(), false);
            }
        }, (dialog, which) -> {
            abrirCamera();
        });
    }

    private void atualizalista() {
        try {
            itensCod = dbEstoque.getPistolagemNaoFinalizada(produto.getId(), venda.getIdCliente(), String.valueOf(venda.getId()));
            if (produto.getQtdCombo() > 0) {
                produtoCombo = CodigoBarra.retornaCombo(produto.getQtdCombo(), itensCod, UsoCodBarra.GERAL);
                ((TextView) findViewById(R.id.txtQtdBipado)).setText(String.valueOf(produtoCombo.getQtdTotal()));
                if (produtoCombo.getQtdFalta() == 0)
                    (findViewById(R.id.falta)).setVisibility(View.GONE);
                else
                    (findViewById(R.id.falta)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.txtQtdFaltaBipar)).setText(String.valueOf(produtoCombo.getQtdFalta()));
            }
            BarcodeAdapter itemsAdapter = new BarcodeAdapter(ProdutoVendaActivity.this, R.layout.item_codigobarra_range, itensCod, UsoCodBarra.GERAL);
            listView.setEmptyView(findViewById(R.id.empty));
            listView.setAdapter(itemsAdapter);
            txtCodigo.setText("");
        } catch (Exception ex) {
            Mensagens.mensagemErro(ProdutoVendaActivity.this, ex.getMessage(), false);
        }
    }
}