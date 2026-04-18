package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

public class SugestaoVendaAdapter extends ArrayAdapter<SugestaoVenda> {

    private Context mContext;
    private ArrayList<SugestaoVenda> mLista;
    private int mLayoutResourceId;

    public SugestaoVendaAdapter(Context context, int resourceId, ArrayList<SugestaoVenda> objects) {
        super(context, resourceId, objects);
        this.mLayoutResourceId = resourceId;
        this.mContext = context;
        this.mLista = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SugestaoVenda objSugestao = mLista.get(position);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_sugestao_venda, null);

        TextView txtGrupo = (TextView) convertView.findViewById(R.id.item_sugestao_venda_txtGrupo);
        ImageView imgOperadora = (ImageView) convertView.findViewById(R.id.item_sugestao_venda_imgOperadora);

        txtGrupo.setText(objSugestao.getDescricaogrupo());

        switch (objSugestao.getIdOperadora()) {
            case 1:
                imgOperadora.setImageResource(R.drawable.ic_oi_wrapped);
                break;
            case 2:
                imgOperadora.setImageResource(R.drawable.ic_claro_wrapped);
                break;
            case 3:
                imgOperadora.setImageResource(R.drawable.ic_vivo_wrapped);
                break;
            case 4:
                imgOperadora.setImageResource(R.drawable.ic_tim_wrapped);
                break;
        }

        return convertView;
    }

}
