package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemSolicitacaoPidCustoEfetivoAdapter;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorBandeiraCreditoEfetivoResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorCreditoEfetivo;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorRequest;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorResponse;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidSimuladorTaxaBandeiraRequest;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.models.viewmodel.SolicitacaoPidViewModel;
import com.axys.redeflexmobile.shared.services.SolicitacaoPidService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.RetrofitClient;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Fragment_SimuladorPidTela3 extends Fragment {
    Button btnVoltar, btnContinuar;
    TextView tvDentroDaAlcada, tvAlcadaMensagem, tvReceitaTotal, tvImpostoTotal, tvReceitaLiquidaTotal, tvReceitaBrutaTotal, tvTakeRate;
    RecyclerView recycler_CreditoEfetivo;
    ItemSolicitacaoPidCustoEfetivoAdapter adapterCustoefetivo;
    View viewLoading;
    private SolicitacaoPidViewModel solicitacaoPidViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__simulador_pid_tela3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnVoltar = view.findViewById(R.id.llsimulador3_btnVoltar);
        btnContinuar = view.findViewById(R.id.llsimulador3_btnContinuar);

        tvDentroDaAlcada = view.findViewById(R.id.llsimulador3_tvDentroDaAlcada);
        tvAlcadaMensagem = view.findViewById(R.id.llsimulador3_tvAlcadaMensagem);
        tvReceitaTotal = view.findViewById(R.id.llsimulador3_tvReceitaTotal);
        tvImpostoTotal = view.findViewById(R.id.llsimulador3_tvImpostoTotal);
        tvReceitaLiquidaTotal = view.findViewById(R.id.llsimulador3_tvReceitaLiquidaTotal);
        tvReceitaBrutaTotal = view.findViewById(R.id.llsimulador3_tvReceitaBrutaTotal);
        tvTakeRate = view.findViewById(R.id.llsimulador3_tvTakeRate);

        recycler_CreditoEfetivo = view.findViewById(R.id.llsimulador3_recycler_CreditoEfetivo);
        recycler_CreditoEfetivo.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        viewLoading = view.findViewById(R.id.loading_view);

        criarEventos();
        carregarDados();
    }

    private void carregarDados(){
        solicitacaoPidViewModel = new ViewModelProvider(requireActivity()).get(SolicitacaoPidViewModel.class);
        solicitacaoPidViewModel.getSolicitacaoPid().observe(getViewLifecycleOwner(), solicitacaoPid -> {
            viewLoading.setVisibility(View.VISIBLE);

            SolicitacaoPidSimuladorRequest request = new SolicitacaoPidSimuladorRequest();
            request.setTipoCliente(solicitacaoPid.getTipoCliente());
            request.setMcc(String.valueOf(solicitacaoPid.getMCCPrincipal()));
            request.setCpfCnpj(solicitacaoPid.getCpfCnpj());
            request.setTpvTotal(solicitacaoPid.getFaturamentoPrevisto());
            request.setTaxaRav(solicitacaoPid.getValorTaxaRAV());
            if (!solicitacaoPid.getTipoTaxaRAV().isEmpty())
                request.setTipoTaxaRav(solicitacaoPid.getTipoTaxaRAV());

            if (solicitacaoPid.getDistribuicaoDebito().isPresent())
                request.setDistribuicaoDebitoProposta(solicitacaoPid.getDistribuicaoDebito().get());
            if (solicitacaoPid.getDistribuicaoCredito().isPresent())
                request.setDistribuicaoCreditoProposta(solicitacaoPid.getDistribuicaoCredito().get());
            if (solicitacaoPid.getDistribuicaoCredito6x().isPresent())
                request.setDistribuicaoCredito6xProposta(solicitacaoPid.getDistribuicaoCredito6x().get());
            if (solicitacaoPid.getDistribuicaoCredito12x().isPresent())
                request.setDistribuicaoCredito12xProposta(solicitacaoPid.getDistribuicaoCredito12x().get());

            // Taxas Bandeiras
            List<SolicitacaoPidSimuladorTaxaBandeiraRequest> lista = new ArrayList<>();
            SolicitacaoPidSimuladorTaxaBandeiraRequest txBandeira = new SolicitacaoPidSimuladorTaxaBandeiraRequest();
            for (SolicitacaoPidTaxaMDR item : solicitacaoPid.getListaTaxas()) {
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
            Call<SolicitacaoPidSimuladorResponse> call = services.simuladorPid(request);
            call.enqueue(new Callback<SolicitacaoPidSimuladorResponse>() {
                @Override
                public void onResponse(Call<SolicitacaoPidSimuladorResponse> call, Response<SolicitacaoPidSimuladorResponse> response) {
                    if (response.isSuccessful()) {
                        ArrayList<SolicitacaoPidSimuladorCreditoEfetivo> listaCreditoefetivo = new ArrayList<>();
                        if (response.body() != null) {
                            String jsonResponse = gson.toJson(response.body());
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

                        viewLoading.setVisibility(View.GONE);
                    } else {
                        viewLoading.setVisibility(View.GONE);
                        Alerta alerta = new Alerta(requireActivity(), "Erro", "Erro ao Simular Dados!");
                        alerta.show();
                    }
                }

                @Override
                public void onFailure(Call<SolicitacaoPidSimuladorResponse> call, Throwable throwable) {
                    viewLoading.setVisibility(View.GONE);
                    Alerta alerta = new Alerta(requireActivity(), "Erro", "Erro ao Simular Dados!");
                    alerta.show();
                }
            });
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
        ArrayList<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> listaBandeiras = new DBFlagsBank(requireActivity()).getAll(null, null);
        adapterCustoefetivo = new ItemSolicitacaoPidCustoEfetivoAdapter(lista, requireActivity(), listaBandeiras);
        recycler_CreditoEfetivo.setAdapter(adapterCustoefetivo);
    }

    private void criarEventos() {
        btnVoltar.setOnClickListener(v -> {
            // Navegar de volta para Tela1
            getParentFragmentManager().popBackStack();
        });

        btnContinuar.setOnClickListener(v -> {
            SolicitacaoPid solicitacaoPid = solicitacaoPidViewModel.getSolicitacaoPid().getValue();
            Intent intent = new Intent(requireActivity(), Activity_SolPID_DadosCliente.class);
            intent.putExtra("Solicitacao", solicitacaoPid);
            startActivityForResult(intent, 0);

            // Fecha a Activity atual que contém os Fragments
            requireActivity().finish();
        });
    }
}