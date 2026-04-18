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
import com.axys.redeflexmobile.shared.util.Notificacoes;

import java.util.ArrayList;
import java.util.Date;

public class MensagemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mensagem");
        }

        int icodigo = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            icodigo = bundle.getInt(Config.CodigoMensagem);

        if (icodigo > 0) {
            Notificacoes.cancelarNotificacao(MensagemActivity.this, icodigo);
            DBMensagem dbMensagem = new DBMensagem(MensagemActivity.this);
            ListView listView = (ListView) findViewById(R.id.listView);
            ArrayList<Mensagem> listMensagem = new ArrayList<>();
            Mensagem mensagem = dbMensagem.getById(icodigo);
            listMensagem.add(mensagem);
            MensagemAdapter mApdater = new MensagemAdapter(MensagemActivity.this, R.layout.item_mensagem, listMensagem);
            listView.setAdapter(mApdater);
            dbMensagem.setVisualizacao(mensagem.getId(), new Date());
        } else {
            Mensagens.mensagemErro(MensagemActivity.this, "Mensagem não encontrada.", true);
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
}