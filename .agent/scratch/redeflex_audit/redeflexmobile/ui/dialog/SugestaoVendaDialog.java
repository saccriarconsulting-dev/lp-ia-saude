package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.DevolucaoAdapter;
import com.axys.redeflexmobile.shared.adapters.SugestaoVendaAdapter;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

public class SugestaoVendaDialog extends DialogFragment {
    ArrayList<SugestaoVenda> listaSugestoesVenda;
    private Cliente cliente;
    SugestaoVendaAdapter mAdapter;

    // Objetos
    private ListView listaRuptura;
    private LinearLayout layoutSugestaoVenda;
    private Button btnok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_sugestao_venda, container);

        try {
            setCancelable(false);
            String idCliente = getArguments().getString(Config.CodigoCliente);
            cliente = new DBCliente(view.getContext()).getById(idCliente);
            criaObjetos(view);
            criaEventos();

            if (cliente != null)
                carregaDados(view);

        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), "Erro: " + ex.getMessage(), false);
        }
        return view;
    }

    private void criaObjetos(View view) {
        layoutSugestaoVenda = (LinearLayout) view.findViewById(R.id.layoutPrazo);
        btnok = (Button) view.findViewById(R.id.DialogSugestaoVenda_btnOk);
        listaRuptura = (ListView)view.findViewById(R.id.DialogSugestaoVenda_lista);
    }

    private void criaEventos() {
        try {
            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
        }
    }

    private void carregaDados(View view) {
        try {
            listaSugestoesVenda = new DBSugestaoVenda(view.getContext()).getSugestaoRuptura(cliente.getId());
            mAdapter = new SugestaoVendaAdapter(view.getContext(), R.layout.item_sugestao_venda, listaSugestoesVenda);
            listaRuptura.setAdapter(mAdapter);
        } catch (Exception ex) {
            Alerta alerta = new Alerta(view.getContext(), "Erro", ex.getMessage());
            alerta.show();
        }
    }
}
