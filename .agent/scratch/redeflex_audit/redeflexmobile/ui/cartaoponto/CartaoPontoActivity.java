package com.axys.redeflexmobile.ui.cartaoponto;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CartaoPonto;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.CartaoPontoService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.login.LoginActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;

@SuppressLint("NonConstantResourceId")
public class CartaoPontoActivity extends BaseActivity implements CartaoPontoView {

    @Inject CartaoPontoAdapter adapter;
    @Inject CartaoPontoPresenter presenter;

    @BindView(R.id.cartao_ponto_nome) TextView tvNome;
    @BindView(R.id.cartao_ponto_data) TextView tvData;
    @BindView(R.id.cartao_ponto_hora_atual) TextView tvHoraAtual;
    @BindView(R.id.comprovante) LinearLayout llComprovante;
    @BindView(R.id.tela_desabilitada) RelativeLayout rlTelaDesabilitada;
    @BindView(R.id.cartao_ponto_tv_registros_hoje) TextView tvRegistrosHoje;
    @BindView(R.id.cartao_ponto_rv) RecyclerView rvCartoPonto;

    private CountDownTimer timer;
    private BottomSheetBehavior sheetBehavior;
    private List<CartaoPonto> cartoesPonto = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_cartao_ponto;
    }

    @Override
    protected void initialize() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getStringByResId(R.string.cartao_ponto_toolbar));
        }

        configuracoesIniciais();
        iniciarHora();
        iniciarComprovanteBottom();
        setupRecyclerView();
        presenter.obterPontosDoDia();

        if (!Utilidades.verificarHorarioComercial(this, true)) {
            Mensagens.horarioComercial(this, this::closeCurrentActivityGoToLogin);
        }
    }

    private void closeCurrentActivityGoToLogin(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void preencherRegistros(List<CartaoPonto> cartoesPonto) {
        adapter.setCartoesPonto(cartoesPonto);
        this.cartoesPonto = cartoesPonto;
        tvRegistrosHoje.setVisibility(View.VISIBLE);
    }

    @Override
    public void verificarListaVazia() {
        tvRegistrosHoje.setVisibility(View.GONE);
    }

    @Override
    public void exibirComprovante(CartaoPonto cartaoPonto, Colaborador colaborador) {
        presenter.obterPontosDoDia();

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            ((TextView) llComprovante.findViewById(R.id.comprovante_cnpj))
                    .setText(getString(R.string.comprovante_cnpj, colaborador.getCnpjFilial()));
            ((TextView) llComprovante.findViewById(R.id.comprovante_nome))
                    .setText(getString(R.string.comprovante_nome, colaborador.getNome()));
            ((TextView) llComprovante.findViewById(R.id.comprovante_pis))
                    .setText(getString(R.string.comprovante_pis, colaborador.getPis()));
            ((TextView) llComprovante.findViewById(R.id.comprovante_data))
                    .setText(getString(R.string.comprovante_data,
                            Util_IO.dateTimeToString(cartaoPonto.getHorario(),
                                    Config.FormatDateStringBr)));
            ((TextView) llComprovante.findViewById(R.id.comprovante_hora))
                    .setText(getString(R.string.comprovante_hora,
                            Util_IO.dateTimeToString(cartaoPonto.getHorario(),
                                    Config.FormatHoraMinutoString)));
            llComprovante.findViewById(R.id.comprovante_compartilhar).setOnClickListener(v ->
                    startActivity(Intent.createChooser(Utilidades.obterIntentCompartilhar(llComprovante),
                            getString(R.string.title_modal_share))));

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void pararServico() {
        stopService(new Intent(this, CartaoPontoService.class));
    }

    @Override
    public void mostrarErroColaborador() {
        Mensagens.colaboradorSemImeiVinculado(this);
    }

    private void configuracoesIniciais() {
        rlTelaDesabilitada.setVisibility(View.GONE);
        tvNome.setText(getStringByResId(R.string.cartao_ponto_tv_nome, presenter.obterNomeColaborador()));
        tvData.setText(getStringByResId(R.string.cartao_ponto_tv_data, Utilidades.retornaDataAtualFormatada()));
    }

    private void iniciarHora() {
        this.timer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvHoraAtual.setText(Utilidades.retornaHoraAtual());
            }

            public void onFinish() {
                // unused
            }
        };
    }

    private void iniciarComprovanteBottom() {
        llComprovante = findViewById(R.id.comprovante);
        sheetBehavior = BottomSheetBehavior.from(llComprovante);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        rlTelaDesabilitada.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NotNull View bottomSheet, float slideOffset) {
                // unused
            }
        });
    }

    private void setupRecyclerView() {
        rvCartoPonto.setLayoutManager(new LinearLayoutManager(this));
        rvCartoPonto.setNestedScrollingEnabled(false);
        rvCartoPonto.setAdapter(adapter);
    }

    private boolean verificarHorarioTolerancia() {
        CartaoPonto ultimoRegistro = cartoesPonto.get(cartoesPonto.size() - SINGLE_INT);
        Date horarioUltimoRegistro = ultimoRegistro.getHorario();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horarioUltimoRegistro);
        final int TOLERANCE_TIME_IN_SECONDS = 60;
        calendar.add(Calendar.SECOND, TOLERANCE_TIME_IN_SECONDS);

        Date horarioToleranciaUltimoRegistro = DateUtils.calendarToDate(calendar);
        Date horarioNovoRegistro = new Date();
        if (horarioNovoRegistro.before(horarioToleranciaUltimoRegistro)) {
            showOneButtonDialog(null,
                    getStringByResId(R.string.cartao_ponto_registro_efetuado),
                    null);
            return true;
        }
        return false;
    }

    @OnClick(R.id.tela_desabilitada)
    public void onClickTela() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.cartao_ponto_bt_marcar_ponto)
    public void onClickMarcarPonto() {
        if (Validacoes.validacoesHorarioInicioAplicativo(this, true)) {
            Mensagens.horarioComercialInicial(this);
        } else {
            if (!cartoesPonto.isEmpty() && verificarHorarioTolerancia()) return;

            Alerta alerta = new Alerta(this,
                    getResources().getString(R.string.cartao_ponto_dialog_titulo),
                    getResources().getString(R.string.cartao_ponto_dialog_mensagem));

            alerta.showConfirmRegistroPontoLogado((dialog, which) -> {
                        rlTelaDesabilitada.setVisibility(View.VISIBLE);
                        presenter.registrarPonto(this);
                    },
                    null);
        }
    }
}
