package com.axys.redeflexmobile.ui.redeflex;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ListaSolicitacoesPDAdapter;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.enums.EnumSituacaoPrecoDif;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.services.bus.PrecoBus;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoPrecoDiferenciadoBus;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class ConsultarSolicitacoesPrecoDifActivity extends AppCompatActivity {
    LinearLayout Empty;
    FloatingActionButton btn_novasolicitacao;
    SearchableSpinner spSituacao;
    public static Button btnFiltrar;
    RecyclerView recyclerView;
    ListaSolicitacoesPDAdapter itemsAdapter;
    ArrayList<SolicitacaoPrecoDiferenciado> listaSolicitacoes;
    EditText dataInicial, dataFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_sol_preco_dif);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Consultar Solicitações");
        }

        inicializarComponentes();
        iniciarSituacao();
        inicializarEventos();

        try {
            new CarregaDados().execute(dataInicial.getText().toString(),
                    dataFinal.getText().toString(),
                    spSituacao.getSelectedItem().toString());
        } catch (Exception ex) {
            Alerta alerta = new Alerta(ConsultarSolicitacoesPrecoDifActivity.this, ConsultarSolicitacoesPrecoDifActivity.this.getResources().getString(R.string.app_name), "Erro ao Carregar Dados:\n" + ex.getLocalizedMessage());
            alerta.show();
        }

    }

    private void inicializarComponentes() {
        dataInicial = findViewById(R.id.listaPD_periodoInicial);
        dataFinal = findViewById(R.id.listaPD_periodoFinal);

        // Inicializa datas
        dataInicial.setText(Util_IO.dateTimeToString(new Date(), "dd/MM/yyyy"));
        dataFinal.setText(Util_IO.dateTimeToString(new Date(), "dd/MM/yyyy"));

        Empty = findViewById(R.id.empty);
        btn_novasolicitacao = findViewById(R.id.listaPD_btn_novasolicitacao);
        spSituacao = findViewById(R.id.listaPD_spn_Situacao);
        btnFiltrar = findViewById(R.id.listaPD_btnFiltro);
        Utilidades.defineSpinner(spSituacao);

        recyclerView = findViewById(R.id.listaPD_recycler_solicitacao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaSolicitacoes = new ArrayList<>();
    }

    private void inicializarEventos() {
        try {
            Utilidades.setButtonCalendar(ConsultarSolicitacoesPrecoDifActivity.this, dataInicial);
            Utilidades.setButtonCalendar(ConsultarSolicitacoesPrecoDifActivity.this, dataFinal);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ConsultarSolicitacoesPrecoDifActivity.this, ex.getMessage(), false);
        }

        btn_novasolicitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultarSolicitacoesPrecoDifActivity.this, SolicitacaoPrecoDifActivity.class);
                intent.putExtra("Operacao", 0); // 0 - Inserção  / 1 - Editar / 2 - Visualizar
                startActivityForResult(intent, 1, null);
            }
        });

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String periodo = dataInicial.getText().toString();
                    if (periodo.isEmpty()) {
                        Utilidades.retornaMensagem(ConsultarSolicitacoesPrecoDifActivity.this, "Data inicial não informada, Verifique", false);
                        return;
                    }

                    Date dInicial = Util_IO.getDateByInput(periodo);
                    periodo = dataFinal.getText().toString();
                    if (periodo.isEmpty()) {
                        Utilidades.retornaMensagem(ConsultarSolicitacoesPrecoDifActivity.this, "Data final não informada, Verifique", false);
                        return;
                    }

                    Date dFinal = Util_IO.getDateByInput(periodo);
                    if (dFinal.before(dInicial)) {
                        Utilidades.retornaMensagem(ConsultarSolicitacoesPrecoDifActivity.this, "Data final não pode ser menor que inicial, Verifique", false);
                        return;
                    }

                    String parseInicial = Util_IO.getStringByDateFormatBanco(dInicial);
                    String parseFinal = Util_IO.getStringByDateFormatBanco(dFinal);

                    new CarregaDados().execute(dataInicial.getText().toString(),
                            dataFinal.getText().toString(),
                            spSituacao.getSelectedItem().toString());
                } catch (Exception ex) {
                    Mensagens.mensagemErro(ConsultarSolicitacoesPrecoDifActivity.this, ex.getMessage(), false);
                }
            }
        });
    }

    private void iniciarSituacao() {
        ArrayList<String> situations = new ArrayList<>();
        EnumSituacaoPrecoDif[] values = EnumSituacaoPrecoDif.values();
        situations.add("Selecionar Todos");
        for (EnumSituacaoPrecoDif valores : values) {
            situations.add(valores.descricao);
        }
        StringAdapter stringAdapter = new StringAdapter(this, R.layout.custom_spinner_title_bar, situations);
        spSituacao.setAdapter(stringAdapter);
    }

    private void mockInformations() {
        SolicitacaoPrecoDiferenciado solicitacaoPrecoDiferenciado = new SolicitacaoPrecoDiferenciado();
        solicitacaoPrecoDiferenciado.setDataSolicitacao(Util_IO.stringToDate("05/09/2022", Config.FormatDateStringBr));
        solicitacaoPrecoDiferenciado.setDataInicial(Util_IO.stringToDate("05/09/2022", Config.FormatDateStringBr));
        solicitacaoPrecoDiferenciado.setDataFinal(Util_IO.stringToDate("31/12/2022", Config.FormatDateStringBr));
        solicitacaoPrecoDiferenciado.setId(1);
        solicitacaoPrecoDiferenciado.setSituacaoId(8);
        solicitacaoPrecoDiferenciado.setIdVendedor(163);
        solicitacaoPrecoDiferenciado.setNomeSolicitante("Roni Andrei Wartha");
        listaSolicitacoes.add(solicitacaoPrecoDiferenciado);

        solicitacaoPrecoDiferenciado = new SolicitacaoPrecoDiferenciado();
        solicitacaoPrecoDiferenciado.setDataSolicitacao(Util_IO.stringToDate("10/01/2024", Config.FormatDateStringBr));
        solicitacaoPrecoDiferenciado.setDataInicial(Util_IO.stringToDate("01/01/2024", Config.FormatDateStringBr));
        solicitacaoPrecoDiferenciado.setDataFinal(Util_IO.stringToDate("31/01/2024", Config.FormatDateStringBr));
        solicitacaoPrecoDiferenciado.setId(2);
        solicitacaoPrecoDiferenciado.setSituacaoId(2);
        solicitacaoPrecoDiferenciado.setIdVendedor(163);
        solicitacaoPrecoDiferenciado.setNomeSolicitante("Roni Andrei Wartha");
        listaSolicitacoes.add(solicitacaoPrecoDiferenciado);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        if (item.getItemId() == R.id.sync) {
            if (Utilidades.isConectado(this.getBaseContext())) {
                new EnviarAtualizacao().execute();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class CarregaDados extends AsyncTask<String, Void, ArrayList<SolicitacaoPrecoDiferenciado>> {
        private ProgressDialog load = new ProgressDialog(ConsultarSolicitacoesPrecoDifActivity.this);

        @Override
        protected void onPreExecute() {
            load.setMessage("Carregando Solicitações Preço Diferenciado!");
            load.show();
        }

        @Override
        protected ArrayList<SolicitacaoPrecoDiferenciado> doInBackground(String... params) {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();

            sb.appendLine(" AND strftime('%d/%m/%Y',[DataSolicitacao]) >= '" + params[0].trim() + "'");
            sb.appendLine(" AND strftime('%d/%m/%Y',[DataSolicitacao]) <= '" + params[1].trim() + "'");

            if (!params[2].equals("Selecionar Todos"))
                sb.appendLine(" and SituacaoId = " + EnumSituacaoPrecoDif.getPorDescricao(params[2]).valor);

            listaSolicitacoes = new BDSolicitacaoPrecoDiferenciado(ConsultarSolicitacoesPrecoDifActivity.this).getSolicitacao(sb.toString(), null);
            return listaSolicitacoes;
        }

        @Override
        protected void onPostExecute(ArrayList<SolicitacaoPrecoDiferenciado> items) {
            super.onPostExecute(items);
            load.dismiss();
            itemsAdapter = new ListaSolicitacoesPDAdapter(ConsultarSolicitacoesPrecoDifActivity.this, items);
            recyclerView.setAdapter(itemsAdapter);
            if (items.size() == 0)
                Empty.setVisibility(View.VISIBLE);
            else
                Empty.setVisibility(View.GONE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                new CarregaDados().execute(dataInicial.getText().toString(),
                        dataFinal.getText().toString(),
                        spSituacao.getSelectedItem().toString());
            }
        } catch (Exception e) {
            //trace("Erro : " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sincronizar, menu);
        return true;
    }

    private class EnviarAtualizacao extends AsyncTask<Void, Void, String> {
        private ProgressDialog load;

        @Override
        protected void onPreExecute() {
            // Criar e exibir o ProgressDialog
            load = new ProgressDialog(ConsultarSolicitacoesPrecoDifActivity.this);
            load.setTitle("Atualizando Dados:");
            load.setMessage("Aguarde, atualizando dados Preço Diferenciado...");
            load.setCancelable(false);
            load.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Envia Solicitações Pendentes
                SolicitacaoPrecoDiferenciadoBus.enviarSolPrecoDiferenciado(ConsultarSolicitacoesPrecoDifActivity.this);

                // Atualiza Solicitação para Sincronizar
                SolicitacaoPrecoDiferenciadoBus.getSolPrecoDiferenciado(1,ConsultarSolicitacoesPrecoDifActivity.this);

                PrecoBus.getPrecoDiferenciado(1, ConsultarSolicitacoesPrecoDifActivity.this);

                return "OK";
            }
            catch (Exception ex)
            {
                return "Erro: " + ex.getMessage();
            }
        }

        protected void onPostExecute(String unused) {
            load.dismiss();
            if (unused.equals("OK")) {
                new CarregaDados().execute(dataInicial.getText().toString(),
                        dataFinal.getText().toString(),
                        spSituacao.getSelectedItem().toString());

                Alerta msg = new Alerta(ConsultarSolicitacoesPrecoDifActivity.this, "Informação", "Dados Atualizados com Sucesso!");
                msg.show();
            } else {
                Alerta msg = new Alerta(ConsultarSolicitacoesPrecoDifActivity.this, "Erro Sincronização", unused);
                msg.show();
            }
        }
    }
}