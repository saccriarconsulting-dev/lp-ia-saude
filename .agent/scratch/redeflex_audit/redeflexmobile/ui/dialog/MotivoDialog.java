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
import com.axys.redeflexmobile.shared.adapters.MotivoAdapter;
import com.axys.redeflexmobile.shared.bd.DBMotivo;
import com.axys.redeflexmobile.shared.models.Motivo;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 25/09/2017.
 */

public class MotivoDialog extends DialogFragment {
    ArrayList<Motivo> listMotivo;
    Motivo motivo;
    Spinner cbMotivo;
    Button btnCancelar, btnOk;
    MotivoAdapter mAdapter;
    Visita visita;

    public MotivoDialog() {
        listMotivo = new DBMotivo(getContext()).getMotivos();
        motivo = null;
    }

    private void criarObjetos(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        cbMotivo = (Spinner) view.findViewById(R.id.spinnerMotivo);
    }

    private void criarEventos() {
        cbMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                motivo = mAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        btnOk.setOnClickListener((view) -> {
            try {
                if (motivo == null) {
                    Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Motivo não selecionado, Verifique!");
                    alerta.show();
                    return;
                }

                if (visita == null) {
                    Alerta alerta = new Alerta(getContext(), getResources().getString(R.string.app_name), "Visita não inciada, Verifique");
                    alerta.show();
                    return;
                }

                Utilidades.encerrarAtendimento(getActivity(), true, null, null, visita, null, motivo.getId(), null);
                dismiss();
            } catch (Exception ex) {
                Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
            }
        });

        btnCancelar.setOnClickListener((view) -> {
            dismiss();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_motivo, container);

        try {
            setCancelable(false);
            criarObjetos(view);
            visita = Utilidades.getVisita(getContext());
            mAdapter = new MotivoAdapter(getContext(), R.layout.custom_spinner_title_bar, listMotivo);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cbMotivo.setAdapter(mAdapter);
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(getContext(), ex.getMessage(), false);
        }
        return view;
    }
}