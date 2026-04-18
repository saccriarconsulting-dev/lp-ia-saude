package com.axys.redeflexmobile.ui.cliente.lista;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static android.widget.LinearLayout.VERTICAL;

public class ClienteListaActivity extends BaseActivity implements ClienteListaView {

    public static final int BUTTON_WAIT_DURATION = 2200;
    public static final int TAMANHO_MINIMO = 3;

    @Inject
    ClienteListaAdapter adapter;
    @Inject
    ClienteListaPresenter presenter;

    @BindView(R.id.cliente_lista_rv_clientes)
    RecyclerView rvClientes;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cliente_lista_tv_vazio)
    AppCompatTextView tvVazio;
    @BindView(R.id.cliente_cbClienteConsignado)
    AppCompatCheckBox cbClienteConsignado;
    @BindView(R.id.cliente_cbClienteTodos)
    AppCompatCheckBox cbClienteTodos;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.cliente_lista_layout;
    }

    @Override
    protected void initialize() {
        criarToolbar();
        configurarAdapter();
        presenter.carregarClientes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        EditText etSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        etSearch.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        etSearch.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        try {
            final Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(etSearch, R.drawable.register_list_activity_search_bar_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < TAMANHO_MINIMO) {
                    presenter.pesquisarClientes("");
                    return true;
                }
                presenter.pesquisarClientes(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void popularAdapter(List<Cliente> clientes) {
        adapter.setClientes(clientes);
    }

    @Override
    public void abrirInformacoes(String codigo, String latitude, String longitude) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.BLOQUEAR_VISITA, true);
        bundle.putString(Config.CodigoCliente, codigo);
        bundle.putString(Config.Latitude, latitude);
        bundle.putString(Config.Longitude, longitude);

        Intent intent = new Intent(ClienteListaActivity.this, ClienteActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void validarMensagemListaVazia(boolean apresentar) {
        tvVazio.setVisibility(apresentar ? View.VISIBLE : View.GONE);
    }

    private void criarToolbar() {
        setSupportActionBar(toolbar);
        mudarTitulo();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );

        cbClienteTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbClienteTodos.setChecked(true);
                cbClienteConsignado.setChecked(false);
                presenter.carregarClientes();
            }
        });

        cbClienteConsignado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbClienteConsignado.setChecked(true);
                cbClienteTodos.setChecked(false);
                presenter.carregarClientesConsignado();
            }
        });
    }

    private void configurarAdapter() {
        rvClientes.setLayoutManager(new LinearLayoutManager(this));
        rvClientes.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rvClientes.setAdapter(adapter);

        adapter.setCallback(presenter::abrirInformacoes);
    }

    private void mudarTitulo() {
        setTitle(getString(R.string.cliente_lista_titulo));
    }
}
