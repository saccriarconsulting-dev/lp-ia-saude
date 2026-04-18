package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Imagem;
import com.axys.redeflexmobile.ui.component.ViewImage;

import java.util.ArrayList;

/**
 * Created by joao.viana on 04/10/2017.
 */

public class ViewImagAdapter extends RecyclerView.Adapter<ViewImagAdapter.ViewImagHolder> {
    private ArrayList<Imagem> mImagems;
    private Context mContext;

    public ViewImagAdapter(ArrayList<Imagem> pList, Context pContext) {
        mImagems = pList;
        mContext = pContext;
    }

    @Override
    public ViewImagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_imagem, parent, false);
        return new ViewImagHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewImagHolder holder, int position) {
        final int iposicao = position;
        Imagem item = mImagems.get(iposicao);
        Bitmap bImagem = BitmapFactory.decodeFile(item.getLocal());
        holder.viewImagem.setImageBitmap(bImagem, item.getLocal());
        holder.viewImagem.setStatus("0", null);
        holder.btnImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(iposicao);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagems.size();
    }

    private void removeAt(int position) {
        mImagems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mImagems.size());
    }

    protected class ViewImagHolder extends RecyclerView.ViewHolder {
        private ViewImage viewImagem;
        private Button btnImagem;

        ViewImagHolder(View itemView) {
            super(itemView);
            viewImagem = (ViewImage) itemView.findViewById(R.id.img);
            btnImagem = (Button) itemView.findViewById(R.id.btnRemover);
        }
    }
}