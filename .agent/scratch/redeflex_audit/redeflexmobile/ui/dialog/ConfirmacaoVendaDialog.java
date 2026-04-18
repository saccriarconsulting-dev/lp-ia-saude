package com.axys.redeflexmobile.ui.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItensConfirVendaAdapter;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.models.ConfirmacaoVenda;
import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConfirmacaoVendaDialog extends DialogFragment {
    Button btnCancelar, btnConfirmar;
    ListView listaProdutos;
    TextView txtCliente, txtValorTotal, txtDataCobranca, txtFormaPagamento;
    ItensConfirVendaAdapter mAdapter;

    ArrayList<ItemVenda> lista;
    DBVenda dbVenda;
    Visita visita;
    DBVisita dbVisita;
    Venda venda;
    int idvisita;
    ConfirmacaoVenda confirmacaoVenda;
    FormaPagamento formaPagamento;

    public interface OnCompleteListener {
        void onComplete(ConfirmacaoVenda venda);
    }

    public OnCompleteListener myCompleteListenerVenda;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_confirmacao_venda, container);

        Utilidades.getDataServidorESalvaBanco(view.getContext());

        try {
            setCancelable(false);
            idvisita = getArguments().getInt("visita");
            criaObjetos(view);
            criaEventos();
            String cliente = visita.getCodigoExibirCliente() + " - " + visita.getNomeFantasia();
            txtCliente.setText(cliente);
            listaProdutos.setAdapter(mAdapter);
            txtValorTotal.setText(retornaValorTotal());
            if (formaPagamento != null)
                txtFormaPagamento.setText(formaPagamento.getDescricao());
            txtDataCobranca.setText(Util_IO.dateToStringBr(venda.getDataCobranca()));
        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), ex.getMessage(), false);
        }

        return view;
    }

    private void criaObjetos(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnConfirmar = (Button) view.findViewById(R.id.btnOk);
        listaProdutos = (ListView) view.findViewById(R.id.listaItens);
        txtCliente = (TextView) view.findViewById(R.id.txtClienteDialog);
        txtValorTotal = (TextView) view.findViewById(R.id.txtValor);
        try {
            dbVisita = new DBVisita(view.getContext());
            dbVenda = new DBVenda(view.getContext());
            visita = dbVisita.getVisitabyId(idvisita);
            venda = dbVenda.getVendabyIdVisita(visita.getId());
            if (visita.getIdProjetoTrade().equals("0"))
                lista = dbVenda.getItensVendabyIdVenda(venda.getId());
            else
                lista = dbVenda.getItensVendabyIdVendaTrade(venda.getId());
            mAdapter = new ItensConfirVendaAdapter(view.getContext(), R.layout.item_lista_confir_venda, lista);
            txtFormaPagamento = (TextView) view.findViewById(R.id.txtFormaPagamento);
            formaPagamento = new DBFormaPagamento(view.getContext()).getFormaPagamentoById(venda.getIdFormaPagamento());
            txtDataCobranca = (TextView) view.findViewById(R.id.txtDataCobranca);
        } catch (Exception ex) {
            Utilidades.retornaMensagem(view.getContext(), ex.getMessage(), false);
        }
    }

    private void criaEventos() {
        btnCancelar.setOnClickListener((view) -> {
            dismiss();
        });

        btnConfirmar.setOnClickListener((view) -> {
            try {
                if (!Validacoes.validacaoDataAparelho(view.getContext())) {
                    return;
                }
                confirmacaoVenda = new ConfirmacaoVenda();
                confirmacaoVenda.setStatus(true);

                if (myCompleteListenerVenda != null) {
                    myCompleteListenerVenda.onComplete(confirmacaoVenda);
                }
                dismiss();
            } catch (Exception ex) {
                Utilidades.retornaMensagem(view.getContext(), ex.getMessage(), false);
            }
        });
    }

    private String retornaValorTotal() {
        double valor = 0.0;
        DecimalFormat precision = new DecimalFormat("0.00");
        if (lista != null && lista.size() > 0) {
            for (ItemVenda item : lista) {
                double vlunit = Double.valueOf(precision.format(item.getValorUN()).replace(".", "").replace(",", "."));
                valor += (vlunit * item.getQtde());
            }
        }

        return "R$ " + precision.format(valor);
    }
}