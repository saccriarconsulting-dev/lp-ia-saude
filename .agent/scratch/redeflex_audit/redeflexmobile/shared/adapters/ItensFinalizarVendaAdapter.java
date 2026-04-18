package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.PrecoFormaCache;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.TipoFormaPgto;
import com.axys.redeflexmobile.shared.models.ValorPorFormaPagto;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by enzo teles on 27/02/2026.
 */

public class ItensFinalizarVendaAdapter extends RecyclerView.Adapter<ItensFinalizarVendaAdapter.ViewItensVendaHolder> {

    private Context mContext;
    private final ArrayList<ItemVendaCombo> mLista;
    private TipoFormaPgto formaSelecionada;


    public ItensFinalizarVendaAdapter(ArrayList<ItemVendaCombo> lista, TipoFormaPgto formaSelecionada,  Context pContext) {
        this.mLista = lista;
        this.formaSelecionada = formaSelecionada;
        this.mContext = pContext;
    }

    public void setFormaSelecionada(TipoFormaPgto forma) {
        this.formaSelecionada = forma;
        notifyDataSetChanged();
    }

    @Override
    public ViewItensVendaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_produto_venda, parent, false);
        return new ViewItensVendaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewItensVendaHolder holder, int position) {
        try {
            final ItemVendaCombo itemVenda = mLista.get(position);

            if (itemVenda == null) return;

            holder.txtProduto.setText(itemVenda.getIdProduto() + " - " + itemVenda.getNomeProduto());
            holder.txtQtd.setText("Qtd: " + itemVenda.getQtde());
            double valorUnFinal = getPrecoPorForma(itemVenda, formaSelecionada);

            double totalItem = valorUnFinal * itemVenda.getQtde();

            holder.txtUnit.setText("Valor Unit. R$ " + Util_IO.formatDoubleToDecimalNonDivider(valorUnFinal));
            holder.txtValor.setText("Valor R$ " + Util_IO.formatDoubleToDecimalNonDivider(totalItem));

            holder.btnExcluir.setVisibility(View.GONE);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg == null || !msg.contains("Attempt to invoke")) {
                Log.e("ItensFinalizarVenda", "Erro bind: " + msg, ex);
            } else {
                Log.e("ItensFinalizarVenda", "Ignorado (paliativo): " + msg);
            }
        }

    }

    private double getPrecoPorForma(ItemVendaCombo item, TipoFormaPgto forma) {

        String idProduto = item.getIdProduto();

        double precoBase = item.getValorUN();

        if (forma == null) return precoBase;

        ValorPorFormaPagto v = resolveValorPorForma(idProduto);
        if (v == null) return precoBase;

        double acrescimo;
        switch (forma) {
            case APRAZO:
                acrescimo = v.getAPrazo();
                break;
            case PIX:
                acrescimo = v.getPix();
                break;
            case CARTAO:
                acrescimo = v.getCartao();
                break;
            case AVISTA:
            default:
                acrescimo = v.getAVista();
                break;
        }
        return (acrescimo > 0.0) ? (precoBase + acrescimo) : precoBase;
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

    private ValorPorFormaPagto resolveValorPorForma(String idProduto) {
        ValorPorFormaPagto v = PrecoFormaCache.getValorPorForma(idProduto);
        if (v != null) return v;

        try {
            Produto p = new DBEstoque(mContext).getProdutoById(idProduto);
            if (p != null && p.getValorPorFormaPagto() != null) {
                PrecoFormaCache.put(p);
                return p.getValorPorFormaPagto();
            }
        } catch (Exception ignored) {}

        return null;
    }

}