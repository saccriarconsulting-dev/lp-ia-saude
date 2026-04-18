package com.axys.redeflexmobile.ui.venda.bipagem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.CodBarraAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDaoImpl;
import com.axys.redeflexmobile.shared.dao.IccidDao;
import com.axys.redeflexmobile.shared.dao.IccidDaoImpl;
import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.dao.PrecoDaoImpl;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.dao.VendaDaoImpl;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.RetCodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.PrecoDiferenciadoValidadorImpl;
import com.axys.redeflexmobile.shared.util.PrecoDiferenciadoValidadorImpl.PrecoVendido;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemActivity;
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

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;


public class BipagemVendaActivity extends AppCompatActivity
        implements CodBarraAdapter.ICodBarraAdapterListenner {

    public static final int BUTTON_WAIT_DURATION = 2200;

    private EditText txtCodigoBarras;
    private ImageButton btnAddCodigo;
    private LinearLayout btnLerCodigoBarra;

    private Produto produto;
    private Iccid iccid;
    private CodBarra newCodBarra;
    private Venda venda;
    private Visita visita;
    private EstruturaProd estruturaProd;
    private ArrayList<EstruturaProd> itensEstruturaProd;

    private ArrayList<ItemVendaCombo> listFinal;
    private CodBarraAdapter mAdapter;
    private ExpandableListView lsICCIDs;

    private IccidDao iccidDao;
    private EstoqueDao estoqueDao;
    private VendaDao vendaDao;
    private PrecoDao precoDao;
    private String idVendaItemExterno;
    private int quantidadeExternaBipada;
    private int quantidadeExterna;
    private int quantidadeExternaCombo;
    private int quantidadeExternaComboBipada;
    private boolean isComboExterno;
    private String comboIdExterno;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ProgressDialog mDialog;

    @Override
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
            isComboExterno = bundle.getBoolean(Config.PRODUTO_COMBO, false);
            comboIdExterno = bundle.getString(Config.PRODUTO_COMBO_ID);

            quantidadeExternaComboBipada = bundle.getInt(Config.QTD_ITEM_COMBO_LIDO_VENDA, 0);
            quantidadeExternaCombo = bundle.getInt(Config.QTD_ITEM_COMBO_VENDA, 0);

            if (!Util_IO.isNullOrEmpty(bundle.getString(Config.CODIGO_ITEM_VENDA))) {
                idVendaItemExterno = bundle.getString(Config.CODIGO_ITEM_VENDA);
            }
        }

        criarEventos();
        lsICCIDs.setEmptyView(findViewById(R.id.empty));

        try {
            visita = Utilidades.getVisita(BipagemVendaActivity.this);
            if (visita == null) {
                Mensagens.visitaNaoIniciada(BipagemVendaActivity.this);
                return;
            }

            venda = Utilidades.getVenda(BipagemVendaActivity.this, visita);
            atualizarItens();

            removerPistolagemDivergente();
        } catch (Exception ex) {
            Mensagens.mensagemErro(BipagemVendaActivity.this, ex.getMessage(), true);
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

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                runOnUiThread(() -> getWindow().setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE));
                new Handler().postDelayed(() -> {
                    runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
                    verificaInclusaoCodBarra(result.getContents());
                }, 1000);
            }
        } catch (Exception ex) {
            runOnUiThread(() -> getWindow().clearFlags(FLAG_NOT_TOUCHABLE));
            Mensagens.mensagemErro(BipagemVendaActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void removeGroupItem(ArrayList<ItemVendaCombo> lista, int groupPosition) {
        Alerta alerta = new Alerta(
                BipagemVendaActivity.this,
                getString(R.string.app_name),
                getString(R.string.bipagem_venda_dialog_remover_mensagem)
        );
        alerta.showConfirm((dialog, which) -> {
            try {
                estoqueDao.deletarPistolagemByComboId(lista.get(groupPosition).getId());

                atualizarItens();
            } catch (Exception ex) {
                Mensagens.mensagemErro(
                        BipagemVendaActivity.this,
                        ex.getMessage(),
                        false
                );
            }
        }, null);
    }

    @Override
    public void removeChildItem(ArrayList<ItemVendaCombo> lista, CodBarra codBarra,
                                int groupPosition, int childPosition) {
        Alerta alerta = new Alerta(
                BipagemVendaActivity.this,
                getString(R.string.app_name),
                getString(R.string.bipagem_venda_dialog_remover_mensagem)
        );
        alerta.showConfirm((dialog, which) -> {
            try {
                DBEstoque dbEstoque = new DBEstoque(BipagemVendaActivity.this);
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
                Mensagens.mensagemErro(
                        BipagemVendaActivity.this,
                        ex.getMessage(),
                        false
                );
            }
        }, null);
    }

    private void salvar() {
        mostrarCarregando();
        new Thread(() -> {
            try {

                if (listFinal == null || listFinal.size() == 0) {
                    runOnUiThread(() -> Mensagens.nenhumItemIncluido(BipagemVendaActivity.this));
                    runOnUiThread(this::ocultarCarregando);
                    return;
                }

                PrecoDiferenciadoValidadorImpl precoDiferenciadoValidador = new PrecoDiferenciadoValidadorImpl(precoDao, vendaDao);
                boolean precoValido = precoDiferenciadoValidador.validarPrecoDiferenciado(listFinal, venda);
                if (!precoValido) {
                    runOnUiThread(() -> {
                        PrecoVendido precoVendido = precoDiferenciadoValidador.getPrecoVendido();
                        if (precoVendido == null) {
                            return;
                        }
                        Mensagens.bonificadoAteTantasUnds(
                                BipagemVendaActivity.this,
                                precoVendido.getNomeProduto(),
                                Util_IO.formatDoubleToDecimalNonDivider(precoVendido.getValorDiferenca()),
                                precoVendido.getLimiteVenda(),
                                precoVendido.getQuantidadeDiferenciada()
                        );
                    });
                    runOnUiThread(this::ocultarCarregando);
                    return;
                }

                boolean bValidacaoOK = false;
                for (ItemVendaCombo item : listFinal) {
                    Produto produto = estoqueDao.getProdutoById(item.getIdProduto());
                    PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0)
                            ? precoDao.getPrecoById(String.valueOf(item.getIdPreco()))
                            : null;

                    produto.setPrecovenda(item.getValorUN());
                    ItemVenda tempQuantidade = vendaDao.getItemVendaById(Integer.parseInt(idVendaItemExterno));
                    produto.setQtde(tempQuantidade.getQtde());

                    bValidacaoOK = Validacoes.validacoesPreAddICCID(BipagemVendaActivity.this,
                            produto,
                            precoDiferenciado,
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
                        PrecoDiferenciado precoDiferenciadoInterno = (item.getIdPreco() > 0)
                                ? precoDao.getPrecoById(String.valueOf(item.getIdPreco()))
                                : null;

                        produto.setPrecovenda(item.getValorUN());
                        ItemVenda tempQuantidade = vendaDao.getItemVendaById(
                                Integer.parseInt(idVendaItemExterno)
                        );
                        produto.setQtde(tempQuantidade.getQtde());

                        bValidacaoOK = Validacoes.validacoesPreAddICCID(BipagemVendaActivity.this,
                                produto,
                                precoDiferenciadoInterno,
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

                        if (produto.getQtdCombo() == 0) {
                            quantidade += quantidadeExternaBipada;
                            int tempoQuantidadeCombo = validarCombo(quantidade);
                            if (quantidade > quantidadeExterna) {
                                int finalQuantidade = quantidade;
                                PedidoVendaQuantidadeDialog dialog = PedidoVendaQuantidadeDialog.newInstance(
                                        quantidadeExterna,
                                        quantidade,
                                        () -> pedidoQuantidadeEvento(item, produto, tempoQuantidadeCombo, finalQuantidade)
                                );

                                runOnUiThread(() -> {
                                    ocultarCarregando();
                                    dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
                                });
                                return;
                            }
                            produto.setQtde(quantidade);
                        }

                        if (venda == null) {
                            venda = Utilidades.getVenda(BipagemVendaActivity.this, visita);
                        }

                        boolean comboTemp = estoqueDao.isProdutoCombo(item.getIdProduto());
                        if (comboTemp) {
                            ComboVenda comboVenda = new ComboVenda();
                            comboVenda.setIdProduto(item.getCodigosList().get(0).getIdProduto());
                            comboVenda.setQuantidade(produto.getQtde());
                            item.getListItens().add(comboVenda);
                        }
                        PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0)
                                ? precoDao.getPrecoById(String.valueOf(item.getIdPreco()))
                                : null;
                        Utilidades.addICCIDItensVenda(
                                BipagemVendaActivity.this,
                                produto,
                                item.getQtde(),
                                item.getValorUN(),
                                venda,
                                precoDiferenciado,
                                (ArrayList<CodBarra>) item.getCodigosList(),
                                item.getListItens(),
                                Long.parseLong(idVendaItemExterno),
                                false
                        );
                    }

                    estoqueDao.confirmarPistolagens(venda);
                    runOnUiThread(() -> {
                        setResult(RESULT_OK);
                        finish();
                    });
                }

            } catch (Exception ex) {
                runOnUiThread(() -> Mensagens.mensagemErro(BipagemVendaActivity.this, ex.getMessage(), false));
            }

            runOnUiThread(this::ocultarCarregando);

        }).start();
    }

    private void pedidoQuantidadeEvento(final ItemVendaCombo item, final Produto produto,
                                        final int tempoQuantidadeCombo, final int finalQuantidade) {
        try {
            produto.setQtde(finalQuantidade);
            if (isComboExterno && !item.getCodigosList().isEmpty()) {
                EstruturaProd formacaoCombo = estoqueDao.obterEstruturaPeloPaiEFilho(comboIdExterno, produto.getId());
                int novaQuantidadeCombo = (int) Math.ceil((float) finalQuantidade / formacaoCombo.getQtd());
                produto.setQtde(novaQuantidadeCombo);
            }
            if (venda == null) {
                venda = Utilidades.getVenda(
                        BipagemVendaActivity.this,
                        visita
                );
            }

            boolean comboTemp = estoqueDao.isProdutoCombo(item.getIdProduto());
            if (comboTemp) {
                ComboVenda comboVenda = new ComboVenda();
                comboVenda.setIdProduto(item.getCodigosList().get(0).getIdProduto());
                comboVenda.setQuantidade(produto.getQtde());
                item.getListItens().add(comboVenda);
            }

            ItemVendaCombo itemExterno = (ItemVendaCombo) vendaDao.getItemVendaById(Integer.parseInt(idVendaItemExterno));
            PrecoDiferenciado pre = precoDao.getPrecoById(String.valueOf(itemExterno.getIdPreco()));
            PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0)
                    ? precoDao.getPrecoById(String.valueOf(item.getIdPreco()))
                    : null;
            int quantidadePreco = pre != null
                    ? vendaDao.retornaQtdPrecoDiferenciado(String.valueOf(itemExterno.getIdPreco()))
                    : 0;
            if (!isComboExterno) {
                quantidadePreco += (produto.getQtde() - quantidadeExterna);
            }
            if (isComboExterno) {
                quantidadePreco += (produto.getQtde() - (quantidadeExternaCombo - quantidadeExternaComboBipada));
            }
            if (pre != null && quantidadePreco > pre.getQtdPreco()) {
                String msg = getString(
                        R.string.pedido_venda_alerta_promocao_quantidade,
                        String.valueOf(pre.getQtdPreco())
                );
                runOnUiThread(() -> {
                    ocultarCarregando();
                    Alerta alerta = new Alerta(
                            BipagemVendaActivity.this,
                            getString(R.string.app_name),
                            msg
                    );
                    alerta.show();
                });
                return;
            }

            if (tempoQuantidadeCombo != -1) {
                Utilidades.atualizarQuantidadeCombo(
                        BipagemVendaActivity.this,
                        tempoQuantidadeCombo,
                        idVendaItemExterno
                );
            }

            Utilidades.addICCIDItensVenda(
                    BipagemVendaActivity.this,
                    produto,
                    item.getQtde(),
                    item.getValorUN(),
                    venda,
                    precoDiferenciado,
                    (ArrayList<CodBarra>) item.getCodigosList(),
                    item.getListItens(),
                    Long.parseLong(idVendaItemExterno),
                    true
            );

            estoqueDao.confirmarPistolagens(venda);
            runOnUiThread(() -> {
                ocultarCarregando();
                setResult(RESULT_OK);
                finish();
            });
        } catch (Exception e) {
            Timber.e(e);
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

    private void criarObjetos() {
        txtCodigoBarras = findViewById(R.id.txtCodigoBarras);
        btnAddCodigo = findViewById(R.id.btnAddCodigo);
        btnLerCodigoBarra = findViewById(R.id.btnLerCodigoBarra);
        lsICCIDs = findViewById(R.id.expandableICCIDs);

        iccidDao = new IccidDaoImpl(BipagemVendaActivity.this);
        estoqueDao = new EstoqueDaoImpl(BipagemVendaActivity.this);
        vendaDao = new VendaDaoImpl(BipagemVendaActivity.this);
        precoDao = new PrecoDaoImpl(BipagemVendaActivity.this);
    }

    private void criarEventos() {
        compositeDisposable.add(
                RxView.clicks(btnAddCodigo)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> {
                            String cod = txtCodigoBarras.getText().toString().trim();
                            verificaInclusaoCodBarra(cod);
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
            if (txtCodigoBarras != null) txtCodigoBarras.setText("");
            CodeReader.openCodeReader(BipagemVendaActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(BipagemVendaActivity.this, ex.getMessage(), false);
        }
    }

    private void verificaInclusaoCodBarra(final String pCodBarra) {
        if (Util_IO.isNullOrEmpty(pCodBarra)) {
            Mensagens.codigoBarraNaoInformado(BipagemVendaActivity.this, (dialog, which) -> openCamera());
            return;
        }

        iccid = iccidDao.getByCodigo(pCodBarra);
        if (iccid == null) {
            Alerta alerta = new Alerta(
                    BipagemVendaActivity.this,
                    pCodBarra,
                    getString(R.string.bipagem_venda_dialog_iccid_inexistente_mensagem)
            );
            alerta.show((dialog, which) -> openCamera());
            return;
        }

        if (!iccid.getItemCode().equalsIgnoreCase(produto.getId())) {
            Alerta alerta = new Alerta(
                    BipagemVendaActivity.this,
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

        if (this.produto != null && !itensEstruturaProd.isEmpty()) {
            if (this.produto.getAtivo().equalsIgnoreCase("N")) {
                Alerta alerta = new Alerta(
                        BipagemVendaActivity.this,
                        this.produto.getNome(),
                        getString(R.string.bipagem_venda_dialog_produto_inativo_mensagem)
                );
                alerta.show((dialog, which) -> openCamera());
                return;
            }

            estruturaProd = null;

            itensEstruturaProd = estoqueDao.getEstruturaByItemPai(this.produto.getId());
            if (itensEstruturaProd != null && !itensEstruturaProd.isEmpty()) {
                for (EstruturaProd item : itensEstruturaProd) {
                    if (item.getItemFilho().equalsIgnoreCase(iccid.getItemCode())) {
                        estruturaProd = item;
                        break;
                    }
                }
            }

            if (estruturaProd == null) {
                Mensagens.codigoBarraNaoVinculadoParaEsteProduto(BipagemVendaActivity.this, pCodBarra, this.produto.getNome(), (dialog, which) -> openCamera());
                return;
            }

            produto = estoqueDao.getProdutoById(estruturaProd.getItemFilho());
            if (produto == null) {
                Alerta alerta = new Alerta(
                        BipagemVendaActivity.this,
                        pCodBarra,
                        getString(R.string.bipagem_venda_dialog_produto_inexistente_mensagem, estruturaProd.getItemFilho())
                );
                alerta.show((dialog, which) -> openCamera());
                return;
            }
        } else {
            estruturaProd = null;
            produto = estoqueDao.getProdutoById(iccid.getItemCode());
            if (produto == null) {
                Alerta alerta = new Alerta(
                        BipagemVendaActivity.this,
                        pCodBarra,
                        getString(R.string.bipagem_venda_dialog_produto_inexistente_mensagem, iccid.getItemCode())
                );
                alerta.show((dialog, which) -> openCamera());
                return;
            }
        }

        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !pCodBarra.startsWith(produto.getIniciaCodBarra())) {
            Alerta alerta = new Alerta(BipagemVendaActivity.this, pCodBarra, "Código de Barra deve começar com: " + produto.getIniciaCodBarra());
            alerta.show((dialog, which) -> openCamera());
        } else if (produto.getQtdCodBarra() > 0 && pCodBarra.trim().length() != produto.getQtdCodBarra()) {
            Alerta alerta = new Alerta(BipagemVendaActivity.this, pCodBarra, "Código de Barra deve conter " + produto.getQtdCodBarra() + " caracteres");
            alerta.show((dialog, which) -> openCamera());
        } else {
            Alerta alerta = new Alerta(BipagemVendaActivity.this, pCodBarra, "Deseja incluir esse código de barra?");
            alerta.showConfirm((dialog, which) -> {
                if (newCodBarra == null && produto.getQtdCombo() == 0) {
                    Alerta alerta1 = new Alerta(BipagemVendaActivity.this, pCodBarra, "Código de barra unitário?");
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

    private void addCodBarra(Produto pProduto, boolean pIndividual, String pCodigo, boolean pInicial) {
        new Thread(() -> {

            try {

                if (listFinal == null) {
                    listFinal = new ArrayList<>();
                }

                ArrayList<CodBarra> listValidacao = new ArrayList<>();
                String codigoProd = this.produto != null
                        ? this.produto.getId()
                        : pProduto.getId();

                Preco preco = new Preco();
                preco.setIdPreco("");
                preco.setValor(pProduto.getPrecovenda());

                ItemVendaCombo itemVendaCombo = estoqueDao.getPistolagem(venda, codigoProd, preco.getIdPreco());
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
                    }

                    if (itemVendaCombo.getListItens() != null) {
                        listCombo = itemVendaCombo.getListItens();
                    }

                }
                else {
                    itemVendaCombo = new ItemVendaCombo();
                    itemVendaCombo.setQtdCombo(pProduto.getQtdCombo());

                    if (this.produto != null && this.estruturaProd != null) {
                        itemVendaCombo.setIdProduto(this.produto.getId());
                        itemVendaCombo.setNomeProduto(this.produto.getNome());
                        itemVendaCombo.setCombo(true);
                    } else {
                        itemVendaCombo.setIdProduto(this.iccid.getItemCode());
                        itemVendaCombo.setNomeProduto(pProduto.getNome());
                        itemVendaCombo.setCombo(false);
                    }

                    if (!Util_IO.isNullOrEmpty(preco.getIdPreco())) {
                        itemVendaCombo.setIdPreco(Integer.parseInt(preco.getIdPreco()));
                    }
                    itemVendaCombo.setValorUN(preco.getValor());
                }

                if (listCodBarra != null) {
                    listValidacao.addAll(listCodBarra);
                }

                listValidacao.addAll(Utilidades.getCodBarraItensByVendidos(this));

                if (pIndividual) {

                    newCodBarra = new CodBarra();
                    newCodBarra.setIdVendaItem(idVendaItemExterno);
                    newCodBarra.setIndividual(true);
                    newCodBarra.setGrupoProduto(pProduto.getGrupo());
                    newCodBarra.setCodBarraInicial(pCodigo);

                    if (this.produto != null && this.estruturaProd != null) {
                        newCodBarra.setIdProduto(this.estruturaProd.getItemFilho());
                    } else {
                        newCodBarra.setIdProduto(this.iccid.getItemCode());
                    }

                    if (listCodBarra == null) {
                        listCodBarra = new ArrayList<>();
                    }

                    if (vendaDao.iccidVendido(newCodBarra)) {
                        this.runOnUiThread(() -> {
                            Mensagens.iccidJaVendido(BipagemVendaActivity.this, newCodBarra);
                            newCodBarra = null;
                            hideProgress();
                        });
                        return;
                    }

                    if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                        this.runOnUiThread(() -> {
                            Mensagens.iccidJaVendido(BipagemVendaActivity.this, newCodBarra);
                            newCodBarra = null;
                            hideProgress();
                        });
                        return;
                    }

                    for (ItemVendaCombo item : listFinal) {
                        if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, (ArrayList<CodBarra>) item.getCodigosList())) {
                            this.runOnUiThread(() -> {
                                Mensagens.iccidJaVendido(BipagemVendaActivity.this, newCodBarra);
                                newCodBarra = null;
                                hideProgress();
                            });
                            return;
                        }
                    }

                    listCodBarra.add(newCodBarra);
                    if (this.produto != null && this.estruturaProd != null) {
                        listCombo = getEstruturaVenda(estruturaProd.getItemFilho(), Integer.parseInt(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)), listCombo);
                        itemVendaCombo.setListItens(listCombo);
                    }

                    itemVendaCombo.setCodigosList(listCodBarra);
                    newCodBarra = null;

                }
                else {
                    if (newCodBarra == null) {
                        newCodBarra = new CodBarra();
                        newCodBarra.setIdVendaItem(idVendaItemExterno);
                        newCodBarra.setIndividual(false);
                        newCodBarra.setGrupoProduto(pProduto.getGrupo());

                        if (this.produto != null && this.estruturaProd != null) {
                            newCodBarra.setIdProduto(this.estruturaProd.getItemFilho());
                        } else {
                            newCodBarra.setIdProduto(this.iccid.getItemCode());
                        }
                    }

                    if (listCodBarra == null) {
                        listCodBarra = new ArrayList<>();
                    }

                    if (pInicial) {
                        newCodBarra.setCodBarraInicial(pCodigo);
                    } else {

                        showProgress();

                        newCodBarra.setCodBarraFinal(pCodigo);

                        if (vendaDao.iccidVendido(newCodBarra)) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.iccidJaVendido(BipagemVendaActivity.this, newCodBarra);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (!CodigoBarra.validaQuantidadeRange(newCodBarra)) {
                            this.runOnUiThread(() -> {
                                Alerta alerta = new Alerta(BipagemVendaActivity.this,
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

                        RetCodBarra retCodBarra = CodigoBarra.validacao(
                                pCodigo,
                                produto,
                                Integer.parseInt(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)),
                                newCodBarra,
                                listValidacao,
                                pIndividual,
                                true,
                                BipagemVendaActivity.this
                        );
                        if (!retCodBarra.isInclusaoOK()) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(
                                        BipagemVendaActivity.this,
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
                                Mensagens.mensagemErro(BipagemVendaActivity.this, "Verifique o ICCID não é sequêncial!", false);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (CodigoBarra.retornaICCID(newCodBarra.getCodBarraInicial(), newCodBarra.getGrupoProduto()).compareTo(CodigoBarra.retornaICCID(newCodBarra.getCodBarraFinal(), newCodBarra.getGrupoProduto())) > -1) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(BipagemVendaActivity.this, "Verifique o ICCID não é sequêncial!", false);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.iccidJaVendido(BipagemVendaActivity.this, newCodBarra);
                                newCodBarra = null;
                            });
                            return;
                        }

                        if (!newCodBarra.getIdProduto().equals(pProduto.getId())) {
                            Produto produtoInicial = estoqueDao.getProdutoById(newCodBarra.getIdProduto());
                            this.runOnUiThread(() -> {
                                hideProgress();
                                Mensagens.mensagemErro(BipagemVendaActivity.this, getString(R.string.nova_bipagem_range_iccid_produto_diferente, produtoInicial.getNome()), false);
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
                                Mensagens.mensagemErro(BipagemVendaActivity.this, "Range muito grande.", false);
                            });
                            return;
                        }
                    }

                    //Combo
                    ProdutoCombo produtoCombo = null;
                    if (itemVendaCombo.isCombo() || itemVendaCombo.getQtdCombo() > 0) {

                        int qtdCombo = itemVendaCombo.getQtdCombo();
                        if (itemVendaCombo.isCombo()) {
                            qtdCombo = CodigoBarra.retornaQtCombo(this.produto.getId(), BipagemVendaActivity.this);
                        }

                        if (qtdCombo == 0) {
                            throw new Exception("Estrutura de produto " + this.produto.getId() + " não localizado, Verifique com o Departamento de TI");
                        }

                        itemVendaCombo.setQtdCombo(qtdCombo);
                        produtoCombo = CodigoBarra.retornaCombo(itemVendaCombo.getQtdCombo(), itemVendaCombo.getCodigosList(), UsoCodBarra.GERAL);
                    }

                    itemVendaCombo.setQtde(produtoCombo != null
                            ? produtoCombo.getQtdTotal()
                            : quantidade);

                    estoqueDao.incluirComboPistolagem(itemVendaCombo, venda);
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
                    Mensagens.mensagemErro(BipagemVendaActivity.this, ex.getMessage(), false);
                });
            }

        }).start();
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

    private ArrayList<ComboVenda> getEstruturaVenda(String pIdProduto, int pQtd, ArrayList<ComboVenda> pLista) {
        if (pLista == null)
            pLista = new ArrayList<>();

        ComboVenda comboVenda = new ComboVenda();
        comboVenda.setIdProduto(pIdProduto);
        comboVenda.setQuantidade(pQtd);

        pLista.add(comboVenda);

        return pLista;
    }

    private void atualizarItens() {
        listFinal = (ArrayList<ItemVendaCombo>) estoqueDao.getPistolagensComboNaoFinalizada(venda);

        mAdapter = new CodBarraAdapter(listFinal, BipagemVendaActivity.this, this, quantidadeExterna);
        lsICCIDs.setEmptyView(findViewById(R.id.empty));
        lsICCIDs.setAdapter(mAdapter);
        for (int i = 0; i < listFinal.size(); i++) {
            if (!listFinal.get(i).getCodigosList().isEmpty()) {
                lsICCIDs.expandGroup(i);
            }
        }
    }

    private void removerPistolagemDivergente() {
        List<ItemVendaCombo> lista = Stream.of(listFinal)
                .filter(value -> !value.getIdProduto().equals(produto.getId()))
                .toList();
        if (lista.isEmpty()) {
            return;
        }
        Alerta alerta = new Alerta(
                this,
                getString(R.string.app_titulo),
                getString(R.string.bipagem_venda_dialog_iccid_invalido, produto.getNome())
        );
        alerta.showConfirm((dialog, which) -> {
            estoqueDao.deletarPistolagensComboNaoFinalizadas(venda);
            atualizarItens();
        }, (dialog, which) -> finish());
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
                                if (venda != null)
                                    estoqueDao.deletarPistolagensComboNaoFinalizadas(venda);
                            } catch (Exception ex) {
                                Utilidades.retornaMensagem(BipagemVendaActivity.this, ex.getMessage(), false);
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
                                salvar();
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

    private int validarCombo(int quantidade) {
        if (!isComboExterno) {
            return -1;
        }

        List<EstruturaProd> listaEstrutura = estoqueDao.getEstruturaByItemPai(comboIdExterno);
        EstruturaProd estrutura = Stream.of(listaEstrutura)
                .filter(value -> value.getItemFilho().equalsIgnoreCase(produto.getId()))
                .findFirst()
                .orElse(null);

        float baseCombo = (float) quantidade / (float) estrutura.getQtd();
        return (int) Math.ceil(baseCombo);
    }
}