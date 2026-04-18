package com.axys.redeflexmobile.ui.redeflex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.models.CartaoConsultaRequest;
import com.axys.redeflexmobile.shared.models.CartaoConsultaResponse;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.models.QrCodeConsultaRequest;
import com.axys.redeflexmobile.shared.models.QrCodeConsultaResponse;
import com.axys.redeflexmobile.shared.models.QrCodeGerarRequest;
import com.axys.redeflexmobile.shared.models.QrCodeGerarResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorResponse;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.VendaMobile;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.services.SolicitacaoPidService;
import com.axys.redeflexmobile.shared.services.tasks.VendaSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.RetrofitClient;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Util_Imagem;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.dialog.ImageChooserDialog;
import com.axys.redeflexmobile.ui.redeflex.solicitacaopid.Activity_SolPID_DadosCliente;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class FinalizarFormaPagtoActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;
    private long endTime;
    private ProgressDialog progressDialog; // Declaração do ProgressDialog

    public static final int BUTTON_WAIT_DURATION = 1200;
    private Toolbar toolbar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    Venda mVenda;
    ArrayList<ItemVenda> mItemVendas;
    Visita mVisita;
    long lastConsultaTime;

    // Objetos
    TextView txtTitulo, tvValorTotal, tvDataPagto, txtValorTotal, txtDataPagto, tvObservacao, tvQrCode;
    LinearLayout llQrCode, llNSUCartao;
    LinearLayout btnAlterarPagto, btnCompartilhar, btnCopiar, btnContinuar, btnAnexo;
    ImageView imgQrCode;
    CustomEditText txtNSUCartao;
    View viewLoading;
    ProgressBar progressBarVertical;
    TextView tvTimerVertical;

    // Anexo
    AppCompatImageView imgAnexo;
    ImageView imgRemove;
    TextView tvStatusAnexo, tvAdicionar, tvTamanho;
    private ImageChooserDialog dialog;
    private File tempFile;
    private String localArquivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_forma_pagto);

        Bundle bundle = getIntent().getExtras();
        mVenda = (Venda) bundle.getSerializable("Venda");
        mVisita = (Visita) bundle.getSerializable("Visita");
        mItemVendas = new DBVenda(FinalizarFormaPagtoActivity.this).getItensVendabyIdVenda(mVenda.getId());

        criarObjetos();
        criarToolbar();
        criarEventos();

        // Configura Objetos
        configuraObjetos();

        // Carrega Dados
        carregaDados();
    }

    private void carregaDados() {
        txtValorTotal.setText(Util_IO.formatDoubleToDecimalNonDivider(mVenda.getValorTotal()));
        if (mVenda.getIdFormaPagamento() == 6) {
            if (Util_IO.isNullOrEmpty(mVenda.getQrCodeLink())) {
                // Gera o QrCode e Dispara a Consulta
                gerarQRCode();
            } else {
                // Carrega o Qr Code na tela e Dispara a Consulta Pagamento
                carregaQrCode(mVenda.getQrCodeLink());
            }
        } else if (mVenda.getIdFormaPagamento() == 7) {
            if (!Util_IO.isNullOrEmpty(mVenda.getChaveCobranca()))
                txtNSUCartao.setText(mVenda.getChaveCobranca());
        }
    }

    private void carregaQrCode(String pLink) {
        tvQrCode.setText(pLink.toString().trim());
        // Gerar QrCode
        try {
            Bitmap qrCodeBitmap = generateQrCodeBitmap(tvQrCode.getText().toString());
            imgQrCode.setImageBitmap(qrCodeBitmap);

            // Dispara a Consulta de Pagamento
            Date dataExpiracao = mVenda.getDataExpiracaoPix();
            long endTime = dataExpiracao.getTime();
            txtDataPagto.setText(Util_IO.dateTimeToString(dataExpiracao, "dd/MM HH:mm"));

            viewLoading.setVisibility(View.GONE);

            iniciarConsultaPagtoPix(endTime);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private void gerarQRCode() {
        viewLoading.setVisibility(View.VISIBLE);
        RegisterService services;
        Retrofit retrofit = RetrofitClient.getRetrofit();
        services = retrofit.create(RegisterService.class);

        QrCodeGerarRequest request = new QrCodeGerarRequest();
        request.setClienteId(Integer.parseInt(mVenda.getIdCliente()));
        request.setColaboradorId(new DBColaborador(FinalizarFormaPagtoActivity.this).get().getId());
        request.setValor(mVenda.getValorTotal());
        request.setAppMobileId(mVenda.getId());
        request.setVisitaId(mVenda.getIdVisita());
        request.setVersaoApp(BuildConfig.VERSION_NAME);

        Call<QrCodeGerarResponse> call = services.postGerarQrCode(request);
        call.enqueue(new Callback<QrCodeGerarResponse>() {
            @Override
            public void onResponse(Call<QrCodeGerarResponse> call, Response<QrCodeGerarResponse> response) {
                viewLoading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        QrCodeGerarResponse retorno = response.body();

                        // Atualiza DataHora de Expiração conforme Fuso Horário
                        String dataExpiracao = Util_IO.dateTimeToString(retorno.getExpiraEm(), DateUtils.DATE_TIME_FULL_WEB_SERVICE_MOCK);
                        if (dataExpiracao != null) {
                            // Parse da data UTC recebida
                            Instant instantExpiracao = Instant.parse(dataExpiracao);

                            // Converte para o fuso horário local do dispositivo
                            ZonedDateTime dataExpiracaoLocal = instantExpiracao
                                    .atZone(ZoneId.systemDefault());

                            // Formatar a data para exibição
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            String dataExpiracaoFormatada = dataExpiracaoLocal.format(formatter);

                            // Se precisar converter de volta para Date, use:
                            Date dataExpiracaoAjustada = Date.from(dataExpiracaoLocal.toInstant());

                            // Atualiza a Confirmação pagamento no Pedido
                            new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), retorno.getTxtId(), 0, retorno.getQrCodeLink(), dataExpiracaoAjustada, localArquivo);
                            mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());

                            // Carrega o Qr Code na tela e Dispara a Consulta Pagamento
                            carregaQrCode(retorno.getQrCodeLink());
                        }
                    } else {
                        Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, "Atenção!", "Não foi possível gerar o QrCode Pix");
                        alerta.show((dialog, which) -> {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        });
                    }
                } else {
                    Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, "Atenção!", "Não foi possível gerar o QrCode Pix");
                    alerta.show((dialog, which) -> {
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    });
                }
            }

            @Override
            public void onFailure(Call<QrCodeGerarResponse> call, Throwable throwable) {
                viewLoading.setVisibility(View.GONE);
                Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, "Atenção!", "Erro ao gerar QrCode Pix. Verifique!");
                alerta.show((dialog, which) -> {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                });
            }
        });
    }

    private void iniciarConsultaPagtoPix(long endTime) {
        long totalTime = endTime - System.currentTimeMillis(); // Tempo total do temporizador
        progressBarVertical.setMax((int) totalTime); // Define o máximo do ProgressBar como o tempo total

        lastConsultaTime = System.currentTimeMillis();

        if (handler == null) {
            // Verificação extra para garantir que o handler não seja nulo
            handler = new Handler(Looper.getMainLooper());
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long timeRemaining = endTime - currentTime;

                if (timeRemaining > 0) {
                    int progress = (int) (totalTime - timeRemaining); // Calcula o progresso invertido
                    progressBarVertical.setProgress(progress);

                    long secondsRemaining = timeRemaining / 1000;
                    long minutes = secondsRemaining / 60;
                    long seconds = secondsRemaining % 60;

                    // Formata o tempo restante como MM:ss
                    String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                    tvTimerVertical.setText(timeFormatted);

                    if (currentTime - lastConsultaTime >= 15000) {
                        Log.d("MainActivity", "Consultando....");
                        // Uncomment the following line to call the actual method
                        consultarPagtoPIX();
                        lastConsultaTime = currentTime;
                    }

                    handler.postDelayed(this, 1000); // Atualiza a cada segundo
                } else {
                    Log.d("MainActivity", "Tempo final atingido.");
                    new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), null, 0, null, null, localArquivo);
                    mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());

                    Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, FinalizarFormaPagtoActivity.this.getResources().getString(R.string.app_name), "Tempo expirado para pagamento PIX!");
                    alerta.show((dialog, which) -> {
                        try {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        };

        handler.post(runnable); // Inicia a primeira execução
    }

    private void iniciarConsultaCartao() {
        long totalTime = 3 * 60 * 1000; // Tempo total de 3 minutos em milissegundos (180000 ms)

        lastConsultaTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + totalTime;

        // Inicializa a Consula de Cartão
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Consultando Pagamento Cartão");
        progressDialog.setMessage("Tempo restante para a consulta...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax((int) (totalTime / 1000));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (handler != null)
            pararConsultas();
        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long timeRemaining = endTime - currentTime;

                if (timeRemaining > 0) {
                    int progress = (int) ((totalTime - timeRemaining) / 1000);
                    progressDialog.setProgress(progress);

                    long secondsRemaining = timeRemaining / 1000;
                    long minutes = secondsRemaining / 60;
                    long seconds = secondsRemaining % 60;

                    String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                    progressDialog.setMessage("Tempo restante: " + timeFormatted);

                    if (currentTime - lastConsultaTime >= 15000) {
                        Log.d("MainActivity", "Consultando....");

                        // Chama a função de consulta com o callback
                        consultarPagtoCartao(new ConsultaCartaoCallback() {
                            @Override
                            public void onConsultaSucesso() {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                handler.removeCallbacks(runnable);
                            }

                            @Override
                            public void onConsultaFalha() {
                                // Continua a execução do handler caso a consulta falhe
                                handler.postDelayed(runnable, 1000);
                            }
                        });
                        lastConsultaTime = currentTime;
                    } else {
                        handler.postDelayed(this, 1000);
                    }
                } else {
                    Log.d("MainActivity", "Tempo final atingido.");

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), null, 0, null, null, localArquivo);
                    mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());

                    Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, FinalizarFormaPagtoActivity.this.getResources().getString(R.string.app_name), "Tempo consulta pagamento Cartão expirado. Anexe o comprovante para finalizar a Venda!");
                    alerta.show((dialog, which) -> {
                        try {
                            // Abre botão para estar podendo anexar o documento
                            btnAnexo.setVisibility(View.VISIBLE);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        };

        handler.post(runnable);
    }

    // Método para encerrar o agendamento
    private void pararConsultas() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            Log.d("MainActivity", "Handler agendador encerrado.");
        }
    }

    private void criarEventos() {
        btnAlterarPagto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap qrCodeBitmap = generateQrCodeBitmap(tvQrCode.getText().toString());
                    if (qrCodeBitmap != null) {
                        String qrKey = tvQrCode.getText().toString();
                        try {
                            shareQrCodeAndKey(qrCodeBitmap, qrKey);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(FinalizarFormaPagtoActivity.this, "Erro ao compartilhar o QR Code", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FinalizarFormaPagtoActivity.this, "Primeiro gere o QR Code", Toast.LENGTH_SHORT).show();
                    }
                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util_IO.isNullOrEmpty(tvQrCode.getText().toString())) {
                    Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, getApplicationContext().getResources().getString(R.string.app_name), "Não foi possível ler a Chave Pix. Verifique!");
                    alerta.show();
                } else
                    Utilidades.copyText(FinalizarFormaPagtoActivity.this, tvQrCode.getText().toString());
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fecha o teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                // Validar Dados
                if (Util_IO.isNullOrEmpty(txtNSUCartao.getText().toString())) {
                    Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, "Atenção!", "Informe o código REF da transação,\nlocalizado no comprovante.");
                    alerta.show();
                    return;
                }

                Venda vendaMobile = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyNSU(txtNSUCartao.getText().toString());
                if (vendaMobile != null) {
                    Alerta alerta = new Alerta(FinalizarFormaPagtoActivity.this, "Atenção!", "Código REF da transação, já foi\ninformado em outra Venda. Verifique!");
                    alerta.show();
                    return;
                }

                if (Util_IO.isNullOrEmpty(localArquivo))
                    iniciarConsultaCartao();
                else
                {
                    // Atualiza a Confirmação pagamento no Pedido
                    new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), txtNSUCartao.getText().trim(), 1, null, null, localArquivo);
                    mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());

                    // Finaliza Pedido
                    try {
                        Utilidades.enviaPedido(FinalizarFormaPagtoActivity.this, mVenda, mVisita, mItemVendas);

                        // Finaliza atividade com resultado positivo
                        setResult(Activity.RESULT_OK);
                        finish();
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, ex.getMessage(), false);
                    }
                }
            }
        });

        imgAnexo.setOnClickListener(v -> AnexarArquivo());
        imgRemove.setOnClickListener(v -> RemoverAnexo());
    }

    private void RemoverAnexo()
    {
        imgAnexo.setImageResource(R.drawable.attachment_item_add);
        tvTamanho.setText(R.string.customer_register_attachment_item_image_size_limit);
        tvStatusAnexo.setVisibility(View.VISIBLE);
    }

    private void AnexarArquivo() {
        dialog = ImageChooserDialog.newInstance(EnumRegisterAttachmentType.OTHERS,
                v -> takePhoto(),
                v -> selectImageFromGallery());
        dialog.show(getSupportFragmentManager(), ImageChooserDialog.class.getSimpleName());
    }

    private void takePhoto() {
        dialog.dismiss();
        localArquivo = "";
        tempFile = Utilidades.setImagem();
        startCameraIntent();
    }

    private void selectImageFromGallery() {
        dialog.dismiss();
        localArquivo = "";
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
        localArquivo = (finalPhoto.getAbsolutePath());
    }

    private void configuraObjetos() {
        if (mVenda != null) {
            // Configura os campos para o PIX
            if (mVenda.getIdFormaPagamento() == 6) {
                txtTitulo.setText("Peça ao cliente que faça o pagamento usando o\nQrCode abaixo ou envie o link pelo WhatsApp.");
                tvObservacao.setText("Utilize o QRCode ou copie o código Pix abaixo e cole em seu aplicativo de pagamento para concluir a compra.");
                tvValorTotal.setVisibility(View.VISIBLE);
                tvDataPagto.setVisibility(View.VISIBLE);
                txtValorTotal.setVisibility(View.VISIBLE);
                txtDataPagto.setVisibility(View.VISIBLE);
                tvQrCode.setVisibility(View.VISIBLE);
                llQrCode.setVisibility(View.VISIBLE);
                llNSUCartao.setVisibility(View.GONE);
                btnCompartilhar.setVisibility(View.VISIBLE);
                btnCopiar.setVisibility(View.VISIBLE);
                btnContinuar.setVisibility(View.GONE);
                btnAnexo.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
                progressBarVertical.setVisibility(View.VISIBLE);
                tvTimerVertical.setVisibility(View.VISIBLE);
            }
            // Configura os campos para o Cartão Debito / Crédito
            else {
                txtTitulo.setText("Realize a transação pela maquininha, em seguida\ninsira o NSU da transação para seguir com a venda.");
                tvObservacao.setText("Para prosseguir com a sua venda, insira o código REF da\ntransação, localizado no comprovante.");
                tvValorTotal.setVisibility(View.VISIBLE);
                tvDataPagto.setVisibility(View.GONE);
                txtValorTotal.setVisibility(View.VISIBLE);
                txtDataPagto.setVisibility(View.GONE);
                tvQrCode.setVisibility(View.GONE);
                llQrCode.setVisibility(View.GONE);
                llNSUCartao.setVisibility(View.VISIBLE);
                btnCompartilhar.setVisibility(View.GONE);
                btnCopiar.setVisibility(View.GONE);
                btnContinuar.setVisibility(View.VISIBLE);
                btnAnexo.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                progressBarVertical.setVisibility(View.GONE);
                tvTimerVertical.setVisibility(View.GONE);
            }
        }
    }

    private void criarObjetos() {
        txtTitulo = findViewById(R.id.finalizaPagto_txtTitulo);
        tvValorTotal = findViewById(R.id.finalizaPagto_tvValorTotal);
        tvDataPagto = findViewById(R.id.finalizaPagto_tvDataPagto);
        txtValorTotal = findViewById(R.id.finalizaPagto_txtValorTotal);
        txtDataPagto = findViewById(R.id.finalizaPagto_txtDataPagto);
        tvObservacao = findViewById(R.id.finalizaPagto_tvObservacao);
        tvQrCode = findViewById(R.id.finalizaPagto_tvQrCode);

        llQrCode = findViewById(R.id.finalizaPagto_llQrCode);
        llNSUCartao = findViewById(R.id.finalizaPagto_llNSUCartao);
        btnAlterarPagto = findViewById(R.id.finalizaPagto_btnAlterarPagto);
        btnCompartilhar = findViewById(R.id.finalizaPagto_btnCompartilhar);
        btnCopiar = findViewById(R.id.finalizaPagto_btnCopiar);
        btnContinuar = findViewById(R.id.finalizaPagto_btnContinuar);
        btnAnexo = findViewById(R.id.finalizaPagto_btnAnexoComprovante);

        imgQrCode = findViewById(R.id.finalizaPagto_imgQrCode);
        txtNSUCartao = findViewById(R.id.finalizaPagto_txtNSUCartao);

        viewLoading = findViewById(R.id.loading_view);
        progressBarVertical = findViewById(R.id.finalizaPagto_progressBarVertical);
        tvTimerVertical = findViewById(R.id.finalizaPagto_tvTimerVertical);

        // Anexo
        imgAnexo = findViewById(R.id.finalizaPagto_anexo_item_iv_thumbnail);
        imgRemove = findViewById(R.id.finalizaPagto_anexo_item_iv_remove);
        tvStatusAnexo = findViewById(R.id.finalizaPagto_anexo_item_tv_status);
        tvAdicionar = findViewById(R.id.finalizaPagto_anexo_item_tv_add);
        tvTamanho = findViewById(R.id.finalizaPagto_anexo_item_tv_tamanho);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        pararConsultas();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void criarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (mVenda.getIdFormaPagamento() == 6)
            mudarTitulo("Pagamento Pix");
        else
            mudarTitulo("Pagamento Cartão");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    private void mudarTitulo(String texto) {
        setTitle(texto);
        toolbar.setTitle(texto);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(texto);
        }
    }

    private Bitmap generateQrCodeBitmap(String qrKey) throws WriterException {
        int size = 500; // Tamanho do QR Code em pixels
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter().encode(qrKey, BarcodeFormat.QR_CODE, size, size);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    private void shareQrCodeAndKey(Bitmap qrCodeBitmap, String qrKey) throws IOException {
        // Salvar o Bitmap como arquivo
        File file = Utilidades.setImagem();
        try (FileOutputStream stream = new FileOutputStream(file)) {
            qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        Uri contentUri = FileProvider.getUriForFile(this, (this.getApplicationContext().getPackageName() + Util_Imagem.PROVIDER), file);

        // Criar a Intent de compartilhamento
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, qrKey);
        startActivity(Intent.createChooser(shareIntent, "Compartilhar QR Code via"));
    }

    private void consultarPagtoPIX() {
        RegisterService services;
        Retrofit retrofit = RetrofitClient.getRetrofit();
        services = retrofit.create(RegisterService.class);

        QrCodeConsultaRequest request = new QrCodeConsultaRequest();
        request.setColaboradorId(new DBColaborador(FinalizarFormaPagtoActivity.this).get().getId());
        request.setTxtId(mVenda.getChaveCobranca());

        services.getConsultaQrCode(request).enqueue(new Callback<QrCodeConsultaResponse>() {
            @Override
            public void onResponse(Call<QrCodeConsultaResponse> call, Response<QrCodeConsultaResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            QrCodeConsultaResponse qrResponse = response.body();

                            // Atualiza a Confirmação pagamento no Pedido
                            new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), request.getTxtId(), qrResponse.getStatus(), mVenda.getQrCodeLink(), mVenda.getDataExpiracaoPix(), localArquivo);
                            mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());

                            // Finaliza Pedido
                            try {
                                Utilidades.enviaPedido(FinalizarFormaPagtoActivity.this, mVenda, mVisita, mItemVendas);

                                // Carrega Tela de Pagamento Executado com Sucesso
                                setResult(Activity.RESULT_OK);
                                finish();
                            } catch (Exception ex) {
                                pararConsultas();
                                Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, ex.getMessage(), false);
                            }
                        } else if (response.body().getStatus() == 2) {
                            pararConsultas();
                            new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), null, 0, null, null, null);
                            mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());
                            Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, "QrCode Cancelado. Gere um novo QrCode!!!", false);
                        } else if (response.body().getStatus() == 3) {
                            pararConsultas();
                            new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), null, 0, null, null, null);
                            mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());
                            Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, "Tempo expirado para pagamento PIX!", false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QrCodeConsultaResponse> call, Throwable t) {
                Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, "Serviço Consulta Pix indisponível. Aguarde!!!", false);
            }
        });
    }

    public interface ConsultaCartaoCallback {
        void onConsultaSucesso();

        void onConsultaFalha();
    }

    private void consultarPagtoCartao(ConsultaCartaoCallback callback) {
        // Monta Json de Consulta Pagamento Cartão
        CartaoConsultaRequest request = new CartaoConsultaRequest();
        request.setClienteId(Integer.parseInt(mVenda.getIdCliente()));
        request.setColaboradorId(new DBColaborador(FinalizarFormaPagtoActivity.this).get().getId());
        request.setValor(mVenda.getValorTotal());
        request.setAppMobileId(mVenda.getId());
        request.setVisitaId(mVenda.getIdVisita());
        request.setVersaoApp(BuildConfig.VERSION_NAME);
        request.setChaveNSU(txtNSUCartao.getText().toString().trim());

        RegisterService services;
        Retrofit retrofit = RetrofitClient.getRetrofit();
        services = retrofit.create(RegisterService.class);

        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);
        Log.d("Roni", "ConsultaPagto: " + jsonRequest);

        Call<CartaoConsultaResponse> call = services.getConsultaPagtoCartao(request);
        call.enqueue(new Callback<CartaoConsultaResponse>() {
            @Override
            public void onResponse(Call<CartaoConsultaResponse> call, Response<CartaoConsultaResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 1) {
                    CartaoConsultaResponse retorno = response.body();

                    // Atualiza a Confirmação pagamento no Pedido
                    new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), request.getChaveNSU(), retorno.getStatus(), null, null, localArquivo);
                    mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());

                    // Finaliza Pedido
                    try {
                        Utilidades.enviaPedido(FinalizarFormaPagtoActivity.this, mVenda, mVisita, mItemVendas);

                        // Retorna sucesso
                        callback.onConsultaSucesso();

                        // Finaliza atividade com resultado positivo
                        setResult(Activity.RESULT_OK);
                        finish();
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, ex.getMessage(), false);
                    }
                } else {
                    // Retorna falha caso o status não seja 1
                    callback.onConsultaFalha();
                }
            }

            @Override
            public void onFailure(Call<CartaoConsultaResponse> call, Throwable throwable) {
                // Atualiza a Chave Informada no Pedido
                new DBVenda(FinalizarFormaPagtoActivity.this).updateChaveCobranca(mVenda.getId(), request.getChaveNSU(), 0, null, null, localArquivo);
                mVenda = new DBVenda(FinalizarFormaPagtoActivity.this).getVendabyId(mVenda.getId());
                Mensagens.mensagemErro(FinalizarFormaPagtoActivity.this, "Serviço Consulta Cartão indisponível!!!", false);

                // Retorna falha para o callback
                callback.onConsultaFalha();
            }
        });
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
            if (localArquivo.contains("/Files/Compressed"))
                bitmap = BitmapFactory.decodeFile(localArquivo);
            else
                bitmap = Utilidades.decodeBase64(localArquivo);

            imgAnexo.setImageBitmap(bitmap);
            tvTamanho.setText(Formatter.formatShortFileSize(this, filePath.length()));
            tvStatusAnexo.setVisibility(View.GONE);

        } catch (Exception ex) {
            //Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }
}