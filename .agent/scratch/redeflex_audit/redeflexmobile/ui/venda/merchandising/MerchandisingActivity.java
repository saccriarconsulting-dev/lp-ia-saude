package com.axys.redeflexmobile.ui.venda.merchandising;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBMerchandising;
import com.axys.redeflexmobile.shared.models.venda.Imagem;
import com.axys.redeflexmobile.shared.models.venda.merchandising.MerchandisingEnvelopamento;
import com.axys.redeflexmobile.shared.models.venda.merchandising.MerchandisingNenhum;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_Imagem;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MerchandisingActivity extends BaseActivity {

    public String itemSelecionado = "";

    @BindView(R.id.merchadising_sp_tipos) Spinner spTipos;
    @BindView(R.id.merchandising_ll_fachada_padrao) LinearLayout llFachadaPadrao;
    @BindView(R.id.merchandising_ll_envelopamento) LinearLayout llEnvelopamento;
    @BindView(R.id.merchandising_ll_nenhum) LinearLayout llNenhum;
    @BindView(R.id.merchandising_bt_proximo) Button btProximo;
    @BindView(R.id.merchadising_sp_interno) Spinner spInterno;
    @BindView(R.id.merchadising_sp_externo) Spinner spExterno;
    @BindView(R.id.merchadising_sp_operadora) Spinner spOperadora;
    @BindView(R.id.merchadising_nenhum_sp_permite_merchan) Spinner spNenhumMerchandising;
    @BindView(R.id.merchandising_evelopamento_foto) LinearLayout llEnvelopamentoFoto;
    @BindView(R.id.merchandising_nenhum_foto) LinearLayout llNenhumFoto;

    private ArrayAdapter<String> adapterSpinner;
    private ArrayAdapter<String> adapterSpinnerMerchandising;
    private ArrayAdapter<String> adapterSpinnerOperadoras;
    private Imagem imagem;
    private List<String> listaTipos;
    private List<String> listaSimNao;
    private List<String> listaOperadoras;
    private String ENVELOPAMENTO = "";
    private String NENHUM = "";
    private String FACHADA = "";
    private String PADRAO = "";
    private String SELECIONE_PRODUTO = "Selecione o tipo";
    private String SIM = "";
    private String NAO = "";
    private DBMerchandising dbMerchandising;

    @Override
    protected int getContentView() {
        return R.layout.activity_merchandising;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getStringByResId(R.string.merchandising_titulo_toolbar));
        }

        ENVELOPAMENTO = getStringByResId(R.string.merchandising_tipo_envelopamento);
        NENHUM = getStringByResId(R.string.merchandising_tipo_nenhum);
        FACHADA = getStringByResId(R.string.merchandising_tipo_fachada);
        PADRAO = getStringByResId(R.string.merchandising_tipo_padrao);
        SIM = getStringByResId(R.string.sim);
        NAO = getStringByResId(R.string.nao);

        listaTipos = Arrays.asList(ENVELOPAMENTO, NENHUM, FACHADA, PADRAO, SELECIONE_PRODUTO);
        listaSimNao = Arrays.asList(SIM, NAO);
        listaOperadoras = Arrays.asList(ConstantesMerchandising.VIVO, ConstantesMerchandising.CLARO, ConstantesMerchandising.TIM, ConstantesMerchandising.OI);

        dbMerchandising = new DBMerchandising(this);
        iniciarSpinner();
        iniciarSpinnerMerchandising();
        iniciarSpinnerOperadoras();
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
                onBackPressed();
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

    @OnClick(R.id.merchandising_bt_proximo)
    public void onClickProximo() {
        if (itemSelecionado.equals(ENVELOPAMENTO)) {
            validaConclusao(ENVELOPAMENTO);
            return;
        }

        if (itemSelecionado.equals(NENHUM)) {
            validaConclusao(NENHUM);
            return;
        }

        if (itemSelecionado.equals(FACHADA)) {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVisita, getIntent().getExtras().getInt(Config.CodigoVisita));
            bundle.putString(Config.CodigoCliente, getIntent().getExtras().getString(Config.CodigoCliente));
            bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_INTERNO, spInterno.getSelectedItem().toString().equalsIgnoreCase(SIM));
            bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_EXTERNO, spExterno.getSelectedItem().toString().equalsIgnoreCase(SIM));
            Utilidades.openNewActivity(this, SelecionarFachadaActivity.class, bundle, false);
            return;
        }

        if (itemSelecionado.equals(PADRAO)) {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVisita, getIntent().getExtras().getInt(Config.CodigoVisita));
            bundle.putString(Config.CodigoCliente, getIntent().getExtras().getString(Config.CodigoCliente));

            boolean merchanInterno = spInterno.getSelectedItem().toString().equalsIgnoreCase(SIM);
            boolean merchanExterno = spExterno.getSelectedItem().toString().equalsIgnoreCase(SIM);
            int tipo;

            if (merchanInterno) {
                tipo = ConstantesMerchandising.TIPO_INTERNO;
            } else if (merchanExterno) {
                tipo = ConstantesMerchandising.TIPO_EXTERNO;
            } else {
                tipo = ConstantesMerchandising.TIPO_OBRIGATORIO;
            }

            bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_INTERNO, merchanInterno);
            bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_EXTERNO, merchanExterno);
            bundle.putInt(ConstantesMerchandising.TIPO_MERCHANDISING, tipo);
            Utilidades.openNewActivity(this, SelecionarProdutoActivity.class, bundle, false);
        }
    }

    private void validaConclusao(String itemSelecionado) {
        if (this.imagem == null || !this.imagem.fotoTirada()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.merchandising_erro_imagem), false);
            return;
        }

        long idMerchandising = dbMerchandising.obterIdMerchandisingPorIdVisita(
                getIntent().getExtras().getInt(Config.CodigoVisita),
                itemSelecionado.equals(ENVELOPAMENTO) ? ConstantesMerchandising.ID_ENVELOPAMENTO : ConstantesMerchandising.ID_NENHUM
        );

        Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao));
        alerta.showConfirm((dialog, which) -> {
            if (ENVELOPAMENTO.equals(itemSelecionado)) {
                dbMerchandising.salvarMerchandisingEnvelopamento(
                        new MerchandisingEnvelopamento(
                                (int) idMerchandising,
                                obterIdOperadoraSelecionada(),
                                imagem.getUri().toString())
                );
            } else {
                dbMerchandising.salvarMerchandisingNenhum(
                        new MerchandisingNenhum(
                                (int) idMerchandising,
                                spNenhumMerchandising.getSelectedItem().toString().equals(SIM) ? "S" : "N",
                                imagem.getUri().toString())
                );
            }

            Utilidades.retornaMensagem(this, getStringByResId(R.string.merchandising_mensagem_sucesso), false);

            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVisita, getIntent().getExtras().getInt(Config.CodigoVisita));
            bundle.putString(Config.CodigoCliente, getIntent().getExtras().getString(Config.CodigoCliente));
            Utilidades.openNewActivity(this, VendaAberturaActivity.class, bundle, true);
        }, null);
    }

    private int obterIdOperadoraSelecionada() {
        String operadoraSelecionada = spOperadora.getSelectedItem().toString();

        switch (operadoraSelecionada) {
            case ConstantesMerchandising.VIVO:
                return ConstantesMerchandising.ID_VIVO;
            case ConstantesMerchandising.CLARO:
                return ConstantesMerchandising.ID_CLARO;
            case ConstantesMerchandising.OI:
                return ConstantesMerchandising.ID_OI;
            case ConstantesMerchandising.TIM:
                return ConstantesMerchandising.ID_TIM;
            default:
                return -1;
        }
    }

    private void editarFoto() {
        if (this.imagem == null || this.imagem.getUri() == null) {
            return;
        }

        this.imagem.setFotoTirada(true);

        Bitmap image = Util_Imagem.createScaledBitmap(this, this.imagem, Util_Imagem.IMAGE_SIZE);
        if (image == null) {
            return;
        }

        if (this.imagem.getUri().toString().contains(Util_Imagem.DEVICE_FILE)) {
            Util_Imagem.saveImage(image, this.imagem.getPath(), Util_Imagem.IMAGE_QUALITY);
        }

        preencherImagem();
    }

    private void preencherImagem() {
        if (itemSelecionado.equals(ENVELOPAMENTO)) {
            ImageView imageView = (ImageView) llEnvelopamentoFoto.findViewById(R.id.merchandising_foto_iv_foto);
            imageView.setImageURI(imagem.getUri());
            llEnvelopamentoFoto.findViewById(R.id.merchandising_foto_tv_remover).setOnClickListener(view -> {
                imageView.setImageResource(R.drawable.ic_loja);
            });

            return;
        }

        if (itemSelecionado.equals(NENHUM)) {
            ImageView imageView = (ImageView) llNenhumFoto.findViewById(R.id.merchandising_foto_iv_foto);
            imageView.setImageURI(imagem.getUri());
            llNenhumFoto.findViewById(R.id.merchandising_foto_tv_remover).setOnClickListener(view -> {
                imageView.setImageResource(R.drawable.ic_loja);
            });
        }
    }

    private void iniciarLayoutNenhum() {
        ((TextView) llNenhumFoto.findViewById(R.id.merchandising_foto_tv_local)).setText("");
        llNenhum.setVisibility(View.VISIBLE);
        btProximo.setVisibility(View.VISIBLE);
        btProximo.setText(getStringByResId(R.string.merchandising_nenhum_bt_concluir));

        llNenhumFoto.findViewById(R.id.merchandising_foto_bt_tirar_foto).setOnClickListener(view -> {
            tirarFoto();
        });
    }

    private void esconderTudo() {
        btProximo.setVisibility(View.GONE);
        llFachadaPadrao.setVisibility(View.GONE);
        llEnvelopamento.setVisibility(View.GONE);
        llNenhum.setVisibility(View.GONE);
    }

    private void iniciarLayoutFachadaPadrao() {
        llFachadaPadrao.setVisibility(View.VISIBLE);
        btProximo.setVisibility(View.VISIBLE);
        btProximo.setText(getStringByResId(R.string.merchandising_bt_proximo));
    }

    private void iniciarLayoutEnvelopamento() {
        ((TextView) llEnvelopamentoFoto.findViewById(R.id.merchandising_foto_tv_local)).setText("");
        llEnvelopamento.setVisibility(View.VISIBLE);
        btProximo.setVisibility(View.VISIBLE);
        btProximo.setText(getStringByResId(R.string.merchandising_envelop_bt_concluir));

        llEnvelopamentoFoto.findViewById(R.id.merchandising_foto_bt_tirar_foto).setOnClickListener(view -> tirarFoto());
    }

    private void tirarFoto() {
        Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentPhoto.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        String imagemNome = String.valueOf(System.currentTimeMillis()) + Util_Imagem.PHOTO_EXTENSION;
        this.imagem = new Imagem();
        this.imagem.setNome(imagemNome);

        try {
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(imagemNome, Util_Imagem.PHOTO_EXTENSION, storageDir);
            this.imagem.setPath(imageFile.getAbsolutePath());
            this.imagem.setUri(FileProvider.getUriForFile(this, (this.getApplicationContext().getPackageName() + Util_Imagem.PROVIDER), imageFile));
            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, this.imagem.getUri());
            startActivityForResult(intentPhoto, Util_Imagem.TAG_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarSpinnerOperadoras() {
        adapterSpinnerOperadoras = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                this.listaOperadoras);

        spOperadora.setAdapter(adapterSpinnerOperadoras);
    }

    private void iniciarSpinnerMerchandising() {
        adapterSpinnerMerchandising = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                this.listaSimNao);

        spInterno.setAdapter(adapterSpinnerMerchandising);
        spExterno.setAdapter(adapterSpinnerMerchandising);
        spNenhumMerchandising.setAdapter(adapterSpinnerMerchandising);
    }

    private void iniciarSpinner() {
        adapterSpinner = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                this.listaTipos) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(listaTipos.get(getCount()));
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        spTipos.setAdapter(adapterSpinner);
        spTipos.setSelection(adapterSpinner.getCount());
        spTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                esconderTudo();

                if (listaTipos.get(i).equals(ENVELOPAMENTO)) {
                    itemSelecionado = ENVELOPAMENTO;
                    iniciarLayoutEnvelopamento();
                    return;
                }

                if (listaTipos.get(i).equals(NENHUM)) {
                    itemSelecionado = NENHUM;
                    iniciarLayoutNenhum();
                    return;
                }

                if (listaTipos.get(i).equals(FACHADA)) {
                    itemSelecionado = FACHADA;
                    iniciarLayoutFachadaPadrao();
                    return;
                }

                if (listaTipos.get(i).equals(PADRAO)) {
                    itemSelecionado = PADRAO;
                    iniciarLayoutFachadaPadrao();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
