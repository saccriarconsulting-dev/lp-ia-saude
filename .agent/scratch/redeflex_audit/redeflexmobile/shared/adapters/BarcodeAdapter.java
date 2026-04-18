package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 04/07/2016.
 */
public class BarcodeAdapter extends ArrayAdapter<CodBarra> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<CodBarra> listCodBarra;
    private UsoCodBarra usoCodBarra;

    public BarcodeAdapter(Context mContext, int layoutResourceId, ArrayList<CodBarra> _listCodBarra,
                          UsoCodBarra usoCodBarra) {
        super(mContext, layoutResourceId, _listCodBarra);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.listCodBarra = _listCodBarra;
        this.usoCodBarra = usoCodBarra;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView txtQuantidade = (TextView) convertView.findViewById(R.id.lbQuantidadeRange);
        TextView txtInicial = (TextView) convertView.findViewById(R.id.lbInicial);
        TextView txtFinal = (TextView) convertView.findViewById(R.id.lbFinal);
        ImageButton imgDelete = (ImageButton)convertView.findViewById(R.id.imgbtn_deleteRange);

        CodBarra objectItem = listCodBarra.get(position);
        txtInicial.setText(objectItem.getCodBarraInicial());
        txtFinal.setText(objectItem.getCodBarraFinal());
        txtQuantidade.setText(objectItem.retornaQuantidade(usoCodBarra));

        imgDelete.setVisibility(View.GONE);

        convertView.setTag(objectItem);
        return convertView;
    }
}