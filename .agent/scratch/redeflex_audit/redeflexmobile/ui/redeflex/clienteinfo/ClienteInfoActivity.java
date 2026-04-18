package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import com.axys.redeflexmobile.shared.models.ResumoVendasCliente;
import com.axys.redeflexmobile.shared.adapters.InformacaoCorbanTransacaoAdapter;
import com.axys.redeflexmobile.shared.adapters.OportunidadeVendaAdapter;
import com.axys.redeflexmobile.shared.bd.DBInformacaoCorban;
import com.axys.redeflexmobile.shared.bd.DBInformacaoCorbanTransacao;
import com.axys.redeflexmobile.shared.models.InformacaoCorban;
import com.axys.redeflexmobile.shared.models.InformacaoCorbanTransacao;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.redeflex.Lista_OportunidadeVendaActivity;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.bd.DBPOS;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.enums.EnumClienteModeloValor;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.models.UltimaVendaCliente;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoItem;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoPagerAdapter.IClienteInfoPagerAdapterListener;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking.ClientHomeBankingActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes.ClientMdrTaxesActivity;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.axys.redeflexmobile.shared.enums.EnumClienteModeloValor.POSITIVO;
import static com.axys.redeflexmobile.ui.redeflex.Config.KEY_INFO_CLIENT_MDR_TAXES;
import static com.axys.redeflexmobile.ui.redeflex.Config.KEY_INFO_CLIENT_MDR_TAXES_AUTOMATIC;

@SuppressLint("NonConstantResourceId")
public class ClienteInfoActivity extends AppCompatActivity implements IClienteInfoPagerAdapterListener {

    private static final int EMPTY_VALUE = 0;

    @BindView(R.id.cliente_info_tv_tipo)
    TextView tvType;
    @BindView(R.id.cliente_info_tv_cliente)
    TextView tvCliente;
    @BindView(R.id.cliente_info_tv_clienteEndereco)
    TextView tvEndereco;
    @BindView(R.id.cliente_info_tv_clienteDocumento)
    TextView tvDocumento;
    @BindView(R.id.cliente_info_tv_situacao)
    TextView tvSituacao;
    @BindView(R.id.cliente_info_tv_curva_chip)
    TextView tvCurvaChip;
    @BindView(R.id.cliente_info_tv_curva_recarga)
    TextView tvCurvaRecarga;
    @BindView(R.id.cliente_info_tv_curva_adiquirencia)
    TextView tvCurvaAdiquirencia;
    @BindView(R.id.cliente_info_tv_ultima_visita)
    TextView tvUltimaVisita;
    @BindView(R.id.cliente_info_tv_proxima_visita)
    TextView tvProximaVisita;
    @BindView(R.id.cliente_info_tv_limite_compra)
    TextView tvLimiteCompra;
    @BindView(R.id.cliente_info_tv_saldo_compra)
    TextView tvSaldoCompra;
    @BindView(R.id.cliente_info_tv_boletos)
    TextView tvBoletos;
    @BindView(R.id.cliente_info_tv_limite_valor_inadimplente)
    TextView tvValorInadimplente;
    @BindView(R.id.cliente_info_tv_vencimento)
    TextView tvVencimento;
    @BindView(R.id.cliente_info_tv_fechamento)
    TextView tvFechamento;

    @BindView(R.id.cliente_info_tv_antecipacao)
    TextView tvAntecipacao;
    @BindView(R.id.cliente_info_tv_mcc)
    TextView tvMcc;
    @BindView(R.id.cliente_info_tv_segmento)
    TextView tvSegmento;
    @BindView(R.id.cliente_info_tv_classificacao)
    TextView tvClassificacao;
    @BindView(R.id.cliente_info_iv_fisico)
    ImageView ivFisico;
    @BindView(R.id.cliente_info_iv_eletronico)
    ImageView ivEletronico;
    @BindView(R.id.cliente_info_iv_adquirencia)
    ImageView ivAdquirencia;
    @BindView(R.id.cliente_info_iv_app_flex)
    ImageView ivAppFlex;
    @BindView(R.id.cliente_info_iv_corban)
    ImageView ivCorban;

    @BindView(R.id.cliente_info_iv_pix)
    ImageView ivPix;

    @BindView(R.id.cliente_linear_fisico)
    LinearLayout lnFisico;
    @BindView(R.id.cliente_linear_recargas)
    LinearLayout lnRecargas;
    @BindView(R.id.cliente_linear_adquirencia)
    LinearLayout lnAdquirencia;
    @BindView(R.id.cliente_linear_flexbank)
    LinearLayout lnAppFlex;
    @BindView(R.id.cliente_linear_corban)
    LinearLayout lnCorban;

    @BindView(R.id.cliente_info_rl_pos_container)
    RelativeLayout rlPosContainer;
    @BindView(R.id.cliente_info_tv_pos_descricao)
    TextView tvPosDescricao;
    @BindView(R.id.cliente_info_tv_pos_modelo)
    TextView tvPosModelo;
    @BindView(R.id.cliente_info_tv_pos_dias)
    TextView tvPosDias;
    @BindView(R.id.cliente_info_tv_pos_ver_todos)
    TextView tvPosVerTodos;

    @BindView(R.id.cliente_info_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.cliente_info_view_pager)
    ClienteInfoViewPager viewPager;
    @BindView(R.id.cliente_info_cv_financeiras)
    CardView cvFinanceiras;
    @BindView(R.id.bt_mdr_taxes)
    Button btMdrTaxes;
    @BindView(R.id.bt_bank_home)
    Button btBankHome;

    // Aba Corban
    @BindView(R.id.cliente_info_cv_corban)
    CardView cv_corban;
    @BindView(R.id.cliente_info_layout_infocorban)
    LinearLayout lninfocorban;
    @BindView(R.id.cliente_info_layout_infocorbandetalhes)
    LinearLayout lninfocorbandetalhes;
    @BindView(R.id.cliente_infocorban_tv_codCorban)
    TextView tv_codCorban;
    @BindView(R.id.cliente_infocorban_tv_loja)
    TextView tv_loja;
    @BindView(R.id.cliente_infocorban_tv_dataativacao)
    TextView tv_dataativacao;
    @BindView(R.id.cliente_infocorban_tv_curva)
    TextView tv_curva;
    @BindView(R.id.cliente_infocorban_tv_limiteDiario)
    TextView tv_limiteDiario;
    @BindView(R.id.cliente_infocorban_tv_situacao)
    TextView tv_situacao;
    @BindView(R.id.cliente_infocorban_tv_valor)
    TextView tv_valor;
    @BindView(R.id.cliente_infocorban_tv_dias)
    TextView tv_dias;
    @BindView(R.id.cliente_infocorban_tv_dataUltTransacao)
    TextView tv_dataUltTransacao;
    @BindView(R.id.cliente_infocorban_lst_transacoes)
    ListView lst_transacoes;
    @BindView(R.id.cliente_infocorban_btnExpand)
    ImageButton btnExpand;
    @BindView(R.id.cliente_infocorban_tv_infoatualizacao)
    TextView tv_infoatualizacao;

    // Aba POS Adquirência/Recarga
    @BindView(R.id.ll_POSAdquirencia)
    CardView llPOSAdquirencia;
    @BindView(R.id.ll_POSRecarga)
    CardView llPOSRecarga;
    @BindView(R.id.cliente_info_tv_status_pos)
    TextView tvStatusPos;
    @BindView(R.id.cliente_info_tv_status_pos_pix)
    TextView tvStatusPosPix;

    @BindView(R.id.cliente_info_tv_data_ultima_transacao)
    TextView tvDataUltimaTransacao;
    @BindView(R.id.cliente_info_tv_valor_transacionado)
    TextView tvValorTransacionado;
    @BindView(R.id.cliente_info_tv_valor_transacionado_pix)
    TextView tvValorTransacionadoPix;

    @BindView(R.id.cliente_info_tv_valor_transmesatual)
    TextView tvValorTransMesAtual;
    @BindView(R.id.cliente_info_tv_valor_transmesatual_pix)
    TextView tvValorTransMesAtualPix;


    @BindView(R.id.cliente_info_tv_valor_transmesanterior)
    TextView tvValorTransMesAnterior;
    @BindView(R.id.cliente_info_tv_valor_transmesanterior_pix)
    TextView tvValorTransMesAnteriorPix;


    @BindView(R.id.cliente_info_tv_status_pos_recarga)
    TextView tvStatusPosRecarga;
    @BindView(R.id.cliente_info_tv_data_ultima_transacao_recarga)
    TextView tvDataUltimaTransacaoRecarga;
    @BindView(R.id.cliente_info_tv_valor_recarga_transacionado)
    TextView tvValorRecarga_Transacionado;
    @BindView(R.id.cliente_info_tv_valor_recarga_transmesatual)
    TextView tvValorRecarga_TransMesAtual;
    @BindView(R.id.cliente_info_tv_valor_recarga_transmesanterior)
    TextView tvValorRecarga_TransMesAnterior;

    private DBCliente dbCliente;
    private DBSegmento dbSegmento;
    private DBVisita dbVisita;
    private DBLimite dbLimite;
    private DBPreco dbPreco;
    private DBRota dbRota;
    private DBPOS dbpos;
    private DBTaxaMdr dbTaxaMdr;
    private DBInformacaoCorban dbInformacaoCorban;
    private DBInformacaoCorbanTransacao dbInformacaoCorbanTransacao;

    private Cliente cliente;
    private InformacaoCorbanTransacaoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_info);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_toolbar_client_information);
        }

        inicializarElementos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        viewPager.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializar();
        setPixMode(false);
    }

    private void inicializarElementos() {
        dbCliente = new DBCliente(this);
        dbVisita = new DBVisita(this);
        dbLimite = new DBLimite(this);
        dbPreco = new DBPreco(this);
        dbRota = new DBRota(this);
        dbpos = new DBPOS(this);
        dbSegmento = new DBSegmento(this);
        dbTaxaMdr = new DBTaxaMdr(this);
        dbInformacaoCorban = new DBInformacaoCorban(this);
        dbInformacaoCorbanTransacao = new DBInformacaoCorbanTransacao(this);
    }

    private void inicializar() {
        String codigo = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            codigo = bundle.getString(Config.CodigoCliente);
        }

        if (codigo == null) {
            Mensagens.clienteNaoEncontrado(this, true);
            return;
        }

        cliente = dbCliente.getById(codigo);
        if (cliente == null) {
            Mensagens.clienteNaoEncontrado(this, true);
            return;
        }

        alimentarDados();

        // Corban
        lninfocorban.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        if (cliente.getClienteCorban()) {
            cv_corban.setVisibility(View.VISIBLE);
            carregaDadosCorban(cliente);
        } else
            cv_corban.setVisibility(View.GONE);
    }

    private void carregaDadosCorban(Cliente cliente) {
        // Carrega Dados Geral / Financeiro Corban
        ArrayList<InformacaoCorban> informacaoCorbans = new ArrayList<>();
        ArrayList<InformacaoCorbanTransacao> informacaoCorbanTransacaos = new ArrayList<InformacaoCorbanTransacao>();
        informacaoCorbans = dbInformacaoCorban.getInformacaoCorban("AND [idcliente] = ?", new String[]{cliente.getId()});
        informacaoCorbanTransacaos = dbInformacaoCorbanTransacao.getInformacaoCorbanTransacao("AND [idcliente] = ?", new String[]{cliente.getId()});

        setLabelCorban(tv_codCorban, "Código Corban:", "");
        setLabelCorban(tv_loja, "Loja:", "");
        setLabelCorban(tv_dataativacao, "Data Ativação:", "");
        setLabelCorban(tv_curva, "Curva:", "");
        setLabelCorban(tv_limiteDiario, "Limite Operacional Diário:", "");
        setLabelCorban(tv_situacao, "Situação:", "");
        setLabelCorban(tv_valor, "Valor da Inadimplência: R$ ", "");
        setLabelCorban(tv_dias, "Dias em Atraso:", "");
        setLabelCorban(tv_infoatualizacao, "Informações atualizadas:", "");
        setLabelCorban(tv_dataUltTransacao, "Data Ultima Transação:", "");
        lst_transacoes.setEmptyView(findViewById(R.id.empty));

        if (informacaoCorbans.size() > 0) {
            setLabelCorban(tv_codCorban, "Código Corban:", Integer.toString(informacaoCorbans.get(0).getCodigocorban()));

            if (informacaoCorbans.get(0).getLoja() != null)
                setLabelCorban(tv_loja, "Loja:", informacaoCorbans.get(0).getLoja());

            setLabelCorban(tv_dataativacao, "Data Ativação:", Util_IO.dateTimeToString(informacaoCorbans.get(0).getDataativacao(), "dd/MM/yyyy"));
            setLabelCorban(tv_curva, "Curva:", "");
            setLabelCorban(tv_limiteDiario, "Limite Operacional Diário:", Util_IO.formatDoubleToDecimalNonDivider(informacaoCorbans.get(0).getLod()));
            setLabelCorban(tv_situacao, "Situação:", informacaoCorbans.get(0).getSituacao());
            setLabelCorban(tv_valor, "Valor da Inadimplência: R$ ", Util_IO.formatDoubleToDecimalNonDivider(informacaoCorbans.get(0).getValor()));
            setLabelCorban(tv_dias, "Dias em Atraso:", Integer.toString(informacaoCorbans.get(0).getDias()));
            setLabelCorban(tv_infoatualizacao, "Informações atualizadas:", Util_IO.dateTimeToString(informacaoCorbans.get(0).getDataatualizacao(), "dd/MM/yyyy"));
            setLabelCorban(tv_dataUltTransacao, "Data Ultima Transação:", Util_IO.dateTimeToString(informacaoCorbans.get(0).getDataultimatransacao(), "dd/MM/yyyy"));

            lst_transacoes.setAdapter(new InformacaoCorbanTransacaoAdapter(this, informacaoCorbanTransacaos));

            // Ajustar Tamanho da ListView
            setListViewHeightBasedOnChildren(lst_transacoes);
        }
    }

    private void alimentarDados() {
        tvCliente.setText(cliente.mountClientInfo());

        tvEndereco.setText("Endereço: " + cliente.retornaEnderecoCompleto());
        tvDocumento.setText("CNPJ/CPF: " + StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()));

        setLabel(tvCurvaChip, "Curva Chip:", cliente.getCurvaChip());
        setLabel(tvCurvaRecarga, "Curva Recarga:", cliente.getCurvaRecarga());
        setLabel(tvCurvaAdiquirencia, "Curva Adquirencia:", cliente.getCurvaAdquirencia());
        setLabel(tvProximaVisita, "Próxima Visita:", dbRota.getProximaVisita(cliente.getId()));

        setLabel(tvSituacao, "Situação:", cliente.retornaSituacao());
        setLabel(tvBoletos, "Qtd. Boletos em aberto:",
                cliente.getQtdBoletoPendente() <= EMPTY_VALUE
                        ? null
                        : String.valueOf(cliente.getQtdBoletoPendente()));
        setLabel(tvValorInadimplente, "Valor da Inadimplência:",
                cliente.getValorBoletoPendente() <= EMPTY_VALUE
                        ? null
                        : Util_IO.formatDoubleToDecimalNonDivider(cliente.getValorBoletoPendente()));

        setLabel(tvFechamento, "Fechamento:", cliente.getFechamentoFatura());
        setLabel(tvVencimento, "Vencimento:", cliente.getVencimentoFatura());

        LimiteCliente limiteCliente = dbLimite.getByIdCliente(cliente.getId());
        if (limiteCliente != null) {
            setLabel(tvLimiteCompra, "Limite de compra (itens físicos):",
                    Util_IO.formatDoubleToDecimalNonDivider(limiteCliente.getLimite()));
            setLabel(tvSaldoCompra, "Saldo disponível (itens físicos):",
                    Util_IO.formatDoubleToDecimalNonDivider(limiteCliente.getSaldo()));
        } else {
            setLabel(tvLimiteCompra, "Limite de compra (itens físicos):", null);
            setLabel(tvSaldoCompra, "Saldo disponível (itens físicos):", null);
        }

        Visita visita = dbVisita.retornaUltimaVisitaByClienteId(cliente.getId());
        if (visita != null) {
            setLabel(tvUltimaVisita, "Última Visita:", Utilidades.getDateName(visita.getDataInicio()));
        } else {
            setLabel(tvUltimaVisita, "Última Visita:", null);
        }

        if (cliente.isAntecipacaoAutomatica() != null) {
            setLabel(
                    tvAntecipacao,
                    getString(R.string.label_antecipation),
                    cliente.isAntecipacaoAutomatica() ? getString(R.string.sim) : getString(R.string.nao)
            );
        }

        String clientType = cliente.selectClientAdquirenciaType();
        if (clientType != null) {
            tvType.setVisibility(View.VISIBLE);
            setLabel(tvType, "Cliente Tipo:", clientType);
        } else {
            tvType.setVisibility(View.GONE);
        }

        inicializarModelosCliente();
        inicializarUltimaPos();
        inicializarListagem();
        ocultarFinanceiras();

        initializerMccLabel();
        initializerSegmentLabel();
        initializerClassificacaoLabel();

        initializerActionsButtons();
    }

    private void inicializarModelosCliente() {
        lnFisico.setVisibility(View.GONE);
        lnRecargas.setVisibility(View.GONE);
        lnAdquirencia.setVisibility(View.GONE);
        lnAppFlex.setVisibility(View.GONE);
        lnCorban.setVisibility(View.GONE);

        if (EnumClienteModeloValor.getEnumByValue(cliente.getClienteFisico()).equals(POSITIVO)) {
            lnFisico.setVisibility(View.VISIBLE);
        }
        if (EnumClienteModeloValor.getEnumByValue(cliente.getClienteEletronico()).equals(POSITIVO)) {
            lnRecargas.setVisibility(View.VISIBLE);
        }
        if (EnumClienteModeloValor.getEnumByValue(cliente.getClienteAdquirencia()).equals(POSITIVO)) {
            lnAdquirencia.setVisibility(View.VISIBLE);
        }
        if (EnumClienteModeloValor.getEnumByValue(cliente.getClienteAppFlex()).equals(POSITIVO)) {
            lnAppFlex.setVisibility(View.VISIBLE);
        }
        if (EnumClienteModeloValor.getEnumByValue(cliente.getClienteCorban()).equals(POSITIVO)) {
            lnCorban.setVisibility(View.VISIBLE);
        }
    }

    private void inicializarUltimaPos() {
        InformacaoGeralPOS ultimaTransacao = dbpos.obterUltimaInformacoesGeraisPOSPorId(cliente.getId());
        if (ultimaTransacao == null) {
            rlPosContainer.setVisibility(View.GONE);
            return;
        }

        rlPosContainer.setVisibility(View.VISIBLE);
        tvPosDescricao.setText(ultimaTransacao.getDescricao());
        tvPosModelo.setText(ultimaTransacao.getModelo());

        if (ultimaTransacao.getDataUltimaTransacao() != null) {
            int dias = Utilidades.daybetween(ultimaTransacao.getDataUltimaTransacao(), new Date());
            tvPosDias.setText(getResources().getQuantityString(R.plurals.dias, dias, dias));
        } else
            tvPosDias.setText("");

        tvPosVerTodos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, cliente.getId());
            if (getIntent().getExtras() != null) {
                boolean inicioPelaVenda = getIntent().getExtras()
                        .getBoolean(ClienteInfoPosList.INICIO_PELA_VENDA, false);
                bundle.putBoolean(ClienteInfoPosList.INICIO_PELA_VENDA, inicioPelaVenda);
            }
            Utilidades.openNewActivity(this, ClienteInfoPosList.class, bundle, false);
        });

        // Alimentar as Abas de POS Adquirência e Recarga
        List<InformacaoGeralPOS> lista_POS = dbpos.obterInformacoesGeraisPOSPorId(cliente.getId());

        if (lista_POS.size() <= 0) {
            llPOSAdquirencia.setVisibility(View.GONE);
            llPOSRecarga.setVisibility(View.GONE);
        } else {
            llPOSAdquirencia.setVisibility(View.VISIBLE);
            llPOSRecarga.setVisibility(View.VISIBLE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    Config.FormatDateStringBr,
                    new Locale("pt", "BR")
            );

            double ValorTransAdq = 0, ValorTransAtualAdq = 0, ValorTransAntAdq = 0;
            double ValorTransRec = 0, ValorTransAtualRec = 0, ValorTransAntRec = 0;
            double ValorTransPix = 0, ValorTransAtualPix = 0, ValorTransAntPix = 0;

            String StatusPOS = lista_POS.get(0).getDescricao(), StatusPosRecarga = lista_POS.get(0).getTransacionadorecarga();
            Date DataAdquirencia = lista_POS.get(0).getDataUltimaTransacaoAdquirencia(), DataRecarga = lista_POS.get(0).getDataUltimaVendaRecarga();

            for (int aa = 0; aa < lista_POS.size(); aa++) {
                ValorTransAdq += lista_POS.get(aa).getValorTransacionadoAdquirencia();
                ValorTransAtualAdq += lista_POS.get(aa).getValortransmesatual();
                ValorTransAntAdq += lista_POS.get(aa).getValortransmesanterior();

                ValorTransRec += lista_POS.get(aa).getValorrecarga_transacionado();
                ValorTransAtualRec += lista_POS.get(aa).getValorrecarga_transmesatual();
                ValorTransAntRec += lista_POS.get(aa).getValorrecarga_transmesanterior();

                ValorTransPix += lista_POS.get(aa).getValorPix();
                ValorTransAtualPix += lista_POS.get(aa).getValorPixTransMesAtual();
                ValorTransAntPix += lista_POS.get(aa).getValorPixTransMesAnterior();

                if (lista_POS.get(aa).getDescricao() != null) {
                    if (lista_POS.get(aa).getDescricao().equalsIgnoreCase("TRANSACIONANDO")) {
                        StatusPOS = lista_POS.get(aa).getDescricao().toUpperCase();
                    }
                }

                if (lista_POS.get(aa).getTransacionadorecarga() != null) {
                    if (lista_POS.get(aa).getTransacionadorecarga().equalsIgnoreCase("TRANSACIONANDO")) {
                        StatusPosRecarga = lista_POS.get(aa).getTransacionadorecarga().toUpperCase();
                    }
                }

                if (lista_POS.get(aa).getDataUltimaTransacaoAdquirencia() != null) {
                    if (DataAdquirencia.after(lista_POS.get(aa).getDataUltimaTransacaoAdquirencia()))
                        DataAdquirencia = lista_POS.get(aa).getDataUltimaTransacaoAdquirencia();
                }

                if (lista_POS.get(aa).getDataUltimaVendaRecarga() != null) {
                    if (DataRecarga.after(lista_POS.get(aa).getDataUltimaVendaRecarga()))
                        DataRecarga = lista_POS.get(aa).getDataUltimaVendaRecarga();
                }
            }

            // Data da Ultima transação (Data mais recente)
            if (DataAdquirencia != null)
                tvDataUltimaTransacao.setText(simpleDateFormat.format(DataAdquirencia));

            if (DataRecarga != null)
                tvDataUltimaTransacaoRecarga.setText(simpleDateFormat.format(DataRecarga));

            // Caso Status seja TRANSACIONADO deverá ser colocado na cor Verde senão Cor padrão Redeflex
            if (StatusPOS != null) {
                if (!StatusPOS.equalsIgnoreCase("TRANSACIONANDO")){
                    tvStatusPos.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStatusPosPix.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                }

            }

            if (StatusPosRecarga != null) {
                if (!StatusPosRecarga.equalsIgnoreCase("TRANSACIONANDO"))
                    tvStatusPosRecarga.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }

            tvStatusPos.setText(StatusPOS);
            tvStatusPosPix.setText(StatusPOS);

            tvStatusPosRecarga.setText(StatusPosRecarga);


            tvValorTransacionadoPix.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransPix));
            tvValorTransMesAtualPix.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAtualPix));
            tvValorTransMesAnteriorPix.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAntPix));

            tvValorTransacionado.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAdq));
            tvValorTransMesAtual.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAtualAdq));
            tvValorTransMesAnterior.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAntAdq));

            tvValorRecarga_Transacionado.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransRec));
            tvValorRecarga_TransMesAtual.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAtualRec));
            tvValorRecarga_TransMesAnterior.setText(Util_IO.formatDoubleToDecimalNonDivider(ValorTransAntRec));
        }
    }

    private void inicializarListagem() {
        new Thread(() -> {
            List<ClienteInfoItem> precosDiferenciados = obterPrecosDiferenciados();
            //List<ClienteInfoItem> ultimasVendas = obterUltimasVendas();
            List<ClienteInfoItem> resumoVendasCliente = carregarResumoVendasCliente();

            if (precosDiferenciados.isEmpty() && (resumoVendasCliente == null || resumoVendasCliente.isEmpty())) {
                runOnUiThread(() -> {
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                });
                return;
            }

            runOnUiThread(() -> {
                @SuppressLint("WrongCall") ClienteInfoPagerAdapter pAdapter = new ClienteInfoPagerAdapter(getSupportFragmentManager(),
                        precosDiferenciados,
                        resumoVendasCliente,
                        this);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                viewPager.setAdapter(pAdapter);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.addOnTabSelectedListener(pAdapter);
            });
        }).start();
    }

    private void setLabel(TextView field, String label, String value) {
        if (field == null || label == null) return;
        if (Util_IO.isNullOrEmpty(value)) {
            field.setText("");
            field.setVisibility(View.GONE);
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString span1 = new SpannableString(label);
        span1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, span1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(span1).append(" ").append(value);
        field.setText(builder);
    }

    private void setLabelCorban(TextView field, String label, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString span1 = new SpannableString(label);
        span1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, span1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(span1).append(" ").append(value);
        field.setText(builder);
    }

    private List<ClienteInfoItem> obterPrecosDiferenciados() {
        List<ClienteInfoItem> lista = new ArrayList<>();
        List<ClienteInfoItem> temp = Stream.ofNullable(dbPreco.getPrecoDiferenciadoPorCliente(cliente.getId()))
                .map(precoDiferenciado -> {
                    String valor = Util_IO.formatDoubleToDecimalNonDivider(precoDiferenciado.getValor());

                    return new ClienteInfoItem(precoDiferenciado.getProdutoNome(),
                            String.valueOf(precoDiferenciado.getQtdPreco()),
                            valor,
                            null,
                            null,
                            null);
                })
                .toList();

        if (!temp.isEmpty()) lista.add(new ClienteInfoItem());
        lista.addAll(temp);
        return lista;
    }

    private List<ClienteInfoItem> obterUltimasVendas() {
        List<ClienteInfoItem> lista = new ArrayList<>();
        try {

            String urlfinal = String.format("%s?idCliente=%s", URLs.ULTIMA_VENDA_CLIENTE, cliente.getId());
            UltimaVendaCliente[] retorno = Utilidades.getArrayObject(urlfinal, UltimaVendaCliente[].class);
            if (retorno == null || retorno.length == 0) {
                return lista;
            }

            List<String> operadoras = Stream.ofNullable(retorno)
                    .map(UltimaVendaCliente::getOperadora)
                    .distinct()
                    .sorted(String::compareTo)
                    .toList();

            Stream.ofNullable(operadoras).forEach(operadora -> {

                lista.add(new ClienteInfoItem(operadora, operadora));
                lista.add(new ClienteInfoItem(operadora));

                Stream.ofNullable(retorno).forEach(item -> {

                    if (item.getOperadora().equals(operadora)) {
                        lista.add(new ClienteInfoItem(item.getNomeProduto(),
                                String.valueOf(item.getQuantidade()),
                                Util_IO.dateTimeToString(item.getData(), Config.FormatDateStringBr),
                                null,
                                null,
                                operadora));
                    }
                });
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        lista.add(new ClienteInfoItem("", ""));
        return lista;
    }

    private void ocultarFinanceiras() {
        boolean temStatus = Util_IO.isNullOrEmpty(cliente.retornaSituacao());
        boolean temBoleto = cliente.getQtdBoletoPendente() == EMPTY_VALUE;
        boolean temPendencia = cliente.getValorBoletoPendente() == EMPTY_VALUE;
        if (temBoleto && temStatus && temPendencia) {
            cvFinanceiras.setVisibility(View.GONE);
        }
    }

    private void initializerSegmentLabel() {
        if (cliente.getIdSegmentoSGV() != null) {
            Segmento segmento = dbSegmento.getById(cliente.getIdSegmentoSGV());
            if (segmento != null) {
                tvSegmento.setVisibility(View.VISIBLE);
                setLabel(tvSegmento, getString(R.string.label_segments), segmento.getDescricao());
            }
        }
    }

    private void initializerClassificacaoLabel() {
        if (cliente.getPremium()) {
            tvClassificacao.setVisibility(View.VISIBLE);
            setLabel(tvClassificacao, "Classificação: ", "PREMIUM");
        }
    }

    private void initializerMccLabel() {
        if (cliente.getIdMcc() != null) {
            TaxaMdr taxaMdr = dbTaxaMdr.pegarPorMcc(String.valueOf(cliente.getIdMcc()));
            if (taxaMdr != null) {
                tvMcc.setVisibility(View.VISIBLE);
                setLabel(tvMcc, getString(R.string.label_mcc), taxaMdr.getDescriptionValue());
            }
        }
    }

    private void initializerActionsButtons() {
        if (cliente.getClienteAdquirencia()) {
            btBankHome.setVisibility(View.VISIBLE);
            btBankHome.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_INFO_CLIENT_MDR_TAXES, cliente.getId());
                Utilidades.openNewActivity(ClienteInfoActivity.this, ClientHomeBankingActivity.class, bundle, false);
            });
            btMdrTaxes.setVisibility(View.VISIBLE);
            btMdrTaxes.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_INFO_CLIENT_MDR_TAXES, cliente.getId());
                boolean antecipacaoAutomatica = cliente.isAntecipacaoAutomatica() != null && cliente.isAntecipacaoAutomatica();
                bundle.putBoolean(KEY_INFO_CLIENT_MDR_TAXES_AUTOMATIC, antecipacaoAutomatica);
                Utilidades.openNewActivity(ClienteInfoActivity.this, ClientMdrTaxesActivity.class, bundle, false);
            });
        }

        // Adquirencia
        ivAdquirencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPOSAdquirencia.setVisibility(View.VISIBLE);
                rlPosContainer.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                cv_corban.setVisibility(View.GONE);
                cvFinanceiras.setVisibility(View.GONE);
                llPOSRecarga.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                setPixMode(false);
            }
        });

        // Chip
        ivFisico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.setVisibility(View.VISIBLE);
                llPOSRecarga.setVisibility(View.GONE);
                llPOSAdquirencia.setVisibility(View.GONE);
                cv_corban.setVisibility(View.GONE);
                cvFinanceiras.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                rlPosContainer.setVisibility(View.GONE);
                setPixMode(false);
            }
        });

        // Corban
        ivCorban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_corban.setVisibility(View.VISIBLE);
                cvFinanceiras.setVisibility(View.GONE);
                llPOSAdquirencia.setVisibility(View.GONE);
                llPOSRecarga.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                rlPosContainer.setVisibility(View.GONE);
                setPixMode(false);
            }
        });

        ivAppFlex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Recarga
        ivEletronico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvFinanceiras.setVisibility(View.VISIBLE);
                llPOSRecarga.setVisibility(View.VISIBLE);
                rlPosContainer.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                cv_corban.setVisibility(View.GONE);
                llPOSAdquirencia.setVisibility(View.GONE);
                setPixMode(false);
            }
        });

        // Pix
        ivPix.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                llPOSAdquirencia.setVisibility(View.VISIBLE);
                rlPosContainer.setVisibility(View.VISIBLE);
                // Esconde os demais blocos
                viewPager.setVisibility(View.GONE);
                cv_corban.setVisibility(View.GONE);
                cvFinanceiras.setVisibility(View.GONE);
                llPOSRecarga.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                setPixMode(true);
            }
        });
    }

    private void setPixMode(boolean enabled) {
        int vPix = enabled ? View.VISIBLE : View.VISIBLE;
        int vAdq = enabled ? View.GONE : View.VISIBLE;

        // Status
        tvStatusPosPix.setVisibility(vPix);
        tvStatusPos.setVisibility(vAdq);

        // Totais (Pix)
        tvValorTransacionadoPix.setVisibility(vPix);
        tvValorTransMesAtualPix.setVisibility(vPix);
        tvValorTransMesAnteriorPix.setVisibility(vPix);

        // Totais (Adquirência)
        tvValorTransacionado.setVisibility(vAdq);
        tvValorTransMesAtual.setVisibility(vAdq);
        tvValorTransMesAnterior.setVisibility(vAdq);
    }

    private List<ClienteInfoItem> carregarResumoVendasCliente() {
        List<ClienteInfoItem> lista = new ArrayList<>();
        try {
            String urlAcesso = String.format("%s?idCliente=%s", URLs.URL_RESUMOVENDAS_CLIENTE, cliente.getId());
            ResumoVendasCliente[] retorno = Utilidades.getArrayObject(urlAcesso, ResumoVendasCliente[].class);
            if (retorno == null || retorno.length == 0) {
                return lista;
            }

            // Filtra os Tipos a serem colocados
            List<String> tipos = Stream.ofNullable(retorno)
                    .map(ResumoVendasCliente::getTipo)
                    .distinct()
                    .sorted(String::compareTo)
                    .toList();

            Stream.ofNullable(tipos).forEach(tipoConteudo -> {
                String pTitulo = "";
                if (tipoConteudo.equals("1"))
                    pTitulo = "Operadora";
                else if (tipoConteudo.equals("2"))
                    pTitulo = "Físico";
                else if (tipoConteudo.equals("3"))
                    pTitulo = "Conteúdo";

                lista.add(new ClienteInfoItem(pTitulo, tipoConteudo));
                lista.add(new ClienteInfoItem(tipoConteudo));

                Stream.ofNullable(retorno).forEach(item -> {
                    if (item.getTipo().equals(tipoConteudo)) {
                        lista.add(new ClienteInfoItem(item.getProduto(),
                                String.valueOf(item.getQuantidade()),
                                Util_IO.formatDoubleToDecimalNonDivider(item.getValorface()),
                                Util_IO.formatDoubleToDecimalNonDivider(item.getValortotal()),
                                Util_IO.dateTimeToString(item.getDatavenda(), Config.FormatDateStringBr),
                                tipoConteudo));
                    }
                });
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        lista.add(new ClienteInfoItem("", ""));
        return lista;
    }

    public void expandCorban(View view) {
        int v = (lninfocorbandetalhes.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        TransitionManager.beginDelayedTransition(lnCorban, new AutoTransition());
        lninfocorbandetalhes.setVisibility(v);
        if (lninfocorbandetalhes.getVisibility() == View.GONE)
            btnExpand.setImageResource(R.drawable.ic_arrow_up_animated);
        else
            btnExpand.setImageResource(R.drawable.ic_arrow_down_animated);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (listAdapter.getCount() * 90);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
