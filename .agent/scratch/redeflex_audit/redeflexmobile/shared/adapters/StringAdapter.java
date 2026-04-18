package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by joao.viana on 19/01/2017.
 */

public class StringAdapter extends ArrayAdapter<String> implements Serializable {
    private LayoutInflater mInflter;
    private static final long serialVersionUID = 1;
    private ArrayList<String> mValues;

    public StringAdapter(Context context, int textViewResourceId, ArrayList<String> values) {
        super(context, textViewResourceId, values);
        mValues = values;
        mInflter = (LayoutInflater.from(context));
    }

    public int getCount() {
        return mValues.size();
    }

    public String getItem(int position) {
        return mValues.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(mValues.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = mInflter.inflate(R.layout.custom_spinner_title_bar, null);
        TextView names = (TextView) convertView.findViewById(R.id.txtValor);
        names.setText(mValues.get(position));
        return convertView;
    }
}