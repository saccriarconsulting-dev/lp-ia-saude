package com.axys.redeflexmobile.ui.redeflex;

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
import com.axys.redeflexmobile.shared.adapters.RotaAdapter;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class RotaDiariaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {
    ArrayList<Rota> listaRota;
    RotaAdapter mAdapter;
    int dayOfWeek, weekOfMonth;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rota);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Rota do dia");
        }

        try {
            Colaborador colaborador = new DBColaborador(RotaDiariaActivity.this).get();
            int[] atendimento = Utilidades.retornaDiaSemanaAtendimento(colaborador);
            dayOfWeek = atendimento[0];
            weekOfMonth = atendimento[1];

            criarObjetos();
            carregarDados("");
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(RotaDiariaActivity.this, ex.getMessage(), false);
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

    private void criarObjetos() {
        listView = (ListView) findViewById(R.id.lvRota);
    }

    private void carregarDados(String pParametro) {
        try {
            listaRota = new DBRota(RotaDiariaActivity.this).getRotasByDiaSemana(dayOfWeek, pParametro, weekOfMonth, 0);
            mAdapter = new RotaAdapter(RotaDiariaActivity.this, R.layout.item_rota, listaRota);
            listView.setEmptyView(findViewById(R.id.empty));
            listView.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(RotaDiariaActivity.this, ex.getMessage(), false);
        }
    }

    private void criarEventos() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alerta alerta = new Alerta(RotaDiariaActivity.this, getResources().getString(R.string.app_name)
                        , "Somente visualização, Para iniciar atendimento vá para o menu de rota");
                alerta.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Pesquisar Cliente...");
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
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
