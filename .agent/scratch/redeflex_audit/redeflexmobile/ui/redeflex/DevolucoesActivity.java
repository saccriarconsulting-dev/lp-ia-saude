package com.axys.redeflexmobile.ui.redeflex;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.DevolucaoAdapter;
import com.axys.redeflexmobile.shared.bd.DBDevolucao;
import com.axys.redeflexmobile.shared.models.Devolucao;
import com.axys.redeflexmobile.shared.util.Alerta;

import java.util.ArrayList;

public class DevolucoesActivity extends AppCompatActivity {

    FloatingActionButton fabNovaDevolucao;
    ListView listaDevolucao;
    DevolucaoAdapter mAdapter;
    ArrayList<Devolucao> listaDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucoes);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Devoluções");
        }

        try {
            criaObjetos();
            criaEventos();
            alimentaDados();
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucoesActivity.this, "Erro", ex.getMessage());
            alerta.showError();
        }
    }

    private void criaEventos() {
        try {
            fabNovaDevolucao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DevolucoesActivity.this, DevolucaoActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucoesActivity.this, "Erro", ex.getMessage());
            alerta.show();
        }
    }

    private void alimentaDados() {
        try {
            listaDev = new DBDevolucao(DevolucoesActivity.this).getDevolucoes();
            mAdapter = new DevolucaoAdapter(DevolucoesActivity.this, R.layout.item_devolucao, listaDev);
            listaDevolucao.setEmptyView(findViewById(R.id.empty));
            listaDevolucao.setAdapter(mAdapter);
        } catch (Exception ex) {
            Alerta alerta = new Alerta(DevolucoesActivity.this, "Erro", ex.getMessage());
            alerta.show();
        }
    }

    private void criaObjetos() {
        fabNovaDevolucao = (FloatingActionButton) findViewById(R.id.fabNovaDevolucao);
        listaDevolucao = (ListView) findViewById(R.id.listaDevolucao);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}