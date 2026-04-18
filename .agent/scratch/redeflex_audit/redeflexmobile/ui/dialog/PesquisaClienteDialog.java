package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.RotaAdapter;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 09/06/2017.
 */

public class PesquisaClienteDialog extends DialogFragment {

    private ArrayList<Rota> listaRota;
    private ListView listCliente;
    public boolean isValidaOrdemRota;
    public PesquisaClienteDialog.OnCompleteListener myCompleteListenerRota;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(R.layout.dialog_pesquisa_cliente, container);

        try {
            listCliente = (ListView) view.findViewById(R.id.listCliente);
            EditText txtPesquisa = (EditText) view.findViewById(R.id.txtPesquisa);
            txtPesquisa.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() > 2) {
                        listaRota = new DBRota(getContext()).getRotasByDiaSemana(
                                0, s.toString().trim(), 0, 0);
                    } else {
                        listaRota = new ArrayList<>();
                    }

                    RotaAdapter mAdapter = new RotaAdapter(getContext(), R.layout.item_rota, listaRota);
                    listCliente.setEmptyView(view.findViewById(R.id.empty));
                    listCliente.setAdapter(mAdapter);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listaRota.size() > 0) {
                        if (myCompleteListenerRota != null) {
                            myCompleteListenerRota.onCompletePesquisaCliente(listaRota.get(position));
                            dismiss();
                        }
                    }
                }
            });
        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), "Erro: " + ex.getMessage(), true);
        }

        return view;
    }

    public interface OnCompleteListener {
        void onCompletePesquisaCliente(Rota cliente);
    }
}