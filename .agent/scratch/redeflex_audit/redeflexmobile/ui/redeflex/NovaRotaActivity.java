package com.axys.redeflexmobile.ui.redeflex;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

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
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.dialog.CadastroClienteDialog;
import com.axys.redeflexmobile.ui.dialog.PesquisaClienteDialog;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class NovaRotaActivity extends AppCompatActivity implements OnMapReadyCallback
        , PesquisaClienteDialog.OnCompleteListener
        , CadastroClienteDialog.OnCompleteListener {

    public static final int DIA_SEMANA = 0;
    public static final int SEMANA_COLABORADOR = 1;
    private static final int DELAY_FOR_ENABLE_BUTTON = 1800;
    private boolean existeRota;
    private SupportMapFragment mapa;
    private GPSTracker gpsTracker;
    private Button btnIniciar;
    private File fileImagem;
    private Rota rota;
    private Cliente cliente;
    private Colaborador colaborador;
    private DBCliente dbCliente;
    private DBLocalizacaoCliente dbLocalizacaoCliente;
    private DBColaborador dbColaborador;
    private DBRota dbRota;
    private LinearLayout btnInfoCliente;
    private int dayOfWeek;
    private int weekOfMonth;
    private CompositeDisposable disposables = new CompositeDisposable();
    private int origem;

    ImageView imgCopy;
    TextView tvDocumento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_rota);

        try {
            iniciarToolbar();
            mudarTituloToolbar(R.string.nova_rota_titulo_inicial);

            Visita visita = new DBVisita(NovaRotaActivity.this).getVisitaAtiva();
            if (visita != null) {
                Utilidades.openAtendimento(NovaRotaActivity.this, true, visita);
                return;
            }

            criarObjetos();

            try {
                iniciarDadosColaborador();
            } catch (Exception e) {
                timber.log.Timber.w(e, "Falha ao carregar dados do colaborador (tratado como recuperável).");
                Mensagens.mensagemErro(NovaRotaActivity.this, "Erro ao carregar o colaborador.", false);
                if (btnIniciar != null) btnIniciar.setEnabled(false);
                if (btnInfoCliente != null) btnInfoCliente.setEnabled(false);
            }

            iniciarDadosRota();
            criarEventos();
            iniciarMapa();

        } catch (Exception ex) {
            // Erro não recuperável (falhas estruturais de inicialização)
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), /* fatal = */ true);
        }
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        disposables.dispose();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.buscarModal:
                abrirPesquisaCliente();
                break;
            case R.id.call_chat_bot:
                //BlipProvider.callChatBot(this);
                //startActivity(new Intent(this, ChatbotActivity.class));
                Colaborador colaborador = new DBColaborador(this).get();
                String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buscar, menu);
        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(this));
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RequestCode.CaptureImagem && resultCode == RESULT_OK) {
                if (gpsTracker == null)
                    gpsTracker = new GPSTracker(NovaRotaActivity.this);

                if (gpsTracker.isMockLocationON) {
                    gpsTracker.showMockAlert();
                    return;
                }

                if (gpsTracker.getPrecisao() != 0) {
                    String imageCompress = Utilidades.getFilePath(NovaRotaActivity.this);
                    File fotoFinal = new Compressor(NovaRotaActivity.this).setDestinationDirectoryPath(imageCompress).compressToFile(fileImagem);
                    imageCompress = fotoFinal.getAbsolutePath();
                    Utilidades.capturarLocal(gpsTracker, cliente.getId(), imageCompress, NovaRotaActivity.this);
                    cliente.setAtualizaLocal("N");

                    if (rota != null) {
                        rota.setLatitude(gpsTracker.getLatitude());
                        rota.setLongitude(gpsTracker.getLongitude());
                    }

                    Utilidades.retornaMensagem(NovaRotaActivity.this, "Localização encontrada!", false);
                } else
                    gpsTracker.showSettingsAlert();
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onCompletePesquisaCliente(Rota rota) {
        try {
            this.rota = rota;
            mudarTituloToolbar(R.string.nova_rota_titulo);
            atualizarRota();
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    public void onComplete(Cadastro cadastroAlterado) {
        if (cadastroAlterado == null || !cadastroAlterado.getAlterado().equalsIgnoreCase(Cadastro.CADASTRO_ALTERADO)) {
            return;
        }

        try {
            new ClienteSyncTask(NovaRotaActivity.this, ClienteSyncTask.CARGA_PARCIAL).execute();
            cliente = dbCliente.getById(rota.getIdCliente());
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onMapReady(GoogleMap pMap) {
        pMap.clear();
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(this);
        }

        LatLng origem = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        trocarPosicoesDosBotoes();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean fineGranted =
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean coarseGranted =
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (fineGranted && coarseGranted) {
                try {
                    pMap.setMyLocationEnabled(true);
                } catch (SecurityException se) {
                    timber.log.Timber.w(se, "setMyLocationEnabled falhou (>=M). Seguindo sem MyLocation.");
                }
            } else {
                String nome = (colaborador != null && colaborador.getNome() != null && !colaborador.getNome().isEmpty())
                        ? colaborador.getNome() : getString(R.string.app_name);

                pMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                        .position(origem)
                        .title(nome)
                        .snippet(nome)
                        .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory
                                .defaultMarker(com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE))
                        .draggable(true));
            }
        } else {
            try {
                pMap.setMyLocationEnabled(true);
            } catch (SecurityException se) {
                timber.log.Timber.w(se, "setMyLocationEnabled falhou (<M). Seguindo sem MyLocation.");
            }
        }

        pMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(origem));
        pMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.zoomTo(16));

        if (rota != null && rota.getLatitude() != 0 && rota.getLongitude() != 0) {
            LatLng destino = new LatLng(rota.getLatitude(), rota.getLongitude());
            pMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                    .position(destino)
                    .snippet(rota.getCliente())
                    .title(rota.getCliente()));

            if (colaborador != null && colaborador.getDistancia() > 0) {
                com.google.android.gms.maps.model.CircleOptions circleOptions =
                        new com.google.android.gms.maps.model.CircleOptions()
                                .center(destino)
                                .radius(colaborador.getDistancia())
                                .strokeColor(android.graphics.Color.parseColor("#000000"))
                                .strokeWidth(2);
                pMap.addCircle(circleOptions);
            }

            if (origem.latitude == destino.latitude && origem.longitude == destino.longitude) {
                pMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(destino, 16));
            } else {
                new com.axys.redeflexmobile.shared.services.tasks.TracaRotaTask(this, pMap)
                        .execute(String.valueOf(origem.latitude),
                                String.valueOf(origem.longitude),
                                String.valueOf(destino.latitude),
                                String.valueOf(destino.longitude));
            }
        }
    }

    private void criarObjetos() {
        imgCopy = findViewById(R.id.cliente_img_copy);
        tvDocumento = findViewById(R.id.txtDocumentoCliente);
        btnIniciar = findViewById(R.id.btnAcao);
        btnInfoCliente = findViewById(R.id.btnInfoCliente);
        dbRota = new DBRota(NovaRotaActivity.this);
        dbCliente = new DBCliente(NovaRotaActivity.this);
        dbColaborador = new DBColaborador(NovaRotaActivity.this);
        dbLocalizacaoCliente = new DBLocalizacaoCliente(NovaRotaActivity.this);
    }

    private void criarEventos() {
        imgCopy.setOnClickListener(v-> {
            Utilidades.copyText(this,tvDocumento.getText().toString());
        });

        Disposable iniciarDisposable = RxView.clicks(btnIniciar)
                .throttleFirst(DELAY_FOR_ENABLE_BUTTON, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    try {
                        if (rota == null) {
                            String mensagem = existeRota
                                    ? getString(R.string.nova_rota_mensagem_sem_atendimento)
                                    : getString(R.string.nova_rota_mensagem_sem_cliente);
                            Alerta alerta = new Alerta(
                                    NovaRotaActivity.this,
                                    getResources().getString(R.string.app_name),
                                    mensagem
                            );
                            alerta.show();
                            return;
                        }

                        colaborador = dbColaborador.get();
                        cliente = dbCliente.getById(rota.getIdCliente());
                        if (Validacoes.validacoesPreAtendimento(NovaRotaActivity.this, colaborador, cliente)) {
                            if (colaborador.isValidaOrdemRota()) {
                                if (!dbRota.validaOrdemAtendimento(dayOfWeek, weekOfMonth, rota.getOrdem(), cliente.getId())) {
                                    Alerta alerta = new Alerta(
                                            NovaRotaActivity.this,
                                            getResources().getString(R.string.app_name),
                                            getString(R.string.nova_rota_mensagem_atendimento_fora_ordem)
                                    );
                                    alerta.show();
                                    return;
                                }
                            }

                            //Atualizar Localização do Cliente
                            if (cliente.getAtualizaLocal() != null && cliente.getAtualizaLocal().equalsIgnoreCase(Cliente.DEVE_ATUALIZAR_LOCAL) && !dbLocalizacaoCliente.jaCapturouCoordenadas(cliente.getId())) {
                                fileImagem = Utilidades.setImagem();
                                Utilidades.atualizarLocalizacaoCliente(NovaRotaActivity.this, fileImagem, true);
                                return;
                            }

                            //Atualizar Contato
                            if (cliente.getAtualizaContato() != null && cliente.getAtualizaContato().equalsIgnoreCase(Cliente.DEVE_ATUALIZAR_CONTATO)) {
                                abrirCadastroCliente();
                                return;
                            }

                            iniciarAtendimento();
                        }
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), false);
                    }
                }, Timber::e);
        disposables.add(iniciarDisposable);

        Disposable infoDisposable = RxView.clicks(btnInfoCliente)
                .throttleFirst(DELAY_FOR_ENABLE_BUTTON, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!Validacoes.validacaoDataAparelho(NovaRotaActivity.this)) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Config.CodigoCliente, cliente.getId());
                    Utilidades.openNewActivity(NovaRotaActivity.this, ClienteInfoActivity.class, bundle, false);
                }, Timber::e);
        disposables.add(infoDisposable);
    }

    private void atualizarRota() {
        try {
            if (rota == null) {
                mudarVisibilidadeComponente(View.GONE);
                mudarTituloToolbar(R.string.nova_rota_titulo);
            } else {
                mudarVisibilidadeComponente(View.VISIBLE);
                mudarTextoTextView(R.id.txtCliente, rota.getCliente());
                mudarTextoTextView(R.id.txtEnderecoCliente, rota.getEndereco());
                mudarTextoTextView(R.id.txtTipoCliente, getString(R.string.nova_rota_tv_tipo_cliente, rota.getExibirCodigo()));

                cliente = dbCliente.getById(rota.getIdCliente());
                if (cliente != null) {
                    mudarTextoTextView(R.id.txtDocumentoCliente, cliente.personType() == EnumRegisterPersonType.JURIDICAL ?
                            "CNPJ: " + StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()) : "CPF: " + StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()));
                    mudarTextoTextView(R.id.txtTelefone, cliente.retornaNumeroTelefone());
                }
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), true);
        }
    }

    private void mudarTextoTextView(@IdRes int textView, String texto) {
        ((TextView) findViewById(textView)).setText(texto);
    }

    private void mudarVisibilidadeComponente(int visibilidade) {
        findViewById(R.id.layoutCliente).setVisibility(visibilidade);
    }

    private void iniciarMapa() {
        try {
            mapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapa == null) {
                return;
            }
            mapa.getMapAsync(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void trocarPosicoesDosBotoes() {
        if (mapa == null) return;
        final View mapView = mapa.getView();
        if (mapView == null) return;

        try {
            // Parent interno do Map
            @android.annotation.SuppressLint("ResourceType")
            final View id1 = mapView.findViewById(1);
            if (id1 == null || id1.getParent() == null) {
                Timber.w("Map UI parent not found; skipping reposition.");
                return;
            }
            final View mapViewParent = (View) id1.getParent();

            final View startVisitButton = findViewById(R.id.btnAcao);
            if (startVisitButton == null) {
                Timber.w("Start visit button not found; skipping reposition.");
                return;
            }
            final ViewGroup.LayoutParams svLp = startVisitButton.getLayoutParams();
            if (!(svLp instanceof CoordinatorLayout.LayoutParams)) {
                Timber.w("Unexpected LP for start button: %s", (svLp != null ? svLp.getClass().getSimpleName() : "null"));
                return;
            }
            final CoordinatorLayout.LayoutParams startVisitButtonLayoutParams =
                    (CoordinatorLayout.LayoutParams) svLp;

            int measuredH = startVisitButton.getMeasuredHeight();
            if (measuredH == 0) {
                startVisitButton.measure(
                        View.MeasureSpec.makeMeasureSpec(startVisitButton.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                measuredH = startVisitButton.getMeasuredHeight();
            }

            final int startVisitMarginRightEnd = startVisitButtonLayoutParams.getMarginEnd();
            final int startVisitButtonInverseY = measuredH + (2 * startVisitButtonLayoutParams.bottomMargin);

            final View myLocationButton = mapViewParent.findViewWithTag("GoogleMapMyLocationButton");
            if (myLocationButton != null) {
                final ViewGroup.LayoutParams myLocLp = myLocationButton.getLayoutParams();
                if (myLocLp instanceof RelativeLayout.LayoutParams) {
                    final RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) myLocLp;
                    p.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    p.setMargins(0, 0, startVisitMarginRightEnd, startVisitButtonInverseY);
                    p.setMarginEnd(startVisitMarginRightEnd);
                    myLocationButton.setLayoutParams(p);
                } else {
                    Timber.w("Unexpected LP for MyLocationButton: %s",
                            (myLocLp != null ? myLocLp.getClass().getSimpleName() : "null"));
                }
            } else {
                Timber.w("MyLocationButton not found; skipping reposition.");
            }

            final View toolbar = mapViewParent.findViewWithTag("GoogleMapToolbar");
            if (toolbar != null) {
                final ViewGroup.LayoutParams tbLp = toolbar.getLayoutParams();
                if (tbLp instanceof RelativeLayout.LayoutParams) {
                    final RelativeLayout.LayoutParams tp = (RelativeLayout.LayoutParams) tbLp;
                    tp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    tp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    tp.setMargins(0, 0, 178, Math.max(0, startVisitButtonInverseY - 2));
                    tp.setMarginEnd(178);
                    toolbar.setLayoutParams(tp);
                } else {
                    Timber.w("Unexpected LP for Map toolbar: %s",
                            (tbLp != null ? tbLp.getClass().getSimpleName() : "null"));
                }
            } else {
                Timber.w("Map toolbar not found; skipping reposition.");
            }
        } catch (Exception e) {
            Timber.e(e, "Failed to reposition Google Map UI; continuing without it.");
            // Não propagar; manter Activity viva e UX intacta.
        }
    }

    private void abrirPesquisaCliente() {
        try {
            PesquisaClienteDialog dialog = new PesquisaClienteDialog();
            dialog.myCompleteListenerRota = this;
            dialog.isValidaOrdemRota = colaborador.isValidaOrdemRota();
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), false);
        }
    }

    private void iniciarAtendimento() {
        try {
            if (gpsTracker.areThereMockPermissionApps()) {
                Log.d("Roni", "iniciarAtendimento: não tem permissão");
                gpsTracker.showMockAlert();
                return;
            }
            else
            {
                Log.d("Roni", "iniciarAtendimento: tem permissão");
            }

            Utilidades.iniciarAtendimento(
                    gpsTracker,
                    NovaRotaActivity.this,
                    rota.getLatitude(),
                    rota.getLongitude(),
                    cliente.getId(),
                    null,
                    origem
            );
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), false);
        }
    }

    private void abrirCadastroCliente() {
        try {
            CadastroClienteDialog dialog = new CadastroClienteDialog();
            dialog.myCompleteListenerCadastro = this;
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, cliente.getId());
            args.putBoolean(Config.AtualizaContato, true);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaRotaActivity.this, ex.getMessage(), false);
        }
    }

    private void iniciarToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void mudarTituloToolbar(@StringRes int titulo) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(titulo);
    }

    private void iniciarDadosColaborador() throws Exception {
        colaborador = dbColaborador.get();

        int[] atendimento = Utilidades.retornaDiaSemanaAtendimento(colaborador);
        dayOfWeek = atendimento[DIA_SEMANA];
        weekOfMonth = atendimento[SEMANA_COLABORADOR];
    }

    private void iniciarDadosRota() {
        String codigoCliente = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            codigoCliente = bundle.getString(Config.CodigoCliente);
            origem = bundle.getInt(Config.NovosCredenciadosFlag);
        }

        if (codigoCliente != null) {
            rota = dbRota.getRotaByIdCliente(codigoCliente);
            mudarTituloToolbar(R.string.nova_rota_titulo);
        } else
            rota = dbRota.getRotaByDiaSemana(dayOfWeek, weekOfMonth);

        if (rota == null) {
            btnInfoCliente.setVisibility(View.GONE);
        }

        existeRota = dbRota.existeRotaDia(dayOfWeek, weekOfMonth);
        atualizarRota();
    }
}