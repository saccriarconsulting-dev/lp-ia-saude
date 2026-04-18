package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ItemVenda;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by joao.viana on 20/07/2016.
 */
public class ItensConfirVendaAdapter extends ArrayAdapter<ItemVenda> {
    private ArrayList<ItemVenda> itensVenda;
    private LayoutInflater inflater;
    int layoutResourceId;

    public ItensConfirVendaAdapter(Context context, int resourceId, ArrayList<ItemVenda> objects) {
        super(context, resourceId, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResourceId = resourceId;
        this.itensVenda = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemVenda item = itensVenda.get(position);
        if (convertView == null)
            convertView = inflater.inflate(layoutResourceId, parent, false);

        TextView codigo = (TextView) convertView.findViewById(R.id.linhaProduto);
        TextView quantidade = (TextView) convertView.findViewById(R.id.linhaQuantidade);
        TextView descricao = (TextView) convertView.findViewById(R.id.linhaDescricao);
        TextView valor = (TextView) convertView.findViewById(R.id.linhaValor);

        codigo.setText(item.getIdProduto());
        DecimalFormat precision = new DecimalFormat("0.00");

        descricao.setText(item.getNomeProduto());
        quantidade.setText(String.valueOf(item.getQtde()));
        valor.setText("R$ " + precision.format(item.getValorUN()));

        return convertView;
    }
}