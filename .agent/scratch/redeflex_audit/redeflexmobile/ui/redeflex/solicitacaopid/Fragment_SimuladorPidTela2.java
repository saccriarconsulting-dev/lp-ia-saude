package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_BIGGER_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_IN_CASH;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_UNTIL_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.DEBIT;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemBandeiraAdapter;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.models.QrCodeGerarResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.models.TaxaMdrRequest;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.models.viewmodel.SolicitacaoPidViewModel;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.RetrofitClient;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBankEventListener;
import com.axys.redeflexmobile.ui.redeflex.FinalizarFormaPagtoActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Fragment_SimuladorPidTela2 extends Fragment implements ItemBandeiraAdapter.selecionarBandeira {
    Button btnVoltar, btnSimular;
    Button btnRavEventual, btnRavAutomatico, btnAluguel;
    Button btnDebito, btnCredito, btnCredito2_6x, btnCredito7_12x;
    TextView btnMarcar, btnDesmarcar;
    CustomEditText txtFaturamentoPrevisto, txtTaxaRav;
    CustomEditText txtDistPercDebito, txtDistPercCredito, txtDistPercCredito2_6, txtDistPercCredito7_12;
    TextView tvDistribuicaoTotal, tvDadosBandeira;
    CustomEditText txtTaxaPercDebito, txtTaxaPercCredito, txtTaxaPercCredito2_6, txtTaxaPercCredito7_12;
    TabLayout tabLayout;
    LinearLayout llAba1, llAba2;

    RecyclerView rvBandeiras;
    ItemBandeiraAdapter adapterBandeiras;
    private ProgressDialog progressDialog;
    private TextWatcher textWatcherTaxas;
    View viewLoading;

    private SolicitacaoPidViewModel solicitacaoPidViewModel;
    private SolicitacaoPid solPidTela2;
    ArrayList<FlagsBank> listaDadosBandeiras;
    private ArrayList<SolicitacaoPidTaxaMDR> listaSolicitacaoPidTaxaMDR;
    Integer controlaIdBandeiraSel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__simulador_pid_tela2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnVoltar = view.findViewById(R.id.llsimulador2_btnVoltar);
        btnSimular = view.findViewById(R.id.llsimulador2_btnSimular);
        txtFaturamentoPrevisto = view.findViewById(R.id.llsimulador2_edtFaturamentoPrevisto);
        txtTaxaRav = view.findViewById(R.id.llsimulador2_edtPercRAV);

        tabLayout = view.findViewById(R.id.llsimulador2_tab_layout);
        llAba1 = view.findViewById(R.id.llsimulador2_Aba1);
        llAba2 = view.findViewById(R.id.llsimulador2_Aba2);

        // Adiciona abas ao TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Distribuição TPV"));
        tabLayout.addTab(tabLayout.newTab().setText("Taxas por Bandeira"));

        btnRavEventual = view.findViewById(R.id.llsimulador2_btnRavEventual);
        btnRavAutomatico = view.findViewById(R.id.llsimulador2_btnRavAutomatica);
        btnAluguel = view.findViewById(R.id.llsimulador2_btnAluguel);

        btnDebito = view.findViewById(R.id.llsimulador2_btnDebito);
        btnCredito = view.findViewById(R.id.llsimulador2_btnCredito);
        btnCredito2_6x = view.findViewById(R.id.llsimulador2_btnCredito2_6x);
        btnCredito7_12x = view.findViewById(R.id.llsimulador2_btnCredito7_12x);

        btnMarcar = view.findViewById(R.id.llsimulador2_btnMarcar);
        btnDesmarcar = view.findViewById(R.id.llsimulador2_btnDesmarcar);

        txtDistPercDebito = view.findViewById(R.id.llsimulador2_edtPercDebito);
        txtDistPercCredito = view.findViewById(R.id.llsimulador2_edtPercCredito);
        txtDistPercCredito2_6 = view.findViewById(R.id.llsimulador2_edtPercCredito2_6);
        txtDistPercCredito7_12 = view.findViewById(R.id.llsimulador2_edtPercCredito7_12);
        tvDistribuicaoTotal = view.findViewById(R.id.llsimulador2_tvDistribuicaoTotal);

        rvBandeiras = view.findViewById(R.id.llsimulador2_card_flags);
        rvBandeiras.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        txtTaxaPercDebito = view.findViewById(R.id.llsimulador2_edtTaxaPercDebito);
        txtTaxaPercCredito = view.findViewById(R.id.llsimulador2_edtTaxaPercCredito);
        txtTaxaPercCredito2_6 = view.findViewById(R.id.llsimulador2_edtTaxaPercCredito2_6);
        txtTaxaPercCredito7_12 = view.findViewById(R.id.llsimulador2_edtTaxaPercCredito7_12);
        tvDadosBandeira = view.findViewById(R.id.llsimulador2_tvBandeira);

        viewLoading = view.findViewById(R.id.loading_view);

        criarEventos();
        iniciarBandeiras();

        // Seleciona a primeira aba e dispara o onTabSelected manualmente
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) {
            firstTab.select(); // Seleciona a primeira aba
            // Executar manualmente o comportamento de onTabSelected para a primeira aba
            llAba1.setVisibility(View.VISIBLE);
            llAba2.setVisibility(View.GONE);

            // Alterar estilo da Aba 1
            firstTab.view.setBackgroundColor(Color.parseColor("#A52A2A")); // Fundo vermelho
            tabLayout.setTabTextColors(Color.parseColor("#A52A2A"), Color.WHITE); // Texto branco ao ser selecionado
        }

        // Atualiza tela com os dados já informados
        solicitacaoPidViewModel = new ViewModelProvider(requireActivity()).get(SolicitacaoPidViewModel.class);
        solicitacaoPidViewModel.getSolicitacaoPid().observe(getViewLifecycleOwner(), solicitacaoPid -> {
            if (solicitacaoPid != null) {
                solPidTela2 = solicitacaoPid;

                txtFaturamentoPrevisto.setText(Util_IO.formataValor(solicitacaoPid.getFaturamentoPrevisto()));
                txtTaxaRav.setText(Util_IO.formatDoubleToDecimalPercent(solicitacaoPid.getValorTaxaRAV()));

                if (!Util_IO.isNullOrEmpty(solicitacaoPid.getTipoTaxaRAV())) {
                    if (solicitacaoPid.getTipoTaxaRAV().equals("AUT"))
                        btnRavAutomatico.callOnClick();
                    else if (solicitacaoPid.getTipoTaxaRAV().equals("EVE"))
                        btnRavEventual.callOnClick();
                }

                if (solicitacaoPid.getAluguel() == 1)
                    btnAluguel.callOnClick();

                if (solicitacaoPid.getModDebito() == 1)
                    btnDebito.callOnClick();

                if (solicitacaoPid.getModCredito() == 1)
                    btnCredito.callOnClick();

                if (solicitacaoPid.getModCredito2a6() == 1)
                    btnCredito2_6x.callOnClick();

                if (solicitacaoPid.getModCredito7a12() == 1)
                    btnCredito7_12x.callOnClick();

                // Carrega as Percentuais de Distribuição Faturamento
                if (solicitacaoPid.getDistribuicaoDebito().isPresent())
                    txtDistPercDebito.setText(Util_IO.formatDoubleToDecimalPercent(solicitacaoPid.getDistribuicaoDebito().get()));
                if (solicitacaoPid.getDistribuicaoCredito().isPresent())
                    txtDistPercCredito.setText(Util_IO.formatDoubleToDecimalPercent(solicitacaoPid.getDistribuicaoCredito().get()));
                if (solicitacaoPid.getDistribuicaoCredito6x().isPresent())
                    txtDistPercCredito2_6.setText(Util_IO.formatDoubleToDecimalPercent(solicitacaoPid.getDistribuicaoCredito6x().get()));
                if (solicitacaoPid.getDistribuicaoCredito12x().isPresent())
                    txtDistPercCredito7_12.setText(Util_IO.formatDoubleToDecimalPercent(solicitacaoPid.getDistribuicaoCredito12x().get()));

                // Carrega Taxas, caso informadas
                if (solPidTela2.getListaTaxas() != null && solPidTela2.getListaTaxas().size() > 0) {
                    listaSolicitacaoPidTaxaMDR = solPidTela2.getListaTaxas();
                    for (SolicitacaoPidTaxaMDR itemBandeira : listaSolicitacaoPidTaxaMDR) {
                        Optional<FlagsBank> existingBandeira = listaDadosBandeiras.stream()
                                .filter(item -> itemBandeira.getBandeiraTipoId() == item.getId())
                                .findFirst();
                        if (existingBandeira.isPresent())
                            existingBandeira.get().setActive(true);
                    }
                    adapterBandeiras.notifyDataSetChanged();
                    controlaIdBandeiraSel = 0;
                } else
                    listaSolicitacaoPidTaxaMDR = new ArrayList<>();
            }
        });
    }

    public void InicializaTaxasMDR() {
        txtTaxaPercDebito.setText("");
        txtTaxaPercCredito.setText("");
        txtTaxaPercCredito2_6.setText("");
        txtTaxaPercCredito7_12.setText("");
    }

    private void iniciarBandeiras() {
        listaDadosBandeiras = new DBFlagsBank(requireActivity()).getAll(null, null);
        for (com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank item : listaDadosBandeiras) {
            item.setActive(false);
        }
        adapterBandeiras = new ItemBandeiraAdapter(listaDadosBandeiras, requireActivity(), this);
        rvBandeiras.setAdapter(adapterBandeiras);
    }

    private void atualizaViewModel()
    {
        solPidTela2.setFaturamentoPrevisto(txtFaturamentoPrevisto.getCurrencyDouble());
        solPidTela2.setValorTaxaRAV(txtTaxaRav.getCurrencyDouble());

        // Tipo RAV
        if (btnRavAutomatico.getTag().equals("1"))
            solPidTela2.setTipoTaxaRAV("AUT");
        else if (btnRavEventual.getTag().equals("1"))
            solPidTela2.setTipoTaxaRAV("EVE");
        else
            solPidTela2.setTipoTaxaRAV(null);

        // Tem Aluguel
        if (btnAluguel.getTag().equals("1"))
            solPidTela2.setAluguel(1);
        else
            solPidTela2.setAluguel(0);

        // Modalidades
        if (btnDebito.getTag().equals("1"))
            solPidTela2.setModDebito(1);
        else
            solPidTela2.setModDebito(0);

        if (btnCredito.getTag().equals("1"))
            solPidTela2.setModCredito(1);
        else
            solPidTela2.setModCredito(0);

        if (btnCredito2_6x.getTag().equals("1"))
            solPidTela2.setModCredito2a6(1);
        else
            solPidTela2.setModCredito2a6(0);

        if (btnCredito7_12x.getTag().equals("1"))
            solPidTela2.setModCredito7a12(1);
        else
            solPidTela2.setModCredito7a12(0);

        // Percentuais de Distribuição
        if (!Util_IO.isNullOrEmpty(txtDistPercDebito.getText()))
            solPidTela2.setDistribuicaoDebito(txtDistPercDebito.getCurrencyDouble());
        else
            solPidTela2.setDistribuicaoDebito(null);

        if (!Util_IO.isNullOrEmpty(txtDistPercCredito.getText()))
            solPidTela2.setDistribuicaoCredito(txtDistPercCredito.getCurrencyDouble());
        else
            solPidTela2.setDistribuicaoCredito(null);

        if (!Util_IO.isNullOrEmpty(txtDistPercCredito2_6.getText()))
            solPidTela2.setDistribuicaoCredito6x(txtDistPercCredito2_6.getCurrencyDouble());
        else
            solPidTela2.setDistribuicaoCredito6x(null);

        if (!Util_IO.isNullOrEmpty(txtDistPercCredito7_12.getText()))
            solPidTela2.setDistribuicaoCredito12x(txtDistPercCredito7_12.getCurrencyDouble());
        else
            solPidTela2.setDistribuicaoCredito12x(null);

        // Atualiza lista de taxas
        solPidTela2.setListaTaxas(listaSolicitacaoPidTaxaMDR);

        // Atualiza ViewModel
        solicitacaoPidViewModel.setSolicitacaoPid(solPidTela2);
    }

    private void criarEventos() {
        btnVoltar.setOnClickListener(v -> {
            atualizaViewModel();

            // Navegar de volta para Tela1
            getParentFragmentManager().popBackStack();
        });

        btnSimular.setOnClickListener(v -> {
            if (Util_IO.isNullOrEmpty(txtFaturamentoPrevisto.getText()) || txtFaturamentoPrevisto.getCurrencyDouble() <= 0) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Informe o Faturamento Previsto. Verifique!!!");
                alerta.show();
                return;
            }

            if (Util_IO.isNullOrEmpty(txtTaxaRav.getText()) || txtTaxaRav.getCurrencyDouble() <= 0) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Informe a Taxa Rav. Verifique!!!");
                alerta.show();
                return;
            }

            if (CalculaDistribuicao() < 100)
            {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Distribuição de Faturamento menor que 100%. Verifique!!!");
                alerta.show();
                return;
            }

            if (listaSolicitacaoPidTaxaMDR == null || listaSolicitacaoPidTaxaMDR.size() == 0)
            {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Nenhuma Bandeira selecionada para a Simulação. Verifique!!!");
                alerta.show();
                return;
            }

            // Atualiza ViewModel
            atualizaViewModel();

            // Vai para a próxima tela
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.simuladorpid_fragment_container, new Fragment_SimuladorPidTela3())
                    .addToBackStack(null)
                    .commit();
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Aba 1 selecionada: Mostrar conteúdo da aba 1 e aplicar estilo
                    llAba1.setVisibility(View.VISIBLE);
                    llAba2.setVisibility(View.GONE);

                    // Alterar estilo da Aba 1
                    tab.view.setBackgroundColor(Color.parseColor("#A52A2A")); // Fundo vermelho
                    tabLayout.setTabTextColors(Color.parseColor("#A52A2A"), Color.WHITE); // Texto branco ao ser selecionado
                } else if (position == 1) {
                    // Aba 2 selecionada: Mostrar conteúdo da aba 2 e aplicar estilo padrão
                    llAba1.setVisibility(View.GONE);
                    llAba2.setVisibility(View.VISIBLE);

                    // Alterar estilo da Aba 2
                    tab.view.setBackgroundColor(Color.parseColor("#A52A2A")); // Fundo vermelho
                    tabLayout.setTabTextColors(Color.parseColor("#A52A2A"), Color.WHITE); // Texto branco ao ser selecionado
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Restaurar o estilo padrão ao desmarcar a aba
                tab.view.setBackgroundColor(Color.WHITE); // Restaurar o fundo branco
                tabLayout.setTabTextColors(Color.parseColor("#A52A2A"), Color.WHITE); // Texto branco ao ser selecionado
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnAluguel.setOnClickListener(v -> {
            if (btnAluguel.getTag().equals("1")) {
                btnAluguel.setTag("0");
                btnAluguel.setBackgroundColor(Color.WHITE);
                btnAluguel.setTextColor(Color.GRAY);
            } else {
                btnAluguel.setTag("1");
                btnAluguel.setBackgroundColor(Color.DKGRAY);
                btnAluguel.setTextColor(Color.WHITE);
            }
        });

        btnRavEventual.setOnClickListener(v -> {
            if (btnRavEventual.getTag().equals("1")) {
                btnRavEventual.setTag("0");
                btnRavEventual.setBackgroundColor(Color.WHITE);
                btnRavEventual.setTextColor(Color.GRAY);
            } else {
                btnRavEventual.setTag("1");
                btnRavEventual.setBackgroundColor(Color.DKGRAY);
                btnRavEventual.setTextColor(Color.WHITE);

                btnRavAutomatico.setTag("0");
                btnRavAutomatico.setBackgroundColor(Color.WHITE);
                btnRavAutomatico.setTextColor(Color.GRAY);
            }
        });

        btnRavAutomatico.setOnClickListener(v -> {
            if (btnRavAutomatico.getTag().equals("1")) {
                btnRavAutomatico.setTag("0");
                btnRavAutomatico.setBackgroundColor(Color.WHITE);
                btnRavAutomatico.setTextColor(Color.GRAY);
            } else {
                btnRavAutomatico.setTag("1");
                btnRavAutomatico.setBackgroundColor(Color.DKGRAY);
                btnRavAutomatico.setTextColor(Color.WHITE);

                btnRavEventual.setTag("0");
                btnRavEventual.setBackgroundColor(Color.WHITE);
                btnRavEventual.setTextColor(Color.GRAY);
            }
        });

        btnDebito.setOnClickListener(v -> {
            selecionarButtonModalidade(btnDebito);
        });

        btnCredito.setOnClickListener(v -> {
            selecionarButtonModalidade(btnCredito);
        });

        btnCredito2_6x.setOnClickListener(v -> {
            selecionarButtonModalidade(btnCredito2_6x);
        });

        btnCredito7_12x.setOnClickListener(v -> {
            selecionarButtonModalidade(btnCredito7_12x);
        });

        btnMarcar.setOnClickListener(v -> {
            selecionarModalidades(true);
        });

        btnDesmarcar.setOnClickListener(v -> {
            selecionarModalidades(false);
        });

        // Adicionando TextWatcher
        adicionarTextWatcher(txtDistPercDebito);
        adicionarTextWatcher(txtDistPercCredito);
        adicionarTextWatcher(txtDistPercCredito2_6);
        adicionarTextWatcher(txtDistPercCredito7_12);

        adicionaEventoTaxasMDR();
    }

    private void adicionarTextWatcher(CustomEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não há necessidade de implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não há necessidade de implementar
            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaDistribuicao();
            }
        });
    }

    private void adicionarTextWatcherCamposTaxas(CustomEditText editText) {
        textWatcherTaxas = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Lógica antes de mudar o texto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lógica quando o texto muda
            }

            @Override
            public void afterTextChanged(Editable s) {
                AtualizaDadosTaxa(s);
            }
        };
        editText.addTextChangedListener(textWatcherTaxas);
        editText.setTag(R.id.textWatcher, textWatcherTaxas);
    }

    private void removerTextWatcherCamposTaxas(CustomEditText editText) {
        Object tag = editText.getTag(R.id.textWatcher);
        if (tag instanceof TextWatcher) {
            // Remove o TextWatcher e limpa a referência
            editText.removeTextChangedListener((TextWatcher) tag);
            editText.setTag(R.id.textWatcher, null);  // Limpa o Tag
        }
    }

    private void adicionaEventoTaxasMDR() {
        adicionarTextWatcherCamposTaxas(txtTaxaPercDebito);
        adicionarTextWatcherCamposTaxas(txtTaxaPercCredito);
        adicionarTextWatcherCamposTaxas(txtTaxaPercCredito2_6);
        adicionarTextWatcherCamposTaxas(txtTaxaPercCredito7_12);
    }

    private void removeEventoTaxasMDR() {
        removerTextWatcherCamposTaxas(txtTaxaPercDebito);
        removerTextWatcherCamposTaxas(txtTaxaPercCredito);
        removerTextWatcherCamposTaxas(txtTaxaPercCredito2_6);
        removerTextWatcherCamposTaxas(txtTaxaPercCredito7_12);
    }

    public void AtualizaDadosTaxa(Editable s) {
        if (controlaIdBandeiraSel != null) {
            if (txtTaxaPercDebito.hasFocus()) {
                saveTaxChanged(controlaIdBandeiraSel, DEBIT, txtTaxaPercDebito.getCurrencyDouble(), txtTaxaPercDebito);
            } else if (txtTaxaPercCredito.hasFocus()) {
                saveTaxChanged(controlaIdBandeiraSel, CREDIT_IN_CASH, txtTaxaPercCredito.getCurrencyDouble(), txtTaxaPercCredito);
            } else if (txtTaxaPercCredito2_6.hasFocus()) {
                saveTaxChanged(controlaIdBandeiraSel, CREDIT_UNTIL_SIX, txtTaxaPercCredito2_6.getCurrencyDouble(), txtTaxaPercCredito2_6);
            } else if (txtTaxaPercCredito7_12.hasFocus()) {
                saveTaxChanged(controlaIdBandeiraSel, CREDIT_BIGGER_SIX, txtTaxaPercCredito7_12.getCurrencyDouble(), txtTaxaPercCredito7_12);
            }
        }
    }

    public void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value, CustomEditText componentEdit) {
        Stream.ofNullable(listaSolicitacaoPidTaxaMDR).forEach(pidTaxaMDR -> {
            if (pidTaxaMDR.getBandeiraTipoId() == flagType) {
                switch (taxType) {
                    case DEBIT:
                        if (componentEdit.getId() == R.id.llsimulador2_edtTaxaPercDebito)
                            pidTaxaMDR.setTaxaDebito(value);
                        break;
                    case CREDIT_IN_CASH:
                        if (componentEdit.getId() == R.id.llsimulador2_edtTaxaPercCredito)
                            pidTaxaMDR.setTaxaCredito(value);
                        break;
                    case CREDIT_UNTIL_SIX:
                        if (componentEdit.getId() == R.id.llsimulador2_edtTaxaPercCredito2_6)
                            pidTaxaMDR.setTaxaCredito6x(value);
                        break;
                    case CREDIT_BIGGER_SIX:
                        if (componentEdit.getId() == R.id.llsimulador2_edtTaxaPercCredito7_12)
                            pidTaxaMDR.setTaxaCredito12x(value);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void selecionarButtonModalidade(View view) {
        Button button = (Button) view;
        String currentTag = button.getTag().toString();

        if (currentTag.equals("1")) {
            button.setTag("0");
            button.setBackgroundColor(Color.WHITE);
            button.setTextColor(Color.GRAY);
        } else {
            button.setTag("1");
            button.setBackgroundColor(Color.DKGRAY);
            button.setTextColor(Color.WHITE);
        }

        HabilitaCamposOnClickProdutos();
    }

    public void selecionarModalidades(boolean pHabilita) {
        // Altera TAG
        btnDebito.setTag(pHabilita ? "0" : "1");
        btnCredito.setTag(pHabilita ? "0" : "1");
        btnCredito2_6x.setTag(pHabilita ? "0" : "1");
        btnCredito7_12x.setTag(pHabilita ? "0" : "1");

        // Executa seleção conforme TAG
        selecionarButtonModalidade(btnDebito);
        selecionarButtonModalidade(btnCredito);
        selecionarButtonModalidade(btnCredito2_6x);
        selecionarButtonModalidade(btnCredito7_12x);
    }

    public void HabilitaCamposOnClickProdutos() {
        if (btnDebito.getTag().equals("1")) {
            txtDistPercDebito.setEnabled(true);
        } else {
            txtDistPercDebito.setText("");
            txtDistPercDebito.setEnabled(false);
        }

        if (btnCredito.getTag().equals("1")) {
            txtDistPercCredito.setEnabled(true);
        } else {
            txtDistPercCredito.setText("");
            txtDistPercCredito.setEnabled(false);
        }

        if (btnCredito2_6x.getTag().equals("1")) {
            txtDistPercCredito2_6.setEnabled(true);
        } else {
            txtDistPercCredito2_6.setText("");
            txtDistPercCredito2_6.setEnabled(false);
        }

        if (btnCredito7_12x.getTag().equals("1")) {
            txtDistPercCredito7_12.setEnabled(true);
        } else {
            txtDistPercCredito7_12.setText("");
            txtDistPercCredito7_12.setEnabled(false);
        }
    }

    private void atualizaTaxas(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank selectedItem) {
        removeEventoTaxasMDR();

        if (selectedItem.isActive()) {
            Optional<SolicitacaoPidTaxaMDR> existingItemTaxa = listaSolicitacaoPidTaxaMDR.stream()
                    .filter(itemTaxa -> itemTaxa.getBandeiraTipoId() == selectedItem.getId())
                    .findFirst();
            if (existingItemTaxa.isPresent()) {
                // Se já existe, carrega os dados no campo txtTaxaPercDebito
                SolicitacaoPidTaxaMDR itemTaxa = existingItemTaxa.get();
                if (itemTaxa.getTaxaDebito().isPresent())
                    txtTaxaPercDebito.setText(Util_IO.formatDoubleToDecimalPercent(itemTaxa.getTaxaDebito().get()));
                if (itemTaxa.getTaxaCredito().isPresent())
                    txtTaxaPercCredito.setText(Util_IO.formatDoubleToDecimalPercent(itemTaxa.getTaxaCredito().get()));
                if (itemTaxa.getTaxaCredito6x().isPresent())
                    txtTaxaPercCredito2_6.setText(Util_IO.formatDoubleToDecimalPercent(itemTaxa.getTaxaCredito6x().get()));
                if (itemTaxa.getTaxaCredito12x().isPresent())
                    txtTaxaPercCredito7_12.setText(Util_IO.formatDoubleToDecimalPercent(itemTaxa.getTaxaCredito12x().get()));
            }

            tvDadosBandeira.setText("Dados da Bandeira: " + selectedItem.getName().toString().trim());
            tvDadosBandeira.setVisibility(View.VISIBLE);
            if (btnDebito.getTag().equals("1") && !(selectedItem.getId() == 3 || selectedItem.getId() == 5))
                txtTaxaPercDebito.setEnabled(true);
            else
                txtTaxaPercDebito.setEnabled(false);

            if (btnCredito.getTag().equals("1"))
                txtTaxaPercCredito.setEnabled(true);
            else
                txtTaxaPercCredito.setEnabled(false);

            if (btnCredito2_6x.getTag().equals("1"))
                txtTaxaPercCredito2_6.setEnabled(true);
            else
                txtTaxaPercCredito2_6.setEnabled(false);

            if (btnCredito7_12x.getTag().equals("1"))
                txtTaxaPercCredito7_12.setEnabled(true);
            else
                txtTaxaPercCredito7_12.setEnabled(false);
        } else {
            tvDadosBandeira.setText("");
            tvDadosBandeira.setVisibility(View.GONE);
            txtTaxaPercDebito.setEnabled(false);
            txtTaxaPercCredito.setEnabled(false);
            txtTaxaPercCredito2_6.setEnabled(false);
            txtTaxaPercCredito7_12.setEnabled(false);
        }

        adicionaEventoTaxasMDR();
    }

    @Override
    public void onSelecionarClick(int pos) {
        if (Util_IO.isNullOrEmpty(txtFaturamentoPrevisto.getText()) || txtFaturamentoPrevisto.getCurrencyDouble() <= 0) {
            Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Informe o Faturamento Previsto");
            alerta.show();
            return;
        }

        removeEventoTaxasMDR();
        InicializaTaxasMDR();
        adicionaEventoTaxasMDR();

        com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank selectedItem = listaDadosBandeiras.get(pos);
        if (controlaIdBandeiraSel != null) {
            if (listaDadosBandeiras.get(pos).getId() == controlaIdBandeiraSel && selectedItem.isActive()) {
                selectedItem.setActive(!selectedItem.isActive());
                adapterBandeiras.notifyDataSetChanged();
            } else if (selectedItem.isActive() == false) {
                selectedItem.setActive(!selectedItem.isActive());
                adapterBandeiras.notifyDataSetChanged();
            }
        } else if (controlaIdBandeiraSel == null) {
            selectedItem.setActive(!selectedItem.isActive());
            adapterBandeiras.notifyDataSetChanged();
        }

        // Ao selecionar se estiver ativa adiciona a lista caso esteja sendo desmarcada retira
        listaDadosBandeiras.forEach(item -> {
            if (item.isActive()) {
                Optional<SolicitacaoPidTaxaMDR> existingItemTaxa = listaSolicitacaoPidTaxaMDR.stream()
                        .filter(itemTaxa -> itemTaxa.getBandeiraTipoId() == item.getId())
                        .findFirst();
                if (!existingItemTaxa.isPresent()) {
                    viewLoading.setVisibility(View.VISIBLE);
                    RegisterService services;
                    Retrofit retrofit = RetrofitClient.getRetrofit();
                    services = retrofit.create(RegisterService.class);

                    TaxaMdrRequest taxaMdrRequest = new TaxaMdrRequest();
                    taxaMdrRequest.setPessoaTipoId(EnumRegisterPersonType.getEnumByCharValue(solPidTela2.getTipoPessoa()).getIdValue());
                    taxaMdrRequest.setTaxaMDRTipoPrazoNegociacaoId(2);
                    taxaMdrRequest.setTaxaMDRTipoNegociacaoId(Util_IO.isNullOrEmpty(solPidTela2.getCodigoSGV()) ? 1 : 2);
                    taxaMdrRequest.setBandeiraTipoId(item.getId());
                    taxaMdrRequest.setMCC(solPidTela2.getMCCPrincipal());
                    taxaMdrRequest.setFaturamento(txtFaturamentoPrevisto.getCurrencyDouble());

                    if (solPidTela2.getTipoCliente().equals("ISO"))
                        taxaMdrRequest.setTaxaMDRTipoClassificacaoId(1);
                    else if (solPidTela2.getTipoCliente().equals("SUB"))
                        taxaMdrRequest.setTaxaMDRTipoClassificacaoId(2);
                    else if (solPidTela2.getTipoCliente().equals("ADQ"))
                        taxaMdrRequest.setTaxaMDRTipoClassificacaoId(3);

                    if (btnRavEventual.getTag().equals("1") || btnRavAutomatico.getTag().equals("1"))
                        taxaMdrRequest.setRav(1);
                    else
                        taxaMdrRequest.setRav(0);

                    Call<TaxaMdr> call = services.getGetTaxaMdr(taxaMdrRequest);
                    call.enqueue(new Callback<TaxaMdr>() {
                        @Override
                        public void onResponse(Call<TaxaMdr> call, Response<TaxaMdr> response) {
                            if (response.isSuccessful()) {
                                viewLoading.setVisibility(View.GONE);
                                if (response.body() != null) {
                                    TaxaMdr retorno = response.body();
                                    // Carrega Campo
                                    SolicitacaoPidTaxaMDR newItemTaxa = new SolicitacaoPidTaxaMDR();
                                    newItemTaxa.setBandeiraTipoId(item.getId());
                                    newItemTaxa.setTaxaDebito(retorno.getTaxDebit());
                                    newItemTaxa.setTaxaCredito(retorno.getTaxCredit());
                                    newItemTaxa.setTaxaCredito6x(retorno.getTaxUntilSix());
                                    newItemTaxa.setTaxaCredito12x(retorno.getTaxBiggerSix());
                                    listaSolicitacaoPidTaxaMDR.add(newItemTaxa);
                                } else {
                                    SolicitacaoPidTaxaMDR newItemTaxa = new SolicitacaoPidTaxaMDR();
                                    newItemTaxa.setBandeiraTipoId(item.getId());
                                    listaSolicitacaoPidTaxaMDR.add(newItemTaxa);
                                }
                            } else {
                                viewLoading.setVisibility(View.GONE);
                                SolicitacaoPidTaxaMDR newItemTaxa = new SolicitacaoPidTaxaMDR();
                                newItemTaxa.setBandeiraTipoId(item.getId());
                                listaSolicitacaoPidTaxaMDR.add(newItemTaxa);
                            }

                            adapterBandeiras.notifyDataSetChanged();
                            atualizaTaxas(selectedItem);
                        }

                        @Override
                        public void onFailure(Call<TaxaMdr> call, Throwable throwable) {
                            viewLoading.setVisibility(View.GONE);
                            SolicitacaoPidTaxaMDR newItemTaxa = new SolicitacaoPidTaxaMDR();
                            newItemTaxa.setBandeiraTipoId(item.getId());
                            listaSolicitacaoPidTaxaMDR.add(newItemTaxa);

                            adapterBandeiras.notifyDataSetChanged();
                            atualizaTaxas(selectedItem);
                        }
                    });
                } else {
                    atualizaTaxas(selectedItem);
                }
            } else {
                listaSolicitacaoPidTaxaMDR.removeIf(
                        itemTaxa -> itemTaxa.getBandeiraTipoId() == item.getId()
                );
            }
        });

        controlaIdBandeiraSel = listaDadosBandeiras.get(pos).getId();
    }

    public double CalculaDistribuicao() {
        double vPercDebito = obterValorOuZero(txtDistPercDebito);
        double vPercCredito = obterValorOuZero(txtDistPercCredito);
        double vPercCredito2 = obterValorOuZero(txtDistPercCredito2_6);
        double vPercCredito7 = obterValorOuZero(txtDistPercCredito7_12);

        double valorTotal = vPercDebito + vPercCredito + vPercCredito2 + vPercCredito7;
        tvDistribuicaoTotal.setText(String.format("Distribuição Total: %s %%", Util_IO.formataValor(valorTotal)));
        return valorTotal;
    }

    private double obterValorOuZero(CustomEditText campo) {
        return !Util_IO.isNullOrEmpty(campo.getText()) ? campo.getCurrencyDouble() : 0;
    }
}