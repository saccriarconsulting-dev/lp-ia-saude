package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItensVendaAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class VendaResumoActivity extends AppCompatActivity {
    Venda venda;
    Cliente cliente;

    DBVenda dbVenda;
    ArrayList<ItemVendaCombo> listaItens;
    FormaPagamento formaPagamento;

    RecyclerView mRecyclerView;

    TextView txtCliente, txtTotalPedido, txtDataCobranca, txtFormaPagamento, txtPedido, txtVendedor;
    Button btnCancelar, btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_resumo);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pedido de Venda");
        }

        try {
            int codigo = 0;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                codigo = bundle.getInt(Config.CodigoVenda);

            if (codigo == 0) {
                Mensagens.vendaNaoEncontrada(VendaResumoActivity.this);
                return;
            }

            criarObjetos();
            venda = dbVenda.getVendabyId(codigo);
            if (venda == null) {
                Mensagens.vendaNaoEncontrada(VendaResumoActivity.this);
                return;
            }

            criarEventos();
            carregarDados();
        } catch (Exception ex) {
            Mensagens.mensagemErro(VendaResumoActivity.this, ex.getMessage(), true);
        }
    }

    private void carregarDados() throws Exception {
        this.cliente = new DBCliente(VendaResumoActivity.this).getById(venda.getIdCliente());
        listaItens = dbVenda.getItensComboVendabyIdVenda(venda.getId());

        txtPedido.setText("PEDIDO DE VENDA nº " + String.valueOf(venda.getId()));

        ItensVendaAdapter itensVendaAdapter = new ItensVendaAdapter(listaItens, VendaResumoActivity.this);
        formaPagamento = new DBFormaPagamento(VendaResumoActivity.this).getFormaPagamentoById(venda.getIdFormaPagamento());

        //Exibir
        String nomeCliente = "CLIENTE NÃO PERTENCE MAIS A BASE DO VENDEDOR";
        if (cliente != null)
            nomeCliente = cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia();

        txtCliente.setText(nomeCliente);
        txtTotalPedido.setText(Util_IO.formatDoubleToDecimalNonDivider(dbVenda.retornaValorTotalVenda(venda.getId())));
        mRecyclerView.setAdapter(itensVendaAdapter);
        txtFormaPagamento.setText(formaPagamento.getDescricao());
        txtDataCobranca.setText(Util_IO.dateToStringBr(venda.getDataCobranca()));
        Colaborador colaborador = new DBColaborador(VendaResumoActivity.this).get();
        txtVendedor.setText("Vendedor: " + Util_IO.toTitleCase(colaborador.getNome()));
    }

    private void criarObjetos() {
        txtCliente = (TextView) findViewById(R.id.txtCliente);
        txtTotalPedido = (TextView) findViewById(R.id.txtTotalPedido);
        dbVenda = new DBVenda(VendaResumoActivity.this);
        txtDataCobranca = (TextView) findViewById(R.id.txtDataCobranca);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        txtFormaPagamento = (TextView) findViewById(R.id.txtFormaPagamento);
        txtPedido = (TextView) findViewById(R.id.txtPedido);
        txtVendedor = (TextView) findViewById(R.id.txtVendedor);
    }

    private void criarEventos() {
        btnCancelar.setOnClickListener((view) -> {
            onBackPressed();
        });

        btnEnviar.setOnClickListener((view) -> {
            try {
                Utilidades.compartilharTela(VendaResumoActivity.this);
            } catch (Exception ex) {
                Mensagens.mensagemErro(VendaResumoActivity.this, ex.getMessage(), false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                btnCancelar.performClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}