package com.axys.redeflexmobile.ui.redeflex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.RemessaItemAdapter;
import com.axys.redeflexmobile.shared.bd.DBRemessa;
import com.axys.redeflexmobile.shared.models.RemessaLista;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.zelory.compressor.Compressor;

public class RemessaDetalheActivity extends AppCompatActivity {
    ListView listaItens;
    RemessaItemAdapter mAdapter;
    ArrayList<RemessaLista> listaRemessas;
    DBRemessa dbRemessa;
    String idRemessa;
    View footerView;
    ImageView imgComprovante;
    File fileImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remessa_detalhe);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idRemessa = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            idRemessa = bundle.getString(Config.CodigoRemessa);

        if (!Util_IO.isNullOrEmpty(idRemessa)) {
            Notificacoes.cancelarNotificacao(RemessaDetalheActivity.this, Integer.parseInt(idRemessa));

            dbRemessa = new DBRemessa(RemessaDetalheActivity.this);
            listaRemessas = dbRemessa.getRemessaById(idRemessa);

            if (listaRemessas == null || listaRemessas.size() == 0) {
                Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name), "Remessa não encontrada.");
                alerta.showError();
            } else {
                getSupportActionBar().setTitle("Remessa " + listaRemessas.get(0).getNumero_capa());
                criarObjetos();
            }
        } else {
            Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name), "Remessa não encontrada.");
            alerta.showError();
        }
    }

    private void criarObjetos() {
        try {
            listaItens = (ListView) findViewById(R.id.listaItens);
            listaItens.setItemsCanFocus(true);
            mAdapter = new RemessaItemAdapter(RemessaDetalheActivity.this, R.layout.item_remessa_detalhe, listaRemessas);
            listaItens.setAdapter(mAdapter);

            if (!Util_IO.isNullOrEmpty(listaRemessas.get(0).getLocalArquivo())) {
                Bitmap bImagem = BitmapFactory.decodeFile(listaRemessas.get(0).getLocalArquivo());
                addImagem(bImagem);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(RemessaDetalheActivity.this, "Erro ao carregar dados: " + ex.getMessage(), true);
        }
    }

    private void fechar() {
        Intent detalhes = new Intent(RemessaDetalheActivity.this, RemessaActivity.class);
        startActivity(detalhes);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fechar();
                return true;
            case R.id.menu_salvar_remessa:
                if ((listaRemessas.get(0).getSituacao_capa() != 3 && listaRemessas.get(0).getSituacao_capa() != 4)
                        || (listaRemessas.get(0).getSituacao_capa() == 3 && (listaRemessas.get(0).getSituacaoArquivo() == 2
                        || listaRemessas.get(0).getSituacaoArquivo() == 3 || listaRemessas.get(0).getSituacaoArquivo() == 0))) {

                    Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name), "Deseja confirmar a remessa?");
                    alerta.showConfirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            salvar();
                        }
                    }, null);
                } else if (listaRemessas.get(0).getSituacao_capa() == 4) {
                    Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name), "Remessa cancelada");
                    alerta.show();
                } else {
                    Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name), "Remessa já confirmada");
                    alerta.show();
                }
                return true;
            case R.id.menu_add_comprovante:
                try {
                    fileImagem = Utilidades.setImagem();
                    Utilidades.loadImagefromCamera(RemessaDetalheActivity.this, fileImagem, true);
                } catch (Exception ex) {
                    Mensagens.mensagemErro(RemessaDetalheActivity.this, ex.getMessage(), false);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_remessa, menu);
        if (listaRemessas.size() > 0) {
            if (listaRemessas.get(0).getSituacao_capa() == 4) {
                return false;
            } else if (listaRemessas.get(0).getSituacao_capa() == 3) {
                return (listaRemessas.get(0).getSituacaoArquivo() == 2 || listaRemessas.get(0).getSituacaoArquivo() == 3);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void salvar() {
        try {
            if (dbRemessa.anexoPendente(idRemessa) && listaRemessas.get(0).getSituacao_capa() != 3) {
                Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name)
                        , "Existem remessas pendentes, Verifique se foi fotografado todos os comprovantes de recebimento!");
                alerta.show();
                return;
            }

            if (Util_IO.isNullOrEmpty(listaRemessas.get(0).getLocalArquivo())) {
                Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name)
                        , "Não foi fotografado o comprovante de recebimento da remessa, Verifique!");
                alerta.show();
                return;
            }

            if (listaRemessas.get(0).getSituacaoArquivo() == 2 || listaRemessas.get(0).getSituacaoArquivo() == 3) {
                Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name)
                        , "Verifique o comprovante de recebimento da remessa está inválido");
                alerta.show();
                return;
            }

            if (listaRemessas.get(0).getSituacao_capa() != 3 && listaRemessas != null) {
                for (RemessaLista item : listaRemessas) {
                    if (item.getQtdInformada_item() == 0) {
                        Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name)
                                , "Não foi informada a quantidade do item " + item.getItemDescricao() + ", Verifique");
                        alerta.show();
                        return;
                    } else if (item.getQtdInformada_item() != item.getQtdRemessa_item()) {
                        Alerta alerta = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name)
                                , "A quantidade do item " + item.getItemDescricao() + " está divergente da remessa");
                        alerta.show();
                        return;
                    }
                    dbRemessa.updateQtd(item.getIdItem_item(), item.getQtdInformada_item());
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());

            dbRemessa.updateConfirmacao(idRemessa, cal.getTime(), listaRemessas.get(0).getLocalArquivo());

            Alerta msg = new Alerta(RemessaDetalheActivity.this, getResources().getString(R.string.app_name), "Remessa confirmada com sucesso.");
            msg.show((dialog, which) -> {
                fechar();
            });
        } catch (Exception ex) {
            Mensagens.mensagemErro(RemessaDetalheActivity.this, "Erro ao confirmar remessa: " + ex.getMessage(), false);
        }
    }

    private void addImagem(Bitmap bImagem) {
        if (bImagem != null) {
            if (footerView == null) {
                footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
                listaItens.addFooterView(footerView);
            }
            TextView txtTitulo = (TextView) footerView.findViewById(R.id.txtTitulo);
            LinearLayout layoutFoto = (LinearLayout) footerView.findViewById(R.id.layoutFoto);

            footerView.setVisibility(View.VISIBLE);

            if (imgComprovante == null)
                imgComprovante = (ImageView) footerView.findViewById(R.id.imagem);

            imgComprovante.setImageBitmap(bImagem);
            Utilidades.setcorLayoutImagem(layoutFoto, String.valueOf(listaRemessas.get(0).getSituacaoArquivo()), txtTitulo);
        } else {
            if (footerView != null)
                footerView.setVisibility(View.GONE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RequestCode.CaptureImagem && resultCode == RESULT_OK) {
                String strImageCompress = Utilidades.getFilePath(RemessaDetalheActivity.this);
                File fotoFinal = new Compressor(this).setDestinationDirectoryPath(strImageCompress).compressToFile(fileImagem);
                Bitmap bImagem = BitmapFactory.decodeFile(fotoFinal.getAbsolutePath());
                if (bImagem != null) {
                    if (!Util_IO.isNullOrEmpty(this.listaRemessas.get(0).getLocalArquivo())) {
                        Utilidades.deletaArquivo(this.listaRemessas.get(0).getLocalArquivo());
                    }
                    this.listaRemessas.get(0).setLocalArquivo(fotoFinal.getAbsolutePath());
                    this.listaRemessas.get(0).setSituacaoArquivo(0);
                } else
                    this.listaRemessas.get(0).setLocalArquivo(null);
                addImagem(bImagem);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(RemessaDetalheActivity.this, "Erro ao fotografar: " + ex.getMessage(), false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utilidades.verificarHorarioComercial(RemessaDetalheActivity.this, true)) {
            Mensagens.horarioComercial(RemessaDetalheActivity.this);
        }
    }
}