package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemConsignadoAdapter;
import com.axys.redeflexmobile.shared.adapters.ItensVendaAdapter;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBPistolagemTemp;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDaoImpl;
import com.axys.redeflexmobile.shared.dao.VisitaDao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.BlipProvider;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteActivity;
import com.axys.redeflexmobile.ui.dialog.NovoProdutoDialog;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ConsignadoActivity extends AppCompatActivity {
    FloatingActionButton btnNovaConsignacao;
    AppCompatTextView tvCliente;
    AppCompatTextView tvDadosConsignacao, tvSubtotal;
    AppCompatButton btnGerarCobranca, btnGerarDevolucao, btnAuditagem, btnReconsignacao;
    RecyclerView mRecyclerView;
    LinearLayout ll_Gravacao, llAuditagem;

    Visita visita = new Visita();
    Cliente cliente = new Cliente();
    Consignado consignado = new Consignado();
    ArrayList<ConsignadoItem> listaItens;

    // Utilizado para gerar a Reconsignação dos Itens não Vendidos
    List<ItemVendaCombo> itensNaoVendidos = new ArrayList<>();

    ItemConsignadoAdapter adapter;

    DBVisita dbVisita;
    DBCliente dbCliente;
    BDConsignado bdConsignado;
    BDConsignadoItem bdConsignadoItem;
    BDConsignadoItemCodBarra bdConsignadoItemCodBarra;
    DBVenda dbVenda;

    EstoqueDao estoqueDao;
    int visitaId, clienteId;
    int operacao = 1;
    int pEvento = 0;

    Context mChamouTela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignado);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            visitaId = bundle.getInt("IdVisita");
            clienteId = Integer.parseInt(bundle.getString("IdCliente"));
            operacao = bundle.getInt("Operacao");
        }

        criarObjetos();
        montarActionBar();
        criarEventos();
        pEvento = 0;
        carregarDados(visitaId, clienteId);
    }

    private void montarActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Consignação de Venda");
        }
    }

    private void carregarDados(int visitaId, int idcliente) {
        visita = dbVisita.getVisitabyId(visitaId);
        if (visita != null)
            cliente = dbCliente.getById(visita.getIdCliente());
        else
            cliente = dbCliente.getById(String.valueOf(idcliente));

        // Caso seja reconsignação terá que carregar a consignação em andamento
        if (pEvento == 0) {
            consignado = bdConsignado.getByIdCLiente(cliente.getId(), 0);
        }

        if (consignado != null) {
            tvDadosConsignacao.setText("ITENS CONSIGNADOS DIA: " + Utilidades.getDateName(consignado.getDataEmissao()));
            listaItens = bdConsignadoItem.getByIdCConsignadoAuditagem(String.valueOf(consignado.getId()), 1);
            btnNovaConsignacao.setVisibility(View.GONE);

            if (operacao == 2) {
                btnNovaConsignacao.setVisibility(View.GONE);
                llAuditagem.setVisibility(View.GONE);
                ll_Gravacao.setVisibility(View.GONE);
            } else {
                if (pEvento == 1) {
                    llAuditagem.setVisibility(View.GONE);
                    ll_Gravacao.setVisibility(View.VISIBLE);
                    btnGerarCobranca.setVisibility(View.GONE);
                    btnGerarDevolucao.setVisibility(View.VISIBLE);
                    btnReconsignacao.setVisibility(View.VISIBLE);
                    btnAuditagem.setText("Auditagem");
                    popularAdapter(listaItens, 0);
                } else if (!ProdutosAuditados(listaItens)) {
                    llAuditagem.setVisibility(View.VISIBLE);
                    ll_Gravacao.setVisibility(View.GONE);
                } else {
                    if (ProdutosVendidos(listaItens)) {
                        llAuditagem.setVisibility(View.GONE);
                        ll_Gravacao.setVisibility(View.VISIBLE);
                        btnGerarCobranca.setVisibility(View.VISIBLE);
                        btnGerarDevolucao.setVisibility(View.GONE);
                        btnReconsignacao.setVisibility(View.GONE);
                    } else {
                        llAuditagem.setVisibility(View.GONE);
                        ll_Gravacao.setVisibility(View.VISIBLE);
                        btnGerarDevolucao.setVisibility(View.VISIBLE);
                        btnReconsignacao.setVisibility(View.VISIBLE);
                        popularAdapter(listaItens, 0);
                    }
                }
            }
        } else {
            tvDadosConsignacao.setText("ITENS CONSIGNADOS DIA: ");
            listaItens = new ArrayList<ConsignadoItem>();
            btnNovaConsignacao.setVisibility(View.VISIBLE);
            llAuditagem.setVisibility(View.GONE);
            ll_Gravacao.setVisibility(View.GONE);
        }

        if (btnAuditagem.getText().equals("Cancelar"))
            popularAdapter(listaItens, 1);
        else
            popularAdapter(listaItens, 0);
        tvCliente.setText("Cliente: " + cliente.getCodigoSGV() + " - " + cliente.getNomeFantasia());
    }

    private void criarObjetos() {
        btnNovaConsignacao = findViewById(R.id.consignado_btn_nova_consignacao);
        btnGerarCobranca = (AppCompatButton) findViewById(R.id.consignado_btn_gerarcobranca);
        btnGerarDevolucao = (AppCompatButton) findViewById(R.id.consignado_btn_gerardevolucao);
        btnReconsignacao = (AppCompatButton) findViewById(R.id.consignado_btn_reconsignar);
        btnAuditagem = (AppCompatButton) findViewById(R.id.consignado_btn_auditagem);
        mRecyclerView = (RecyclerView) findViewById(R.id.consignado_rv_itens);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tvCliente = (AppCompatTextView) findViewById(R.id.consignado_tv_cliente);
        tvDadosConsignacao = (AppCompatTextView) findViewById(R.id.consignado_tv_dadosconsignacao);
        tvSubtotal = (AppCompatTextView) findViewById(R.id.consignado_tv_subtotal);
        ll_Gravacao = (LinearLayout) findViewById(R.id.consignado_ll_gravacao);
        llAuditagem = (LinearLayout) findViewById(R.id.consignado_ll_auditagem);

        dbVisita = new DBVisita(ConsignadoActivity.this);
        dbCliente = new DBCliente(ConsignadoActivity.this);
        bdConsignado = new BDConsignado(ConsignadoActivity.this);
        bdConsignadoItem = new BDConsignadoItem(ConsignadoActivity.this);
        bdConsignadoItemCodBarra = new BDConsignadoItemCodBarra(ConsignadoActivity.this);
        estoqueDao = new EstoqueDaoImpl(ConsignadoActivity.this);
        dbVenda = new DBVenda(ConsignadoActivity.this);
    }

    private void criarEventos() {
        btnNovaConsignacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visita.setIdClienteSGV(cliente.getCodigoSGV());
                visita.setNomeFantasia(cliente.getNomeFantasia());
                Intent intent = new Intent(ConsignadoActivity.this, ConsignadoAuditagemActivity.class);
                intent.putExtra("Visita", (Serializable) visita);
                intent.putExtra("Operacao", 0); // 0 - Inserção  / 1 - Reconsignacao / 2 - Editar
                startActivityForResult(intent, 1, null);
            }
        });

        btnGerarCobranca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaItens.size() <= 0) {
                    Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                            "Nenhum item adicionado. Verifique!"
                    );
                    alerta.show();
                } else {
                    // Verifica se todos os Produtos Foram auditados
                    if (!ProdutosAuditados(listaItens)) {
                        Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                                "Nenhum item foi Auditado! Verifique!!!"
                        );

                        alerta.showConfirm((dialog, which) -> gerarCobranca(), "Sim", (dialog, which) -> chamarAuditagem(), "Não");
                    } else {
                        double vTotalItens = 0;
                        for (int i = 0; i < listaItens.size(); i++) {
                            vTotalItens += listaItens.get(i).getValorTotalItem();
                        }

                        Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                                "Confirma a geração da Cobrança no Valor de " + Util_IO.formatDoubleToDecimalNonDivider(vTotalItens)
                        );

                        alerta.showConfirm((dialog, which) -> gerarCobranca(), "Sim", (dialog, which) -> chamarAuditagem(), "Não");
                    }
                }
            }
        });

        btnGerarDevolucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se todos os Produtos Foram auditados
                if (listaItens.size() <= 0) {
                    Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                            "Nenhum item adicionado. Verifique!"
                    );
                    alerta.show();
                } else {
                    // Verifica se todos os Produtos Foram auditados
                    if (!ProdutosAuditados(listaItens) && pEvento != 1) {
                        Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                                "Nenhum item foi Auditado! Verifique!!!"
                        );
                        alerta.show();
                    } else {
                        if (pEvento == 1 && VerificaDevolucaoParcial(listaItens))
                        {
                            Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                                    "Confirma a geração da Devolução Parcial?"
                            );
                            alerta.showConfirm((dialog, which) -> gerarDevolucaoParcial(), "Sim", (dialog, which) -> chamarAuditagem(), "Não");
                        }
                        else {
                            double vTotalItens = 0;
                            for (int i = 0; i < listaItens.size(); i++) {
                                vTotalItens += listaItens.get(i).getValorTotalItem();
                            }

                            Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                                    "Confirma a geração da Devolução Total?"
                            );

                            alerta.showConfirm((dialog, which) -> gerarDevolucao(), "Sim", (dialog, which) -> chamarAuditagem(), "Não");
                        }
                    }
                }
            }
        });

        btnAuditagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAuditagem.getText().equals("Cancelar")) {
                    btnAuditagem.setText("Auditagem");
                    popularAdapter(listaItens, 0);
                } else {
                    btnAuditagem.setText("Cancelar");
                    // Libera o icone Auditagem no Item
                    popularAdapter(listaItens, 1);
                }
            }
        });

        btnReconsignacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se todos os Produtos Foram auditados
                if (listaItens.size() <= 0) {
                    Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                            "Nenhum item adicionado. Verifique!"
                    );
                    alerta.show();
                } else {
                    // Verifica se todos os Produtos Foram auditados
                    if (!ProdutosAuditados(listaItens) && pEvento != 1) {
                        Alerta alerta = new Alerta(ConsignadoActivity.this, "Atenção!",
                                "Nenhum item foi Auditado! Verifique!!!"
                        );
                        alerta.show();
                    } else {
                        double vTotalItens = 0;
                        for (int i = 0; i < listaItens.size(); i++) {
                            vTotalItens += listaItens.get(i).getValorTotalItem();
                        }

                        // Chama tela de gravação Auditagem
                        visita.setIdClienteSGV(cliente.getCodigoSGV());
                        visita.setNomeFantasia(cliente.getNomeFantasia());
                        Intent intent = new Intent(ConsignadoActivity.this, ConsignadoAuditagemActivity.class);
                        intent.putExtra("Visita", (Serializable) visita);
                        intent.putExtra("ConsignadoItem", (Serializable) listaItens);
                        if (pEvento == 1)
                            intent.putExtra("Operacao", 2); // 0 - Inserção  / 1 - Reconsignacao / 2 - Editar
                        else
                            intent.putExtra("Operacao", 1); // 0 - Inserção  / 1 - Reconsignacao / 2 - Editar
                        intent.putExtra("IdConsignadoServer", consignado.getIdServer()); // 0 - Inserção  / 1 - Reconsignacao / 2 - Editar
                        startActivityForResult(intent, 1, null);
                    }
                }
            }
        });
    }

    private boolean VerificaDevolucaoParcial(ArrayList<ConsignadoItem> listaItens) {
        boolean ehParcial = false;
        for (ConsignadoItem item : listaItens)
        {
            if (item.getQtd() > item.getQtdAuditado())
                ehParcial = true;
        }

        return ehParcial;
    }

    private void gerarDevolucaoParcial()
    {
        // Remove as pistolagens temporárias
        estoqueDao.deletarPistolagemByIdConsignado(consignado.getId());

        // Apaga a Consignação Aberta Aneteriormente
        bdConsignado.deleteById(String.valueOf(consignado.getId()));
        bdConsignadoItem.deleteByIdConsignado(String.valueOf(consignado.getId()));
        bdConsignadoItemCodBarra.deleteByIdConsignado(String.valueOf(consignado.getId()));

        new DBVisita(ConsignadoActivity.this).encerrarVisita(visita.getId(), 16);
        Alerta alerta = new Alerta(ConsignadoActivity.this, getString(R.string.app_name), "Devolução da Consignação efetuada com Sucesso!");
        alerta.show((dialog, which) -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    private void gerarCobranca() {
        // Carrega dados das tabelas temporárias
        List<ItemVendaCombo> itens = estoqueDao.getPistolagensConsignadoItens(consignado);

        // Cria Registro de Venda
        Venda venda = new Venda();
        long codigoVenda = dbVenda.novaVendaConsignado(visitaId, visita.getIdCliente(), consignado);
        venda = dbVenda.getVendabyId((int) codigoVenda);
        for (int aa = 0; aa < itens.size(); aa++) {
            if (itens.get(aa).getQtdCombo() > 0) {
                Produto produto = new EstoqueDaoImpl(ConsignadoActivity.this).getProdutoById(itens.get(aa).getIdProduto());
                try {
                    produto.setQtde(itens.get(aa).getQtdCombo());
                    ArrayList<CodBarra> listaFinalItem = new ArrayList<>();

                    // Carrega por Iccids o que foi auditado
                    ArrayList<Iccid> listaCodigosAuditados = new ArrayList<>();

                    for (CodBarra pCodigo : itens.get(aa).getCodigosList()) {
                        ArrayList<Iccid> x = CodigoBarra.listaICCID(pCodigo);
                        for (Iccid itemIccid : x) {
                            listaCodigosAuditados.add(itemIccid);
                        }
                    }

                    // Carrega os Códigos de Barras Consignação
                    ArrayList<Iccid> listaCodigosVendidos = new ArrayList<>();
                    List<ConsignadoItemCodBarra> listaDadosCodBarra = new BDConsignadoItemCodBarra(ConsignadoActivity.this).getByIdConsignadoProduto(String.valueOf(consignado.getId()), produto.getId());
                    for (ConsignadoItemCodBarra pCod : listaDadosCodBarra) {
                        // Carrego Todos Iccid da Consignação
                        CodBarra codBarra = new CodBarra();
                        codBarra.setIdProduto(produto.getId());
                        codBarra.setGrupoProduto(produto.getGrupo());
                        codBarra.setCodBarraInicial(pCod.getCodigoBarraIni());
                        codBarra.setCodBarraFinal(pCod.getCodigoBarraFim());
                        ArrayList<Iccid> listaCodigos = CodigoBarra.listaICCID(codBarra);
                        for (Iccid itemIccid : listaCodigos) {
                            listaCodigosVendidos.add(itemIccid);
                        }
                    }

                    // Monta os Itens Vendidos
                    for (Iccid itemA : listaCodigosVendidos) {
                        boolean ehAchou = false;
                        for (Iccid itemB : listaCodigosAuditados) {
                            // Caso não conste na lista de Codigos de Barras da Consignação é porque o mesmo foi vendido
                            if (itemA.getCodigoSemVerificador().equals(itemB.getCodigoSemVerificador())) {
                                ehAchou = true;
                                break;
                            }
                        }

                        if (!ehAchou) {
                            CodBarra codBarra = new CodBarra();
                            codBarra.setIndividual(true);
                            codBarra.setIdProduto(produto.getId());
                            codBarra.setGrupoProduto(produto.getGrupo());
                            codBarra.setNomeProduto(produto.getNome());
                            codBarra.setCodBarraInicial(itemA.getCodigo());
                            listaFinalItem.add(codBarra);
                        }
                    }

                    // Atribui os Códigos de Barras Individualmente na Venda
                    itens.get(aa).setCodigosList(listaFinalItem);
                    dbVenda.addItemVenda(venda, produto, itens.get(aa).getCodigosList(), produto.getPrecovenda(), null, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Carrega dados das tabelas temporárias
        List<ItemVendaCombo> itensTemporarios = estoqueDao.getPistolagensConsignadoItens(consignado);
        this.itensNaoVendidos = new ArrayList<ItemVendaCombo>();
        // Carrega os Itens não Vendidos e Bipados na uaditagem
        for (ItemVendaCombo itemVendaCombo : itensTemporarios) {
            if (itemVendaCombo.getQtde() > 0)
                this.itensNaoVendidos.add(itemVendaCombo);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Config.CodigoVisita, visita.getId());
        Utilidades.openNewActivityForResult(ConsignadoActivity.this, FinalizarAtendActivity.class, 2, bundle);
    }

    private void chamarAuditagem() {

    }

    private void gerarDevolucao() {
        try {
            List<ItemVendaCombo> itens = estoqueDao.getPistolagensConsignadoItens(consignado);
            if (itens != null && itens.size() > 0) {
                Consignado pConsignado = new Consignado();
                pConsignado.setTipoConsignado("DEV");
                pConsignado.setIdVendedor(new DBColaborador(ConsignadoActivity.this).get().getId());
                pConsignado.setIdCliente(Integer.parseInt(visita.getIdCliente()));
                pConsignado.setDataEmissao(visita.getDataInicio());
                pConsignado.setIdVisita(visita.getId());
                pConsignado.setValorTotal(0);
                pConsignado.setLatitude(visita.getLatitude());
                pConsignado.setLongitude(visita.getLongitude());
                pConsignado.setPrecisao(visita.getPrecisao());
                pConsignado.setVersaoApp(visita.getVersaoApp());
                pConsignado.setIdConsignadoRefer(consignado.getIdServer());
                pConsignado.setStatus("1");
                pConsignado.setId((int) bdConsignado.addConsignado(pConsignado));

                List<ConsignadoItem> listaConsignacao = new ArrayList<>();
                double vValorTotal = 0;
                for (ItemVendaCombo itemConsig : itens) {
                    if (itemConsig.getQtde() > 0) {
                        ConsignadoItem pConsignadoItem = new ConsignadoItem();
                        pConsignadoItem.setIdConsignado(pConsignado.getId());
                        pConsignadoItem.setIdProduto(itemConsig.getIdProduto());
                        pConsignadoItem.setQtd(itemConsig.getQtde());
                        pConsignadoItem.setValorUnit(itemConsig.getValorUN());
                        pConsignadoItem.setValorTotalItem(itemConsig.getValorUN() * itemConsig.getQtde());
                        long pCodigo = bdConsignadoItem.addConsignadoItem(pConsignadoItem);
                        vValorTotal += pConsignadoItem.getValorTotalItem();

                        if (itemConsig.getCodigosList().size() > 0) {
                            for (CodBarra pCodBarra : itemConsig.getCodigosList()) {
                                ConsignadoItemCodBarra consignadoItemCodBarra = new ConsignadoItemCodBarra();
                                consignadoItemCodBarra.setIdConsignado(pConsignado.getId());
                                consignadoItemCodBarra.setIdConsignadoItem((int) pCodigo);
                                consignadoItemCodBarra.setCodigoBarraIni(pCodBarra.getCodBarraInicial());
                                consignadoItemCodBarra.setCodigoBarraFim(pCodBarra.getCodBarraFinal());
                                consignadoItemCodBarra.setQtd(Integer.parseInt(pCodBarra.retornaQuantidade(UsoCodBarra.GERAL)));
                                bdConsignadoItemCodBarra.addConsignadoItemCodBarra(consignadoItemCodBarra);

                                // Devolve os Iccids para a tabela
                                ArrayList<Iccid> listaCodigos = CodigoBarra.listaICCID(pCodBarra);
                                for (Iccid itemIccid : listaCodigos) {
                                    itemIccid.setAtivo(true);
                                    itemIccid.setItemCode(pCodBarra.getIdProduto());
                                    new DBIccid(ConsignadoActivity.this).addIccid(itemIccid);
                                }
                            }
                        }

                        // Atualizar Qtd Estoque
                        new DBEstoque(ConsignadoActivity.this).atualizaEstoque(pConsignadoItem.getIdProduto(), false, pConsignadoItem.getQtd());
                    }
                }

                // Remove as pistolagens temporárias
                estoqueDao.deletarPistolagemByIdConsignado(consignado.getId());

                if (pEvento != 1) {
                    // Atualiza o Status da Consignação anterior em Casod e Devolução Total
                    bdConsignado.atualizaStatusConsignado(String.valueOf(consignado.getId()), "3");
                }
                else
                {
                    // Apaga a Consignação Aberta Aneteriormente
                    bdConsignado.deleteById(String.valueOf(consignado.getId()));
                    bdConsignadoItem.deleteByIdConsignado(String.valueOf(consignado.getId()));
                    bdConsignadoItemCodBarra.deleteByIdConsignado(String.valueOf(consignado.getId()));

                    // Atualiza o Status da Nova Consignação do tipo DEV criada
                    bdConsignado.atualizaStatusConsignado(String.valueOf(pConsignado.getId()), vValorTotal, 0);
                }

                // Encerra Visita
                new DBVisita(ConsignadoActivity.this).encerrarVisita(visita.getId(), 16);

                Alerta alerta = new Alerta(this, getString(R.string.app_name), "Devolução da Consignação efetuada com Sucesso!");
                alerta.show((dialog, which) -> {
                    setResult(Activity.RESULT_OK);
                    finish();
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void popularAdapter(ArrayList<ConsignadoItem> itens, int pOperacao) {
        adapter = new ItemConsignadoAdapter(itens, ConsignadoActivity.this, pOperacao);
        mRecyclerView.setAdapter(adapter);

        double vTotalItens = 0;
        for (int i = 0; i < itens.size(); i++) {
            vTotalItens += itens.get(i).getValorTotalItem();
        }
        tvSubtotal.setText("SubTotal: " + Util_IO.formatDoubleToDecimalNonDivider(vTotalItens));
    }

    private boolean ProdutosAuditados(ArrayList<ConsignadoItem> itens) {
        boolean ehAuditado = false;
        for (int aa = 0; aa < itens.size(); aa++) {
            // Verifica se Qtd Consiganada
            if (itens.get(aa).getQtd() == (itens.get(aa).getQtdVendido() + itens.get(aa).getQtdAuditado()))
                ehAuditado = true;
            else {
                ehAuditado = false;
                break;
            }
        }

        return ehAuditado;
    }

    private boolean ProdutosVendidos(ArrayList<ConsignadoItem> itens) {
        boolean ehTemVenda = false;
        for (int aa = 0; aa < itens.size(); aa++) {
            // Verifica se Qtd Consiganada
            if (itens.get(aa).getQtdVendido() > 0) {
                ehTemVenda = true;
                break;
            } else
                ehTemVenda = false;
        }

        return ehTemVenda;
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
        if (btnGerarDevolucao.getVisibility() == View.VISIBLE && btnReconsignacao.getVisibility() == View.VISIBLE)
        {
            Alerta alerta = new Alerta(this, getString(R.string.app_name), "Você deve fazer a devolução ou a reconsignação!");
            alerta.show((dialog, which) -> {
                return;
            });
        }
        else {
            Alerta alerta = new Alerta(this, getString(R.string.app_name), "Deseja sair da opção de consignação?");
            alerta.showConfirm((dialog, which) -> fecharConsignacao(), null);
        }
    }

    private void fecharConsignacao() {
        if (consignado != null) {
            if (consignado.getStatus().equals("1")) {
                bdConsignado.deleteById(String.valueOf(consignado.getId()));
                bdConsignadoItem.deleteByIdConsignado(String.valueOf(consignado.getId()));
                bdConsignadoItemCodBarra.deleteByIdConsignado(String.valueOf(consignado.getId()));
            }

            // Remove dados das Tabelas Temporarias
            estoqueDao.deletarPistolagemByIdConsignado(consignado.getId());
        }

        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            bdConsignado.atualizaStatusConsignado(String.valueOf(consignado.getId()), "3");

            if (itensNaoVendidos.size() == 0) {
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                listaItens = new ArrayList<ConsignadoItem>();
                ArrayList<ConsignadoLimiteProduto> listaProdutos = new BDConsignadoLimiteProduto(ConsignadoActivity.this).getLimiteProdutoPorCliente(visita.getIdCliente());
                for (ConsignadoLimiteProduto produtoLimite : listaProdutos) {
                    int pos = -1;
                    for (int aa = 0; aa < this.itensNaoVendidos.size(); aa++) {
                        if (this.itensNaoVendidos.get(aa).getIdProduto().equals(produtoLimite.getIdProduto()))
                            pos = aa;
                    }

                    // Se encontrou o Produto carrega dos Itens
                    if (pos >= 0) {
                        try {
                            ConsignadoItem pConsignadoItem = new ConsignadoItem();
                            pConsignadoItem.setIdProduto(this.itensNaoVendidos.get(pos).getIdProduto());
                            pConsignadoItem.setNomeProduto(this.itensNaoVendidos.get(pos).getNomeProduto());
                            pConsignadoItem.setQtd(produtoLimite.getQuantidade());
                            pConsignadoItem.setValorUnit(this.itensNaoVendidos.get(pos).getValorUN());
                            pConsignadoItem.setValorTotalItem(this.itensNaoVendidos.get(pos).getValorUN() * this.itensNaoVendidos.get(pos).getQtde());
                            if (this.itensNaoVendidos.get(pos).getCodigosList().size() > 0) {
                                ArrayList<ConsignadoItemCodBarra> listItensConsigCodBarra = new ArrayList<>();
                                for (CodBarra pCodBarra : this.itensNaoVendidos.get(pos).getCodigosList()) {
                                    ConsignadoItemCodBarra consignadoItemCodBarra = new ConsignadoItemCodBarra();
                                    consignadoItemCodBarra.setCodigoBarraIni(pCodBarra.getCodBarraInicial());
                                    consignadoItemCodBarra.setCodigoBarraFim(pCodBarra.getCodBarraFinal());
                                    consignadoItemCodBarra.setQtd(Integer.parseInt(pCodBarra.retornaQuantidade(UsoCodBarra.GERAL)));
                                    listItensConsigCodBarra.add(consignadoItemCodBarra);
                                }

                                if (listItensConsigCodBarra.size() > 0) {
                                    pConsignadoItem.setListaCodigoBarra(listItensConsigCodBarra);
                                    listaItens.add(pConsignadoItem);
                                }
                            }
                        } catch (Exception ex) {
                            Log.d("Roni", "MERDA DE ERROOOO: " + ex.getMessage());
                        }
                    } else {
                        Produto produto = new EstoqueDaoImpl(ConsignadoActivity.this).getProdutoById(produtoLimite.getIdProduto());
                        ConsignadoItem pConsignadoItem = new ConsignadoItem();
                        pConsignadoItem.setIdProduto(produtoLimite.getIdProduto());
                        pConsignadoItem.setNomeProduto(produto.getNome());
                        pConsignadoItem.setQtd(produtoLimite.getQuantidade());
                        pConsignadoItem.setValorUnit(produto.getPrecovenda());
                        pConsignadoItem.setValorTotalItem(0);
                        pConsignadoItem.setListaCodigoBarra(new ArrayList<>());
                        listaItens.add(pConsignadoItem);
                    }
                }

                if (listaItens.size() > 0) {
                    // Chama tela de gravação Auditagem
                    try {
                        Utilidades.iniciarAtendimentoReconsigna(null, ConsignadoActivity.this, 0, 0, cliente.getId(), null, 0);
                    } catch (Exception ex) {
                        Mensagens.mensagemErro(ConsignadoActivity.this, ex.getMessage(), false);
                    }
                    visita = new DBVisita(ConsignadoActivity.this).getVisitaAtiva();
                    visita.setIdClienteSGV(cliente.getCodigoSGV());
                    visita.setNomeFantasia(cliente.getNomeFantasia());

                    // Cria cabecalho da Consignação
                    long codigo = bdConsignado.novoConsignado(visita, new DBColaborador(ConsignadoActivity.this).get().getId(), 0);

                    // Altera o Status para recarregar
                    consignado = bdConsignado.getById(String.valueOf(codigo));

                    for (int aa = 0; aa < listaItens.size(); aa++) {
                        Produto produto = new DBEstoque(ConsignadoActivity.this).getProdutoById(listaItens.get(aa).getIdProduto());
                        ConsignadoItem consignadoItem = new ConsignadoItem();
                        consignadoItem.setIdConsignado(consignado.getId());
                        consignadoItem.setQtd(listaItens.get(aa).getQtd());
                        consignadoItem.setValorTotalItem(produto.getPrecovenda() * listaItens.get(aa).getQtd());
                        consignadoItem.setIdProduto(listaItens.get(aa).getIdProduto());
                        consignadoItem.setValorUnit(produto.getPrecovenda());
                        consignadoItem.setNomeProduto(produto.getNome());
                        try {
                            int totAuditados = 0;
                            long codigoItem = bdConsignadoItem.addConsignadoItem(consignadoItem);
                            for (ConsignadoItemCodBarra itemCodBarra : listaItens.get(aa).getListaCodigoBarra()) {
                                itemCodBarra.setId(0);
                                itemCodBarra.setIdConsignado(consignado.getId());
                                itemCodBarra.setIdConsignadoItem((int) codigoItem);
                                itemCodBarra.setCodigoBarraIni(itemCodBarra.getCodigoBarraIni());
                                itemCodBarra.setCodigoBarraFim(itemCodBarra.getCodigoBarraFim());
                                itemCodBarra.setQtd(itemCodBarra.getQtd());
                                bdConsignadoItemCodBarra.addConsignadoItemCodBarra(itemCodBarra);
                                totAuditados += itemCodBarra.getQtd();
                            }

                            listaItens.get(aa).setQtdVendido(0);
                            listaItens.get(aa).setQtdAuditado(totAuditados);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Insere dados nas tabelas temporárias de Pistolagem
                    for (ConsignadoItem consigItem : listaItens) {
                        ArrayList<CodBarra> listCodBarra = new ArrayList<>();
                        Produto produto = new DBEstoque(ConsignadoActivity.this).getProdutoById(consigItem.getIdProduto());
                        int TotalQtdCod = 0;
                        for (ConsignadoItemCodBarra codBarra : consigItem.getListaCodigoBarra()) {
                            CodBarra codBarraAux = new CodBarra();
                            if (!Util_IO.isNullOrEmpty(codBarra.getCodigoBarraIni()) && !Util_IO.isNullOrEmpty(codBarra.getCodigoBarraFim()))
                                codBarraAux.setIndividual(false);
                            else
                                codBarraAux.setIndividual(true);
                            codBarraAux.setCodBarraInicial(codBarra.getCodigoBarraIni());
                            codBarraAux.setCodBarraFinal(codBarra.getCodigoBarraFim());
                            codBarraAux.setIdProduto(consigItem.getIdProduto());
                            codBarraAux.setGrupoProduto(produto.getGrupo());

                            // Roni para identificar o que estava consignado
                            codBarraAux.setAuditadoCons("S");
                            TotalQtdCod += Integer.parseInt(codBarraAux.retornaQuantidade(UsoCodBarra.GERAL));
                            listCodBarra.add(codBarraAux);
                        }

                        ItemVendaCombo itemVendaCombo = new ItemVendaCombo();
                        itemVendaCombo.setQtdCombo(0);
                        itemVendaCombo.setIdProduto(consigItem.getIdProduto());
                        itemVendaCombo.setCombo(false);
                        itemVendaCombo.setValorUN(consigItem.getValorUnit());
                        itemVendaCombo.setCodigosList(listCodBarra);
                        itemVendaCombo.setQtde(TotalQtdCod);
                        estoqueDao.incluirComboPistolagem(itemVendaCombo, consignado);
                    }

                    pEvento = 1;
                    llAuditagem.setVisibility(View.GONE);
                    ll_Gravacao.setVisibility(View.VISIBLE);
                    btnGerarCobranca.setVisibility(View.GONE);
                    btnGerarDevolucao.setVisibility(View.VISIBLE);
                    btnReconsignacao.setVisibility(View.VISIBLE);
                    btnAuditagem.setText("Auditagem");
                    popularAdapter(listaItens, 0);
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        } else if (requestCode == 4) {
            pEvento = 0;
            carregarDados(visita.getId(), Integer.parseInt(cliente.getId()));
        } else
            Log.d("Roni", "onActivityResult: Não Mandou carregar");
    }
}
