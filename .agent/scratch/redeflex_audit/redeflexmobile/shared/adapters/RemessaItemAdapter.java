package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.RemessaLista;

import java.util.ArrayList;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class RemessaItemAdapter extends ArrayAdapter<RemessaLista> {
    private ArrayList<RemessaLista> data;
    private Context mContext;
    int layoutResourceId;

    public RemessaItemAdapter(Context context, int resourceId, ArrayList<RemessaLista> objects) {
        super(context, resourceId, objects);
        this.mContext = context;
        this.layoutResourceId = resourceId;
        this.data = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        RemessaLista obj = data.get(position);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(layoutResourceId, parent, false);

        TextView txtItem = (TextView) convertView.findViewById(R.id.txtItem);
        final EditText txtQtd = (EditText) convertView.findViewById(R.id.txtQtd);

        txtItem.setText(obj.getItemDescricao());

        if (obj.getQtdInformada_item() > 0) {
            txtQtd.setText(String.valueOf(obj.getQtdInformada_item()));
        }

        if (obj.getSituacao_capa() == 3) {
            txtQtd.setEnabled(false);
        }

        txtQtd.setTag(position);
        txtQtd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                    data.get(position).setQtdInformada_item(Integer.parseInt(s.toString().trim()));
                else
                    data.get(position).setQtdInformada_item(0);
            }
        });

        return convertView;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        boolean notifyChanged = true;
    }

    public ArrayList<RemessaLista> update() {
        return this.data;
    }
}