package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Devolucao;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by joao.viana on 01/09/2017.
 */

public class DevolucaoAdapter extends ArrayAdapter<Devolucao> {
    private Context mContext;
    private ArrayList<Devolucao> mLista;
    private int mLayoutResourceId;

    public DevolucaoAdapter(Context context, int resourceId, ArrayList<Devolucao> objects) {
        super(context, resourceId, objects);
        this.mLayoutResourceId = resourceId;
        this.mContext = context;
        this.mLista = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Devolucao objDevolucao = mLista.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }

        String numero = "# ";
        if (!Util_IO.isNullOrEmpty(objDevolucao.getIdServer()))
            numero += Util_IO.padLeft(objDevolucao.getIdServer(), 10, "0");
        else
            numero += Util_IO.padLeft(String.valueOf(objDevolucao.getId()), 10, "0");
        ((TextView) convertView.findViewById(R.id.txtNumero)).setText(numero);

        TextView txtTipo = (TextView) convertView.findViewById(R.id.txtTipo);

        switch (objDevolucao.getTipo()) {
            case 1:
                txtTipo.setText("Tipo: Troca");
                break;
            default:
                break;
        }

        TextView txtCliente = (TextView) convertView.findViewById(R.id.txtCliente);
        if (!Util_IO.isNullOrEmpty(objDevolucao.getIdCliente())) {
            txtCliente.setVisibility(View.VISIBLE);
            Cliente cliente = new DBCliente(mContext).getById(objDevolucao.getIdCliente());
            if (cliente != null)
                txtCliente.setText("Cliente: " + cliente.retornaCodigoExibicao() + " - " + cliente.getNomeFantasia());
            else
                txtCliente.setText("Cliente não encontrado");
        } else
            txtCliente.setVisibility(View.GONE);

        ((TextView) convertView.findViewById(R.id.txtData)).setText("Data: " + Util_IO.dateTimeToStringBr(objDevolucao.getData()));

        TextView txtSituacao = (TextView) convertView.findViewById(R.id.txtSituacao);

        switch (objDevolucao.getSituacao()) {
            case 1:
                txtSituacao.setText("Situação: Pendente");
                break;
            default:
                break;
        }

        return convertView;
    }
}