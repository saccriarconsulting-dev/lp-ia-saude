package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItensFinalizarVendaAdapter;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.models.FormaPagamentoVenc;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.PrecoFormaCache;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.QrCodeConsultaRequest;
import com.axys.redeflexmobile.shared.models.QrCodeConsultaResponse;
import com.axys.redeflexmobile.shared.models.TipoFormaPgto;
import com.axys.redeflexmobile.shared.models.ValorPorFormaPagto;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RetrofitClient;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.dialog.FormaPagamentoDialog;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class FinalizarAtendActivity extends AppCompatActivity implements FormaPagamentoDialog.OnCompleteListener {
    public static final int BUTTON_WAIT_DURATION = 1200;
    TextView txtCliente, txtTotalPedido, txtDataCobranca;
    Button btnFormaPagamento, btnCancelar, btnEnviar;

    Cliente cliente;
    Visita visita;
    Venda venda;
    FormaPagamento formaPagamento;
    DBVenda dbVenda;
    DBFormaPagamento dbFormaPagamento;
    RecyclerView mRecyclerView;
    ArrayList<ItemVendaCombo> listaItens;
    private Toolbar toolbar;

    private Produto prod;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Dialog progressDialog;

    private ItensFinalizarVendaAdapter itensAdapter;
    private TipoFormaPgto formaSelecionada = TipoFormaPgto.AVISTA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_atend);

        criarToolbar();

        Utilidades.getDataServidorESalvaBanco(FinalizarAtendActivity.this);

        try {
            criarObjetos();
            visita = Utilidades.getVisita(FinalizarAtendActivity.this);
            if (visita == null) {
                Mensagens.visitaNaoIniciada(FinalizarAtendActivity.this);
                return;
            }

            cliente = new DBCliente(FinalizarAtendActivity.this).getById(visita.getIdCliente());
            if (cliente == null) {
                Mensagens.clienteNaoEncontradoFinalizarAtend(FinalizarAtendActivity.this, visita);
                return;
            }

            atualizaVenda();
            carregarDados();
            criarEventos();

            if (!Util_IO.isNullOrEmpty(venda.getQrCodeLink()))
                consultarPagtoPIX();

        } catch (Exception ex) {
            Mensagens.mensagemErro(FinalizarAtendActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onComplete(FormaPagamentoVenc retorno) {
        try {
            if (Validacoes.validacoesAtualizaFormaPagamento(FinalizarAtendActivity.this, retorno, venda)) {
                dbVenda.updateFormaPagamentoVenda(venda.getId(), retorno.getFormapgto().getId(), retorno.getDatavencimento());
                atualizaVenda();
                atualizaFormaPagamento();
                TipoFormaPgto novaForma = mapForma(retorno.getFormapgto().getId());
                onFormaPagamentoEscolhida(novaForma);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(FinalizarAtendActivity.this, ex.getMessage(), false);
        }

    }

    private void onFormaPagamentoEscolhida(TipoFormaPgto novaForma) {
        formaSelecionada = novaForma;

        if (itensAdapter == null || mRecyclerView.getAdapter() == null) {
            itensAdapter = new ItensFinalizarVendaAdapter(listaItens, formaSelecionada, this);
            mRecyclerView.setAdapter(itensAdapter);
        } else {
            itensAdapter.setFormaSelecionada(novaForma);
        }

        atualizarTotalTela();
    }

    private TipoFormaPgto mapForma(int idFormaPagto) {
        switch (idFormaPagto) {
            case 2:
                return TipoFormaPgto.APRAZO;

            case 3:
                return TipoFormaPgto.PIX;
            case 4:
                return TipoFormaPgto.CARTAO;

            case 1:
            default:
                return TipoFormaPgto.AVISTA;
        }
    }

    private void criarObjetos() {
        txtCliente = findViewById(R.id.txtCliente);
        txtTotalPedido = findViewById(R.id.txtTotalPedido);
        dbVenda = new DBVenda(FinalizarAtendActivity.this);
        btnFormaPagamento = findViewById(R.id.btnFormaPagamento);
        dbFormaPagamento = new DBFormaPagamento(FinalizarAtendActivity.this);
        btnCancelar = findViewById(R.id.btnCancelar);
        txtDataCobranca = findViewById(R.id.txtDataCobranca);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        btnEnviar = findViewById(R.id.btnEnviar);
    }

    private void openFormaPagamento() {
        try {
            if (!Validacoes.validacaoDataAparelho(FinalizarAtendActivity.this)) {
                return;
            }

            int countVendaAvista = 0;
            for (ItemVenda item : listaItens) {
                prod = new DBEstoque(FinalizarAtendActivity.this).getProdutoById(item.getIdProduto());
                if (prod != null)
                {
                    if (prod.getVendaAvista().equals("S"))
                        countVendaAvista++;
                }
            }

            FormaPagamentoDialog dialog = new FormaPagamentoDialog();
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, visita.getIdCliente());
            bundle.putBoolean("VendeAvista", countVendaAvista == listaItens.size() ? true : false);
            dialog.setArguments(bundle);
            dialog.myCompleteListener = FinalizarAtendActivity.this;
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(FinalizarAtendActivity.this, ex.getMessage(), false);
        }
    }

    private void criarEventos() {
        compositeDisposable.add(
                RxView.clicks(btnFormaPagamento)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> openFormaPagamento(), Timber::e)
        );

        compositeDisposable.add(
                RxView.clicks(btnCancelar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            try {
                                Utilidades.openMotivo(FinalizarAtendActivity.this, visita);
                            } catch (Exception ex) {
                                Mensagens.mensagemErro(FinalizarAtendActivity.this, ex.getMessage(), false);
                            }
                        }, Timber::e)
        );

        compositeDisposable.add(
                RxView.clicks(btnEnviar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            if (!Validacoes.validacaoDataAparelho(FinalizarAtendActivity.this)) {
                                return;
                            }

                            atualizaVenda();
                            if (listaItens == null || listaItens.size() == 0) {
                                btnCancelar.performClick();
                                return;
                            }

                            ArrayList<ItemVenda> listVenda = dbVenda.getItensVendabyIdVenda(venda.getId());

                            if (formaPagamento.getId() == 6 || formaPagamento.getId() == 7) {
                                Intent intent = new Intent(FinalizarAtendActivity.this, FinalizarFormaPagtoActivity.class);
                                intent.putExtra("Venda", venda);
                                intent.putExtra("Visita", visita);
                                startActivityForResult(intent, 1, null);
                            } else {
                                Alerta alerta = new Alerta(FinalizarAtendActivity.this, getResources().getString(R.string.app_name), "Deseja Realmente enviar o pedido?");
                                alerta.showConfirm((dialog, which) -> {
                                    try {
                                        // Gera Pedido de Devolução
                                        if (listVenda.size() > 0 && venda.getIdConsignadoRefer() > 0) {
                                            // Gerar Devolução
                                            GerarDevolucaoTotal(venda, listVenda);
                                        }

                                        // Atualiza os campos no caso de Forma de Pagamento ser Diferente de 6 - Pix e 7 - Cartão
                                        venda.setChaveCobranca(null);
                                        venda.setPago(0);

                                        // Gerar Consignação do restante
                                        Utilidades.enviaPedido(FinalizarAtendActivity.this, venda, visita, listVenda);
                                    } catch (Exception ex) {
                                        Mensagens.mensagemErro(FinalizarAtendActivity.this, ex.getMessage(), false);
                                    }
                                }, null);
                            }
                        }, Timber::e)
        );
    }

    private void atualizaVenda() {
        venda = dbVenda.getVendabyIdVisita(visita.getId());
        listaItens =VendaMemoria.getLista();
        carregarDados();
    }

    private void atualizaFormaPagamento() {
        if (venda.getIdFormaPagamento() == 0) {
            btnFormaPagamento.setText("SELECIONE");
            btnEnviar.setText("Enviar Pedido");
        } else {
            formaPagamento = dbFormaPagamento.getFormaPagamentoById(venda.getIdFormaPagamento());
            btnFormaPagamento.setText(formaPagamento.getDescricao());
            txtDataCobranca.setText(Util_IO.dateToStringBr(venda.getDataCobranca()));
            btnFormaPagamento.setEnabled(true);

            if (formaPagamento.getId() == 6)
                btnEnviar.setText("Gerar QR Code");
            else if (formaPagamento.getId() == 7)
                btnEnviar.setText("Continuar");
            else
                btnEnviar.setText("Enviar Pedido");
        }
    }

    private void carregarDados() {
        try {
            txtCliente.setText(cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
            txtTotalPedido.setText(Util_IO.formatDoubleToDecimalNonDivider(dbVenda.retornaValorTotalVenda(venda.getId())));

            formaSelecionada = resolveFormaSelecionada();
            itensAdapter = new ItensFinalizarVendaAdapter(listaItens, formaSelecionada, FinalizarAtendActivity.this);
            mRecyclerView.setAdapter(itensAdapter);
            atualizaFormaPagamento();
            atualizarTotalTela();
        } catch (Exception ex) {
            String msg = ex.getMessage();

            if (msg == null || !msg.contains("Attempt to invoke")) {
                Mensagens.mensagemErro(FinalizarAtendActivity.this, msg, false);
            }
        }
    }

    private void atualizarTotalTela() {
        double total = 0.0;

        for (ItemVendaCombo item : listaItens) {
            double valorUnFinal = getPrecoPorForma(item, formaSelecionada);
            total += valorUnFinal * item.getQtde();
        }

        txtTotalPedido.setText(Util_IO.formatDoubleToDecimalNonDivider(total));
    }

    private void criarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mudarTitulo("Finalizar Atendimento");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buscar, menu);
        menu.findItem(R.id.buscarModal).setVisible(false);
        menu.findItem(R.id.sync).setVisible(true);
        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(this));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call_chat_bot) {
            Colaborador colaborador = new DBColaborador(this).get();
            String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return false;
        }

        if (item.getItemId() == R.id.sync) {
            Bundle bundle = new Bundle();
            bundle.putInt("mTipoOperacao", 1);
            Utilidades.openNewActivity(FinalizarAtendActivity.this, SyncActivity.class, bundle, false);
            return false;
        }


        return super.onOptionsItemSelected(item);
    }

    private void GerarDevolucaoTotal(Venda venda, ArrayList<ItemVenda> listVenda) {
        try {
            int idConsignacao = 0;
            Consignado consignado = new BDConsignado(FinalizarAtendActivity.this).getByIdConsignadoRefer(String.valueOf(venda.getIdConsignadoRefer()));
            idConsignacao = consignado.getId();
            consignado.setId(0);
            consignado.setTipoConsignado("DEV");
            consignado.setIdVendedor(new DBColaborador(FinalizarAtendActivity.this).get().getId());
            consignado.setIdCliente(Integer.parseInt(venda.getIdCliente()));
            consignado.setIdVisita(venda.getIdVisita());
            consignado.setDataEmissao(venda.getData());
            consignado.setLatitude(visita.getLatitude());
            consignado.setLongitude(visita.getLongitude());
            consignado.setPrecisao(visita.getPrecisao());
            consignado.setVersaoApp(visita.getVersaoApp());
            consignado.setIdConsignadoRefer(venda.getIdConsignadoRefer());
            consignado.setStatus("0");
            consignado.setIdServer(0);
            long newIdConsignado = new BDConsignado(FinalizarAtendActivity.this).addConsignado(consignado);

            // Carrega Itens da Consignação Origem
            List<ConsignadoItem> listaConsignacao = new ArrayList<>();
            listaConsignacao = new BDConsignadoItem(FinalizarAtendActivity.this).getByIdCConsignado(String.valueOf(idConsignacao));
            for (ConsignadoItem consignadoItem : listaConsignacao) {
                // Carrega o Id do Item Anterior
                int idConsignacaoItem = consignadoItem.getId();
                consignadoItem.setId(0);
                consignadoItem.setIdConsignado((int) newIdConsignado);
                consignadoItem.setIdServer(0);
                long newIdConsignadoItem = new BDConsignadoItem(FinalizarAtendActivity.this).addConsignadoItem(consignadoItem);

                for (ConsignadoItemCodBarra consignadoItemCodBarra : consignadoItem.getListaCodigoBarra()) {
                    consignadoItemCodBarra.setId(0);
                    consignadoItemCodBarra.setIdConsignado((int) newIdConsignado);
                    consignadoItemCodBarra.setIdConsignadoItem((int) newIdConsignadoItem);
                    consignadoItemCodBarra.setIdServer(0);
                    new BDConsignadoItemCodBarra(FinalizarAtendActivity.this).addConsignadoItemCodBarra(consignadoItemCodBarra);
                }
            }

            // Baixa o Status da Consignação de Referencia
            if (idConsignacao > 0)
                new BDConsignado(FinalizarAtendActivity.this).atualizaStatusConsignado(String.valueOf(idConsignacao), "3");

        } catch (Exception e) {
            Log.d("Roni", "GerarDevolucaoTotal: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
                Intent intent = new Intent(FinalizarAtendActivity.this, PagamentoConcluidoActivity.class);
                startActivity(intent);
            }
        }
    }

    private void consultarPagtoPIX() {
        // Ativa Progressbar de Tela
        mostrarProgressBar();

        RegisterService services;
        Retrofit retrofit = RetrofitClient.getRetrofit();
        services = retrofit.create(RegisterService.class);

        QrCodeConsultaRequest request = new QrCodeConsultaRequest();
        request.setColaboradorId(new DBColaborador(FinalizarAtendActivity.this).get().getId());
        request.setTxtId(venda.getChaveCobranca());

        services.getConsultaQrCode(request).enqueue(new Callback<QrCodeConsultaResponse>() {
            @Override
            public void onResponse(Call<QrCodeConsultaResponse> call, Response<QrCodeConsultaResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("Roni", "onResponse: " + response.body().getStatus());
                        Log.d("Roni", "onResponse: " + response.body().getMensagem());

                        if (response.body().getStatus() == 1) {
                            QrCodeConsultaResponse qrResponse = response.body();

                            // Atualiza a Confirmação pagamento no Pedido
                            new DBVenda(FinalizarAtendActivity.this).updateChaveCobranca(venda.getId(), request.getTxtId(), qrResponse.getStatus(), venda.getQrCodeLink(), venda.getDataExpiracaoPix(), null);
                            venda = new DBVenda(FinalizarAtendActivity.this).getVendabyId(venda.getId());

                            // Finaliza Pedido
                            try {
                                ArrayList<ItemVenda> listVenda = dbVenda.getItensVendabyIdVenda(venda.getId());
                                Utilidades.enviaPedido(FinalizarAtendActivity.this, venda, visita, listVenda);

                                ocultarProgressBar();

                                // Carrega Tela de Pagamento Executado com Sucesso
                                finish();
                                Intent intent = new Intent(FinalizarAtendActivity.this, PagamentoConcluidoActivity.class);
                                startActivity(intent);
                            } catch (Exception ex) {
                                ocultarProgressBar();
                                Mensagens.mensagemErro(FinalizarAtendActivity.this, ex.getMessage(), false);
                            }
                        } else if (response.body().getStatus() == 2) {
                            new DBVenda(FinalizarAtendActivity.this).updateChaveCobranca(venda.getId(), null, 0, null, null, null);
                            venda = new DBVenda(FinalizarAtendActivity.this).getVendabyId(venda.getId());

                            ocultarProgressBar();
                            Mensagens.mensagemErro(FinalizarAtendActivity.this, "QrCode Cancelado. Gere um novo QrCode!!!", false);
                        } else if (response.body().getStatus() == 3) {
                            new DBVenda(FinalizarAtendActivity.this).updateChaveCobranca(venda.getId(), null, 0, null, null, null);
                            venda = new DBVenda(FinalizarAtendActivity.this).getVendabyId(venda.getId());

                            ocultarProgressBar();
                            Mensagens.mensagemErro(FinalizarAtendActivity.this, "Tempo expirado para pagamento PIX. Gere um novo QrCode!!!", false);
                        } else if (response.body().getStatus() == 0){
                            ocultarProgressBar();

                            Intent intent = new Intent(FinalizarAtendActivity.this, FinalizarFormaPagtoActivity.class);
                            intent.putExtra("Venda", venda);
                            intent.putExtra("Visita", visita);
                            startActivityForResult(intent, 1, null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QrCodeConsultaResponse> call, Throwable t) {
                ocultarProgressBar();
                Mensagens.mensagemErro(FinalizarAtendActivity.this, "Serviço Consulta Pix indisponível. Aguarde!!!", false);
            }
        });
    }

    private void mostrarProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // Não permite fechar manualmente

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        layout.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(this);
        layout.addView(progressBar);

        TextView textView = new TextView(this);
        textView.setText("Aguarde, consultando pagamento...");
        textView.setPadding(10, 20, 10, 0);
        textView.setGravity(Gravity.CENTER);
        layout.addView(textView);

        builder.setView(layout);
        progressDialog = builder.create();
        progressDialog.show();
    }

    private void ocultarProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


   private double getPrecoPorForma(ItemVenda item, TipoFormaPgto forma) {

       String idProduto = item.getIdProduto();
       double precoBase = item.getValorUN();

       if (forma == null) return precoBase;
       ValorPorFormaPagto v = resolveValorPorForma(idProduto);
       if (v == null) return precoBase;

       double acrescimo;
       switch (forma) {
           case APRAZO: acrescimo = v.getAPrazo(); break;
           case PIX:    acrescimo = v.getPix(); break;
           case CARTAO: acrescimo = v.getCartao(); break;
           case AVISTA:
           default:     acrescimo = v.getAVista(); break;
       }

       return (acrescimo > 0.0) ? (precoBase + acrescimo) : precoBase;
   }

    private TipoFormaPgto resolveFormaSelecionada() {
        int id = venda.getIdFormaPagamento();

        if (id == 1) return TipoFormaPgto.AVISTA;
        if (id == 2) return TipoFormaPgto.APRAZO;
        if (id == 3) return TipoFormaPgto.PIX;
        if (id == 4) return TipoFormaPgto.CARTAO;

        return TipoFormaPgto.AVISTA;
    }

    private ValorPorFormaPagto resolveValorPorForma(String idProduto) {
        ValorPorFormaPagto v = PrecoFormaCache.getValorPorForma(idProduto);
        if (v != null) return v;

        try {
            Produto p = new DBEstoque(this).getProdutoById(idProduto);
            if (p != null && p.getValorPorFormaPagto() != null) {
                PrecoFormaCache.put(p);
                return p.getValorPorFormaPagto();
            }
        } catch (Exception ignored) {}

        return null;
    }
}

