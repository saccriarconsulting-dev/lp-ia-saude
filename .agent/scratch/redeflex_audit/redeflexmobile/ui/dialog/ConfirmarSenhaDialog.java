package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.bd.DBSenhaCliente;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;
import com.axys.redeflexmobile.shared.models.SenhaCliente;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.dialog.cliente.ClienteInfoDialog;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.FinalizarAtendActivity;

import java.util.ArrayList;

import dagger.multibindings.ElementsIntoSet;

/**
 * Created by joao.viana on 26/07/2016.
 */
public class ConfirmarSenhaDialog extends DialogFragment {
    Button btnCancelar, btnSalvar;
    EditText txtSenha;
    Cliente cliente;
    Venda venda;
    DBVenda dbVenda;
    TextView tvEsqueciSenha, tvMensagem;

    public interface OnCompleteListener {
        void onCompleteSenha(boolean senhaok);
    }

    public OnCompleteListener myCompleteListenerSenha;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_confirmar_senha, container);

        try {
            setCancelable(false);
            Bundle bundle = getArguments();
            String codigo = bundle.getString(Config.CodigoCliente);
            cliente = new DBCliente(getActivity()).getById(codigo);
            if (!Util_IO.isNullOrEmpty(bundle.getString(Config.CodigoVenda))) {
                dbVenda = new DBVenda(getActivity());
                venda = dbVenda.getVendabyId(Integer.parseInt(bundle.getString(Config.CodigoVenda)));
            }
            criaObjetos(view);
            criaEventos();

            if (venda != null)
                tvMensagem.setText("PARA GERAR COBRANÇA DE " + Util_IO.formatDoubleToDecimalNonDivider(venda.getValorTotal()) + "\nDIGITE SUA SENHA");
            else
                tvMensagem.setText("PARA EFETUAR A VENDA");

        } catch (Exception ex) {
            Mensagens.mensagemErro(getActivity(), ex.getMessage(), false);
        }
        return view;
    }

    private void criaObjetos(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnSalvar = (Button) view.findViewById(R.id.btnOk);
        txtSenha = (EditText) view.findViewById(R.id.editSenha);
        tvEsqueciSenha = (TextView)view.findViewById(R.id.tvEsqueciSenha);
        tvMensagem = (TextView)view.findViewById(R.id.tvMensagem);
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

                    if (getActivity() instanceof FinalizarAtendActivity) {
                        if (venda == null) {
                            Utilidades.retornaMensagem(getActivity(), "Venda não iniciada, inicie o atendimento novamente", true);
                            return;
                        }

                        Visita visita = new DBVisita(getActivity()).getVisitabyId(venda.getIdVisita());
                        ArrayList<ItemVenda> listaItens = dbVenda.getItensVendabyIdVenda(venda.getId());
                        LimiteCliente limiteCliente = new DBLimite(getActivity()).getByIdCliente(cliente.getId());
                        Utilidades.encerrarAtendimento(getActivity(), false, venda, limiteCliente, visita, listaItens, 0, null);
                        dismiss();
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

        tvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReenviaSenhaCliente senhaCliente = new ReenviaSenhaCliente();
                senhaCliente.setIdVendedor(new DBColaborador(getActivity()).get().getId());

                //senhaCliente.setPrecisao(cliente.getpre);
                //senhaCliente.setLatitude(gpsTracker.getLatitude());
                //senhaCliente.setLongitude(gpsTracker.getLongitude());

                senhaCliente.setIdCliente(Integer.parseInt(cliente.getId()));
                String jsonSenha = Utilidades.getGsonInstance().toJson(senhaCliente);
                String json = Utilidades.getGsonInstance().toJson(cliente);

                ClienteInfoDialog.newInstance(json, jsonSenha)
                        .show(((FragmentActivity) getActivity()).getSupportFragmentManager(), ClienteInfoDialog.class.getSimpleName());
            }
        });
    }
}