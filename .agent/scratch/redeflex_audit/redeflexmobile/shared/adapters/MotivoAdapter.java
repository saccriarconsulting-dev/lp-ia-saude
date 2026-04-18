package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Motivo;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 01/07/2016.
 */
public class MotivoAdapter extends ArrayAdapter<Motivo> {
    private LayoutInflater inflter;
    private ArrayList<Motivo> values;

    public MotivoAdapter(Context context, int textViewResourceId, ArrayList<Motivo> values) {
        super(context, textViewResourceId, values);
        this.values = values;
        this.inflter = (LayoutInflater.from(context));
    }

    public int getCount() {
        return values.size();
    }

    public Motivo getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(values.get(position).getDescricao());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(values.get(position).getDescricao());
        return convertView;
    }
}