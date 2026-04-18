package com.axys.redeflexmobile.ui.redeflex;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.bd.DBCanalSuporte;
import com.axys.redeflexmobile.shared.models.CanalSuporte;
import com.axys.redeflexmobile.shared.services.tasks.CanalSuporteTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.component.ViewImage;

import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class SuporteActivity extends AppCompatActivity {
    Button btnAddPrint, btnTeamViewer;
    EditText txtExplicacao;
    SearchableSpinner ddlMotivo;
    ViewImage imgErro;
    String localArquivo;
    StringAdapter mStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suporte);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("O que aconteceu?");
        }

        try {
            criarObjetos();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(SuporteActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_suporte, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.envia_suporte:
                salvar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        Alerta alerta = new Alerta(SuporteActivity.this, getResources().getString(R.string.app_name), "Deseja enviar a solicitação de suporte?");
        alerta.showConfirm(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    CanalSuporte suporte = new CanalSuporte();
                    suporte.setProblema(ddlMotivo.getSelectedItem().toString());
                    if (!Util_IO.isNullOrEmpty(txtExplicacao.getText().toString()))
                        suporte.setDescricao(Util_IO.trataString(txtExplicacao.getText().toString().trim()));
                    if (!Util_IO.isNullOrEmpty(localArquivo)) {
                        String strImageCompress = Utilidades.getFilePath(SuporteActivity.this);
                        File fotoOriginal = new File(localArquivo);
                        File fotoFinal = new Compressor(SuporteActivity.this).setDestinationDirectoryPath(strImageCompress).compressToFile(fotoOriginal);
                        suporte.setLocalArquivo(fotoFinal.getAbsolutePath());
                    }
                    suporte.setVersaoApp(BuildConfig.VERSION_NAME);

                    new DBCanalSuporte(SuporteActivity.this).add(suporte);
                    new CanalSuporteTask(SuporteActivity.this).execute();

                    Alerta alertafechar = new Alerta(SuporteActivity.this, "Solicitação enviada com sucesso", "Entraremos em contato se precisarmos de mais detalhes");
                    alertafechar.show(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                } catch (Exception ex) {
                    Mensagens.mensagemErro(SuporteActivity.this, ex.getMessage(), false);
                }
            }
        }, null);
    }

    private void criarObjetos() {
        btnAddPrint = (Button) findViewById(R.id.btnAddTela);
        btnTeamViewer = (Button) findViewById(R.id.btnTeamViewer);
        txtExplicacao = (EditText) findViewById(R.id.txtExplicacao);
        ddlMotivo = (SearchableSpinner) findViewById(R.id.ddlMotivo);

        Utilidades.defineSpinner(ddlMotivo);

        ArrayList<String> arrMotivo = new ArrayList<>();
        arrMotivo.add("Cliente não habilitado para venda à prazo");
        arrMotivo.add("Senha inválida na venda à prazo");
        arrMotivo.add("Produto não aparece");
        arrMotivo.add("Desconto não aparece");
        arrMotivo.add("Outros");

        mStringAdapter = new StringAdapter(SuporteActivity.this, R.layout.custom_spinner_title_bar, arrMotivo);
        ddlMotivo.setAdapter(mStringAdapter);

        imgErro = (ViewImage) findViewById(R.id.imgErro);
        localArquivo = null;
    }

    private void criarEventos() {
        btnAddPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utilidades.loadImagefromGallery(SuporteActivity.this);
                } catch (Exception ex) {
                    Mensagens.mensagemErro(SuporteActivity.this, ex.getMessage(), false);
                }
            }
        });

        btnTeamViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utilidades.openNewApp(SuporteActivity.this, "com.teamviewer.quicksupport.market");
                } catch (Exception ex) {
                    Mensagens.mensagemErro(SuporteActivity.this, ex.getMessage(), false);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RequestCode.GaleriaImagem && resultCode == RESULT_OK && null != data) {
                File fotoOriginal = Utilidades.getImagemFromGallery(data, SuporteActivity.this);
                if (fotoOriginal != null) {
                    localArquivo = fotoOriginal.getAbsolutePath();
                    Bitmap bImagem = BitmapFactory.decodeFile(localArquivo);
                    if (bImagem != null) {
                        imgErro.setVisibility(View.VISIBLE);
                        imgErro.setImageBitmap(bImagem, localArquivo);
                        imgErro.setStatus("0", null);
                    } else
                        Utilidades.retornaMensagem(SuporteActivity.this, "Imagem não selecionada", false);
                } else
                    Utilidades.retornaMensagem(SuporteActivity.this, "Imagem não selecionada", false);
            }
        } catch (Exception ex) {
            Utilidades.retornaMensagem(SuporteActivity.this, "Erro: " + ex.toString(), false);
        }
    }
}