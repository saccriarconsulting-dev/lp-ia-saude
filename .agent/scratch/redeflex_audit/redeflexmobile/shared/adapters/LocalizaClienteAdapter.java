package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;

import java.util.ArrayList;

/**
 * Created by joao.viana on 30/08/2017.
 */

public class LocalizaClienteAdapter extends ArrayAdapter<Cliente> {
    private ArrayList<Cliente> mLista;
    private Context mContext;
    private int layoutResourceId;

    public LocalizaClienteAdapter(Context context, int resourceId, ArrayList<Cliente> objects) {
        super(context, resourceId, objects);
        this.layoutResourceId = resourceId;
        this.mContext = context;
        this.mLista = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cliente objectItem = mLista.get(position);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.txtCliente)).setText(objectItem.retornaCodigoExibicao() + " - " + objectItem.getNomeFantasia());

        return convertView;
    }
}