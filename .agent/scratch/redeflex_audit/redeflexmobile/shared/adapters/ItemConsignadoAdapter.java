package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ConsignadoBipagemActivity;

import java.util.ArrayList;

public class ItemConsignadoAdapter extends RecyclerView.Adapter<ItemConsignadoAdapter.ViewItemConsignadoHolder> {

    private Context mContext;
    private ArrayList<ConsignadoItem> mLista;
    private int mOperacao;

    public ItemConsignadoAdapter(ArrayList<ConsignadoItem> pLista, Context pContext, int pOperacao) {
        mContext = pContext;
        mLista = pLista;
        mOperacao = pOperacao;
    }

    @Override
    public ViewItemConsignadoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_consignado, parent, false);
        return new ViewItemConsignadoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewItemConsignadoHolder holder, int position) {
        final ConsignadoItem consignadoItem = mLista.get(position);
        holder.txtProduto.setText(consignadoItem.getIdProduto() + " - " + consignadoItem.getNomeProduto());
        holder.txtQtd.setText("QTD. VENDIDA");
        holder.txtQtdConsignado.setText("Qtd. Consignada: " + String.valueOf(consignadoItem.getQtd()));
        holder.txtQtdVendido.setText(String.valueOf(consignadoItem.getQtdVendido()));
        holder.txtQtdAuditado.setText("Auditados: " + String.valueOf(consignadoItem.getQtdAuditado()));
        holder.txtValorUnit.setText("Valor Unit: " + Util_IO.formatDoubleToDecimalNonDivider(consignadoItem.getValorUnit()));
        holder.txtQtdDevolvido.setText("Devolvidos: " + String.valueOf(consignadoItem.getQtdDevolvido()));
        holder.txtValorTotItem.setText("Total Item: " + Util_IO.formatDoubleToDecimalNonDivider(consignadoItem.getValorTotalItem()));

        if (mOperacao == 1)
            holder.ivCodigoBarra.setVisibility(View.VISIBLE);
        else
            holder.ivCodigoBarra.setVisibility(View.INVISIBLE);

        holder.ivCodigoBarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Config.CodigoVisita, 1);
                bundle.putString(Config.CodigoProduto, consignadoItem.getIdProduto());
                bundle.putString(Config.CODIGO_ITEM_VENDA, String.valueOf(consignadoItem.getId()));
                bundle.putInt(Config.QTD_ITEM_VENDA, consignadoItem.getQtd());
                bundle.putInt(Config.QTD_ITEM_LIDO_VENDA, 0);
                bundle.putBoolean(Config.PRODUTO_COMBO, false);
                bundle.putString(Config.PRODUTO_COMBO_ID, "");
                bundle.putString("Operacao", "Auditagem");
                Utilidades.openNewActivityForResult(
                        mContext,
                        ConsignadoBipagemActivity.class,
                        RequestCode.ProdutoVenda,
                        bundle
                );
            }
        });
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

    protected class ViewItemConsignadoHolder extends RecyclerView.ViewHolder {
        TextView txtProduto, txtQtd, txtQtdConsignado, txtQtdVendido, txtQtdAuditado, txtValorUnit, txtQtdDevolvido, txtValorTotItem;
        ImageView ivCodigoBarra;

        ViewItemConsignadoHolder(View itemView) {
            super(itemView);
            txtProduto = (TextView) itemView.findViewById(R.id.consignado_item_tv_produto);
            txtQtd = (TextView) itemView.findViewById(R.id.consignado_item_tv_quantidade);
            txtQtdConsignado = (TextView) itemView.findViewById(R.id.consignado_item_tv_qtdconsignado);
            txtQtdVendido = (TextView) itemView.findViewById(R.id.consignado_item_tv_qtdvendida);
            txtQtdAuditado = (TextView) itemView.findViewById(R.id.consignado_item_tv_qtdauditados);
            txtValorUnit = (TextView) itemView.findViewById(R.id.consignado_item_tv_valorunit);
            txtQtdDevolvido = (TextView) itemView.findViewById(R.id.consignado_item_tv_qtddevolvidos);
            txtValorTotItem = (TextView) itemView.findViewById(R.id.consignado_item_tv_valortotal);
            ivCodigoBarra = (ImageView) itemView.findViewById(R.id.consignado_item_iv_iccid);
        }
    }
}
