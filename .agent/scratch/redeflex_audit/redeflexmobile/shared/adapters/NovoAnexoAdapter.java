package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.NovoAnexo;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.ImagemActivity;

import java.util.ArrayList;

/**
 * Created by joao.viana on 15/03/2017.
 */

public class NovoAnexoAdapter extends ArrayAdapter<NovoAnexo> {
    private ArrayList<NovoAnexo> mLista;
    private Context mContext;

    public NovoAnexoAdapter(Context pContext, ArrayList<NovoAnexo> pLista) {
        super(pContext, R.layout.item_chamado_anexo, pLista);
        this.mContext = pContext;
        this.mLista = pLista;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public View getView(int position, View convertView, ViewGroup parent) {
        final NovoAnexo novoAnexo = mLista.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_chamado_anexo, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.txtValor)).setText(novoAnexo.getNomeArquivo());

        convertView.setOnClickListener((view) -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString(Config.LocalImagem, novoAnexo.getLocalArquivo());
                bundle.putString(Config.NomeImagem, novoAnexo.getNomeArquivo());
                Utilidades.openNewActivity(mContext, ImagemActivity.class, bundle, false);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return convertView;
    }
}