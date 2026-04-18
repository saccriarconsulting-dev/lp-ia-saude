package com.axys.redeflexmobile.ui.redeflex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ClientesAdapter;
import com.axys.redeflexmobile.shared.adapters.VendedorAdapter;
import com.axys.redeflexmobile.shared.bd.BDVendedor;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Vendedor;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.jakewharton.rxbinding2.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class CredenciadosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {

    private static final int BUTTON_DELAY_TAP = 2200;

    ListView listaCred;
    ArrayList<Cliente> listaClientes;
    DBCliente dbCliente;
    ClientesAdapter mAdapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private VendedorAdapter mAdapterVendedor;
    Spinner ddlVendedor;
    public static ArrayList<Vendedor> listVendedores;
    LinearLayout ll_Supervisor;
    int idVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credenciados);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_new_registers);
        }

        try {
            criarObjetos();
            criarEventos();
            // Carrega os dados dos Vendedores
            alimentaDados();
            idVendedor = 0;

            // Carrega dados
            atualizarLista("", idVendedor);
        } catch (Exception ex) {
            Mensagens.mensagemErro(CredenciadosActivity.this, ex.getMessage(), true);
        }
    }

    private void alimentaDados() {
        listVendedores = new BDVendedor(CredenciadosActivity.this).getVendedor(null, null);
        mAdapterVendedor = new VendedorAdapter(CredenciadosActivity.this, R.layout.custom_spinner_title_bar, listVendedores);
        mAdapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlVendedor.setAdapter(mAdapterVendedor);

        if (listVendedores != null && listVendedores.size() > 1)
            ll_Supervisor.setVisibility(View.VISIBLE);
        else
            ll_Supervisor.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        disposables.dispose();
    }

    private void criarEventos() {
        Disposable disposable = RxAdapterView.itemClickEvents(listaCred)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    Visita visita = new DBVisita(CredenciadosActivity.this).getVisitaAtiva();
                    if (visita != null) {
                        Alerta msg = new Alerta(CredenciadosActivity.this, getResources().getString(R.string.app_name), "A sua última visita não foi encerrada, Favor encerrar o atendimento");
                        msg.show();
                        return;
                    }

                    Intent intent = new Intent(CredenciadosActivity.this, NovaRotaActivity.class);
                    intent.putExtra(Config.CodigoCliente, value.clickedView().getTag().toString());
                    intent.putExtra(Config.NovosCredenciadosFlag, Config.NovosCredenciadosValue);
                    startActivity(intent);

                }, Timber::e);
        disposables.add(disposable);

        ddlVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idVendedor = (int) listVendedores.get(position).getIdVendedor();
                atualizarLista("", idVendedor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void criarObjetos() {
        listaCred = findViewById(R.id.listCredenciados);
        dbCliente = new DBCliente(CredenciadosActivity.this);
        ddlVendedor = (Spinner) findViewById(R.id.ddlVendedor);
        ll_Supervisor = findViewById(R.id.ll_Supervisor);
    }

    private void atualizarLista(String pCliente, int pVendedor) {
        try {
            listaClientes = dbCliente.getCredenciados(pCliente, pVendedor);
            mAdapter = new ClientesAdapter(CredenciadosActivity.this, R.layout.linha_credenciados, listaClientes);
            listaCred.setEmptyView(findViewById(R.id.empty));
            listaCred.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(CredenciadosActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Pesquisar cliente...");
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(this));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.call_chat_bot) {
            Colaborador colaborador = new DBColaborador(this).get();
            String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
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
        atualizarLista(newText, idVendedor);
        return true;
    }
}
