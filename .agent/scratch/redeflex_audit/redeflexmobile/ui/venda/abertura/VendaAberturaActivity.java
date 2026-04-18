package com.axys.redeflexmobile.ui.venda.abertura;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteCliente;
import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.bd.DBAuditagemCliente;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBMerchandising;
import com.axys.redeflexmobile.shared.bd.DBSenhaMasters;
import com.axys.redeflexmobile.shared.bd.DBPermissao;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ClienteDaoImpl;
import com.axys.redeflexmobile.shared.dao.RotaDao;
import com.axys.redeflexmobile.shared.dao.RotaDaoImpl;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.dao.VendaDaoImpl;
import com.axys.redeflexmobile.shared.dao.VisitaDao;
import com.axys.redeflexmobile.shared.dao.VisitaDaoImpl;
import com.axys.redeflexmobile.shared.enums.EnumAtualizarCliente;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteCliente;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SenhaMasters;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.models.venda.VerificaBotoesVenda;
import com.axys.redeflexmobile.shared.services.BlipProvider;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.cliente.atualizar.AtualizarClienteActivity;
import com.axys.redeflexmobile.ui.clientpendent.ClientePendenteActivity;
import com.axys.redeflexmobile.ui.dialog.ConfirmarConsignacaoDialog;
import com.axys.redeflexmobile.ui.dialog.SugestaoVendaDialog;
import com.axys.redeflexmobile.ui.dialog.TokenDialog;
import com.axys.redeflexmobile.ui.redeflex.ChatbotActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ConsignadoActivity;
import com.axys.redeflexmobile.ui.redeflex.ConsultarSolicitacoesPrecoDifActivity;
import com.axys.redeflexmobile.ui.redeflex.FinalizarAtendActivity;
import com.axys.redeflexmobile.ui.redeflex.New_Activity_ProdutoAuditagem;
import com.axys.redeflexmobile.ui.redeflex.SolicitacaoPrecoDifActivity;
import com.axys.redeflexmobile.ui.redeflex.MainActivity;
import com.axys.redeflexmobile.ui.redeflex.OSActivity;
import com.axys.redeflexmobile.ui.redeflex.SenhaMasterActivity;
import com.axys.redeflexmobile.ui.redeflex.VendaActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosList;
import com.axys.redeflexmobile.ui.venda.auditagem.AuditagemPdvActivity;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class VendaAberturaActivity extends BaseActivity {

    public static final float BOTAO_HABILITADO = 1.0f;
    private static final int BUTTON_DELAY_TAP = 1800;
    private static final String CURVA = "CURVA";
    private static final String SIM = "S";
    private static final float BOTAO_DESABILITADO = (float) 0.6;

    @BindView(R.id.venda_tv_cliente)
    TextView tvCliente;
    @BindView(R.id.venda_tv_ultima_visita)
    TextView tvUltimaVisita;
    @BindView(R.id.venda_tv_curva_recarga)
    TextView tvCurvaRecarga;
    @BindView(R.id.venda_tv_curva_chip)
    TextView tvCurvaChip;
    @BindView(R.id.venda_tv_proxima_visita)
    TextView tvProximaVisita;
    @BindView(R.id.venda_tempo_atendimento)
    TextView tvTempoAtendimento;
    @BindView(R.id.venda_ll_cliente_pex)
    LinearLayout llClientePex;
    @BindView(R.id.venda_bt_financeiro)
    Button btFinanceiro;
    @BindView(R.id.venda_bt_abastecimento)
    Button btAbastecimento;
    @BindView(R.id.venda_bt_atualizacao_cadastro)
    Button btAtualizacaoCadastro;
    @BindView(R.id.venda_bt_auditagem_pdv)
    Button btAuditagemPdv;
    @BindView(R.id.venda_bt_pendencias)
    Button btPendencias;
    @BindView(R.id.venda_bt_merchandising)
    Button btMerchandising;
    @BindView(R.id.venda_bt_pedido_venda)
    Button btPedidoVenda;
    @BindView(R.id.venda_bt_finalizar_visita)
    Button btFinalizarVisita;
    @BindView(R.id.venda_tv_cancelar_visita)
    TextView tvCancelarVisita;
    @BindView(R.id.venda_bt_ruptura)
    Button btRuptura;

    @BindView(R.id.venda_tv_statusconsignacao)
    TextView venda_tv_statusconsignacao;
    @BindView(R.id.venda_tv_consignacao)
    TextView venda_tv_consignacao;
    @BindView(R.id.venda_tv_clickconsignacao)
    TextView venda_tv_ClickConsignacao;
    @BindView(R.id.venda_tv_cpfcnpj)
    TextView venda_tv_cpfcnpj;
    @BindView(R.id.venda_tv_senhamaster)
    TextView venda_tv_senhamaster;
    @BindView(R.id.venda_tv_clickVendas)
    TextView venda_tv_clickVendas;

    private ClienteDao clienteDao;
    private VisitaDao visitaDao;
    private RotaDao rotaDao;
    private VendaDao vendaDao;
    private DBChamados dbChamados;
    private DBSugestaoVenda dbSugestaoVenda;

    private BDConsignadoLimiteCliente bdConsignadoLimiteCliente;
    private BDConsignadoLimiteProduto bdConsignadoLimiteProduto;
    private BDConsignado bdConsignado;

    @Nullable
    private Cliente cliente;
    private Visita visita;

    private CountDownTimer timer;
    private String codigoCliente;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_venda_abertura;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getStringByResId(R.string.venda_titulo_toolbar));
        }

        try {
            clienteDao = new ClienteDaoImpl(this);
            visitaDao = new VisitaDaoImpl(this);
            rotaDao = new RotaDaoImpl(this);
            vendaDao = new VendaDaoImpl(this);
            dbChamados = new DBChamados(this);
            dbSugestaoVenda = new DBSugestaoVenda(this);
            bdConsignadoLimiteCliente = new BDConsignadoLimiteCliente(this);
            bdConsignadoLimiteProduto = new BDConsignadoLimiteProduto(this);
            bdConsignado = new BDConsignado(this);

            iniciarTimer();
            configuracaoInicial();
            preencherDados();

            // Validação de Chave Pix aguardando pagamento
            // Caso afirmativo -- Vendedor será direcionado diretamente para o fechamento da venda
            Venda pDadosVenda = vendaDao.getVendaByIdVisita(visita.getId());
            if (pDadosVenda != null && !Util_IO.isNullOrEmpty(pDadosVenda.getQrCodeLink())) {
                btFinalizarVisita.post(() -> btFinalizarVisita.performClick());
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    private void prepararLayoutRecadastro() {
        iniciarEventos();
        if (cliente == null) {
            desabilitarBotoesVisita();
            desabilitarBotao(btAtualizacaoCadastro);
            return;
        }

        AtualizaCliente atualizaCliente = new DBAtualizarCliente(this).obterPorId(cliente.getId());
        if (cliente.isRecadastro() && cliente.getPossuiRecadastro().equalsIgnoreCase("N") && Util_IO.isNullOrEmpty(cliente.getRetornoRecadastro()) && (atualizaCliente == null || atualizaCliente.getStatus() != EnumAtualizarCliente.ANDAMENTO.getValue())) {
            desabilitarBotoesVisita();
            exibirMensagem(getResources().getString(R.string.app_name), getStringByResId(R.string.atualizar_cliente_recadastro_mensagem));
            return;
        }

        if (!cliente.isRecadastro() && cliente.getPossuiRecadastro().equalsIgnoreCase("S") && Util_IO.isNullOrEmpty(cliente.getRetornoRecadastro())) {
            desabilitarBotao(btAtualizacaoCadastro);
            return;
        }

        if (cliente.isRecadastro() && cliente.getPossuiRecadastro().equalsIgnoreCase("N") && !Util_IO.isNullOrEmpty(cliente.getRetornoRecadastro()) && (atualizaCliente == null || atualizaCliente.getStatus() != EnumAtualizarCliente.ANDAMENTO.getValue())) {
            desabilitarBotoesVisita();
            exibirMensagem(getStringByResId(R.string.atualizar_cliente_recadastro_titulo_reprovado), cliente.getRetornoRecadastro());
            return;
        }

        if (cliente.isRecadastro() && cliente.getPossuiRecadastro().equalsIgnoreCase("S") && Util_IO.isNullOrEmpty(cliente.getRetornoRecadastro())) {
            desabilitarBotao(btAtualizacaoCadastro);
        }
    }

    private void desabilitarBotoesVisita() {
        desabilitarBotao(btAuditagemPdv);
        desabilitarBotao(btMerchandising);
        desabilitarBotao(btPedidoVenda);
        desabilitarBotao(btFinalizarVisita);
        desabilitarBotao(btPendencias);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_venda, menu);
        menu.findItem(R.id.buscarModal).setVisible(false);
        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(this));

        // Verifica se o acesso ao Cadastro de Solicitação Preço Diferenciado está liberada
        if (new DBPermissao(this).isPermissaoLiberada("nav_solicitacao_preco_dif")) {
            menu.findItem(R.id.menu_mnuConsultarPrecoDif).setVisible(true);
            menu.findItem(R.id.menu_mnuSolicitarPrecoDif).setVisible(true);
        } else {
            menu.findItem(R.id.menu_mnuConsultarPrecoDif).setVisible(false);
            menu.findItem(R.id.menu_mnuSolicitarPrecoDif).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            try {
                if (!Validacoes.validacaoDataAparelho(this)) {
                    return false;
                }

                Consignado consignado = bdConsignado.getByIdClienteConsigAtivo(visita.getIdCliente());
                if (consignado != null) {
                    if (cliente.isAuditagemConsignadaObriga()) {
                        Alerta alerta = new Alerta(this, this.getResources().getString(R.string.app_name), "O cliente possui itens consignados.\nVerifique!");
                        alerta.show();
                        return false;
                    } else {
                        Alerta alerta = new Alerta(this, this.getResources().getString(R.string.app_name), "Cliente possui estoque em consignação.\nDeseja realmente encerrar a visita?");
                        alerta.showConfirm(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
                                botoesVenda.setJaFezAuditagem(new DBAuditagemCliente(VendaAberturaActivity.this).temAuditagemCliente(cliente.getId()));
                                finalizarVisita(botoesVenda);

                            }
                        }, null);
                    }
                } else
                    Utilidades.openMotivo(this, visita);

            } catch (Exception ex) {
                Mensagens.mensagemErro(this, ex.getMessage(), false);
            }
        }

        if (item.getItemId() == R.id.call_chat_bot) {
            Colaborador colaborador = new DBColaborador(this).get();
            String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        if (item.getItemId() == R.id.menu_mnuSolicitarPrecoDif) {
            Intent intent = new Intent(this, SolicitacaoPrecoDifActivity.class);
            intent.putExtra("Cliente", cliente);
            startActivityForResult(intent, 0);
            return false;
        }

        if (item.getItemId() == R.id.menu_mnuConsultarPrecoDif) {
            Intent intent = new Intent(this, ConsultarSolicitacoesPrecoDifActivity.class);
            startActivityForResult(intent, 0);
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        configuracaoTela();
        verificarPendencias();
        if (timer != null) {
            timer.start();
        }
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();

        if (timer != null) {
            timer.cancel();
        }

        compositeDisposable.clear();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    private void iniciarEventos() {
        Disposable disposableCancelar = RxView.clicks(tvCancelarVisita)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    try {
                        if (!Validacoes.validacaoDataAparelho(this)) {
                            return;
                        }

                        Consignado consignado = bdConsignado.getByIdClienteConsigAtivo(visita.getIdCliente());
                        if (consignado != null) {
                            if (cliente.isAuditagemConsignadaObriga()) {
                                Alerta alerta = new Alerta(this, this.getResources().getString(R.string.app_name), "O cliente possui itens consignados.\nVerifique!");
                                alerta.show();
                                return;
                            } else {
                                Alerta alerta = new Alerta(this, this.getResources().getString(R.string.app_name), "Cliente possui estoque em consignação.\nDeseja realmente encerrar a visita?");
                                alerta.showConfirm(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
                                        botoesVenda.setJaFezAuditagem(new DBAuditagemCliente(VendaAberturaActivity.this).temAuditagemCliente(cliente.getId()));
                                        finalizarVisita(botoesVenda);

                                    }
                                }, null);
                            }
                        } else {
                            if (!temClienteValido()) {
                                return;
                            }
                            Utilidades.openMotivo(this, visita);
                        }
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(this, ex.getMessage(), false);
                    }
                }, Timber::e);

        Disposable disposableAbastecimento = RxView.clicks(btAbastecimento)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Config.ExibirOperadoras, true);
                    bundle.putString(Config.ClienteCodigoExibicao, cliente.retornaCodigoExibicao());
                    bundle.putString(Config.ClienteNomeFantasia, cliente.getNomeFantasia());
                    bundle.putString(Config.ClienteID, cliente.getId());
                    bundle.putString(Config.CodigoCliente, cliente.getId());
                    bundle.putBoolean(ClienteInfoPosList.INICIO_PELA_VENDA, true);
                    Utilidades.openNewActivity(this, ClienteInfoActivity.class, bundle, false);
                }, Timber::e);
        Disposable disposableFinanceiro = RxView.clicks(btFinanceiro)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Config.ExibirFinanceiro, true);
                    bundle.putString(Config.ClienteCodigoExibicao, cliente.retornaCodigoExibicao());
                    bundle.putString(Config.ClienteNomeFantasia, cliente.getNomeFantasia());
                    bundle.putString(Config.ClienteCodigoSGV, cliente.getCodigoSGV());
                    bundle.putString(Config.CodigoCliente, cliente.getId());
                    bundle.putBoolean(ClienteInfoPosList.INICIO_PELA_VENDA, true);
                    Utilidades.openNewActivity(this, ClienteInfoActivity.class, bundle, false);
                }, Timber::e);
        Disposable disposableAtualizar = RxView.clicks(btAtualizacaoCadastro)
                .filter(value -> btAtualizacaoCadastro.getAlpha() == BOTAO_HABILITADO)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }

                    Context context = VendaAberturaActivity.this;
                    startActivity(AtualizarClienteActivity.iniciarActivity(
                            context,
                            cliente.getId()
                    ));
                }, Timber::e);

        Disposable disposableFinalizar = RxView.clicks(btFinalizarVisita)
                .filter(value -> btFinalizarVisita.getAlpha() == BOTAO_HABILITADO)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }

                    Consignado consignado = bdConsignado.getByIdClienteConsigAtivo(visita.getIdCliente());
                    if (consignado != null) {
                        if (cliente.isAuditagemConsignadaObriga()) {
                            Alerta alerta = new Alerta(this, this.getResources().getString(R.string.app_name), "O cliente possui itens consignados.\nVerifique!");
                            alerta.show();
                            return;
                        } else {
                            Alerta alerta = new Alerta(this, this.getResources().getString(R.string.app_name), "Cliente possui estoque em consignação.\nDeseja realmente encerrar a visita?");
                            alerta.showConfirm(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
                                    botoesVenda.setJaFezAuditagem(new DBAuditagemCliente(VendaAberturaActivity.this).temAuditagemCliente(cliente.getId()));
                                    finalizarVisita(botoesVenda);

                                }
                            }, null);
                        }
                    } else {
                        VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
                        botoesVenda.setJaFezAuditagem(new DBAuditagemCliente(this).temAuditagemCliente(cliente.getId()));
                        finalizarVisita(botoesVenda);
                    }
                }, Timber::e);

        Disposable disposableAuditagem = RxView.clicks(btAuditagemPdv)
                .filter(value -> btAuditagemPdv.getAlpha() == BOTAO_HABILITADO)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }
                    abrirAuditagem();
                }, Timber::e);

        Disposable disposablePendencias = RxView.clicks(btPendencias)
                .filter(value -> btPendencias.getAlpha() == BOTAO_HABILITADO)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }
                    abrirPendencias();
                }, Timber::e);

        Disposable disposablePedido = RxView.clicks(btPedidoVenda)
                .filter(value -> btPedidoVenda.getAlpha() == BOTAO_HABILITADO)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }
                    VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
                    botoesVenda.setJaFezAuditagem(new DBAuditagemCliente(this).temAuditagemCliente(cliente.getId()));
                    abrirPedidoVenda(botoesVenda);
                }, Timber::e);

        Disposable disposableRuptura = RxView.clicks(btRuptura)
                .filter(value -> btRuptura.getAlpha() == BOTAO_HABILITADO)
                .throttleFirst(BUTTON_DELAY_TAP, TimeUnit.MILLISECONDS)
                .subscribe(value -> {
                    if (!temClienteValido()) {
                        return;
                    }
                    abrirRupturas();
                }, Timber::e);

        compositeDisposable.add(disposableCancelar);
        compositeDisposable.add(disposableAbastecimento);
        compositeDisposable.add(disposableFinanceiro);
        compositeDisposable.add(disposableAtualizar);
        compositeDisposable.add(disposableFinalizar);
        compositeDisposable.add(disposableAuditagem);
        compositeDisposable.add(disposablePedido);
        compositeDisposable.add(disposablePendencias);
        compositeDisposable.add(disposableRuptura);

        venda_tv_ClickConsignacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChamaTelaConsignado();
            }
        });

        venda_tv_clickVendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chama a Tela de Vendas Passando o Filtro Cliente
                Intent intent = new Intent(VendaAberturaActivity.this, VendaActivity.class);
                intent.putExtra("IdCliente", cliente.getId());
                startActivity(intent);
            }
        });
    }

    private void iniciarTimer() {
        timer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTempoAtendimento.setText(Utilidades.retornaTempoAtendimento(visita.getDataInicio()));
            }

            public void onFinish() {
            }
        };
    }

    private void getCodClient() {
        codigoCliente = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            codigoCliente = bundle.getString(Config.CodigoCliente);
    }

    private void configuracaoInicial() {
        try {
            getCodClient();

            visita = Utilidades.getVisita(this);
            if (visita == null) {
                Mensagens.visitaNaoIniciada(this);
                return;
            }

            if (codigoCliente == null) {
                Mensagens.clienteNaoEncontrado(this, true);
                return;
            }

            obterCliente();
            if (cliente == null) {
                Mensagens.clienteNaoEncontrado(this, true);
            }

            Consignado consignado = new Consignado();
            consignado = bdConsignado.getByIdCLiente(cliente.getId(), 0);
            if (consignado == null) {
                ConsignadoLimiteCliente consignadoLimiteCliente = bdConsignadoLimiteCliente.getByIdCLiente(cliente.getId());
                if (cliente.isVendaConsignada() && consignadoLimiteCliente != null) {
                    ConfirmarConsignacaoDialog.newInstance(
                                    R.string.consignacao_titulo, "Cliente possui chips liberados para consignação",
                                    R.string.consignar,
                                    R.string.naoconsignar
                            )
                            .setPositiveListener(() -> ChamaTelaConsignado())
                            .show(getSupportFragmentManager(), ConfirmarConsignacaoDialog.class.getSimpleName());
                }
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    private void ChamaTelaConsignado() {
        VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
        Boolean ClienteJaFezAuditagem = new DBAuditagemCliente(this).temAuditagemCliente(cliente.getId());

        if (botoesVenda.isAuditagemObrigatoria() && !ClienteJaFezAuditagem) {
            Mensagens.mensagemErro(this, "A auditagem é orbigatória antes de realizar o pedido consignado.", false);
            return;
        }

        // Chama a Tela de Consignação
        Intent intent = new Intent(VendaAberturaActivity.this, ConsignadoActivity.class);
        intent.putExtra("IdVisita", visita.getId());
        intent.putExtra("IdCliente", cliente.getId());
        startActivityForResult(intent, 1);
    }

    private void obterCliente() {
        cliente = clienteDao.obterPorId(codigoCliente);
    }

    private void configuracaoTela() {
        llClientePex.setVisibility(View.GONE);
        obterCliente();
        regraBotoes();
        prepararLayoutRecadastro();
    }

    private void regraBotoes() {
        // Valida Dados do Cliente
        if (!temClienteValido()) {
            return;
        }

        // Verifica se Cliente possui Ruptura
        if (!verificaRuptura())
            btRuptura.setVisibility(View.GONE);
        else
            btRuptura.setVisibility(View.VISIBLE);

        if (clienteDao.clientePossuiPendencias(cliente.getId())) {
            btPendencias.setVisibility(View.VISIBLE);
            btPedidoVenda.setAlpha(BOTAO_DESABILITADO);
            btFinalizarVisita.setAlpha(BOTAO_DESABILITADO);
            btAuditagemPdv.setAlpha(BOTAO_DESABILITADO);
            btAtualizacaoCadastro.setAlpha(BOTAO_DESABILITADO);

            Alerta alerta = new Alerta(this,
                    getResources().getString(R.string.app_name),
                    getResources().getString(R.string.mensagem_alerta_pendencias));

            alerta.showPendencias((dialog, which) -> {
                        abrirPendencias();
                    },
                    null);
        } else {
            btPendencias.setVisibility(View.GONE);
            btPedidoVenda.setAlpha(BOTAO_HABILITADO);
            btFinalizarVisita.setAlpha(BOTAO_HABILITADO);
            btAtualizacaoCadastro.setAlpha(BOTAO_HABILITADO);

            VerificaBotoesVenda botoesVenda = verificaBotoesVenda(cliente);
            botoesVenda.setJaFezAuditagem(new DBAuditagemCliente(this).temAuditagemCliente(cliente.getId()));
            botoesVenda.setJaFezMerchandising(new DBMerchandising(this).temMerchadising(String.valueOf(visita.getId())));
            //if (botoesVenda.jaFezAuditagem()) {
            //    btAuditagemPdv.setAlpha(BOTAO_DESABILITADO);
            //} else {
            btAuditagemPdv.setAlpha(BOTAO_HABILITADO);
            //}

            if (new BDConsignado(this).auditagemConsignacaoAberta(Integer.parseInt(cliente.getId()))) {
                btPedidoVenda.setAlpha(BOTAO_DESABILITADO);
            }

            //}
        }

        //TODO: Descomentar bloco de código quando voltar o Merchandising
//        if (botoesVenda.jaFezMerchandising()) {
//            btMerchandising.setAlpha(BOTAO_DESABILITADO);
//            btMerchandising.setOnClickListener(null);
//        } else {
//            btMerchandising.setOnClickListener(v -> abrirMerchandising());
//        }

        //TODO: Remover para usar as funcionalidades de Merchandising e Pedido venda
        desabilitarBotao(btMerchandising);
    }

    private void finalizarVisita(VerificaBotoesVenda botoesVenda) {
        Venda venda = vendaDao.getVendaByIdVisita(visita.getId());
        List<ItemVenda> itens = vendaDao.getItensVendaByIdVenda(venda == null ? 0 : venda.getId());
        if (itens.isEmpty()) {
            vendaDao.deleteVendaByIdVisita(visita.getId());
        }

        List<ItemVenda> tempItem = Stream.ofNullable(itens)
                .filter(value -> value.getBipagem().equalsIgnoreCase(ItemVenda.LER_CODIGO_BARRA) && !value.isCombo())
                .filter(value -> {
                    ItemVendaCombo itemVendaCombo = (ItemVendaCombo) value;
                    boolean isSimpleItem = itemVendaCombo.getQtdCombo() == 0 && value.getQtde() != value.getQuantidadeSerial();
                    boolean isSimpleCombo = itemVendaCombo.getQtdCombo() > 0 && value.getQuantidadeSerial() != (value.getQtde() * itemVendaCombo.getQtdCombo());
                    return isSimpleItem || isSimpleCombo;
                })
                .toList();

        List<ItemVenda> tempCombo = Stream.ofNullable(itens)
                .filter(value -> value.getBipagem().equalsIgnoreCase(ItemVenda.LER_CODIGO_BARRA) && value.isCombo())
                .filter(value -> {
                    HashMap<String, Boolean> contadorTemp = new HashMap<>();
                    Stream.of(value.getCodigosList())
                            .groupBy(CodBarra::getIdProduto)
                            .forEach(t -> {
                                final int[] contador = {0};
                                Stream.of(t.getValue()).forEach(cod -> contador[0] += cod.somarQuantidade(UsoCodBarra.GERAL));

                                contadorTemp.put(t.getKey(), contador[0] == t.getValue().get(0).getQuantidadeTemporaria());
                            });

                    boolean valido = true;
                    for (boolean b : contadorTemp.values()) {
                        if (!b) {
                            valido = false;
                            break;
                        }
                    }

                    return valido;
                })
                .toList();

        if (!tempItem.isEmpty() || !tempCombo.isEmpty()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.venda_abertura_finalizar_visita_itens_incompleto), false);
            return;
        }
        if (botoesVenda.isAuditagemObrigatoria() && !botoesVenda.jaFezAuditagem()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.venda_auditagem_obrigatoria_finalizar_visita), false);
            return;
        }

        if (botoesVenda.isMerchandisingObrigatorio() && !botoesVenda.jaFezMerchandising()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.venda_merchandising_obrigatorio), false);
            return;
        }

        if (vendaDao.getVendaByIdVisita(visita.getId()) == null) {
            try {
                Utilidades.openMotivo(this, visita);
            } catch (Exception e) {
                Timber.e(e);
            }

            return;
        }

        // Verifica se existem produtos com Ruptura/Pre-Ruptura
        if (!produtoRuptura(venda, itens)) {
            Alerta alerta = new Alerta(this, "Pré-Ruptura ou Ruptura", "Você ainda tem itens em Pré-Ruptura ou Ruptura, deseja continuar a finalização da venda?");
            alerta.showConfirm(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Config.CodigoVisita, visita.getId());
                    bundle.putInt("TIPO_VENDA", visita.getId());
                    Utilidades.openNewActivity(VendaAberturaActivity.this, FinalizarAtendActivity.class, bundle, true);
                }
            }, null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVisita, visita.getId());
            Utilidades.openNewActivity(this, FinalizarAtendActivity.class, bundle, true);
        }
    }

    private void abrirPedidoVenda(VerificaBotoesVenda botoesVenda) {
        if (botoesVenda.isAuditagemObrigatoria() && !botoesVenda.jaFezAuditagem()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.venda_auditagem_obrigatoria_pedido_venda), false);
            return;
        }

        startActivity(PedidoVendaActivity.iniciar(this, visita.getId()));
    }

    //TODO: Descomentar bloco de código quando voltar o Merchandising
//    private void abrirMerchandising() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(Config.CodigoVisita, visita.getId());
//        bundle.putString(Config.CodigoCliente, cliente.getId());
//        Utilidades.openNewActivity(this, MerchandisingActivity.class, bundle, false);
//    }

    private void abrirAuditagem() {
        Bundle bundle = new Bundle();
        bundle.putString(Config.CodigoCliente, cliente.getId());
        Utilidades.openNewActivityForResult(this, AuditagemPdvActivity.class, 9, bundle);
        //Utilidades.openNewActivity(this, AuditagemPdvActivity.class, bundle, false);
    }

    private void abrirPendencias() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.ATIVAR_DECREMENTAL_FUNCAO, true);
        bundle.putString(Config.FILTRO_PENDENCIA_CLIENTE_ID, cliente.getId());
        Utilidades.openNewActivity(this, ClientePendenteActivity.class, bundle, false);
    }

    private void preencherDados() {
        try {
            tvCliente.setText(getStringByResId(R.string.venda_tv_info_cliente, cliente.retornaCodigoExibicao(), cliente.getNomeFantasia()));

            visita = visitaDao.retornaUltimaVisitaByClienteId(cliente.getId());

            venda_tv_cpfcnpj.setText("CPF/CNPJ: " + StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()));

            tvUltimaVisita.setText(
                    visita == null
                            ? getStringByResId(R.string.venda_tv_ultima_visita, "")
                            : getStringByResId(R.string.venda_tv_ultima_visita, Utilidades.getDateName(visita.getDataInicio()))
            );

            tvCurvaRecarga.setVisibility(View.GONE);
            if (!Util_IO.isNullOrEmpty(cliente.getCurvaRecarga())) {
                tvCurvaRecarga.setText(getStringByResId(R.string.venda_tv_curva_recarga, cliente.getCurvaRecarga().toUpperCase().replace(CURVA, "")));
                tvCurvaRecarga.setVisibility(View.VISIBLE);
            }

            tvCurvaChip.setVisibility(View.GONE);
            if (!Util_IO.isNullOrEmpty(cliente.getCurvaChip())) {
                String curvaChip = cliente.getCurvaChip().toUpperCase().replace(CURVA, "");
                tvCurvaChip.setText(getStringByResId(R.string.venda_tv_curva_chip, curvaChip));
                tvCurvaChip.setVisibility(View.VISIBLE);
            }

            tvProximaVisita.setText(getStringByResId(R.string.venda_tv_proxima_visita, rotaDao.getProximaVisita(cliente.getId())));
            venda_tv_statusconsignacao.setText("Status consignado: ");

            SenhaMasters senhaMasters = new DBSenhaMasters(VendaAberturaActivity.this).getSenhaMasters();
            if (senhaMasters == null)
                venda_tv_senhamaster.setText("Senha Master: ");
            else
                venda_tv_senhamaster.setText("Senha Master: " + senhaMasters.getSenha());

            if (cliente.isVendaConsignada()) {
                Consignado pConsignado = new Consignado();
                pConsignado = bdConsignado.getByIdCLiente(cliente.getId(), 0);
                if (pConsignado == null) {
                    ConsignadoLimiteCliente consignadoLimiteCliente = bdConsignadoLimiteCliente.getByIdCLiente(cliente.getId());
                    if (consignadoLimiteCliente != null) {
                        venda_tv_consignacao.setTextColor(Color.parseColor("#FF0000"));
                        venda_tv_consignacao.setText("Apto para consignação");
                        venda_tv_ClickConsignacao.setVisibility(View.VISIBLE);
                    } else {
                        venda_tv_consignacao.setText("Não habilitado para consignação");
                        venda_tv_ClickConsignacao.setVisibility(View.GONE);
                    }
                } else {
                    venda_tv_consignacao.setTextColor(Color.parseColor("#788CFC"));
                    venda_tv_consignacao.setText("Consignação em Andamento");
                    venda_tv_ClickConsignacao.setVisibility(View.VISIBLE);
                }
            } else {
                venda_tv_consignacao.setText("Não habilitado para consignação");
                venda_tv_ClickConsignacao.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), true);
        }
    }

    private VerificaBotoesVenda verificaBotoesVenda(Cliente cliente) {

        VerificaBotoesVenda botoesVenda = new VerificaBotoesVenda();

        if (cliente.getAuditagem() != null && cliente.getAuditagem().equalsIgnoreCase(SIM)) {
            botoesVenda.setAuditagemObrigatoria(true);
        }

        if (cliente.getMerchandising() != null && cliente.getMerchandising().equalsIgnoreCase(SIM)) {
            botoesVenda.setMerchandisingObrigatorio(true);
        }

        return botoesVenda;
    }

    private void exibirMensagem(String titulo, String mensagem) {
        Alerta alerta = new Alerta(this, titulo, mensagem);
        alerta.showConfirm(
                (dialog, which) -> dialog.cancel(), getString(R.string.ok),
                (dialog, which) -> btAtualizacaoCadastro.performClick(), getString(R.string.atualizar_cliente_recadastro_bt_atualizar)
        );
    }

    private void desabilitarBotao(Button button) {
        button.setAlpha(BOTAO_DESABILITADO);
        button.setOnClickListener(null);
    }

    private void verificarPendencias() {
        if (cliente == null) {
            return;
        }

        Chamado chamado = dbChamados.getChamadoByClienteId(cliente.getId());
        if (chamado != null) {
            Utilidades.openChamadoCliente(this, chamado);
            return;
        }

        if (cliente.isAtualizaBinario()) {
            atualizarBinario();
        }
    }

    private void atualizarBinario() {
        try {
            TokenDialog tokenDialog = new TokenDialog();
            tokenDialog.setCancelable(false);
            tokenDialog.onCompleteListenet = confirmado -> {
                if (!confirmado) {
                    return;
                }

                try {
                    clienteDao.confirmaAtualizacaoBinario(visita.getIdCliente());
                    cliente = clienteDao.obterPorId(visita.getIdCliente());
                    Utilidades.retornaMensagem(
                            VendaAberturaActivity.this,
                            getString(R.string.venda_abertura_token_atualizado),
                            false
                    );
                } catch (Exception ex) {
                    Mensagens.mensagemErro(VendaAberturaActivity.this, ex.getMessage(), false);
                }
            };
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, visita.getIdCliente());
            tokenDialog.setArguments(args);
            tokenDialog.show(
                    getSupportFragmentManager(),
                    TokenDialog.class.getSimpleName()
            );
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    private boolean temClienteValido() {
        obterCliente();
        if (cliente != null) {
            return true;
        }

        if (visita == null) {
            Mensagens.visitaNaoIniciada(this);
            return false;
        }

        final int motivoCancelamento = 15;
        new DBVisita(VendaAberturaActivity.this).encerrarVisitaSemDataFim(visita.getId(),
                motivoCancelamento,
                true,
                visita.getIdCliente());
        Mensagens.clienteNaoEncontrado(VendaAberturaActivity.this, true);

        return false;
    }

    private boolean verificaRuptura() {
        if (cliente == null) {
            return false;
        }
        ArrayList<SugestaoVenda> sugestaovenda = dbSugestaoVenda.getSugestaoRuptura(cliente.getId());
        if (sugestaovenda != null && sugestaovenda.size() > 0)
            return true;
        else
            return false;
    }

    private void abrirRupturas() {
        try {
            SugestaoVendaDialog dialog = new SugestaoVendaDialog();
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, cliente.getId());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(VendaAberturaActivity.this, ex.getMessage(), false);
        }
    }

    private boolean produtoRuptura(Venda venda, List<ItemVenda> itens) {
        if (cliente == null) {
            return false;
        }
        boolean ehValido = true;
        ArrayList<SugestaoVenda> listaSugestaoVenda = dbSugestaoVenda.getSugestaoRuptura(cliente.getId());
        for (SugestaoVenda sugestao : listaSugestaoVenda) {
            ehValido = dbSugestaoVenda.validaSugestaoVendaItem(venda.getId(), sugestao.getGrupoProduto(), sugestao.getIdOperadora());
            if (!ehValido)
                break;
        }
        return ehValido;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            finish();
        } else if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED)
            Log.d("Roni", "Consignacao: RESULT_CANCELED");
        else if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            Consignado consignado = new Consignado();
            consignado = bdConsignado.getByIdCLiente(cliente.getId(), 0);
            if (consignado == null) {
                ConsignadoLimiteCliente consignadoLimiteCliente = bdConsignadoLimiteCliente.getByIdCLiente(cliente.getId());
                if (cliente.isVendaConsignada() && consignadoLimiteCliente != null) {
                    ConfirmarConsignacaoDialog.newInstance(
                                    R.string.consignacao_titulo, "Cliente possui chips liberados para consignação",
                                    R.string.consignar,
                                    R.string.naoconsignar
                            )
                            .setPositiveListener(() -> ChamaTelaConsignado())
                            .show(getSupportFragmentManager(), ConfirmarConsignacaoDialog.class.getSimpleName());
                }
            }
        }
    }
}
