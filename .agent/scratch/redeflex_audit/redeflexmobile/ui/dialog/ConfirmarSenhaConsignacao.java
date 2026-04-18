package com.axys.redeflexmobile.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBSenhaCliente;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.SenhaCliente;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ConsignadoAuditagemActivity;

import java.util.ArrayList;

public class ConfirmarSenhaConsignacao extends DialogFragment {
    Button btnCancelar, btnSalvar;
    EditText txtSenha;
    Cliente cliente;

    Consignado consignado;

    BDConsignado bdConsignado;

    public interface OnCompleteListener {
        void onCompleteSenha(boolean senhaok);
    }

    public ConfirmarSenhaDialog.OnCompleteListener myCompleteListenerSenha;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_confirmar_senhaconsignacao, container);

        try {
            setCancelable(false);
            Bundle bundle = getArguments();
            String codigo = bundle.getString(Config.CodigoCliente);
            cliente = new DBCliente(getActivity()).getById(codigo);

            criaObjetos(view);
            criaEventos();

            if (!Util_IO.isNullOrEmpty(bundle.getString("CodigoConsignacao"))) {
                bdConsignado = new BDConsignado(getActivity());
                consignado = bdConsignado.getById(bundle.getString("CodigoConsignacao"));
            }

        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
        return view;
    }

    private void criaObjetos(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnSalvar = (Button) view.findViewById(R.id.btnOk);
        txtSenha = (EditText) view.findViewById(R.id.editSenha);
    }

    private void criaEventos() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String senhadigitada = txtSenha.getText().toString().trim();

                    if (senhadigitada.isEmpty()) {
                        Mensagens.mensagemErro(getActivity(), "Senha não informada, Verifique!", false);
                        return;
                    }

                    boolean bSenhaOk = false, bAguarde = false;
                    if (cliente.getExibirCodigo().equalsIgnoreCase("SGV")) {
                        ArrayList<SenhaCliente> listSenha = new DBSenhaCliente(getActivity()).getSenhasByClienteId(cliente.getId());
                        if (listSenha != null && listSenha.size() > 0) {
                            for (SenhaCliente item : listSenha) {
                                if (item.getSenha().equals(senhadigitada)) {
                                    bSenhaOk = true;
                                    break;
                                }
                            }
                        } else {
                            if (senhadigitada.trim().length() == 5)
                                bSenhaOk = true;
                        }
                    } else {
                        if (Util_IO.isNullOrEmpty(cliente.getSenha()) || cliente.getSenha().equalsIgnoreCase("Aguarde"))
                            bAguarde = true;
                        else
                            bSenhaOk = cliente.getSenha().equals(senhadigitada);
                    }

                    if (bAguarde) {
                        new ClienteSyncTask(getActivity(), 1).execute();
                        Mensagens.mensagemErro(getActivity(), "Aguarde. A senha será enviada via sms para o cliente", false);
                        return;
                    }

                    if (!bSenhaOk) {
                        Mensagens.mensagemErro(getActivity(), "A senha está inválida, Verifique!", false);
                        return;
                    }

                    if (getActivity() instanceof ConsignadoAuditagemActivity) {
                        if (consignado == null) {
                            Utilidades.retornaMensagem(getActivity(), "Consignação não iniciada, inicie o atendimento novamente", true);
                            return;
                        }

                        Visita visita = new DBVisita(getActivity()).getVisitabyId(consignado.getIdVisita());
                        Utilidades.encerrarAtendimentoConsignacao(getActivity(), consignado, visita, 16, null);
                        dismiss();
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    } else {
                        if (myCompleteListenerSenha != null) {
                            myCompleteListenerSenha.onCompleteSenha(true);
                            dismiss();
                        }
                    }
                } catch (Exception ex) {
                    Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
                }
            }
        });
    }
}
