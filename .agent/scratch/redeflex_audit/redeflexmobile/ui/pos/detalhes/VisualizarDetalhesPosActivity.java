package com.axys.redeflexmobile.ui.pos.detalhes;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.MainActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosList;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Vitor Herrmann on 26/12/18.
 */
public class VisualizarDetalhesPosActivity extends BaseActivity {

    private static final String TRANSACIONANDO = "TRANSACIONANDO";

    @BindView(R.id.detalhes_pos_tv_titulo) TextView tvTitulo;
    @BindView(R.id.detalhes_pos_ll_iccid) LinearLayout llIccid;
    @BindView(R.id.detalhes_pos_ll_cliente_terminal) LinearLayout llClienteTerminal;
    @BindView(R.id.detalhes_pos_ll_masters) LinearLayout llMasters;
    @BindView(R.id.detalhes_pos_ll_muxx) LinearLayout llMuxx;
    @BindView(R.id.detalhes_pos_ll_cliente_flex) LinearLayout llClienteFlex;
    @BindView(R.id.detalhes_pos_ll_numero_serie) LinearLayout llNumeroSerie;
    @BindView(R.id.detalhes_pos_ll_data_cadastro) LinearLayout llDataCadastro;
    @BindView(R.id.detalhes_pos_ll_vendedor_instalacao) LinearLayout llVendedorInstalacao;
    @BindView(R.id.detalhes_pos_ll_data_instalacao) LinearLayout llDataInstalacao;
    @BindView(R.id.detalhes_pos_ll_valor_aluguel) LinearLayout llValorAluguel;
    @BindView(R.id.detalhes_pos_ll_terminal_alocado_sgv) LinearLayout llTerminalAlocadoSGV;
    @BindView(R.id.detalhes_pos_tv_status_pos) TextView tvStatusPos;
    @BindView(R.id.detalhes_pos_tv_data_ultima_transacao) TextView tvDataUltimaTransacao;
    @BindView(R.id.detalhes_pos_tv_valor_transacionado) TextView tvValorTransacionado;
    @BindView(R.id.detalhes_pos_tv_valor_transmesatual) TextView tvValorTransMesAtual;
    @BindView(R.id.detalhes_pos_tv_valor_transmesanterior) TextView tvValorTransMesAnterior;
    @BindView(R.id.detalhes_pos_tv_status_pos_recarga) TextView tvStatusPosRecarga;
    @BindView(R.id.detalhes_pos_tv_data_ultima_transacao_recarga) TextView tvDataUltimaTransacaoRecarga;
    @BindView(R.id.detalhes_pos_tv_valor_recarga_transacionado) TextView tvValorRecarga_Transacionado;
    @BindView(R.id.detalhes_pos_tv_valor_recarga_transmesatual) TextView tvValorRecarga_TransMesAtual;
    @BindView(R.id.detalhes_pos_tv_valor_recarga_transmesanterior) TextView tvValorRecarga_TransMesAnterior;

    @Inject DBCliente dbCliente;

    @Override
    protected int getContentView() {
        return R.layout.activity_visualizar_detalhes_pos;
    }

    @Override
    protected void initialize() {
        atualizarToolbar();
        inicializar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void atualizarToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getStringByResId(R.string.detalhes_pos_toolbar));
        }
    }

    private void inicializar() {
        InformacaoGeralPOS informacaoGeralPOS = new Gson().fromJson(
                getIntent().getStringExtra(ClienteInfoPosList.DETALHES_POS), InformacaoGeralPOS.class
        );

        Cliente cliente = dbCliente.getById(String.valueOf(informacaoGeralPOS.getIdCliente()));

        if (cliente == null) {
            Alerta alerta = new Alerta(this, getString(R.string.app_name), "Cliente não encontrado");
            alerta.show((dialog, which) -> {
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
            });
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                Config.FormatDateStringBr,
                new Locale("pt", "BR")
        );

        tvTitulo.setText(informacaoGeralPOS.getModelo());

        setInformacoes(llIccid, getStringByResId(R.string.detalhes_pos_tv_iccid), informacaoGeralPOS.getIccid());
        setInformacoes(llClienteTerminal, getStringByResId(R.string.detalhes_pos_tv_cliente_terminal), cliente.getId());
        setInformacoes(llMasters, getStringByResId(R.string.detalhes_pos_tv_masters), informacaoGeralPOS.getCodigoMasters());
        setInformacoes(llMuxx, getStringByResId(R.string.detalhes_pos_tv_muxx), informacaoGeralPOS.getCodigoMuxx());
        setInformacoes(llClienteFlex, getStringByResId(R.string.detalhes_pos_tv_cliente_flex),
                !cliente.getClienteAppFlex() ? getStringByResId(R.string.nao) : getStringByResId(R.string.sim));
        setInformacoes(llNumeroSerie, getStringByResId(R.string.detalhes_pos_tv_numero_serie), informacaoGeralPOS.getNumeroSerie());
        setInformacoes(llValorAluguel, getStringByResId(R.string.detalhes_pos_tv_valor_aluguel),
                Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValorAluguel()));
        setInformacoes(llTerminalAlocadoSGV, getStringByResId(R.string.detalhes_pos_tv_terminal_alocado_sgv),
                informacaoGeralPOS.getTerminalAlocadoSGV() ? getStringByResId(R.string.sim) : getStringByResId(R.string.nao));
        setLabel(llVendedorInstalacao, getString(R.string.detalhes_pos_tv_vendedor_instalacao),
                informacaoGeralPOS.getVendedorInstalacao());
        setInformacoes(
                llDataCadastro,
                getString(R.string.detalhes_pos_tv_data_cadastro),
                informacaoGeralPOS.getDataCadastro() != null
                        ? simpleDateFormat.format(informacaoGeralPOS.getDataCadastro())
                        : null
        );
        if (informacaoGeralPOS.getDescricao() != null) {
            tvStatusPos.setText(informacaoGeralPOS.getDescricao().toUpperCase());
            if (!informacaoGeralPOS.getDescricao().equalsIgnoreCase(TRANSACIONANDO)) {
                tvStatusPos.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
        }
        tvValorTransacionado.setText(Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValorTransacionadoAdquirencia()));

        setInformacoes(
                llDataInstalacao, getStringByResId(R.string.detalhes_pos_tv_data_instalacao),
                informacaoGeralPOS.getDataInstalacaoAdquirencia() != null
                        ? simpleDateFormat.format(informacaoGeralPOS.getDataInstalacaoAdquirencia())
                        : null
        );

        if (informacaoGeralPOS.getDataUltimaTransacaoAdquirencia() != null) {
            tvDataUltimaTransacao.setText(simpleDateFormat.format(informacaoGeralPOS.getDataUltimaTransacaoAdquirencia()));
        }

        tvValorTransMesAtual.setText(Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValortransmesatual()));
        tvValorTransMesAnterior.setText(Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValortransmesanterior()));

        if (informacaoGeralPOS.getTransacionadorecarga() != null) {
            tvStatusPosRecarga.setText(informacaoGeralPOS.getTransacionadorecarga().toUpperCase());
            if (!informacaoGeralPOS.getTransacionadorecarga().equalsIgnoreCase(TRANSACIONANDO)) {
                tvStatusPosRecarga.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
        }
        if (informacaoGeralPOS.getDataUltimaVendaRecarga() != null) {
            tvDataUltimaTransacaoRecarga.setText(simpleDateFormat.format(informacaoGeralPOS.getDataUltimaVendaRecarga()));
        }
        tvValorRecarga_Transacionado.setText(Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValorrecarga_transacionado()));
        tvValorRecarga_TransMesAtual.setText(Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValorrecarga_transmesatual()));
        tvValorRecarga_TransMesAnterior.setText(Util_IO.formatDoubleToDecimalNonDivider(informacaoGeralPOS.getValorrecarga_transmesanterior()));
    }

    private void setInformacoes(LinearLayout layout, String titulo, String info) {
        if (Util_IO.isNullOrEmpty(info)) {
            layout.setVisibility(View.GONE);
            return;
        }

        ((TextView) layout.findViewById(R.id.detalhes_pos_tv_item)).setText(titulo);
        ((TextView) layout.findViewById(R.id.detalhes_pos_tv_info)).setText(info);
    }

    private void setLabel(LinearLayout field, String label, String value) {
        if (field == null || label == null) return;
        if (Util_IO.isNullOrEmpty(value)) {
            field.setVisibility(View.GONE);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString span1 = new SpannableString(label);
        span1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, span1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(span1).append(" ").append(value);
        ((TextView) field.findViewById(R.id.detalhes_pos_tv_info)).setText(builder);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(0);
        ((TextView) field.findViewById(R.id.detalhes_pos_tv_info)).setLayoutParams(params);
    }
}
