package com.axys.redeflexmobile.ui.redeflex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemVendaExpListAdapter;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.bd.DBMotivo;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cadastro;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ConfirmacaoVenda;
import com.axys.redeflexmobile.shared.models.FormaPagamentoVenc;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.dialog.CadastroClienteDialog;
import com.axys.redeflexmobile.ui.dialog.ConfirmacaoVendaDialog;
import com.axys.redeflexmobile.ui.dialog.ConfirmarSenhaDialog;
import com.axys.redeflexmobile.ui.dialog.FormaPagamentoDialog;
import com.axys.redeflexmobile.ui.dialog.TokenDialog;

import java.util.ArrayList;

@SuppressLint({"NonConstantResourceId", "SetTextI18n"})
public class AtendimentoActivity extends AppCompatActivity implements ConfirmacaoVendaDialog.OnCompleteListener
        , CadastroClienteDialog.OnCompleteListener
        , ConfirmarSenhaDialog.OnCompleteListener
        , FormaPagamentoDialog.OnCompleteListener
        , TokenDialog.OnCompleteListenet {

    DBEstoque dbEstoque;
    DBMotivo dbMotivo;
    DBVisita dbVisita;
    DBVenda dbVenda;
    DBPreco dbPreco;
    DBFormaPagamento dbFormaPagamento;
    DBCliente dbCliente;
    DBLimite dbLimite;
    DBChamados dbChamados;

    Cliente cliente;
    Produto produto;
    Preco precoVenda;
    Chamado chamado;

    ItemVendaExpListAdapter mItensAdapter;
    ArrayList<Produto> listaProduto;
    ArrayList<Preco> listaPreco;
    ArrayList<ItemVenda> listaItens;
    Visita visita;
    Venda venda;
    LimiteCliente limiteCliente;

    ExpandableListView listaView;
    CountDownTimer timer;
    TextView txtCliente, txtTotal, txtTotalVenda;
    EditText txtQtd;
    SearchableSpinner cbproduto, cbValorProduto;
    Button btnAddProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        Utilidades.getDataServidorESalvaBanco(AtendimentoActivity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Visita");
        }

        try {
            int idVisita = 0;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                idVisita = bundle.getInt(Config.CodigoVisita);


            if (idVisita == 0) {
                Mensagens.visitaNaoIniciada(AtendimentoActivity.this);
                return;
            }

            criarObjetos();
            visita = dbVisita.getVisitabyId(idVisita);
            if (visita == null) {
                Mensagens.visitaNaoIniciada(AtendimentoActivity.this);
                return;
            }

            cliente = dbCliente.getById(visita.getIdCliente());
            if (cliente == null) {
                Mensagens.clienteNaoEncontradoFinalizarAtend(AtendimentoActivity.this, visita);
                return;
            }

            carregaDados();
            criarEventos();

            timer = new CountDownTimer(1000000000, 1000) {
                public void onTick(long millisUntilFinished) {
                    ((TextView) findViewById(R.id.txtTempo)).setText(Utilidades.retornaTempoAtendimento(visita.getDataInicio()));
                    Utilidades.atualizaTotalVenda(dbVenda, venda, txtTotalVenda, true);
                    if (!txtTotalVenda.getText().toString().equalsIgnoreCase("Total R$ 0,00") && (listaItens == null || listaItens.size() == 0)) {
                        atualizaItens(false);
                    }
                }

                public void onFinish() {
                }
            };

            if (cliente != null) {
                if (cliente.getAuditagem() != null && cliente.getAuditagem().equalsIgnoreCase("S"))
                    Utilidades.openAuditagemCliente(AtendimentoActivity.this, cliente.getId(), true);
                else if (cliente.isAtualizaBinario())
                    atualizarBinario();
                else {
                    chamado = dbChamados.getChamadoByClienteId(cliente.getId());
                    if (chamado != null)
                        Utilidades.openChamadoCliente(AtendimentoActivity.this, chamado);
                }
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), true);
        }
    }

    private void atualizarBinario() {
        try {
            TokenDialog tokenDialog = new TokenDialog();
            tokenDialog.setCancelable(false);
            tokenDialog.onCompleteListenet = (pValor) -> {
                try {
                    if (pValor) {
                        dbCliente.confirmaAtualizacaoBinario(visita.getIdCliente());
                        cliente = dbCliente.getById(visita.getIdCliente());
                        Utilidades.retornaMensagem(AtendimentoActivity.this, "Atualização confirmada!", false);
                    }
                } catch (Exception ex) {
                    Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
                }
            };
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, visita.getIdCliente());
            tokenDialog.setArguments(args);
            tokenDialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onCompleteToken(boolean pValor) {
        try {
            if (pValor) {
                dbCliente.confirmaAtualizacaoBinario(visita.getIdCliente());
                cliente = dbCliente.getById(visita.getIdCliente());
                Utilidades.retornaMensagem(AtendimentoActivity.this, "Atualização confirmada!", false);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private void criarObjetos() {
        listaView = findViewById(R.id.listaProdutoVenda);
        listaView.setScrollContainer(false);
        listaView.setFastScrollEnabled(false);
        dbVenda = new DBVenda(AtendimentoActivity.this);
        dbEstoque = new DBEstoque(AtendimentoActivity.this);
        dbMotivo = new DBMotivo(AtendimentoActivity.this);
        dbFormaPagamento = new DBFormaPagamento(AtendimentoActivity.this);
        dbPreco = new DBPreco(AtendimentoActivity.this);
        dbLimite = new DBLimite(AtendimentoActivity.this);
        txtCliente = findViewById(R.id.txtVisitaCliente);
        cbproduto = findViewById(R.id.spinnerProdutoVisita);
        Utilidades.defineSpinner(cbproduto);
        btnAddProduto = findViewById(R.id.btnAddProduto);
        txtQtd = findViewById(R.id.txtQuantidadeVisita);
        txtTotal = findViewById(R.id.txtTotalProduto);
        listaItens = new ArrayList<>();
        dbVisita = new DBVisita(AtendimentoActivity.this);
        dbCliente = new DBCliente(AtendimentoActivity.this);
        cbValorProduto = findViewById(R.id.spinnerValorProduto);
        Utilidades.defineSpinner(cbValorProduto);
        txtTotalVenda = findViewById(R.id.txtTotalVenda);
        dbChamados = new DBChamados(AtendimentoActivity.this);
    }

    private void atualizaItens(boolean pAtualizaQtd) {
        listaView.setEmptyView(findViewById(R.id.empty));
        Utilidades.atualizaTotalVenda(dbVenda, venda, findViewById(R.id.txtTotalVenda), true);
        if (venda != null) {
            listaItens = dbVenda.getItensVendabyIdVenda(venda.getId());
            mItensAdapter = new ItemVendaExpListAdapter(AtendimentoActivity.this, listaItens);
            listaView.setAdapter(mItensAdapter);
            if (pAtualizaQtd)
                txtQtd.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            SimpleDbHelper.INSTANCE.open(AtendimentoActivity.this);
            if (timer != null)
                timer.start();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        if (timer != null)
            timer.cancel();
        super.onPause();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visita, menu);
        MenuItem item = menu.findItem(R.id.menu_audita_cliente);
        if (cliente != null && (cliente.getAuditagem() == null || cliente.getAuditagem().equals("N"))) {
            if (item != null)
                item.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    if (!Validacoes.validacaoDataAparelho(AtendimentoActivity.this)) {
                        return false;
                    }
                    Utilidades.openMotivo(AtendimentoActivity.this, visita);
                } catch (Exception ex) {
                    Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
                }
                break;
            case R.id.menu_salvar:
                salvar();
                break;
            case R.id.menu_audita_cliente:
                Utilidades.openAuditagemCliente(AtendimentoActivity.this, cliente.getId(), false);
                break;
            case R.id.ajuda:
                Utilidades.openSuporte(AtendimentoActivity.this);
                break;
            case R.id.sync:
                //new BaixarParcialTask(AtendimentoActivity.this).execute();
                Bundle bundle = new Bundle();
                bundle.putInt("mTipoOperacao", 1);
                Utilidades.openNewActivity(AtendimentoActivity.this, SyncActivity.class, bundle, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        try {
            if (!Validacoes.validacaoDataAparelho(AtendimentoActivity.this)) {
                return;
            }
            if (listaItens == null || listaItens.size() == 0)
                Utilidades.openMotivo(AtendimentoActivity.this, visita);
            else
                openFormaPagamento();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private void atualizaPrecos() {
        try {
            if (produto != null) {
                listaPreco = Utilidades.getListaPrecos(AtendimentoActivity.this, produto, cliente.getId());
                cbValorProduto.setAdapter(UtilAdapter.adapterPreco(AtendimentoActivity.this, listaPreco));
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private double retornaValor() {
        double dPreco = produto.getPrecovenda();
        if (precoVenda != null)
            dPreco = precoVenda.getValor();
        return dPreco;
    }

    private void criarEventos() {
        cbproduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produto = listaProduto.get(position);

                if (produto == null) {
                    Mensagens.produtoNaoSelecionado(view.getContext());
                    return;
                }

                txtQtd.setEnabled(!(produto.getBipagem().equals("S")));
                atualizaPrecos();
                txtQtd.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cbValorProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listaPreco != null)
                    precoVenda = listaPreco.get(position);
                txtQtd.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtQtd.setText("");
            }
        });

        txtQtd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.toString().trim().length() > 0) {
                        if (produto != null) {
                            int number = Integer.parseInt(s.toString());
                            double dPreco = retornaValor();
                            txtTotal.setText("R$ " + Util_IO.formataValor(Util_IO.getValor(dPreco * number)));
                        } else
                            throw new Exception("Produto não selecionado, Verifique!");
                    } else
                        txtTotal.setText("R$ 0,00");
                } catch (Exception ex) {
                    txtQtd.setText("");
                    Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
                }
            }
        });

        btnAddProduto.setOnClickListener((view) -> {
            try {
                if (!Validacoes.validacaoDataAparelho(AtendimentoActivity.this)) {
                    return;
                }
                cliente = dbCliente.getById(visita.getIdCliente());
                if (cliente.getAuditagem() != null && cliente.getAuditagem().equals("S")) {
                    Utilidades.openAuditagemCliente(AtendimentoActivity.this, cliente.getId(), false);
                    return;
                }

                if (cliente.isAtualizaBinario()) {
                    atualizarBinario();
                    return;
                }

                chamado = dbChamados.getChamadoByClienteId(cliente.getId());
                if (chamado != null) {
                    Utilidades.openChamadoCliente(AtendimentoActivity.this, chamado);
                    return;
                }

                venda = dbVenda.getVendabyIdVisita(visita.getId());
                if (venda == null) {
                    long codigo = dbVenda.novaVenda(visita.getId(), visita.getIdCliente());
                    venda = dbVenda.getVendabyId((int) codigo);
                }

                if (produto == null) {
                    Mensagens.produtoNaoSelecionado(AtendimentoActivity.this);
                    return;
                }

                if (Utilidades.verificaItensDuplicado(AtendimentoActivity.this, listaItens, produto)) {
                    return;
                }

                double valor = retornaValor();
                if (valor <= 0 && produto.getPermiteVendaSemValor().equalsIgnoreCase("N")) {
                    Mensagens.produtoComValorZerado(view.getContext());
                    return;
                }

                if (produto.getBipagem().equals("S")) {
                    Intent intent = new Intent(AtendimentoActivity.this, ProdutoVendaActivity.class);
                    intent.putExtra(Config.CodigoProdutoVenda, produto.getId());
                    intent.putExtra(Config.CodigoVenda, venda.getId());
                    if (precoVenda != null && !Util_IO.isNullOrEmpty(precoVenda.getIdPreco()))
                        intent.putExtra(Config.CodigoPreco, precoVenda.getIdPreco());
                    else
                        intent.putExtra(Config.CodigoPreco, 0);
                    startActivityForResult(intent, RequestCode.ProdutoVenda);
                } else if (produto.getBipagem().equals("N")) {
                    if (txtQtd.getText().toString().trim().length() == 0) {
                        Mensagens.quantidadeNaoInformada(AtendimentoActivity.this);
                        return;
                    }

                    int number = Integer.parseInt(txtQtd.getText().toString().trim());
                    if (number <= 0) {
                        Mensagens.quantidadeNaoInformada(AtendimentoActivity.this);
                        return;
                    }

                    if (!dbEstoque.verificaEstoque(produto.getId(), number)) {
                        Mensagens.quantidadeSuperiorAEstoque(AtendimentoActivity.this);
                        return;
                    }

                    ArrayList<CodBarra> lista = new ArrayList<>();
                    if (precoVenda != null && precoVenda.getQuantidade() > 0) {
                        if (number > precoVenda.getQuantidade()) {
                            Mensagens.promocaoAteTantasUnds(AtendimentoActivity.this, precoVenda.getQuantidade());
                            return;
                        } else {
                            produto.setQtde(number);
                            long idVendaItem = dbVenda.addItemVenda(venda, produto, lista, valor, null, precoVenda.getIdPreco());
                            dbPreco.atualizaIdVenda(String.valueOf(precoVenda.getIdPreco()), String.valueOf(venda.getId()), String.valueOf(idVendaItem), number);
                            dbPreco.atualizaQtdPreco(String.valueOf(precoVenda.getIdPreco()), number);
                        }
                        atualizaPrecos();
                    } else {
                        produto.setQtde(number);
                        dbVenda.addItemVenda(venda, produto, lista, valor, null, null);
                    }

                    dbEstoque.atualizaEstoque(produto.getId(), true, number);
                    atualizaItens(true);
                    Mensagens.produtoAdicionado(AtendimentoActivity.this);
                }
            } catch (Exception ex) {
                Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
            }
        });
    }

    private void openFormaPagamento() {
        try {
            if (!Validacoes.validacaoDataAparelho(AtendimentoActivity.this)) {
                return;
            }

            // Verificar se será liberado Pagamento Avista em caso de ser Bobina
            int countVendaAvista = 0;
            for (ItemVenda item : listaItens) {
                Produto prod = new DBEstoque(AtendimentoActivity.this).getProdutoById(item.getIdProduto());
                if (prod != null)
                {
                    if (prod.getVendaAvista().equals("S"))
                        countVendaAvista++;
                }
            }

            FormaPagamentoDialog dialog = new FormaPagamentoDialog();
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, visita.getIdCliente());
            args.putBoolean("VendeAvista", countVendaAvista == listaItens.size() ? true : false);
            dialog.setArguments(args);
            dialog.myCompleteListener = AtendimentoActivity.this;
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private void openConfirmacaoVenda() {
        try {
            ConfirmacaoVendaDialog dialog = new ConfirmacaoVendaDialog();
            dialog.myCompleteListenerVenda = this;
            Bundle args = new Bundle();
            args.putInt("visita", visita.getId());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.ProdutoVenda) {
                atualizaItens(true);
                Mensagens.produtoAdicionado(AtendimentoActivity.this);
            }
        }
    }

    public void onComplete(ConfirmacaoVenda confirmavenda) {
        if (confirmavenda != null && confirmavenda.getStatus()) {
            try {
                Utilidades.encerrarAtendimento(AtendimentoActivity.this, false, venda, limiteCliente, visita, listaItens, 0, timer);
            } catch (Exception ex) {
                Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
            }
        }
    }

    @Override
    public void onComplete(FormaPagamentoVenc retorno) {
        try {
            if (Validacoes.validacoesAtualizaFormaPagamento(AtendimentoActivity.this, retorno, venda)) {
                dbVenda.updateFormaPagamentoVenda(venda.getId(), retorno.getFormapgto().getId(), retorno.getDatavencimento());
                if (retorno.getFormapgto().getId() == 2) {
                    if (cliente.getExibirCodigo().equalsIgnoreCase("SGV"))
                        openConfirmarSenha();
                    else {
                        if (!Util_IO.isNullOrEmpty(cliente.getSenha())) {
                            if (cliente.getSenha().equalsIgnoreCase("Aguarde") && Utilidades.isConectado(AtendimentoActivity.this))
                                aguardeSenha();
                            else
                                openConfirmarSenha();
                        } else
                            openCadastroCliente();
                    }
                } else
                    openConfirmacaoVenda();
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private void aguardeSenha() {
        Alerta alerta = new Alerta(AtendimentoActivity.this, getResources().getString(R.string.app_name), "Aguarde um momento, a senha será enviada via sms para o cliente!");
        alerta.show((dialog, which) -> {
            new ClienteSyncTask(AtendimentoActivity.this, 1).execute();
            openConfirmarSenha();
        });
    }

    private void openCadastroCliente() {
        try {
            CadastroClienteDialog dialog = new CadastroClienteDialog();
            dialog.myCompleteListenerCadastro = AtendimentoActivity.this;
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, cliente.getId());
            args.putBoolean(Config.AtualizaContato, false);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onComplete(Cadastro cadastroalterado) {
        try {
            if (cadastroalterado != null && cadastroalterado.getAlterado().equals("S")) {
                new ClienteSyncTask(AtendimentoActivity.this, 1).execute();
                venda = dbVenda.getVendabyIdVisita(visita.getId());
                cliente = dbCliente.getById(visita.getIdCliente());
                if (cliente.getPedeSenha().equalsIgnoreCase("S") && venda.getIdFormaPagamento() == 2) {
                    if (cliente.getExibirCodigo().equalsIgnoreCase("SGV")) {
                        openConfirmarSenha();
                    } else if (cliente.getExibirCodigo().equalsIgnoreCase("EFRESH")) {
                        if (Util_IO.isNullOrEmpty(cliente.getSenha()) || cliente.getSenha().equalsIgnoreCase("Aguarde")) {
                            cliente.setSenha("Aguarde");
                            dbCliente.updateSenha(cliente.getId(), "Aguarde");
                            aguardeSenha();
                        } else
                            openConfirmarSenha();
                    } else {
                        Mensagens.formaPagamentoNaoPermitida(AtendimentoActivity.this);
                    }
                } else {
                    openConfirmacaoVenda();
                }
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private void openConfirmarSenha() {
        try {
            ConfirmarSenhaDialog dialog = new ConfirmarSenhaDialog();
            dialog.myCompleteListenerSenha = this;
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, cliente.getId());
            args.putString(Config.CodigoVenda, "");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    private void carregaDados() {
        try {
            cliente = dbCliente.getById(visita.getIdCliente());
            if (cliente == null) {
                Mensagens.clienteNaoEncontrado(AtendimentoActivity.this, true);
                return;
            }

            listaProduto = dbEstoque.getProdutosComEstoque();
            txtCliente.setText(cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
            cbproduto.setAdapter(UtilAdapter.adapterProduto(AtendimentoActivity.this, listaProduto));
            venda = dbVenda.getVendabyIdVisita(visita.getId());
            atualizaItens(true);
            atualizaPrecos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onCompleteSenha(boolean senhaok) {
        try {
            if (senhaok)
                openConfirmacaoVenda();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AtendimentoActivity.this, ex.getMessage(), false);
        }
    }
}
