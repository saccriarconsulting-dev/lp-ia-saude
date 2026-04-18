package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.PainelRelatorioMetaAdapter;
import com.axys.redeflexmobile.shared.bd.DBRelatorioMeta;
import com.axys.redeflexmobile.shared.models.painelRelatorioMeta;
import com.axys.redeflexmobile.shared.util.Mensagens;

import java.util.ArrayList;

public class RelatorioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Relatórios");
        }

        try {
            ListView listView = findViewById(R.id.listRelatorios);
            ArrayList<painelRelatorioMeta> listaRelatorio = new DBRelatorioMeta(RelatorioActivity.this).getPainel();
            PainelRelatorioMetaAdapter mAdapter = new PainelRelatorioMetaAdapter(RelatorioActivity.this, R.layout.item_relatorio_meta_01, listaRelatorio);
            listView.setEmptyView(findViewById(R.id.empty));
            listView.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(RelatorioActivity.this, ex.getMessage(), true);
        }
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