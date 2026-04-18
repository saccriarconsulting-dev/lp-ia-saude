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
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 06/04/2016.
 */
public class SolicitacaoAdapter extends ArrayAdapter<SolicitacaoMercadoria> {
    public interface OnClickListener {
        void onClickVisualizar(int codSolicitacao);
    }

    public OnClickListener myClickListener;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<SolicitacaoMercadoria> listaSolicitacao;

    public SolicitacaoAdapter(Context mContext, int layoutResourceId, ArrayList<SolicitacaoMercadoria> pLista) {
        super(mContext, layoutResourceId, pLista);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.listaSolicitacao = pLista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        final SolicitacaoMercadoria objectItem = listaSolicitacao.get(position);

        TextView txtcod = (TextView) convertView.findViewById(R.id.lbl_codpedido);
        TextView txtdata = (TextView) convertView.findViewById(R.id.lbl_datapedido);
        TextView txtstatus = (TextView) convertView.findViewById(R.id.lbl_statuspedido);
        TextView txtdataat = (TextView) convertView.findViewById(R.id.lbl_ultimaatualizpedido);

        ImageButton imgbtn_ver = (ImageButton) convertView.findViewById(R.id.imgbtn_ver);

        if (objectItem.getIdServer() > 0)
            txtcod.setText(String.valueOf(objectItem.getIdServer()));
        else
            txtcod.setText(String.valueOf(objectItem.getId()));

        txtdata.setText(Util_IO.dateTimeToString(objectItem.getDataCriacao()));
        txtstatus.setText(SolicitacaoMercadoria.getDescricaoStatus(objectItem.getStatus()));
        txtdataat.setText(Util_IO.dateTimeToString(objectItem.getDataUltimaAtualizacao()));

        imgbtn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myClickListener != null) {
                    myClickListener.onClickVisualizar(objectItem.getId());
                }
            }
        });

        convertView.setTag(objectItem);
        return convertView;
    }
}