package com.axys.redeflexmobile.ui.redeflex;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.RelatorioSupervisorAdapter;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RelatorioSupervisorVendedor;
import com.axys.redeflexmobile.shared.services.tasks.RelatorioSupervisorResumoTask;
import com.axys.redeflexmobile.shared.services.tasks.RelatorioSupervisorVendasTask;
import com.axys.redeflexmobile.shared.services.tasks.RelatorioSupervisorVendedorTask;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.io.IOException;
import java.util.Locale;

public class RelatorioSupervisorActivity extends AppCompatActivity {

    public static final String NOME_MES = "MMMM";
    public static final String DATA_EUA_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter FORMATTER_DATA_EUA = DateTimeFormatter.ofPattern(DATA_EUA_FORMAT);
    public static final String DATA_BR_SIMPLES = "dd/MM/yy";
    public static final DateTimeFormatter FORMATTER_DATA_BR_SIMPLES = DateTimeFormatter.ofPattern(DATA_BR_SIMPLES);
    public static final Locale LOCALE_BR = new Locale("pt", "BR");
    public static final int DELAY_SCROLL = 200;
    private RecyclerView rvVendedores;
    private TextView tvMesAtual;
    private TextView tvDataInicial;
    private TextView tvDataFinal;
    private RelatorioSupervisorAdapter adapter;
    private DatePickerDialog pickerInicial;
    private DatePickerDialog pickerFinal;
    private LinearLayout llPesquisar;
    private LinearLayout llMain;
    private LinearLayout llCarregando;
    private LinearLayout llVazio;
    private LocalDate dataInicialSelecionada = LocalDate.now();
    private LocalDate dataFinalSelecionada = LocalDate.now();
    private Colaborador colaborador;
    private RelatorioSupervisorVendedor dataRelatorio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_supervisor);
        iniciarPropriedades();
        criarVoltar();
        carregarUsuario();
        iniciarDatas();
        iniciarAdapter();
        criarDatePickerInicial();
        criarDatePickerFinal();
        criarEventos();
        carregarTela();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregarUsuario() {
        if (!temInternet()) {
            return;
        }
        DBColaborador dbColaborador = new DBColaborador(this);
        colaborador = dbColaborador.get();
    }

    private void criarVoltar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.relatorio_supervisor_titulo));
        }
    }

    private void iniciarPropriedades() {
        rvVendedores = (RecyclerView) findViewById(R.id.relatorio_supervisor_rv_vendedores);
        tvMesAtual = (TextView) findViewById(R.id.relatorio_supervisor_tv_mes_atual);
        tvDataInicial = (TextView) findViewById(R.id.relatorio_supervisor_tv_data_inicial);
        tvDataFinal = (TextView) findViewById(R.id.relatorio_supervisor_tv_data_final);
        llPesquisar = (LinearLayout) findViewById(R.id.relatorio_supervisor_ll_pesquisar);
        llMain = (LinearLayout) findViewById(R.id.relatorio_supervisor_ll_main);
        llCarregando = (LinearLayout) findViewById(R.id.relatorio_supervisor_ll_carregando);
        llVazio = (LinearLayout) findViewById(R.id.relatorio_supervisor_ll_vazio);
    }

    private void iniciarDatas() {
        LocalDate data = LocalDate.now();
        String mesAtual = data.format(DateTimeFormatter.ofPattern(NOME_MES, LOCALE_BR));
        String dataInicial = data.with(TemporalAdjusters.firstDayOfMonth()).format(FORMATTER_DATA_BR_SIMPLES);
        String dataFinal = data.format(FORMATTER_DATA_BR_SIMPLES);

        String novoMes = Character.toString(mesAtual.charAt(0)).toUpperCase() + mesAtual.substring(1);
        tvMesAtual.setText(novoMes);
        tvDataInicial.setText(dataInicial);
        tvDataFinal.setText(dataFinal);

        dataInicialSelecionada = data.with(TemporalAdjusters.firstDayOfMonth());
        dataFinalSelecionada = data;
    }

    private void iniciarAdapter() {
        dataRelatorio = new RelatorioSupervisorVendedor();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        adapter = new RelatorioSupervisorAdapter((vendedor, position) -> {
            if (vendedor.atualizado || vendedor.idVendedor == -1) {
                adapter.notifyItemChanged(position);
                return;
            }

            dataRelatorio.idVendedor = String.valueOf(vendedor.idVendedor);
            dataRelatorio.dataFinal = dataFinalSelecionada.format(FORMATTER_DATA_EUA);
            dataRelatorio.dataInicial = dataInicialSelecionada.format(FORMATTER_DATA_EUA);

            if (!temInternet()) {
                return;
            }
            new RelatorioSupervisorVendasTask((produtos, vendasVendedor, vendasPosition) -> {
                if (produtos.exception instanceof IOException) {
                    mensagemInternet();
                    return;
                }
                vendasVendedor.produtos = produtos.models;
                vendasVendedor.atualizado = true;
                adapter.setProdutos(vendasVendedor, vendasPosition);
                adapter.notifyDataSetChanged();

                new Handler().postDelayed(
                        () -> rvVendedores.smoothScrollToPosition(vendasPosition),
                        DELAY_SCROLL
                );
            })
                    .setPosition(position)
                    .setVendedor(vendedor)
                    .execute(dataRelatorio);
        });
        rvVendedores.setLayoutManager(layoutManager);
        rvVendedores.setAdapter(adapter);
    }

    private void popularLista() {
        if (!temInternet()) {
            return;
        }
        if (colaborador == null) {
            carregarUsuario();
        }
        new RelatorioSupervisorVendedorTask(vendedores -> {
            if (vendedores.exception instanceof IOException) {
                mensagemInternet();
                return;
            }
            if (vendedores.models.isEmpty()) {
                esconderCarregando();
                mostrarLayoutVazio();
                return;
            }
            adapter.setVendedores(vendedores.models);
            dataRelatorio.idVendedor = String.valueOf(colaborador.getId());
            dataRelatorio.dataFinal = dataFinalSelecionada.format(FORMATTER_DATA_EUA);
            dataRelatorio.dataInicial = dataInicialSelecionada.format(FORMATTER_DATA_EUA);

            if (!temInternet()) {
                return;
            }
            new RelatorioSupervisorResumoTask((produtos, resumoVendedor, resumoPosition) -> {
                if (produtos.exception instanceof IOException) {
                    mensagemInternet();
                    return;
                }
                resumoVendedor.produtos = produtos.models;
                resumoVendedor.atualizado = true;
                resumoVendedor.selecionado = true;
                adapter.notifyItemChanged(resumoPosition);
                esconderCarregando();
            }).setPosition(0)
                    .setVendedor(vendedores.models.get(0))
                    .execute(dataRelatorio);
        }).execute(String.valueOf(colaborador.getId()));
    }

    private void criarDatePickerInicial() {
        LocalDate agora = LocalDate.now();
        pickerInicial = new DatePickerDialog(
                this,
                (datePicker, year, month, dayOfMonth) -> {
                    dataInicialSelecionada = LocalDate.of(year, month + 1, dayOfMonth);
                    tvDataInicial.setText(dataInicialSelecionada.format(FORMATTER_DATA_BR_SIMPLES));
                    if (dataInicialSelecionada.isAfter(dataFinalSelecionada)) {
                        Mensagens.dataMaior(RelatorioSupervisorActivity.this);
                    }
                },
                agora.getYear(),
                agora.getMonthValue(),
                agora.getDayOfMonth()
        );

        long primeiro = LocalDateTime.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long ultimo = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        pickerInicial.getDatePicker().setMinDate(primeiro);
        pickerInicial.getDatePicker().setMaxDate(ultimo);
        pickerInicial.setTitle(null);
    }

    private void criarDatePickerFinal() {
        LocalDate agora = LocalDate.now();
        pickerFinal = new DatePickerDialog(
                this,
                (datePicker, year, month, dayOfMonth) -> {
                    dataFinalSelecionada = LocalDate.of(year, month + 1, dayOfMonth);
                    tvDataFinal.setText(dataFinalSelecionada.format(FORMATTER_DATA_BR_SIMPLES));

                    if (dataFinalSelecionada.isBefore(dataInicialSelecionada)) {
                        Mensagens.dataMenor(RelatorioSupervisorActivity.this);
                    }
                },
                agora.getYear(),
                agora.getMonthValue(),
                agora.getDayOfMonth()
        );

        long primeiro = LocalDateTime.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        long ultimo = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        pickerFinal.getDatePicker().setMinDate(primeiro);
        pickerFinal.getDatePicker().setMaxDate(ultimo);
        pickerFinal.setTitle(null);
    }

    private void criarEventos() {
        tvDataInicial.setOnClickListener(view -> pickerInicial.show());
        tvDataFinal.setOnClickListener(view -> pickerFinal.show());

        llPesquisar.setOnClickListener(view -> {
            if (dataInicialSelecionada.isAfter(dataFinalSelecionada)) {
                Mensagens.dataMaior(RelatorioSupervisorActivity.this);
                return;
            }

            if (dataFinalSelecionada.isBefore(dataInicialSelecionada)) {
                Mensagens.dataMenor(RelatorioSupervisorActivity.this);
                return;
            }

            carregarTela();
        });
    }

    private void carregarTela() {
        mostrarCarregando();
        popularLista();
    }

    private void mostrarCarregando() {
        llMain.setVisibility(View.GONE);
        llCarregando.setVisibility(View.VISIBLE);
        llVazio.setVisibility(View.GONE);
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
}
