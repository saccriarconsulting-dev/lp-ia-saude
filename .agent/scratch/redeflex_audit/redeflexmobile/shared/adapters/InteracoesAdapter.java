package com.axys.redeflexmobile.shared.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.AnexoChamado;
import com.axys.redeflexmobile.shared.models.InteracaoAnexos;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ImagemActivity;

import java.util.ArrayList;

/**
 * Created by joao.viana on 08/03/2017.
 */

public class InteracoesAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<InteracaoAnexos> mLista;

    public InteracoesAdapter(Context pContext, ArrayList<InteracaoAnexos> pLista) {
        this.mContext = pContext;
        this.mLista = pLista;
    }

    @Override
    public int getGroupCount() {
        return mLista.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mLista.get(groupPosition).getListaAnexos().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mLista.get(groupPosition).getListaAnexos().get(childPosition);
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
        return mLista.get(groupPosition).getListaAnexos().get(childPosition).getIdAppMobile();
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        InteracaoAnexos item = mLista.get(groupPosition);
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_interacao, null);

        ((TextView) convertView.findViewById(R.id.txtUsuario)).setText(item.getUsuario() + ":");
        ((TextView) convertView.findViewById(R.id.txtData)).setText(Util_IO.dateTimeToStringBr(item.getDataCadastro()));
        ((TextView) convertView.findViewById(R.id.txtDescricao)).setText(item.getDescricao());

        return convertView;
    }

    @Override
    @SuppressWarnings("TryWithIdenticalCatches")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_chamado_anexo, null);

        final AnexoChamado anexosChamado = mLista.get(groupPosition).getListaAnexos().get(childPosition);
        ((TextView) convertView.findViewById(R.id.txtValor)).setText(anexosChamado.getNome());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(mContext, ImagemActivity.class);
                    intent.putExtra(Config.LocalImagem, anexosChamado.getArquivo());
                    intent.putExtra(Config.NomeImagem, anexosChamado.getNome());
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}