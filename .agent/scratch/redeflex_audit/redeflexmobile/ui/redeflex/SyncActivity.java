package com.axys.redeflexmobile.ui.redeflex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.core.SyncManager;
import com.axys.redeflexmobile.service.ServiceData;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.services.bus.AdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.AtualizarClienteBus;
import com.axys.redeflexmobile.shared.services.bus.AudOpeBus;
import com.axys.redeflexmobile.shared.services.bus.BancosBus;
import com.axys.redeflexmobile.shared.services.bus.CallReasonBus;
import com.axys.redeflexmobile.shared.services.bus.CampanhaMerchanClaroMaterialBus;
import com.axys.redeflexmobile.shared.services.bus.ConsignadoBus;
import com.axys.redeflexmobile.shared.services.bus.ConsignadoLimiteClienteBus;
import com.axys.redeflexmobile.shared.services.bus.FaixaFaturamentoMensalBus;
import com.axys.redeflexmobile.shared.services.bus.FaixaPatrimonialBus;
import com.axys.redeflexmobile.shared.services.bus.FaixaSalarialBus;
import com.axys.redeflexmobile.shared.services.bus.IccidOperadoraBus;
import com.axys.redeflexmobile.shared.services.bus.InformacaoCorbanBus;
import com.axys.redeflexmobile.shared.services.bus.ProfissoesBus;
import com.axys.redeflexmobile.shared.services.bus.RegisterMigrationSubBus;
import com.axys.redeflexmobile.shared.services.bus.CadastroVendedorPOSBus;
import com.axys.redeflexmobile.shared.services.bus.CanalSuporteBus;
import com.axys.redeflexmobile.shared.services.bus.CartaoPontoBus;
import com.axys.redeflexmobile.shared.services.bus.CatalogoAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.ChamadosBus;
import com.axys.redeflexmobile.shared.services.bus.ClienteBus;
import com.axys.redeflexmobile.shared.services.bus.ClienteCadastroBus;
import com.axys.redeflexmobile.shared.services.bus.CnaeBus;
import com.axys.redeflexmobile.shared.services.bus.CobrancaBus;
import com.axys.redeflexmobile.shared.services.bus.ColaboradorBus;
import com.axys.redeflexmobile.shared.services.bus.ComandosBus;
import com.axys.redeflexmobile.shared.services.bus.ComprovanteSkyTaBusImpl;
import com.axys.redeflexmobile.shared.services.bus.DepartamentoBus;
import com.axys.redeflexmobile.shared.services.bus.DevolucaoBus;
import com.axys.redeflexmobile.shared.services.bus.EstoqueBus;
import com.axys.redeflexmobile.shared.services.bus.FilialBus;
import com.axys.redeflexmobile.shared.services.bus.FormaPagamentoBus;
import com.axys.redeflexmobile.shared.services.bus.HorarioNotificacaoBus;
import com.axys.redeflexmobile.shared.services.bus.IccidBus;
import com.axys.redeflexmobile.shared.services.bus.IsencaoBus;
import com.axys.redeflexmobile.shared.services.bus.LimiteBus;
import com.axys.redeflexmobile.shared.services.bus.LocalizacaoClienteBus;
import com.axys.redeflexmobile.shared.services.bus.MensagemBus;
import com.axys.redeflexmobile.shared.services.bus.ModeloPOSBus;
import com.axys.redeflexmobile.shared.services.bus.MotivoAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.MotivoBus;
import com.axys.redeflexmobile.shared.services.bus.MotiveMigrationSubBus;
import com.axys.redeflexmobile.shared.services.bus.OperadoraBus;
import com.axys.redeflexmobile.shared.services.bus.OsBus;
import com.axys.redeflexmobile.shared.services.bus.POSBus;
import com.axys.redeflexmobile.shared.services.bus.PendenciaBus;
import com.axys.redeflexmobile.shared.services.bus.PendenciasBus;
import com.axys.redeflexmobile.shared.services.bus.PerguntasQualidadeBus;
import com.axys.redeflexmobile.shared.services.bus.PermissaoBus;
import com.axys.redeflexmobile.shared.services.bus.PrazoNegociacaoBus;
import com.axys.redeflexmobile.shared.services.bus.PrecoBus;
import com.axys.redeflexmobile.shared.services.bus.ProjetoTradeBus;
import com.axys.redeflexmobile.shared.services.bus.ProspectBus;
import com.axys.redeflexmobile.shared.services.bus.RelatorioMetaBus;
import com.axys.redeflexmobile.shared.services.bus.RemessaBus;
import com.axys.redeflexmobile.shared.services.bus.RotaAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.RotaBus;
import com.axys.redeflexmobile.shared.services.bus.SegmentoBus;
import com.axys.redeflexmobile.shared.services.bus.SenhaClienteBus;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoMercadoriaBus;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoPidBus;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoPrecoDiferenciadoBus;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoTrocaBus;
import com.axys.redeflexmobile.shared.services.bus.SugestaoVendaBus;
import com.axys.redeflexmobile.shared.services.bus.TaxaMdrBus;
import com.axys.redeflexmobile.shared.services.bus.TaxasAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.TaxasAdquirenciaPFBus;
import com.axys.redeflexmobile.shared.services.bus.TelemetriaBus;
import com.axys.redeflexmobile.shared.services.bus.TipoContaBus;
import com.axys.redeflexmobile.shared.services.bus.TipoLogradouroBus;
import com.axys.redeflexmobile.shared.services.bus.TokenClienteBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBoletoBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBus;
import com.axys.redeflexmobile.shared.services.bus.VendaSenhaBus;
import com.axys.redeflexmobile.shared.services.bus.VendedorBus;
import com.axys.redeflexmobile.shared.services.bus.VisitaAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.VisitaBus;
import com.axys.redeflexmobile.shared.services.bus.clientinfo.ClientHomeBankingBus;
import com.axys.redeflexmobile.shared.services.bus.clientinfo.ClientTaxMdrBus;
import com.axys.redeflexmobile.shared.services.bus.clientinfo.FlagsBankBus;
import com.axys.redeflexmobile.shared.services.bus.registerrate.ProspectingClientAcquisitionBus;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Utilidades;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;

public class SyncActivity extends AppCompatActivity {

    public static final String ARGS_TIPO_OPERACAO = "mTipoOperacao";
    int tipoOperacao;
    Boolean sincronizado;
    private Context mContext;
    private ProgressBar progressBar;
    private ProgressBar progressBarCyclic;
    private TextView txtProgresso;
    private TextView txtMensagem;
    private int total = 0;

    private SyncManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Utilidades.isConectado(SyncActivity.this)) {
            setContentView(R.layout.activity_sync);
            progressBarCyclic = findViewById(R.id.progressBar_cyclic);
            progressBar = findViewById(R.id.progressBar);
            txtProgresso = findViewById(R.id.txtProgresso);
            txtMensagem = findViewById(R.id.txtMensagem);
            tipoOperacao = 0;
            sincronizado = false;

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                tipoOperacao = bundle.getInt("mTipoOperacao");
            }

            this.syncManager = new SyncManager(this);
            this.syncManager.setOnData(serviceData -> {
                if (serviceData instanceof ServiceData.Start) {
                    SyncActivity.this.runOnUiThread(this::syncStart);
                }
                if (serviceData instanceof ServiceData.Progress) {
                    SyncActivity.this.runOnUiThread(this::updateProgress);
                }
                if (serviceData instanceof ServiceData.Finished) {
                    SyncActivity.this.runOnUiThread(this::syncFinish);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
                return null;
            });
            this.syncManager.start(tipoOperacao);
        } else {
            Utilidades.retornaMensagem(SyncActivity.this, "Verifique sua conexão com a Internet", false);
            finish();
        }
    }

    private void syncStart() {
        txtProgresso.setText("0%");
        txtMensagem.setText("Sincronizando");
        progressBarCyclic.setVisibility(View.VISIBLE);
    }

    private void syncFinish() {
        progressBarCyclic.setVisibility(View.GONE);
        txtProgresso.setText("Dados sinconizados com Sucesso!");
    }

    private void updateProgress() {
        int progresso = 1;
        total += progresso;
        if (total >= 100)
            total = 100;

        progressBar.incrementProgressBy(progresso);
        txtProgresso.setText(total + "%");

        if (txtMensagem.getText().equals("Sincronizando") || txtMensagem.getText().equals("Sincronizando..."))
            txtMensagem.setText("Sincronizando.  ");
        else if (txtMensagem.getText().equals("Sincronizando.  "))
            txtMensagem.setText("Sincronizando.. ");
        else
            txtMensagem.setText("Sincronizando...");

        if (total == 100)
            txtMensagem.setText("Sincronizado!  ");
    }

    @Override
    public void onBackPressed() {
        // unused
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!sincronizado) {
            Notificacoes.sincronizacaoPendente(SyncActivity.this, "Sincronização Pendente");
            // syncTask.cancel(true);
            finish();
        }
    }
}
