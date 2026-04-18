package com.axys.redeflexmobile.ui.clientevalidacao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.Cadastro;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.services.BlipProvider;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationActivity;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.RegisterMotiveMigrationCancelDialog;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationActivity;
import com.axys.redeflexmobile.ui.clientevalidacao.dialog.OpenMigrationDialog;
import com.axys.redeflexmobile.ui.dialog.CadastroClienteDialog;
import com.axys.redeflexmobile.ui.dialog.cliente.ClienteInfoDialog;
import com.axys.redeflexmobile.ui.redeflex.ChatbotActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding2.view.RxView;

import org.threeten.bp.LocalDate;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class ClienteActivity extends BaseActivity implements ClienteView,
        OnMapReadyCallback,
        CadastroClienteDialog.OnCompleteListener {
    public static final int DIA_SEMANA = 0;
    public static final int SEMANA_COLABORADOR = 1;
    public static final String CLIENT_ID_KEY = "CLIENT_ID_KEY";

    @BindView(R.id.cliente_tv_nome)
    TextView tvNome;
    @BindView(R.id.cliente_tv_fantasia)
    TextView tvFantasia;
    @BindView(R.id.cliente_tv_tipoDocumento)
    TextView tvTipoDocumento;
    @BindView(R.id.cliente_tv_documento)
    TextView tvDocumento;
    @BindView(R.id.cliente_tv_endereco)
    TextView tvEndereco;
    @BindView(R.id.cliente_tv_telefone)
    TextView tvTelefone;
    @BindView(R.id.cliente_ll_info_cliente)
    LinearLayout llInfoCliente;
    @BindView(R.id.cliente_btn_iniciar)
    Button btnIniciarAtendimento;
    @BindView(R.id.cliente_tv_ver_mais)
    TextView tvVerMais;
    @BindView(R.id.cliente_img_copy)
    ImageView imgCopy;

    @Inject
    ClientePresenter clientePresenter;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private Cliente cliente;
    private Colaborador colaborador;
    private DBCliente dbCliente;
    private DBLocalizacaoCliente dbLocalizacaoCliente;
    private DBColaborador dbColaborador;
    private DBVisita dbVisita;
    private File fileImagem;
    private GPSTracker gpsTracker;
    private String codigoCliente;
    private boolean bloquearBotao;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private String diaSemanaRota;
    private String semanaRota;
    private boolean verificaPendencia = false;
    private boolean temVisitasHoje;

    @Override
    protected int getContentView() {
        return R.layout.activity_cliente;
    }

    @Override
    protected void initialize() {
        configurarToolbar();
        criarObjetos();

        Bundle bundle = getIntent().getExtras();
        boolean bloquearIniciarVisita;
        if (bundle == null) {
            Mensagens.clienteNaoEncontrado(ClienteActivity.this, true);
            return;
        }

        bloquearIniciarVisita = bundle.getBoolean(Config.BLOQUEAR_VISITA, false);
        codigoCliente = bundle.getString(Config.CodigoCliente, null);
        diaSemanaRota = bundle.getString(Config.DIA_SEMANA, null);
        semanaRota = bundle.getString(Config.SEMANA, null);
        String latitude = bundle.getString(Config.Latitude, null);
        String longitude = bundle.getString(Config.Longitude, null);

        if (codigoCliente == null) {
            Mensagens.clienteNaoEncontrado(ClienteActivity.this, true);
            return;
        }

        cliente = dbCliente.getById(codigoCliente);
        verificaPendencia = !(dbCliente.obterClientesComPendenciaNaoRespondido().size() == 0);
        temVisitasHoje = dbVisita.temVisitasHoje();

        colaborador = dbColaborador.get();
        if (cliente == null) {
            Mensagens.clienteNaoEncontrado(ClienteActivity.this, true);
            return;
        }

        Notificacoes.cancelarNotificacao(ClienteActivity.this, Integer.parseInt(cliente.getId()));

        atualizarTitulo();

        tvNome.setText(cliente.getRazaoSocial());
        if (StringUtils.isNotEmpty(cliente.getDddTelefone()) && StringUtils.isNotEmpty(cliente.getTelefone())) {
            tvTelefone.setText("(" + cliente.getDddTelefone() + ") " + cliente.getTelefone());
        }
        String enderecoCliente = cliente.getTipoLogradouro() + " " + cliente.getNomeLogradouro() + ", " + cliente.getNumeroLogradouro() + ", Bairro: " + cliente.getBairro();
        enderecoCliente += ", " + cliente.getCidade() + "/" + cliente.getEstado();
        tvEndereco.setText(enderecoCliente);
        tvFantasia.setText(cliente.getNomeFantasia());
        tvTipoDocumento.setText(cliente.personType() == EnumRegisterPersonType.JURIDICAL? "CNPJ": "CPF");
        tvDocumento.setText(StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()));

        criarEventos();

        //Mapa
        localizacao(latitude, longitude);
        if (bloquearIniciarVisita || (colaborador.isVerificaClientePendencia() && verificaPendencia && !temVisitasHoje)) {
            btnIniciarAtendimento.setEnabled(false);
            btnIniciarAtendimento.setBackground(ContextCompat.getDrawable(ClienteActivity.this, R.drawable.botao_neutro));
            btnIniciarAtendimento.setTextColor(
                    ContextCompat.getColor(ClienteActivity.this, R.color.branco)
            );
        }
    }

    @Override
    public void callModalMotivesCancelMigration(List<MotiveMigrationSub> list) {
        final RegisterMotiveMigrationCancelDialog dialog = RegisterMotiveMigrationCancelDialog
                .newInstance(this::saveMigrationCancelMotive, list);
        dialog.show(getSupportFragmentManager(), RegisterMotiveMigrationCancelDialog.class.getName());
    }

    @Override
    public void showResponseMotivesCancelMigration() {
        Toast.makeText(this, R.string.message_success_cancel_migration,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerAreCanceled(RegisterMigrationSub registerMigrationSub) {
        if (registerMigrationSub != null) {
            eventBtnIniciarAtendimento();
        } else {
            if (cliente.isClienteMigracaoSub()) {
                OpenMigrationDialog.newInstance(this::openMigrationActivity).show(getSupportFragmentManager(), null);
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Esse cliente está disponível para migração Adquirência. Você será direcionado para o formulário de migração.");
                dialog.setPositiveButton("MIGRAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        AbrirMigracaoAdquirencia();
                    }
                });

                dialog.setNegativeButton("AGENDAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        eventBtnIniciarAtendimento();
                    }
                });

                dialog.setTitle("Informação");
                dialog.show();
            }
        }
    }

    @Override
    public void onComplete(Cadastro cadastroalterado) {
        try {
            if (cadastroalterado != null && cadastroalterado.getAlterado().equals("S")) {
                new ClienteSyncTask(ClienteActivity.this, 1).execute();
                cliente = dbCliente.getById(codigoCliente);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(ClienteActivity.this, ex.getMessage(), false);
        }
    }

    private void saveMigrationCancelMotive(int motiveId, String observation) {
        RegisterMigrationSub register = new RegisterMigrationSub();
        register.setIdCliente(Integer.parseInt(cliente.getId()));
        register.setIdMotivoRecusa(motiveId);
        register.setObservacaoRecusa(observation);
        register.setIdMcc(cliente.getIdMcc());
        register.setTelefoneCelular(returnCellphoneClient());
        register.setVersaoApp(BuildConfig.VERSION_NAME);
        register.setLatitude(gpsTracker.getLatitude());
        register.setLongitude(gpsTracker.getLongitude());
        register.setPrecisao(gpsTracker.getPrecisao());
        register.setDataCadastro(Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));

        clientePresenter.saveMigrationCancel(register);
    }

    private String returnCellphoneClient() {
        if (cliente.getCelular().length() > 0)
            return StringUtils.returnOnlyNumbers(cliente.getCelular());
        else if (cliente.getCelular2().length() > 0) {
            return StringUtils.returnOnlyNumbers(cliente.getCelular2());
        } else {
            return "";
        }
    }

    private void atualizarTitulo() {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setTitle(cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
    }

    private void configurarToolbar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buscar, menu);

        MenuItem searchItem = menu.findItem(R.id.buscarModal);
        searchItem.setVisible(false);

        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(this));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call_chat_bot) {
            //BlipProvider.callChatBot(this);
            //startActivity(new Intent(this, ChatbotActivity.class));
            Colaborador colaborador = new DBColaborador(this).get();
            String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void criarObjetos() {
        dbColaborador = new DBColaborador(ClienteActivity.this);
        dbCliente = new DBCliente(ClienteActivity.this);
        dbLocalizacaoCliente = new DBLocalizacaoCliente(ClienteActivity.this);
        dbVisita = new DBVisita(ClienteActivity.this);
    }

    private void criarEventos() {
        imgCopy.setOnClickListener(v-> {
          Utilidades.copyText(this, tvDocumento.getText().toString());
        });

        btnIniciarAtendimento.setOnClickListener(v -> {
            if (cliente.isClienteMigracaoSub()) {
                clientePresenter.checkRegisterMigrationAreCanceled(cliente.getId(), "SUB");
                return;
            } else if (cliente.isClienteMigracaoAdq()) {
                clientePresenter.checkRegisterMigrationAreCanceled(cliente.getId(), "ADQ");
                return;
            }

            eventBtnIniciarAtendimento();

        });

        llInfoCliente.setOnClickListener((view) -> {
            if (!Validacoes.validacaoDataAparelho(ClienteActivity.this)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, cliente.getId());
            Utilidades.openNewActivity(ClienteActivity.this, ClienteInfoActivity.class, bundle, false);
        });

        Disposable disposableTv = RxView.clicks(tvVerMais)
                .throttleFirst(1200, TimeUnit.MILLISECONDS)
                .subscribe(v -> {
                    ReenviaSenhaCliente senhaCliente = new ReenviaSenhaCliente();
                    senhaCliente.setIdVendedor(colaborador.getId());
                    if (gpsTracker != null && gpsTracker.getLatitude() != 0
                            && gpsTracker.getLongitude() != 0) {
                        senhaCliente.setPrecisao(gpsTracker.getPrecisao());
                        senhaCliente.setLatitude(gpsTracker.getLatitude());
                        senhaCliente.setLongitude(gpsTracker.getLongitude());
                    }
                    senhaCliente.setIdCliente(Integer.parseInt(cliente.getId()));
                    String jsonSenha = Utilidades.getGsonInstance().toJson(senhaCliente);
                    String json = Utilidades.getGsonInstance().toJson(cliente);

                    ClienteInfoDialog.newInstance(json, jsonSenha)
                            .show(getSupportFragmentManager(), ClienteInfoDialog.class.getSimpleName());
                }, Timber::e);
        disposable.add(disposableTv);
    }

    private void eventBtnIniciarAtendimento() {
        if (bloquearBotao || (colaborador.isVerificaClientePendencia() && verificaPendencia && !temVisitasHoje)) {
            return;
        }

        bloquearBotao = true;
        try {
            colaborador = new DBColaborador(ClienteActivity.this).get();
            cliente = dbCliente.getById(codigoCliente);
            int[] atendimento = Utilidades.retornaDiaSemanaAtendimento(colaborador);
            int dayOfWeek = atendimento[DIA_SEMANA];
            int weekOfMonth = atendimento[SEMANA_COLABORADOR];

            DBRota dbRota = new DBRota(ClienteActivity.this);
            Rota rota = dbRota.getRotasByDiaSemanaCliente(dayOfWeek, cliente.getId(), weekOfMonth);
            boolean existeRota = dbRota.existeRotaDia(dayOfWeek, weekOfMonth);
            Rota rotaSemana = dbRota.getRotaByDiaSemana(dayOfWeek, weekOfMonth);

            if (colaborador.isValidaOrdemRota() && (existeRota && rotaSemana != null)) {
                if (rota == null) {
                    int mensagem = LocalDate.now().getDayOfWeek().getValue() == dayOfWeek
                            ? R.string.nova_rota_mensagem_sem_atendimento
                            : R.string.nova_rota_mensagem_atendimento_fora_ordem;
                    Alerta alerta = new Alerta(
                            ClienteActivity.this,
                            getResources().getString(R.string.app_name),
                            getString(mensagem)
                    );
                    alerta.show();
                    bloquearBotao = false;
                    return;
                }

                String diaSemanaTemp = String.valueOf(rota.getDiaSemana());
                String semanaTemp = String.valueOf(rota.getSemana());
                if (!diaSemanaTemp.equalsIgnoreCase(diaSemanaRota) ||
                        !semanaTemp.equalsIgnoreCase(semanaRota)) {
                    Alerta alerta = new Alerta(
                            ClienteActivity.this,
                            getResources().getString(R.string.app_name),
                            getString(R.string.nova_rota_mensagem_atendimento_fora_ordem)
                    );
                    alerta.show();
                    bloquearBotao = false;
                    return;
                }
                if (!dbRota.validaOrdemAtendimento(dayOfWeek, weekOfMonth, rota.getOrdem(), cliente.getId())) {
                    Alerta alerta = new Alerta(
                            ClienteActivity.this,
                            getResources().getString(R.string.app_name),
                            getString(R.string.nova_rota_mensagem_atendimento_fora_ordem)
                    );
                    alerta.show();
                    bloquearBotao = false;
                    return;
                }
            }

            if (Validacoes.validacoesPreAtendimento(ClienteActivity.this, colaborador, cliente)) {
                //Atualizar Localização do Cliente
                if (cliente.getAtualizaLocal() != null && cliente.getAtualizaLocal().equalsIgnoreCase("S") && !dbLocalizacaoCliente.jaCapturouCoordenadas(cliente.getId())) {
                    fileImagem = Utilidades.setImagem();
                    Utilidades.atualizarLocalizacaoCliente(ClienteActivity.this, fileImagem, true);
                    bloquearBotao = false;
                    return;
                }

                //Atualizar Contato
                if (cliente.getAtualizaContato() != null && cliente.getAtualizaContato().equalsIgnoreCase("S")) {
                    openCadastroCliente();
                    bloquearBotao = false;
                    return;
                }

                iniciarAtendimento(null);
            } else {
                bloquearBotao = false;
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(ClienteActivity.this, ex.getMessage(), false);
            bloquearBotao = false;
        }
    }

    private void openCadastroCliente() {
        try {
            CadastroClienteDialog dialog = new CadastroClienteDialog();
            dialog.myCompleteListenerCadastro = this;
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, cliente.getId());
            args.putBoolean(Config.AtualizaContato, true);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(ClienteActivity.this, ex.getMessage(), false);
        }
    }

    private void iniciarAtendimento(ProjetoTrade pProjetoTrade) {
        try {
            Utilidades.iniciarAtendimento(
                    gpsTracker,
                    ClienteActivity.this,
                    this.latitude,
                    this.longitude,
                    cliente.getId(),
                    pProjetoTrade,
                    Config.RotasValue
            );
        } catch (Exception ex) {
            Mensagens.mensagemErro(ClienteActivity.this, ex.getMessage(), false);
        }
    }

    private void localizacao(String pLatitude, String pLongitude) {
        try {
            if (!Util_IO.isNullOrEmpty(pLatitude) && !Util_IO.isNullOrEmpty(pLongitude)) {
                latitude = Double.parseDouble(pLatitude);
                longitude = Double.parseDouble(pLongitude);
            } else {
                latitude = 0.0;
                longitude = 0.0;
            }
        } catch (NumberFormatException ex) {
            latitude = 0.0;
            longitude = 0.0;
            ex.printStackTrace();
        } catch (Exception ex) {
            latitude = 0.0;
            longitude = 0.0;
            ex.printStackTrace();
        }
        initilizeMap();
    }

    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(this.latitude, this.longitude))
                .title(cliente.getNomeFantasia()));

        if (gpsTracker == null)
            gpsTracker = new GPSTracker(ClienteActivity.this);

        if (colaborador != null && colaborador.getDistancia() > 0) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(this.latitude, this.longitude));
            circleOptions.radius(colaborador.getDistancia());
            circleOptions.strokeColor(Color.parseColor("#000000"));
            circleOptions.strokeWidth(2);
            map.addCircle(circleOptions);
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(this.latitude, this.longitude), 16));
    }

    private void initilizeMap() {
        try {
            SupportMapFragment fragMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cliente_frag_mapa);
            if (fragMapa != null) {
                fragMapa.getMapAsync(this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RequestCode.CaptureImagem && resultCode == RESULT_OK) {
                if (gpsTracker == null)
                    gpsTracker = new GPSTracker(this);

                if (gpsTracker.isMockLocationON) {
                    gpsTracker.showMockAlert();
                    return;
                }

                if (gpsTracker.getPrecisao() != 0) {
                    String imagemComprimida = Utilidades.getFilePath(ClienteActivity.this);
                    File fotoFinal = new Compressor(ClienteActivity.this).setDestinationDirectoryPath(imagemComprimida).compressToFile(fileImagem);
                    imagemComprimida = fotoFinal.getAbsolutePath();
                    Utilidades.capturarLocal(gpsTracker, cliente.getId(), imagemComprimida, ClienteActivity.this);
                    cliente.setAtualizaLocal("N");

                    localizacao(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()));
                    Utilidades.retornaMensagem(ClienteActivity.this, "Localização encontrada!", false);
                } else
                    gpsTracker.showSettingsAlert();
            }
            else if (requestCode == RequestCode.SolicitaMigracao && resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(this, ClientMigrationActivity.class);
                startActivity(intent);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(ClienteActivity.this, ex.getMessage(), false);
        }
    }

    private void openMigrationActivity() {
        Intent intent = new Intent(this, RegisterMigrationActivity.class);
        intent.putExtra(CLIENT_ID_KEY, Integer.parseInt(cliente.getId()));
        intent.putExtra("TIPO_MIGRACAO", "SUB");
        startActivityForResult(intent, RequestCode.SolicitaMigracao); ;
    }

    private void AbrirMigracaoAdquirencia() {
        Intent intent = new Intent(this, RegisterMigrationActivity.class);
        intent.putExtra(CLIENT_ID_KEY, Integer.parseInt(cliente.getId()));
        intent.putExtra("TIPO_MIGRACAO", "ADQ");
        startActivityForResult(intent, RequestCode.SolicitaMigracao); ;
    }
}
