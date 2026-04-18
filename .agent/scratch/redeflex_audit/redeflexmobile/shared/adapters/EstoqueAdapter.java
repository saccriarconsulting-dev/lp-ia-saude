package com.axys.redeflexmobile.shared.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Produto;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by joao.viana on 14/10/2016.
 */

public class EstoqueAdapter extends ArrayAdapter<Produto> {
    private final ArrayList<Produto> mLista;
    private final Context mContext;
    private final int mLayoutResourceId;

    public EstoqueAdapter(Context context, int resourceId, ArrayList<Produto> objects) {
        super(context, resourceId, objects);
        this.mLayoutResourceId = resourceId;
        this.mContext = context;
        this.mLista = objects;
    }

    public @NotNull View getView(int position, View convertView, @NotNull ViewGroup parent) {
        Produto obj = mLista.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.txtQuantidade)).setText("Qtd: " + obj.getQtde());
        ((TextView) convertView.findViewById(R.id.txtProduto)).setText(obj.getId() + "-" + obj.getNome());
        ((TextView) convertView.findViewById(R.id.txtQuantidadeConsignado)).setText("Consignado: " + obj.getQuantidadeConsignada());
        return convertView;
    }
}
