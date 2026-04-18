package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.FormaPagamentoAdapter;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.models.FormaPagamentoVenc;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by joao.viana on 19/09/2016.
 */
public class FormaPagamentoDialog extends DialogFragment {
    ArrayList<FormaPagamento> listaFormaPagamentos;
    private String[] FechamentoFatura;

    public interface OnCompleteListener {
        void onComplete(FormaPagamentoVenc retorno);
    }

    FormaPagamento formapagamento;
    FormaPagamentoVenc retorno;

    Spinner cbmotivo, cbVencimento;
    Button btncanc, btnok;
    private FormaPagamentoAdapter adapter;
    public OnCompleteListener myCompleteListener;
    private LinearLayout layoutPrazo;
    private EditText txtVencimento;
    private Cliente cliente;

    public FormaPagamentoDialog() {
        formapagamento = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_forma_pagamento, container);

        try {
            setCancelable(false);
            String idCliente = getArguments().getString(Config.CodigoCliente);
            Boolean podeAvista = getArguments().getBoolean("VendeAvista");

            cliente = new DBCliente(view.getContext()).getById(idCliente);
            criaObjetos(view);
            criaEventos();
            String fechamento = null;
            if (cliente != null)
                fechamento = cliente.getFechamentoFatura();
            if (!Util_IO.isNullOrEmpty(fechamento))
                FechamentoFatura = fechamento.split(";");
            else
                FechamentoFatura = null;

            // Carrega Dados Forma de Pagamento Caso possa Vender Avista
            if (podeAvista)
                listaFormaPagamentos = new DBFormaPagamento(getContext()).getFormaPagamentos_Avista();
            else
                listaFormaPagamentos = new DBFormaPagamento(getContext()).getFormaPagamentos();

            carregaDados();
        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), "Erro: " + ex.getMessage(), false);
        }
        return view;
    }

    private void criaObjetos(View view) {
        layoutPrazo = (LinearLayout) view.findViewById(R.id.layoutPrazo);
        btncanc = (Button) view.findViewById(R.id.btnCancelar);
        btnok = (Button) view.findViewById(R.id.btnOk);
        txtVencimento = (EditText) view.findViewById(R.id.txtVencimento);
        cbmotivo = (Spinner) view.findViewById(R.id.spinnerFormaPagamento);
        cbVencimento = (Spinner) view.findViewById(R.id.cbVencimento);
    }

    private void carregaDados() {
        try {
            adapter = new FormaPagamentoAdapter(getContext(), android.R.layout.simple_spinner_item, listaFormaPagamentos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cbmotivo.setAdapter(adapter);
            if (FechamentoFatura != null) {
                txtVencimento.setVisibility(View.GONE);
                ArrayList<String> list = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                int iCorte = 15;
                if (cliente != null && cliente.getDiasCortes() > 0)
                    iCorte = cliente.getDiasCortes();
                for (int i = 0; i < iCorte; i++) {
                    calendar.add(Calendar.DATE, 1);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    try {
                        for (String obj : FechamentoFatura) {
                            if (dayOfWeek == Integer.parseInt(obj))
                                list.add(Util_IO.dateTimeToString(calendar.getTime(), "dd/MM/yyyy"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                StringAdapter stringAdapter = new StringAdapter(getContext(), R.layout.custom_spinner_title_bar, list);
                cbVencimento.setAdapter(stringAdapter);
                if (list.size() == 0) {
                    txtVencimento.setVisibility(View.VISIBLE);
                    cbVencimento.setVisibility(View.GONE);
                    FechamentoFatura = null;
                }
            } else
                cbVencimento.setVisibility(View.GONE);
        } catch (Exception ex) {
            Utilidades.retornaMensagem(getContext(), "Erro: " + ex.getMessage(), false);
        }
    }

    private void criaEventos() {
        try {
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (formapagamento == null) {
                        Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Selecione a forma de pagamento!");
                        alerta.show();
                        return;
                    }

                    retorno = new FormaPagamentoVenc();
                    if (formapagamento.getId() == 2) {
                        Date dataVencimento = null;
                        if (FechamentoFatura != null)
                            dataVencimento = Util_IO.stringToDate(cbVencimento.getSelectedItem().toString());
                        else {
                            String data = txtVencimento.getText().toString().trim();
                            if (data.length() <= 0) {
                                txtVencimento.requestFocus();
                                Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Informar a data de vencimento!");
                                alerta.show();
                                return;
                            }

                            if (!Validacoes.validaData(data)) {
                                txtVencimento.requestFocus();
                                Mensagens.dataDeVencimentoInvalida(getContext());
                                return;
                            }

                            int iCorte = 15;
                            if (cliente != null && cliente.getDiasCortes() > 0)
                                iCorte = cliente.getDiasCortes();

                            dataVencimento = Util_IO.stringToDate(data);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            Date data_atual = cal.getTime();
                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            cal.add(Calendar.DAY_OF_YEAR, iCorte);
                            Date data_limite = cal.getTime();

                            if (dataVencimento == null) {
                                txtVencimento.requestFocus();
                                Mensagens.dataDeVencimentoInvalida(getContext());
                                return;
                            }

                            if (fmt.format(dataVencimento).equals(fmt.format(data_atual))) {
                                txtVencimento.requestFocus();
                                Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Data de vencimento deve ser maior que data atual!");
                                alerta.show();
                                return;
                            } else {
                                if (dataVencimento.before(data_atual)) {
                                    txtVencimento.requestFocus();
                                    Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Data de vencimento não pode ser menor que data atual!");
                                    alerta.show();
                                    return;
                                }
                            }

                            if (dataVencimento.after(data_limite)) {
                                txtVencimento.requestFocus();
                                Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Data de vencimento superior à " + iCorte + " dias, Verifique!");
                                alerta.show();
                                return;
                            }

                        }
                        retorno.setDatavencimento(dataVencimento);
                    }

                    retorno.setFormapgto(formapagamento);

                    if (myCompleteListener != null) {
                        myCompleteListener.onComplete(retorno);
                    }
                    dismiss();
                }
            });

            txtVencimento.addTextChangedListener(Util_IO.Mask.insert("##/##/####", txtVencimento));

            cbmotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    formapagamento = adapter.getItem(position);

                    if (formapagamento == null) {
                        Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Selecione a forma de pagamento!");
                        alerta.show();
                        return;
                    }

                    if (String.valueOf(formapagamento.getId()).equals("2")) {
                        layoutPrazo.setVisibility(View.VISIBLE);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                        Date data_atual = cal.getTime();

                        txtVencimento.setText(Util_IO.dateToStringBr(data_atual));
                    } else {
                        layoutPrazo.setVisibility(View.GONE);
                        txtVencimento.setText("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

            btncanc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
        }
    }
}