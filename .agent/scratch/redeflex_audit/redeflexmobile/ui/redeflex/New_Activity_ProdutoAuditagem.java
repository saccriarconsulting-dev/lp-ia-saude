package com.axys.redeflexmobile.ui.redeflex;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.BarcodeAdapter;
import com.axys.redeflexmobile.shared.bd.BDIccidOperadora;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.IccidOperadora;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class New_Activity_ProdutoAuditagem extends AppCompatActivity {
    public static final int BUTTON_WAIT_DURATION = 2200;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Toolbar toolbar;

    private LinearLayout layout_Bipagem, layout_scanner;
    private EditText txtCodigoBarras;
    private ImageButton buttonAddCodigoBarra, buttonCamera, buttonScanner;
    private Button button_FinalizaScanner;
    private TextView layoutbipagemproduto_txtQuantidade, layoutbipagemproduto_txtQtdLida;
    private ListView listViewCodBarras;

    // Objetos/Componentes Scanner
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private ToneGenerator toneGen1;
    private String barcodeData;
    private boolean isScannerActive = false;

    private Produto mProduto;
    private Cliente mCliente;
    private ProdutoCombo mProdutoCombo;
    private int QtdBipagem;
    private CodBarra linhaCodigoBarra;
    BarcodeAdapter itemsAdapter;
    // Objetos
    ArrayList<CodBarra> itensCodBarra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_new_produto_auditagem);

        // Inicializando abertura
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String idProduto = null;
        String idCliente = null;
        Bundle args = getIntent().getExtras();
        if (args != null) {
            idProduto = args.getString(Config.CodigoProduto);
            idCliente = args.getString(Config.CodigoCliente);
        }

        if (Util_IO.isNullOrEmpty(idProduto)) {
            Mensagens.produtoNaoEncontrado(this);
            return;
        } else {
            mProduto = new DBEstoque(this).getProdutoById(idProduto);
        }

        if (Util_IO.isNullOrEmpty(idCliente)) {
            Mensagens.clienteNaoEncontrado(this, true);
            return;
        } else {
            mCliente = new DBCliente(this).getById(idCliente);
        }

        criarObjetos(mProduto);
        criarEventos();
        atualizarlista();

        createBarcodeDetector();
    }

    private void createBarcodeDetector() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.CODE_128)
                .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    layoutbipagemproduto_txtQtdLida.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).displayValue != null) {
                                layoutbipagemproduto_txtQtdLida.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).displayValue;

                                // Verirfica se o Inicial do Código corresponde ao Produto Selecionado
                                if (!Util_IO.isNullOrEmpty(mProduto.getIniciaCodBarra()) && !barcodeData.startsWith(mProduto.getIniciaCodBarra()))
                                    return;

                                IccidOperadora iccidOperadora = new BDIccidOperadora(New_Activity_ProdutoAuditagem.this).getByInicioBarra(barcodeData.substring(0,6));
                                if (iccidOperadora != null && mProduto.getOperadora() != iccidOperadora.getOperadoraId())
                                    return;

                                // Adiciona o Código de Barras na Lista
                                linhaCodigoBarra = new CodBarra();
                                linhaCodigoBarra.setIdProduto(mProduto.getId());
                                linhaCodigoBarra.setGrupoProduto(mProduto.getGrupo());
                                linhaCodigoBarra.setNomeProduto(mProduto.getNome());
                                linhaCodigoBarra.setIndividual(true);
                                linhaCodigoBarra.setCodBarraInicial(barcodeData);

                                // Valida Qtd digitos do Codigo de Barras registrado na tabela Produto
                                if (mProduto.getQtdCodBarra() > 0 && barcodeData.trim().length() != mProduto.getQtdCodBarra()){
                                    return;
                                }

                                // Valida se o Codigo de Barras já foi auditado
                                if (verificaLinhaDuplicadaScanner()) {
                                    return;
                                }

                                // Valida se o Código de Barras já não foi informado em algum range
                                if (CodigoBarra.verificaICCIDDuplicado(linhaCodigoBarra, itensCodBarra)) {
                                    return;
                                }

                                // Verifica se o codigo de barras não consta individualmente na lista
                                if (!itensCodBarra.contains(linhaCodigoBarra)) {
                                    itensCodBarra.add(linhaCodigoBarra);
                                    toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 150);
                                    vibratePhone();
                                }

                                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                                int result = retornaQtdBipada(itensCodBarra);
                                layoutbipagemproduto_txtQtdLida.setText("Quantidade Lida: " +  numberFormat.format(result));
                                atualizarlista();
                            }
                        }
                    });
                }
            }
        });
    }

    private void abrirCamera() {
        try {
            txtCodigoBarras.setText("");
            CodeReader.openCodeReader(this, IntentIntegrator.CODE_128);
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    private void criarEventos() {
        button_FinalizaScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_Bipagem.setVisibility(View.VISIBLE);
                layout_scanner.setVisibility(View.GONE);
                isScannerActive = false;
                stopCamera();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
            }
        });

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ContextCompat.checkSelfPermission(New_Activity_ProdutoAuditagem.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (isScannerActive) {
                        startCamera();
                    }
                } else {
                    ActivityCompat.requestPermissions(New_Activity_ProdutoAuditagem.this, new String[]{Manifest.permission.CAMERA}, 200);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                stopCamera();
            }
        });

        buttonScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_Bipagem.setVisibility(View.GONE);
                layout_scanner.setVisibility(View.VISIBLE);
                isScannerActive = true;

                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                int result = retornaQtdBipada(itensCodBarra);
                layoutbipagemproduto_txtQtdLida.setText("Quantidade Lida: " +  numberFormat.format(result));

                startCamera();
            }
        });

        buttonAddCodigoBarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInclusaoCodBarra(txtCodigoBarras.getText().toString());
            }
        });

        listViewCodBarras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int posicao = position;
                if (itensCodBarra != null && itensCodBarra.size() > 0) {
                    Alerta alerta = new Alerta(New_Activity_ProdutoAuditagem.this, getResources().getString(R.string.app_name), "Deseja realmente excluir?");
                    alerta.showConfirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itensCodBarra.remove(posicao);
                            atualizarlista();
                        }
                    }, null);
                }
            }
        });
    }

    private void criarObjetos(Produto pProduto) {
        layout_Bipagem = (LinearLayout) findViewById(R.id.layout_Bipagem);
        layout_scanner = (LinearLayout) findViewById(R.id.layout_scanner);
        buttonAddCodigoBarra = (ImageButton) findViewById(R.id.buttonAddCodigo);
        buttonCamera = (ImageButton) findViewById(R.id.buttonCamera);
        buttonScanner = (ImageButton) findViewById(R.id.buttonScanner);
        button_FinalizaScanner = (Button) findViewById(R.id.button_FinalizaScanner);
        layoutbipagemproduto_txtQuantidade = (TextView) findViewById(R.id.layoutbipagemproduto_txtQuantidade);
        layoutbipagemproduto_txtQtdLida = (TextView) findViewById(R.id.layoutbipagemproduto_txtQtdLida);
        txtCodigoBarras = (EditText) findViewById(R.id.txtCodigoBarras);

        toneGen1 = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();

        try {
            QtdBipagem = 0;
            mProdutoCombo = new ProdutoCombo();
            if (pProduto.getQtdCombo() > 0)
                (findViewById(R.id.combo)).setVisibility(View.VISIBLE);

            listViewCodBarras = findViewById(R.id.layoutbipagemproduto_listView);
            itensCodBarra = new ArrayList<>();
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(pProduto.getId() + " - " + pProduto.getNome());
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    protected void onResume() {
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solic_merc, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (layout_scanner.getVisibility() == View.VISIBLE) {
            layout_Bipagem.setVisibility(View.VISIBLE);
            layout_scanner.setVisibility(View.GONE);
            isScannerActive = false;
            stopCamera();
        } else {
            Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), "Os dados não foram salvos. Deseja continuar sem salvar?");
            alerta.showConfirm((dialog, which) -> {
                finish();
            }, null);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_salvar:
                salvar();
                break;
            case R.id.action_limpar:
                itensCodBarra = new ArrayList<>();
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                int result = retornaQtdBipada(itensCodBarra);
                layoutbipagemproduto_txtQtdLida.setText("Quantidade Lida: " +  numberFormat.format(result));
                atualizarlista();
                break;
            case R.id.action_addproduto:
                abrirCamera();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void atualizarlista() {
        if (mProduto.getQtdCombo() > 0) {
            ProdutoCombo mProdutoCombo = CodigoBarra.retornaCombo(mProduto.getQtdCombo(), itensCodBarra, UsoCodBarra.AUDITAGEM_CLIENTE);
            ((TextView) findViewById(R.id.txtQtdBipado)).setText(String.valueOf(mProdutoCombo.getQtdTotal()));
            if (mProdutoCombo.getQtdFalta() == 0)
                (findViewById(R.id.falta)).setVisibility(View.GONE);
            else
                (findViewById(R.id.falta)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txtQtdFaltaBipar)).setText(String.valueOf(mProdutoCombo.getQtdFalta()));
        }
        itemsAdapter = new BarcodeAdapter(this, R.layout.item_codigobarra_range, itensCodBarra, UsoCodBarra.AUDITAGEM_CLIENTE);
        listViewCodBarras.setAdapter(itemsAdapter);
        txtCodigoBarras.setText("");
    }

    private int retornaQtdBipada(ArrayList<CodBarra> itensCodBarra) {
        int qtd = 0;
        if (itensCodBarra.size() <= 0)
            qtd = 0;
        else {
            for (CodBarra codBarra:itensCodBarra) {
                qtd += Integer.valueOf(codBarra.retornaQuantidade(UsoCodBarra.GERAL));
            }
        }
        return qtd;
    }

    private void stopCamera() {
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    private void startCamera() {
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(this, new
                        String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            Log.e("Roni", "Erro ao iniciar a câmera: " + e.getMessage());
        }
    }

    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 200, 100};
            vibrator.vibrate(pattern, -1);
        }
    }

    private void salvar() {
        try {
            int quantidadebipada;
            if (mProduto.getQtdCombo() > 0)
                quantidadebipada = mProdutoCombo.getQtdTotal();
            else
                quantidadebipada = CodigoBarra.quantidadeBipada(itensCodBarra, UsoCodBarra.AUDITAGEM_CLIENTE);

            if (mProduto.getQtdCombo() > 0 && mProdutoCombo.getQtdFalta() > 0) {
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name),
                        "Está faltando escanear " + mProdutoCombo.getQtdFalta() + " " + ((mProdutoCombo.getQtdFalta() == 1) ? "código" : "codigos") + " de barra, Verifique!");
                alerta.show();
                return;
            }

            if (quantidadebipada <= 0) {
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), "Nenhum produto foi selecionado, Verifique!");
                alerta.show();
                return;
            }

            AuditagemCliente auditagem = new AuditagemCliente();
            auditagem.setIdProduto(mProduto.getId());
            auditagem.setQuantidade(quantidadebipada);
            auditagem.setIdCliente(mCliente.getId());
            auditagem.setListaCodigos(itensCodBarra);
            new DBEstoque(this).addAuditagemCliente(auditagem);

            setResult(RESULT_OK);
            finish();
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    private void addInclusaoCodBarra(String pCodBarra) {
        if (Util_IO.isNullOrEmpty(pCodBarra)) {
            Utilidades.retornaMensagem(this, "Informe o código", false);
            return;
        }

        Alerta alerta = new Alerta(this, pCodBarra, "Deseja incluir esse código de barra?");
        alerta.showConfirm((dialog, which) -> {
            try {
                if (mProduto != null) {
                    // Veerifica se o Codigo de Barras Lido pertence a operadora do Produto
                    IccidOperadora iccidOperadora = new BDIccidOperadora(New_Activity_ProdutoAuditagem.this).getByInicioBarra(pCodBarra.substring(0,6));
                    if (iccidOperadora !=null &&  mProduto.getOperadora() != iccidOperadora.getOperadoraId()){
                        Alerta alerta1 = new Alerta(this, pCodBarra, "Código de Barras não pertence a operadora do Produto");
                        alerta1.show((dialog1, which1) -> {
                            QtdBipagem = 0;
                        });
                        return;
                    }

                    if (!Util_IO.isNullOrEmpty(mProduto.getIniciaCodBarra()) && !pCodBarra.startsWith(mProduto.getIniciaCodBarra())) {
                        Alerta alerta1 = new Alerta(this, pCodBarra, "Código de Barra deve começar com: " + mProduto.getIniciaCodBarra());
                        alerta1.show((dialog1, which1) -> {
                            QtdBipagem = 0;
                            abrirCamera();
                        });
                        return;
                    }

                    if (mProduto.getQtdCodBarra() > 0 && pCodBarra.trim().length() != mProduto.getQtdCodBarra()) {
                        new Alerta(this, pCodBarra, "Código de Barra deve conter " + mProduto.getQtdCodBarra() + " caracteres")
                                .show((dialog1, which1) -> {
                                    QtdBipagem = 0;
                                    abrirCamera();
                                });
                        return;
                    }
                }

                if (QtdBipagem == 1) {
                    SetCodBarra(pCodBarra, false);
                    if (verificaLinhaDuplicada())
                        return;

                    if (!CodigoBarra.validaQuantidadeRange(linhaCodigoBarra)) {
                        new Alerta(this, "Erro", "Favor conferir o ICCID inicial e final")
                                .show((dialog1, which1) -> {
                                    QtdBipagem = 0;
                                    abrirCamera();
                                });
                        return;
                    }

                    itensCodBarra.add(linhaCodigoBarra);
                    QtdBipagem = 0;
                    atualizarlista();
                    abrirCamera();
                    return;
                }

                Alerta alerta1 = new Alerta(this, pCodBarra, "Código de barra unitário?");
                alerta1.showConfirm((dialog2, which2) -> {
                    SetCodBarra(pCodBarra, true);
                    if (verificaLinhaDuplicada())
                        return;

                    itensCodBarra.add(linhaCodigoBarra);
                    atualizarlista();
                    abrirCamera();
                }, (dialog2, which2) -> {
                    SetCodBarra(pCodBarra, false);
                    abrirCamera();
                });
            } catch (Exception ex) {
                Mensagens.mensagemErro(this, ex.getMessage(), false);
            }
        }, (dialog, which) -> {
            abrirCamera();
        });
    }

    private void SetCodBarra(String pCodBarra, Boolean pIsIndividual) {
        CodBarra codAdd = new CodBarra();
        codAdd.setIndividual(pIsIndividual);
        codAdd.setIdProduto(mProduto.getId());
        codAdd.setGrupoProduto(mProduto.getGrupo());
        codAdd.setNomeProduto(mProduto.getNome());

        if (pIsIndividual)
            codAdd.setCodBarraInicial(pCodBarra);
        else if (QtdBipagem == 0) {
            codAdd.setCodBarraInicial(pCodBarra);
            QtdBipagem = 1;
        } else {
            linhaCodigoBarra.setCodBarraFinal(pCodBarra);
            return;
        }

        linhaCodigoBarra = codAdd;
    }

    private Boolean verificaLinhaDuplicada() {
        ArrayList<CodBarra> itensCodValidacao = new ArrayList<>();
        itensCodValidacao.addAll(itensCodBarra);

        ArrayList<AuditagemCliente> auditagemRealizadas = new DBEstoque(this).getAuditagensCliente(mCliente.getId());
        for (AuditagemCliente auditagemCliente : auditagemRealizadas) {
            itensCodValidacao.addAll(auditagemCliente.getListaCodigos());
        }

        if (CodigoBarra.verificaICCIDDuplicado(linhaCodigoBarra, itensCodValidacao)
                || (!linhaCodigoBarra.getIndividual() && QtdBipagem == 1
                && Long.parseLong(linhaCodigoBarra.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE)) < 2)) {
            Alerta alerta = new Alerta(this, "Informação", "Item já adicionado");
            alerta.show((dialog, which) -> {
                linhaCodigoBarra = null;
                QtdBipagem = 0;
                abrirCamera();
            });
            return true;
        }

        if (!linhaCodigoBarra.getIndividual()) {
            Long codigoBarraInicialSemDigito = Long.parseLong(CodigoBarra.retornaICCIDSemDigito(linhaCodigoBarra.getCodBarraInicial()));
            Long digitoCodigoBarraInicial = Long.valueOf(CodigoBarra.retornaDigitoVerificadorICCID(linhaCodigoBarra.getCodBarraInicial()));

            Long codigoBarraFinalSemDigito = Long.parseLong(CodigoBarra.retornaICCIDSemDigito(linhaCodigoBarra.getCodBarraFinal()));
            Long digitoCodigoBarraFinal = Long.valueOf(CodigoBarra.retornaDigitoVerificadorICCID(linhaCodigoBarra.getCodBarraFinal()));

            if (codigoBarraInicialSemDigito >= codigoBarraFinalSemDigito && digitoCodigoBarraInicial >= digitoCodigoBarraFinal) {
                Alerta alerta = new Alerta(this, "Informação", "Item não é sequencial");
                alerta.show((dialog, which) -> {
                    linhaCodigoBarra = null;
                    QtdBipagem = 0;
                    abrirCamera();
                });
                return true;
            }
        }

        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                runOnUiThread(() -> getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE));
                new Handler().postDelayed(() -> {
                    runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
                    addInclusaoCodBarra(result.getContents());
                }, 1000);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    private Boolean verificaLinhaDuplicadaScanner() {
        ArrayList<CodBarra> itensCodValidacao = new ArrayList<>();
        itensCodValidacao.addAll(itensCodBarra);

        ArrayList<AuditagemCliente> auditagemRealizadas = new DBEstoque(this).getAuditagensCliente(mCliente.getId());
        for (AuditagemCliente auditagemCliente : auditagemRealizadas) {
            itensCodValidacao.addAll(auditagemCliente.getListaCodigos());
        }

        if (CodigoBarra.verificaICCIDDuplicado(linhaCodigoBarra, itensCodValidacao)
                || (!linhaCodigoBarra.getIndividual() && QtdBipagem == 1
                && Long.parseLong(linhaCodigoBarra.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE)) < 2)) {
            return true;
        }

        if (!linhaCodigoBarra.getIndividual()) {
            Long codigoBarraInicialSemDigito = Long.parseLong(CodigoBarra.retornaICCIDSemDigito(linhaCodigoBarra.getCodBarraInicial()));
            Long digitoCodigoBarraInicial = Long.valueOf(CodigoBarra.retornaDigitoVerificadorICCID(linhaCodigoBarra.getCodBarraInicial()));

            Long codigoBarraFinalSemDigito = Long.parseLong(CodigoBarra.retornaICCIDSemDigito(linhaCodigoBarra.getCodBarraFinal()));
            Long digitoCodigoBarraFinal = Long.valueOf(CodigoBarra.retornaDigitoVerificadorICCID(linhaCodigoBarra.getCodBarraFinal()));

            if (codigoBarraInicialSemDigito >= codigoBarraFinalSemDigito && digitoCodigoBarraInicial >= digitoCodigoBarraFinal) {
                return true;
            }
        }

        return false;
    }
}