package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

import static com.axys.redeflexmobile.shared.util.Util_IO.formatDoubleToDecimalNonDivider;

/**
 * Created by joao.viana on 17/10/2017.
 */

public class ItensVendaAdapter extends RecyclerView.Adapter<ItensVendaAdapter.ViewItensVendaHolder> {
    private Context mContext;
    private ArrayList<ItemVendaCombo> mLista;

    public ItensVendaAdapter(ArrayList<ItemVendaCombo> pLista, Context pContext) {
        mContext = pContext;
        mLista = pLista;
    }

    @Override
    public ViewItensVendaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_produto_venda, parent, false);
        return new ViewItensVendaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewItensVendaHolder holder, int position) {
        final ItemVendaCombo itemVenda = mLista.get(position);
        holder.txtProduto.setText(itemVenda.getIdProduto() + " - " + itemVenda.getNomeProduto());
        holder.txtQtd.setText("Qtd: " + String.valueOf(itemVenda.getQtde()));
        holder.txtValor.setText("Valor " + (formatDoubleToDecimalNonDivider(itemVenda.getValorUN() * itemVenda.getQtde())));
        holder.txtUnit.setText("Valor Unit. " + Util_IO.formatDoubleToDecimalNonDivider(itemVenda.getValorUN()));
        holder.btnExcluir.setVisibility(View.GONE);
    }

    private void removeAt(int position) {
        mLista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mLista.size());
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewItensVendaHolder extends RecyclerView.ViewHolder {
        TextView txtProduto, txtQtd, txtValor, txtUnit;
        ImageButton btnExcluir;

        ViewItensVendaHolder(View itemView) {
            super(itemView);

            txtProduto = (TextView) itemView.findViewById(R.id.txtProduto);
            txtQtd = (TextView) itemView.findViewById(R.id.txtQuantidade);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);
            txtUnit = (TextView) itemView.findViewById(R.id.txtUnit);
            btnExcluir = (ImageButton) itemView.findViewById(R.id.btnExcluir);
        }
    }
}