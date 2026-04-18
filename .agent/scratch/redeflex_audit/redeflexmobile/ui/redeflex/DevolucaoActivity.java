package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.axys.redeflexmobile.shared.adapters.DevolucaoExpListAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBDevolucao;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Devolucao;
import com.axys.redeflexmobile.shared.models.DevolucaoItens;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.services.tasks.DevolucaoTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;

import java.util.ArrayList;

public class DevolucaoActivity extends AppCompatActivity {
    SearchableSpinner cbproduto;
    ArrayList<Produto> listaProduto;
    ArrayList<DevolucaoItens> listaItens;
    DBEstoque dbEstoque;
    LinearLayout layoutCliente;
    private static final int RESULT_CODE = 0;
    private static final int RESULT_BIPAGEM = 1;
    Cliente cliente;
    TextView txtCliente;
    EditText txtQuantidade;
    Button btnInserir;
    Produto produto;
    DBDevolucao dbDevolucao;
    Devolucao devolucao;
    DevolucaoExpListAdapter mAdapter;
    ExpandableListView listDevItens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucao);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Devolução");
        }

        try {
            criaObjetos();
            carregaDados();
            criaEventos();

            devolucao = dbDevolucao.getNaoFinalizada();
            if (devolucao != null && !Util_IO.isNullOrEmpty(devolucao.getIdCliente())) {
                Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Existe uma devolução não finalizada");
                alerta.show();
                atualizaCliente(devolucao.getIdCliente());
                atualizaItens();
            }
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucaoActivity.this, "Erro", ex.getMessage());
            alerta.showError();
        }
    }

    private void criaObjetos() {
        layoutCliente = (LinearLayout) findViewById(R.id.layoutCliente);
        cbproduto = (SearchableSpinner) findViewById(R.id.spinnerProduto);
        defineSpinner(cbproduto);
        dbEstoque = new DBEstoque(DevolucaoActivity.this);
        txtCliente = (TextView) findViewById(R.id.txtCliente);
        btnInserir = (Button) findViewById(R.id.btnInserir);
        txtQuantidade = (EditText) findViewById(R.id.txtQuantidade);
        dbDevolucao = new DBDevolucao(DevolucaoActivity.this);
        listDevItens = (ExpandableListView) findViewById(R.id.listDevItens);
    }

    private void criaEventos() {
        layoutCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevolucaoActivity.this, LocalizaClienteActivity.class);
                startActivityForResult(intent, RESULT_CODE);
                txtCliente.setText("");
                cliente = null;
            }
        });

        cbproduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produto = listaProduto.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente == null) {
                    Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Cliente não informado, Verifique");
                    alerta.show();
                    return;
                }

                if (produto == null) {
                    Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Produto não informado, Verifique");
                    alerta.show();
                    return;
                }

                if (listaItens != null && listaItens.size() > 0) {
                    for (DevolucaoItens item : listaItens) {
                        if (item.getIdProduto().equals(produto.getId())) {
                            Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Produto já incluído, Verifique");
                            alerta.show();
                            return;
                        }
                    }
                }

                String valor = txtQuantidade.getText().toString().trim();
                if (valor.isEmpty()) {
                    Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Quantidade não informada, Verifique");
                    alerta.show();
                    txtQuantidade.requestFocus();
                    return;
                }

                int quantidade = Integer.parseInt(valor);
                if (quantidade <= 0) {
                    Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Verifique quantidade, Verifique");
                    alerta.show();
                    txtQuantidade.requestFocus();
                    return;
                }

                if (devolucao == null) {
                    devolucao = dbDevolucao.getNaoFinalizada();
                    if (devolucao == null) {
                        dbDevolucao.addDevolucao(cliente.getId());
                        devolucao = dbDevolucao.getNaoFinalizada();
                    }
                }

                if (produto.getBipagem().equalsIgnoreCase("S")) {
                    Intent intent = new Intent(DevolucaoActivity.this, DevBipagemActivity.class);
                    intent.putExtra(Config.CodigoDevolucao, String.valueOf(devolucao.getId()));
                    intent.putExtra(Config.CodigoProduto, produto.getId());
                    intent.putExtra("Quantidade", quantidade);
                    startActivityForResult(intent, RESULT_BIPAGEM);
                } else {
                    dbDevolucao.addItemDevolucao(devolucao.getId(), produto.getId(), quantidade);
                    Utilidades.retornaMensagem(DevolucaoActivity.this, "Item incluído", true);
                    atualizaItens();
                }
                txtQuantidade.setText("");
            }
        });
    }

    private void defineSpinner(SearchableSpinner obj) {
        obj.setTitle("Selecionar item");
        obj.setPositiveButton("OK");
    }

    private void carregaDados() {
        try {
            listaProduto = dbEstoque.getProdutos();
            cbproduto.setAdapter(UtilAdapter.adapterProduto(DevolucaoActivity.this, listaProduto));
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucaoActivity.this, "Erro", ex.getMessage());
            alerta.show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (devolucao != null) {
                    Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Existe uma devolução não finalizada, deseja finalizar?");
                    alerta.showConfirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbDevolucao.deleteById(String.valueOf(devolucao.getId()));
                            Utilidades.retornaMensagem(DevolucaoActivity.this, "Devolução cancelada", true);
                            sair();
                        }
                    }, null);
                } else
                    sair();
                return true;
            case R.id.opcao_salvar:
                salvar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        try {
            if (cliente == null) {
                Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Cliente não informado, Verifique");
                alerta.show();
                return;
            }

            if (produto == null) {
                Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Produto não informado, Verifique");
                alerta.show();
                return;
            }

            if (devolucao == null || listaItens == null || listaItens.size() == 0) {
                Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Nenhum item informado, Verifique");
                alerta.show();
                return;
            }

            dbDevolucao.finalizaDevolucao(devolucao.getId(), cliente.getId());
            Alerta alerta = new Alerta(DevolucaoActivity.this, getResources().getString(R.string.app_name), "Devolução confirmada com sucesso");
            alerta.show(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DevolucaoTask(DevolucaoActivity.this).execute();
                    sair();
                }
            });
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucaoActivity.this, "Erro", ex.getMessage());
            alerta.show();
        }
    }

    private void sair() {
        Intent intent = new Intent(DevolucaoActivity.this, DevolucoesActivity.class);
        startActivity(intent);
        finish();
    }

    private void atualizaCliente(String pCliente) {
        try {
            cliente = new DBCliente(DevolucaoActivity.this).getById(pCliente);
            txtCliente.setText(cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucaoActivity.this, "Erro", ex.getMessage());
            alerta.show();
        }
    }

    private void atualizaItens() {
        if (devolucao != null) {
            listaItens = dbDevolucao.getItensByIdDev(String.valueOf(devolucao.getId()));
            mAdapter = new DevolucaoExpListAdapter(DevolucaoActivity.this, listaItens);
            listDevItens.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE && resultCode == Activity.RESULT_OK) {
            atualizaCliente(data.getStringExtra(Config.CodigoCliente));
        } else if (requestCode == RESULT_BIPAGEM && resultCode == Activity.RESULT_OK) {
            atualizaItens();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }
}