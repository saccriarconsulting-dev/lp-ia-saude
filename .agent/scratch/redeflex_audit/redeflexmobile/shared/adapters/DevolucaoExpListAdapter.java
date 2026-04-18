package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBDevolucao;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.DevolucaoICCID;
import com.axys.redeflexmobile.shared.models.DevolucaoItens;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Alerta;

import java.util.ArrayList;

/**
 * Created by joao.viana on 01/09/2017.
 */

public class DevolucaoExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<DevolucaoItens> mLista;

    public DevolucaoExpListAdapter(Context pContext, ArrayList<DevolucaoItens> pLista) {
        mContext = pContext;
        mLista = pLista;
    }

    @Override
    public int getGroupCount() {
        return mLista.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mLista.get(groupPosition).getListICCID().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getListICCID().get(childPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mLista.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getListICCID().get(childPosition).getId();
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final DevolucaoItens item = mLista.get(groupPosition);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_devolucao_agrup, null);

        Produto produto = new DBEstoque(mContext).getProdutoById(item.getIdProduto());
        ((TextView) convertView.findViewById(R.id.txtProduto)).setText(produto.getId() + " - " + produto.getNome());
        ((TextView) convertView.findViewById(R.id.txtQuantidade)).setText(String.valueOf(item.getQuantidade()));
        ImageButton imgDelete = (ImageButton) convertView.findViewById(R.id.imgDelete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "Deseja remover o item?");
                alerta.showConfirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DBDevolucao(mContext).deleteItemById(String.valueOf(item.getId()));
                        mLista.remove(item);
                        notifyDataSetChanged();
                    }
                }, null);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_dev_iccid, null);

        DevolucaoICCID devolucaoICCID = mLista.get(groupPosition).getListICCID().get(childPosition);
        ((TextView) convertView.findViewById(R.id.txtICCIDEntrada)).setText(devolucaoICCID.getIccidEntrada());
        ((TextView) convertView.findViewById(R.id.txtICCIDSaida)).setText(devolucaoICCID.getIccidSaida());
        convertView.findViewById(R.id.imgDelete).setVisibility(View.GONE);

        return convertView;
    }
}