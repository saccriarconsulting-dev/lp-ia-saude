package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.CobrancaExpListAdapter;
import com.axys.redeflexmobile.shared.bd.DBCobranca;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cobranca;
import com.axys.redeflexmobile.shared.services.bluetooth.BluetoothService;
import com.axys.redeflexmobile.shared.services.bluetooth.DeviceListActivity;
import com.axys.redeflexmobile.shared.services.bluetooth.Print;
import com.axys.redeflexmobile.shared.services.tasks.GerarBoletoTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;

import java.util.ArrayList;
import java.util.Calendar;

public class Cobranca2Activity extends AppCompatActivity {
    ArrayList<Cobranca> lista;
    CobrancaExpListAdapter mAdapter;
    ExpandableListView lsCobranca;
    TextView txtValor;
    LinearLayout layoutValorpendente;
    Button btnGerar;
    Print mPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobranca2);
        Utilidades.getDataServidorESalvaBanco(Cobranca2Activity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cobrança");
        }

        try {
            criarObjetos();
            carregarDados();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(Cobranca2Activity.this, ex.getMessage(), true);
        }
    }

    protected synchronized void onResume() {
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();

        if (mPrint != null && mPrint.mService != null && mPrint.isBluetoothEnable()) {
            if (mPrint.mService.getState() == BluetoothService.STATE_NONE) {
                mPrint.mService.start();
            }
        }
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        if (mPrint != null && mPrint.mService != null && mPrint.isBluetoothEnable()) {
            mPrint.mService.stop();
        }
    }

    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    private void criarObjetos() {
        btnGerar = (Button) findViewById(R.id.btnGerar);
        lsCobranca = (ExpandableListView) findViewById(R.id.expandableListCobranca);
        layoutValorpendente = (LinearLayout) findViewById(R.id.LayoutGerarBoleto);
        txtValor = (TextView) findViewById(R.id.txtValorPendente);
        mPrint = new Print(Cobranca2Activity.this);
    }

    private void carregarDados() {
        try {
            lista = new DBCobranca(Cobranca2Activity.this).getCobrancas();
            mAdapter = new CobrancaExpListAdapter(Cobranca2Activity.this, lista, mPrint);
            lsCobranca.setEmptyView(findViewById(R.id.empty));
            lsCobranca.setAdapter(mAdapter);
            Double valor = new DBVenda(Cobranca2Activity.this).retornaValorPendente();
            if (valor == 0 || valor == 0.0) {
                btnGerar.setEnabled(false);
                btnGerar.setBackgroundResource(R.drawable.botao_neutro);
            } else {
                btnGerar.setEnabled(true);
                btnGerar.setBackgroundResource(R.drawable.botao_ok);
            }
            txtValor.setText("R$ " + Util_IO.formataValor(valor));
        } catch (Exception ex) {
            Mensagens.mensagemErro(Cobranca2Activity.this, ex.getMessage(), false);
        }
    }

    private void criarEventos() {
        btnGerar.setOnClickListener((view) -> {
            if (!Validacoes.validacaoDataAparelho(Cobranca2Activity.this)) {
                return;
            }
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek) {
                Utilidades.retornaMensagem(Cobranca2Activity.this, "Geração não permitida nos finais de semana!", false);
            } else {
                Alerta alerta = new Alerta(Cobranca2Activity.this, getResources().getString(R.string.app_name), "Deseja realmente gerar o boleto?");
                alerta.showConfirm((dialog, which) -> {
                    if (Utilidades.isConectado(Cobranca2Activity.this))
                        new GerarBoletoTask(Cobranca2Activity.this).execute();
                    else
                        Utilidades.retornaMensagem(Cobranca2Activity.this, "Verifique sua conexão com a Internet", false);
                }, null);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mPrint != null && mPrint.mService != null && mPrint.isBluetoothEnable()) {
                    mPrint.mService.stop();
                }
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    if (mPrint == null)
                        mPrint = new Print(Cobranca2Activity.this);

                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String address = bundle.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        mPrint.connect(address);
                    }
                }
                break;
            }
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    if (mPrint == null)
                        mPrint = new Print(Cobranca2Activity.this);
                } else
                    Utilidades.retornaMensagem(Cobranca2Activity.this, "Bluetooth não iniciado, Verifique", false);
                break;
        }
    }
}