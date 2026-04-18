package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.OperadoraAtend;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by joao.viana on 31/10/2017.
 */

public class AtendOperadoraAdapter extends RecyclerView.Adapter<AtendOperadoraAdapter.ViewAtendOperadoraHolder> {
    private Context mContext;
    private ArrayList<OperadoraAtend> mLista;

    public AtendOperadoraAdapter(Context pContext, ArrayList<OperadoraAtend> pLista) {
        mContext = pContext;
        mLista = pLista;
    }

    @Override
    public ViewAtendOperadoraHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_atividadevisita, parent, false);
        return new ViewAtendOperadoraHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewAtendOperadoraHolder holder, int position) {
        OperadoraAtend operadoraAtend = mLista.get(position);
        holder.txtDescricao.setText(operadoraAtend.getDescricao());
        holder.txtData.setText(Util_IO.dateToStringBr(operadoraAtend.getData()));
        if (operadoraAtend.isAtendido()) {
            holder.imgIcone.setImageResource(R.mipmap.icon_checkok);
            holder.txtDescricao.setTextColor(Color.parseColor("#00963A"));
            holder.txtData.setTextColor(Color.parseColor("#00963A"));
        } else {
            holder.imgIcone.setImageResource(R.mipmap.icon_checkcinza);
            holder.txtDescricao.setTextColor(Color.parseColor("#A3A3A3"));
            holder.txtData.setTextColor(Color.parseColor("#A3A3A3"));
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewAtendOperadoraHolder extends RecyclerView.ViewHolder {
        private TextView txtDescricao, txtData;
        private ImageView imgIcone;

        ViewAtendOperadoraHolder(View itemView) {
            super(itemView);
            txtDescricao = (TextView) itemView.findViewById(R.id.txtDescricao);
            imgIcone = (ImageView) itemView.findViewById(R.id.imgIcone);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
        }
    }
}