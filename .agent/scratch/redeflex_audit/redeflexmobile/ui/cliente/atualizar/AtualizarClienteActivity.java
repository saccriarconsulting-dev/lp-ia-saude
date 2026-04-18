package com.axys.redeflexmobile.ui.cliente.atualizar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstado;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Estado;
import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.CampoCliente;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;
import com.axys.redeflexmobile.ui.dialog.ImageChooserDialog;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import io.reactivex.disposables.CompositeDisposable;

public class AtualizarClienteActivity extends BaseActivity implements AtualizarClienteView {

    public static final String TEXTO_VAZIO = "";
    public static final int TAMANHO_CEP = 8;
    private static final String ID_CLIENTE = "ac_ic_cliente";
    private static final String MASK_CEP = "[00000]-[000]";
    private static final String MASK_CELULAR = "[00000]-[0000]";
    private static final String MASK_TELEFONE = "[0000]-[0000]";
    @Inject AtualizarClientePresenter presenter;
    @Inject DBEstado dbEstado;
    @Inject DBSegmento dbSegmento;
    @Inject DBLocalizacaoCliente dbLocalizacaoCliente;

    @BindView(R.id.atualizar_cliente_et_nome_fantasia) CampoCliente etNomeFantasia;
    @BindView(R.id.atualizar_cliente_et_razao_social) CampoCliente etRazaoSocial;
    @BindView(R.id.atualizar_cliente_et_nome_contato) CampoCliente etNomeContato;
    @BindView(R.id.atualizar_cliente_et_ddd_telefone) CampoCliente etDddTelefone;
    @BindView(R.id.atualizar_cliente_et_telefone) CampoCliente etTelefone;
    @BindView(R.id.atualizar_cliente_et_ddd_celular) CampoCliente etDddCelular;
    @BindView(R.id.atualizar_cliente_et_celular) CampoCliente etCelular;
    @BindView(R.id.atualizar_cliente_et_tipo_logradouro) CampoCliente etTipoLogradouro;
    @BindView(R.id.atualizar_cliente_et_nome_logradouro) CampoCliente etNomeLogradouro;
    @BindView(R.id.atualizar_cliente_et_numero_logradouro) CampoCliente etNumeroLogradouro;
    @BindView(R.id.atualizar_cliente_et_complemento_logradouro) CampoCliente etComplementoLogradouro;
    @BindView(R.id.atualizar_cliente_et_cidade) CampoCliente etCidade;
    @BindView(R.id.atualizar_cliente_et_bairro) CampoCliente etBairro;
    @BindView(R.id.atualizar_cliente_et_estado) CampoCliente etEstado;
    @BindView(R.id.atualizar_cliente_et_cep) CampoCliente etCep;
    @BindView(R.id.atualizar_cliente_et_segmento) CampoCliente etSegmento;
    @BindView(R.id.atualizar_cliente_et_ponto_referencia) CampoCliente etPontoReferencia;
    @BindView(R.id.atualizar_cliente_et_email) CampoCliente etEmail;
    @BindView(R.id.atualizar_cliente_btn_salvar) Button btnSalvar;
    @BindView(R.id.atualizar_cliente_sv_scroll) ScrollView svScroll;
    @BindView(R.id.atualizar_cliente_anexo_item_iv_thumbnail) AppCompatImageView imgAnexo;
    @BindView(R.id.atualizar_cliente_anexo_item_iv_remove) ImageView imgRemove;
    @BindView(R.id.atualizar_cliente_anexo_item_tv_status)    TextView tvStatusAnexo;
    @BindView(R.id.atualizar_cliente_anexo_item_tv_add) TextView tvAdicionar;
    @BindView(R.id.atualizar_cliente_anexo_item_tv_tamanho) TextView tvTamanho;

    private ImageChooserDialog dialog;
    private File tempFile;
    private LocalizacaoCliente localizaCliente;

    private GenericaDialog searchableListDialogEstado;
    private GenericaDialog searchableListDialogSegmento;
    private String segmentoSelecionado;
    private String estadoSelecionado;
    private ProgressDialog progressDialog;

    public static Intent iniciarActivity(Context context, String id) {
        Intent intent = new Intent(context, AtualizarClienteActivity.class);
        intent.putExtra(ID_CLIENTE, id);

        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.atualizar_cliente_layout;
    }

    @Override
    protected void initialize() {
        setTitle();
        adicionarMascara();
        iniciarEventos();
        carregarDados();
        criarListaEstado();
        criarListaSegmento();
        localizaCliente = new LocalizacaoCliente();
    }

    @Override
    public void showLoading() {
        progressDialog.setMessage(
                getString(R.string.atualizar_cliente_message_carregando)
        );
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        boolean possoVoltar = presenter.possoVoltar(
                etNomeFantasia.getText(),
                etRazaoSocial.getText(),
                etNomeContato.getText(),
                etDddTelefone.getText(),
                etTelefone.getText(),
                etDddCelular.getText(),
                etCelular.getText(),
                etEmail.getText(),
                etTipoLogradouro.getText(),
                etNomeLogradouro.getText(),
                etNumeroLogradouro.getText(),
                etComplementoLogradouro.getText(),
                etCidade.getText(),
                etBairro.getText(),
                etEstado.getText(),
                etCep.getText(),
                etSegmento.getText(),
                etPontoReferencia.getText()
        );
        if (possoVoltar) {
            finish();

            return;
        }

        Alerta alerta = new Alerta(
                this,
                getString(R.string.atualizar_cliente_titulo_alerta),
                getString(R.string.atualizar_cliente_mensagem_alerta)
        );
        alerta.showConfirm((dialogInterface, i) -> finish(), (dialogInterface, i) -> {
        });
    }

    @Override
    public void popularTela(AtualizaCliente cliente, Segmento segmento) {
        popularCampos(
                cliente.getNomeFantasia(),
                cliente.getRazaoSocial(),
                cliente.getNomeContato(),
                cliente.getDddTelefone(),
                cliente.getTelefone(),
                cliente.getDddCelular(),
                cliente.getCelular(),
                cliente.getEmail(),
                cliente.getTipoLogradouro(),
                cliente.getNomeLogradouro(),
                cliente.getNumeroLogradouro(),
                cliente.getComplementoLogradouro(),
                cliente.getCidade(),
                cliente.getBairro(),
                dbEstado.getEstadobySigla(cliente.getEstado()).getDescricao(),
                cliente.getCep(),
                segmento != null ? segmento.getDescricao() : null,
                cliente.getPontoReferencia()
        );

        this.segmentoSelecionado = cliente.getSegmento();
        this.estadoSelecionado = cliente.getEstado();
    }

    @Override
    public void popularTela(Cliente cliente, Segmento segmento) {
        popularCampos(
                cliente.getNomeFantasia(),
                cliente.getRazaoSocial(),
                cliente.getContato(),
                cliente.getDddTelefone(),
                cliente.getTelefone(),
                cliente.getDddCelular(),
                cliente.getCelular(),
                cliente.getEmailCliente(),
                cliente.getTipoLogradouro(),
                cliente.getNomeLogradouro(),
                cliente.getNumeroLogradouro(),
                cliente.getComplementoLogradouro(),
                cliente.getCidade(),
                cliente.getBairro(),
                dbEstado.getEstadobySigla(cliente.getEstado()).getDescricao(),
                cliente.getCep(),
                segmento != null ? segmento.getDescricao() : null,
                cliente.getPontoReferencia()
        );

        this.segmentoSelecionado = cliente.getIdSegmentoSGV();
        this.estadoSelecionado = cliente.getEstado();
    }

    @Override
    public void mostrarErroNomeFantasia() {
        etNomeFantasia.setError(getString(R.string.atualizar_cliente_erro_nome_fantasia));
    }

    @Override
    public void mostrarErroRazaoSocial() {
        etRazaoSocial.setError(getString(R.string.atualizar_cliente_erro_razao_social));
    }

    @Override
    public void mostrarErroNomeContato() {
        etNomeContato.setError(getString(R.string.atualizar_cliente_erro_nome_contato));
    }

    @Override
    public void mostrarErroTipoLogradouro() {
        etTipoLogradouro.setError(getString(R.string.atualizar_cliente_erro_tipo_logradouro));
    }

    @Override
    public void mostrarErroNomeLogradouro() {
        etNomeLogradouro.setError(getString(R.string.atualizar_cliente_erro_nome_logradouro));
    }

    @Override
    public void mostrarErroNumeroLogradouro() {
        etNumeroLogradouro.setError(getString(R.string.atualizar_cliente_erro_numero_logradouro));
    }

    @Override
    public void mostrarErroCidade() {
        etCidade.setError(getString(R.string.atualizar_cliente_erro_cidade));
    }

    @Override
    public void mostrarErroBairro() {
        etBairro.setError(getString(R.string.atualizar_cliente_erro_bairro));
    }

    @Override
    public void mostrarErroEstado() {
        etEstado.setError(getString(R.string.atualizar_cliente_erro_estado));
    }

    @Override
    public void mostrarErroSegmento() {
        etSegmento.setError(getString(R.string.atualizar_cliente_erro_segmento));
        etNomeFantasia.requestFocus();
        svScroll.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    public void mostrarErroCep() {
        etCep.setError(getString(R.string.atualizar_cliente_erro_cep));
    }

    @Override
    public void mostrarSucesso() {
        Alerta alerta = new Alerta(
                this,
                getString(R.string.atualizar_cliente_titulo_alerta),
                getString(R.string.atualizar_cliente_mensagem_sucesso_alerta)
        );
        alerta.show((dialogInterface, i) -> finish());
    }

    @Override
    public void trocarLabelsPessoaFisica() {
        etNomeFantasia.setLabel(
                getString(R.string.atualizar_cliente_tv_empresa)
        );
        etRazaoSocial.setLabel(
                getString(R.string.atualizar_cliente_tv_nome)
        );
    }

    @Override
    public void mostrarErroTelefoneCelular() {
        Utilidades.retornaMensagem(this, getString(R.string.atualizar_cliente_erro_telefone_celular), true);
    }

    @Override
    public void mostrarErroDDDTelefone() {
        etDddTelefone.setError(getString(R.string.atualizar_cliente_erro_ddd_telefone));
    }

    @Override
    public void mostrarErroTelefone() {
        etTelefone.setError(getString(R.string.atualizar_cliente_erro_telefone));
    }

    @Override
    public void mostrarErroDDDCelular() {
        etDddCelular.setError(getString(R.string.atualizar_cliente_erro_ddd_telefone));
    }

    @Override
    public void mostrarErroCelular() {
        etCelular.setError(getString(R.string.atualizar_cliente_erro_celular));
    }

    @Override
    public void mostrarErroEmail() {
        etEmail.setError(getString(R.string.atualizar_cliente_erro_email));
    }

    @Override
    public void exibirErroCep() {
        new Alerta(this, getResources().getString(R.string.app_name), getString(R.string.atualizar_cliente_erro_obter_cep)).show();
        limparCamposCep();
    }

    private void limparCamposCep() {
        etTipoLogradouro.setText(TEXTO_VAZIO);
        etNomeLogradouro.setText(TEXTO_VAZIO);
        etComplementoLogradouro.setText(TEXTO_VAZIO);
        etBairro.setText(TEXTO_VAZIO);
        etCidade.setText(TEXTO_VAZIO);
        etEstado.setText(TEXTO_VAZIO);
        etNumeroLogradouro.setText(TEXTO_VAZIO);
    }

    @Override
    public void preencherCamposCep(Cep cep) {
        etTipoLogradouro.setText(cep.getTipoLogradouro());
        etNomeLogradouro.setText(cep.getNomeLogradouro());
        etComplementoLogradouro.setText(cep.getComplementoLogradouro());
        etBairro.setText(cep.getNomeBairro());
        etCidade.setText(cep.getNomeMunicipio());
        etEstado.setText(dbEstado.getEstadobySigla(cep.getUf()).getDescricao());
        etNumeroLogradouro.setText(TEXTO_VAZIO);
    }

    private void popularCampos(String nomeFantasia, String razaoSocial, String nomeContato,
                               String dddTelefone, String telefone, String dddCelular,
                               String celular, String email, String tipoLogradouro, String nomeLogradouro,
                               String numeroLogradouro, String complementoLogradouro, String cidade,
                               String bairro, String estado, String cep, String segmento, String pontoReferencia) {
        runOnUiThread(() -> {
            etNomeFantasia.setText(nomeFantasia);
            etRazaoSocial.setText(razaoSocial);
            etNomeContato.setText(nomeContato);
            etDddTelefone.setText(dddTelefone);
            etTelefone.setText(telefone);
            etDddCelular.setText(dddCelular);
            etCelular.setText(celular);
            etEmail.setText(email);
            etTipoLogradouro.setText(tipoLogradouro);
            etNomeLogradouro.setText(nomeLogradouro);
            etNumeroLogradouro.setText(numeroLogradouro);
            etComplementoLogradouro.setText(complementoLogradouro);
            etCidade.setText(cidade);
            etBairro.setText(bairro);
            etEstado.setText(estado);
            etCep.setText(cep);
            etSegmento.setText(segmento);
            etPontoReferencia.setText(pontoReferencia);

            etDddCelular.setOnTextChanged((charSequence, i, i1, i2) -> {
                if (charSequence.length() == 2) {
                    etCelular.requestFocus();
                }
            });

            etDddTelefone.setOnTextChanged((charSequence, i, i1, i2) -> {
                if (charSequence.length() == 2) {
                    etTelefone.requestFocus();
                }
            });

            etCep.setOnTextChanged((charSequence, i, i1, i2) -> {
                String cepInformado = charSequence.toString().replace("-", TEXTO_VAZIO);
                if (cepInformado.length() == TAMANHO_CEP) {
                    if (!etCep.getText().replace("-", TEXTO_VAZIO).equals(cep))
                        presenter.obterInformacoesCep(cepInformado);
                }
            });
        });
    }

    private void iniciarEventos() {
        progressDialog = new ProgressDialog(this);
        btnSalvar.setOnClickListener(v -> salvar());
        etEstado.setClickableEventField(v -> mostrarListaEstado());
        etSegmento.setClickableEventField(v -> mostrarListaSegmento());
        imgAnexo.setOnClickListener(v -> AnexarArquivo());
        imgRemove.setOnClickListener(v -> RemoverAnexo());
    }

    private void carregarDados() {
        String id = getIntent().getStringExtra(ID_CLIENTE);
        presenter.carregarUsuarioAtualizado(id);
    }

    private void salvar() {
        if (!presenter.validaEndereco(etTipoLogradouro.getText(),
                etNomeLogradouro.getText(),
                etNumeroLogradouro.getText(),
                etCidade.getText(),
                etBairro.getText(),
                this.estadoSelecionado,
                etCep.getText()))
        {
            if (Util_IO.isNullOrEmpty(localizaCliente.getLocalArquivo()))
            {
                new Alerta(this, getResources().getString(R.string.app_name), "Houve alteração no Endereço, obrigatório anexar o comprovante de Endereço.").show();
                return;
            }
        }

        presenter.salvar(
                etNomeFantasia.getText(),
                etRazaoSocial.getText(),
                etNomeContato.getText(),
                etDddTelefone.getText(),
                etTelefone.getText(),
                etDddCelular.getText(),
                etCelular.getText(),
                etEmail.getText(),
                etTipoLogradouro.getText(),
                etNomeLogradouro.getText(),
                etNumeroLogradouro.getText(),
                etComplementoLogradouro.getText(),
                etCidade.getText(),
                etBairro.getText(),
                this.estadoSelecionado,
                etCep.getText(),
                this.segmentoSelecionado,
                etPontoReferencia.getText(),
                String.valueOf(new DBColaborador(this).get().getId()),
                localizaCliente
        );
    }

    private void criarListaEstado() {
        List<GenericaDialog.GenericaItem> listEstado = Stream.ofNullable(dbEstado.getEstado())
                .map(value -> (GenericaDialog.GenericaItem) value)
                .toList();
        searchableListDialogEstado = GenericaDialog.newInstance(listEstado);
        searchableListDialogEstado.setOnSearchableItemClickListener(item -> {
            Estado estado = (Estado) item;
            etEstado.setText(estado.getDescricao());
            estadoSelecionado = estado.getSigla();
        });
    }

    private void criarListaSegmento() {
        List<GenericaDialog.GenericaItem> listSegmento = Stream.ofNullable(dbSegmento.getSegmento())
                .map(value -> (GenericaDialog.GenericaItem) value)
                .toList();
        searchableListDialogSegmento = GenericaDialog.newInstance(listSegmento);
        searchableListDialogSegmento.setOnSearchableItemClickListener(item -> {
            Segmento segmento = (Segmento) item;
            etSegmento.setText(segmento.getDescricao());
            etSegmento.setError(null);
            segmentoSelecionado = segmento.getCodigo();
        });
    }

    private void mostrarListaSegmento() {
        searchableListDialogSegmento.show(getSupportFragmentManager(), GenericaDialog.class.getSimpleName());
    }

    private void mostrarListaEstado() {
        searchableListDialogEstado.show(getSupportFragmentManager(), GenericaDialog.class.getSimpleName());
    }

    private void setTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.atualizar_cliente_titulo));
        }
    }

    private void adicionarMascara() {
        final MaskedTextChangedListener maskCep = new MaskedTextChangedListener(
                MASK_CEP,
                etCep.getEditText(),
                null
        );
        final MaskedTextChangedListener maskCelular = new MaskedTextChangedListener(
                MASK_CELULAR,
                etCelular.getEditText(),
                null);
        final MaskedTextChangedListener maskTelefone = new MaskedTextChangedListener(
                MASK_TELEFONE,
                etTelefone.getEditText(),
                null
        );

        etCep.addTextWatch(maskCep);
        etCep.addTextFocus(maskCep);
        etCelular.addTextWatch(maskCelular);
        etCelular.addTextFocus(maskCelular);
        etTelefone.addTextWatch(maskTelefone);
        etTelefone.addTextFocus(maskTelefone);
    }

    private void RemoverAnexo()
    {
        localizaCliente = new LocalizacaoCliente();
        imgAnexo.setImageResource(R.drawable.attachment_item_add);
        tvTamanho.setText(R.string.customer_register_attachment_item_image_size_limit);
        tvStatusAnexo.setVisibility(View.VISIBLE);
    }

    private void AnexarArquivo() {
        localizaCliente = new LocalizacaoCliente();
        dialog = ImageChooserDialog.newInstance(EnumRegisterAttachmentType.OTHERS,
                v -> takePhoto(localizaCliente),
                v -> selectImageFromGallery(localizaCliente));
        dialog.show(getSupportFragmentManager(), ImageChooserDialog.class.getSimpleName());
    }

    private void takePhoto(LocalizacaoCliente item) {
        dialog.dismiss();
        localizaCliente = item;
        tempFile = Utilidades.setImagem();
        startCameraIntent();
    }

    private void selectImageFromGallery(LocalizacaoCliente item) {
        dialog.dismiss();
        localizaCliente = item;
        startGalleryIntent();
    }

    private void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider", tempFile));
        startActivityForResult(intent, RequestCode.CaptureImagem);
    }

    private void startGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RequestCode.GaleriaImagem);
    }

    private void getImageFromGallery(Intent data, String filePath) {
        try {
            File file = Utilidades.getImagemFromGallery(data, this);
            prepareImage(filePath, file);
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, "O aplicativo não tem permissão para acessar esta pasta. \nSerá necessário configurar a permissão de gerenciamento de todos os arquivos.", false);
        }
    }

    private void prepareImage(String path, File file) throws IOException, NullPointerException {
        File finalPhoto = new Compressor(this)
                .setDestinationDirectoryPath(path)
                .compressToFile(file);

        if (finalPhoto == null) throw new IOException("Imagem não selecionada");

        // Armazena a Imagem selecionada
        localizaCliente = new LocalizacaoCliente();
        localizaCliente.setIdCliente(getIntent().getStringExtra(ID_CLIENTE));

        GPSTracker tracker = new GPSTracker(this);
        localizaCliente.setLatitude(tracker.getLatitude());
        localizaCliente.setLongitude(tracker.getLongitude());
        localizaCliente.setPrecisao(tracker.getPrecisao());
        localizaCliente.setLocalArquivo(finalPhoto.getAbsolutePath());
        localizaCliente.setData(tracker.getDataGps());
        localizaCliente.setTipoId(2); // Tipo de Localização Cliente (Anexo Atualização)
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            String filePath = Utilidades.getFilePath(this);
            if (requestCode == RequestCode.CaptureImagem) {
                prepareImage(filePath, tempFile);
            }

            if (requestCode == RequestCode.GaleriaImagem) {
                getImageFromGallery(data, filePath);
            }

            Bitmap bitmap = null;
            if (localizaCliente.getLocalArquivo().contains("/Files/Compressed"))
                bitmap = BitmapFactory.decodeFile(localizaCliente.getLocalArquivo());
            else
                bitmap = Utilidades.decodeBase64(localizaCliente.getLocalArquivo());

            imgAnexo.setImageBitmap(bitmap);
            tvTamanho.setText(Formatter.formatShortFileSize(this, filePath.length()));
            tvStatusAnexo.setVisibility(View.GONE);

        } catch (Exception ex) {
            //Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }
}
