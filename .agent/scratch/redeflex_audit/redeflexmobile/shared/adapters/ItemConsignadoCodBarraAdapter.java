package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ConsignadoBipagemActivity;
import com.axys.redeflexmobile.ui.venda.bipagem.BipagemVendaActivity;

import java.util.ArrayList;

public class ItemConsignadoCodBarraAdapter extends RecyclerView.Adapter<ItemConsignadoCodBarraAdapter.ViewItemConsignadoCodBarraHolder> {

    private Context mContext;
    private ArrayList<ConsignadoItem> mLista;
    private int idVisita;

    public ItemConsignadoCodBarraAdapter(ArrayList<ConsignadoItem> pLista, Context pContext, int pIdVisita) {
        mContext = pContext;
        mLista = pLista;
        idVisita = pIdVisita;
    }

    @Override
    public ItemConsignadoCodBarraAdapter.ViewItemConsignadoCodBarraHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_consignado_auditagem, parent, false);
        return new ItemConsignadoCodBarraAdapter.ViewItemConsignadoCodBarraHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemConsignadoCodBarraAdapter.ViewItemConsignadoCodBarraHolder holder, int position) {
        final ConsignadoItem consignadoItem = mLista.get(position);
        holder.txtProduto.setText(consignadoItem.getIdProduto() + " - " + consignadoItem.getNomeProduto());
        holder.txtQtd.setText(String.valueOf(consignadoItem.getQtd()));
        holder.txtValor.setText(Util_IO.formatDoubleToDecimalNonDivider(consignadoItem.getValorUnit()));

        // Verifica Numero de Seriais Informados
        int qtdSeriais = 0;
        for (int aa = 0; aa < consignadoItem.getListaCodigoBarra().size(); aa++) {
            qtdSeriais += consignadoItem.getListaCodigoBarra().get(aa).getQtd();
        }
        holder.txtQtdSerial.setText(String.valueOf(qtdSeriais));

        // Caso já tenha feito a leitura de todos os seriais desabilitar o icone de leitura dos códigos de barras
        if (consignadoItem.getQtd() == qtdSeriais) {
            holder.txtQtdSerial.setTextColor(ContextCompat.getColor(mContext, R.color.botao_ok));
            holder.imgIccid.setVisibility(View.INVISIBLE);
        }
        else {
            holder.txtQtdSerial.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            holder.imgIccid.setVisibility(View.VISIBLE);
        }

        holder.imgIccid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Roni", "onClick: " + idVisita);
                Bundle bundle = new Bundle();
                bundle.putInt(Config.CodigoVisita, idVisita);
                bundle.putString(Config.CodigoProduto, consignadoItem.getIdProduto());
                bundle.putString(Config.CODIGO_ITEM_VENDA, String.valueOf(consignadoItem.getId()));
                bundle.putInt(Config.QTD_ITEM_VENDA, consignadoItem.getQtd());
                bundle.putInt(Config.QTD_ITEM_LIDO_VENDA, 0);
                bundle.putBoolean(Config.PRODUTO_COMBO, false);
                bundle.putString(Config.PRODUTO_COMBO_ID, "");
                bundle.putInt(Config.QTD_ITEM_COMBO_VENDA, 0);
                bundle.putInt(Config.QTD_ITEM_COMBO_LIDO_VENDA, 0);
                if (consignadoItem.getIdConsignado() > 0)
                    bundle.putString("Operacao", "Nova");
                else
                    bundle.putString("Operacao", "Parcial");

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

    protected class ViewItemConsignadoCodBarraHolder extends RecyclerView.ViewHolder {
        TextView txtProduto, txtQtd, txtQtdSerial, txtValor;
        ImageView imgIccid;

        ViewItemConsignadoCodBarraHolder(View itemView) {
            super(itemView);
            txtProduto = (TextView) itemView.findViewById(R.id.consAuditItem_tv_produto);
            txtQtd = (TextView) itemView.findViewById(R.id.consAuditItem_tv_quantidade);
            txtQtdSerial = (TextView) itemView.findViewById(R.id.consAuditItem_tv_serial);
            txtValor = (TextView) itemView.findViewById(R.id.consAuditItem_tv_valor);
            imgIccid = (ImageView) itemView.findViewById(R.id.consAuditItem_iv_iccid);
        }
    }
}
