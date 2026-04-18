package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.SolicitacaoAdapter;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.services.tasks.SolicMercSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;

import java.util.ArrayList;

public class SolicitacaoMercActivity extends AppCompatActivity implements SolicitacaoAdapter.OnClickListener {
    ArrayList<SolicitacaoMercadoria> listSolicitacao;
    SolicitacaoAdapter mAdapter;
    ListView listView;
    FloatingActionButton btnNovaSolicitacao;
    DBSolicitacaoMercadoria dbSolicitacaoMercadoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_merc);

        Utilidades.getDataServidorESalvaBanco(SolicitacaoMercActivity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Solicitação de Mercadoria");
        }

        try {
            criarObjetos();
            criarEventos();
            atualizaLista();
        } catch (Exception ex) {
            Mensagens.mensagemErro(SolicitacaoMercActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SolicitacaoMerc && resultCode == Activity.RESULT_OK) {
            atualizaLista();
            new SolicMercSyncTask(SolicitacaoMercActivity.this).execute();
        }
    }

    private void atualizaLista() {
        try {
            listSolicitacao = dbSolicitacaoMercadoria.getByAll();
            mAdapter = new SolicitacaoAdapter(SolicitacaoMercActivity.this, R.layout.item_solic_merc_header, listSolicitacao);
            mAdapter.myClickListener = this;
            listView.setEmptyView(findViewById(R.id.empty));
            listView.setAdapter(mAdapter);
        } catch (Exception ex) {
            Alerta alerta = new Alerta(SolicitacaoMercActivity.this, "Erro", ex.getMessage());
            alerta.show();
        }
    }

    private void criarObjetos() {
        dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(SolicitacaoMercActivity.this);
        btnNovaSolicitacao = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void criarEventos() {
        btnNovaSolicitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validacoes.validacaoDataAparelho(SolicitacaoMercActivity.this)) {
                    return;
                }
                boolean verifica = new DBOs(SolicitacaoMercActivity.this).existeOsPendenteAgendamento();
                if (verifica) {
                    Alerta alerta = new Alerta(SolicitacaoMercActivity.this, getResources().getString(R.string.app_name), "Você possui OS sem agendamento, Favor agendar todas as ordem de serviço!!!");
                    alerta.show();
                    return;
                }

                Intent intent = new Intent(SolicitacaoMercActivity.this, SolicMercActivity.class);
                startActivityForResult(intent, RequestCode.SolicitacaoMerc);
            }
        });
    }

    @Override
    public void onClickVisualizar(int codSolicitacao) {
        Intent intent = new Intent(SolicitacaoMercActivity.this, SolicMercDetalhesActivity.class);
        intent.putExtra(Config.CodigoSolicMerc, codSolicitacao);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}