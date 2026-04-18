package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.ui.component.InputText;

import java.util.ArrayList;

/**
 * Created by Jonathan Feitosa on 22/03/2022
 */

public class ListaClientesAdapter extends RecyclerView.Adapter<ListaClientesAdapter.ViewListaClientesHolder> {
    private Context mContext;
    private ArrayList<Cliente> mLista;
    private ListaClientesCallback mCallback;
    DBCliente dbCliente;
    Cliente cliente02;

    public ListaClientesAdapter(Context pContext, ArrayList<Cliente> pLista, ListaClientesCallback callback) {
        mContext = pContext;
        mLista = pLista;
        mCallback = callback;
    }

    @Override
    public ViewListaClientesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cliente_sol_preco_dif, parent, false);
        return new ViewListaClientesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewListaClientesHolder holder, int position) {
        Cliente cliente = mLista.get(position);

        // Visibilidade Botão ocultado primeiro
        holder.botaoExcluir.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);

        holder.codSGV.setText(cliente.getCodigoSGV());
        holder.clienteInput.setText(cliente.getNomeFantasia());
        holder.botaoExcluir.setOnClickListener(view -> {
            this.mCallback.excluirCliente(position);
        });

        try {

            dbCliente = new DBCliente(mContext);
            holder.codSGV.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    holder.clienteInput.setText("");
                    cliente02 = dbCliente.getById(charSequence.toString());
                    if (cliente02 != null) {
                        holder.clienteInput.setText("" + cliente02.getNomeFantasia());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } catch (Exception ex) {
            Mensagens.mensagemErro(mContext, ex.getMessage(), true);
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewListaClientesHolder extends RecyclerView.ViewHolder {

        InputText codSGV;
        InputText clienteInput;
        Button botaoExcluir;

        ViewListaClientesHolder(View itemView) {
            super(itemView);
            codSGV = itemView.findViewById(R.id.codCliSgv_spd);
            clienteInput = itemView.findViewById(R.id.cliente_spd);
            botaoExcluir = itemView.findViewById(R.id.btn_del_spd);
        }
    }

    public interface ListaClientesCallback {
        void excluirCliente(int position);
    }
}


