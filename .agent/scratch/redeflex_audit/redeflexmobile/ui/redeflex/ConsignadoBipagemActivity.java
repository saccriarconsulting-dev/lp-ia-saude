package com.axys.redeflexmobile.ui.redeflex;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.CodBarraAdapter;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDaoImpl;
import com.axys.redeflexmobile.shared.dao.IccidDao;
import com.axys.redeflexmobile.shared.dao.IccidDaoImpl;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.RetCodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.venda.bipagem.BipagemVendaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.quantidade.PedidoVendaQuantidadeDialog;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ConsignadoBipagemActivity extends AppCompatActivity implements CodBarraAdapter.ICodBarraAdapterListenner {
    public static final int BUTTON_WAIT_DURATION = 2200;

    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressDialog mDialog;
    private EditText txtCodigoBarras;
    private ImageButton btnAddCodigo;
    private LinearLayout btnLerCodigoBarra;
    private ExpandableListView lsICCIDs;


    // Metodos
    private IccidDao iccidDao;
    private EstoqueDao estoqueDao;
    private BDConsignado consignadoDao;

    // Objetos
    private CodBarra newCodBarra;
    private Visita visita;
    private Consignado consignado;
    private Produto produto;
    private Iccid iccid;
    private EstruturaProd estruturaProd;
    private ArrayList<EstruturaProd> itensEstruturaProd;
    private ArrayList<ItemVendaCombo> listFinal;

    ConsignadoItem consignadoItem;

    // Adapters
    private CodBarraAdapter mAdapter;

    // Variaveis Globais
    private String vOperacao;
    private int quantidadeExterna, quantidadeExternaBipada;
    private String idVendaItemExterno;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bipagem_venda);

        criarToolbar();
        criarObjetos();

        Bundle bundle = getIntent().getExtras();
        produto = null;

        if (bundle != null && !Util_IO.isNullOrEmpty(bundle.getString(Config.CodigoProduto))) {
            produto = estoqueDao.getProdutoById(bundle.getString(Config.CodigoProduto));
            if (produto == null) {
                Mensagens.produtoNaoEncontrado(this);
                return;
            }
            mudarTitulo("Pistolagem: " + produto.getId());

            quantidadeExternaBipada = bundle.getInt(Config.QTD_ITEM_LIDO_VENDA, 0);
            quantidadeExterna = bundle.getInt(Config.QTD_ITEM_VENDA, 0);
            vOperacao = bundle.getString("Operacao");

            if (!Util_IO.isNullOrEmpty(bundle.getString(Config.CODIGO_ITEM_VENDA))) {
                idVendaItemExterno = bundle.getString(Config.CODIGO_ITEM_VENDA);
            }
        }

        criarEventos();
        lsICCIDs.setEmptyView(findViewById(R.id.empty));

        // Processo que será feito a medida que for fazer auditagem de uma Venda de Consignação
        if (vOperacao.equals("Auditagem")) {
            // Carregar dados codigo Barras do Produto Consignado
            consignadoItem = new BDConsignadoItem(ConsignadoBipagemActivity.this).getById(idVendaItemExterno);
            if (consignadoItem != null) {
                consignado = new BDConsignado(ConsignadoBipagemActivity.this).getById(String.valueOf(consignadoItem.getIdConsignado()));
                atualizarItens();
            }
        }
        else if ((vOperacao.equals("Parcial")))
        {
            Log.d("Roni", "onCreate: " + vOperacao);
            consignadoItem = new BDConsignadoItem(ConsignadoBipagemActivity.this).getById(idVendaItemExterno);
            if (consignadoItem != null) {
                consignado = new BDConsignado(ConsignadoBipagemActivity.this).getById(String.valueOf(consignadoItem.getIdConsignado()));
                atualizarItens();
            }
        }
        else {
            try {
                visita = Utilidades.getVisita(ConsignadoBipagemActivity.this);
                if (visita == null) {
                    Mensagens.visitaNaoIniciada(ConsignadoBipagemActivity.this);
                    return;
                }

                consignado = Utilidades.getConsignado(ConsignadoBipagemActivity.this, visita);
                atualizarItens();

                //removerPistolagemDivergente();
            } catch (Exception ex) {
                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        hideProgress();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    private void criarObjetos() {
        txtCodigoBarras = findViewById(R.id.txtCodigoBarras);
        btnAddCodigo = findViewById(R.id.btnAddCodigo);
        btnLerCodigoBarra = findViewById(R.id.btnLerCodigoBarra);
        lsICCIDs = findViewById(R.id.expandableICCIDs);

        iccidDao = new IccidDaoImpl(ConsignadoBipagemActivity.this);
        estoqueDao = new EstoqueDaoImpl(ConsignadoBipagemActivity.this);
        consignadoDao = new BDConsignado(ConsignadoBipagemActivity.this);
    }

    private void criarEventos() {
        compositeDisposable.add(
                RxView.clicks(btnAddCodigo)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            String cod = txtCodigoBarras.getText().toString().trim();
                            if (vOperacao.equals("Auditagem")) {
                                Log.d("Roni", "criarEventos: 1");
                                VerificaInclusaoAuditagem(cod);
                            }
                            else {
                                Log.d("Roni", "criarEventos: 2");
                                verificaInclusaoCodBarra(cod);
                            }
                        }, Timber::e)
        );
        compositeDisposable.add(
                RxView.clicks(btnLerCodigoBarra)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> openCamera(), Timber::e)
        );
    }

    private void openCamera() {
        try {
            if (txtCodigoBarras != null)
                txtCodigoBarras.setText("");
            CodeReader.openCodeReader(ConsignadoBipagemActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false);
        }
    }

    // IMPORTANTE
    private void verificaInclusaoCodBarra(final String pCodBarra) {
        if (Util_IO.isNullOrEmpty(pCodBarra)) {
            Mensagens.codigoBarraNaoInformado(ConsignadoBipagemActivity.this, (dialog, which) -> openCamera());
            return;
        }

        iccid = iccidDao.getByCodigo(pCodBarra);
        if (iccid == null) {
            Alerta alerta = new Alerta(
                    ConsignadoBipagemActivity.this,
                    pCodBarra,
                    getString(R.string.bipagem_venda_dialog_iccid_inexistente_mensagem)
            );
            alerta.show((dialog, which) -> openCamera());
            return;
        }

        if (!iccid.getItemCode().equalsIgnoreCase(produto.getId())) {
            Alerta alerta = new Alerta(
                    ConsignadoBipagemActivity.this,
                    pCodBarra,
                    getString(R.string.bipagem_venda_dialog_iccid_invalido_mensagem)
            );
            alerta.show();
            return;
        }

        final Produto produto;
        if (this.produto != null) {
            itensEstruturaProd = estoqueDao.getEstruturaByItemPai(this.produto.getId());
        }

        produto = estoqueDao.getProdutoById(iccid.getItemCode());
        if (produto == null) {
            Alerta alerta = new Alerta(
                    ConsignadoBipagemActivity.this,
                    pCodBarra,
                    getString(R.string.bipagem_venda_dialog_produto_inexistente_mensagem, iccid.getItemCode())
            );
            alerta.show((dialog, which) -> openCamera());
            return;
        }


        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !pCodBarra.startsWith(produto.getIniciaCodBarra())) {
            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Código de Barra deve começar com: " + produto.getIniciaCodBarra());
            alerta.show((dialog, which) -> openCamera());
        } else if (produto.getQtdCodBarra() > 0 && pCodBarra.trim().length() != produto.getQtdCodBarra()) {
            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Código de Barra deve conter " + produto.getQtdCodBarra() + " caracteres");
            alerta.show((dialog, which) -> openCamera());
        } else {
            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Deseja incluir esse código de barra?");
            alerta.showConfirm((dialog, which) -> {
                if (newCodBarra == null && produto.getQtdCombo() == 0) {
                    Alerta alerta1 = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Código de barra unitário?");
                    alerta1.showConfirm(
                            (dialogYes, whichYes) -> addCodBarra(produto, true, pCodBarra, true),
                            (dialogNO, whichNo) -> addCodBarra(produto, false, pCodBarra, true)
                    );
                } else if (produto.getQtdCombo() > 0) {
                    addCodBarra(produto, true, pCodBarra, true);
                } else {
                    addCodBarra(produto, false, pCodBarra, false);
                }
            }, (dialog, which) -> openCamera());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void showProgress() {
        this.runOnUiThread(() -> mDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), "Aguarde, validando range...", false, false));
    }

    private void hideProgress() {
        // https://stackoverflow.com/questions/22924825/view-not-attached-to-window-manager-crash/23703286
        this.runOnUiThread(() -> {
            if (mDialog != null && mDialog.isShowing() && !isFinishing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        });
    }

    private void criarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mudarTitulo("Pistolagem");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            try {
                                if (consignado != null)
                                    estoqueDao.deletarPistolagensComboNaoFinalizadas(consignado);
                            } catch (Exception ex) {
                                Utilidades.retornaMensagem(ConsignadoBipagemActivity.this, ex.getMessage(), false);
                            }
                            setResult(RESULT_CANCELED);
                            finish();
                        }, Timber::e)
        );

        compositeDisposable.add(
                RxToolbar.itemClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            if (v.getItemId() == R.id.opcao_salvar) {
                                if (vOperacao.equals("Auditagem") || vOperacao.equals("Parcial")) {
                                    Log.d("Roni", "criarToolbar: " + vOperacao);
                                    salvarAuditagem();
                                }
                                else {
                                    Log.d("Roni", "criarToolbar: " + vOperacao);
                                    salvar();
                                }
                            }
                        }, Timber::e)
        );
    }

    private void mudarTitulo(String texto) {
        setTitle(texto);
        toolbar.setTitle(texto);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(texto);
        }
    }

    private void mostrarCarregando() {
        progressDialog = ProgressDialog.show(
                this,
                getString(R.string.app_name),
                getString(R.string.bipagem_venda_progress_mensagem),
                false,
                false
        );
    }

    private void ocultarCarregando() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void atualizarItens() {
        listFinal = (ArrayList<ItemVendaCombo>) estoqueDao.getPistolagemItem(consignado, produto.getId());
        mAdapter = new CodBarraAdapter(listFinal, ConsignadoBipagemActivity.this, this, 0);
        lsICCIDs.setEmptyView(findViewById(R.id.empty));
        lsICCIDs.setAdapter(mAdapter);
        for (int i = 0; i < listFinal.size(); i++) {
            if (!listFinal.get(i).getCodigosList().isEmpty()) {
                lsICCIDs.expandGroup(i);
            }
        }
    }

    @Override
    public void removeGroupItem(ArrayList<ItemVendaCombo> lista, int groupPosition) {
        Alerta alerta = new Alerta(
                ConsignadoBipagemActivity.this,
                getString(R.string.app_name),
                getString(R.string.bipagem_venda_dialog_remover_mensagem)
        );
        alerta.showConfirm((dialog, which) -> {
            try {
                estoqueDao.deletarPistolagemByComboId(lista.get(groupPosition).getId());
                atualizarItens();
            } catch (Exception ex) {
                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false);
            }
        }, null);
    }

    @Override
    public void removeChildItem(ArrayList<ItemVendaCombo> lista, CodBarra codBarra, int groupPosition, int childPosition) {
        Alerta alerta = new Alerta(
                ConsignadoBipagemActivity.this,
                getString(R.string.app_name),
                getString(R.string.bipagem_venda_dialog_remover_mensagem)
        );
        alerta.showConfirm((dialog, which) -> {
            try {
                DBEstoque dbEstoque = new DBEstoque(ConsignadoBipagemActivity.this);
                int quantidade = 0, codigoPai = lista.get(groupPosition).getId();
                lista.get(groupPosition).getCodigosList().remove(childPosition);
                for (CodBarra items : lista.get(groupPosition).getCodigosList())
                    quantidade += Integer.parseInt(items.retornaQuantidade(UsoCodBarra.GERAL));

                if (quantidade == 0) {
                    lista.remove(groupPosition);
                    dbEstoque.deletarPistolagemByComboId(codigoPai);
                } else {
                    lista.get(groupPosition).setQtde(quantidade);

                    dbEstoque.updateQtdPistolagem(codigoPai, quantidade);
                    dbEstoque.deletarPitolagemUnitario(codBarra.getIdPistolagem());
                }

                atualizarItens();
            } catch (Exception ex) {
                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false);
            }
        }, null);
    }

    private ArrayList<ComboVenda> getEstruturaVenda(String pIdProduto, int pQtd, ArrayList<ComboVenda> pLista) {
        if (pLista == null)
            pLista = new ArrayList<>();

        ComboVenda comboVenda = new ComboVenda();
        comboVenda.setIdProduto(pIdProduto);
        comboVenda.setQuantidade(pQtd);

        pLista.add(comboVenda);

        return pLista;
    }

    private void addCodBarra(Produto pProduto, boolean pIndividual, String pCodigo, boolean pInicial) {
        new Thread(() -> {
            try {
                if (listFinal == null) {
                    listFinal = new ArrayList<>();
                }

                ArrayList<CodBarra> listValidacao = new ArrayList<>();
                String codigoProd = this.produto != null ? this.produto.getId() : pProduto.getId();

                ItemVendaCombo itemVendaCombo = estoqueDao.getPistolagem(consignado, codigoProd, null, 0);
                ArrayList<CodBarra> listCodBarra = null;
                ArrayList<ComboVenda> listCombo = null;

                if (itemVendaCombo != null) {
                    if (itemVendaCombo.getQtdCombo() > 0
                            && itemVendaCombo.getCodigosList() != null
                            && itemVendaCombo.getCodigosList().size() == (itemVendaCombo.getQtdCombo() * quantidadeExterna)) {
                        throw new Exception("Já foi realizada a leitura de todos os ICCIDs para este produto.");
                    }

                    if (itemVendaCombo.getCodigosList() != null) {
                        listCodBarra = (ArrayList<CodBarra>) itemVendaCombo.getCodigosList();
                        listCombo = itemVendaCombo.getListItens();
                    }
                } else {
                    itemVendaCombo = new ItemVendaCombo();
                    itemVendaCombo.setQtdCombo(pProduto.getQtdCombo());
                    itemVendaCombo.setIdProduto(pProduto.getId());
                    itemVendaCombo.setNomeProduto(pProduto.getNome());
                    itemVendaCombo.setCombo(false);
                    itemVendaCombo.setValorUN(produto.getPrecovenda());
                }

                if (listCodBarra != null) {
                    listValidacao.addAll(listCodBarra);
                }
                listValidacao.addAll(Utilidades.getCodBarraItensByVendidos(this));

                if (pIndividual) {
                    newCodBarra = new CodBarra();
                    newCodBarra.setIdConsignadoItem(idVendaItemExterno);
                    newCodBarra.setIndividual(true);
                    newCodBarra.setGrupoProduto(pProduto.getGrupo());
                    newCodBarra.setCodBarraInicial(pCodigo);
                    newCodBarra.setIdProduto(produto.getId());

                    if (listCodBarra == null) {
                        listCodBarra = new ArrayList<>();
                    }

                    // Verifica se existe codigo barras Consignado
                    if (consignadoDao.iccidVendido(newCodBarra)) {
                        this.runOnUiThread(() -> {
                            Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                            newCodBarra = null;
                            hideProgress();
                        });
                        return;
                    }

                    if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                        this.runOnUiThread(() -> {
                            Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                            newCodBarra = null;
                            hideProgress();
                        });
                        return;
                    }

                    for (ItemVendaCombo item : listFinal) {
                        if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, (ArrayList<CodBarra>) item.getCodigosList())) {
                            this.runOnUiThread(() -> {
                                Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                                newCodBarra = null;
                                hideProgress();
                            });
                            return;
                        }
                    }

                    listCodBarra.add(newCodBarra);
                    itemVendaCombo.setCodigosList(listCodBarra);
                    newCodBarra = null;
                } else {
                    if (newCodBarra == null) {
                        newCodBarra = new CodBarra();
                        newCodBarra.setIdConsignadoItem(idVendaItemExterno);
                        newCodBarra.setIndividual(false);
                        newCodBarra.setGrupoProduto(pProduto.getGrupo());
                        newCodBarra.setIdProduto(pProduto.getId());
                    }

                    if (listCodBarra == null) {
                        listCodBarra = new ArrayList<>();
                    }

                    if (pInicial) {
                        newCodBarra.setCodBarraInicial(pCodigo);
                    } else {
                        showProgress();
                        newCodBarra.setCodBarraFinal(pCodigo);

                        if (consignadoDao.iccidVendido(newCodBarra)) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (!CodigoBarra.validaQuantidadeRange(newCodBarra)) {
                            this.runOnUiThread(() -> {
                                Alerta alerta = new Alerta(ConsignadoBipagemActivity.this,
                                        getString(R.string.app_titulo),
                                        "Favor conferir o ICCID inicial e final");

                                alerta.show((dialog, which) -> {
                                    newCodBarra = null;
                                    openCamera();
                                });

                                hideProgress();
                            });
                            return;
                        }

                        RetCodBarra retCodBarra = CodigoBarra.validacaoConsignacao(
                                pCodigo,
                                produto,
                                Integer.parseInt(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)),
                                newCodBarra,
                                listValidacao,
                                pIndividual,
                                true,
                                ConsignadoBipagemActivity.this
                        );

                        if (!retCodBarra.isInclusaoOK()) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(
                                        ConsignadoBipagemActivity.this,
                                        retCodBarra.getMensagem(),
                                        false
                                );
                                newCodBarra = null;
                            });
                            return;
                        }

                        BigInteger value = new BigInteger(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL));
                        if (value.compareTo(BigInteger.valueOf(2)) < 0) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Verifique o ICCID não é sequêncial!", false);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (CodigoBarra.retornaICCID(newCodBarra.getCodBarraInicial(), newCodBarra.getGrupoProduto()).compareTo(CodigoBarra.retornaICCID(newCodBarra.getCodBarraFinal(), newCodBarra.getGrupoProduto())) > -1) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Verifique o ICCID não é sequêncial!", false);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (!newCodBarra.getIdProduto().equals(pProduto.getId())) {
                            Produto produtoInicial = estoqueDao.getProdutoById(newCodBarra.getIdProduto());
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, getString(R.string.nova_bipagem_range_iccid_produto_diferente, produtoInicial.getNome()), false);
                            });
                            newCodBarra = null;
                            return;
                        }

                        listCodBarra.add(newCodBarra);
                        itemVendaCombo.setCodigosList(listCodBarra);
                        newCodBarra = null;
                        hideProgress();
                    }
                }

                if (newCodBarra == null) {
                    int quantidade = 0;
                    if (itemVendaCombo.getCodigosList() != null) {
                        try {
                            for (CodBarra items : itemVendaCombo.getCodigosList()) {
                                quantidade += Integer.parseInt(items.retornaQuantidade(UsoCodBarra.GERAL));
                            }
                        } catch (NumberFormatException e) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Range muito grande.", false);
                            });
                            return;
                        }
                    }

                    itemVendaCombo.setQtde(quantidade);
                    estoqueDao.incluirComboPistolagem(itemVendaCombo, consignado);
                    this.runOnUiThread(this::atualizarItens);
                }

                if (itemVendaCombo.getQtdCombo() > 0 && itemVendaCombo.getCodigosList() != null && itemVendaCombo.getCodigosList().size() == (itemVendaCombo.getQtdCombo() * quantidadeExterna)) {
                    txtCodigoBarras.setText("");
                } else {
                    this.runOnUiThread(() -> {
                        openCamera();
                        txtCodigoBarras.setText("");
                    });
                }

            } catch (Exception ex) {
                this.runOnUiThread(() -> {
                    hideProgress();
                    Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false);
                });
            }

        }).start();
    }

    private void salvar() {
        mostrarCarregando();
        new Thread(() -> {
            try {
                // 1 - Verificar se tem Itens digitados
                if (listFinal == null || listFinal.size() == 0) {
                    runOnUiThread(() -> Mensagens.nenhumItemIncluido(ConsignadoBipagemActivity.this));
                    runOnUiThread(this::ocultarCarregando);
                    return;
                }

                // 2 - Verificar se a quantidade informada está conforme exigida
                int vTotSeriaisLidos = 0;
                for (ItemVendaCombo item : listFinal)
                {
                    for (CodBarra pCodBarra : item.getCodigosList()) {
                        vTotSeriaisLidos += Integer.parseInt(pCodBarra.retornaQuantidade(UsoCodBarra.GERAL));
                    }
                }

                if (vTotSeriaisLidos > quantidadeExterna)
                {
                    int finalVTotSeriaisLidos = vTotSeriaisLidos;
                    runOnUiThread(() -> Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Você inseriu " +
                            finalVTotSeriaisLidos + " unidades.\nLimite consignação: " + quantidadeExterna +".\n\nPor favor exclua " + (finalVTotSeriaisLidos - quantidadeExterna) +
                            " item(s) para seguir.",
                            false));
                    runOnUiThread(this::ocultarCarregando);
                    return;
                }

                boolean bValidacaoOK = false;
                for (ItemVendaCombo item : listFinal) {
                    Produto produto = estoqueDao.getProdutoById(item.getIdProduto());
                    produto.setPrecovenda(item.getValorUN());
                    bValidacaoOK = Validacoes.validacoesPreAddICCID(ConsignadoBipagemActivity.this,
                            produto,
                            null,
                            (ArrayList<CodBarra>) item.getCodigosList(),
                            item.isCombo(),
                            item.getListItens(),
                            UsoCodBarra.GERAL,
                            quantidadeExterna - quantidadeExternaBipada,
                            item,
                            quantidadeExterna);
                    produto.setQtde(0);
                    if (!bValidacaoOK) {
                        break;
                    }
                }

                if (bValidacaoOK) {
                    for (ItemVendaCombo item : listFinal) {
                        int quantidade = 0;
                        for (CodBarra temp : item.getCodigosList()) {
                            temp.setIdVendaItem(idVendaItemExterno);

                            try {
                                quantidade += Integer.parseInt(temp.retornaQuantidade(UsoCodBarra.GERAL));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Produto produto = estoqueDao.getProdutoById(item.getIdProduto());
                        produto.setPrecovenda(item.getValorUN());
                        produto.setQtde(item.getQtde());

                        bValidacaoOK = Validacoes.validacoesPreAddICCID(ConsignadoBipagemActivity.this,
                                produto,
                                null,
                                (ArrayList<CodBarra>) item.getCodigosList(),
                                item.isCombo(),
                                item.getListItens(),
                                UsoCodBarra.GERAL,
                                quantidadeExterna - quantidadeExternaBipada,
                                item,
                                quantidadeExterna);

                        if (!bValidacaoOK) {
                            break;
                        }

                        Utilidades.addICCIDItensConsignado(
                                ConsignadoBipagemActivity.this,
                                produto,
                                item.getQtde(),
                                item.getValorUN(),
                                consignado,
                                null,
                                (ArrayList<CodBarra>) item.getCodigosList(),
                                item.getListItens(),
                                Long.parseLong(idVendaItemExterno),
                                false
                        );
                    }

                    estoqueDao.confirmarPistolagens(consignado);
                    runOnUiThread(() -> {
                        setResult(RESULT_OK);
                        finish();
                    });
                }
            } catch (Exception ex) {
                runOnUiThread(() -> Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false));
            }

            runOnUiThread(this::ocultarCarregando);

        }).start();
    }

    private void VerificaInclusaoAuditagem(String pCodBarra) {
        if (Util_IO.isNullOrEmpty(pCodBarra)) {
            Mensagens.codigoBarraNaoInformado(ConsignadoBipagemActivity.this, (dialog, which) -> openCamera());
            return;
        }

        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !pCodBarra.startsWith(produto.getIniciaCodBarra())) {
            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Código de Barra deve começar com: " + produto.getIniciaCodBarra());
            alerta.show((dialog, which) -> openCamera());
        } else if (produto.getQtdCodBarra() > 0 && pCodBarra.trim().length() != produto.getQtdCodBarra()) {
            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Código de Barra deve conter " + produto.getQtdCodBarra() + " caracteres");
            alerta.show((dialog, which) -> openCamera());
        } else {
            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Deseja incluir esse código de barra?");
            alerta.showConfirm((dialog, which) -> {
                if (newCodBarra == null) {
                    Alerta alerta1 = new Alerta(ConsignadoBipagemActivity.this, pCodBarra, "Código de barra unitário?");
                    alerta1.showConfirm(
                            (dialogYes, whichYes) -> addCodBarraAuditagem(produto, true, pCodBarra, true),
                            (dialogNO, whichNo) -> addCodBarraAuditagem(produto, false, pCodBarra, true)
                    );
                } else {
                    addCodBarraAuditagem(produto, false, pCodBarra, false);
                }
            }, (dialog, which) -> openCamera());
        }
    }

    private void addCodBarraAuditagem(Produto pProduto, boolean pIndividual, String pCodigo, boolean pInicial) {
        new Thread(() -> {
            try {
                if (listFinal == null) {
                    listFinal = new ArrayList<>();
                }

                ArrayList<CodBarra> listValidacao = new ArrayList<>();
                String codigoProd = this.produto != null ? this.produto.getId() : pProduto.getId();

                ItemVendaCombo itemVendaCombo = estoqueDao.getPistolagem(consignado, codigoProd, "", 0);
                ArrayList<CodBarra> listCodBarra = null;
                ArrayList<ComboVenda> listCombo = null;

                if (itemVendaCombo != null) {
                    if (itemVendaCombo.getQtdCombo() > 0
                            && itemVendaCombo.getCodigosList() != null
                            && itemVendaCombo.getCodigosList().size() == (itemVendaCombo.getQtdCombo() * quantidadeExterna)) {
                        throw new Exception("Já foi realizada a leitura de todos os ICCIDs para este produto.");
                    }

                    if (itemVendaCombo.getCodigosList() != null) {
                        listCodBarra = (ArrayList<CodBarra>) itemVendaCombo.getCodigosList();
                        listCombo = itemVendaCombo.getListItens();
                    }
                } else {
                    itemVendaCombo = new ItemVendaCombo();
                    itemVendaCombo.setQtdCombo(pProduto.getQtdCombo());
                    itemVendaCombo.setIdProduto(pProduto.getId());
                    itemVendaCombo.setNomeProduto(pProduto.getNome());
                    itemVendaCombo.setCombo(false);
                    itemVendaCombo.setValorUN(produto.getPrecovenda());
                }

                if (listCodBarra != null) {
                    listValidacao.addAll(listCodBarra);
                }
                listValidacao.addAll(Utilidades.getCodBarraItensByVendidos(this));

                if (pIndividual) {
                    newCodBarra = new CodBarra();
                    newCodBarra.setIdConsignadoItem(idVendaItemExterno);
                    newCodBarra.setIndividual(true);
                    newCodBarra.setGrupoProduto(pProduto.getGrupo());
                    newCodBarra.setCodBarraInicial(pCodigo);
                    newCodBarra.setIdProduto(produto.getId());

                    if (listCodBarra == null) {
                        listCodBarra = new ArrayList<>();
                    }

                    // Valida se Codigos Informados estão no range informado Consignação
                    boolean ehRange = false;
                    ArrayList<CodBarra> retCodigosBarras = new BDConsignado(ConsignadoBipagemActivity.this).listCodigosbyConsignadoProduto(consignado.getId(), pProduto.getId());

                    if (CodigoBarra.verificaICCIDConsignado(newCodBarra, retCodigosBarras))
                        ehRange = true;

                    if (!ehRange) {
                        this.runOnUiThread(() -> {
                            Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, getString(R.string.app_titulo), "Código Barra Informado não pertence a Consignação do Cliente. Verifique!");
                            alerta.show((dialog, which) -> {
                                newCodBarra = null;
                                openCamera();
                            });
                            hideProgress();
                        });
                        return;
                    }

                    if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                        this.runOnUiThread(() -> {
                            Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                            newCodBarra = null;
                            hideProgress();
                        });
                        return;
                    }

                    for (ItemVendaCombo item : listFinal) {
                        if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, (ArrayList<CodBarra>) item.getCodigosList())) {
                            this.runOnUiThread(() -> {
                                Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                                newCodBarra = null;
                                hideProgress();
                            });
                            return;
                        }
                    }

                    if (!newCodBarra.getIdProduto().equals(pProduto.getId())) {
                        Produto produtoInicial = estoqueDao.getProdutoById(newCodBarra.getIdProduto());
                        this.runOnUiThread(() -> {
                            hideProgress();
                            Mensagens.mensagemErro(ConsignadoBipagemActivity.this, getString(R.string.nova_bipagem_range_iccid_produto_diferente, produtoInicial.getNome()), false);
                        });
                        newCodBarra = null;
                        return;
                    }

                    listCodBarra.add(newCodBarra);
                    itemVendaCombo.setCodigosList(listCodBarra);
                    newCodBarra = null;
                } else {
                    if (newCodBarra == null) {
                        newCodBarra = new CodBarra();
                        newCodBarra.setIdConsignadoItem(idVendaItemExterno);
                        newCodBarra.setIndividual(false);
                        newCodBarra.setGrupoProduto(pProduto.getGrupo());
                        newCodBarra.setIdProduto(pProduto.getId());
                    }

                    if (listCodBarra == null) {
                        listCodBarra = new ArrayList<>();
                    }

                    if (pInicial) {
                        newCodBarra.setCodBarraInicial(pCodigo);
                    } else {
                        showProgress();
                        newCodBarra.setCodBarraFinal(pCodigo);

                        // Valida se Codigos Informados estão no range informado Consignação
                        boolean ehRange = false;
                        ArrayList<CodBarra> retCodigosBarras = new BDConsignado(ConsignadoBipagemActivity.this).listCodigosbyConsignadoProduto(consignado.getId(), pProduto.getId());

                        if (CodigoBarra.verificaICCIDConsignado(newCodBarra, retCodigosBarras))
                            ehRange = true;

                        if (!ehRange) {
                            this.runOnUiThread(() -> {
                                Alerta alerta = new Alerta(ConsignadoBipagemActivity.this, getString(R.string.app_titulo), "Código Barra Informado não pertence a Consignação do Cliente. Verifique!");
                                alerta.show((dialog, which) -> {
                                    newCodBarra = null;
                                    openCamera();
                                });
                                hideProgress();
                            });
                            return;
                        }

                        if (!CodigoBarra.validaQuantidadeRange(newCodBarra)) {
                            this.runOnUiThread(() -> {
                                Alerta alerta = new Alerta(ConsignadoBipagemActivity.this,
                                        getString(R.string.app_titulo),
                                        "Favor conferir o ICCID inicial e final");

                                alerta.show((dialog, which) -> {
                                    newCodBarra = null;
                                    openCamera();
                                });

                                hideProgress();
                            });
                            return;
                        }

                        //RetCodBarra retCodBarra = CodigoBarra.validacaoConsignacao(
                        //        pCodigo,
                        //        produto,
                        //        Integer.parseInt(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)),
                        //        newCodBarra,
                        //        listValidacao,
                        //        pIndividual,
                        //        true,
                        //        ConsignadoBipagemActivity.this
                        //);

                        //if (!retCodBarra.isInclusaoOK()) {
                        //    this.runOnUiThread(() -> {
                        //        hideProgress();
                        //        Mensagens.mensagemErro(
                        //                ConsignadoBipagemActivity.this,
                        //                retCodBarra.getMensagem(),
                        //                false
                        //        );
                        //        newCodBarra = null;
                        //    });
                        //    return;
                        //}

                        BigInteger value = new BigInteger(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL));
                        if (value.compareTo(BigInteger.valueOf(2)) < 0) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Verifique o ICCID não é sequêncial!", false);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (CodigoBarra.retornaICCID(newCodBarra.getCodBarraInicial(), newCodBarra.getGrupoProduto()).compareTo(CodigoBarra.retornaICCID(newCodBarra.getCodBarraFinal(), newCodBarra.getGrupoProduto())) > -1) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Verifique o ICCID não é sequêncial!", false);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.iccidJaVendido(ConsignadoBipagemActivity.this, newCodBarra);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (!newCodBarra.getIdProduto().equals(pProduto.getId())) {
                            Produto produtoInicial = estoqueDao.getProdutoById(newCodBarra.getIdProduto());
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, getString(R.string.nova_bipagem_range_iccid_produto_diferente, produtoInicial.getNome()), false);
                            });
                            newCodBarra = null;
                            return;
                        }

                        listCodBarra.add(newCodBarra);
                        if (this.produto != null && this.estruturaProd != null) {
                            listCombo = getEstruturaVenda(estruturaProd.getItemFilho(), Integer.parseInt(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)), listCombo);
                            itemVendaCombo.setListItens(listCombo);
                        }

                        itemVendaCombo.setCodigosList(listCodBarra);
                        newCodBarra = null;
                        hideProgress();
                    }
                }

                if (newCodBarra == null) {
                    int quantidade = 0;
                    if (itemVendaCombo.getCodigosList() != null) {
                        try {
                            for (CodBarra items : itemVendaCombo.getCodigosList()) {
                                quantidade += Integer.parseInt(items.retornaQuantidade(UsoCodBarra.GERAL));
                            }
                        } catch (NumberFormatException e) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(ConsignadoBipagemActivity.this, "Range muito grande.", false);
                            });
                            return;
                        }
                    }

                    itemVendaCombo.setQtde(quantidade);
                    estoqueDao.incluirComboPistolagem(itemVendaCombo, consignado);
                    this.runOnUiThread(this::atualizarItens);
                }

                if (itemVendaCombo.getQtdCombo() > 0 && itemVendaCombo.getCodigosList() != null && itemVendaCombo.getCodigosList().size() == (itemVendaCombo.getQtdCombo() * quantidadeExterna)) {
                    txtCodigoBarras.setText("");
                } else {
                    this.runOnUiThread(() -> {
                        openCamera();
                        txtCodigoBarras.setText("");
                    });
                }

            } catch (Exception ex) {
                this.runOnUiThread(() -> {
                    hideProgress();
                    Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false);
                });
            }

        }).start();
    }

    private void salvarAuditagem() {
        mostrarCarregando();
        new Thread(() -> {
            try {

                // Para o caso de não ter bipado nenhum código de barras do Produto
                // será criado o registro na tabela de PistolagemComboTemp apenas para informar
                // que foi feita a auditoria, mas que o cliente não possuia nenhum produto em estoque
                if (listFinal == null || listFinal.size() == 0) {
                    ItemVendaCombo itemVendaCombo = new ItemVendaCombo();
                    ArrayList<CodBarra> listCodBarra = null;
                    itemVendaCombo.setQtdCombo(consignadoItem.getQtd());
                    itemVendaCombo.setIdProduto(produto.getId());
                    itemVendaCombo.setNomeProduto(produto.getNome());
                    itemVendaCombo.setCombo(false);
                    itemVendaCombo.setValorUN(produto.getPrecovenda());
                    itemVendaCombo.setQtde(0);
                    estoqueDao.incluirComboPistolagem(itemVendaCombo, consignado);
                } else {
                    ItemVendaCombo itemVendaCombo = estoqueDao.getPistolagem(consignado, consignadoItem.getIdProduto(), "", 0);

                    ArrayList<CodBarra> listCodBarra = new ArrayList<>();
                    // Quantidade de Codigos de Barras Informados
                    int quantidade = 0;
                    for (ItemVendaCombo item : listFinal) {
                        for (CodBarra items : item.getCodigosList()) {
                            listCodBarra.add(items);
                            quantidade += Integer.parseInt(items.retornaQuantidade(UsoCodBarra.GERAL));
                        }
                    }

                    int qtdVendido = (consignadoItem.getQtd() - quantidade);
                    itemVendaCombo.setQtdCombo(qtdVendido);
                    itemVendaCombo.setQtde(quantidade);
                    itemVendaCombo.setCodigosList(listCodBarra);
                    estoqueDao.incluirComboPistolagem(itemVendaCombo, consignado);
                }

                // Momento em que define a Pistolagem como Finalizada
                estoqueDao.confirmarPistolagensConsignadoAuditagem(consignado, produto);
                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
            } catch (Exception ex) {
                runOnUiThread(() -> Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false));
            }

            runOnUiThread(this::ocultarCarregando);

        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                runOnUiThread(() -> getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE));
                new Handler().postDelayed(() -> {
                    runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
                    if (vOperacao.equals("Auditagem"))
                        VerificaInclusaoAuditagem(result.getContents());
                    else
                        verificaInclusaoCodBarra(result.getContents());
                }, 1000);
            }
        } catch (Exception ex) {
            runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
            Mensagens.mensagemErro(ConsignadoBipagemActivity.this, ex.getMessage(), false);
        }
    }

}
