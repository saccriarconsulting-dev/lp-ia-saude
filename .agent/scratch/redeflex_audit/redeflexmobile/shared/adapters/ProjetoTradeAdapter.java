package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;

import java.util.ArrayList;

/**
 * Created by joao.viana on 06/10/2016.
 */

public class ProjetoTradeAdapter extends ArrayAdapter<ProjetoTrade> {
    private LayoutInflater inflter;
    private ArrayList<ProjetoTrade> values;

    public ProjetoTradeAdapter(Context context, int textViewResourceId, ArrayList<ProjetoTrade> values) {
        super(context, textViewResourceId, values);
        this.values = values;
        inflter = (LayoutInflater.from(context));
    }

    public int getCount() {
        return values.size();
    }

    public ProjetoTrade getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(values.get(position).getDdd() + " - " + values.get(position).getDescricao());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(values.get(position).getDdd() + " - " + values.get(position).getDescricao());
        return convertView;
    }
}