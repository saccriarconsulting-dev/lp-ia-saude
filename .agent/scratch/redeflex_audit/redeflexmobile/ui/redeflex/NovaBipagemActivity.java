package com.axys.redeflexmobile.ui.redeflex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.CodBarraAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodeReader;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.google.zxing.integration.android.IntentResult;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public class NovaBipagemActivity extends AppCompatActivity implements CodBarraAdapter.ICodBarraAdapterListenner {

    public static final int BUTTON_WAIT_DURATION = 2200;
    EditText txtCodigoBarras;
    ImageButton btnAddCodigo;
    LinearLayout btnLerCodigoBarra;

    Produto produto;
    Iccid iccid;
    CodBarra newCodBarra;
    Venda venda;
    Visita visita;
    EstruturaProd estruturaProd;
    ArrayList<EstruturaProd> itensEstruturaProd;

    ArrayList<ItemVendaCombo> listFinal;
    CodBarraAdapter mAdapter;
    ExpandableListView lsICCIDs;

    DBIccid dbIccid;
    DBEstoque dbEstoque;
    DBVenda dbVenda;
    DBPreco dbPreco;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_bipagem);

        criarToolbar();
        criarObjetos();

        Bundle bundle = getIntent().getExtras();
        produto = null;

        if (bundle != null && !Util_IO.isNullOrEmpty(bundle.getString(Config.CodigoProduto))) {
            produto = dbEstoque.getProdutoById(bundle.getString(Config.CodigoProduto));
            mudarTitulo("Pistolagem: " + produto.getId());
        }

        criarEventos();
        lsICCIDs.setEmptyView(findViewById(R.id.empty));

        try {

            visita = Utilidades.getVisita(NovaBipagemActivity.this);
            if (visita == null) {
                Mensagens.visitaNaoIniciada(NovaBipagemActivity.this);
                return;
            }

            venda = Utilidades.getVenda(NovaBipagemActivity.this, visita);
            atualizarItens();

        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaBipagemActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
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
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                try {
//                    if (venda != null)
//                        dbEstoque.deletarPistolagensComboNaoFinalizadas(venda);
//                } catch (Exception ex) {
//                    Utilidades.retornaMensagem(NovaBipagemActivity.this, ex.getMessage(), false);
//                }
//                setResult(RESULT_CANCELED);
//                finish();
//                break;
//            case R.id.opcao_salvar:
//                salvar();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = CodeReader.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null)
                verificaInclusaoCodBarra(result.getContents());
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaBipagemActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void removeGroupItem(ArrayList<ItemVendaCombo> lista, int groupPosition) {
        Alerta alerta = new Alerta(NovaBipagemActivity.this, getResources().getString(R.string.app_name), "Deseja excluir esse item?");
        alerta.showConfirm((dialog, which) -> {
            try {
                new DBEstoque(NovaBipagemActivity.this)
                        .deletarPistolagemByComboId(lista.get(groupPosition).getId());

                atualizarItens();
            } catch (Exception ex) {
                Mensagens.mensagemErro(NovaBipagemActivity.this, ex.getMessage(), false);
            }
        }, null);
    }

    @Override
    public void removeChildItem(ArrayList<ItemVendaCombo> lista, CodBarra codBarra, int groupPosition, int childPosition) {
        Alerta alerta = new Alerta(NovaBipagemActivity.this, getResources().getString(R.string.app_name), "Deseja excluir esse item?");
        alerta.showConfirm((dialog, which) -> {
            DBEstoque dbEstoque = new DBEstoque(NovaBipagemActivity.this);
            DBVenda dbVenda = new DBVenda(NovaBipagemActivity.this);
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
        }, null);
    }

    private void salvar() {
        mostrarCarregando();
        new Thread(() -> {
            try {
                if (listFinal == null || listFinal.size() == 0) {
                    runOnUiThread(() -> Mensagens.nenhumItemIncluido(NovaBipagemActivity.this));
                    ocultarCarregando();
                    return;
                }

                boolean isValid = Validacoes.validacoesPrecoBonificado(NovaBipagemActivity.this, listFinal, venda);
                if (!isValid) {
                    ocultarCarregando();
                    return;
                }

                boolean bValidacaoOK = false;
                for (ItemVendaCombo item : listFinal) {
                    Produto produto = dbEstoque.getProdutoById(item.getIdProduto());
                    PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0) ? dbPreco.getPrecoById(String.valueOf(item.getIdPreco())) : null;

                    produto.setPrecovenda(item.getValorUN());
                    bValidacaoOK = Validacoes.validacoesPreAddICCID(NovaBipagemActivity.this,
                            produto,
                            precoDiferenciado,
                            (ArrayList<CodBarra>) item.getCodigosList(),
                            item.isCombo(),
                            item.getListItens(),
                            UsoCodBarra.GERAL);
                    if (!bValidacaoOK) {
                        break;
                    }
                }

                if (bValidacaoOK) {
                    for (ItemVendaCombo item : listFinal) {
                        if (venda == null)
                            venda = Utilidades.getVenda(NovaBipagemActivity.this, visita);
                        Produto produto = dbEstoque.getProdutoById(item.getIdProduto());
                        PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0) ? dbPreco.getPrecoById(String.valueOf(item.getIdPreco())) : null;
                        Utilidades.addICCIDItensVenda(NovaBipagemActivity.this, produto, item.getQtde(), item.getValorUN(), venda, precoDiferenciado, (ArrayList<CodBarra>) item.getCodigosList(), item.getListItens());
                    }
                    dbEstoque.confirmarPistolagens(venda);
                    runOnUiThread(() -> {
                        ocultarCarregando();
                        setResult(RESULT_OK);
                        finish();
                    });
                }

            } catch (Exception ex) {
                runOnUiThread(() -> Mensagens.mensagemErro(NovaBipagemActivity.this, ex.getMessage(), false));
            }

            ocultarCarregando();
        }).start();
    }

    private void mostrarCarregando() {
        progressDialog = ProgressDialog.show(
                this,
                getString(R.string.app_name),
                "Validando informações, aguarde...",
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
        dbIccid = new DBIccid(NovaBipagemActivity.this);
        dbEstoque = new DBEstoque(NovaBipagemActivity.this);
        lsICCIDs = findViewById(R.id.expandableICCIDs);
        dbVenda = new DBVenda(NovaBipagemActivity.this);
        dbPreco = new DBPreco(NovaBipagemActivity.this);
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
            CodeReader.openCodeReader((Activity) NovaBipagemActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaBipagemActivity.this, ex.getMessage(), false);
        }
    }

    private void verificaInclusaoCodBarra(final String pCodBarra) {
        if (Util_IO.isNullOrEmpty(pCodBarra)) {
            Mensagens.codigoBarraNaoInformado(NovaBipagemActivity.this, (dialog, which) -> {
                openCamera();
            });
            return;
        }

        iccid = dbIccid.getByCodigo(pCodBarra);
        if (iccid == null) {
            Alerta alerta = new Alerta(NovaBipagemActivity.this, pCodBarra, "Código de barra não está disponível, Entrar em contato com o setor de logística!");
            alerta.show((dialog, which) -> {
                openCamera();
            });
            return;
        }

        final Produto produto;
        if (this.produto != null) {
            if (this.produto.getAtivo().equalsIgnoreCase("N")) {
                Alerta alerta = new Alerta(NovaBipagemActivity.this, this.produto.getNome(), "Produto indisponível, Entrar em contato com o setor de logística!");
                alerta.show((dialog, which) -> {
                    openCamera();
                });
                return;
            }

            estruturaProd = null;

            itensEstruturaProd = dbEstoque.getEstruturaByItemPai(this.produto.getId());
            if (itensEstruturaProd != null && !itensEstruturaProd.isEmpty()) {
                for (EstruturaProd item : itensEstruturaProd) {
                    if (item.getItemFilho().equalsIgnoreCase(iccid.getItemCode())) {
                        estruturaProd = item;
                        break;
                    }
                }
            }

            if (estruturaProd == null) {
                Mensagens.codigoBarraNaoVinculadoParaEsteProduto(NovaBipagemActivity.this, pCodBarra, this.produto.getNome(), (dialog, which) -> openCamera());
                return;
            }

            produto = dbEstoque.getProdutoById(estruturaProd.getItemFilho());
            if (produto == null) {
                Alerta alerta = new Alerta(NovaBipagemActivity.this, pCodBarra, estruturaProd.getItemFilho() + " - produto não está disponível, Entrar em contato com o setor de logística!");
                alerta.show((dialog, which) -> {
                    openCamera();
                });
                return;
            }
        } else {
            estruturaProd = null;
            produto = dbEstoque.getProdutoById(iccid.getItemCode());
            if (produto == null) {
                Alerta alerta = new Alerta(NovaBipagemActivity.this, pCodBarra, iccid.getItemCode() + " - produto não está disponível, Entrar em contato com o setor de logística!");
                alerta.show((dialog, which) -> {
                    openCamera();
                });
                return;
            }
        }

        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !pCodBarra.startsWith(produto.getIniciaCodBarra())) {
            Alerta alerta = new Alerta(NovaBipagemActivity.this, pCodBarra, "Código de Barra deve começar com: " + produto.getIniciaCodBarra());
            alerta.show((dialog, which) -> openCamera());
        } else if (produto.getQtdCodBarra() > 0 && pCodBarra.trim().length() != produto.getQtdCodBarra()) {
            Alerta alerta = new Alerta(NovaBipagemActivity.this, pCodBarra, "Código de Barra deve conter " + produto.getQtdCodBarra() + " caracteres");
            alerta.show((dialog, which) -> openCamera());
        } else {
            Alerta alerta = new Alerta(NovaBipagemActivity.this, pCodBarra, "Deseja incluir esse código de barra?");
            alerta.showConfirm((dialog, which) -> {
                if (newCodBarra == null) {
                    Alerta alerta1 = new Alerta(NovaBipagemActivity.this, pCodBarra, "Código de barra unitário?");
                    alerta1.showConfirm((dialogYes, whichYes) -> {
                        addCodBarra(produto, true, pCodBarra, true);
                    }, (dialogNO, whichNo) -> addCodBarra(produto, false, pCodBarra, true));
                } else
                    addCodBarra(produto, false, pCodBarra, false);
            }, (dialog, which) -> openCamera());
        }
    }

    private void addCodBarra(Produto pProduto, boolean pIndividual, String pCodigo, boolean pInicial) {
        try {
            if (listFinal == null)
                listFinal = new ArrayList<>();

            ArrayList<CodBarra> listValidacao = new ArrayList<>();

            String codigoProd = (this.produto != null) ? this.produto.getId() : pProduto.getId();
            Preco preco = null;
            if (this.produto != null && this.estruturaProd != null) {
                preco = Utilidades.getPreco(this.produto, visita.getIdCliente(), NovaBipagemActivity.this);
            } else {
                preco = Utilidades.getPreco(pProduto, visita.getIdCliente(), NovaBipagemActivity.this);
            }

            ItemVendaCombo itemVendaCombo = dbEstoque.getPistolagem(venda, codigoProd, preco.getIdPreco());
            ArrayList<CodBarra> listCodBarra = null;
            ArrayList<ComboVenda> listCombo = null;

            if (itemVendaCombo != null) {
                if (itemVendaCombo.getCodigosList() != null)
                    listCodBarra = (ArrayList<CodBarra>) itemVendaCombo.getCodigosList();
                if (itemVendaCombo.getListItens() != null)
                    listCombo = itemVendaCombo.getListItens();
            }

            if (itemVendaCombo == null) {
                itemVendaCombo = new ItemVendaCombo();
                itemVendaCombo.setQtdCombo(pProduto.getQtdCombo());
                double valorUnt = preco.getValor();
                if (this.produto != null && this.estruturaProd != null) {
                    itemVendaCombo.setIdProduto(this.produto.getId());
                    itemVendaCombo.setNomeProduto(this.produto.getNome());
                    itemVendaCombo.setCombo(true);
                } else {
                    itemVendaCombo.setIdProduto(this.iccid.getItemCode());
                    itemVendaCombo.setNomeProduto(pProduto.getNome());
                    itemVendaCombo.setCombo(false);
                }
                if (!Util_IO.isNullOrEmpty(preco.getIdPreco()))
                    itemVendaCombo.setIdPreco(Integer.parseInt(preco.getIdPreco()));
                itemVendaCombo.setValorUN(valorUnt);
            }

            if (listCodBarra != null)
                listValidacao.addAll(listCodBarra);

            listValidacao.addAll(Utilidades.getCodBarraItensByVendidos(this));

            if (pIndividual) {
                newCodBarra = new CodBarra();
                newCodBarra.setIndividual(true);
                newCodBarra.setGrupoProduto(pProduto.getGrupo());
                newCodBarra.setCodBarraInicial(pCodigo);
                if (this.produto != null)
                    newCodBarra.setIdProduto(this.estruturaProd.getItemFilho());
                else
                    newCodBarra.setIdProduto(this.iccid.getItemCode());

                if (listCodBarra == null)
                    listCodBarra = new ArrayList<>();

                if (dbVenda.iccidVendido(newCodBarra)) {
                    Mensagens.iccidJaVendido(NovaBipagemActivity.this, newCodBarra);
                    newCodBarra = null;
                    return;
                }

                if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                    Mensagens.iccidJaVendido(NovaBipagemActivity.this, newCodBarra);
                    newCodBarra = null;
                    return;
                }

                for (ItemVendaCombo item : listFinal) {
                    if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, (ArrayList<CodBarra>) item.getCodigosList())) {
                        Mensagens.iccidJaVendido(NovaBipagemActivity.this, newCodBarra);
                        newCodBarra = null;
                        return;
                    }
                }

                listCodBarra.add(newCodBarra);
                if (this.produto != null && this.estruturaProd != null) {
                    listCombo = getEstruturaVenda(estruturaProd.getItemFilho(), Integer.valueOf(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)), listCombo);
                    itemVendaCombo.setListItens(listCombo);
                }

                itemVendaCombo.setCodigosList(listCodBarra);
                newCodBarra = null;
            } else {
                if (newCodBarra == null) {
                    newCodBarra = new CodBarra();
                    newCodBarra.setIndividual(false);
                    newCodBarra.setGrupoProduto(pProduto.getGrupo());
                    if (this.produto != null && this.estruturaProd != null)
                        newCodBarra.setIdProduto(this.estruturaProd.getItemFilho());
                    else
                        newCodBarra.setIdProduto(this.iccid.getItemCode());
                }

                if (listCodBarra == null)
                    listCodBarra = new ArrayList<>();

                if (pInicial) {
                    newCodBarra.setCodBarraInicial(pCodigo);
                } else {
                    newCodBarra.setCodBarraFinal(pCodigo);

                    if (dbVenda.iccidVendido(newCodBarra)) {
                        Mensagens.iccidJaVendido(NovaBipagemActivity.this, newCodBarra);
                        newCodBarra = null;
                        return;
                    }

                    BigInteger value = new BigInteger(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL));
                    if (value.compareTo(BigInteger.valueOf(2)) < 0) {
                        Mensagens.mensagemErro(NovaBipagemActivity.this, "Verifique o ICCID não é sequêncial!", false);
                        newCodBarra = null;
                        return;
                    }

                    if (CodigoBarra.retornaICCID(newCodBarra.getCodBarraInicial(), newCodBarra.getGrupoProduto()).compareTo(CodigoBarra.retornaICCID(newCodBarra.getCodBarraFinal(), newCodBarra.getGrupoProduto())) > -1) {
                        Mensagens.mensagemErro(NovaBipagemActivity.this, "Verifique o ICCID não é sequêncial!", false);
                        newCodBarra = null;
                        return;
                    }

                    if (CodigoBarra.verificaICCIDDuplicado(newCodBarra, listValidacao)) {
                        Mensagens.iccidJaVendido(NovaBipagemActivity.this, newCodBarra);
                        newCodBarra = null;
                        return;
                    }

                    if (!newCodBarra.getIdProduto().equals(pProduto.getId())) {
                        Produto produtoInicial = dbEstoque.getProdutoById(newCodBarra.getIdProduto());
                        Mensagens.mensagemErro(NovaBipagemActivity.this,
                                getString(R.string.nova_bipagem_range_iccid_produto_diferente,
                                        produtoInicial.getNome()),
                                false);
                        newCodBarra = null;
                        return;
                    }

                    if (!CodigoBarra.validaQuantidadeRange(newCodBarra)) {
                        Alerta alerta = new Alerta(NovaBipagemActivity.this,
                                getString(R.string.app_titulo),
                                "Favor conferir o ICCID inicial e final");

                        alerta.show((dialog, which) -> {
                            newCodBarra = null;
                            openCamera();
                        });
                        return;
                    }

                    listCodBarra.add(newCodBarra);
                    if (this.produto != null && this.estruturaProd != null) {
                        listCombo = getEstruturaVenda(estruturaProd.getItemFilho(), Integer.valueOf(newCodBarra.retornaQuantidade(UsoCodBarra.GERAL)), listCombo);
                        itemVendaCombo.setListItens(listCombo);
                    }

                    itemVendaCombo.setCodigosList(listCodBarra);
                    newCodBarra = null;
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
                        Mensagens.mensagemErro(NovaBipagemActivity.this, "Favor conferir o ICCID inicial e final", false);
                        return;
                    }
                }

                //Combo
                ProdutoCombo produtoCombo = null;
                if (itemVendaCombo.isCombo() || itemVendaCombo.getQtdCombo() > 0) {
                    int qtdCombo = itemVendaCombo.getQtdCombo();
                    if (itemVendaCombo.isCombo())
                        qtdCombo = CodigoBarra.retornaQtCombo(this.produto.getId(), NovaBipagemActivity.this);
                    if (qtdCombo == 0)
                        throw new Exception("Estrutura de produto " + this.produto.getId() + " não localizado, Verifique com o Departamento de TI");
                    itemVendaCombo.setQtdCombo(qtdCombo);
                    produtoCombo = CodigoBarra.retornaCombo(itemVendaCombo.getQtdCombo(), itemVendaCombo.getCodigosList(), UsoCodBarra.GERAL);
                }
                itemVendaCombo.setQtde(((produtoCombo != null) ? produtoCombo.getQtdTotal() : quantidade));

                dbEstoque.incluirComboPistolagem(itemVendaCombo, venda);
                atualizarItens();
            }

            openCamera();
            txtCodigoBarras.setText("");
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovaBipagemActivity.this, ex.getMessage(), false);
        }
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
        listFinal = dbEstoque.getPistolagensComboNaoFinalizada(venda);

        mAdapter = new CodBarraAdapter(listFinal, NovaBipagemActivity.this, this, 1);
        lsICCIDs.setEmptyView(findViewById(R.id.empty));
        lsICCIDs.setAdapter(mAdapter);
        for (int i = 0; i < listFinal.size(); i++) {
            if (!listFinal.get(i).getCodigosList().isEmpty()) {
                lsICCIDs.expandGroup(i);
            }
        }
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
                                    dbEstoque.deletarPistolagensComboNaoFinalizadas(venda);
                            } catch (Exception ex) {
                                Utilidades.retornaMensagem(NovaBipagemActivity.this, ex.getMessage(), false);
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
}