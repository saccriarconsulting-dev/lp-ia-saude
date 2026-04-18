package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_BIGGER_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_IN_CASH;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_UNTIL_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.DEBIT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CNPJ;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CPF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemBandeiraAdapter;
import com.axys.redeflexmobile.shared.adapters.ItemSolicitacaoPidCustoEfetivoAdapter;
import com.axys.redeflexmobile.shared.adapters.SolicitacaoPidAnexoAdapter;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPid;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidPOS;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidProduto;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidRede;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.enums.EnumPidAnexo;
import com.axys.redeflexmobile.shared.enums.EnumTipoCliente;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidPos;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidProduto;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorCreditoEfetivo;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorRequest;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorTaxaBandeiraRequest;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorTaxasRecomendadasResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaRAV;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.services.SolicitacaoPidService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.RetrofitClient;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBank;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBankEventListener;
import com.google.gson.Gson;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Activity_SolPID_DadosCliente extends AppCompatActivity implements FlagsBankEventListener, ItemBandeiraAdapter.selecionarBandeira, SolicitacaoPidAnexoAdapter.CameraCallback {

    LinearLayout llCliente, llProduto, llComercial, llAnexo, llPedido;
    CustomSpinner spTipoCliente, spTipoPessoa, spnMCC, spConcorrente;
    CustomEditText edtCpfCNPJ, edtCodigoSGV, edtRazaoSocial, edtNomeFantasia;
    Button btnRedeLojas, btnAddPOS, btnSimular, btnGravar;

    List<TaxaMdr> taxas;
    private ProgressDialog progressDialog;

    private SolicitacaoPid solicitacaoPID;
    private ArrayList<SolicitacaoPidTaxaMDR> listaSolicitacaoPidTaxaMDR;
    ArrayList<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> listaDadosBandeiras;

    FlagsBank llComercial_card_flags;
    private Integer selectedOptionCardFlags;

    Button btnDebito, btnCredito, btnCredito2_6x, btnCredito7_12x, btnAluguel, btnRavEventual, btnRavAutomatico;
    RecyclerView rvBandeirasModalidade;
    ItemBandeiraAdapter adapterBandeiras;

    // Comercial
    CustomEditText edtPercDebito, edtPercCredito, edtPErcCredito2a6, edtPercCredito7a12;
    CustomEditText edtFaturamentoPrevisto, edtPercConcorrencia, edtPercNovaProposta;
    TextView tvDistribuicaoTotal, tvSugeridoDebito, tvSugeridoCredito, tvSugeridoCredito2a6, tvSugeridoCredito7a12, tvDiferencaRAV, tvTituloTaxaRAV;
    CustomEditText edtNovaTaxaPercDebito, edtNovaTaxaPercCredito, edtNovaTaxaPercCredito2a6, edtNovaTaxaPercCredito7a12;
    TextView tvTaxaDebitoRecomenda, tvTaxaCreditoRecomenda, tvTaxaCredito6xRecomenda, tvTaxaCredito12xRecomenda;
    private TextWatcher textWatcherDebito, textWatcherCredito, textWatcherCredito2a6, textWatcherCredito7a12;
    LinearLayout llTaxaRAV, llComercial_TaxasNormal, llComercial_TaxasConcorrente;
    CustomEditText edtPercDebitoConcorrencia, edtPercDebitoNovaProposta, edtPercCreditoConcorrencia, edtPercCreditoNovaProposta;
    CustomEditText edtPercCredito2_6Concorrencia, edtPercCredito2_6NovaProposta, edtPercCredito7_12Concorrencia, edtPercCredito7_12NovaProposta;

    // Anexos
    CustomEditText edtObservacao;
    RecyclerView recycler_Anexos;
    SolicitacaoPidAnexoAdapter adapterAnexos;
    ArrayList<SolicitacaoPidAnexo> listaAnexos;

    // Resumo
    TextView tvDentroDaAlcada, tvAlcadaMensagem, tvReceitaTotal, tvImpostoTotal, tvReceitaLiquidaTotal, tvReceitaBrutaTotal, tvTakeRate;
    RecyclerView recycler_CreditoEfetivo;
    ItemSolicitacaoPidCustoEfetivoAdapter adapterCustoefetivo;


    private void recalculateSize() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_solicita_cliente:
                    llCliente.setVisibility(View.VISIBLE);
                    llProduto.setVisibility(View.GONE);
                    llComercial.setVisibility(View.GONE);
                    llAnexo.setVisibility(View.GONE);
                    llPedido.setVisibility(View.GONE);
                    recalculateSize();
                    return true;
                case R.id.nav_solicita_produto:
                    llCliente.setVisibility(View.GONE);
                    llProduto.setVisibility(View.VISIBLE);
                    llComercial.setVisibility(View.GONE);
                    llAnexo.setVisibility(View.GONE);
                    llPedido.setVisibility(View.GONE);
                    recalculateSize();
                    return true;
                case R.id.nav_solicita_comercial:
                    llCliente.setVisibility(View.GONE);
                    llProduto.setVisibility(View.GONE);
                    llComercial.setVisibility(View.VISIBLE);
                    llAnexo.setVisibility(View.GONE);
                    llPedido.setVisibility(View.GONE);
                    recalculateSize();
                    return true;
                case R.id.nav_solicita_anexos:
                    llCliente.setVisibility(View.GONE);
                    llProduto.setVisibility(View.GONE);
                    llComercial.setVisibility(View.GONE);
                    llAnexo.setVisibility(View.VISIBLE);
                    llPedido.setVisibility(View.GONE);
                    recalculateSize();
                    return true;
                case R.id.nav_solicita_resumo:
                    llCliente.setVisibility(View.GONE);
                    llProduto.setVisibility(View.GONE);
                    llComercial.setVisibility(View.GONE);
                    llAnexo.setVisibility(View.GONE);
                    llPedido.setVisibility(View.VISIBLE);
                    recalculateSize();
                    return true;
            }
            return false;
        }
    };

    private boolean lastImeVisible = false;


    public boolean changeImage(int itemid) {

        switch (itemid) {
            case R.id.nav_solicita_cliente:
                llCliente.setVisibility(View.VISIBLE);
                llProduto.setVisibility(View.GONE);
                llComercial.setVisibility(View.GONE);
                llAnexo.setVisibility(View.GONE);
                llPedido.setVisibility(View.GONE);
                recalculateSize();
                return true;
            case R.id.nav_solicita_produto:
                llCliente.setVisibility(View.GONE);
                llProduto.setVisibility(View.VISIBLE);
                llComercial.setVisibility(View.GONE);
                llAnexo.setVisibility(View.GONE);
                llPedido.setVisibility(View.GONE);
                recalculateSize();
                return true;
            case R.id.nav_solicita_comercial:
                llCliente.setVisibility(View.GONE);
                llProduto.setVisibility(View.GONE);
                llComercial.setVisibility(View.VISIBLE);
                llAnexo.setVisibility(View.GONE);
                llPedido.setVisibility(View.GONE);
                recalculateSize();
                return true;
            case R.id.nav_solicita_anexos:
                llCliente.setVisibility(View.GONE);
                llProduto.setVisibility(View.GONE);
                llComercial.setVisibility(View.GONE);
                llAnexo.setVisibility(View.VISIBLE);
                llPedido.setVisibility(View.GONE);
                recalculateSize();
                return true;
            case R.id.nav_solicita_resumo:
                llCliente.setVisibility(View.GONE);
                llProduto.setVisibility(View.GONE);
                llComercial.setVisibility(View.GONE);
                llAnexo.setVisibility(View.GONE);
                llPedido.setVisibility(View.VISIBLE);
                recalculateSize();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solpid_dadoscliente);


        criarObjetos();
        montarActionBar();
        criarEventos();
        AdicionaEventoTaxasMDR();

        // Inicializa Bandeiras
        IniciarBandeiras();
        HabilitaCamposOnClickProdutos();

        Bundle bundle = getIntent().getExtras();
        solicitacaoPID = (SolicitacaoPid) bundle.getSerializable("Solicitacao");
        if (Util_IO.isNullOrEmpty(solicitacaoPID.getCpfCnpj())) {
            solicitacaoPID = new SolicitacaoPid();
            listaSolicitacaoPidTaxaMDR = new ArrayList<>();
        } else {
            listaSolicitacaoPidTaxaMDR = solicitacaoPID.getListaTaxas();
            carregarDados(solicitacaoPID);
        }

        // Anexos
        prepareAttachmentList();

        final View root = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int imeHeight = ime.bottom;

            if (imeVisible != lastImeVisible) {
                lastImeVisible = imeVisible;
                onKeyboardVisibilityChanged(imeVisible, imeHeight);
            }
            return insets; // não consome
        });

        // força a (re)entrega dos insets
        ViewCompat.requestApplyInsets(root);

        configureBottomNav();
    }


    private View itemCliente, itemProduto, itemComercial, itemAnexos, itemResumo;
    private List<View> bottomItems;

    private void configureBottomNav() {
        itemCliente = findViewById(R.id.nav_solicita_cliente);
        itemProduto = findViewById(R.id.nav_solicita_produto);
        itemComercial = findViewById(R.id.nav_solicita_comercial);
        itemAnexos = findViewById(R.id.nav_solicita_anexos);
        itemResumo = findViewById(R.id.nav_solicita_resumo);

        bottomItems = Arrays.asList(itemCliente, itemProduto, itemComercial, itemAnexos, itemResumo);

        View.OnClickListener listener = v -> {
            selectBottomItem(v.getId());
            changeImage(v.getId());
        };

        for (View it : bottomItems) it.setOnClickListener(listener);

        // Seleção inicial
        selectBottomItem(R.id.nav_solicita_cliente);
        changeImage(R.id.nav_solicita_cliente);
    }


    private void selectBottomItem(int selectedId) {
        for (View v : bottomItems) {
            v.setSelected(v.getId() == selectedId);
        }
        // Os ícones/labels herdam o estado via android:duplicateParentState="true" no XML.
    }


    private void onKeyboardVisibilityChanged(boolean visible, int heightPx) {
        Log.i("log", "onKeyboardVisibilityChanged: " + visible);
        recalculateSize();
    }

    private void carregarDados(SolicitacaoPid solicitacao) {
        EnumTipoCliente tipoCliente = EnumTipoCliente.getEnumByDescription(solicitacao.getTipoCliente());
        spTipoCliente.doSelect(tipoCliente);

        EnumRegisterPersonType tipoPessoa = EnumRegisterPersonType.getEnumByCharValue(solicitacao.getTipoPessoa());
        spTipoPessoa.doSelect(tipoPessoa);
        taxas = new DBTaxaMdr(Activity_SolPID_DadosCliente.this).getAllMccByPersonType(tipoPessoa.getIdValue());
        iniciarMcc(new ArrayList<>(taxas));
        spnMCC.doSelectWithCallback(solicitacao.getMCCPrincipal());

        edtCpfCNPJ.setText(solicitacao.getCpfCnpj());
        edtCodigoSGV.setText(solicitacao.getCodigoSGV());
        edtRazaoSocial.setText(solicitacao.getRazaoSocial());
        edtNomeFantasia.setText(solicitacao.getNomeFantasia());

        btnDebito.setTag(solicitacao.getModDebito() == 1 ? "0" : "1");
        toggleButtonState(btnDebito);
        btnCredito.setTag(solicitacao.getModCredito() == 1 ? "0" : "1");
        toggleButtonState(btnCredito);
        btnCredito2_6x.setTag(solicitacao.getModCredito2a6() == 1 ? "0" : "1");
        toggleButtonState(btnCredito2_6x);
        btnCredito7_12x.setTag(solicitacao.getModCredito7a12() == 1 ? "0" : "1");
        toggleButtonState(btnCredito7_12x);
        btnAluguel.setTag(solicitacao.getAluguel() == 1 ? "0" : "1");
        toggleButtonState(btnAluguel);
        if (!solicitacao.getTipoTaxaRAV().isEmpty()) {
            if (solicitacao.getTipoTaxaRAV().equals("AUT")) {
                btnRavAutomatico.setTag("0");
                toggleButtonState(btnRavAutomatico);
            } else {
                btnRavEventual.setTag("0");
                toggleButtonState(btnRavEventual);
            }
        }

        edtPercNovaProposta.setText(Util_IO.formatDoubleToDecimalPercent(solicitacao.getValorTaxaRAV()));
        edtPercDebito.setText(Util_IO.formatDoubleToDecimalPercent(solicitacao.getDistribuicaoDebito().get()));
        edtPercCredito.setText(Util_IO.formatDoubleToDecimalPercent(solicitacao.getDistribuicaoCredito().get()));
        edtPErcCredito2a6.setText(Util_IO.formatDoubleToDecimalPercent(solicitacao.getDistribuicaoCredito6x().get()));
        edtPercCredito7a12.setText(Util_IO.formatDoubleToDecimalPercent(solicitacao.getDistribuicaoCredito12x().get()));
        edtFaturamentoPrevisto.setText(Util_IO.formataValor(solicitacao.getFaturamentoPrevisto()));
        CalculaDistribuicao();

        spConcorrente.doSelectWithCallback(0);

        // Atualiza Bandeiras Selecionadas
        for (com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank item : listaDadosBandeiras) {
            boolean exists = listaSolicitacaoPidTaxaMDR.stream()
                    .anyMatch(solicitacaoPidTaxaMDR -> solicitacaoPidTaxaMDR.getBandeiraTipoId().equals(item.getId()));

            if (exists) {
                item.setActive(true);
            }
        }
        adapterBandeiras.notifyDataSetChanged();

        List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> listaComercialBandeiras = listaDadosBandeiras.stream()
                .filter(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank::isActive)
                .collect(Collectors.toList());
        llComercial_card_flags.updateData(listaComercialBandeiras);
        llComercial_card_flags.updateSelected(0);
    }


    private void montarActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Solicitação PID");
    }

    private void criarObjetos() {
        llCliente = findViewById(R.id.llSolPID_DadosCliente);
        llProduto = findViewById(R.id.llSolPID_ProdModalidade);
        llComercial = findViewById(R.id.llSolPID_Comercial);
        llAnexo = findViewById(R.id.llSolPID_Anexos);
        llPedido = findViewById(R.id.llSolPID_Resumo);

        // Aba Dados Cliente
        spTipoCliente = findViewById(R.id.llSolPID_spnTipoCliente);
        iniciarTipoCliente(new ArrayList<>(EnumTipoCliente.getList()));

        spTipoPessoa = findViewById(R.id.llSolPID_spnTipoPessoa);
        iniciarTipoPessoa(new ArrayList<>(EnumRegisterPersonType.getList()));

        spnMCC = findViewById(R.id.llSolPID_spnMCC);

        edtCpfCNPJ = findViewById(R.id.llSolPID_edtCpfCNPJ);
        edtCodigoSGV = findViewById(R.id.llSolPID_edtCodigoSGV);
        edtRazaoSocial = findViewById(R.id.llSolPID_edtRazaoSocial);
        edtNomeFantasia = findViewById(R.id.llSolPID_edtNomeFantasia);

        btnRedeLojas = findViewById(R.id.llSolPID_btnAddRedeLojas);

        // Dados Modalidade Produto
        rvBandeirasModalidade = findViewById(R.id.llProdModalidade_card_flags1);
        rvBandeirasModalidade.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        btnDebito = findViewById(R.id.llProdModalidade_btnDebito);
        btnCredito = findViewById(R.id.llProdModalidade_btnCredito);
        btnCredito2_6x = findViewById(R.id.llProdModalidade_btnCredito2_6x);
        btnCredito7_12x = findViewById(R.id.llProdModalidade_btnCredito7_12x);
        btnAluguel = findViewById(R.id.llProdModalidade_btnAluguel);
        btnRavEventual = findViewById(R.id.llProdModalidade_btnRavEventual);
        btnRavAutomatico = findViewById(R.id.llProdModalidade_btnRavAutomatica);

        // Anexos
        edtObservacao = findViewById(R.id.llSolPID_edtObservacao_Anexos);
        recycler_Anexos = findViewById(R.id.llSolPID_recycler_Anexos);
        recycler_Anexos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Comercial
        llComercial_card_flags = findViewById(R.id.llComercial_card_flags);
        llComercial_card_flags.setCardFlagsEventListener(this);
        edtPercDebito = findViewById(R.id.llComercial_edtPercDebito);
        edtPercCredito = findViewById(R.id.llComercial_edtPercCredito);
        edtPErcCredito2a6 = findViewById(R.id.llComercial_edtPercCredito2_6);
        edtPercCredito7a12 = findViewById(R.id.llComercial_edtPercCredito7_12);
        btnAddPOS = findViewById(R.id.llSolPID_btnAddPOS);
        edtFaturamentoPrevisto = findViewById(R.id.llComercial_edtFaturamentoPrevisto);
        spConcorrente = findViewById(R.id.llComercial_spnConcorrente);
        iniciarConcorrente(new ArrayList<>(EnumRegisterAnticipation.getEnumList()));
        tvDistribuicaoTotal = findViewById(R.id.llComercial_tvDistribuicaoTotal);
        tvSugeridoDebito = findViewById(R.id.llComercial_tvSugeridoDebito);
        tvSugeridoCredito = findViewById(R.id.llComercial_tvSugeridoCredito);
        tvSugeridoCredito2a6 = findViewById(R.id.llComercial_tvSugeridoCredito2a6);
        tvSugeridoCredito7a12 = findViewById(R.id.llComercial_tvSugeridoCredito7a12);
        edtPercConcorrencia = findViewById(R.id.llComercial_edtPercConcorrencia);
        edtPercNovaProposta = findViewById(R.id.llComercial_edtPercNovaProposta);
        tvDiferencaRAV = findViewById(R.id.llComercial_DiferencaRAV);
        edtPercConcorrencia.setVisibility(View.GONE);
        edtNovaTaxaPercDebito = findViewById(R.id.llComercial_edtNovaTaxaPercDebito);
        edtNovaTaxaPercCredito = findViewById(R.id.llComercial_edtNovaTaxaPercCredito);
        edtNovaTaxaPercCredito2a6 = findViewById(R.id.llComercial_edtNovaTaxaPercCredito2a6);
        edtNovaTaxaPercCredito7a12 = findViewById(R.id.llComercial_edtNovaTaxaPercCredito7a12);
        llTaxaRAV = findViewById(R.id.llComercial_llTaxaRAV);
        tvTituloTaxaRAV = findViewById(R.id.llComercial_tvTituloTaxaRAV);

        tvTaxaDebitoRecomenda = findViewById(R.id.llComercial_tvNovaTaxaPercDebitoRecomendado);
        tvTaxaCreditoRecomenda = findViewById(R.id.llComercial_tvNovaTaxaPercCreditoRecomendado);
        tvTaxaCredito6xRecomenda = findViewById(R.id.llComercial_tvNovaTaxaPercCredito2a6Recomendado);
        tvTaxaCredito12xRecomenda = findViewById(R.id.llComercial_tvNovaTaxaPercCredito7a12Recomendado);

        llComercial_TaxasNormal = findViewById(R.id.llComercial_TaxasNormal);
        llComercial_TaxasConcorrente = findViewById(R.id.llComercial_TaxasConcorrente);
        edtPercDebitoConcorrencia = findViewById(R.id.llComercial_edtPercDebitoConcorrencia);
        edtPercDebitoNovaProposta = findViewById(R.id.llComercial_edtPercDebitoNovaProposta);
        edtPercCreditoConcorrencia = findViewById(R.id.llComercial_edtPercCreditoConcorrencia);
        edtPercCreditoNovaProposta = findViewById(R.id.llComercial_edtPercCreditoNovaProposta);
        edtPercCredito2_6Concorrencia = findViewById(R.id.llComercial_edtPercCredito2_6Concorrencia);
        edtPercCredito2_6NovaProposta = findViewById(R.id.llComercial_edtPercCredito2_6NovaProposta);
        edtPercCredito7_12Concorrencia = findViewById(R.id.llComercial_edtPercCredito7_12Concorrencia);
        edtPercCredito7_12NovaProposta = findViewById(R.id.llComercial_edtPercCredito7_12NovaProposta);

        // Simulação
        btnSimular = findViewById(R.id.solPD_btnSimular);
        btnGravar = findViewById(R.id.solPD_btnSalvar);
        tvDentroDaAlcada = findViewById(R.id.llResumo_tvDentroDaAlcada);
        tvAlcadaMensagem = findViewById(R.id.llResumo_tvAlcadaMensagem);
        tvReceitaTotal = findViewById(R.id.llResumo_tvReceitaTotal);
        tvImpostoTotal = findViewById(R.id.llResumo_tvImpostoTotal);
        tvReceitaLiquidaTotal = findViewById(R.id.llResumo_tvReceitaLiquidaTotal);
        tvReceitaBrutaTotal = findViewById(R.id.llResumo_tvReceitaBrutaTotal);
        tvTakeRate = findViewById(R.id.llResumo_tvTakeRate);
        recycler_CreditoEfetivo = findViewById(R.id.llSolPID_Resumo_recycler_CreditoEfetivo);
        recycler_CreditoEfetivo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void criarEventos() {
        spTipoPessoa.setCallback(item -> {
            if (item.getIdValue() == PHYSICAL.getIdValue()) {
                edtCpfCNPJ.setLabel("CPF");
                edtCpfCNPJ.setMask(CPF);
                edtCpfCNPJ.clearValue();
            } else {
                edtCpfCNPJ.setLabel("CNPJ");
                edtCpfCNPJ.setMask(CNPJ);
                edtCpfCNPJ.clearValue();
            }

            taxas = new DBTaxaMdr(Activity_SolPID_DadosCliente.this).getAllMccByPersonType(item.getIdValue());
            iniciarMcc(new ArrayList<>(taxas));
        });

        spConcorrente.setCallback(item -> {
            if (item.getIdValue() == EnumRegisterAnticipation.YES.getIdValue()) {
                edtPercConcorrencia.setVisibility(View.VISIBLE);
                llComercial_TaxasNormal.setVisibility(View.GONE);
                llComercial_TaxasConcorrente.setVisibility(View.VISIBLE);
            } else {
                edtPercConcorrencia.setText("");
                edtPercConcorrencia.setVisibility(View.GONE);
                llComercial_TaxasNormal.setVisibility(View.VISIBLE);
                llComercial_TaxasConcorrente.setVisibility(View.GONE);
            }
        });

        // Caso mude o Tipo de Cliente / Filtrar as Bandeiras de exibição
        spTipoCliente.setCallback(item -> IniciarBandeiras());

        edtCpfCNPJ.setAfterTextListener(this::cnpjListener);

        btnRedeLojas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_SolPID_DadosCliente.this, Activity_SolPID_DadosLoja.class);
                intent.putExtra("Lista_Redes", solicitacaoPID.getListaRedes());
                intent.putExtra("Titulo", getSupportActionBar().getTitle());
                startActivityForResult(intent, 1);
            }
        });

        btnAddPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_SolPID_DadosCliente.this, Activity_SolPID_DadosPOS.class);
                intent.putExtra("Lista_POS", solicitacaoPID.getListaPOS());
                intent.putExtra("Titulo", getSupportActionBar().getTitle());
                startActivityForResult(intent, 2);
            }
        });

        btnSimular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimularTaxas();
            }
        });

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GravarDados();
                } catch (Exception e) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Erro ao gravar Dados Solicitação Pid");
                    alerta.show();
                    return;
                }
            }
        });

        edtPercDebito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDistribuicao();
            }
        });

        edtPercCredito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDistribuicao();
            }
        });

        edtPErcCredito2a6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDistribuicao();
            }
        });

        edtPercCredito7a12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDistribuicao();
            }
        });

        edtPercConcorrencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDifTaxaRav();
            }
        });

        edtPercNovaProposta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDifTaxaRav();
            }
        });

        textWatcherDebito = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                AtualizaDadosTaxa(s);
            }
        };

        textWatcherCredito = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                AtualizaDadosTaxa(s);
            }
        };

        textWatcherCredito2a6 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                AtualizaDadosTaxa(s);
            }
        };

        textWatcherCredito7a12 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                AtualizaDadosTaxa(s);
            }
        };
    }

    public void prepareAttachmentList() {
        List<EnumPidAnexo> anexosTipos;
        anexosTipos = EnumPidAnexo.getPidAnexos();
        listaAnexos = new ArrayList<>();
        for (EnumPidAnexo item : anexosTipos) {
            SolicitacaoPidAnexo itemAnexo = new SolicitacaoPidAnexo();
            itemAnexo.setTipo(String.valueOf(item.getDescriptionValue()));
            itemAnexo.setTipoArquivo(String.valueOf(item.getTipo()));
            listaAnexos.add(itemAnexo);
        }
        adapterAnexos = new SolicitacaoPidAnexoAdapter(listaAnexos, Activity_SolPID_DadosCliente.this, getSupportFragmentManager());
        recycler_Anexos.setAdapter(adapterAnexos);
    }

    public void AtualizaDadosTaxa(Editable s) {
        if (selectedOptionCardFlags != null) {
            if (edtNovaTaxaPercDebito.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, DEBIT, edtNovaTaxaPercDebito.getCurrencyDouble(), edtNovaTaxaPercDebito);
            } else if (edtNovaTaxaPercCredito.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_IN_CASH, edtNovaTaxaPercCredito.getCurrencyDouble(), edtNovaTaxaPercCredito);
            } else if (edtNovaTaxaPercCredito2a6.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_UNTIL_SIX, edtNovaTaxaPercCredito2a6.getCurrencyDouble(), edtNovaTaxaPercCredito2a6);
            } else if (edtNovaTaxaPercCredito7a12.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_BIGGER_SIX, edtNovaTaxaPercCredito7a12.getCurrencyDouble(), edtNovaTaxaPercCredito7a12);
            } else if (edtPercDebitoNovaProposta.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, DEBIT, edtPercDebitoNovaProposta.getCurrencyDouble(), edtPercDebitoNovaProposta);
            } else if (edtPercCreditoNovaProposta.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_IN_CASH, edtPercCreditoNovaProposta.getCurrencyDouble(), edtPercCreditoNovaProposta);
            } else if (edtPercCredito2_6NovaProposta.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_UNTIL_SIX, edtPercCredito2_6NovaProposta.getCurrencyDouble(), edtPercCredito2_6NovaProposta);
            } else if (edtPercCredito7_12NovaProposta.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_BIGGER_SIX, edtPercCredito7_12NovaProposta.getCurrencyDouble(), edtPercCredito7_12NovaProposta);
            } else if (edtPercDebitoConcorrencia.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, DEBIT, edtPercDebitoConcorrencia.getCurrencyDouble(), edtPercDebitoConcorrencia);
            } else if (edtPercCreditoConcorrencia.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_IN_CASH, edtPercCreditoConcorrencia.getCurrencyDouble(), edtPercCreditoConcorrencia);
            } else if (edtPercCredito2_6Concorrencia.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_UNTIL_SIX, edtPercCredito2_6Concorrencia.getCurrencyDouble(), edtPercCredito2_6Concorrencia);
            } else if (edtPercCredito7_12Concorrencia.hasFocus()) {
                saveTaxChanged(selectedOptionCardFlags, CREDIT_BIGGER_SIX, edtPercCredito7_12Concorrencia.getCurrencyDouble(), edtPercCredito7_12Concorrencia);
            }
        }
    }

    public void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value, CustomEditText componentEdit) {
        Stream.ofNullable(listaSolicitacaoPidTaxaMDR).forEach(pidTaxaMDR -> {
            if (pidTaxaMDR.getBandeiraTipoId() == flagType) {
                switch (taxType) {
                    case DEBIT:
                        if (componentEdit.getId() == R.id.llComercial_edtNovaTaxaPercDebito || componentEdit.getId() == R.id.llComercial_edtPercDebitoNovaProposta)
                            pidTaxaMDR.setTaxaDebito(value);
                        else
                            pidTaxaMDR.setTaxaDebitoConcorrente(value);
                        break;
                    case CREDIT_IN_CASH:
                        if (componentEdit.getId() == R.id.llComercial_edtNovaTaxaPercCredito || componentEdit.getId() == R.id.llComercial_edtPercCreditoNovaProposta)
                            pidTaxaMDR.setTaxaCredito(value);
                        else
                            pidTaxaMDR.setTaxaCreditoConcorrente(value);
                        break;
                    case CREDIT_UNTIL_SIX:
                        if (componentEdit.getId() == R.id.llComercial_edtNovaTaxaPercCredito2a6 || componentEdit.getId() == R.id.llComercial_edtPercCredito2_6NovaProposta)
                            pidTaxaMDR.setTaxaCredito6x(value);
                        else
                            pidTaxaMDR.setTaxaCredito6xConcorrente(value);
                        break;
                    case CREDIT_BIGGER_SIX:
                        if (componentEdit.getId() == R.id.llComercial_edtNovaTaxaPercCredito7a12 || componentEdit.getId() == R.id.llComercial_edtPercCredito7_12NovaProposta)
                            pidTaxaMDR.setTaxaCredito12x(value);
                        else
                            pidTaxaMDR.setTaxaCredito12xConcorrente(value);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void AdicionaEventoTaxasMDR() {
        edtNovaTaxaPercDebito.addTextChangedListener(textWatcherDebito);
        edtNovaTaxaPercCredito.addTextChangedListener(textWatcherCredito);
        edtNovaTaxaPercCredito2a6.addTextChangedListener(textWatcherCredito2a6);
        edtNovaTaxaPercCredito7a12.addTextChangedListener(textWatcherCredito7a12);

        // Campos caso tenha Concorrência
        edtPercDebitoConcorrencia.addTextChangedListener(textWatcherDebito);
        edtPercDebitoNovaProposta.addTextChangedListener(textWatcherDebito);
        edtPercCreditoConcorrencia.addTextChangedListener(textWatcherDebito);
        edtPercCreditoNovaProposta.addTextChangedListener(textWatcherDebito);
        edtPercCredito2_6Concorrencia.addTextChangedListener(textWatcherDebito);
        edtPercCredito2_6NovaProposta.addTextChangedListener(textWatcherDebito);
        edtPercCredito7_12Concorrencia.addTextChangedListener(textWatcherDebito);
        edtPercCredito7_12NovaProposta.addTextChangedListener(textWatcherDebito);
    }

    private void RemoveEventoTaxasMDR() {
        edtNovaTaxaPercDebito.removeTextChangedListener(textWatcherDebito);
        edtNovaTaxaPercCredito.removeTextChangedListener(textWatcherCredito);
        edtNovaTaxaPercCredito2a6.removeTextChangedListener(textWatcherCredito2a6);
        edtNovaTaxaPercCredito7a12.removeTextChangedListener(textWatcherCredito7a12);

        // Campos caso tenha Concorrência
        edtPercDebitoConcorrencia.removeTextChangedListener(textWatcherDebito);
        edtPercDebitoNovaProposta.removeTextChangedListener(textWatcherDebito);
        edtPercCreditoConcorrencia.removeTextChangedListener(textWatcherDebito);
        edtPercCreditoNovaProposta.removeTextChangedListener(textWatcherDebito);
        edtPercCredito2_6Concorrencia.removeTextChangedListener(textWatcherDebito);
        edtPercCredito2_6NovaProposta.removeTextChangedListener(textWatcherDebito);
        edtPercCredito7_12Concorrencia.removeTextChangedListener(textWatcherDebito);
        edtPercCredito7_12NovaProposta.removeTextChangedListener(textWatcherDebito);
    }

    public void CalculaDifTaxaRav() {
        double pDif = 0.00;
        if (edtPercConcorrencia.getVisibility() == View.GONE)
            pDif = 0.00;
        else {
            if (!Util_IO.isNullOrEmpty(edtPercConcorrencia.getText()) && !Util_IO.isNullOrEmpty(edtPercNovaProposta.getText()))
                pDif = edtPercNovaProposta.getCurrencyDouble() - edtPercConcorrencia.getCurrencyDouble();
            else
                pDif = 0.00;
        }

        tvDiferencaRAV.setText("Diferença: " + Util_IO.formataValor(pDif) + "%");
    }

    public double CalculaDistribuicao() {
        double vPercDebito = obterValorOuZero(edtPercDebito);
        double vPercCredito = obterValorOuZero(edtPercCredito);
        double vPercCredito2 = obterValorOuZero(edtPErcCredito2a6);
        double vPercCredito7 = obterValorOuZero(edtPercCredito7a12);

        double valorTotal = vPercDebito + vPercCredito + vPercCredito2 + vPercCredito7;
        tvDistribuicaoTotal.setText(String.format("Distribuição Total: %s %%", Util_IO.formataValor(valorTotal)));
        return valorTotal;
    }

    private double obterValorOuZero(CustomEditText campo) {
        return !Util_IO.isNullOrEmpty(campo.getText()) ? campo.getCurrencyDouble() : 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), "Tem certeza que gostaria de sair?\nAs alterações feitas não serão salvas.");
                alerta.showConfirm((dialog, which) -> {
                    finish();
                }, null);
                break;
            default:
                break;
        }
        return true;
    }

    private void iniciarTipoCliente(List<ICustomSpinnerDialogModel> list) {
        spTipoCliente.setList(list);
    }

    private void iniciarTipoPessoa(List<ICustomSpinnerDialogModel> list) {
        spTipoPessoa.setList(list);
    }

    private void iniciarMcc(List<ICustomSpinnerDialogModel> list) {
        spnMCC.removeSelection();
        spnMCC.setList(list);
    }

    private void iniciarConcorrente(List<ICustomSpinnerDialogModel> list) {
        spConcorrente.setList(list);
    }

    private void cnpjListener(String text) {
        if (edtCpfCNPJ.isNotFocused()) {
            return;
        }

        text = StringUtils.returnOnlyNumbers(text);
        if (text.length() == StringUtils.CPF_LENGTH && StringUtils.isCpfValid(text)) {
            verificaCliente(text);
        } else if (text.length() == StringUtils.CNPJ_LENGTH && StringUtils.isCnpjValid(text)) {
            verificaCliente(text);
        }
    }

    private void verificaCliente(String pCpfCnpj) {
        RegisterService services;
        Retrofit retrofit = RetrofitClient.getRetrofit();
        services = retrofit.create(RegisterService.class);

        Call<ConsultaReceita> call = services.retornaCliente(pCpfCnpj);
        call.enqueue(new Callback<ConsultaReceita>() {
            @Override
            public void onResponse(Call<ConsultaReceita> call, Response<ConsultaReceita> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ConsultaReceita consulta = response.body();
                        edtCodigoSGV.setText(consulta.getCodigoSgv());
                        edtRazaoSocial.setText(consulta.getRazaoSocial());
                        edtNomeFantasia.setText(consulta.getFantasia());
                        if (consulta.getMcc() != null)
                            spnMCC.doSelectWithCallback(Integer.valueOf(consulta.getMcc()));
                    } else {
                        // So consulta dados da receita para CNPJ
                        if (pCpfCnpj.length() > 11) {
                            // Consulta Receita
                            Call<ConsultaReceita> callReceita = services.retornaClienteReceita(pCpfCnpj);
                            callReceita.enqueue(new Callback<ConsultaReceita>() {
                                @Override
                                public void onResponse(Call<ConsultaReceita> call, Response<ConsultaReceita> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            ConsultaReceita consulta = response.body();
                                            edtCodigoSGV.setText(consulta.getCodigoSgv());
                                            edtRazaoSocial.setText(consulta.getRazaoSocial());
                                            edtNomeFantasia.setText(consulta.getFantasia());
                                            if (consulta.getMcc() != null)
                                                spnMCC.doSelectWithCallback(Integer.valueOf(consulta.getMcc()));
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ConsultaReceita> call, Throwable throwable) {

                                }
                            });
                        }
                    }
                }

                // Caso cliente não seja da Base será considerada uma Negociação, caso contrário Renegociação
                if (Util_IO.isNullOrEmpty(edtCodigoSGV.getText()))
                    getSupportActionBar().setTitle("Negociação - PID");
                else
                    getSupportActionBar().setTitle("Renegociação - PID");
            }

            @Override
            public void onFailure(Call<ConsultaReceita> call, Throwable throwable) {

            }
        });
    }

    private void onErrorObterInformacoes(Throwable throwable) {

    }

    private void onObterInformacoesCep(ConsultaReceita retorno) {

    }

    public void showLoading() {
        progressDialog.setMessage(
                getString(R.string.atualizar_cliente_message_carregando)
        );
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File tempFile = null;

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    ArrayList<SolicitacaoPidRede> listaDados = (ArrayList<SolicitacaoPidRede>) data.getSerializableExtra("Dados_loja");
                    solicitacaoPID.setListaRedes(listaDados);

                    if (listaDados != null && listaDados.size() > 0) {
                        btnRedeLojas.setText("Rede de lojas - " + solicitacaoPID.getListaRedes().size() + " Lojas");
                        Drawable drawableEnd = getResources().getDrawable(R.drawable.ic_check_circle_grey_wraped);
                        btnRedeLojas.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableEnd, null);
                    } else {
                        btnRedeLojas.setText("Rede de lojas - 0 Lojas");
                        btnRedeLojas.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                } else {
                    btnRedeLojas.setText("Rede de lojas - 0 Lojas");
                    btnRedeLojas.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if (requestCode == 2) { // Cadastro POS
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    ArrayList<SolicitacaoPidPos> listaDados = (ArrayList<SolicitacaoPidPos>) data.getSerializableExtra("Dados_POS");
                    solicitacaoPID.setListaPOS(listaDados);

                    if (listaDados != null && listaDados.size() > 0) {
                        btnAddPOS.setText("Adicionar POS - " + solicitacaoPID.getListaPOS().size() + " POS");
                        Drawable drawableEnd = getResources().getDrawable(R.drawable.ic_check_circle_grey_wraped);
                        btnAddPOS.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableEnd, null);
                    } else {
                        btnAddPOS.setText("Adicionar POS - 0 POS");
                        btnAddPOS.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                } else {
                    btnAddPOS.setText("Adicionar POS - 0 POS");
                    btnAddPOS.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                tempFile = adapterAnexos.getTempFile();
                if (tempFile != null && tempFile.exists()) {
                    onImageCaptured(tempFile, adapterAnexos.getCurrentPosition());
                } else {
                    onImageCaptureFailed();
                }
            } else {
                onImageCaptureFailed();
            }
        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Uri uri = data.getData();
                    File imgSelecionada = handleSelectedImage(uri);
                    onImageCaptured(imgSelecionada, adapterAnexos.getCurrentPosition());
                    imgSelecionada.delete();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                onImageCaptureFailed();
            }
        }

    }

    private File handleSelectedImage(Uri uri) {
        File file = null;
        if (uri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    file = Utilidades.setImagem();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Activity_SolPID_DadosCliente", "Uri is null");
        }
        return file;
    }

    @Override
    public void onFlagBankSelected(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank flag) {
        if (Util_IO.isNullOrEmpty(edtFaturamentoPrevisto.getText()) || edtFaturamentoPrevisto.getCurrencyDouble() <= 0) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Faturamento Previsto");
            alerta.show();
            return;
        }

        if (flag != null) {
            selectedOptionCardFlags = flag.getId();
            setarTaxasMDR(flag.getId());

            if (flag.getId() == 3 || flag.getId() == 5)
                edtNovaTaxaPercDebito.setEnabled(false);
            else
                edtNovaTaxaPercDebito.setEnabled(true);
        }
    }

    public void setarTaxasMDR(int flagType) {
        SolicitacaoPidTaxaMDR itemTaxas = Stream.ofNullable(listaSolicitacaoPidTaxaMDR)
                .filter(tax -> tax.getBandeiraTipoId() == flagType)
                .findFirst()
                .orElse(null);

        RemoveEventoTaxasMDR();
        if (itemTaxas != null) {
            int pRav = 0, pTipoNegociacao = 0, pTipoClassificacao = 0;

            // Verifica se foi selecionado RAV
            if (solicitacaoPID != null && !Util_IO.isNullOrEmpty(solicitacaoPID.getTipoTaxaRAV()))
                pRav = 1;

            // Carrega Tipo de Classificacao
            if (spTipoCliente.getSelectedItem().getDescriptionValue().equals("ISO"))
                pTipoClassificacao = 1;
            else if (spTipoCliente.getSelectedItem().getDescriptionValue().equals("SUB"))
                pTipoClassificacao = 2;
            else if (spTipoCliente.getSelectedItem().getDescriptionValue().equals("ADQ"))
                pTipoClassificacao = 3;

            // Carrega Tipo de Negociacao
            // Caso cliente não seja da Base será considerada uma Negociação = 1, caso contrário Renegociação = 2
            if (Util_IO.isNullOrEmpty(edtCodigoSGV.getText()))
                pTipoNegociacao = 1;
            else
                pTipoNegociacao = 2;

            // Carrega Taxas Iniciais
            TaxaMdr taxaMdr = new DBTaxaMdr(Activity_SolPID_DadosCliente.this).getTaxasBandeira(spTipoPessoa.getSelectedItemId(),
                    edtFaturamentoPrevisto.getCurrencyDouble(),
                    2, spnMCC.getSelectedItemId(), flagType,
                    pTipoClassificacao, pTipoNegociacao, pRav);

            // Debito
            if (edtNovaTaxaPercDebito.isEnabled()) {
                if (itemTaxas.getTaxaDebito().isPresent())
                    edtNovaTaxaPercDebito.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaDebito().get()));
                else {
                    if (taxaMdr != null) {
                        edtNovaTaxaPercDebito.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxDebit()));
                        saveTaxChanged(flagType, DEBIT, taxaMdr.getTaxDebit(), edtNovaTaxaPercDebito);
                    } else
                        edtNovaTaxaPercDebito.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Credito
            if (edtNovaTaxaPercCredito.isEnabled()) {
                if (itemTaxas.getTaxaCredito().isPresent())
                    edtNovaTaxaPercCredito.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito().get()));
                else {
                    if (taxaMdr != null) {
                        edtNovaTaxaPercCredito.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxCredit()));
                        saveTaxChanged(flagType, CREDIT_IN_CASH, taxaMdr.getTaxCredit(), edtNovaTaxaPercCredito);
                    } else
                        edtNovaTaxaPercCredito.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Credito até 6x
            if (edtNovaTaxaPercCredito2a6.isEnabled()) {
                if (itemTaxas.getTaxaCredito6x().isPresent())
                    edtNovaTaxaPercCredito2a6.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito6x().get()));
                else {
                    if (taxaMdr != null) {
                        edtNovaTaxaPercCredito2a6.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxUntilSix()));
                        saveTaxChanged(flagType, CREDIT_UNTIL_SIX, taxaMdr.getTaxUntilSix(), edtNovaTaxaPercCredito2a6);
                    } else
                        edtNovaTaxaPercCredito2a6.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Credito 7 a 12x
            if (edtNovaTaxaPercCredito7a12.isEnabled()) {
                if (itemTaxas.getTaxaCredito12x().isPresent())
                    edtNovaTaxaPercCredito7a12.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito12x().get()));
                else {
                    if (taxaMdr != null) {
                        edtNovaTaxaPercCredito7a12.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxBiggerSix()));
                        saveTaxChanged(flagType, CREDIT_BIGGER_SIX, taxaMdr.getTaxBiggerSix(), edtNovaTaxaPercCredito7a12);
                    } else
                        edtNovaTaxaPercCredito7a12.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Taxas Concorrentes Debito
            if (edtPercDebitoConcorrencia.isEnabled()) {
                if (itemTaxas.getTaxaDebitoConcorrente().isPresent())
                    edtPercDebitoConcorrencia.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaDebitoConcorrente().get()));
                else {
                    edtPercDebitoConcorrencia.setText(StringUtils.maskCurrencyDouble("0"));
                    saveTaxChanged(flagType, DEBIT, 0.00, edtPercDebitoConcorrencia);
                }
            }

            // Taxas Concorrentes Credito
            if (edtPercCreditoConcorrencia.isEnabled()) {
                if (itemTaxas.getTaxaCreditoConcorrente().isPresent())
                    edtPercCreditoConcorrencia.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCreditoConcorrente().get()));
                else {
                    edtPercCreditoConcorrencia.setText(StringUtils.maskCurrencyDouble("0"));
                    saveTaxChanged(flagType, CREDIT_IN_CASH, 0.00, edtPercCreditoConcorrencia);
                }
            }

            // Taxas Concorrentes Credito 2 a 6
            if (edtPercCredito2_6Concorrencia.isEnabled()) {
                if (itemTaxas.getTaxaCredito6xConcorrente().isPresent())
                    edtPercCredito2_6Concorrencia.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito6xConcorrente().get()));
                else {
                    edtPercCredito2_6Concorrencia.setText(StringUtils.maskCurrencyDouble("0"));
                    saveTaxChanged(flagType, CREDIT_UNTIL_SIX, 0.00, edtPercCredito2_6Concorrencia);
                }
            }

            // Taxas Concorrentes Credito 7 a 12
            if (edtPercCredito7_12Concorrencia.isEnabled()) {
                if (itemTaxas.getTaxaCredito12xConcorrente().isPresent())
                    edtPercCredito7_12Concorrencia.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito12xConcorrente().get()));
                else {
                    edtPercCredito7_12Concorrencia.setText(StringUtils.maskCurrencyDouble("0"));
                    saveTaxChanged(flagType, CREDIT_BIGGER_SIX, 0.00, edtPercCredito7_12Concorrencia);
                }
            }

            // Nova Taxa Debito/Concorrente
            if (edtPercDebitoNovaProposta.isEnabled()) {
                if (itemTaxas.getTaxaDebito().isPresent())
                    edtPercDebitoNovaProposta.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaDebito().get()));
                else {
                    if (taxaMdr != null) {
                        edtPercDebitoNovaProposta.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxDebit()));
                        saveTaxChanged(flagType, DEBIT, taxaMdr.getTaxDebit(), edtPercDebitoNovaProposta);
                    } else
                        edtPercDebitoNovaProposta.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Nova Taxa Credito/Concorrente
            if (edtPercCreditoNovaProposta.isEnabled()) {
                if (itemTaxas.getTaxaCredito().isPresent())
                    edtPercCreditoNovaProposta.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito().get()));
                else {
                    if (taxaMdr != null) {
                        edtPercCreditoNovaProposta.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxCredit()));
                        saveTaxChanged(flagType, CREDIT_IN_CASH, taxaMdr.getTaxCredit(), edtPercCreditoNovaProposta);
                    } else
                        edtPercCreditoNovaProposta.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Nova Taxa Credito até 6x/Concorrente
            if (edtPercCredito2_6NovaProposta.isEnabled()) {
                if (itemTaxas.getTaxaCredito6x().isPresent())
                    edtPercCredito2_6NovaProposta.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito6x().get()));
                else {
                    if (taxaMdr != null) {
                        edtPercCredito2_6NovaProposta.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxUntilSix()));
                        saveTaxChanged(flagType, CREDIT_UNTIL_SIX, taxaMdr.getTaxUntilSix(), edtPercCredito2_6NovaProposta);
                    } else
                        edtPercCredito2_6NovaProposta.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Nova Taxa Credito 7 a 12x/Concorrente
            if (edtPercCredito7_12NovaProposta.isEnabled()) {
                if (itemTaxas.getTaxaCredito12x().isPresent())
                    edtPercCredito7_12NovaProposta.setText(StringUtils.maskCurrencyDouble(itemTaxas.getTaxaCredito12x().get()));
                else {
                    if (taxaMdr != null) {
                        edtPercCredito7_12NovaProposta.setText(StringUtils.maskCurrencyDouble(taxaMdr.getTaxBiggerSix()));
                        saveTaxChanged(flagType, CREDIT_BIGGER_SIX, taxaMdr.getTaxBiggerSix(), edtPercCredito7_12NovaProposta);
                    } else
                        edtPercCredito7_12NovaProposta.setText(StringUtils.maskCurrencyDouble("0"));
                }
            }

            // Valida questão da Taxa Recomendada
            tvTaxaDebitoRecomenda.setVisibility(View.GONE);
            tvTaxaCreditoRecomenda.setVisibility(View.GONE);
            tvTaxaCredito6xRecomenda.setVisibility(View.GONE);
            tvTaxaCredito12xRecomenda.setVisibility(View.GONE);
            if (itemTaxas != null && spConcorrente.getSelectedItem().getIdValue() != EnumRegisterAnticipation.YES.getIdValue()) {
                // Debito
                if (itemTaxas.getTaxaDebitoRecomendada().isPresent()
                        && itemTaxas.getTaxaDebito().isPresent()
                        && itemTaxas.getTaxaDebito().get() < itemTaxas.getTaxaDebitoRecomendada().get()) {
                    tvTaxaDebitoRecomenda.setVisibility(View.VISIBLE);
                    tvTaxaDebitoRecomenda.setText("Taxa Recomendada " + Util_IO.formataValor(itemTaxas.getTaxaDebitoRecomendada().get()) + " %");
                } else
                    tvTaxaDebitoRecomenda.setVisibility(View.GONE);

                // Credito
                if (itemTaxas.getTaxaCreditoRecomendada().isPresent()
                        && itemTaxas.getTaxaCredito().isPresent()
                        && itemTaxas.getTaxaCredito().get() < itemTaxas.getTaxaCreditoRecomendada().get()) {
                    tvTaxaCreditoRecomenda.setVisibility(View.VISIBLE);
                    tvTaxaCreditoRecomenda.setText("Taxa Recomendada " + Util_IO.formataValor(itemTaxas.getTaxaCreditoRecomendada().get()) + " %");
                } else
                    tvTaxaCreditoRecomenda.setVisibility(View.GONE);

                // Credito 6x
                if (itemTaxas.getTaxaCredito6xRecomendada().isPresent()
                        && itemTaxas.getTaxaCredito6x().isPresent()
                        && itemTaxas.getTaxaCredito6x().get() < itemTaxas.getTaxaCredito6xRecomendada().get()) {
                    tvTaxaCredito6xRecomenda.setVisibility(View.VISIBLE);
                    tvTaxaCredito6xRecomenda.setText("Taxa Recomendada " + Util_IO.formataValor(itemTaxas.getTaxaCredito6xRecomendada().get()) + " %");
                } else
                    tvTaxaCredito6xRecomenda.setVisibility(View.GONE);

                // Credito 12x
                if (itemTaxas.getTaxaCredito12xRecomendada().isPresent()
                        && itemTaxas.getTaxaCredito12x().isPresent()
                        && itemTaxas.getTaxaCredito12x().get() < itemTaxas.getTaxaCredito12xRecomendada().get()) {
                    tvTaxaCredito12xRecomenda.setVisibility(View.VISIBLE);
                    tvTaxaCredito12xRecomenda.setText("Taxa Recomendada " + Util_IO.formataValor(itemTaxas.getTaxaCredito12xRecomendada().get()) + " %");
                } else
                    tvTaxaCredito12xRecomenda.setVisibility(View.GONE);
            }
        } else {
            tvTaxaDebitoRecomenda.setVisibility(View.GONE);
            tvTaxaCreditoRecomenda.setVisibility(View.GONE);
            tvTaxaCredito6xRecomenda.setVisibility(View.GONE);
            tvTaxaCredito12xRecomenda.setVisibility(View.GONE);
        }
        AdicionaEventoTaxasMDR();
    }

    private void IniciarBandeiras() {
        // Inicializar a lista de bandeiras com a condição padrão
        String filtro = null;

        // Verifica o tipo de cliente selecionado
        if (spTipoCliente.getSelectedItem() != null) {
            if (spTipoCliente.getSelectedItem().getDescriptionValue().equals("ADQ")) {
                filtro = "and (idBandeira not in (3,5))";
            }
        }

        // Recupera os dados das bandeiras com base no filtro
        listaDadosBandeiras = new DBFlagsBank(this).getAll(filtro, null);

        for (com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank item : listaDadosBandeiras) {
            item.setActive(false);
        }
        adapterBandeiras = new ItemBandeiraAdapter(listaDadosBandeiras, Activity_SolPID_DadosCliente.this, this);
        rvBandeirasModalidade.setAdapter(adapterBandeiras);
    }

    public void toggleButtonState(View view) {
        Button button = (Button) view;
        String currentTag = button.getTag().toString();

        if (button.getId() == R.id.llProdModalidade_btnRavEventual) {
            if (currentTag.equals("1")) {
                button.setTag("0");
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.GRAY);

                llTaxaRAV.setVisibility(View.GONE);
                solicitacaoPID.setTipoTaxaRAV(null);
            } else {
                button.setTag("1");
                button.setBackgroundColor(Color.DKGRAY);
                button.setTextColor(Color.WHITE);

                Button btnRAV = findViewById(R.id.llProdModalidade_btnRavAutomatica);
                btnRAV.setTag("0");
                btnRAV.setBackgroundColor(Color.WHITE);
                btnRAV.setTextColor(Color.GRAY);

                solicitacaoPID.setTipoTaxaRAV("EVE");
                llTaxaRAV.setVisibility(View.VISIBLE);
                tvTituloTaxaRAV.setText("RAV EVENTUAL");
            }
        } else if (button.getId() == R.id.llProdModalidade_btnRavAutomatica) {
            if (currentTag.equals("1")) {
                button.setTag("0");
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.GRAY);

                llTaxaRAV.setVisibility(View.GONE);
                solicitacaoPID.setTipoTaxaRAV(null);
            } else {
                button.setTag("1");
                button.setBackgroundColor(Color.DKGRAY);
                button.setTextColor(Color.WHITE);

                Button btnRAV = findViewById(R.id.llProdModalidade_btnRavEventual);
                btnRAV.setTag("0");
                btnRAV.setBackgroundColor(Color.WHITE);
                btnRAV.setTextColor(Color.GRAY);

                solicitacaoPID.setTipoTaxaRAV("AUT");
                llTaxaRAV.setVisibility(View.VISIBLE);
                tvTituloTaxaRAV.setText("RAV AUTOMÁTICA");
            }
        } else if (button.getId() == R.id.llProdModalidade_btnAluguel) {
            if (currentTag.equals("1")) {
                button.setTag("0");
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.GRAY);
                btnAddPOS.setVisibility(View.GONE);

                // Limpa os dados informados caso não seja informado Aluguel
                solicitacaoPID.setListaPOS(null);
            } else {
                button.setTag("1");
                button.setBackgroundColor(Color.DKGRAY);
                button.setTextColor(Color.WHITE);
                btnAddPOS.setVisibility(View.VISIBLE);
            }
        } else {
            if (currentTag.equals("1")) {
                button.setTag("0");
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.GRAY);
            } else {
                button.setTag("1");
                button.setBackgroundColor(Color.DKGRAY);
                button.setTextColor(Color.WHITE);
            }
        }

        HabilitaCamposOnClickProdutos();
    }

    public void toggleSelecionar(View view) {
        TextView button = (TextView) view;

        btnDebito.setTag(button.getId() == R.id.llProdModalidade_btnMarcar ? "1" : "0");
        btnDebito.setBackgroundColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.DKGRAY : Color.WHITE);
        btnDebito.setTextColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.WHITE : Color.GRAY);

        btnCredito.setTag(button.getId() == R.id.llProdModalidade_btnMarcar ? "1" : "0");
        btnCredito.setBackgroundColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.DKGRAY : Color.WHITE);
        btnCredito.setTextColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.WHITE : Color.GRAY);

        btnCredito2_6x.setTag(button.getId() == R.id.llProdModalidade_btnMarcar ? "1" : "0");
        btnCredito2_6x.setBackgroundColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.DKGRAY : Color.WHITE);
        btnCredito2_6x.setTextColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.WHITE : Color.GRAY);

        btnCredito7_12x.setTag(button.getId() == R.id.llProdModalidade_btnMarcar ? "1" : "0");
        btnCredito7_12x.setBackgroundColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.DKGRAY : Color.WHITE);
        btnCredito7_12x.setTextColor(button.getId() == R.id.llProdModalidade_btnMarcar ? Color.WHITE : Color.GRAY);

        HabilitaCamposOnClickProdutos();
    }

    public void HabilitaCamposOnClickProdutos() {
        if (btnDebito.getTag().equals("1")) {
            edtPercDebito.setEnabled(true);
            edtNovaTaxaPercDebito.setEnabled(true);
            tvSugeridoDebito.setVisibility(View.VISIBLE);

            // Quando existe Concorrente
            edtPercDebitoNovaProposta.setEnabled(true);
            edtPercDebitoConcorrencia.setEnabled(true);
        } else {
            edtPercDebito.setText("");
            edtPercDebito.setEnabled(false);

            edtNovaTaxaPercDebito.setText("");
            edtNovaTaxaPercDebito.setEnabled(false);
            tvSugeridoDebito.setVisibility(View.GONE);

            // Quando existe Concorrente
            edtPercDebitoNovaProposta.setEnabled(false);
            edtPercDebitoNovaProposta.setText("");
            edtPercDebitoConcorrencia.setEnabled(false);
            edtPercDebitoConcorrencia.setText("");
        }

        if (btnCredito.getTag().equals("1")) {
            edtPercCredito.setEnabled(true);
            edtNovaTaxaPercCredito.setEnabled(true);
            tvSugeridoCredito.setVisibility(View.VISIBLE);

            // Quando existe Concorrente
            edtPercCreditoNovaProposta.setEnabled(true);
            edtPercCreditoConcorrencia.setEnabled(true);
        } else {
            edtPercCredito.setText("");
            edtPercCredito.setEnabled(false);

            edtNovaTaxaPercCredito.setText("");
            edtNovaTaxaPercCredito.setEnabled(false);
            tvSugeridoCredito.setVisibility(View.GONE);

            // Quando existe Concorrente
            edtPercCreditoNovaProposta.setEnabled(false);
            edtPercCreditoNovaProposta.setText("");
            edtPercCreditoConcorrencia.setEnabled(false);
            edtPercCreditoConcorrencia.setText("");
        }

        if (btnCredito2_6x.getTag().equals("1")) {
            edtPErcCredito2a6.setEnabled(true);
            edtNovaTaxaPercCredito2a6.setEnabled(true);
            tvSugeridoCredito2a6.setVisibility(View.VISIBLE);

            // Quando existe Concorrente
            edtPercCredito2_6NovaProposta.setEnabled(true);
            edtPercCredito2_6Concorrencia.setEnabled(true);
        } else {
            edtPErcCredito2a6.setText("");
            edtPErcCredito2a6.setEnabled(false);

            edtNovaTaxaPercCredito2a6.setText("");
            edtNovaTaxaPercCredito2a6.setEnabled(false);
            tvSugeridoCredito2a6.setVisibility(View.GONE);

            // Quando existe Concorrente
            edtPercCredito2_6NovaProposta.setEnabled(false);
            edtPercCredito2_6NovaProposta.setText("");
            edtPercCredito2_6Concorrencia.setEnabled(false);
            edtPercCredito2_6Concorrencia.setText("");
        }

        if (btnCredito7_12x.getTag().equals("1")) {
            edtPercCredito7a12.setEnabled(true);
            edtNovaTaxaPercCredito7a12.setEnabled(true);
            tvSugeridoCredito7a12.setVisibility(View.VISIBLE);

            // Quando existe Concorrente
            edtPercCredito7_12NovaProposta.setEnabled(true);
            edtPercCredito7_12Concorrencia.setEnabled(true);
        } else {
            edtPercCredito7a12.setText("");
            edtPercCredito7a12.setEnabled(false);

            edtNovaTaxaPercCredito7a12.setText("");
            edtNovaTaxaPercCredito7a12.setEnabled(false);
            tvSugeridoCredito7a12.setVisibility(View.GONE);

            // Quando existe Concorrente
            edtPercCredito7_12NovaProposta.setEnabled(false);
            edtPercCredito7_12NovaProposta.setText("");
            edtPercCredito7_12Concorrencia.setEnabled(false);
            edtPercCredito7_12Concorrencia.setText("");
        }

        if (btnCredito.getTag().equals("1") || btnCredito.getTag().equals("1") || btnCredito2_6x.getTag().equals("1") || btnCredito7_12x.getTag().equals("1"))
            tvDistribuicaoTotal.setVisibility(View.VISIBLE);
        else
            tvDistribuicaoTotal.setVisibility(View.GONE);
    }

    public void SimularTaxas() {
        // Valida Campos
        if (spTipoCliente.getSelectedItemId() == null) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione o Tipo de Cliente");
            alerta.show();
            return;
        }

        if (spnMCC.getSelectedItemId() == null || spnMCC.getSelectedItemId() == 0) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione o MCC do Cliente");
            alerta.show();
            return;
        }

        if (Util_IO.isNullOrEmpty(edtCpfCNPJ.getText())) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Cpf/Cnpj do Cliente");
            alerta.show();
            return;
        }

        if (Util_IO.isNullOrEmpty(edtFaturamentoPrevisto.getText()) || edtFaturamentoPrevisto.getCurrencyDouble() <= 0) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Faturamento Previsto");
            alerta.show();
            return;
        }

        if (solicitacaoPID != null && !Util_IO.isNullOrEmpty(solicitacaoPID.getTipoTaxaRAV()) && solicitacaoPID.getTipoTaxaRAV().equals("AUT")) {
            if (Util_IO.isNullOrEmpty(edtPercNovaProposta.getText()) || edtPercNovaProposta.getCurrencyDouble() <= 0) {
                Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Percentual Taxa RAV");
                alerta.show();
                return;
            }
        }

        SolicitacaoPidSimuladorRequest request = new SolicitacaoPidSimuladorRequest();
        request.setTipoCliente(spTipoCliente.getSelectedItem().getDescriptionValue());
        request.setMcc(spnMCC.getSelectedItem().getIdValue().toString());
        request.setCpfCnpj(StringUtils.returnOnlyNumbers(edtCpfCNPJ.getText()));
        request.setTpvTotal(edtFaturamentoPrevisto.getCurrencyDouble());

        if (solicitacaoPID != null && !Util_IO.isNullOrEmpty(solicitacaoPID.getTipoTaxaRAV())) {
            if (solicitacaoPID.getTipoTaxaRAV().equals("AUT"))
                request.setTaxaRav(edtPercNovaProposta.getCurrencyDouble());
            else
                request.setTaxaRav(0);
            request.setTipoTaxaRav(solicitacaoPID.getTipoTaxaRAV());
        } else {
            request.setTipoTaxaRav(null);
            request.setTaxaRav(0);
        }

        // Percentuais de Distribuição
        if (edtPercDebito.getVisibility() == View.VISIBLE && !Util_IO.isNullOrEmpty(edtPercDebito.getText()))
            request.setDistribuicaoDebitoProposta(edtPercDebito.getCurrencyDouble());

        if (edtPercCredito.getVisibility() == View.VISIBLE && !Util_IO.isNullOrEmpty(edtPercCredito.getText()))
            request.setDistribuicaoCreditoProposta(edtPercCredito.getCurrencyDouble());

        if (edtPErcCredito2a6.getVisibility() == View.VISIBLE && !Util_IO.isNullOrEmpty(edtPErcCredito2a6.getText()))
            request.setDistribuicaoCredito6xProposta(edtPErcCredito2a6.getCurrencyDouble());

        if (edtPercCredito7a12.getVisibility() == View.VISIBLE && !Util_IO.isNullOrEmpty(edtPercCredito7a12.getText()))
            request.setDistribuicaoCredito12xProposta(edtPercCredito7a12.getCurrencyDouble());

        // Taxas Bandeiras
        List<SolicitacaoPidSimuladorTaxaBandeiraRequest> lista = new ArrayList<>();
        SolicitacaoPidSimuladorTaxaBandeiraRequest txBandeira = new SolicitacaoPidSimuladorTaxaBandeiraRequest();
        for (SolicitacaoPidTaxaMDR item : listaSolicitacaoPidTaxaMDR) {
            txBandeira = new SolicitacaoPidSimuladorTaxaBandeiraRequest();
            txBandeira.setIdBandeiraTipo(item.getBandeiraTipoId());
            if (item.getTaxaDebito().isPresent())
                txBandeira.setTaxaDebito(item.getTaxaDebito().get());
            if (item.getTaxaCredito().isPresent())
                txBandeira.setTaxaCredito(item.getTaxaCredito().get());
            if (item.getTaxaCredito6x().isPresent())
                txBandeira.setTaxaCredito6x(item.getTaxaCredito6x().get());
            if (item.getTaxaCredito12x().isPresent())
                txBandeira.setTaxaCredito12x(item.getTaxaCredito12x().get());
            lista.add(txBandeira);
        }
        request.setTaxaBandeira(lista);

        SolicitacaoPidService services;
        Retrofit retrofit = RetrofitClient.getRetrofit();
        services = retrofit.create(SolicitacaoPidService.class);

        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);
        Log.d("Roni", "SimularTaxas: " + jsonRequest);

        Call<SolicitacaoPidSimuladorResponse> call = services.simuladorPid(request);
        call.enqueue(new Callback<SolicitacaoPidSimuladorResponse>() {
            @Override
            public void onResponse(Call<SolicitacaoPidSimuladorResponse> call, Response<SolicitacaoPidSimuladorResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<SolicitacaoPidSimuladorCreditoEfetivo> listaCreditoefetivo = new ArrayList<>();

                    if (response.body() != null) {
                        String jsonResponse = gson.toJson(response.body());
                        Log.d("Roni", "onResponse: " + jsonResponse);

                        SolicitacaoPidSimuladorResponse consulta = response.body();
                        if (consulta.getAlcada().isDentroDaAlcada())
                            tvDentroDaAlcada.setText("ALÇADA APROVADA");
                        else
                            tvDentroDaAlcada.setText("ALÇADA NÃO APROVADA");
                        tvAlcadaMensagem.setText("Mensagem: " + (consulta.getAlcada().getMensagem() != null ? consulta.getAlcada().getMensagem() : ""));
                        tvReceitaTotal.setText("R$ " + Util_IO.formataValor(consulta.getSimuladorTotais().getReceitaTotal()));
                        tvImpostoTotal.setText("R$ " + Util_IO.formataValor(consulta.getSimuladorTotais().getImpostoTotal()));
                        tvReceitaLiquidaTotal.setText("R$ " + Util_IO.formataValor(consulta.getSimuladorTotais().getReceitaTotalLiquida()));
                        tvReceitaBrutaTotal.setText("R$ " + Util_IO.formataValor(consulta.getSimuladorTotais().getRentabilidadeBruta()));
                        tvTakeRate.setText(Util_IO.formataValor(consulta.getSimuladorTotais().getTakeRate()) + " %");

                        // Carrega os Dados de Crédito Efetivo
                        for (SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse item : consulta.getCreditoEfetivo()) {
                            String descricao;
                            if (item.getNaturezaOperacao().equals("D"))
                                descricao = "Débito";
                            else
                                descricao = "Crédito " + item.getParcelas() + "x";
                            int index = buscarIndicePorDescricao(listaCreditoefetivo, descricao);

                            if (index == -1) {
                                SolicitacaoPidSimuladorCreditoEfetivo addRegistro = new SolicitacaoPidSimuladorCreditoEfetivo();
                                addRegistro.setDescricao(descricao);
                                switch (item.getIdBandeiraTipo()) {
                                    case 1:
                                        addRegistro.setTaxaBand1(item.getTaxaCredito());
                                        break;
                                    case 2:
                                        addRegistro.setTaxaBand2(item.getTaxaCredito());
                                        break;
                                    case 3:
                                        addRegistro.setTaxaBand3(item.getTaxaCredito());
                                        break;
                                    case 4:
                                        addRegistro.setTaxaBand4(item.getTaxaCredito());
                                        break;
                                    case 5:
                                        addRegistro.setTaxaBand5(item.getTaxaCredito());
                                        break;
                                }
                                listaCreditoefetivo.add(addRegistro);
                            } else {
                                switch (item.getIdBandeiraTipo()) {
                                    case 1:
                                        listaCreditoefetivo.get(index).setTaxaBand1(item.getTaxaCredito());
                                        break;
                                    case 2:
                                        listaCreditoefetivo.get(index).setTaxaBand2(item.getTaxaCredito());
                                        break;
                                    case 3:
                                        listaCreditoefetivo.get(index).setTaxaBand3(item.getTaxaCredito());
                                        break;
                                    case 4:
                                        listaCreditoefetivo.get(index).setTaxaBand4(item.getTaxaCredito());
                                        break;
                                    case 5:
                                        listaCreditoefetivo.get(index).setTaxaBand5(item.getTaxaCredito());
                                        break;
                                }
                            }
                        }

                        // Atualiza Taxas Recomendadas
                        if (consulta.getAlcada().getTaxasRecomendadas() != null) {
                            for (SolicitacaoPidSimuladorTaxasRecomendadasResponse item : consulta.getAlcada().getTaxasRecomendadas()) {
                                SolicitacaoPidTaxaMDR itemTaxas = Stream.ofNullable(listaSolicitacaoPidTaxaMDR)
                                        .filter(tax -> tax.getBandeiraTipoId() == item.getIdBandeiraTipo())
                                        .findFirst()
                                        .orElse(null);

                                if (itemTaxas != null) {
                                    itemTaxas.setTaxaDebitoRecomendada(item.getTaxaDebito());
                                    itemTaxas.setTaxaCreditoRecomendada(item.getTaxaCredito());
                                    itemTaxas.setTaxaCredito6xRecomendada(item.getTaxaCredito6x());
                                    itemTaxas.setTaxaCredito12xRecomendada(item.getTaxaCredito12x());
                                }
                            }

                            com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank flagsBank = new DBFlagsBank(Activity_SolPID_DadosCliente.this).getById(1);
                            llComercial_card_flags.updateSelected(flagsBank.getId());
                            llComercial_card_flags.onItemFlagBankSelected(flagsBank);
                        }
                    } else {
                        tvDentroDaAlcada.setText("ALÇADA NÃO APROVADA");
                        tvAlcadaMensagem.setText("Mensagem: ");
                        tvReceitaTotal.setText("R$ " + Util_IO.formataValor(0.00));
                        tvImpostoTotal.setText("R$ " + Util_IO.formataValor(0.00));
                        tvReceitaLiquidaTotal.setText("R$ " + Util_IO.formataValor(0.00));
                        tvReceitaBrutaTotal.setText("R$ " + Util_IO.formataValor(0.00));
                        tvTakeRate.setText(Util_IO.formataValor(0.00) + " %");
                    }

                    // Carrega a grid de Crédito efetivo (Apenas no caso RAV Automática)
                    if (!Util_IO.isNullOrEmpty(request.getTipoTaxaRav()) && request.getTipoTaxaRav().equals("AUT")) {
                        recycler_CreditoEfetivo.setVisibility(View.VISIBLE);
                        loadCreditoEfetivo(listaCreditoefetivo);
                    } else
                        recycler_CreditoEfetivo.setVisibility(View.GONE);
                } else {
                    Log.d("Roni", "Não executou: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<SolicitacaoPidSimuladorResponse> call, Throwable throwable) {
                Log.d("Roni", "onResponse: Não executou 1");
            }
        });
    }

    private static int buscarIndicePorDescricao(List<SolicitacaoPidSimuladorCreditoEfetivo> lista, String descricao) {
        for (int aa = 0; aa < lista.size(); aa++) {
            if (lista.get(aa).getDescricao().equals(descricao))
                return aa;
        }
        return -1;
    }

    private void loadCreditoEfetivo(ArrayList<SolicitacaoPidSimuladorCreditoEfetivo> lista) {
        ArrayList<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> listaBandeiras = new DBFlagsBank(Activity_SolPID_DadosCliente.this).getAll(null, null);
        adapterCustoefetivo = new ItemSolicitacaoPidCustoEfetivoAdapter(lista, Activity_SolPID_DadosCliente.this, listaBandeiras);
        recycler_CreditoEfetivo.setAdapter(adapterCustoefetivo);
    }

    private void GravarDados() throws Exception {
        if (spTipoCliente.getSelectedItemId() == null) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione o Tipo de Cliente");
            alerta.show();
            return;
        }

        if (spnMCC.getSelectedItemId() == null || spnMCC.getSelectedItemId() == 0) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione o MCC do Cliente");
            alerta.show();
            return;
        }

        if (Util_IO.isNullOrEmpty(edtCpfCNPJ.getText())) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Cpf/Cnpj do Cliente");
            alerta.show();
            return;
        }

        if (Util_IO.isNullOrEmpty(edtRazaoSocial.getText())) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe a Razão Social do Cliente");
            alerta.show();
            return;
        }

        if (Util_IO.isNullOrEmpty(edtNomeFantasia.getText())) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Nome Fantasia do Cliente");
            alerta.show();
            return;
        }

        if (Util_IO.isNullOrEmpty(edtFaturamentoPrevisto.getText()) || edtFaturamentoPrevisto.getCurrencyDouble() <= 0) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Informe o Faturamento Previsto");
            alerta.show();
            return;
        }

        if (spConcorrente.getSelectedItemId() == null) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione se possui concorrente ou não");
            alerta.show();
            return;
        }

        GPSTracker tracker = new GPSTracker(Activity_SolPID_DadosCliente.this);

        // Carrega Objeto para gravar
        SolicitacaoPid solicitacaoPidGravar = new SolicitacaoPid();

        if (spTipoPessoa.getSelectedItem() != null) {
            String tipoPessoa = spTipoPessoa.getSelectedItem().getIdValue() == PHYSICAL.getIdValue() ? "F" : "J";
            solicitacaoPidGravar.setTipoPessoa(tipoPessoa);
        }

        solicitacaoPidGravar.setTipoCliente(spTipoCliente.getSelectedItem().getDescriptionValue());
        solicitacaoPidGravar.setIdVendedor(new DBColaborador(Activity_SolPID_DadosCliente.this).get().getId());
        solicitacaoPidGravar.setLatitude(tracker.getLatitude());
        solicitacaoPidGravar.setLongitude(tracker.getLongitude());
        solicitacaoPidGravar.setPrecisao(tracker.getPrecisao());
        solicitacaoPidGravar.setCodigoSGV(edtCodigoSGV.getText());
        solicitacaoPidGravar.setCpfCnpj(StringUtils.returnOnlyNumbers(edtCpfCNPJ.getText()));
        solicitacaoPidGravar.setRazaoSocial(edtRazaoSocial.getText());
        solicitacaoPidGravar.setNomeFantasia(edtNomeFantasia.getText());
        solicitacaoPidGravar.setObservacao(edtObservacao.getText());
        solicitacaoPidGravar.setMCCPrincipal(spnMCC.getSelectedItem().getIdValue());

        // Redes
        if (solicitacaoPID.getListaRedes() != null && solicitacaoPID.getListaRedes().size() > 0) {
            solicitacaoPidGravar.setRede(1);
            solicitacaoPidGravar.setNDeLojas(solicitacaoPID.getListaRedes().size());
        } else {
            solicitacaoPidGravar.setRede(0);
            solicitacaoPidGravar.setNDeLojas(0);
        }
        solicitacaoPidGravar.setFaturamentoPrevisto(edtFaturamentoPrevisto.getCurrencyDouble());

        if (edtPercDebito.isEnabled())
            solicitacaoPidGravar.setDistribuicaoDebito(edtPercDebito.getCurrencyDouble());
        if (edtPercCredito.isEnabled())
            solicitacaoPidGravar.setDistribuicaoCredito(edtPercCredito.getCurrencyDouble());
        if (edtPErcCredito2a6.isEnabled())
            solicitacaoPidGravar.setDistribuicaoCredito6x(edtPErcCredito2a6.getCurrencyDouble());
        if (edtPercCredito7a12.isEnabled())
            solicitacaoPidGravar.setDistribuicaoCredito12x(edtPercCredito7a12.getCurrencyDouble());
        solicitacaoPidGravar.setOrigem("RFM");
        if (solicitacaoPidGravar.getCodigoSGV() != null)
            solicitacaoPidGravar.setRenegociacao(1);
        else
            solicitacaoPidGravar.setRenegociacao(0);
        solicitacaoPidGravar.setContraproposta(0);
        solicitacaoPidGravar.setSincronizado(0);
        solicitacaoPidGravar.setDataCadastro(new Date());

        // Mandar Status Inicial
        if (tvDentroDaAlcada.getText().equals("ALÇADA APROVADA"))
            solicitacaoPidGravar.setStatus("APV");
        else
            solicitacaoPidGravar.setStatus("ANS");

        // Gravar Dados
        long codigo = new BDSolicitacaoPid(Activity_SolPID_DadosCliente.this).addSolicitacaoPid(solicitacaoPidGravar);

        // Insere Anexos
        if (listaAnexos != null && listaAnexos.size() > 0) {
            for (SolicitacaoPidAnexo itemAnexo : listaAnexos) {
                itemAnexo.setIdSolicitacaoPid((int) codigo);
                new BDSolicitacaoPidAnexo(Activity_SolPID_DadosCliente.this).addSolicitacaoPidAnexo(itemAnexo);
            }
        }

        // Insere as Redes
        if (solicitacaoPID.getListaRedes() != null) {
            for (SolicitacaoPidRede item : solicitacaoPID.getListaRedes()) {
                item.setIdSolicitacaoPid((int) codigo);
                new BDSolicitacaoPidRede(Activity_SolPID_DadosCliente.this).addSolicitacaoPidRede(item);
            }
        }

        // Insere POS
        if (solicitacaoPID.getListaPOS() != null) {
            for (SolicitacaoPidPos item : solicitacaoPID.getListaPOS()) {
                item.setIdSolicitacaoPid((int) codigo);
                new BDSolicitacaoPidPOS(Activity_SolPID_DadosCliente.this).addSolicitacaoPidPOS(item);
            }
        }

        // Carrega dados Taxas RAV
        SolicitacaoPidTaxaRAV taxaRAV = new SolicitacaoPidTaxaRAV();
        if (solicitacaoPID != null && !Util_IO.isNullOrEmpty(solicitacaoPID.getTipoTaxaRAV())) {
            if (solicitacaoPID.getTipoTaxaRAV().equals("AUT")) {
                taxaRAV.setNovaPropostaRavAutomatica(edtPercNovaProposta.getCurrencyDouble());
                if (spConcorrente.getSelectedItem().getIdValue() == EnumRegisterAnticipation.YES.getIdValue())
                    taxaRAV.setConcorrenciaRavAutomatica(edtPercConcorrencia.getCurrencyDouble());
            } else {
                taxaRAV.setNovaPropostaRavEventual(edtPercNovaProposta.getCurrencyDouble());
                if (spConcorrente.getSelectedItem().getIdValue() == EnumRegisterAnticipation.YES.getIdValue())
                    taxaRAV.setConcorrenciaRavEventual(edtPercConcorrencia.getCurrencyDouble());
            }
        }

        // Insere Taxas
        if (listaSolicitacaoPidTaxaMDR != null && listaSolicitacaoPidTaxaMDR.size() > 0) {
            for (SolicitacaoPidTaxaMDR item : listaSolicitacaoPidTaxaMDR) {
                item.setIdSolicitacaoPid((int) codigo);
                if (taxaRAV != null) {
                    if (taxaRAV.getNovaPropostaRavAutomatica().isPresent())
                        item.setTaxaRavAutomatica(taxaRAV.getNovaPropostaRavAutomatica().get());
                    if (taxaRAV.getNovaPropostaRavEventual().isPresent())
                        item.setTaxaRavEventual(taxaRAV.getNovaPropostaRavEventual().get());
                    if (taxaRAV.getConcorrenciaRavAutomatica().isPresent())
                        item.setTaxaRavAutomaticaConcorrente(taxaRAV.getConcorrenciaRavAutomatica().get());
                    if (taxaRAV.getConcorrenciaRavEventual().isPresent())
                        item.setTaxaRavEventualConcorrente(taxaRAV.getConcorrenciaRavEventual().get());
                }
                new BDSolicitacaoPidTaxaMDR(Activity_SolPID_DadosCliente.this).addSolicitacaoPidTaxaMDR(item);
            }
        }

        // Carrega Produtos
        int pDebito = 0, pCredito = 0, pCredito6x = 0, pCredito12x = 0, pRavAut = 0, pRavEve = 0, pAluguel = 0;

        if (btnDebito.getTag().equals("1"))
            pDebito = 1;

        if (btnCredito.getTag().equals("1"))
            pCredito = 1;

        if (btnCredito2_6x.getTag().equals("1"))
            pCredito6x = 1;

        if (btnCredito7_12x.getTag().equals("1"))
            pCredito12x = 1;

        if (btnAluguel.getTag().equals("1"))
            pAluguel = 1;

        if (solicitacaoPID != null && !Util_IO.isNullOrEmpty(solicitacaoPID.getTipoTaxaRAV())) {
            if (solicitacaoPID.getTipoTaxaRAV().equals("AUT")) {
                pRavAut = 1;
            } else if (solicitacaoPID.getTipoTaxaRAV().equals("EVE"))
                pRavEve = 1;
        }

        if (listaSolicitacaoPidTaxaMDR != null && listaSolicitacaoPidTaxaMDR.size() > 0) {
            for (SolicitacaoPidTaxaMDR item : listaSolicitacaoPidTaxaMDR) {
                SolicitacaoPidProduto produto = new SolicitacaoPidProduto();
                produto.setIdSolicitacaoPid((int) codigo);
                produto.setBandeiraTipoId(item.getBandeiraTipoId());
                produto.setTaxaDebito(pDebito);
                produto.setTaxaCredito(pCredito);
                produto.setTaxaCredito6x(pCredito6x);
                produto.setTaxaCredito12x(pCredito12x);
                produto.setTaxaRavAutomatica(pRavAut);
                produto.setTaxaRavEventual(pRavEve);
                produto.setAluguel(pAluguel);
                new BDSolicitacaoPidProduto(Activity_SolPID_DadosCliente.this).addSolicitacaoPidPRoduto(produto);
            }
        }

        Toast.makeText(this, "Dados gravados com Sucesso!", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    @Override
    public void onSelecionarClick(int pos) {
        if (spTipoCliente.getSelectedItemId() == null) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione o Tipo de Cliente");
            alerta.show();
            return;
        }

        if (spnMCC.getSelectedItemId() == null || spnMCC.getSelectedItemId() == 0) {
            Alerta alerta = new Alerta(Activity_SolPID_DadosCliente.this, "Atenção!", "Selecione o MCC do Cliente");
            alerta.show();
            return;
        }

        com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank selectedItem = listaDadosBandeiras.get(pos);
        selectedItem.setActive(!selectedItem.isActive());
        adapterBandeiras.notifyDataSetChanged();

        List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> listaComercialBandeiras = listaDadosBandeiras.stream()
                .filter(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank::isActive)
                .collect(Collectors.toList());

        listaDadosBandeiras.forEach(item -> {
            if (item.isActive()) {
                boolean exists = listaSolicitacaoPidTaxaMDR.stream()
                        .anyMatch(itemTaxa -> itemTaxa.getBandeiraTipoId() == item.getId());
                if (!exists) {
                    // Carrega Campo
                    SolicitacaoPidTaxaMDR itemTaxa = new SolicitacaoPidTaxaMDR();
                    itemTaxa.setBandeiraTipoId(item.getId());
                    listaSolicitacaoPidTaxaMDR.add(itemTaxa);
                }
            } else {
                listaSolicitacaoPidTaxaMDR.removeIf(
                        itemTaxa -> itemTaxa.getBandeiraTipoId() == item.getId()
                );
            }
        });

        llComercial_card_flags.updateData(listaComercialBandeiras);
        llComercial_card_flags.updateSelected(0);
    }

    @Override
    public void onImageCaptured(File imageFile, int position) {

        Utilidades.obterPermissoesStorage(this);

        // Verifique se o arquivo de imagem foi criado corretamente
        if (imageFile == null || !imageFile.exists()) {
            Log.e("onImageCaptured", "O arquivo de imagem não foi criado.");
            return;
        }
        String filePath = Utilidades.getFilePath(this);
        File finalPhoto = null;
        try {
            finalPhoto = new Compressor(this)
                    .setDestinationDirectoryPath(filePath)
                    .compressToFile(imageFile);
        } catch (IOException e) {
            Utilidades
                    .retornaMensagem(this,
                            "Falta de Permissão de Acesso aos Arquvivos, verifique a configuração e tente novamente.",
                            true);
            return;
        }

        // Atualize o objeto na lista
        SolicitacaoPidAnexo pidAnexo = listaAnexos.get(position);
        pidAnexo.setAnexo(finalPhoto.getAbsolutePath());
        pidAnexo.setTamanhoarquivo(Formatter.formatShortFileSize(Activity_SolPID_DadosCliente.this, finalPhoto.length()));

        // Pega os dados GPS
        GPSTracker tracker = new GPSTracker(Activity_SolPID_DadosCliente.this);
        pidAnexo.setLatitude(tracker.getLatitude());
        pidAnexo.setLongitude(tracker.getLongitude());
        pidAnexo.setPrecisao(tracker.getPrecisao());
        adapterAnexos.notifyItemChanged(position);
    }

    @Override
    public void onImageCaptureFailed() {
    }
}