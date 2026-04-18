package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.AuditagemClienteAdapter;
import com.axys.redeflexmobile.shared.bd.DBAudOpe;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.services.tasks.EstoqueSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AuditagemClienteActivity extends AppCompatActivity {

    ArrayList<Produto> lsProdutos;
    ArrayList<AuditagemCliente> lsItens;

    DBEstoque dbEstoque;
    DBCliente dbCliente;
    DBAudOpe dbAudOpe;

    SearchableSpinner cbproduto;
    Button btnAdd;
    EditText txtQtd;
    ExpandableListView listaItens;

    Produto produto;
    Cliente cliente;
    AuditagemClienteAdapter mAdapterLista;

    LinearLayout llQuantidade;
    LinearLayout llBipagem;
    Button btnSalvar;

    private ArrayList<Produto> listaProdutos;
    private static final String SIM = "S";

    private java.util.Set<Integer> operadorasObrigatorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditagem_cliente);
        Log.i("BIPAGEM", "AuditagemClienteActivity ");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Auditagem de Estoque");
        }

        try {
            String idCliente = null;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                idCliente = bundle.getString(Config.CodigoCliente);

            if (Util_IO.isNullOrEmpty(idCliente)) {
                Mensagens.clienteNaoEncontrado(AuditagemClienteActivity.this, true);
                return;
            }

            dbCliente = new DBCliente(AuditagemClienteActivity.this);
            cliente = dbCliente.getById(idCliente);
            if (cliente == null) {
                Mensagens.clienteNaoEncontrado(AuditagemClienteActivity.this, true);
                return;
            }

            criarObjetos();
            criarEventos();
            atualizarLista();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemClienteActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        try {
            ((TextView) findViewById(R.id.txtCliente)).setText(String.format("%s - %s", cliente.retornaCodigoExibicao(), cliente.getNomeFantasia()));
            dbAudOpe = new DBAudOpe(AuditagemClienteActivity.this);
            dbEstoque = new DBEstoque(AuditagemClienteActivity.this);
            btnAdd = (Button) findViewById(R.id.btnAddProduto);
            txtQtd = (EditText) findViewById(R.id.txtQuantidade);
            listaItens = (ExpandableListView) findViewById(R.id.produtos);
            llQuantidade = (LinearLayout) findViewById(R.id.auditagem_ll_quantidade);
            llBipagem = (LinearLayout) findViewById(R.id.auditagem_ll_bipagem);
            btnSalvar = findViewById(R.id.auditagem_bt_salvar_auditagem);
            btnSalvar.setVisibility(View.GONE);
        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemClienteActivity.this, ex.getMessage(), true);
        }


        this.operadorasObrigatorias = dbAudOpe.getOperadorasObrigatorias(cliente.getId());

        ArrayList<Produto> base = dbEstoque.getProdutos();
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
        cbproduto.setAdapter(UtilAdapter.adapterProduto(AuditagemClienteActivity.this, listaProdutos));
        Utilidades.defineSpinner(cbproduto);
    }

    @Override
    public void onBackPressed() {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Alerta alerta = new Alerta(AuditagemClienteActivity.this, getResources().getString(R.string.app_name)
                        , "Deseja realmente cancelar a auditagem? As informações serão perdidas?");
                alerta.showConfirm((dialog, which) -> {
                    dbEstoque.deletaAuditagemClienteNaoConfirmada(cliente.getId());
                    try {
                        Utilidades.openRota(AuditagemClienteActivity.this, true);
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(AuditagemClienteActivity.this, ex.getMessage(), false);
                    }
                }, null);
                break;
            case R.id.opcao_salvar:
                salvar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void criarEventos() {
        btnAdd.setOnClickListener(view -> {
            if (produto == null) {
                Utilidades.retornaMensagem(view.getContext(), "Produto não informado, Verifique", true);
                return;
            }

            if (verificaDuplicado()) {
                Utilidades.retornaMensagem(view.getContext(), "Item já incluído", true);
                return;
            }

            if (produto.getBipagemCliente().equalsIgnoreCase("S") && dbAudOpe.obrigarPistolagemAuditoria(cliente.getId(), produto.getOperadora())) {
                Intent intent = new Intent(view.getContext(), ProdAudClienteActivity.class);
                intent.putExtra(Config.CodigoProduto, produto.getId());
                intent.putExtra(Config.CodigoCliente, cliente.getId());
                startActivityForResult(intent, Config.RequestCodeAuditoria);
            } else {

                int iQuantidade;
                try {
                    iQuantidade = Integer.parseInt(txtQtd.getText().toString().trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    iQuantidade = 0;
                }

                if (iQuantidade <= 0) {
                    Utilidades.retornaMensagem(view.getContext(), "Verifique quantidade", true);
                    txtQtd.requestFocus();
                    return;
                }

                try {
                    AuditagemCliente auditagem = new AuditagemCliente();
                    auditagem.setIdProduto(produto.getId());
                    auditagem.setQuantidade(iQuantidade);
                    auditagem.setIdCliente(cliente.getId());

                    dbEstoque.addAuditagemCliente(auditagem);
                    atualizarLista();
                    Utilidades.retornaMensagem(view.getContext(), "Produto adicionado", true);
                    txtQtd.setText("");
                } catch (Exception ex) {
                    Utilidades.retornaMensagem(view.getContext(), "Erro ao adicionar produto: " + ex.getMessage(), true);
                }
            }
        });

        cbproduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private boolean isBobina(@Nullable Produto p) {
        if (p == null) return false;
        String desc = p.getDescricao();
        return desc != null && desc.toUpperCase().contains("BOBINA");
    }


    private boolean exigeBipagem(@Nullable Produto p) {
        if (p == null) return false;
        if (cliente == null) return false;

        if (!SIM.equalsIgnoreCase(p.getBipagemCliente())) return false;

        if (this.operadorasObrigatorias == null || this.operadorasObrigatorias.isEmpty()) return true;

        return this.operadorasObrigatorias.contains(p.getOperadora());
    }

    private void atualizarLista() {
        lsItens = dbEstoque.getAuditagensCliente(cliente.getId());
        mAdapterLista = new AuditagemClienteAdapter(this, lsItens);
        listaItens.setAdapter(mAdapterLista);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RequestCodeAuditoria && resultCode == Activity.RESULT_OK) {
            atualizarLista();
            Utilidades.retornaMensagem(AuditagemClienteActivity.this, "Produto adicionado", true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    protected void onResume() {
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    private void salvar() {
        try {
            String strMensagem = "Deseja confirmar a auditagem?";
            if (lsItens == null || lsItens.size() == 0)
                strMensagem = "Nenhum item informado!, Ponto de Venda está sem estoque?";

            Alerta alerta = new Alerta(AuditagemClienteActivity.this, getResources().getString(R.string.app_name), strMensagem);
            alerta.showConfirm((dialog, which) -> {
                if (lsItens == null || lsItens.size() == 0)
                    dbEstoque.deletaAuditagemClienteNaoConfirmada(cliente.getId());
                else {
                    dbEstoque.confirmaAuditagemCliente(cliente.getId());
                    new EstoqueSyncTask(AuditagemClienteActivity.this, 1).execute();
                    Utilidades.retornaMensagem(AuditagemClienteActivity.this, "Auditagem confirmada com sucesso!", true);
                }
                dbCliente.confirmaAuditagem(cliente.getId());
                try {
                    Utilidades.openRota(AuditagemClienteActivity.this, true);
                } catch (Exception ex) {
                    Mensagens.mensagemErro(AuditagemClienteActivity.this, ex.getMessage(), false);
                }
            }, null);
        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemClienteActivity.this, ex.getMessage(), false);
        }
    }

    private boolean verificaDuplicado() {
        if (lsItens != null && !lsItens.isEmpty()) {
            for (AuditagemCliente auditagemCliente : lsItens) {
                if (auditagemCliente.getIdProduto().equals(produto.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}