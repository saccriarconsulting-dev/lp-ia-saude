package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.OportunidadeVendaAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.Operadora;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Mensagens;

import java.util.ArrayList;

public class Lista_OportunidadeVendaActivity extends AppCompatActivity {
    DBCliente dbCliente;

    private String situacaoCliente;
    private int operadoraId;
    ArrayList<Cliente> lista_Clientes;
    OportunidadeVendaAdapter mAdapter;

    ListView listView_Clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaoportunidadevenda);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                situacaoCliente = bundle.getString("SITUACAO");
                operadoraId = bundle.getInt("OPERADORA");
            }

            String titulo = "Oportunidade de Vendas";
            switch (operadoraId) {
                case 1:
                    titulo = situacaoCliente + " Oi";
                    break;
                case 2:
                    titulo = situacaoCliente + " Claro";
                    break;
                case 3:
                    titulo = situacaoCliente + " Vivo";
                    break;
                case 4:
                    titulo = situacaoCliente + " Tim";
                    break;
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(titulo);
            }

            criarObjetos();
            CarregaDados(situacaoCliente, operadoraId);
        } catch (Exception ex) {
            Mensagens.mensagemErro(Lista_OportunidadeVendaActivity.this, ex.getMessage(), true);
        }
    }

    private void CarregaDados(String situacaoCliente, int operadoraId) {
        try {
            lista_Clientes = dbCliente.getClientesSituacao(situacaoCliente, String.valueOf(operadoraId));
            mAdapter = new OportunidadeVendaAdapter(Lista_OportunidadeVendaActivity.this, R.layout.linha_oportunidadevenda, lista_Clientes);
            listView_Clientes.setEmptyView(findViewById(R.id.empty));
            listView_Clientes.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(Lista_OportunidadeVendaActivity.this, ex.getMessage(), false);
        }
    }

    private void criarObjetos() {
        listView_Clientes = findViewById(R.id.oportunidadeVenda_lista);
        dbCliente = new DBCliente(Lista_OportunidadeVendaActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
