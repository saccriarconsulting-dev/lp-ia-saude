package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Remessa;

import java.util.ArrayList;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class RemessaAdapter extends ArrayAdapter<Remessa> {
    private ArrayList<Remessa> data;
    private Context mContext;
    int layoutResourceId;

    public RemessaAdapter(Context context, int resourceId, ArrayList<Remessa> objects) {
        super(context, resourceId, objects);
        this.mContext = context;
        this.layoutResourceId = resourceId;
        this.data = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Remessa obj = data.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.imgStatusRemessa);
        TextView txtNumero = (TextView) convertView.findViewById(R.id.txtNumero);
        TextView txtStatus = (TextView) convertView.findViewById(R.id.txtStatusRemessa);

        txtNumero.setText("Remessa " + obj.getNumero());

        if (String.valueOf(obj.getSituacao()).equals("3")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_ok, mContext.getTheme()));
            else
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_ok));
            txtStatus.setText("Aceite Realizado");
        } else if (String.valueOf(obj.getSituacao()).equals("4")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_cancelar, mContext.getTheme()));
            else
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_cancelar));
            txtStatus.setText("Cancelada");
        } else if (String.valueOf(obj.getSituacao()).equals("32")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_cancelar, mContext.getTheme()));
            else
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_cancelar));
            txtStatus.setText("Comprovante Pendente");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_cinza, mContext.getTheme()));
            else
                imgStatus.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_check_cinza));
            txtStatus.setText("Pendente de Aceite");
        }

        return convertView;
    }
}