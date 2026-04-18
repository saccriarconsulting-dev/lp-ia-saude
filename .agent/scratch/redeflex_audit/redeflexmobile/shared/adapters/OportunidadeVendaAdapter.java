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
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

public class OportunidadeVendaAdapter extends ArrayAdapter<Cliente> {
    private ArrayList<Cliente> mLista;
    private Context mContext;
    private int mLayoutResourceId;

    public OportunidadeVendaAdapter(Context pContext, int pResourceId, ArrayList<Cliente> pLista) {
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

        String endereco = "";
        if (!Util_IO.isNullOrEmpty(cliente.getTipoLogradouro()))
            endereco += cliente.getTipoLogradouro();
        if (!Util_IO.isNullOrEmpty(cliente.getNomeLogradouro()))
            endereco += ", " + cliente.getNomeLogradouro();
        if (!Util_IO.isNullOrEmpty(cliente.getNumeroLogradouro()))
            endereco += ", " + cliente.getNumeroLogradouro();
        if (!Util_IO.isNullOrEmpty(cliente.getComplementoLogradouro()))
            endereco += ", " + cliente.getComplementoLogradouro();
        if (!Util_IO.isNullOrEmpty(cliente.getBairro()))
            endereco += ", " + cliente.getBairro();

        String cidade = "";
        if (!Util_IO.isNullOrEmpty(cliente.getCidade()))
            cidade += cliente.getCidade();
        if (!Util_IO.isNullOrEmpty(cliente.getEstado()))
            cidade += "/" + cliente.getEstado();

        ((TextView) convertView.findViewById(R.id.linhaoportunidadevenda_txtCliente)).setText(cliente.getNomeFantasia());
        ((TextView) convertView.findViewById(R.id.linhaoportunidadevenda_txtEnderecoCliente)).setText(endereco);
        ((TextView) convertView.findViewById(R.id.linhaoportunidadevenda_txtCidadeCliente)).setText(cidade);
        ((TextView) convertView.findViewById(R.id.linhaoportunidadevenda_txtCodigoCliente)).setText(codigo);

        convertView.setTag(cliente.getId());
        return convertView;
    }
}
