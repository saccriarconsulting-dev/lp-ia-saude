package com.axys.redeflexmobile.shared.adapters;

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
 * Created by Desenvolvimento on 05/04/2016.
 */
public class ProdutoAdapter extends ArrayAdapter<Produto> {
    private LayoutInflater inflter;
    private ArrayList<Produto> values;

    public ProdutoAdapter(Context context, int textViewResourceId, ArrayList<Produto> values) {
        super(context, textViewResourceId, values);
        this.values = values;
        inflter = (LayoutInflater.from(context));
    }

    public int getCount() {
        return values.size();
    }

    public Produto getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(values.get(position).getNome());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(values.get(position).getNome());
        return convertView;
    }
}