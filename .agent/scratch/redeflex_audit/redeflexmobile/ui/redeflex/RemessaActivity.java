package com.axys.redeflexmobile.ui.redeflex;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.RemessaAdapter;
import com.axys.redeflexmobile.shared.bd.DBRemessa;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Remessa;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class RemessaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {
    ListView lsRemessa;
    RemessaAdapter mAdapter;
    ArrayList<Remessa> listaProdutos;
    DBRemessa dbRemessa;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Pesquisar Remessa");
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remessa);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Remessas");
        }

        try {
            criarObjetos();
            carregarDados("");
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(RemessaActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        lsRemessa = (ListView) findViewById(R.id.lsRemessa);
        listaProdutos = new ArrayList<>();
        dbRemessa = new DBRemessa(RemessaActivity.this);
    }

    private void criarEventos() {
        lsRemessa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listaProdutos != null && listaProdutos.size() > 0) {
                    Intent detalhes = new Intent(RemessaActivity.this, RemessaDetalheActivity.class);
                    detalhes.putExtra(Config.CodigoRemessa, listaProdutos.get(position).getId());
                    startActivity(detalhes);
                    finish();
                }
            }
        });
    }

    protected void onResume() {
        if (!Utilidades.verificarHorarioComercial(RemessaActivity.this, true)) {
            Mensagens.horarioComercial(RemessaActivity.this);
        }
        SimpleDbHelper.INSTANCE.open(RemessaActivity.this);
        super.onResume();
    }

    private void carregarDados(String pRemessa) {
        try {
            listaProdutos = dbRemessa.getRemessa(false, pRemessa);
            mAdapter = new RemessaAdapter(RemessaActivity.this, R.layout.item_remessa, listaProdutos);
            lsRemessa.setEmptyView(findViewById(R.id.empty));
            lsRemessa.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(RemessaActivity.this, ex.getMessage(), false);
        }
    }

    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        carregarDados(newText);
        return true;
    }
}