package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import androidx.lifecycle.Lifecycle;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.dialog.BaixaPosDialog;
import com.axys.redeflexmobile.ui.pos.detalhes.VisualizarDetalhesPosActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosListAdapter.ClienteInfoPosListItem;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.menu.MenuBottomPos;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.menu.MenuBottomSheetDialog;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.widget.LinearLayout.VERTICAL;

/**
 * @author Rogério Massa on 07/12/18.
 */

public class ClienteInfoPosList extends BaseActivity
        implements ClienteInfoPosListViewHolder.OnPOSClickListener,
        ClienteInfoPosListView {

    public static final String DETALHES_POS = "detalhesPOS";
    public static final String INICIO_PELA_VENDA = "inicio_pela_venda";

    @Inject ClienteInfoPosPresenter presenter;
    @Inject ClienteInfoPosListAdapter adapter;
    @Inject ClienteInfoPosListLocalizacao validarLocalizacao;

    @BindView(R.id.cliente_info_pos_list_rv_list) RecyclerView rvList;

    private boolean iniciouPelaVenda;


    @Override
    protected int getContentView() {
        return R.layout.activity_cliente_info_pos_list;
    }

    @Override
    protected void initialize() {
        aplicarTitulo();
        configurarRecycler();
        carregarDados();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void onClick(ClienteInfoPosListItem clienteInfoPosListItem) {
        InformacaoGeralPOS pos = presenter.pegarPos(clienteInfoPosListItem);
        MenuBottomSheetDialog.newInstance(
                clienteInfoPosListItem.posModelo,
                iniciouPelaVenda,
                menuBottomPos -> {
                    if (menuBottomPos.getType() == MenuBottomPos.EnumMenuBottomPos.DETALHES) {
                        abrirDetalhes(pos);
                        return null;
                    }

                    if (menuBottomPos.getType() == MenuBottomPos.EnumMenuBottomPos.TROCAR) {
                        trocarPos(pos);
                        return null;
                    }

                    if (menuBottomPos.getType() == MenuBottomPos.EnumMenuBottomPos.REMOVER) {
                        removerPos(pos);
                        return null;
                    }
                    return null;
                }).show(getSupportFragmentManager(), MenuBottomSheetDialog.class.getSimpleName());
    }

    @Override
    public void apresentarClienteNaoEncontrado() {
        Mensagens.clienteNaoEncontrado(this, true);
    }

    @Override
    public void apresentarErroTroca(String titulo, String mensagem) {
        if (StringUtils.isNotEmpty(titulo) && StringUtils.isNotEmpty(mensagem)) {
            apresentarDialog(titulo, mensagem);
            return;
        }

        apresentarDialog(
                R.string.cliente_info_pos_erro_troca_titulo,
                R.string.cliente_info_pos_erro_troca_mensagem
        );
    }

    @Override
    public void trocaEfetuada() {
        apresentarDialog(
                R.string.cliente_info_pos_sucesso_troca_titulo,
                R.string.cliente_info_pos_sucesso_troca_mensagem
        );
    }

    @Override
    public void apresentarErroRemocao(String titulo, String mensagem) {
        if (StringUtils.isNotEmpty(titulo) && StringUtils.isNotEmpty(mensagem)) {
            apresentarDialog(titulo, mensagem);
            return;
        }

        apresentarDialog(
                R.string.cliente_info_pos_erro_remocao_titulo,
                R.string.cliente_info_pos_erro_remocao_mensagem
        );
    }

    @Override
    public void remocaoEfetuada() {
        apresentarDialog(
                R.string.cliente_info_pos_sucesso_remocao_titulo,
                R.string.cliente_info_pos_sucesso_remocao_mensagem
        );
    }

    @Override
    public void popularAdapter(List<ClienteInfoPosListItem> lista) {
        adapter.setLista(lista);
    }

    @Override
    public boolean estaProximo(double colaboradorDistancia, int clienteDistancia, Location cliente) {
        GPSTracker gpsTracker = new GPSTracker(this);
        Location gps = new Location("gps");
        gps.setLatitude(gpsTracker.getLatitude());
        gps.setLongitude(gpsTracker.getLongitude());
        gps.setAccuracy((float) gpsTracker.getPrecisao());

        return validarLocalizacao.estaProximo(
                colaboradorDistancia,
                clienteDistancia,
                gps,
                cliente
        );
    }

    @Override
    public void apresentarErroInternet(boolean tentouRemover) {
        int titulo = R.string.cliente_info_pos_erro_troca_titulo;
        int mensagem = R.string.cliente_info_pos_erro_troca_mensagem;
        if (tentouRemover) {
            titulo = R.string.cliente_info_pos_erro_remocao_titulo;
            mensagem = R.string.cliente_info_pos_erro_remocao_mensagem;
        }

        Lifecycle.State currentState = getLifecycle().getCurrentState();
        if (currentState.isAtLeast(Lifecycle.State.STARTED)) {
            BaixaPosDialog baixaPosDialog = BaixaPosDialog.newInstance(titulo, mensagem);
            baixaPosDialog.show(getSupportFragmentManager(), baixaPosDialog.getClass().getCanonicalName());
        }
        hideLoadingDialog();
    }

    @Override
    public void fechar() {
        finish();
    }

    private void carregarDados() {
        String codigo = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            codigo = bundle.getString(Config.CodigoCliente);
            iniciouPelaVenda = bundle.getBoolean(INICIO_PELA_VENDA, false);
        }

        presenter.setClienteId(codigo);
        presenter.carregarDados();
    }

    private void aplicarTitulo() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("POS do cliente");
        }
    }

    private void configurarRecycler() {
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rvList.setAdapter(adapter);
    }

    private void abrirDetalhes(InformacaoGeralPOS informacaoGeralPOS) {
        Intent intent = new Intent(this, VisualizarDetalhesPosActivity.class);
        intent.putExtra(DETALHES_POS, new Gson().toJson(informacaoGeralPOS));
        startActivity(intent);
    }

    private void trocarPos(InformacaoGeralPOS pos) {
        Lifecycle.State currentState = getLifecycle().getCurrentState();
        if (currentState.isAtLeast(Lifecycle.State.STARTED)) {
            BaixaPosDialog.newInstance(
                    R.string.cliente_info_pos_pergunta_troca_titulo,
                    getString(R.string.cliente_info_pos_pergunta_troca_mensagem, pos.getModelo()),
                    R.string.sim,
                    R.string.nao
            )
                    .setPositiveListener(() -> presenter.trocarPos(pos))
                    .show(getSupportFragmentManager(), BaixaPosDialog.class.getSimpleName());
        }
    }

    private void removerPos(InformacaoGeralPOS pos) {
        Lifecycle.State currentState = getLifecycle().getCurrentState();
        if (currentState.isAtLeast(Lifecycle.State.STARTED)) {
            BaixaPosDialog.newInstance(
                    R.string.cliente_info_pos_pergunta_remocao_titulo,
                    getString(R.string.cliente_info_pos_pergunta_remocao_mensagem, pos.getModelo()),
                    R.string.sim,
                    R.string.nao
            )
                    .setPositiveListener(() -> presenter.removerPos(pos))
                    .show(getSupportFragmentManager(), BaixaPosDialog.class.getSimpleName());
        }
    }

    private void apresentarDialog(int titulo, int mensagem) {
        Lifecycle.State currentState = getLifecycle().getCurrentState();
        if (currentState.isAtLeast(Lifecycle.State.STARTED)) {
            BaixaPosDialog.newInstance(titulo, mensagem)
                    .setPositiveListener(this::carregarDados)
                    .show(getSupportFragmentManager(), BaixaPosDialog.class.getCanonicalName());
        }
    }

    private void apresentarDialog(String titulo, String mensagem) {
        Lifecycle.State currentState = getLifecycle().getCurrentState();
        if (currentState.isAtLeast(Lifecycle.State.STARTED)) {
            BaixaPosDialog.newInstance(titulo, mensagem)
                    .setPositiveListener(this::carregarDados)
                    .show(getSupportFragmentManager(), BaixaPosDialog.class.getCanonicalName());
        }
    }
}
