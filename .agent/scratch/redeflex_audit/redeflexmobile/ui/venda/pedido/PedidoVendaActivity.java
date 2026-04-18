package com.axys.redeflexmobile.ui.venda.pedido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPermissao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.PrecoFormaCache;
import com.axys.redeflexmobile.shared.services.BlipProvider;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.redeflex.ChatbotActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ConsultarSolicitacoesPrecoDifActivity;
import com.axys.redeflexmobile.ui.redeflex.SolicitacaoPrecoDifActivity;
import com.axys.redeflexmobile.ui.redeflex.SyncActivity;
import com.axys.redeflexmobile.ui.redeflex.VendaMemoria;
import com.axys.redeflexmobile.ui.venda.bipagem.BipagemVendaActivity;
import com.axys.redeflexmobile.ui.venda.pedido.produto.PedidoVendaProdutoDialog;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class PedidoVendaActivity extends BaseActivity implements PedidoVendaView {

    public static final int TIMEOUT_CLICK = 1200;
    private static final String VISITA_ID = "pv_visita_id";

    @Inject
    PedidoVendaPresenter presenter;
    @Inject
    PedidoVendaAdapter adapter;

    @BindView(R.id.pedido_venda_rv_itens)
    RecyclerView rvProdutos;
    @BindView(R.id.pedido_venda_btn_adicionar_produto)
    AppCompatButton btnAdicionar;
    @BindView(R.id.pedido_venda_tv_cliente)
    AppCompatTextView tvCliente;
    @BindView(R.id.pedido_venda_tv_subtotal)
    AppCompatTextView tvSubtotal;
    @BindView(R.id.pedido_venda_tv_tempo_atendimento)
    AppCompatTextView tvTempo;
    @BindView(R.id.pedido_venda_btn_gravar_pedido)
    AppCompatButton btnGravar;
    @BindView(R.id.pedido_venda_btn_sincronizar)
    AppCompatButton btnSincronizar;

    private CountDownTimer timer;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static Intent iniciar(Context context, int visita) {
        Intent intent = new Intent(context, PedidoVendaActivity.class);
        intent.putExtra(VISITA_ID, visita);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.pedido_venda_layout;
    }

    @Override
    protected void initialize() {
        PrecoFormaCache.clear();
        VendaMemoria.clear();
        montarActionBar();
        iniciarRecycler();
        iniciarPresenter();
        iniciarEventos();
    }

    @Override
    public void onBackPressed() {
        cancelar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.ProdutoVenda) {
            presenter.carregarProdutos();
        }
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
        presenter.detach();
        compositeDisposable.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nova_visita, menu);
        menu.findItem(R.id.menu_audita_cliente).setVisible(false);
        menu.findItem(R.id.sync).setVisible(false);
        menu.findItem(R.id.call_chat_bot).setVisible(DeviceUtils.getChatPermission(this));

        //TODO: caso queiram voltar com a função de ajuda, alterar para true
        menu.findItem(R.id.ajuda).setVisible(false);

        // Verifica se o acesso ao Cadastro de Solicitação Preço Diferenciado está liberada
        if (new DBPermissao(this).isPermissaoLiberada("nav_solicitacao_preco_dif")) {
            menu.findItem(R.id.menu_mnuConsultarPrecoDif).setVisible(true);
            menu.findItem(R.id.menu_mnuSolicitarPrecoDif).setVisible(true);
        }
        else {
            menu.findItem(R.id.menu_mnuConsultarPrecoDif).setVisible(false);
            menu.findItem(R.id.menu_mnuSolicitarPrecoDif).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            cancelar();
            return false;
        }

        if (item.getItemId() == R.id.ajuda) {
            Utilidades.openSuporte(this);
            return false;
        }

        if (item.getItemId() == R.id.call_chat_bot) {
            Colaborador colaborador = new DBColaborador(this).get();
            String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return false;
        }

        if (item.getItemId() == R.id.menu_mnuSolicitarPrecoDif) {
            Intent intent = new Intent(this, SolicitacaoPrecoDifActivity.class);
            intent.putExtra("Cliente", presenter.retornaCliente());
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
    public void showLoading() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void hideLoading() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void popularAdapter(List<ItemVendaCombo> itens) {
        adapter.setProdutos(itens);
        VendaMemoria.setLista(itens);

        for (ItemVendaCombo item : itens) {
            PrecoFormaCache.putPrecoVenda(item.getIdProduto(), item.getValorUN());
        }
    }

    @Override
    public void popularCliente(Cliente cliente) {
        String info = String.format("%s - %s", cliente.getCodigoSGV(), cliente.getNomeFantasia());
        tvCliente.setText(info);
    }

    @Override
    public void atualizarAdicaoAdapter(ItemVendaCombo itemVenda) {
        adapter.addProdutos(itemVenda);
    }

    @Override
    public void lerIccid(int visitaId, String produtoId, String itemVendaId, int quantidade,
                         int quantidadeLido, boolean isCombo, String produtoComboId,
                         int quantidadeCombo, int quantidadeComboLido) {
        Bundle bundle = new Bundle();
        bundle.putInt(Config.CodigoVisita, visitaId);
        bundle.putString(Config.CodigoProduto, produtoId);
        bundle.putString(Config.CODIGO_ITEM_VENDA, itemVendaId);
        bundle.putInt(Config.QTD_ITEM_VENDA, quantidade);
        bundle.putInt(Config.QTD_ITEM_LIDO_VENDA, quantidadeLido);
        bundle.putBoolean(Config.PRODUTO_COMBO, isCombo);
        bundle.putString(Config.PRODUTO_COMBO_ID, produtoComboId);
        bundle.putInt(Config.QTD_ITEM_COMBO_VENDA, quantidadeCombo);
        bundle.putInt(Config.QTD_ITEM_COMBO_LIDO_VENDA, quantidadeComboLido);
        Utilidades.openNewActivityForResult(
                this,
                BipagemVendaActivity.class,
                RequestCode.ProdutoVenda,
                bundle
        );
    }

    @Override
    public void mostrarMensagem(String mensagem) {
        Context context = this;
        runOnUiThread(() -> {
            Alerta alerta = new Alerta(
                    context,
                    getString(R.string.app_name),
                    mensagem
            );
            alerta.show();
        });
    }

    @Override
    public void atualizarTotal(String total) {
        String mensagem = getString(R.string.pedido_venda_tv_subtotal, total);
        tvSubtotal.setText(mensagem);
    }

    @Override
    public void iniciarTimer() {
        timer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTempo.setText(Utilidades.retornaTempoAtendimento(presenter.dataInicioVisita()));
            }

            public void onFinish() {
                /* unused */
            }
        };

        timer.start();
    }

    @Override
    public List<ItemVendaCombo> obterDadosAdapter() {
        return adapter.obterProdutos();
    }

    @Override
    public void atualizarAdapter(int posicao) {
        runOnUiThread(() -> adapter.notifyItemChanged(posicao));
    }

    @Override
    public void fechar() {
        finish();
    }

    private void iniciarRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProdutos.setLayoutManager(linearLayoutManager);
        rvProdutos.setAdapter(adapter);
    }

    private void iniciarPresenter() {
        int visita = getIntent().getIntExtra(VISITA_ID, -1);
        presenter.informarVisita(visita);
        presenter.importarProdutosSugestaoVenda();
        btnAdicionar.setVisibility(View.VISIBLE);
        presenter.carregarCliente();
    }

    private void iniciarEventos() {
        btnGravar.setOnClickListener(v -> presenter.finalizarPedido());
        adapter.setOnTouchRead(this::lerIccid);
        adapter.setOnTouchRemove(this::removerItem);
        adapter.setOnTouchRemoveItem(this::removerItemCodBarra);

        Disposable disposableAdd = RxView.clicks(btnAdicionar)
                .throttleFirst(TIMEOUT_CLICK, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> PedidoVendaProdutoDialog
                        .newInstance(produto -> presenter.adicionarProdutoBase(produto))
                        .show(getSupportFragmentManager(), PedidoVendaProdutoDialog.class.getSimpleName(), presenter.retornaCliente().getId()));

        Disposable disposableSync = RxView.clicks(btnSincronizar)
                .throttleFirst(TIMEOUT_CLICK, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("mTipoOperacao", 1);
                    Utilidades.openNewActivity(
                            PedidoVendaActivity.this,
                            SyncActivity.class,
                            bundle,
                            false
                    );
                });

        compositeDisposable.add(disposableAdd);
        compositeDisposable.add(disposableSync);
    }

    private void removerItem(ItemVendaCombo produto, int posicao) {
        Context context = this;
        Alerta alerta = new Alerta(
                context,
                getString(R.string.app_name),
                getString(R.string.pedido_venda_alerta_remover_mensagem, produto.getNomeProduto())
        );
        alerta.showConfirm((dialog, which) -> {
            ItemVendaCombo produtoValido = adapter.obterProduto(posicao);
            if (produtoValido != null) {
                presenter.removerProduto(produtoValido);
                adapter.removerProduto(posicao);
            }
        }, null);
    }

    private void removerItemCodBarra(CodBarra codBarra, int posicao) {
        Context context = this;
        Alerta alerta = new Alerta(
                context,
                getString(R.string.app_name),
                getString(R.string.pedido_venda_alerta_remover_codigo_barra_mensagem)
        );
        alerta.showConfirm((dialog, which) -> presenter.removerCodigoBarra(codBarra, posicao), null);
    }

    private void lerIccid(String produtoId, String itemVendaId, int quantidade,
                          int quantidadeLido, boolean produtoCombo, String produtoComboId,
                          int quantidadeCombo, int quantidadeComboLido) {
        presenter.lerIccid(
                produtoId,
                itemVendaId,
                quantidade,
                quantidadeLido,
                produtoCombo,
                produtoComboId,
                quantidadeCombo,
                quantidadeComboLido
        );
    }

    private void montarActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(
                    R.string.pedido_venda_titulo
            );
        }
    }

    private void cancelar() {
        Context context = this;
        Alerta alerta = new Alerta(
                context,
                getString(R.string.app_name),
                getString(R.string.pedido_venda_alerta_cancelar)
        );
        alerta.showConfirm((dialog, which) -> presenter.cancelarVenda(), null);
    }
}
