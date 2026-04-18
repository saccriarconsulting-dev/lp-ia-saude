package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.VendaConsulta;
import com.axys.redeflexmobile.shared.models.VendaConsultaItem;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.VendaResumoActivity;

import java.util.ArrayList;

/**
 * Created by joao.viana on 08/08/2016.
 */
public class VendaExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private FragmentActivity mActivity;
    private ArrayList<VendaConsulta> mLista;

    public VendaExpListAdapter(FragmentActivity pFragmentActivity, Context pContext, ArrayList<VendaConsulta> pLista) {
        mContext = pContext;
        mLista = pLista;
        mActivity = pFragmentActivity;
    }

    public int getGroupCount() {
        return mLista.size();
    }

    public int getChildrenCount(int groupPosition) {
        return mLista.get(groupPosition).getList().size();
    }

    public Object getGroup(int groupPosition) {
        return mLista.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getList().get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        VendaConsulta vendaConsulta = mLista.get(groupPosition);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_cabecalho_venda, null);

        TextView periodo = convertView.findViewById(R.id.txtPeriodo);
        TextView valor = convertView.findViewById(R.id.txtValor);

        periodo.setText(Util_IO.formataDataPtBr(vendaConsulta.getDataVenda()));
        valor.setText(Util_IO.formatDoubleToDecimalNonDivider(vendaConsulta.getValor()));

        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_linha_venda, null);

        final VendaConsultaItem vendaConsultaItem = mLista.get(groupPosition).getList().get(childPosition);

        TextView cliente = convertView.findViewById(R.id.txtCliente);
        TextView horario = convertView.findViewById(R.id.txtHorario);
        TextView valor = convertView.findViewById(R.id.txtValor);

        cliente.setText(vendaConsultaItem.getCliente());
        horario.setText(vendaConsultaItem.getHoravenda());
        valor.setText(Util_IO.formatDoubleToDecimalNonDivider(vendaConsultaItem.getValor()));

        convertView.setOnClickListener((view) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.CodigoVenda, vendaConsultaItem.getId());
            Utilidades.openNewActivity(mActivity, VendaResumoActivity.class, bundle, false);
        });

        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}