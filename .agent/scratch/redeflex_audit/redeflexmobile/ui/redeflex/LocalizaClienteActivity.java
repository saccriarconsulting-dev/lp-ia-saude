package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.LocalizaClienteAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class LocalizaClienteActivity extends AppCompatActivity {

    LinearLayout layoutBuscar, layoutBusca;
    ImageButton imgRetornar;
    EditText txtPesquisa;
    ListView listViewCliente;
    ArrayList<Cliente> listaClientes;
    LocalizaClienteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localiza_cliente);

        try {
            criarObjetos();
            criarEventos();
            carregarDados("");
        } catch (Exception ex) {
            Mensagens.mensagemErro(LocalizaClienteActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        layoutBuscar = (LinearLayout) findViewById(R.id.layoutBuscar);
        layoutBusca = (LinearLayout) findViewById(R.id.layoutBusca);
        imgRetornar = (ImageButton) findViewById(R.id.imgRetornar);
        txtPesquisa = (EditText) findViewById(R.id.txtPesquisa);
        listViewCliente = (ListView) findViewById(R.id.lsCliente);
    }

    private void carregarDados(String pParametro) {
        listViewCliente.setEmptyView(findViewById(R.id.empty));
        if (!pParametro.isEmpty()) {
            DBCliente dbCliente = new DBCliente(LocalizaClienteActivity.this);
            listaClientes = dbCliente.getClientes(pParametro);
        } else
            listaClientes = new ArrayList<>();
        mAdapter = new LocalizaClienteAdapter(LocalizaClienteActivity.this, R.layout.item_localiza_cliente, listaClientes);
        listViewCliente.setAdapter(mAdapter);
    }

    private void criarEventos() {
        layoutBuscar.setOnClickListener((view) -> {
            layoutBusca.setVisibility(View.VISIBLE);
            layoutBuscar.setVisibility(View.GONE);
            Utilidades.showKeyboard(txtPesquisa, LocalizaClienteActivity.this);
        });

        imgRetornar.setOnClickListener((view) -> {
            layoutBusca.setVisibility(View.GONE);
            layoutBuscar.setVisibility(View.VISIBLE);
            txtPesquisa.setText("");
            Utilidades.hideKeyboard(txtPesquisa, LocalizaClienteActivity.this);
        });

        txtPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                carregarDados(s.toString());
            }
        });

        listViewCliente.setOnItemClickListener((parent, view, position, id) -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Config.CodigoCliente, listaClientes.get(position).getId());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}