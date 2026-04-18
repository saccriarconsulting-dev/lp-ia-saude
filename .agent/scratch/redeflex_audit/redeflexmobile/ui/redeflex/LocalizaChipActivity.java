package com.axys.redeflexmobile.ui.redeflex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InformacoesChip;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class LocalizaChipActivity extends AppCompatActivity {
    ImageButton scanBtn, localizarBtn;
    EditText txtCodigo;
    DBColaborador dbColaborador;
    Colaborador vendedor;
    ImageView imgStatus;
    TextView txtProduto, txtData, txtColaborador, txtItemCode, txtIccid;
    LinearLayout layoutRetorno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_identificar_chip);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Localizar Chip");
        }

        try {
            criarObjetos();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(LocalizaChipActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirCamera() {
        findViewById(R.id.pnl_resultado).setVisibility(View.GONE);
        txtCodigo.setText("");
        CodeReader.openCodeReader(this, IntentIntegrator.CODE_128);
    }

    private void criarObjetos() {
        dbColaborador = new DBColaborador(LocalizaChipActivity.this);
        vendedor = dbColaborador.get();
        txtCodigo = (EditText) findViewById(R.id.txtCodigoBarras);
        scanBtn = (ImageButton) findViewById(R.id.button);
        localizarBtn = (ImageButton) findViewById(R.id.buttonBuscar);
        txtProduto = (TextView) findViewById(R.id.txtProduto);
        layoutRetorno = (LinearLayout) findViewById(R.id.pnl_resultado);
        imgStatus = (ImageView) findViewById(R.id.imgStatus);
        txtData = (TextView) findViewById(R.id.txtData);
        txtColaborador = (TextView) findViewById(R.id.txtColaborador);
        txtItemCode = (TextView) findViewById(R.id.txtItemCode);
        txtIccid = (TextView) findViewById(R.id.txtIccid);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                findViewById(R.id.pnl_resultado).setVisibility(View.GONE);
                txtCodigo.setText("");
            } else {
                txtCodigo.setText(result.getContents());
                localizar(result.getContents());
            }
        }
    }

    private void localizar(String pCodigo) {
        try {
            if (pCodigo.isEmpty()) {
                Alerta alerta = new Alerta(LocalizaChipActivity.this, getResources().getString(R.string.app_name), "Informe o código.");
                alerta.show();
                return;
            }

            if (Utilidades.isConectado(LocalizaChipActivity.this))
                new ConsultarChipTask().execute(pCodigo);
            else
                Utilidades.retornaMensagem(LocalizaChipActivity.this, "Verifique sua conexão.", false);
        } catch (Exception ex) {
            Mensagens.mensagemErro(LocalizaChipActivity.this, ex.getMessage(), false);
        }
    }

    private void criarEventos() {
        scanBtn.setOnClickListener((view) -> {
            abrirCamera();
        });

        localizarBtn.setOnClickListener((view) -> {
            localizar(txtCodigo.getText().toString().trim());
        });
    }

    private class ConsultarChipTask extends AsyncTask<String, Void, InformacoesChip> {
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(LocalizaChipActivity.this, getResources().getString(R.string.app_name), "Aguarde....", false, false);
        }

        @Override
        protected InformacoesChip doInBackground(String... params) {
            InformacoesChip server;
            try {
                String urlfinal = URLs.IDENTIFICAR_CHIP + "?barcodeChip=" + params[0];
                server = Utilidades.getObject(urlfinal, InformacoesChip.class);
                return server;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(InformacoesChip result) {
            super.onPostExecute(result);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.cancel();
            }

            if (result != null) {
                if (result.getDescricao().equalsIgnoreCase("Não encontrado")) {
                    imgStatus.setImageResource(R.mipmap.ic_cancelar);
                    txtData.setVisibility(View.GONE);
                    txtColaborador.setVisibility(View.GONE);
                    txtItemCode.setVisibility(View.GONE);
                } else {
                    imgStatus.setImageResource(R.mipmap.ic_icone_estoque);
                    txtData.setVisibility(View.VISIBLE);
                    txtColaborador.setVisibility(View.VISIBLE);
                    txtItemCode.setVisibility(View.VISIBLE);
                    txtItemCode.setText("Código: " + result.getProdutoSAPId());
                    txtColaborador.setText("Vendedor: " + Util_IO.toTitleCase(result.getColaborador()));
                    txtData.setText("Data Movimentação: " + Util_IO.dateToStringBr(result.getDataTransacao()));
                }
                txtProduto.setText(result.getDescricao());
                txtIccid.setText(result.getICCID());
            } else {
                txtProduto.setText("Não encontrado");
                imgStatus.setImageResource(R.mipmap.ic_cancelar);
                txtData.setVisibility(View.GONE);
                txtColaborador.setVisibility(View.GONE);
                txtItemCode.setVisibility(View.GONE);
                txtIccid.setText(txtCodigo.getText().toString().trim());
            }
            layoutRetorno.setVisibility(View.VISIBLE);
        }
    }
}