package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Produto;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 21/04/2016.
 */
public class ItemSolicitacaoDetalhesAdapter extends ArrayAdapter<Produto> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Produto> listaProduto;

    public ItemSolicitacaoDetalhesAdapter(Context mContext, int layoutResourceId, ArrayList<Produto> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.listaProduto = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Produto objectItem = listaProduto.get(position);

        ((TextView) convertView.findViewById(R.id.lbl_nome)).setText(objectItem.getNome());
        ((TextView) convertView.findViewById(R.id.lbl_qtde_sug)).setText(String.valueOf(objectItem.getEstoqueSugerido()));
        ((TextView) convertView.findViewById(R.id.lbl_qtde_solicitada)).setText(String.valueOf(objectItem.getQtde()));
        return convertView;
    }
}