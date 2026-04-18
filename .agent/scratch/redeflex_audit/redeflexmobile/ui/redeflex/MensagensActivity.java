package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.MensagemAdapter;
import com.axys.redeflexmobile.shared.bd.DBMensagem;
import com.axys.redeflexmobile.shared.models.Mensagem;
import com.axys.redeflexmobile.shared.util.Mensagens;

import java.util.ArrayList;

public class MensagensActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mensagens");
        }

        try {
            DBMensagem dbMensagem = new DBMensagem(MensagensActivity.this);
            ListView lv = (ListView) findViewById(R.id.listMensagens);
            ArrayList<Mensagem> listMensagem = dbMensagem.getAll();
            MensagemAdapter mAdapter = new MensagemAdapter(MensagensActivity.this, R.layout.item_mensagem, listMensagem);
            lv.setAdapter(mAdapter);
            lv.setEmptyView(findViewById(R.id.empty));
            lv.setSelection(listMensagem.size() > 0 ? listMensagem.size() - 1 : 0);
            dbMensagem.setVisualizacaoAll();
        } catch (Exception ex) {
            Mensagens.mensagemErro(MensagensActivity.this, ex.getMessage(), true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}