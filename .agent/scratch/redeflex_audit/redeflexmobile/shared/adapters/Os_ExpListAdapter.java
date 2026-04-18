package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.OS;
import com.axys.redeflexmobile.shared.models.OsConsulta;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.OSActivity;

import java.util.ArrayList;

public class Os_ExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<OsConsulta> mLista;

    public Os_ExpListAdapter(Context pContext, ArrayList<OsConsulta> pLista) {
        this.mContext = pContext;
        this.mLista = pLista;
    }

    @Override
    public int getGroupCount() {
        return mLista.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mLista.get(groupPosition).getList().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mLista.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mLista.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getList().get(childPosition).getId();
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        OsConsulta item = mLista.get(groupPosition);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_os_agrup, null);
        TextView titulo = (TextView) convertView.findViewById(R.id.txtPeriodo);
        titulo.setText(Util_IO.formataDataPtBr(item.getDataOs()));
        TextView qtdOS = (TextView) convertView.findViewById(R.id.txtQtdOS);
        if (mLista.get(groupPosition).getList() != null && mLista.get(groupPosition).getList().size() > 0)
            qtdOS.setText("Qtd.: " + mLista.get(groupPosition).getList().size());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_os, null);

        final OS ordemServico = mLista.get(groupPosition).getList().get(childPosition);

        ((TextView) convertView.findViewById(R.id.txtNumero)).setText("Protocolo " + String.valueOf(ordemServico.getId()));
        ((TextView) convertView.findViewById(R.id.txt_item_os_cliente)).setText(ordemServico.getNomeCliente());
        ((TextView) convertView.findViewById(R.id.txt_item_os_data)).setText(Util_IO.dateTimeToString(ordemServico.getData()));
        ((TextView) convertView.findViewById(R.id.txt_item_os_tipoos)).setText(ordemServico.getDescricaoTipo());

        if (ordemServico.getDataAgendamento() != null)
            ((TextView) convertView.findViewById(R.id.txt_item_os_data1)).setText(Util_IO.dateTimeToString(ordemServico.getDataAgendamento()));
        if (ordemServico.getDataAtendimento() != null)
            ((TextView) convertView.findViewById(R.id.txt_item_os_data1)).setText(Util_IO.dateTimeToString(ordemServico.getDataAtendimento()));

        if (ordemServico.getDataAtendimento() == null) {
            convertView.setOnClickListener((view) -> {
                Bundle bundle = new Bundle();
                bundle.putInt(Config.CodigoItemOS, ordemServico.getId());
                Utilidades.openNewActivityForResult(mContext, OSActivity.class, Config.RequestCodeOS, bundle);
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}