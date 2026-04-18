package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.models.TipoDocumento;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 08/09/2016.
 */
public class ClienteDocumentoDialog extends DialogFragment {

    public static final int PESSOA_FISICA = 0;
    public static final int PESSOA_JURIDICA = 1;
    public static final int CLIENTE_FISICO = 0;
    public static final int CLIENTE_ELETRONICO = 1;
    public static final int CLIENTE_ADIQUIRENCIA = 2;

    private int tipoCliente, tipoPessoa, tipoCarregamento;
    private Button btnOk, btnCancelar;
    private Spinner spinnerTipo;

    public OnCompleteListener myCompleteListenerTpDoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_tipo_documento, container);

        try {

            tipoCliente = getArguments().getInt("tipoCliente") == CLIENTE_FISICO
                    ? CLIENTE_FISICO : getArguments().getInt("tipoCliente") == CLIENTE_ELETRONICO
                    ? CLIENTE_ELETRONICO : CLIENTE_ADIQUIRENCIA;
            tipoPessoa = getArguments().getInt("tipoPessoa") == PESSOA_FISICA
                    ? PESSOA_FISICA : PESSOA_JURIDICA;
            tipoCarregamento = getArguments().getInt("tipoCarregamento");

            setCancelable(false);
            criarObjetos(view);
            criarEventos();
            alimentarDados(view);

        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), ex.getMessage(), true);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void criarObjetos(View view) {
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        spinnerTipo = (Spinner) view.findViewById(R.id.spinnerTipo);
    }

    private void alimentarDados(View view) {
        ArrayList<String> arrTipo = new ArrayList<>();

        arrTipo.add("Fachada");

        if (tipoCliente != CLIENTE_ADIQUIRENCIA) {

            if (tipoPessoa == PESSOA_FISICA) {

                arrTipo.add("CPF");
                arrTipo.add("Frente do RG");
                arrTipo.add("Verso do RG");
                arrTipo.add("CNH");

            } else {

                arrTipo.add("Cartão CNPJ");
                arrTipo.add("Inscrição Estadual");
                arrTipo.add("CPF do proprietario");
                arrTipo.add("Frente do RG");
                arrTipo.add("Verso do RG");
                arrTipo.add("CNH do proprietario");
                arrTipo.add("MEI");
            }

            arrTipo.add("Comprovante de Endereço");
            arrTipo.add("Termo de adesao da POS");
            arrTipo.add("Declaração de Endereço");

        } else {

            if (tipoPessoa == PESSOA_JURIDICA) {
                arrTipo.add("Contrato Aluguel");
            }

            arrTipo.add("Comprovante de Endereço");
            arrTipo.add("Identificação Bancária");
            arrTipo.add("Ficha Proposta");
        }

        StringAdapter mAdapter = new StringAdapter(view.getContext(), R.layout.custom_spinner_title_bar, arrTipo);
        spinnerTipo.setAdapter(mAdapter);
    }

    private void criarEventos() {
        btnCancelar.setOnClickListener(v -> dismiss());

        btnOk.setOnClickListener(v -> {
            if (myCompleteListenerTpDoc != null) {
                TipoDocumento tipoDocumento = new TipoDocumento();
                tipoDocumento.setDescricao(spinnerTipo.getSelectedItem().toString());
                tipoDocumento.setTipo(tipoCarregamento);

                myCompleteListenerTpDoc.onComplete(tipoDocumento);
            }
            dismiss();
        });
    }

    public interface OnCompleteListener {
        void onComplete(TipoDocumento documento);
    }
}