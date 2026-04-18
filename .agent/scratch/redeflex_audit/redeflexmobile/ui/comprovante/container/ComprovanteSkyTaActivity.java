package com.axys.redeflexmobile.ui.comprovante.container;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.BDCampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.bd.DBComprovanteSkyTa;
import com.axys.redeflexmobile.shared.enums.TypeVoucherEnum;
import com.axys.redeflexmobile.shared.models.CampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;
import com.axys.redeflexmobile.ui.dialog.SearchableListDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ComprovanteSkyTaActivity extends BaseActivity
        implements ComprovanteSkyTaView {

    public static final int WINDOW_DURATION = 1000;
    private static final boolean ATIVAR_FILTRO = true;
    @Inject
    ComprovanteSkyTaPresenter presenter;

    @BindView(R.id.comprovante_sky_ta_et_pesquisa)
    AppCompatAutoCompleteTextView etPesquisa;
    @BindView(R.id.comprovante_sky_ta_tv_cliente)
    TextView tvCliente;
    @BindView(R.id.comprovante_sky_ta_iv_apagar)
    AppCompatImageView ivApagar;
    @BindView(R.id.comprovante_sky_ta_cl_image)
    RelativeLayout clImage;
    @BindView(R.id.comprovante_sky_ta_iv_foto)
    AppCompatImageView ivFoto;
    @BindView(R.id.comprovante_sky_ta_iv_excluir)
    AppCompatImageView ivRemover;
    @BindView(R.id.comprovante_sky_ta_ll_botoes)
    LinearLayout llBotoes;
    @BindView(R.id.comprovante_sky_ta_ll_camera)
    LinearLayout llCamera;
    @BindView(R.id.comprovante_sky_ta_ll_galeria)
    LinearLayout llGaleria;
    @BindView(R.id.comprovante_sky_ta_ll_qrcode)
    LinearLayout llQrCode;
    @BindView(R.id.comprovante_sky_ta_btn_salvar)
    Button btnSalvar;
    @BindView(R.id.comprovante_sky_ta_tv_tipo)
    TextView tvTipo;
    @BindView(R.id.comprovante_sky_cv_action)
    View cvAction;
    @BindView(R.id.comprovante_sky_ta_tv_acao)
    TextView tvActionTitle;
    @BindView(R.id.comprovante_sky_ta_action_subtitle)
    TextView tvActionSubtitle;

    // Componentes Dados Merchan Claro
    @BindView(R.id.comprovante_sky_cv_actionmaterial)
    View cvActionMaterial;
    @BindView(R.id.comprovante_sky_ta_ll_dadosClaro)
    LinearLayout llDadosMerchanClaro;
    @BindView(R.id.comprovante_sky_ta_tv_tipomaterial)
    TextView tvTipoMaterial;
    @BindView(R.id.comprovante_sky_cv_dadosClaroLocalizacao)
    View cvActionMaterialLocalizacao;

    private File fileImagem;
    private CompositeDisposable disposables = new CompositeDisposable();

    public static Intent getStart(Context context) {
        return new Intent(context, ComprovanteSkyTaActivity.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_comprovante_sky_ta;
    }

    @Override
    protected void initialize() {
        mudarTitulo();
        showBackButtonToolbar();
        removerSombraActionBar();
        mudarCorIcone();
        mudarCorIconeSeta();
        iniciarPresenter();
        iniciarEventos();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void popularAdapter(List<Cliente> clientes) {
        ArrayAdapter adapter = new ComprovanteSkyTaAdapter(
                this,
                R.layout.item_comprovante_sky_ta_cliente_pesquisa,
                clientes
        );
        etPesquisa.setThreshold(1);
        etPesquisa.setAdapter(adapter);
    }

    @Override
    public void salvoSucesso() {
        String mensagem = getString(R.string.comprovanteskyta_mensagem_sucesso_sky);
        Alerta alerta = new Alerta(
                this,
                getString(R.string.app_name),
                mensagem
        );
        alerta.show((dialog, which) -> apagarDados());
    }

    @Override
    public void mostrarErroCliente() {
        runOnUiThread(() -> Mensagens.mensagemErro(
                ComprovanteSkyTaActivity.this,
                getString(R.string.comprovanteskyta_mensagem_erro_cliente),
                false
        ));
    }

    @Override
    public void mostrarErroImagem() {
        runOnUiThread(() -> Mensagens.mensagemErro(
                ComprovanteSkyTaActivity.this,
                getString(R.string.comprovanteskyta_mensagem_erro_imagem),
                false
        ));
    }

    @Override
    public void mostrarErroTipo() {
        runOnUiThread(() -> Mensagens.mensagemErro(
                ComprovanteSkyTaActivity.this,
                getString(R.string.comprovanteskyta_mensagem_erro_tipo),
                false
        ));
    }

    @Override
    public void mostrarErroCampanha() {
        runOnUiThread(() -> Mensagens.mensagemErro(
                ComprovanteSkyTaActivity.this,
                getString(R.string.comprovanteskyta_tv_action_qrcode_subtitle),
                false
        ));
    }

    @Override
    public void mostrarErroMaterial() {
        runOnUiThread(() -> Mensagens.mensagemErro(
                ComprovanteSkyTaActivity.this,
                getString(R.string.comprovanteskyta_mensagem_erro_material),
                false
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        String strImageCompress = Utilidades.getFilePath(this);
        if (requestCode == RequestCode.CaptureImagem) {
            try {
                File fotoFinal = new Compressor(this)
                        .setDestinationDirectoryPath(strImageCompress)
                        .compressToFile(fileImagem);
                if (fotoFinal == null) return;

                setImage(fotoFinal.getAbsolutePath());
            } catch (IOException e) {
                getImageError();
            }
        } else if (requestCode == RequestCode.GaleriaImagem && data != null) {
            try {
                File file = Utilidades.getImagemFromGallery(data, this);
                if (file == null) return;
                File fileFinal = new Compressor(this)
                        .setDestinationDirectoryPath(strImageCompress)
                        .compressToFile(file);
                if (fileFinal == null) return;
                setImage(fileFinal.getAbsolutePath());
            } catch (Exception e) {
                getImageError();
            }
        } else {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                getMerchanClaroKey(result.getContents());
            } else {
                scanMerchanClaroKeyError(getString(R.string.scan_merchan_claro_error));
            }
        }
    }

    private void getMerchanClaroKey(String contents) {
        if (contents == null || contents.isEmpty()) {
            scanMerchanClaroKeyError(getString(R.string.scan_merchan_claro_error));
        } else {
            try {
                //int key = Integer.parseInt(contents.split("=")[1]);
                int key = Integer.parseInt(contents.split("campanha=")[1].split("&")[0]);
                setCampanha(key);
            } catch (Exception e) {
                scanMerchanClaroKeyError(getString(R.string.scan_merchan_claro_error));
            }
        }
    }

    public void scanMerchanClaroKeyError(String message) {
        setCampanha(-1);
        Mensagens.mensagemErro(ComprovanteSkyTaActivity.this, message, false);
    }

    public void getImageError() {
        Mensagens.mensagemErro(ComprovanteSkyTaActivity.this, getString(R.string.capture_image_error), false);
    }

    private void mudarCorIcone() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_search_24dp_wrapped);
        if (drawable == null) {
            return;
        }
        Drawable drawableWrap = DrawableCompat.wrap(drawable);
        int cor = ContextCompat.getColor(
                ComprovanteSkyTaActivity.this,
                R.color.colorPrimary
        );
        DrawableCompat.setTint(drawableWrap, cor);
        etPesquisa.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
        );
    }

    private void mudarCorIconeSeta() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down_grey_wrapped);
        if (drawable == null) {
            return;
        }
        Drawable drawableWrap = DrawableCompat.wrap(drawable);
        int cor = ContextCompat.getColor(
                ComprovanteSkyTaActivity.this,
                R.color.colorPrimary
        );
        DrawableCompat.setTint(drawableWrap, cor);
        tvTipo.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
        );
        DrawableCompat.setTint(drawableWrap, cor);
        tvTipoMaterial.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
        );
    }

    private void removerSombraActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
    }

    private void iniciarPresenter() {
        presenter.obterClientes();
    }

    private void iniciarEventos() {
        etPesquisa.setOnItemClickListener((parent, view, position, id) -> {
            Cliente cliente = (Cliente) parent.getItemAtPosition(position);
            tvCliente.setText(cliente.getNomeFantasia());
            etPesquisa.setText(StringUtils.EMPTY_STRING, ATIVAR_FILTRO);
            presenter.setCliente(cliente);
            closeKeyboard();
        });

        ivApagar.setOnClickListener(view -> {
            tvCliente.setText(StringUtils.EMPTY_STRING);
            etPesquisa.requestFocus();
            presenter.setCliente(null);
        });

        ivRemover.setOnClickListener(view -> {
            presenter.setImage(null);
            llBotoes.setVisibility(View.VISIBLE);
            clImage.setVisibility(View.GONE);
        });

        disposables.add(RxView.clicks(tvTipo)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> abrirPesquisaTipo(), Timber::e));

        disposables.add(RxView.clicks(llCamera)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> {
                    try {
                        fileImagem = Utilidades.setImagem();
                        Utilidades.loadImagefromCamera(this, fileImagem, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Timber::e));

        disposables.add(RxView.clicks(llGaleria)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> {
                    try {
                        Utilidades.loadImagefromGallery(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Timber::e));

        disposables.add(RxView.clicks(llQrCode)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> {
                    openScan();
                }, Timber::e));

        disposables.add(RxView.clicks(btnSalvar)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(value -> presenter.salvar(), Timber::e));

        disposables.add(RxView.clicks(tvTipoMaterial)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> abrirPesquisaTipoMaterial(), Timber::e));
    }

    private void openScan() {
        try {
            CodeReader.openCodeReader(this, IntentIntegrator.QR_CODE);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ComprovanteSkyTaActivity.this, ex.getMessage(), false);
        }
    }

    private void setCampanha(int idCampanha) {
        presenter.setCampanha(idCampanha);
        if (idCampanha != -1) {
            tvActionSubtitle.setText("Código da campanha:" + idCampanha);
        } else {
            tvActionSubtitle.setText(getString(R.string.comprovanteskyta_tv_action_qrcode_subtitle));
        }
    }

    private void mudarTitulo() {
        setTitle(getString(R.string.comprovanteskyta_titulo_sky));
    }

    private void mudarAcao(String title, String subtitle) {
        tvActionTitle.setText(title);
        tvActionSubtitle.setText(subtitle);
    }

    private void setImage(Bitmap bitmap) {
        if (bitmap == null) return;
        clImage.setVisibility(View.VISIBLE);
        llBotoes.setVisibility(View.GONE);
        ivFoto.setImageBitmap(bitmap);
    }

    private void setImage(String imageString) {
        presenter.setImage(imageString);
        setImage(BitmapFactory.decodeFile(imageString));
    }

    private void abrirPesquisaTipo() {
        SearchableListDialog dialogPesquisa = SearchableListDialog.newInstance(TypeVoucherEnum.getEnumDisplayList());
        dialogPesquisa.setOnSearchableItemClickListener((item, position) -> {
            String displayType = item.toString();
            int valueType = getTipo(displayType);
            tvTipo.setText(displayType);
            presenter.setTipo(valueType);
            updateViewBySelectedType(valueType);
        });
        dialogPesquisa.show(getSupportFragmentManager(), SearchableListDialog.class.getSimpleName());
    }

    private void abrirPesquisaTipoMaterial() {
        BDCampanhaMerchanClaroMaterial dbCampanhaMerchanClaroMaterial = new BDCampanhaMerchanClaroMaterial(this);

        SearchableListDialog dialogPesquisaMaterial = SearchableListDialog.newInstance(dbCampanhaMerchanClaroMaterial.getMaterialAtivo());
        dialogPesquisaMaterial.setOnSearchableItemClickListener((item, position) -> {
            String displayType = item.toString();
            int valueType = getMaterial(displayType);
            tvTipoMaterial.setText(displayType);
            presenter.setTipoMaterial(valueType);
        });
        dialogPesquisaMaterial.show(getSupportFragmentManager(), SearchableListDialog.class.getSimpleName());
    }

    private void updateViewTypeMerchanClaro() {
        String subtitle = getString(R.string.comprovanteskyta_tv_action_qrcode_subtitle);
        if (presenter.getIdCampanha() != -1) {
            subtitle = "Código da campanha:" + presenter.getIdCampanha();
        }
        mudarAcao(getString(R.string.comprovanteskyta_tv_action_qrcode), subtitle);
        llBotoes.setVisibility(View.VISIBLE);
        clImage.setVisibility(View.GONE);
        llCamera.setVisibility(View.GONE);
        llGaleria.setVisibility(View.GONE);
        llQrCode.setVisibility(View.VISIBLE);
        cvActionMaterial.setVisibility(View.VISIBLE);
        cvActionMaterialLocalizacao.setVisibility(View.VISIBLE);
    }

    private void updateViewTypeOthers() {
        mudarAcao(getString(R.string.comprovanteskyta_tv_foto_titulo_sky), getString(R.string.comprovanteskyta_tv_foto_mensagem));
        llCamera.setVisibility(View.VISIBLE);
        llGaleria.setVisibility(View.VISIBLE);
        llQrCode.setVisibility(View.GONE);
        if (presenter.hasSelectedImage()) {
            clImage.setVisibility(View.VISIBLE);
            llBotoes.setVisibility(View.GONE);
        } else {
            clImage.setVisibility(View.GONE);
            llBotoes.setVisibility(View.VISIBLE);
        }
        cvActionMaterial.setVisibility(View.GONE);
        cvActionMaterialLocalizacao.setVisibility(View.GONE);
    }

    private void updateViewBySelectedType(int valueType) {
        cvAction.setVisibility(View.VISIBLE);
        if (valueType == TypeVoucherEnum.MERCHAN_CLARO.value) {
            updateViewTypeMerchanClaro();
        } else {
            updateViewTypeOthers();
        }
    }

    private int getTipo(String tipo) {
        TypeVoucherEnum type = TypeVoucherEnum.displayToTypeVoucherEnum(tipo);
        if (type != null) {
            return type.value;
        }
        return TypeVoucherEnum.TA.value;
    }

    private int getMaterial(String tipoMaterial) {
        if (!Util_IO.isNullOrEmpty(tipoMaterial)) {
            int posicao = tipoMaterial.indexOf("-");
            String vId = tipoMaterial.substring(4,posicao).trim();
            return Integer.valueOf(vId);
        }
        return -1;
    }

    private void apagarDados() {
        presenter.setImage(null);
        presenter.setCliente(null);
        presenter.setTipo(-1);
        presenter.setCampanha(-1);
        presenter.setTipoMaterial(-1);
        presenter.setInterno(0);

        ivRemover.performClick();
        ivApagar.performClick();
        tvTipo.setText(StringUtils.EMPTY_STRING);
        tvTipoMaterial.setText(StringUtils.EMPTY_STRING);
        cvAction.setVisibility(View.GONE);
        clImage.setVisibility(View.GONE);
        llBotoes.setVisibility(View.GONE);
        cvActionMaterial.setVisibility(View.GONE);
        cvActionMaterialLocalizacao.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.comprovante_sky_rdbInterno:
                if (checked)
                    presenter.setInterno(0);
                break;
            case R.id.comprovante_sky_rdbExterno:
                if (checked)
                    presenter.setInterno(1);
                break;
        }
    }
}
