package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.dialog.TokenDialog;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;

import java.util.ArrayList;

public class NovoAtendimentoActivity extends AppCompatActivity implements TokenDialog.OnCompleteListenet {
    DBCliente dbCliente;
    DBEstoque dbEstoque;
    DBVenda dbVenda;
    DBChamados dbChamados;

    Venda venda;
    Visita visita;
    Cliente cliente;
    Produto produto;
    Chamado chamado;
    CountDownTimer timer;
    ArrayList<Produto> listaProduto;

    TextView txtCliente;
    CheckBox swPistolagem;
    LinearLayout btnAddProduto, btnListarProduto, btnInfoCliente;
    SearchableSpinner cbProduto;
    EditText txtQuantidade;
    Button btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_atendimento);

        Utilidades.getDataServidorESalvaBanco(NovoAtendimentoActivity.this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Visita");
        }

        try {
            visita = Utilidades.getVisita(NovoAtendimentoActivity.this);
            if (visita == null) {
                Mensagens.visitaNaoIniciada(NovoAtendimentoActivity.this);
                return;
            }

            criarObjetos();
            if (!atualizarCliente()) {
                return;
            }

            timer = new CountDownTimer(1000000000, 1000) {
                public void onTick(long millisUntilFinished) {
                    ((TextView) findViewById(R.id.txtTempo)).setText(Utilidades.retornaTempoAtendimento(visita.getDataInicio()));
                    Utilidades.atualizaTotalVenda(dbVenda, venda, ((TextView) findViewById(R.id.txtTotalPedido)), false);
                    atualizarCliente();
                }

                public void onFinish() {
                }
            };

            carregarDados();
            criarEventos();
            if (cliente.getAuditagem() != null && cliente.getAuditagem().equalsIgnoreCase("S"))
                Utilidades.openAuditagemCliente(NovoAtendimentoActivity.this, cliente.getId(), true);
            else if (cliente.isAtualizaBinario())
                atualizarBinario();
            else {
                chamado = dbChamados.getChamadoByClienteId(cliente.getId());
                if (chamado != null)
                    Utilidades.openChamadoCliente(NovoAtendimentoActivity.this, chamado);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), true);
        }
    }

    private boolean atualizarCliente() {
        cliente = dbCliente.getById(visita.getIdCliente());
        if (cliente == null) {
            Mensagens.clienteNaoEncontradoFinalizarAtend(NovoAtendimentoActivity.this, visita);
            return false;
        }

        txtCliente.setText(cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            SimpleDbHelper.INSTANCE.open(NovoAtendimentoActivity.this);
            if (timer != null)
                timer.start();
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.ProdutoVenda) {
            venda = dbVenda.getVendabyIdVisita(visita.getId());
            Mensagens.produtoAdicionado(NovoAtendimentoActivity.this);
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void atualizarBinario() {
        try {
            TokenDialog tokenDialog = new TokenDialog();
            tokenDialog.setCancelable(false);
            tokenDialog.onCompleteListenet = NovoAtendimentoActivity.this;
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, visita.getIdCliente());
            tokenDialog.setArguments(args);
            tokenDialog.show(getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    if (!Validacoes.validacaoDataAparelho(NovoAtendimentoActivity.this)) {
                        return false;
                    }
                    Utilidades.openMotivo(NovoAtendimentoActivity.this, visita);
                } catch (Exception ex) {
                    Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), false);
                }
                break;
            case R.id.menu_audita_cliente:
                Utilidades.openAuditagemCliente(NovoAtendimentoActivity.this, cliente.getId(), false);
                break;
            case R.id.ajuda:
                Utilidades.openSuporte(NovoAtendimentoActivity.this);
                break;
            case R.id.sync:
                //new BaixarParcialTask(NovoAtendimentoActivity.this).execute();
                Bundle bundle = new Bundle();
                bundle.putInt("mTipoOperacao", 1);
                Utilidades.openNewActivity(NovoAtendimentoActivity.this, SyncActivity.class, bundle, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void criarEventos() {

        swPistolagem.setOnClickListener((view) -> {
            if (swPistolagem.isChecked()) {
                (findViewById(R.id.layoutQtd)).setVisibility(View.GONE);
                (findViewById(R.id.layoutProd)).setVisibility(View.GONE);
            } else {
                carregarDados();
                (findViewById(R.id.layoutProd)).setVisibility(View.VISIBLE);
                if (produto != null && produto.getBipagem().equalsIgnoreCase("S"))
                    (findViewById(R.id.layoutQtd)).setVisibility(View.GONE);
                else
                    (findViewById(R.id.layoutQtd)).setVisibility(View.VISIBLE);
            }
        });

        btnAddProduto.setOnClickListener((view) -> {
            try {
                if (!Validacoes.validacaoDataAparelho(NovoAtendimentoActivity.this)) {
                    return;
                }
                cliente = dbCliente.getById(visita.getIdCliente());
                if (cliente.getAuditagem() != null && cliente.getAuditagem().equals("S")) {
                    Utilidades.openAuditagemCliente(NovoAtendimentoActivity.this, cliente.getId(), false);
                    return;
                }

                if (cliente.isAtualizaBinario()) {
                    atualizarBinario();
                    return;
                }

                //Bloquear Atendimento pelas fotos de faixadas erradas
                if (cliente.isBloqueiaAtendimento() && cliente.getQtdLocReprovada() > 1) {
                    Alerta alerta = new Alerta(NovoAtendimentoActivity.this, getResources().getString(R.string.app_name), "Foram reprovadas " + String.valueOf(cliente.getQtdLocReprovada()) + " fotos da fachada do ponto de venda, No próximo atendimento não será possivél realizar venda");
                    if (cliente.getQtdLocReprovada() == 2) {
                        alerta.show();
                        return;
                    } else if (cliente.getQtdLocReprovada() >= 3) {
                        alerta.setMensagem("Foram reprovadas " + String.valueOf(cliente.getQtdLocReprovada()) + " fotos da fachada do ponto de venda" + ", não será possivél realizar venda, Favor entrar em contato com o Gerente");
                        alerta.show();
                        return;
                    }
                }

                chamado = dbChamados.getChamadoByClienteId(cliente.getId());
                if (chamado != null) {
                    Utilidades.openChamadoCliente(NovoAtendimentoActivity.this, chamado);
                    return;
                }

                if (swPistolagem.isChecked()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Config.CodigoVisita, visita.getId());
                    Utilidades.openNewActivityForResult(NovoAtendimentoActivity.this, NovaBipagemActivity.class, RequestCode.ProdutoVenda, bundle);
                } else {
                    if (produto == null) {
                        Mensagens.produtoNaoSelecionado(view.getContext());
                        return;
                    }

                    if (produto.getBipagem().equalsIgnoreCase("S")) {
                        if (!dbEstoque.isProdutoCombo(produto.getId())) {
                            Alerta alerta = new Alerta(view.getContext(), getResources().getString(R.string.app_name)
                                    , "Para incluir esse produto os códigos de barra devem ser pistolados! Selecione a opção de pistolagem e a opção adicionar produto.");
                            alerta.show();
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putInt(Config.CodigoVisita, visita.getId());
                        bundle.putString(Config.CodigoProduto, produto.getId());
                        Utilidades.openNewActivityForResult(NovoAtendimentoActivity.this, NovaBipagemActivity.class, RequestCode.ProdutoVenda, bundle);
                    } else {
                        String quantidade = txtQuantidade.getText().toString().trim();
                        if (quantidade.isEmpty()) {
                            Mensagens.quantidadeNaoInformada(view.getContext());
                            return;
                        }

                        int number = Integer.parseInt(quantidade);
                        if (number <= 0) {
                            Mensagens.quantidadeNaoInformada(view.getContext());
                            return;
                        }

                        PrecoDiferenciado precoDiferenciado = null;
                        String codigoPreco = Utilidades.getPreco(produto, visita.getIdCliente(), view.getContext()).getIdPreco();
                        if (!Util_IO.isNullOrEmpty(codigoPreco))
                            precoDiferenciado = new DBPreco(view.getContext()).getPrecoById(codigoPreco);
                        if (venda == null)
                            venda = Utilidades.getVenda(view.getContext(), visita);
                        ArrayList<CodBarra> lista = new ArrayList<>();
                        produto.setQtde(number);
                        if ((precoDiferenciado != null))
                            produto.setPrecovenda(precoDiferenciado.getValor());

                        boolean validation = Validacoes.validacoesPreAddICCID(NovoAtendimentoActivity.this,
                                produto,
                                precoDiferenciado,
                                lista,
                                false,
                                null,
                                UsoCodBarra.GERAL);

                        if (validation) {
                            Utilidades.addICCIDItemVenda(NovoAtendimentoActivity.this, precoDiferenciado, venda, produto, lista, produto.getPrecovenda(), true, null);
                            Mensagens.produtoAdicionado(NovoAtendimentoActivity.this);
                            txtQuantidade.setText("");
                        }
                    }
                }
            } catch (Exception ex) {
                Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), false);
            }
        });

        cbProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produto = listaProduto.get(position);

                if (produto == null) {
                    Mensagens.produtoNaoSelecionado(view.getContext());
                    return;
                }

                txtQuantidade.setText("");
                if (produto.getBipagem().equalsIgnoreCase("S"))
                    (findViewById(R.id.layoutQtd)).setVisibility(View.GONE);
                else
                    (findViewById(R.id.layoutQtd)).setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnFinalizar.setOnClickListener((view) -> {
            if (!Validacoes.validacaoDataAparelho(NovoAtendimentoActivity.this)) {
                return;
            }
            if (venda == null)
                try {
                    Utilidades.openMotivo(NovoAtendimentoActivity.this, visita);
                } catch (Exception ex) {
                    Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), false);
                }
            else {
                Bundle bundle = new Bundle();
                bundle.putInt(Config.CodigoVisita, visita.getId());
                Utilidades.openNewActivity(NovoAtendimentoActivity.this, FinalizarAtendActivity.class, bundle, true);
            }
        });

        btnListarProduto.setOnClickListener((view) -> {
            if (!Validacoes.validacaoDataAparelho(NovoAtendimentoActivity.this)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVenda, (venda != null) ? venda.getId() : 0);
            Utilidades.openNewActivity(NovoAtendimentoActivity.this, ItensVendaActivity.class, bundle, false);
        });

        btnInfoCliente.setOnClickListener((view) -> {
            if (!Validacoes.validacaoDataAparelho(NovoAtendimentoActivity.this)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, cliente.getId());
            Utilidades.openNewActivity(NovoAtendimentoActivity.this, ClienteInfoActivity.class, bundle, false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nova_visita, menu);
        MenuItem item = menu.findItem(R.id.menu_audita_cliente);
        if (cliente != null && (cliente.getAuditagem() == null || cliente.getAuditagem().equals("N"))) {
            if (item != null)
                item.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        if (timer != null)
            timer.cancel();
        super.onPause();
    }

    private void criarObjetos() {
        txtCliente = (TextView) findViewById(R.id.txtCliente);
        dbCliente = new DBCliente(NovoAtendimentoActivity.this);
        swPistolagem = (CheckBox) findViewById(R.id.swPistolagem);
        btnAddProduto = (LinearLayout) findViewById(R.id.btnAddProduto);
        dbEstoque = new DBEstoque(NovoAtendimentoActivity.this);
        cbProduto = (SearchableSpinner) findViewById(R.id.spinnerProduto);
        Utilidades.defineSpinner(cbProduto);
        txtQuantidade = (EditText) findViewById(R.id.txtQuantidade);
        dbVenda = new DBVenda(NovoAtendimentoActivity.this);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnListarProduto = (LinearLayout) findViewById(R.id.btnListarProduto);
        btnInfoCliente = (LinearLayout) findViewById(R.id.btnInfoCliente);
        dbChamados = new DBChamados(NovoAtendimentoActivity.this);
    }

    private void carregarDados() {
        listaProduto = dbEstoque.getProdutosComboENaoPistolados();
        cbProduto.setAdapter(UtilAdapter.adapterProduto(NovoAtendimentoActivity.this, listaProduto));
        venda = dbVenda.getVendabyIdVisita(visita.getId());
    }

    @Override
    public void onCompleteToken(boolean pValor) {
        try {
            if (pValor) {
                dbCliente.confirmaAtualizacaoBinario(visita.getIdCliente());
                cliente = dbCliente.getById(visita.getIdCliente());
                Utilidades.retornaMensagem(NovoAtendimentoActivity.this, "Atualização confirmada!", false);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovoAtendimentoActivity.this, ex.getMessage(), false);
        }
    }
}