package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemAuditagemEstoqueAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class AuditagemActivity extends AppCompatActivity {

    public static final int BUTTON_WAIT_DURATION = 2200;
    private static final int DELAY_BUTTON_DURATION = 1000;

    @BindView(R.id.btnAddProduto) Button btnAdd;
    @BindView(R.id.txtQuantidade) EditText txtQtd;
    @BindView(R.id.produtos) ExpandableListView lisItens;
    @BindView(R.id.auditagem_ll_quantidade) LinearLayout llQuantidade;
    @BindView(R.id.auditagem_ll_bipagem) LinearLayout llBipagem;
    @BindView(R.id.auditagem_bt_salvar_auditagem) Button btSalvarAuditagem;
    @BindView(R.id.spinnerProduto) SearchableSpinner cbproduto;

    private DBEstoque dbEstoque;
    private Produto produto;
    private String itensAnterior;
    private String itensAtual;
    private ArrayList<Produto> listaProdutos;
    private ArrayList<AuditagemEstoque> itens;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String SIM = "S";

    private java.util.Set<Integer> operadorasObrigatorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditagem);
        ButterKnife.bind(this);

        Utilidades.getDataServidorESalvaBanco(AuditagemActivity.this);

        criarToolbar();

        try {
            itensAnterior = new Gson().toJson(new DBEstoque(this).getAuditagem());
            criarObjetos();
            atualizarLista();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    protected void onResume() {
        SimpleDbHelper.INSTANCE.open(AuditagemActivity.this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (sairActivity()) super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.ProdutoVenda && resultCode == Activity.RESULT_OK) {
            atualizarLista();
            Mensagens.produtoAdicionado(AuditagemActivity.this);
        }
    }

    private void criarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = "Auditagem de Estoque";
        setTitle(title);
        toolbar.setTitle(title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        compositeDisposable.add(RxToolbar.navigationClicks(toolbar)
                .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> sairActivity(), Timber::e));
    }

    private void criarObjetos() {
        dbEstoque = new DBEstoque(AuditagemActivity.this);

        ArrayList<Produto> base = dbEstoque.getProdutosComEstoque();
        if (base == null || base.isEmpty()) {
            Utilidades.retornaMensagem(this, "Sem produtos com estoque para auditagem.", true);
            finish();
            return;
        }
        ArrayList<Produto> filtrada = new ArrayList<>();

        for (Produto p : base) {
            if ("S".equalsIgnoreCase(p.getBipagemAuditoria())) {
                filtrada.add(p);
            }
        }

        listaProdutos = filtrada;
        cbproduto = (SearchableSpinner) findViewById(R.id.spinnerProduto);
        cbproduto.setAdapter(UtilAdapter.adapterProduto(AuditagemActivity.this, listaProdutos));
        Utilidades.defineSpinner(cbproduto);

    }

    private void criarEventos() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onAddClick();
                }
                catch (Exception ex)
                {
                    Alerta alert = new Alerta(view.getContext(), "Erro", "Erro: " + ex.getMessage());
                    alert.show();
                    return;
                }
            }
        });

        compositeDisposable.add(RxView.clicks(btSalvarAuditagem)
                .throttleFirst(DELAY_BUTTON_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> salvar(), throwable -> {
                    Mensagens.mensagemErro(AuditagemActivity.this, throwable.getMessage(), true);
                    Timber.e(throwable);
                }));

        cbproduto.setOnItemSelectedListener(getCbItemSelectedListener());
    }

    private boolean sairActivity() {
        atualizarLista();
        if (itensAnterior.equalsIgnoreCase(itensAtual)) {
            Intent intent = new Intent(AuditagemActivity.this, AuditagemVendedorActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        Alerta alerta1 = new Alerta(AuditagemActivity.this, getString(R.string.app_name), "Deseja cancelar este procedimento?");
        alerta1.showConfirm((dialog, which) -> sairActivityConfirmado(), null);
        return false;
    }

    private void sairActivityConfirmado() {
        try {
            dbEstoque.deleteAuditagensNaoConfirmados();
            Type listType = new TypeToken<List<AuditagemEstoque>>() {
            }.getType();
            List<AuditagemEstoque> temp = new Gson().fromJson(itensAnterior, listType);
            for (AuditagemEstoque auditagemEstoque : temp) {
                dbEstoque.addAuditagemEstoque(auditagemEstoque);
            }
            Intent intent = new Intent(AuditagemActivity.this, AuditagemVendedorActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemActivity.this, ex.getMessage(), false);
        }
    }

    private AuditagemEstoque obterAuditagem(Produto produto, int qtdeInformada) {
        AuditagemEstoque auditagemEstoque = new AuditagemEstoque();
        auditagemEstoque.setIdProduto(produto.getId());
        auditagemEstoque.setNomeProduto(produto.getNome());
        auditagemEstoque.setQtdeInformada(qtdeInformada);
        auditagemEstoque.setQtdeReal(produto.getEstoqueAtual());
        auditagemEstoque.setCodigosList(new ArrayList<>());
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
                .forEach(produto -> dbEstoque.addAuditagemEstoque(obterAuditagem(produto, 0)));
    }

    private void atualizarLista() {
        try {
            ArrayList<AuditagemEstoque> auditagem = dbEstoque.getAuditagem();
            itens = (ArrayList<AuditagemEstoque>) Stream.ofNullable(auditagem)
                    .filter(produto -> produto.getQtdeInformada() > 0)
                    .toList();
            itensAtual = new Gson().toJson(auditagem);
            lisItens.setAdapter(new ItemAuditagemEstoqueAdapter(AuditagemActivity.this, itens, true));

        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemActivity.this, ex.getMessage(), false);
        }
    }

    private boolean validacoesProduto() {
        if (!Validacoes.validacaoDataAparelho(AuditagemActivity.this)) {
            return false;
        }

        if (produto == null) {
            Mensagens.produtoNaoSelecionado(AuditagemActivity.this);
            return false;
        }

        if (dbEstoque.isProdutoCombo(produto.getId())) {
            Alerta alerta = new Alerta(AuditagemActivity.this, produto.getNome(),
                    "Não permitido a auditagem de produtos combos, Verifique!");
            alerta.show();
            return false;
        }

        if (dbEstoque.verificaProdutoAuditadoHoje(produto.getId())) {
            Alerta alerta = new Alerta(AuditagemActivity.this, produto.getNome(),
                    "Já foi realizada uma auditagem deste produto hoje, Verifique!");
            alerta.show();
            return false;
        }

        if (itens != null && itens.size() > 0) {
            for (AuditagemEstoque item : itens) {
                if (item.getIdProduto().equals(produto.getId())) {
                    Mensagens.produtoJaIncluido(AuditagemActivity.this);
                    return false;
                }
            }
        }

        return true;
    }

    private void onAddClick() {
        if (!validacoesProduto()) {
            return;
        }

        if (produto.getBipagemAuditoria().equals("S")) {
            Intent intent = new Intent(AuditagemActivity.this, ProdutoAuditagemActivity.class);
            intent.putExtra(Config.CodigoProduto, produto.getId());
            startActivityForResult(intent, RequestCode.ProdutoVenda);
            return;
        }

        if (produto.getBipagemAuditoria().equals("N")) {
            int iQuantidade;
            try {
                iQuantidade = Integer.parseInt(txtQtd.getText().toString().trim());
            } catch (Exception ex) {
                iQuantidade = 0;
            }

            if (iQuantidade <= 0) {
                Utilidades.retornaMensagem(AuditagemActivity.this, "Verifique quantidade", false);
                return;
            }

            if (iQuantidade > produto.getEstoqueAtual()) {
                iQuantidade = produto.getEstoqueAtual();
            }

            acrescentarProdutos();
            dbEstoque.addAuditagemEstoque(obterAuditagem(produto, iQuantidade));

            atualizarLista();
            Utilidades.retornaMensagem(AuditagemActivity.this, "Produto adicionado", false);
            txtQtd.setText("");
        }
    }

    private void salvar() {
        if (!Validacoes.validacaoDataAparelho(AuditagemActivity.this)) {
            return;
        }
        Intent intent = new Intent(AuditagemActivity.this, AuditagemVendedorActivity.class);
        startActivity(intent);
        finish();
    }

    private AdapterView.OnItemSelectedListener getCbItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listaProdutos == null || listaProdutos.isEmpty() || position < 0 || position >= listaProdutos.size()) {
                    produto = null;
                    return;
                }

                produto = listaProdutos.get(position);

                boolean bipar = isBobina(produto);
                llQuantidade.setVisibility(bipar ? View.VISIBLE : View.GONE);
                llBipagem.setVisibility(bipar ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private boolean isBobina(@Nullable Produto p) {
        if (p == null) return false;
        String desc = p.getDescricao();
        return desc != null && desc.toUpperCase().contains("BOBINA");
    }
}