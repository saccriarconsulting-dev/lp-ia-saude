package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.DevIccidAdapter;
import com.axys.redeflexmobile.shared.bd.DBDevolucao;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.Devolucao;
import com.axys.redeflexmobile.shared.models.DevolucaoICCID;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class DevBipagemActivity extends AppCompatActivity {
    Produto produto;
    Devolucao devolucao;
    DBDevolucao dbDevolucao;
    TextView txtProduto, txtQuantidade, txtICCIDEntrada, txtICCIDSaida;
    int quantidade = 0;
    Button btnManual, btnBipar, btnIncluir;
    EditText txtCodigoBarra;
    ArrayList<DevolucaoICCID> listItens;
    ListView listICCID;
    DevIccidAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_bipagem);

        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Necessário preenchimento");
            }

            Bundle bundle = getIntent().getExtras();
            criaObjetos();
            if (bundle != null) {
                produto = new DBEstoque(DevBipagemActivity.this).getProdutoById(bundle.getString(Config.CodigoProduto));
                devolucao = dbDevolucao.getById(bundle.getString(Config.CodigoDevolucao));
                quantidade = bundle.getInt("Quantidade");
            }

            if (devolucao == null) {
                Mensagens.mensagemErro(DevBipagemActivity.this, "Devolução não iniciada", true);
                return;
            }

            if (produto == null) {
                Mensagens.produtoNaoEncontrado(DevBipagemActivity.this);
                return;
            }

            txtProduto.setText(produto.getId() + " - " + produto.getNome());
            txtQuantidade.setText(String.valueOf(quantidade));
            criaEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(DevBipagemActivity.this, ex.getMessage(), true);
        }
    }

    private void criaObjetos() {
        dbDevolucao = new DBDevolucao(DevBipagemActivity.this);
        txtProduto = (TextView) findViewById(R.id.txtProduto);
        txtQuantidade = (TextView) findViewById(R.id.txtQuantidade);
        btnManual = (Button) findViewById(R.id.btnManual);
        btnBipar = (Button) findViewById(R.id.btnBipar);
        txtCodigoBarra = (EditText) findViewById(R.id.txtCodigoBarra);
        txtICCIDEntrada = (TextView) findViewById(R.id.txtICCIDEntrada);
        txtICCIDSaida = (TextView) findViewById(R.id.txtICCIDSaida);
        btnIncluir = (Button) findViewById(R.id.btnIncluir);
        listICCID = (ListView) findViewById(R.id.listICCID);
    }

    private void criaEventos() {
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util_IO.isNullOrEmpty(txtCodigoBarra.getText().toString())) {
                    txtCodigoBarra.setError("ICCID não informado");
                    txtCodigoBarra.requestFocus();
                    return;
                }

                incluirICCID(txtCodigoBarra.getText().toString().trim());
            }
        });

        btnBipar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreCamera();
            }
        });

        btnIncluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entrada = txtICCIDEntrada.getText().toString().trim();
                String saida = txtICCIDSaida.getText().toString().trim();

                if (Util_IO.isNullOrEmpty(entrada)) {
                    Alerta alerta = new Alerta(DevBipagemActivity.this, getResources().getString(R.string.app_name), "ICCID recolhido não informado, Verifique");
                    alerta.show();
                    return;
                }

                if (Util_IO.isNullOrEmpty(saida)) {
                    Alerta alerta = new Alerta(DevBipagemActivity.this, getResources().getString(R.string.app_name), "ICCID entregue não informado, Verifique");
                    alerta.show();
                    return;
                }

                if (entrada.equals(saida)) {
                    Alerta alerta = new Alerta(DevBipagemActivity.this, getResources().getString(R.string.app_name), "ICCID recolhido igual ICCID entregue, Verifique");
                    alerta.show();
                    return;
                }

                atualizaLista(entrada, saida, true);
                txtICCIDEntrada.setText("");
                txtICCIDSaida.setText("");
                (findViewById(R.id.layoutICCID)).setVisibility(View.GONE);
            }
        });
    }

    private void incluirICCID(final String pICCID) {
        Alerta alerta = new Alerta(DevBipagemActivity.this, pICCID, "Deseja incluir esse código de barra?");
        alerta.showConfirm(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Util_IO.isNullOrEmpty(txtICCIDEntrada.getText().toString())) {
                    txtICCIDEntrada.setText(pICCID);
                    (findViewById(R.id.layoutICCID)).setVisibility(View.VISIBLE);
                } else {
                    (findViewById(R.id.layoutICCID)).setVisibility(View.VISIBLE);
                    txtICCIDSaida.setText(pICCID);
                }
            }
        }, null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !result.getContents().startsWith(produto.getIniciaCodBarra())) {
                Alerta alerta = new Alerta(DevBipagemActivity.this, getResources().getString(R.string.app_name), "Código de Barra deve começar com: " + produto.getIniciaCodBarra());
                alerta.show(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        abreCamera();
                    }
                });
            } else if (produto.getQtdCodBarra() > 0 && result.getContents().trim().length() != produto.getQtdCodBarra()) {
                Alerta alerta = new Alerta(DevBipagemActivity.this, getResources().getString(R.string.app_name), "Código de Barra deve conter " + String.valueOf(produto.getQtdCodBarra()) + " caracteres");
                alerta.show(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        abreCamera();
                    }
                });
            } else
                incluirICCID(result.getContents());
        }
    }

    private void abreCamera() {
        try {
            txtCodigoBarra.setText("");
            CodeReader.openCodeReader(this, IntentIntegrator.CODE_128);
        } catch (Exception ex) {
            Mensagens.mensagemErro(DevBipagemActivity.this, ex.getMessage(), false);
        }
    }

    private void atualizaLista(String pEntrada, String pSaida, boolean pIncluir) {
        if (listItens == null)
            listItens = new ArrayList<>();

        if (pIncluir) {
            DevolucaoICCID itens = new DevolucaoICCID();
            itens.setIccidEntrada(pEntrada);
            itens.setIccidSaida(pSaida);
            listItens.add(itens);
        }

        mAdapter = new DevIccidAdapter(DevBipagemActivity.this, R.layout.item_dev_iccid, listItens);
        listICCID.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.opcao_salvar:
                salvar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        try {
            int quantidadeFinal = 0;

            if (listItens != null) {
                for (DevolucaoICCID item : listItens) {
                    if (!Util_IO.isNullOrEmpty(item.getIccidEntrada()) && !Util_IO.isNullOrEmpty(item.getIccidSaida()))
                        quantidadeFinal++;
                }
            }

            if (quantidadeFinal != quantidade) {
                Alerta alerta = new Alerta(DevBipagemActivity.this, getResources().getString(R.string.app_name), "ICCID's não foram informados, Verifique");
                alerta.show();
                return;
            }

            long codigo = dbDevolucao.addItemDevolucao(devolucao.getId(), produto.getId(), quantidade);
            dbDevolucao.addIccid(devolucao.getId(), (int) codigo, listItens);
            Utilidades.retornaMensagem(DevBipagemActivity.this, "Itens incluídos", true);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } catch (Exception ex) {
            Mensagens.mensagemErro(DevBipagemActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }
}