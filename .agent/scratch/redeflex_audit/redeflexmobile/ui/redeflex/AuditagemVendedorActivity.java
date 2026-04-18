package com.axys.redeflexmobile.ui.redeflex;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemAuditagemEstoqueAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.services.AuditagemVendedorService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class AuditagemVendedorActivity extends SharedAppCompatActivity {

    public static final int DELAY_BUTTON_DURATION = 1000;

    @BindView(R.id.lista) ExpandableListView listAuditagem;
    @BindView(R.id.fab) FloatingActionButton fabNovaAuditagem;

    private DBEstoque dbEstoque;

    private ItemAuditagemEstoqueAdapter mAdapterItem;
    private ArrayList<AuditagemEstoque> listItens;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_auditagem_estoque);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Auditagem de Estoque");
        }

        try {
            criarObjetos();
            criarEventos();
            carregarDados();
            validaClienteVazio();
        } catch (Exception ex) {
            Mensagens.mensagemErro(AuditagemVendedorActivity.this, ex.getMessage(), true);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auditagem, menu);
        MenuItem menuItemAjuda = menu.findItem(R.id.ajuda);
        menuItemAjuda.setVisible(false);
        MenuItem menuItemSalvar = menu.findItem(R.id.menu_confirma_auditagem);
        menuItemSalvar.setVisible(!listItens.isEmpty());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_confirma_auditagem:
                onConfirmSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (listItens.isEmpty()) {
            super.onBackPressed();
            return;
        }
        confirmExit();
    }

    @Override
    public void exibirConfirmacaoAuditagemEstoque() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void carregarDados() {
        try {
            listItens = (ArrayList<AuditagemEstoque>) Stream.of(dbEstoque.getAuditagemEstoqueHoje())
                    .sortBy(value -> -value.getQtdeInformada())
                    .toList();

            if (!listItens.isEmpty()) {
                AuditagemEstoque auditagemEstoque = new AuditagemEstoque();
                auditagemEstoque.setId(-1);
                auditagemEstoque.setCodigosList(new ArrayList<>());
                listItens.add(auditagemEstoque);
            }

            runOnUiThread(() -> {
                mAdapterItem = new ItemAuditagemEstoqueAdapter(
                        AuditagemVendedorActivity.this,
                        listItens,
                        false,
                        true);

                listAuditagem.setEmptyView(findViewById(R.id.empty));
                listAuditagem.setAdapter(mAdapterItem);
            });

        } catch (Exception ex) {
            runOnUiThread(() -> Mensagens.mensagemErro(AuditagemVendedorActivity.this, ex.getMessage(), false));
        }
    }

    private void criarObjetos() {
        dbEstoque = new DBEstoque(AuditagemVendedorActivity.this);
        listAuditagem.setGroupIndicator(null);
    }

    private void criarEventos() {
        compositeDisposable.add(
                RxView.clicks(fabNovaAuditagem)
                        .throttleFirst(DELAY_BUTTON_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> Utilidades.openNewActivity(
                                AuditagemVendedorActivity.this,
                                AuditagemActivity.class,
                                null,
                                true
                        ), Timber::e)
        );
    }

    private void onConfirmSelected() {
        double valorDivergencia = getValorDivergencia();
        if (valorDivergencia > 0) {
            exibirModalDivergencia(valorDivergencia);
        } else {
            exibirModalConfirmacao();
        }
    }

    private void exibirModalDivergencia(double valorDivergencia) {
        String valorFormatado = StringUtils.maskCurrencyDouble(valorDivergencia);

        Alerta alertaDivergencia = new Alerta(this, getString(R.string.app_name),
                String.format("O valor total da divergência de sua auditagem é de %s. Deseja confirmar?",
                        valorFormatado));
        Alerta alertaDivergenciaConfirmar = new Alerta(this, getString(R.string.app_name),
                String.format("Você confirmou o valor da divergência no total de %s, essa cobrança " +
                                "será somada as suas vendas a vista de hoje e será gerado o boleto " +
                                "para pagamento no próximo dia útil ou você poderá gerar essa cobrança " +
                                "acessando o menu Cobranças. Deseja confirmar?",
                        valorFormatado));

        alertaDivergencia.showConfirm((dialog, which) ->
                alertaDivergenciaConfirmar.showConfirm((dialogInterface, i) ->
                                gerarPedidoDeVenda(),
                        null
                ), null
        );
    }

    private void exibirModalConfirmacao() {
        Alerta alerta = new Alerta(this, getString(R.string.app_name), "Confirma auditagem de estoque?");
        alerta.showConfirm((dialog, which) -> gerarPedidoDeVenda(), null);
    }

    public double getValorDivergencia() {
        double total = 0;
        for (AuditagemEstoque item : listItens) {
            total += item.getValorUnitario() * (item.getQtdeReal() - item.getQtdeInformada());
        }
        return total;
    }

    private void confirmExit() {
        new Alerta(this, getString(R.string.app_name), getString(R.string.auditagem_vendedor_dialog_cancelar_mensagem))
                .showConfirm((dialog, which) -> {
                    mostrarCarregandoAuditagemEstoque();
                    new Thread(() -> {
                        dbEstoque.deleteAuditagensNaoConfirmados();
                        runOnUiThread(() -> {
                            fecharCarregandoAuditagemEstoque();
                            super.onBackPressed();
                        });
                    }).start();
                }, null);
    }

    private void gerarPedidoDeVenda() {
        new Thread(() -> {
            GPSTracker gpsTracker = new GPSTracker(this);
            if (!gpsTracker.isGPSEnabled) {
                runOnUiThread(gpsTracker::showSettingsAlert);
                return;
            }
            if (gpsTracker.isMockLocationON || gpsTracker.areThereMockPermissionApps()) {
                runOnUiThread(gpsTracker::showMockAlert);
                return;
            }

            Stream.ofNullable(listItens)
                    .forEach(auditagemEstoque ->
                            dbEstoque.confirmaAuditagemProcessando(auditagemEstoque));

            startService(new Intent(this, AuditagemVendedorService.class));
        }).start();
    }

    private void validaClienteVazio() {
        DBColaborador colaborador = new DBColaborador(this);
        String idCliente = colaborador.get().getIdCliente();
        if (StringUtils.isNotEmpty(idCliente)) {
            Cliente cliente = new DBCliente(this).getById(idCliente);
            if (cliente == null || StringUtils.isEmpty(cliente.getId())) {
                new Alerta(this,
                        getString(R.string.app_name),
                        getString(R.string.auditagem_estoque_sem_cliente_mensagem))
                        .show((dialog, which) -> finish());
            }
        } else {
            new Alerta(this,
                    getString(R.string.app_name),
                    getString(R.string.auditagem_estoque_sem_cliente_mensagem))
                    .show((dialog, which) -> finish());
        }
    }
}