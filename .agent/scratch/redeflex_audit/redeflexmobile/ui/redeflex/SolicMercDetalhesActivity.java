package com.axys.redeflexmobile.ui.redeflex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemSolicitacaoDetalhesAdapter;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

public class SolicMercDetalhesActivity extends AppCompatActivity {
    ProgressDialog mDialog;
    DBSolicitacaoMercadoria dbSolicitacaoMercadoria;
    int iCodigo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solic_merc_detalhes);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Solicitação de mercadoria");
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            iCodigo = bundle.getInt(Config.CodigoSolicMerc);

        dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(SolicMercDetalhesActivity.this);

        if (iCodigo > 0) {
            mDialog = new ProgressDialog(SolicMercDetalhesActivity.this);
            mDialog.setMessage("Aguarde...");
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final SolicitacaoMercadoria sm = dbSolicitacaoMercadoria.getByIdSemItens(iCodigo);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.lbl_codpedido)).setText((sm.getIdServer() > 0) ? String.valueOf(sm.getIdServer()) : String.valueOf(sm.getId()));
                            ((TextView) findViewById(R.id.lbl_datapedido)).setText(Util_IO.dateTimeToString(sm.getDataCriacao()));
                            ((TextView) findViewById(R.id.lbl_statuspedido)).setText(SolicitacaoMercadoria.getDescricaoStatus(sm.getStatus()));
                            ((TextView) findViewById(R.id.lbl_ultimaatualizpedido)).setText(Util_IO.dateTimeToString(sm.getDataUltimaAtualizacao()));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final SolicitacaoMercadoria sm = dbSolicitacaoMercadoria.getById(iCodigo);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            ListView listView = (ListView) findViewById(R.id.listView);
                                            listView.setAdapter(new ItemSolicitacaoDetalhesAdapter(SolicMercDetalhesActivity.this, R.layout.item_solic_merc_detalhes, sm.getItens()));
                                            mDialog.dismiss();
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void onResume() {
        if (!Utilidades.verificarHorarioComercial(SolicMercDetalhesActivity.this, true)) {
            Mensagens.horarioComercial(SolicMercDetalhesActivity.this);
        }
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SolicMercDetalhesActivity.this, SolicitacaoMercActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}