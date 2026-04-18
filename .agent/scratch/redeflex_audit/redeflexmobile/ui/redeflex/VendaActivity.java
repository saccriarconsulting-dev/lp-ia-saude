package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.VendaExpListAdapter;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.VendaConsulta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.Date;

public class VendaActivity extends AppCompatActivity {
    VendaExpListAdapter mAdapter;
    ExpandableListView listVenda;
    ArrayList<VendaConsulta> mLista;
    DBVenda dbVenda;
    EditText dataInicial, dataFinal;
    Button btnPesquisa;
    private String idCliente; // Caso IdCliente esteja preenchido o filtro de vendas será feito via Cliente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Consulta de Vendas");
        }

        // Carrega o Objeto que for passado
        final Bundle data = (Bundle) getIntent().getExtras();
        idCliente = data.getString("IdCliente");

        try {
            criarObjetos();
            criarEventos();
            atualizaLista("", "", String.valueOf(idCliente));
        } catch (Exception ex) {
            Mensagens.mensagemErro(VendaActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        listVenda = findViewById(R.id.expandlistaVenda);
        dbVenda = new DBVenda(VendaActivity.this);
        dataInicial = findViewById(R.id.txtDataInicial);
        dataFinal = findViewById(R.id.txtDataFinal);
        btnPesquisa = findViewById(R.id.btnConsultar);
    }

    private void atualizaLista(String pInicial, String pFinal, String pIdCliente) {
        try {
            mLista = dbVenda.getConsultaVendas(pInicial, pFinal, pIdCliente);
            mAdapter = new VendaExpListAdapter(VendaActivity.this, VendaActivity.this, mLista);
            listVenda.setEmptyView(findViewById(R.id.empty));
            listVenda.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(VendaActivity.this, ex.getMessage(), false);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void criarEventos() {
        try {
            Utilidades.setButtonCalendar(VendaActivity.this, dataInicial);
            Utilidades.setButtonCalendar(VendaActivity.this, dataFinal);
        } catch (Exception ex) {
            Mensagens.mensagemErro(VendaActivity.this, ex.getMessage(), false);
        }

        btnPesquisa.setOnClickListener((view) -> {
            try {
                String periodo = dataInicial.getText().toString();
                if (periodo.isEmpty() && Util_IO.isNullOrEmpty(idCliente)) {
                    Utilidades.retornaMensagem(VendaActivity.this, "Data inicial não informada, Verifique", false);
                    return;
                }

                Date dInicial = Util_IO.getDateByInput(periodo);
                periodo = dataFinal.getText().toString();
                if (periodo.isEmpty() && Util_IO.isNullOrEmpty(idCliente)) {
                    Utilidades.retornaMensagem(VendaActivity.this, "Data final não informada, Verifique", false);
                    return;
                }

                Date dFinal = Util_IO.getDateByInput(periodo);
                if (dFinal.before(dInicial)) {
                    Utilidades.retornaMensagem(VendaActivity.this, "Data final não pode ser menor que inicial, Verifique", false);
                    return;
                }

                String parseInicial = Util_IO.getStringByDateFormatBanco(dInicial);
                String parseFinal = Util_IO.getStringByDateFormatBanco(dFinal);
                if (!Util_IO.isNullOrEmpty(idCliente))
                    atualizaLista("", "", String.valueOf(idCliente));
                else
                    atualizaLista(parseInicial, parseFinal, String.valueOf(idCliente));
            } catch (Exception ex) {
                Mensagens.mensagemErro(VendaActivity.this, ex.getMessage(), false);
            }
        });
    }
}