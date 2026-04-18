package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemConsignadoAdapter;
import com.axys.redeflexmobile.shared.adapters.ItemConsignadoCodBarraAdapter;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDaoImpl;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.tasks.VendaSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.dialog.ConfirmarSenhaConsignacao;
import com.axys.redeflexmobile.ui.dialog.ConfirmarSenhaDialog;
import com.axys.redeflexmobile.ui.venda.bipagem.BipagemVendaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.produto.PedidoVendaProdutoDialog;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ConsignadoAuditagemActivity extends AppCompatActivity {
    RecyclerView rvProdutos;
    AppCompatTextView tvCliente;
    AppCompatTextView tvSubtotal;
    AppCompatTextView tvTempo;
    AppCompatButton btnGravarConsignacao;
    AppCompatButton btnSincronizar;
    private CountDownTimer timer;

    Visita visita = new Visita();

    BDConsignadoLimiteProduto bdConsignadoLimiteProduto;
    BDConsignado bdConsignado;
    BDConsignadoItem bdConsignadoItem;
    BDConsignadoItemCodBarra bdConsignadoItemCodBarra;
    EstoqueDao estoqueDao;
    DBEstoque dbEstoque;

    ArrayList<ConsignadoItem> listaItens;

    ArrayList<ConsignadoLimiteProduto> listaConsignadoLimiteProdutos;

    ItemConsignadoCodBarraAdapter adapterItens;

    int mOperacao = 0;

    int mIdConsignadoServer = 0;

    public static final int TIMEOUT_CLICK = 1200;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignado_auditagem);

        // Carrega o Objeto que for passado
        final Bundle data = (Bundle) getIntent().getExtras();
        visita = (Visita) data.getSerializable("Visita");
        listaItens = (ArrayList<ConsignadoItem>) data.getSerializable("ConsignadoItem");
        mOperacao = data.getInt("Operacao");
        mIdConsignadoServer = data.getInt("IdConsignadoServer");
        criarObjetos();
        montarActionBar();
        criarEventos();
        carregarDados(visita, null);
    }

    private void montarActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Produtos Consignação");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            cancelar();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelar() {
        Context context = this;
        Alerta alerta = new Alerta(
                context,
                getString(R.string.app_name),
                "Tem certeza que deseja cancelar a consignação?\nOs dados inseridos serão perdidos."
        );
        alerta.showConfirm((dialog, which) -> cancelarConsignacao(), null);
    }

    @Override
    public void onBackPressed() {
        cancelar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    public void iniciarTimer() {
        timer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTempo.setText(Utilidades.retornaTempoAtendimento(visita.getDataInicio()));
            }

            public void onFinish() {
                /* unused */
            }
        };

        timer.start();
    }

    private void criarObjetos() {
        rvProdutos = (RecyclerView) findViewById(R.id.consAudit_rv_itens);
        rvProdutos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        tvCliente = (AppCompatTextView) findViewById(R.id.consAudit_tv_cliente);
        tvSubtotal = (AppCompatTextView) findViewById(R.id.consAudit_tv_subtotal);
        tvTempo = (AppCompatTextView) findViewById(R.id.consAudit_tv_tempo_atendimento);
        btnGravarConsignacao = (AppCompatButton) findViewById(R.id.consAudit_btn_gravar_consignacao);
        btnSincronizar = (AppCompatButton) findViewById(R.id.consAudit_btn_sincronizar);

        bdConsignadoLimiteProduto = new BDConsignadoLimiteProduto(ConsignadoAuditagemActivity.this);
        bdConsignado = new BDConsignado(ConsignadoAuditagemActivity.this);
        bdConsignadoItem = new BDConsignadoItem(ConsignadoAuditagemActivity.this);
        bdConsignadoItemCodBarra = new BDConsignadoItemCodBarra(ConsignadoAuditagemActivity.this);
        estoqueDao = new EstoqueDaoImpl(ConsignadoAuditagemActivity.this);
        dbEstoque = new DBEstoque(ConsignadoAuditagemActivity.this);
        iniciarTimer();
    }

    private void criarEventos() {
        Disposable disposableSync = RxView.clicks(btnSincronizar)
                .throttleFirst(TIMEOUT_CLICK, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("mTipoOperacao", 1);
                    Utilidades.openNewActivity(
                            ConsignadoAuditagemActivity.this,
                            SyncActivity.class,
                            bundle,
                            false
                    );
                });

        Disposable disposableAdd = RxView.clicks(btnGravarConsignacao)
                .throttleFirst(TIMEOUT_CLICK, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (!Validacoes.validacaoDataAparelho(ConsignadoAuditagemActivity.this)) {
                        return;
                    }

                    // Valida se todos os códigos de Barras foram informados
                    boolean validaGravacao = true;
                    for (int aa = 0; aa < listaItens.size(); aa++) {
                        int quantidadeCodBarra = 0;
                        for (int bb = 0; bb < listaItens.get(aa).getListaCodigoBarra().size(); bb++) {
                            quantidadeCodBarra += listaItens.get(aa).getListaCodigoBarra().get(bb).getQtd();
                        }

                        if (listaItens.get(aa).getQtd() != quantidadeCodBarra) {
                            validaGravacao = false;
                            break;
                        }
                    }

                    if (validaGravacao) {
                        Alerta alerta = new Alerta(ConsignadoAuditagemActivity.this, getResources().getString(R.string.app_name), "Deseja Realmente enviar o pedido?");
                        alerta.showConfirm((dialog, which) -> {
                            try {
                                Consignado consignado = bdConsignado.getConsignadobyIdVisita(String.valueOf(visita.getId()));
                                if (consignado != null) {
                                    Log.d("Roni", "criarEventos: " + consignado.getIdConsignadoRefer());
                                    // Envia a Consignação
                                    enviaConsignacao(ConsignadoAuditagemActivity.this, consignado, visita);
                                } else {
                                    Mensagens.mensagemErro(ConsignadoAuditagemActivity.this, "Não foi possível fazer o Envio da Consignação!\nEntre em contato com o Suporte.", false);
                                }

                            } catch (Exception ex) {
                                Mensagens.mensagemErro(ConsignadoAuditagemActivity.this, ex.getMessage(), false);
                            }
                        }, null);
                    } else {
                        Alerta alerta = new Alerta(
                                ConsignadoAuditagemActivity.this,
                                getString(R.string.app_name),
                                "Existem produtos sem código de barras informado. Verifique!"
                        );
                        alerta.show();
                    }
                });

        compositeDisposable.add(disposableSync);
        compositeDisposable.add(disposableAdd);
    }

    private void carregarDados(Visita pVisita, Consignado consignado) {
        tvCliente.setText(visita.getIdClienteSGV() + " - " + visita.getNomeFantasia());

        if (visita != null) {
            if (mOperacao == 0) {
                Log.d("Roni", "carregarDados: Passou 1");
                consignado = bdConsignado.getConsignadobyIdVisita(String.valueOf(visita.getId()));
                if (consignado == null) {
                    // Cria cabecalho da Consignação
                    long codigo = bdConsignado.novoConsignado(visita, new DBColaborador(ConsignadoAuditagemActivity.this).get().getId(), 0);
                    consignado = bdConsignado.getById(String.valueOf(codigo));

                    // Alimenta os Itens da Consignação conforme Limites Produtos definidos
                    listaConsignadoLimiteProdutos = bdConsignadoLimiteProduto.getLimiteProdutoPorCliente(visita.getIdCliente());
                    if (listaConsignadoLimiteProdutos.size() > 0) {
                        for (int aa = 0; aa < listaConsignadoLimiteProdutos.size(); aa++) {
                            Produto produto = dbEstoque.getProdutoById(listaConsignadoLimiteProdutos.get(aa).getIdProduto());
                            if (produto != null) {
                                ConsignadoItem consignadoItem = new ConsignadoItem();
                                consignadoItem.setIdConsignado(consignado.getId());
                                consignadoItem.setQtd(listaConsignadoLimiteProdutos.get(aa).getQuantidade());
                                consignadoItem.setValorTotalItem(produto.getPrecovenda() * listaConsignadoLimiteProdutos.get(aa).getQuantidade());
                                consignadoItem.setIdProduto(listaConsignadoLimiteProdutos.get(aa).getIdProduto());
                                consignadoItem.setValorUnit(produto.getPrecovenda());
                                consignadoItem.setNomeProduto(produto.getNome());
                                try {
                                    bdConsignadoItem.addConsignadoItem(consignadoItem);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }

                //Atualiza Grid da Tela com os Produtos
                listaItens = bdConsignadoItem.getByIdCConsignado(String.valueOf(consignado.getId()));
                popularAdapter(listaItens, visita.getId());

            // Reconsignação {Cria Nova Consignação referenciando a que consignação será reconsignado}
            } else if (mOperacao == 1) {
                if (listaItens != null) {
                    Log.d("Roni", "carregarDados: Operacao 1");
                    long codigo = bdConsignado.novoConsignado(visita, new DBColaborador(ConsignadoAuditagemActivity.this).get().getId(), mIdConsignadoServer);
                    consignado = bdConsignado.getById(String.valueOf(codigo));

                    for (int aa = 0; aa < listaItens.size(); aa++) {
                        Produto produto = dbEstoque.getProdutoById(listaItens.get(aa).getIdProduto());
                        ConsignadoItem consignadoItem = new ConsignadoItem();
                        consignadoItem.setIdConsignado(consignado.getId());
                        consignadoItem.setQtd(listaItens.get(aa).getQtdAuditado());
                        consignadoItem.setValorTotalItem(produto.getPrecovenda() * listaItens.get(aa).getQtdAuditado());
                        consignadoItem.setIdProduto(listaItens.get(aa).getIdProduto());
                        consignadoItem.setValorUnit(produto.getPrecovenda());
                        consignadoItem.setNomeProduto(produto.getNome());
                        try {
                            long codigoItem = bdConsignadoItem.addConsignadoItem(consignadoItem);
                            for (ConsignadoItemCodBarra itemCodBarra : listaItens.get(aa).getListaCodigoBarra()) {
                                itemCodBarra.setId(0);
                                itemCodBarra.setIdConsignado(consignado.getId());
                                itemCodBarra.setIdConsignadoItem((int) codigoItem);
                                itemCodBarra.setCodigoBarraIni(itemCodBarra.getCodigoBarraIni());
                                itemCodBarra.setCodigoBarraFim(itemCodBarra.getCodigoBarraFim());
                                itemCodBarra.setQtd(itemCodBarra.getQtd());
                                bdConsignadoItemCodBarra.addConsignadoItemCodBarra(itemCodBarra);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    listaItens = bdConsignadoItem.getByIdCConsignado(String.valueOf(consignado.getId()));
                    popularAdapter(listaItens, visita.getId());
                }
            } else if (mOperacao == 2) {
                Log.d("Roni", "carregarDados: Operacao 2");
                if (listaItens != null) {
                    consignado = bdConsignado.getConsignadobyIdVisita(String.valueOf(visita.getId()));
                    if (consignado == null) {
                        Log.d("Roni", "Não Achou Consignado: ");
                        // Cria cabecalho da Consignação
                        long codigo = bdConsignado.novoConsignado(visita, new DBColaborador(ConsignadoAuditagemActivity.this).get().getId(), 0);
                        consignado = bdConsignado.getById(String.valueOf(codigo));

                        for (int aa = 0; aa < listaItens.size(); aa++) {
                            Produto produto = dbEstoque.getProdutoById(listaItens.get(aa).getIdProduto());
                            ConsignadoItem consignadoItem = new ConsignadoItem();
                            consignadoItem.setIdConsignado(consignado.getId());
                            consignadoItem.setQtd(listaItens.get(aa).getQtd());
                            consignadoItem.setValorTotalItem(produto.getPrecovenda() * listaItens.get(aa).getQtd());
                            consignadoItem.setIdProduto(listaItens.get(aa).getIdProduto());
                            consignadoItem.setValorUnit(produto.getPrecovenda());
                            consignadoItem.setNomeProduto(produto.getNome());
                            try {
                                long codigoItem = bdConsignadoItem.addConsignadoItem(consignadoItem);
                                for (ConsignadoItemCodBarra itemCodBarra : listaItens.get(aa).getListaCodigoBarra()) {
                                    itemCodBarra.setId(0);
                                    itemCodBarra.setIdConsignado(consignado.getId());
                                    itemCodBarra.setIdConsignadoItem((int) codigoItem);
                                    itemCodBarra.setCodigoBarraIni(itemCodBarra.getCodigoBarraIni());
                                    itemCodBarra.setCodigoBarraFim(itemCodBarra.getCodigoBarraFim());
                                    itemCodBarra.setQtd(itemCodBarra.getQtd());
                                    bdConsignadoItemCodBarra.addConsignadoItemCodBarra(itemCodBarra);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        listaItens = bdConsignadoItem.getByIdCConsignado(String.valueOf(consignado.getId()));

                        // Insere dados nas tabelas temporárias de Pistolagem
                        InsereDadosTmp(consignado, listaItens);
                        popularAdapter(listaItens, visita.getId());
                    }
                    else {
                        Log.d("Roni", "Achou Consignado: ");
                        listaItens = bdConsignadoItem.getByIdCConsignado(String.valueOf(consignado.getId()));

                        // Insere dados nas tabelas temporárias de Pistolagem
                        InsereDadosTmp(consignado, listaItens);
                        popularAdapter(listaItens, visita.getId());
                    }
                }
            }
        } else {
            Log.d("Redeflex", "carregarProdutosBipados: Visita está vazio");
        }
    }

    private void cancelarConsignacao() {
        if (visita != null) {
            Consignado consignado = bdConsignado.getConsignadobyIdVisita(String.valueOf(visita.getId()));
            // Remover dados temporários
            if (consignado != null) {
                estoqueDao.deletarPistolagensComboNaoFinalizadas(consignado);
                // Apagar dados Consignação
                bdConsignadoItemCodBarra.deleteByIdConsignado(String.valueOf(consignado.getId()));
            }
        }
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void popularAdapter(ArrayList<ConsignadoItem> itens, int pIdVisita) {
        Log.d("Roni", "popularAdapter: " + pIdVisita);
        adapterItens = new ItemConsignadoCodBarraAdapter(itens, ConsignadoAuditagemActivity.this, pIdVisita);
        rvProdutos.setAdapter(adapterItens);
        double vTotalItens = 0;
        for (int i = 0; i < itens.size(); i++) {
            vTotalItens += itens.get(i).getValorTotalItem();
        }
        tvSubtotal.setText("SubTotal: " + Util_IO.formatDoubleToDecimalNonDivider(vTotalItens));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.ProdutoVenda) {
            carregarDados(visita, null);
        }
        else
        {
            Log.d("Roni", "onActivityResult: ");
        }
    }

    public void enviaConsignacao(Context pContext, Consignado pConsignado, Visita pVisita) throws Exception {
        if (new DBCliente(pContext).getById(pVisita.getIdCliente()).getPedeSenha().equalsIgnoreCase("S")) {
            openSenha(pContext, pConsignado);
        } else {
            Utilidades.encerrarAtendimentoConsignacao(this, pConsignado, pVisita, 16, null);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    public static void openSenha(Context pContext, Consignado pConsignado) throws Exception {
        try {
            ConfirmarSenhaConsignacao dialog = new ConfirmarSenhaConsignacao();
            dialog.myCompleteListenerSenha = null;
            Bundle args = new Bundle();
            args.putString("CodigoCliente", String.valueOf(pConsignado.getIdCliente()));
            args.putString("CodigoConsignacao", String.valueOf(pConsignado.getId()));
            dialog.setArguments(args);
            dialog.show(((FragmentActivity) pContext).getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(pContext, ex.getMessage(), false);
        }
    }

    private void InsereDadosTmp(Consignado pConsignado, ArrayList<ConsignadoItem> plistaItens)
    {
        estoqueDao.deletarPistolagensConsignado(pConsignado);

        // Insere dados nas tabelas temporárias de Pistolagem
        for (ConsignadoItem consigItem : plistaItens) {
            Log.d("Roni", "InsereDadosTmp: Alimenta tabelas temporárias");

            Produto prod = new DBEstoque(ConsignadoAuditagemActivity.this).getProdutoById(consigItem.getIdProduto());
            int TotalQtdCod = 0;
            ArrayList<CodBarra> listCodBarra = new ArrayList<>();
            for (ConsignadoItemCodBarra codBarra: consigItem.getListaCodigoBarra()) {
                CodBarra codBarraAux = new CodBarra();
                if (Util_IO.isNullOrEmpty(codBarra.getCodigoBarraFim()))
                    codBarraAux.setIndividual(true);
                else
                    codBarraAux.setIndividual(false);
                codBarraAux.setCodBarraInicial(codBarra.getCodigoBarraIni());
                codBarraAux.setCodBarraFinal(codBarra.getCodigoBarraFim());
                codBarraAux.setIdProduto(consigItem.getIdProduto());
                codBarraAux.setGrupoProduto(prod.getGrupo());
                codBarraAux.setAuditadoCons("S");
                listCodBarra.add(codBarraAux);
                TotalQtdCod += Integer.parseInt(codBarraAux.retornaQuantidade(UsoCodBarra.GERAL));
            }

            ItemVendaCombo itemVendaCombo = new ItemVendaCombo();
            itemVendaCombo.setQtdCombo(0);
            itemVendaCombo.setIdProduto(consigItem.getIdProduto());
            itemVendaCombo.setCombo(false);
            itemVendaCombo.setValorUN(consigItem.getValorUnit());
            itemVendaCombo.setCodigosList(listCodBarra);
            itemVendaCombo.setQtde(TotalQtdCod);
            estoqueDao.incluirComboPistolagem(itemVendaCombo, pConsignado);
        }
    }
}
