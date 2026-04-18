package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CNPJ;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CPF;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.enums.EnumTipoCliente;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.viewmodel.SolicitacaoPidViewModel;
import com.axys.redeflexmobile.shared.services.RegisterService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.RetrofitClient;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.axys.redeflexmobile.shared.models.viewmodel.SolicitacaoPidViewModel;

public class Fragment_SimuladorPidTela1 extends Fragment {

    CustomSpinner spTipoCliente, spTipoPessoa, spnMCC;
    CustomEditText edtCpfCNPJ, edtCodigoSGV, edtRazaoSocial, edtNomeFantasia;
    Button btnAvancar;
    List<TaxaMdr> taxas;
    private SolicitacaoPidViewModel solicitacaoPidViewModel;
    private SolicitacaoPid solicitacaoPid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__simulador_pid_tela1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spTipoCliente = view.findViewById(R.id.llsimulador1_spnTipoCliente);
        spTipoPessoa = view.findViewById(R.id.llsimulador1_spnTipoPessoa);
        spnMCC = view.findViewById(R.id.llsimulador1_spnMCC);
        edtCpfCNPJ = view.findViewById(R.id.llsimulador1_edtCpfCNPJ);
        edtCodigoSGV = view.findViewById(R.id.llsimulador1_edtCodigoSGV);
        edtRazaoSocial = view.findViewById(R.id.llsimulador1_edtRazaoSocial);
        edtNomeFantasia = view.findViewById(R.id.llsimulador1_edtNomeFantasia);
        btnAvancar = view.findViewById(R.id.llsimulador1_btnAvancar);

        iniciarTipoCliente(new ArrayList<>(EnumTipoCliente.getList()));
        iniciarTipoPessoa(new ArrayList<>(EnumRegisterPersonType.getList()));

        // Desabilita Campos
        HabilitaCampos(false);

        criarEventos();

        solicitacaoPidViewModel = new ViewModelProvider(requireActivity()).get(SolicitacaoPidViewModel.class);

        // Observa o LiveData do ViewModel
        solicitacaoPidViewModel.getSolicitacaoPid().observe(getViewLifecycleOwner(), solicitacao -> {
            if (solicitacao != null) {
                // Atualizar os campos da tela com os valores do objeto `solicitacao`
                edtCodigoSGV.setText(solicitacao.getCodigoSGV());
                edtRazaoSocial.setText(solicitacao.getRazaoSocial());
                edtNomeFantasia.setText(solicitacao.getNomeFantasia());

                // Tipo de Cliente
                EnumTipoCliente tipoCliente = EnumTipoCliente.getEnumByDescription(solicitacao.getTipoCliente());
                spTipoCliente.doSelect(tipoCliente);

                // Tipo de Pessoa
                EnumRegisterPersonType tipoPessoa = EnumRegisterPersonType.getEnumByCharValue(solicitacao.getTipoPessoa());
                setarDocumentoCliente(tipoPessoa);
                spTipoPessoa.doSelect(tipoPessoa);
                edtCpfCNPJ.setText(solicitacao.getCpfCnpj());

                // MCC
                spnMCC.doSelectWithCallback(solicitacao.getMCCPrincipal());
            }
        });
    }

    private void HabilitaCampos(boolean pHabilita) {
        edtCodigoSGV.setEnabled(pHabilita);
        spnMCC.setEnabled(pHabilita);
        edtRazaoSocial.setEnabled(pHabilita);
        edtNomeFantasia.setEnabled(pHabilita);
    }

    private void setarDocumentoCliente(ICustomSpinnerDialogModel item) {
        if (item.getIdValue().equals(EnumRegisterPersonType.PHYSICAL.getIdValue())) {
            edtCpfCNPJ.setLabel("CPF");
            edtCpfCNPJ.setMask(CPF);
            edtCpfCNPJ.clearValue();
        } else {
            edtCpfCNPJ.setLabel("CNPJ");
            edtCpfCNPJ.setMask(CNPJ);
            edtCpfCNPJ.clearValue();
        }

        taxas = new DBTaxaMdr(requireActivity()).getAllMccByPersonType(item.getIdValue());
        iniciarMcc(new ArrayList<>(taxas));
    }

    private void criarEventos() {
        spTipoPessoa.setCallback(item -> {
            setarDocumentoCliente(item);
            HabilitaCampos(false);
        });

        edtCpfCNPJ.setAfterTextListener(this::CpfCnpjListener);

        btnAvancar.setOnClickListener(v -> {
            // Validações campos preenchidos
            if (spTipoCliente.getSelectedItemId() == null) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Selecione o Tipo de Cliente");
                alerta.show();
                return;
            }

            if (spTipoPessoa.getSelectedItemId() == null) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Selecione o Tipo de Pessoa");
                alerta.show();
                return;
            }

            if (spnMCC.getSelectedItemId() == null || spnMCC.getSelectedItemId() == 0) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Selecione o MCC do Cliente");
                alerta.show();
                return;
            }

            if (Util_IO.isNullOrEmpty(edtCpfCNPJ.getText())) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Informe o Cpf/Cnpj do Cliente");
                alerta.show();
                return;
            }

            if (Util_IO.isNullOrEmpty(edtRazaoSocial.getText())) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Informe a Razão Social do Cliente");
                alerta.show();
                return;
            }

            if (Util_IO.isNullOrEmpty(edtNomeFantasia.getText())) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Informe o Nome Fantasia do Cliente");
                alerta.show();
                return;
            }

            // Cria um novo objeto se for null
            solicitacaoPid = solicitacaoPidViewModel.getSolicitacaoPid().getValue();
            if (solicitacaoPid == null)
                solicitacaoPid = new SolicitacaoPid();

            solicitacaoPid.setTipoCliente(spTipoCliente.getSelectedItem().getDescriptionValue());
            solicitacaoPid.setTipoPessoa(spTipoPessoa.getSelectedItem().getIdValue() == PHYSICAL.getIdValue() ? "F" : "J");
            solicitacaoPid.setMCCPrincipal(spnMCC.getSelectedItem().getIdValue());
            solicitacaoPid.setCodigoSGV(edtCodigoSGV.getText());
            solicitacaoPid.setCpfCnpj(StringUtils.returnOnlyNumbers(edtCpfCNPJ.getText()));
            solicitacaoPid.setRazaoSocial(edtRazaoSocial.getText());
            solicitacaoPid.setNomeFantasia(edtNomeFantasia.getText());
            solicitacaoPidViewModel.setSolicitacaoPid(solicitacaoPid);

            // Vai para a próxima tela
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.simuladorpid_fragment_container, new Fragment_SimuladorPidTela2())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void CpfCnpjListener(String text) {
        if (edtCpfCNPJ.isNotFocused()) {
            return;
        }

        text = StringUtils.returnOnlyNumbers(text);
        // Busca Cliente por CPF
        if (text.length() == StringUtils.CPF_LENGTH && StringUtils.isCpfValid(text)) {
            GetCliente(text);
        }
        // Busca Cliente por CNPJ
        else if (text.length() == StringUtils.CNPJ_LENGTH && StringUtils.isCnpjValid(text)) {
            GetCliente(text);
        }
    }

    private void GetCliente(String pCpfCnpj) {
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
                        if (consulta.getMcc() != null) {
                            spnMCC.setEnabled(false);
                            spnMCC.doSelectWithCallback(Integer.valueOf(consulta.getMcc()));
                        }
                    } else {
                        // So consulta dados da receita para CNPJ
                        if (pCpfCnpj.length() > 11) {
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
                                            if (consulta.getMcc() != null) {
                                                spnMCC.setEnabled(false);
                                                spnMCC.doSelectWithCallback(Integer.valueOf(consulta.getMcc()));
                                            }
                                        } else {
                                            // Habilita os campos para serem digitados quando não encontrado dados do Cliente
                                            HabilitaCampos(true);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ConsultaReceita> call, Throwable throwable) {
                                    Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Erro ao Consultar Dados do Cliente na Receita");
                                    alerta.show();
                                }
                            });
                        } else {
                            // Habilita os campos para serem digitados quando não encontrado dados do Cliente
                            HabilitaCampos(true);
                        }
                    }
                } else {
                    Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Erro ao Consultar Dados do Cliente");
                    alerta.show();
                }
            }

            @Override
            public void onFailure(Call<ConsultaReceita> call, Throwable throwable) {
                Alerta alerta = new Alerta(requireActivity(), "Atenção!", "Erro ao Consultar Dados do Cliente");
                alerta.show();
            }
        });
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
}