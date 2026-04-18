package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.EstoqueAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Mensagens;

import java.util.ArrayList;

public class EstoqueActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {
    ListView lsEstoque;
    EstoqueAdapter mAdapter;
    ArrayList<Produto> lista;
    DBEstoque dbEstoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Estoque");
        }

        try {
            criarObjetos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(EstoqueActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Pesquisar Produto...");
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
    }

    private void criarObjetos() {
        lsEstoque = findViewById(R.id.lsEstoque);
        dbEstoque = new DBEstoque(EstoqueActivity.this);
        atualizarLista("");
    }

    private void atualizarLista(String produto) {
        lista = dbEstoque.getProdutosEstoque(produto);
        mAdapter = new EstoqueAdapter(EstoqueActivity.this, R.layout.item_estoque, lista);
        lsEstoque.setAdapter(mAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        atualizarLista(newText);
        return true;
    }
}
