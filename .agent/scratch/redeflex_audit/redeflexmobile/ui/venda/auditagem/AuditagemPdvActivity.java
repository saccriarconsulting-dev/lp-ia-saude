package com.axys.redeflexmobile.ui.venda.auditagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.AuditagemClienteAdapter;
import com.axys.redeflexmobile.shared.bd.DBAudOpe;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.services.tasks.EstoqueSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.New_Activity_ProdutoAuditagem;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class AuditagemPdvActivity extends BaseActivity implements IAuditagemPdvView {

    public static final int BUTTON_WAIT_DURATION = 2200;

    private static final String SIM = "S";

    @BindView(R.id.auditagem_pdv_sp_produto) SearchableSpinner spProdutos;
    @BindView(R.id.auditagem_pdv_ll_quantidade) LinearLayout llQuantidade;
    @BindView(R.id.auditagem_pdv_ll_bipagem) LinearLayout llBipagem;
    @BindView(R.id.auditagem_pdv_bt_adicionar_produto) Button btAdicionarProduto;
    @BindView(R.id.auditagem_pdv_elv_produtos) ExpandableListView elvProdutos;
    @BindView(R.id.auditagem_pdv_bt_salvar_auditagem) Button btSalvarAuditagem;
    @BindView(R.id.auditagem_pdv_et_quantidade) EditText etQuantidade;
    @BindView(R.id.auditagem_pdv_tv_titulo_produto) TextView tvTituloProdutos;

    private AuditagemClienteAdapter estoqueAdapter;
    private DBEstoque dbEstoque;
    private DBCliente dbCliente;
    private DBAudOpe dbAudOpe;
    private Produto produto;
    private Cliente cliente;
    private ArrayList<Produto> listaProdutos;
    private ArrayList<AuditagemCliente> itens;
    private Toolbar toolbar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private java.util.Set<Integer> operadorasObrigatorias;

    @Override
    protected int getContentView() {
        return R.layout.activity_auditagem_pdv;
    }

    @Override
    protected void initialize() {
        criarToolbar();
        try {
            String idCliente = null;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                idCliente = bundle.getString(Config.CodigoCliente);
            }

            if (Util_IO.isNullOrEmpty(idCliente)) {
                Mensagens.clienteNaoEncontrado(this, true);
                return;
            }

            dbCliente = new DBCliente(this);
            cliente = dbCliente.getById(idCliente);
            if (cliente == null) {
                Mensagens.clienteNaoEncontrado(this, true);
                return;
            }

            criarObjetos();
            atualizarLista();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @OnClick(R.id.auditagem_pdv_bt_adicionar_produto)
    public void onClickAdicionarProduto() {
        try {
            if (produto == null) {
                Utilidades.retornaMensagem(
                        this,
                        getStringByResId(R.string.auditagem_pdv_erro_produto_nao_informado),
                        true
                );
                return;
            }

            if (cliente == null) {
                Mensagens.mensagemErro(this, "Cliente não informado para auditagem.", true);
                return;
            }

            if (verificaDuplicado()) {
                Utilidades.retornaMensagem(
                        this,
                        getStringByResId(R.string.auditagem_pdv_erro_item_ja_incluido),
                        true
                );
                return;
            }

            // Fluxo 1: bipagem (regra única - mesma da UI)
            if (exigeBipagem(produto)) {
                Intent intent = new Intent(this, New_Activity_ProdutoAuditagem.class);
                intent.putExtra(Config.CodigoProduto, produto.getId());
                intent.putExtra(Config.CodigoCliente, cliente.getId());
                startActivityForResult(intent, Config.RequestCodeAuditoria);
                return;
            }

            // Fluxo 2: quantidade manual (somente quando não exige bipagem)
            int iQuantidade = 0;
            try {
                iQuantidade = Integer.parseInt(etQuantidade.getText().toString().trim());
            } catch (Exception ignore) {
                iQuantidade = 0;
            }

            if (iQuantidade <= 0) {
                Utilidades.retornaMensagem(
                        this,
                        getStringByResId(R.string.auditagem_pdv_erro_verifique_quantidade),
                        true
                );
                etQuantidade.requestFocus();
                return;
            }

            if (iQuantidade > produto.getEstoqueAtual()) {
                Utilidades.retornaMensagem(
                        this,
                        getStringByResId(R.string.auditagem_pdv_erro_saldo_estoque),
                        true
                );
                return;
            }

            try {
                AuditagemCliente auditagem = new AuditagemCliente();
                auditagem.setIdProduto(produto.getId());
                auditagem.setQuantidade(iQuantidade);
                auditagem.setIdCliente(cliente.getId());

                dbEstoque.addAuditagemCliente(auditagem);
                atualizarLista();

                Utilidades.retornaMensagem(
                        this,
                        getStringByResId(R.string.auditagem_pdv_ok_produto_adicionado),
                        true
                );
                etQuantidade.setText("");

            } catch (Exception ex) {
                Utilidades.retornaMensagem(
                        this,
                        getStringByResId(R.string.auditagem_pdv_erro_adicionar_produto, ex.getMessage()),
                        true
                );
            }

        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    private boolean exigeBipagem(@Nullable Produto p) {
        if (p == null) return false;
        if (cliente == null) return false;

        if (!SIM.equalsIgnoreCase(p.getBipagemCliente())) return false;

        if (this.operadorasObrigatorias == null || this.operadorasObrigatorias.isEmpty()) return true;

        return this.operadorasObrigatorias.contains(p.getOperadora());
    }


    @OnClick(R.id.auditagem_pdv_bt_salvar_auditagem)
    public void onClickSalvarAuditagem() {
        try {
            String strMensagem = getStringByResId(R.string.auditagem_pdv_confirma_auditagem);
            if (itens == null || itens.isEmpty()) {
                strMensagem = getStringByResId(R.string.auditagem_pdv_nada_auditado);
            }

            Alerta alerta = new Alerta(
                    this,
                    getResources().getString(R.string.app_name),
                    strMensagem
            );
            alerta.showConfirm((dialog, which) -> {
                if (itens == null || itens.isEmpty()) {
                    // dbEstoque.deletaAuditagemClienteNaoConfirmada(cliente.getId());
                    AuditagemCliente auditagem = new AuditagemCliente();
                    auditagem.setQuantidade(0);
                    auditagem.setIdCliente(cliente.getId());
                    dbEstoque.addAuditagemCliente(auditagem);

                    dbEstoque.confirmaAuditagemCliente(cliente.getId());
                } else {
                    dbEstoque.confirmaAuditagemCliente(cliente.getId());
                    new EstoqueSyncTask(this, 1).execute();
                    Utilidades.retornaMensagem(
                            this,
                            getStringByResId(R.string.auditagem_pdv_finalizar_auditagem),
                            true
                    );
                }

                dbCliente.confirmaAuditagem(cliente.getId());
                setResult(Activity.RESULT_OK);
                finish();

            }, null);
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    @Override
    public void onBackPressed() {
        Alerta alerta1 = new Alerta(
                this,
                getResources().getString(R.string.app_name),
                getString(R.string.auditagem_pdv_info_cancelar_auditagem)
        );
        alerta1.showConfirm((dialog, which) -> {
            dbEstoque.deletaAuditagemClienteNaoConfirmada(cliente.getId());
            setResult(Activity.RESULT_OK);
            finish();
        }, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    private void criarObjetos() {
        try {
            dbAudOpe = new DBAudOpe(this);
            dbEstoque = new DBEstoque(this);

            if (cliente == null) {
                Mensagens.mensagemErro(this, "Cliente não informado para auditagem.", true);
                finish();
                return;
            }

            // 1) Tenta carregar parâmetros (operadoras obrigatórias) para este cliente.
            this.operadorasObrigatorias = dbAudOpe.getOperadorasObrigatorias(cliente.getId());

            // 2) Base: produtos com estoque
            ArrayList<Produto> base = dbEstoque.getProdutosComEstoque();
            if (base == null || base.isEmpty()) {
                Utilidades.retornaMensagem(this, "Sem produtos com estoque para auditagem.", true);
                finish();
                return;
            }


            //filtrada

            ArrayList<Produto> filtrada = new ArrayList<>();
            for (Produto p : base) {
                if ("S".equalsIgnoreCase(p.getBipagemAuditoria())) {
                    filtrada.add(p);
                }
            }

            listaProdutos = filtrada;
            // end filtrada

            spProdutos.setAdapter(UtilAdapter.adapterProduto(this, listaProdutos));
            Utilidades.defineSpinner(spProdutos);

        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    private void criarEventos() {
        spProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
    }

    private boolean isBobina(@Nullable Produto p) {
        if (p == null) return false;

        String desc = p.getDescricao();
        return desc != null && desc.toUpperCase().contains("BOBINA");
    }

    private void atualizarLista() {
        try {
            itens = dbEstoque.getAuditagensCliente(cliente.getId());
            tvTituloProdutos.setVisibility(
                    (itens != null && itens.isEmpty()) ? View.GONE : View.VISIBLE
            );

            estoqueAdapter = new AuditagemClienteAdapter(this, itens, true);
            estoqueAdapter.remocaoProdutoAuditagemPdv(this::atualizarLista);
            elvProdutos.setAdapter(estoqueAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.RequestCodeAuditoria && resultCode == Activity.RESULT_OK) {
            atualizarLista();
            Utilidades.retornaMensagem(this, "Produto adicionado", true);
        }
    }

    private boolean verificaDuplicado() {
        if (itens != null && !itens.isEmpty()) {
            for (AuditagemCliente auditagemCliente : itens) {
                if (auditagemCliente.getIdProduto().equals(produto.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void criarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mudarTitulo(getString(R.string.auditagem_pdv_titulo_toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    private void mudarTitulo(String texto) {
        setTitle(texto);
        toolbar.setTitle(texto);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(texto);
        }
    }

    public interface RemoverProduto {
        void onRemover();
    }
}