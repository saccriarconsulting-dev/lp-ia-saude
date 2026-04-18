package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.FormaPagamento;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 06/07/2016.
 */
public class FormaPagamentoAdapter extends ArrayAdapter<FormaPagamento> {
    private LayoutInflater layoutInflter;
    private ArrayList<FormaPagamento> listaFormaPag;

    public FormaPagamentoAdapter(Context context, int textViewResourceId, ArrayList<FormaPagamento> values) {
        super(context, textViewResourceId, values);
        listaFormaPag = values;
        layoutInflter = (LayoutInflater.from(context));
    }

    public int getCount() {
        return listaFormaPag.size();
    }

    public FormaPagamento getItem(int position) {
        return listaFormaPag.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(listaFormaPag.get(position).getDescricao());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(listaFormaPag.get(position).getDescricao());
        return convertView;
    }
}