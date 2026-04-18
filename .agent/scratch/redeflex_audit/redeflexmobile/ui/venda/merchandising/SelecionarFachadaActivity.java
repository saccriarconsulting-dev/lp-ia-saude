package com.axys.redeflexmobile.ui.venda.merchandising;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBMerchandising;
import com.axys.redeflexmobile.shared.models.venda.Imagem;
import com.axys.redeflexmobile.shared.models.venda.merchandising.MerchandisingFachada;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_Imagem;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class SelecionarFachadaActivity extends BaseActivity {

    @BindView(R.id.merchandising_foto_interno) LinearLayout llFotoInterno;
    @BindView(R.id.merchandising_foto_externo) LinearLayout llFotoExterno;
    @BindView(R.id.merchandising_ll_interno) LinearLayout llInterno;
    @BindView(R.id.merchandising_ll_externo) LinearLayout llExterno;
    @BindView(R.id.merchandising_fachada_bt_concluir) Button btConcluir;
    private boolean merchandisingInterno;
    private boolean merchandisingExterno;
    private Imagem imagemMerchanInterno;
    private Imagem imagemMerchanExterno;
    private Imagem imagemMerchanObrigatorio;
    private String merchanAtual;
    private DBMerchandising dbMerchandising;

    @Override
    protected int getContentView() {
        return R.layout.merchandising_item_fachada;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getStringByResId(R.string.merchandising_titulo_toolbar));
        }

        dbMerchandising = new DBMerchandising(this);
        llInterno.setVisibility(View.GONE);
        llExterno.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            merchandisingInterno = bundle.getBoolean(ConstantesMerchandising.MERCHANDISING_INTERNO);
            merchandisingExterno = bundle.getBoolean(ConstantesMerchandising.MERCHANDISING_EXTERNO);

            if (merchandisingInterno) {
                llInterno.setVisibility(View.VISIBLE);
                ((TextView) llFotoInterno.findViewById(R.id.merchandising_foto_tv_local)).setText(getStringByResId(R.string.merchandising_fachada_tv_interno));
                llFotoInterno.findViewById(R.id.merchandising_foto_bt_tirar_foto).setOnClickListener(view -> tirarFoto(ConstantesMerchandising.MERCHANDISING_INTERNO));
            }

            if (merchandisingExterno) {
                llExterno.setVisibility(View.VISIBLE);
                ((TextView) llFotoExterno.findViewById(R.id.merchandising_foto_tv_local)).setText(getStringByResId(R.string.merchandising_fachada_tv_externo));
                llFotoExterno.findViewById(R.id.merchandising_foto_bt_tirar_foto).setOnClickListener(view -> tirarFoto(ConstantesMerchandising.MERCHANDISING_EXTERNO));
            }

            if (!merchandisingInterno && !merchandisingExterno) {
                llExterno.setVisibility(View.VISIBLE);
                ((TextView) llFotoExterno.findViewById(R.id.merchandising_foto_tv_local)).setText("");
                llFotoExterno.findViewById(R.id.merchandising_foto_bt_tirar_foto).setOnClickListener(view -> tirarFoto(ConstantesMerchandising.MERCHANDISING_OBRIGATORIO));
            }
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao_cancelar));
                alerta.showConfirm((dialog, which) -> onBackPressed(), null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util_Imagem.TAG_PHOTO && resultCode == RESULT_OK) {
            editarFoto();
        }
    }

    @OnClick(R.id.merchandising_fachada_bt_concluir)
    public void onClickConcluir() {
        if (!merchandisingInterno && !merchandisingExterno && imagemMerchanObrigatorio == null) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.merchandising_erro_imagem), false);
            return;
        }

        if (merchandisingInterno && imagemMerchanInterno == null) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.merchandising_fachada_erro_interno), false);
            return;
        }

        if (merchandisingExterno && imagemMerchanExterno == null) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.merchandising_fachada_erro_externo), false);
            return;
        }

        Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao));
        alerta.showConfirm((dialog, which) -> {
            salvarMerchandising();
            Toast.makeText(this, getStringByResId(R.string.merchandising_mensagem_sucesso), Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVisita, getIntent().getExtras().getInt(Config.CodigoVisita));
            bundle.putString(Config.CodigoCliente, getIntent().getExtras().getString(Config.CodigoCliente));
            Utilidades.openNewActivity(this, VendaAberturaActivity.class, bundle, true);
        }, null);
    }

    private void salvarMerchandising() {
        MerchandisingFachada merchandisingFachada = new MerchandisingFachada();

        long idMerchandising = dbMerchandising.obterIdMerchandisingPorIdVisita(
                getIntent().getExtras().getInt(Config.CodigoVisita),
                ConstantesMerchandising.ID_FACHADA
        );

        merchandisingFachada.setIdMerchandising((int) idMerchandising);

        if (merchandisingInterno) {
            merchandisingFachada.setMerchanInterno(true);
            merchandisingFachada.setCaminhoFotoInterno(imagemMerchanInterno.getUri().toString());
        } else {
            merchandisingFachada.setMerchanInterno(false);
        }

        if (merchandisingExterno) {
            merchandisingFachada.setMerchanExterno(true);
            merchandisingFachada.setCaminhoFotoExterno(imagemMerchanExterno.getUri().toString());
        } else {
            merchandisingFachada.setMerchanExterno(false);
        }

        if (!merchandisingInterno && !merchandisingExterno && imagemMerchanObrigatorio != null) {
            merchandisingFachada.setCaminhoFotoExterno(imagemMerchanObrigatorio.getUri().toString());
        }

        dbMerchandising.salvarMerchandisingFachada(merchandisingFachada);
    }

    private void tirarFoto(String tipoMerchandising) {
        Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentPhoto.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        String imagemNome = String.valueOf(System.currentTimeMillis()) + Util_Imagem.PHOTO_EXTENSION;
        Imagem imagem = new Imagem();
        imagem.setNome(imagemNome);

        try {
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(imagemNome, Util_Imagem.PHOTO_EXTENSION, storageDir);
            imagem.setPath(imageFile.getAbsolutePath());
            imagem.setUri(FileProvider.getUriForFile(this, (this.getApplicationContext().getPackageName() + Util_Imagem.PROVIDER), imageFile));
            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imagem.getUri());

            switch (tipoMerchandising) {
                case ConstantesMerchandising.MERCHANDISING_INTERNO:
                    imagemMerchanInterno = imagem;
                    merchanAtual = ConstantesMerchandising.MERCHANDISING_INTERNO;
                    break;
                case ConstantesMerchandising.MERCHANDISING_EXTERNO:
                    imagemMerchanExterno = imagem;
                    merchanAtual = ConstantesMerchandising.MERCHANDISING_EXTERNO;
                    break;
                default:
                    imagemMerchanObrigatorio = imagem;
                    merchanAtual = ConstantesMerchandising.MERCHANDISING_OBRIGATORIO;
            }

            startActivityForResult(intentPhoto, Util_Imagem.TAG_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editarFoto() {
        Imagem imagem;

        switch (merchanAtual) {
            case ConstantesMerchandising.MERCHANDISING_INTERNO:
                imagem = imagemMerchanInterno;
                break;
            case ConstantesMerchandising.MERCHANDISING_EXTERNO:
                imagem = imagemMerchanExterno;
                break;
            default:
                imagem = imagemMerchanObrigatorio;
                break;
        }

        if (imagem == null || imagem.getUri() == null) {
            return;
        }

        imagem.setFotoTirada(true);

        Bitmap image = Util_Imagem.createScaledBitmap(this, imagem, Util_Imagem.IMAGE_QUALITY);
        if (image == null) {
            return;
        }

        if (imagem.getUri().toString().contains(Util_Imagem.DEVICE_FILE)) {
            Util_Imagem.saveImage(image, imagem.getPath(), Util_Imagem.IMAGE_QUALITY);
        }

        preencherImagem();
    }

    private void preencherImagem() {
        if (merchanAtual.equals(ConstantesMerchandising.MERCHANDISING_INTERNO)) {
            ImageView imageView = (ImageView) llFotoInterno.findViewById(R.id.merchandising_foto_iv_foto);
            imageView.setImageURI(imagemMerchanInterno.getUri());
            llFotoInterno.findViewById(R.id.merchandising_foto_tv_remover).setOnClickListener(view -> {
                imageView.setImageResource(R.drawable.ic_loja);
                imagemMerchanInterno = null;
            });
            return;
        }

        if (merchanAtual.equals(ConstantesMerchandising.MERCHANDISING_EXTERNO)) {
            ImageView imageView = (ImageView) llFotoExterno.findViewById(R.id.merchandising_foto_iv_foto);
            imageView.setImageURI(imagemMerchanExterno.getUri());
            llFotoExterno.findViewById(R.id.merchandising_foto_tv_remover).setOnClickListener(view -> {
                imageView.setImageResource(R.drawable.ic_loja);
                imagemMerchanExterno = null;
            });
            return;
        }

        ImageView imageView = (ImageView) llFotoExterno.findViewById(R.id.merchandising_foto_iv_foto);
        imageView.setImageURI(imagemMerchanObrigatorio.getUri());
        llFotoExterno.findViewById(R.id.merchandising_foto_tv_remover).setOnClickListener(view -> {
            imageView.setImageResource(R.drawable.ic_loja);
            imagemMerchanObrigatorio = null;
        });
    }
}
