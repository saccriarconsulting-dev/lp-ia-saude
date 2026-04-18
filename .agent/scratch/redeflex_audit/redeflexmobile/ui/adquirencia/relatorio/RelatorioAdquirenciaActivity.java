package com.axys.redeflexmobile.ui.adquirencia.relatorio;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.tasks.RelatorioAdquirenciaTask;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Vitor Herrmann on 04/01/19.
 */
public class RelatorioAdquirenciaActivity extends BaseActivity {

    @BindView(R.id.relatorio_adquirencia_rv_itens) RecyclerView rvItens;
    @BindView(R.id.relatorio_adquirencia_ll_main) LinearLayout llMain;
    @BindView(R.id.relatorio_adquirencia_ll_carregando) LinearLayout llCarregando;
    @BindView(R.id.relatorio_adquirencia_ll_vazio) LinearLayout llVazio;
    @BindView(R.id.relatorio_adquirencia_sr_main) SwipeRefreshLayout srMain;

    @Inject DBAdquirencia dbAdquirencia;
    @Inject RelatorioAdquirenciaAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_relatorio_adquirencia;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getStringByResId(R.string.relatorio_adquirencia_toolbar));
        }

        iniciarRecyclerView();
        carregarTela();
        iniciarEventos();
    }

    private void iniciarEventos() {
        srMain.setOnRefreshListener(this::carregarTela);
    }

    private void iniciarRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        adapter.setList(new ArrayList<>());
        rvItens.setLayoutManager(layoutManager);
        rvItens.setHasFixedSize(true);
        rvItens.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    private void carregarTela() {
        mostrarCarregando();
        popularLista();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean temInternet() {
        boolean internet = Utilidades.isConectado(this);
        if (!internet) {
            mensagemInternet();
            return false;
        }

        return true;
    }

    private void mensagemInternet() {
        Mensagens.internet(this);
        esconderCarregando();
        mostrarLayoutVazio();
    }

    private void esconderCarregando() {
        llMain.setVisibility(View.VISIBLE);
        llCarregando.setVisibility(View.GONE);
        llVazio.setVisibility(View.GONE);
    }

    private void mostrarLayoutVazio() {
        llMain.setVisibility(View.GONE);
        llCarregando.setVisibility(View.GONE);
        llVazio.setVisibility(View.VISIBLE);
    }

    private void mostrarCarregando() {
        llMain.setVisibility(View.GONE);
        llCarregando.setVisibility(View.VISIBLE);
        llVazio.setVisibility(View.GONE);
    }

    private void popularLista() {
        if (!temInternet()) {
            removerRefresh();
            return;
        }

        DBColaborador dbColaborador = new DBColaborador(this);
        Colaborador colaborador = dbColaborador.get();
        if (colaborador == null) {
            Mensagens.colaboradorSemImeiVinculado(this);
            return;
        }
        new RelatorioAdquirenciaTask(adquirencias -> {
            if (adquirencias.exception instanceof IOException) {
                mensagemInternet();
                removerRefresh();
                return;
            }
            if (adquirencias.models.isEmpty()) {
                esconderCarregando();
                mostrarLayoutVazio();
                removerRefresh();
                return;
            }

            removerRefresh();
            esconderCarregando();
            adapter.setList(adquirencias.models);
        }).execute(String.valueOf(colaborador.getId()));
    }

    private void removerRefresh() {
        srMain.setRefreshing(false);
    }
}
