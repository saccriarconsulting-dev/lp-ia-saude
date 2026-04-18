package com.axys.redeflexmobile.ui.clientpendent;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteActivity;
import com.axys.redeflexmobile.ui.clientpendent.dialog.ConfirmDialog;
import com.axys.redeflexmobile.ui.clientpendent.dialog.PendenciaExplicacaoDialog;
import com.axys.redeflexmobile.ui.clientpendent.dialog.PendenciaMotivoDialog;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class ClientePendenteActivity extends BaseActivity implements ClientePendenteView {
    private static final int INITIAL_STRING_INDEX = 7;
    private static final int ENDING_STRING_INDEX = 35;
    private static final int BUTTON_WAIT_DURATION = 2200;
    private static final int ERROR_UPDATE_ITEM = -1;

    @BindView(R.id.rv_clientes_pendentes) ExpandableListView elvClientes;
    @BindView(R.id.loading_view) View viewLoading;
    @BindView(R.id.tv_no_items_listed) TextView tvNoItems;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject ClientePendentePresenter presenter;
    @Inject ClientePendenteAdapter adapter;

    private boolean activeDecrementalFunction = false;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String filterByClientId = "0";

    @Override
    protected int getContentView() {
        return R.layout.activity_cliente_pendente;
    }

    @Override
    protected void initialize() {
        carregarBundle();
        configureTextFormat();
        criarToolbar();
        if(filterByClientId.trim().isEmpty()){
            if (activeDecrementalFunction) {
                presenter.carregarClientesPendentesNaoRespondido();
            } else {
                presenter.carregarClientesPendentes();
            }
        }else{
            presenter.carregarClientePendenteNaoRespondidoPorClienteId(filterByClientId);
        }
        presenter.carregarPendenciasCliente();

        adapter.setListenerShowDialog(presenter::showModalMotivos);
        adapter.setListenerOpenNewActivity(presenter::abrirClienteActivity);
        adapter.setCompositeDisposable(compositeDisposable);

        elvClientes.setAdapter(adapter);
        elvClientes.setEmptyView(tvNoItems);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void popularAdapter(List<Cliente> clientes) {
        adapter.setClients(clientes);
    }

    @Override
    public void carregarPendenciasClientes(List<PendenciaCliente> pendencias) {
        adapter.setPendenciaClientes(pendencias);
    }

    @Override
    public void showModalMotivos(List<PendenciaMotivo> motivos, PendenciaCliente pendenciaCliente) {
        PendenciaMotivoDialog.newInstance(
                motivos,
                pendenciaCliente,
                this::salvarRespostaModal
        ).show(getSupportFragmentManager(), null);
    }

    @Override
    public void showModalTodosClientesRespondidos(boolean isEmpty) {
        if(filterByClientId.trim().isEmpty()) {
            if (isEmpty && activeDecrementalFunction) {
                ConfirmDialog.newInstance(this::endingActivity)
                        .show(getSupportFragmentManager(),
                                null);
            }
        }else if(isEmpty) {
            endingActivity();
        }
    }

    @Override
    public void abrirClienteActivity(String id, String latitude, String longitude) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.BLOQUEAR_VISITA, true);
        bundle.putString(Config.CodigoCliente, id);
        bundle.putString(Config.Latitude, latitude);
        bundle.putString(Config.Longitude, longitude);

        Intent intent = new Intent(this, ClienteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void salvarRespostaModal(int motivoId, PendenciaCliente pendenciaCliente) {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.isGPSEnabled()) {
            pendenciaCliente.setLatitude(gpsTracker.getLatitude());
            pendenciaCliente.setLongitude(gpsTracker.getLongitude());
            pendenciaCliente.setPrecision(gpsTracker.getPrecisao());

            if (pendenciaCliente.isExibeExplicacao()) {
                PendenciaExplicacaoDialog.newInstance(
                        this::saveResponseWithExplanation,
                        pendenciaCliente,
                        motivoId
                ).show(getSupportFragmentManager(), null);
            } else {
                pendenciaCliente.setPendenciaMotivoId(motivoId);
                presenter.salvarResposta(pendenciaCliente);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void returnUpdatedNumberRows(int numberRows) {
        if (numberRows != ERROR_UPDATE_ITEM) {
            if(filterByClientId.trim().isEmpty()) {
                if (activeDecrementalFunction) {
                    presenter.carregarClientesPendentesNaoRespondido();
                }else{
                    presenter.carregarClientesPendentes();
                }
            }else {
                presenter.carregarClientePendenteNaoRespondidoPorClienteId(filterByClientId);
            }
        } else {
            Timber.e("returnUpdatedId: ocorreu um erro ao salvar a resposta da pendencia");
        }
    }

    @Override
    public void showMainLoading() {
        viewLoading.setVisibility(View.VISIBLE);
        elvClientes.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void hideMainLoading() {
        viewLoading.setVisibility(View.GONE);
        elvClientes.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
    }

    /**
     * Load bundle data.
     */
    private void carregarBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activeDecrementalFunction = bundle.getBoolean(
                    Config.ATIVAR_DECREMENTAL_FUNCAO,
                    false
            );

            filterByClientId = bundle.getString(Config.FILTRO_PENDENCIA_CLIENTE_ID,"");
        }
    }

    /**
     * Create toolbar for the activity.
     */
    private void criarToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_clientes_pendencia));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    /**
     * Ending the current activity and route to Home ou Login.
     */
    private void endingActivity() {
        this.finish();
    }

    /**
     * Configure the text of the text that will appear
     * when do not have pendencies to show.
     */
    private void configureTextFormat() {
        SpannableString spannableString = new SpannableString(
                getResources().getString(R.string.label_not_pendencies)
        );

        ForegroundColorSpan foregroundColorSpan =
                new ForegroundColorSpan(getResources()
                        .getColor(R.color.colorPrimaryDark));

        spannableString.setSpan(foregroundColorSpan,
                INITIAL_STRING_INDEX,
                ENDING_STRING_INDEX,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvNoItems.setText(spannableString);
    }

    public interface ListenerAdapter {
        void onShowDialogWithResponses(List<PendenciaMotivo> motivos, PendenciaCliente pendenciaCliente);
    }

    public interface ListenerOpenNewActivity {
        void onOpenNewActivity(Cliente cliente);
    }

    public interface ListenerOpenModal {
        void respostaModal(int movivoId, PendenciaCliente pendenciaCliente);
    }

    public interface ListenerCloseModal {
        void sairActivity();
    }

    public void saveResponseWithExplanation(PendenciaCliente pendenciaCliente) {
        presenter.salvarResposta(pendenciaCliente);
    }
}
