package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ProjetoTradeAdapter;
import com.axys.redeflexmobile.shared.bd.DBProjetoTrade;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 05/10/2016.
 */

public class ProjetoTradeDialog extends DialogFragment {
    public static ArrayList<ProjetoTrade> listProjetos;
    Spinner ddlProjeto;
    Button btnConfirmar, btnCancelar;
    public ProjetoTrade retorno;
    private ProjetoTradeAdapter mAdapter;

    public interface OnCompleteListener {
        void onComplete(ProjetoTrade retorno);
    }

    public OnCompleteListener myCompleteListener;

    public ProjetoTradeDialog() {
        if (ProjetoTradeDialog.listProjetos == null)
            ProjetoTradeDialog.listProjetos = new DBProjetoTrade(getContext()).getProjetos();
        this.retorno = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_projeto_trade, container);

        try {
            setCancelable(false);
            criarObjeto(view);
            alimentaDados();
            criaEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
        }

        return view;
    }

    private void criarObjeto(View view) {
        ddlProjeto = (Spinner) view.findViewById(R.id.ddlProjetoTrade);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnConfirmar = (Button) view.findViewById(R.id.btnOk);
        mAdapter = new ProjetoTradeAdapter(getContext(), android.R.layout.simple_spinner_item, ProjetoTradeDialog.listProjetos);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void alimentaDados() {
        ddlProjeto.setAdapter(mAdapter);
    }

    private void criaEventos() {
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retorno == null) {
                    Utilidades.retornaMensagem(getActivity(), "Nenhum projeto selecionado, Verifique!", false);
                    return;
                }

                if (myCompleteListener != null) {
                    myCompleteListener.onComplete(retorno);
                }
                dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ddlProjeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                retorno = mAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}