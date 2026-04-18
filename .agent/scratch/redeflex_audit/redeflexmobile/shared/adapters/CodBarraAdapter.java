package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

import okhttp3.internal.Util;

/**
 * Created by joao.viana on 17/10/2017.
 */

public class CodBarraAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ICodBarraAdapterListenner adapterListenner;
    private ArrayList<ItemVendaCombo> mLista;
    private int quantidadeExterna;

    public CodBarraAdapter(ArrayList<ItemVendaCombo> pLista,
                           Context pContext,
                           ICodBarraAdapterListenner adapterListenner,
                           int quantidadeExterna) {
        this.mLista = pLista;
        this.mContext = pContext;
        this.adapterListenner = adapterListenner;
        this.quantidadeExterna = quantidadeExterna;
    }

    @Override
    public int getGroupCount() {
        return mLista.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mLista.get(groupPosition).getCodigosList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mLista.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getCodigosList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ItemVendaCombo itemVendaCombo = mLista.get(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_produto_venda, parent, false);
        }

        limpaLayoutGroupView(convertView);

        ((TextView) convertView.findViewById(R.id.txtProduto)).setText(itemVendaCombo.getIdProduto() + " - " + itemVendaCombo.getNomeProduto());
        convertView.findViewById(R.id.btnExcluir).setOnClickListener(view -> adapterListenner.removeGroupItem(mLista, groupPosition));

        TextView txtQtdFalta = convertView.findViewById(R.id.txtValor);
        TextView txtQtd = convertView.findViewById(R.id.txtQuantidade);

        txtQtdFalta.setVisibility(View.GONE);
        txtQtd.setText("Qtd: " + itemVendaCombo.getQtde());

        if (itemVendaCombo.isCombo()) {
            int qtCombo = CodigoBarra.retornaQtCombo(itemVendaCombo.getIdProduto(), mContext);
            if (qtCombo > 0) {
                ProdutoCombo produtoCombo = CodigoBarra.retornaCombo(qtCombo, itemVendaCombo.getCodigosList(), UsoCodBarra.GERAL);
                txtQtdFalta.setVisibility(View.VISIBLE);
                txtQtdFalta.setText("Falta Registrar: " + produtoCombo.getQtdFalta());
            }

        } else if (itemVendaCombo.getQtdCombo() > 0) {
            ProdutoCombo produtoCombo = CodigoBarra.retornaCombo(itemVendaCombo.getQtdCombo(), itemVendaCombo.getCodigosList(), UsoCodBarra.GERAL);
            txtQtd.setText("Qtd: " + produtoCombo.getQtdTotal());

            int qtdSerial = itemVendaCombo.getCodigosList() != null ? itemVendaCombo.getCodigosList().size() : 0;
            int qtdeFalta = (quantidadeExterna * itemVendaCombo.getQtdCombo()) - qtdSerial;
            if (qtdeFalta > 0) {
                txtQtdFalta.setVisibility(View.VISIBLE);
                txtQtdFalta.setText("Falta Registrar: " + qtdeFalta);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CodBarra codBarra = mLista.get(groupPosition).getCodigosList().get(childPosition);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_codbarra_venda, null);

        limpaLayoutChildView(convertView);

        TextView txtFinal = convertView.findViewById(R.id.txtFinal);
        ((TextView) convertView.findViewById(R.id.txtInicial)).setText(codBarra.getCodBarraInicial());
        if (codBarra.getIndividual())
            txtFinal.setVisibility(View.GONE);
        else
            txtFinal.setText(codBarra.getCodBarraFinal());

        // TODO: Voltar esse cara para calcular quantidade
//        ((TextView) convertView.findViewById(R.id.txtQuantidade)).setText("Qtd: " + codBarra.retornaQuantidade(UsoCodBarra.GERAL, codBarra.getIdProduto(), convertView.findViewById(R.id.txtQuantidade).getContext()));
        ((TextView) convertView.findViewById(R.id.txtQuantidade)).setText("Qtd: " + codBarra.retornaQuantidade(UsoCodBarra.GERAL));
        ImageButton btnExcluir = convertView.findViewById(R.id.btnExcluir);

        btnExcluir.setVisibility(View.VISIBLE);

        if (mLista.get(groupPosition).isCombo()) {
            btnExcluir.setVisibility(View.GONE);
        }

        // Roni para identificar o que estava consignado
        if (!Util_IO.isNullOrEmpty(codBarra.getAuditadoCons()) && codBarra.getAuditadoCons().equals("S")) {
            btnExcluir.setVisibility(View.GONE);
        }

        btnExcluir.setOnClickListener(view ->
                adapterListenner.removeChildItem(mLista, codBarra, groupPosition, childPosition)
        );

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setmLista(ArrayList<ItemVendaCombo> mLista) {
        this.mLista = mLista;
        notifyDataSetChanged();
    }

    private void limpaLayoutGroupView(View convertView) {
        ((TextView) convertView.findViewById(R.id.txtProduto)).setText("");
        convertView.findViewById(R.id.btnExcluir).setOnClickListener(null);

        TextView txtQtdFalta = convertView.findViewById(R.id.txtValor);
        TextView txtQtd = convertView.findViewById(R.id.txtQuantidade);

        txtQtdFalta.setVisibility(View.VISIBLE);
        txtQtdFalta.setText("");
        txtQtd.setText("");
    }

    private void limpaLayoutChildView(View convertView) {
        TextView txtFinal = convertView.findViewById(R.id.txtFinal);
        ((TextView) convertView.findViewById(R.id.txtInicial)).setText("");
        txtFinal.setVisibility(View.VISIBLE);
        txtFinal.setText("");

        ((TextView) convertView.findViewById(R.id.txtQuantidade)).setText("");
        ImageButton btnExcluir = convertView.findViewById(R.id.btnExcluir);

        btnExcluir.setVisibility(View.VISIBLE);
        btnExcluir.setOnClickListener(null);
    }

    public interface ICodBarraAdapterListenner {
        void removeGroupItem(ArrayList<ItemVendaCombo> lista, int groupPosition);

        void removeChildItem(ArrayList<ItemVendaCombo> lista, CodBarra codBarra, int groupPosition, int childPosition);
    }
}