package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Vendedor;

import java.util.ArrayList;

public class VendedorAdapter extends ArrayAdapter<Vendedor> {
    private LayoutInflater inflter;
    private ArrayList<Vendedor> values;

    public VendedorAdapter(Context context, int textViewResourceId, ArrayList<Vendedor> values) {
        super(context, textViewResourceId, values);
        this.values = values;
        inflter = (LayoutInflater.from(context));
    }

    public int getCount() {
        return values.size();
    }

    public Vendedor getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setTextColor(Color.parseColor("#FFFFFF"));
        names.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        names.setText(values.get(position).vendedor);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setTextColor(Color.parseColor("#FFFFFF"));
        names.setBackgroundColor(Color.parseColor("#A52A2A"));
        names.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        names.setText(values.get(position).vendedor);
        return convertView;
    }
}
