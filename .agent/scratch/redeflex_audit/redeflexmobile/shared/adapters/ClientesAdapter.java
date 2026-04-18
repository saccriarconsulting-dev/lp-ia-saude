package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by joao.viana on 25/07/2017.
 */

public class ClientesAdapter extends ArrayAdapter<Cliente> {
    private ArrayList<Cliente> mLista;
    private Context mContext;
    private int mLayoutResourceId;

    public ClientesAdapter(Context pContext, int pResourceId, ArrayList<Cliente> pLista) {
        super(pContext, pResourceId, pLista);
        mLayoutResourceId = pResourceId;
        mContext = pContext;
        mLista = pLista;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cliente cliente = mLista.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }
        String documento = cliente.personType() == EnumRegisterPersonType.JURIDICAL?
                "CNPJ: "+ StringUtils.maskCpfCnpj(cliente.getCpf_cnpj()):"CPF: "+ StringUtils.maskCpfCnpj(cliente.getCpf_cnpj());
        String codigo = mContext.getString(R.string.cliente_lista_item_tv_codigo, cliente.retornaCodigoExibicao(), documento );

        ((TextView) convertView.findViewById(R.id.txtCliente)).setText(cliente.getNomeFantasia());
        ((TextView) convertView.findViewById(R.id.txtEnderecoCliente)).setText(cliente.retornaEnderecoCompleto());
        ((TextView) convertView.findViewById(R.id.txtTipoCliente)).setText(codigo);

        convertView.setTag(cliente.getId());
        return convertView;
    }
}