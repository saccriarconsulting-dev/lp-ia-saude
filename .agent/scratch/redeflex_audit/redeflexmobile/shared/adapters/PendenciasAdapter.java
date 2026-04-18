package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Pendencias;

import java.util.ArrayList;

/**
 * Created by joao.viana on 16/11/2016.
 */

public class PendenciasAdapter extends RecyclerView.Adapter<PendenciasAdapter.ViewPendenciasHolder> {
    private ArrayList<Pendencias> mLista;
    private Context mContext;

    public PendenciasAdapter(Context pContext, ArrayList<Pendencias> pLista) {
        this.mContext = pContext;
        this.mLista = pLista;
    }

    @Override
    public ViewPendenciasHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_pendencia, parent, false);
        return new ViewPendenciasHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewPendenciasHolder holder, int position) {
        Pendencias objectItem = mLista.get(position);
        holder.txtTipo.setText(objectItem.getDescricao());
        holder.txtQuantidade.setText(String.valueOf(objectItem.getQtd()));
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewPendenciasHolder extends RecyclerView.ViewHolder {
        private TextView txtTipo, txtQuantidade;

        ViewPendenciasHolder(View itemView) {
            super(itemView);
            txtQuantidade = (TextView) itemView.findViewById(R.id.txtQuantidade);
            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
        }
    }
}