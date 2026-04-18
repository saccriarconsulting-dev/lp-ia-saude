package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.DevolucaoICCID;

import java.util.ArrayList;

/**
 * Created by joao.viana on 31/08/2017.
 */

public class DevIccidAdapter extends ArrayAdapter<DevolucaoICCID> {
    private ArrayList<DevolucaoICCID> mLista;
    private Context mContext;
    private int mLayoutResourceId;

    public DevIccidAdapter(Context context, int resourceId, ArrayList<DevolucaoICCID> objects) {
        super(context, resourceId, objects);
        this.mLayoutResourceId = resourceId;
        this.mContext = context;
        this.mLista = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final DevolucaoICCID objectItem = mLista.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.txtICCIDEntrada)).setText(objectItem.getIccidEntrada());
        ((TextView) convertView.findViewById(R.id.txtICCIDSaida)).setText(objectItem.getIccidSaida());

        convertView.findViewById(R.id.imgDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLista.remove(objectItem);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}