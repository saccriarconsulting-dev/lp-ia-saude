package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Produto;

import java.util.ArrayList;

/**
 * Created by joao.viana on 01/11/2017.
 */

public class ProdutosVendAdapter extends RecyclerView.Adapter<ProdutosVendAdapter.ViewProdutosVendHolder> {
    private Context mContext;
    private ArrayList<Produto> mLista;

    public ProdutosVendAdapter(Context pContext, ArrayList<Produto> pLista) {
        mContext = pContext;
        mLista = pLista;
    }

    @Override
    public ViewProdutosVendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewProdutosVendHolder(View.inflate(mContext, R.layout.item_resumo_venda, null));
    }

    @Override
    public void onBindViewHolder(ViewProdutosVendHolder holder, int position) {
        Produto produto = mLista.get(position);
        switch (produto.getOperadora()) {
            case 1:
            case 17:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.oi, mContext.getTheme()));
                else
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.oi));
                break;
            case 4:
            case 25:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tim, mContext.getTheme()));
                else
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tim));
                break;
            case 2:
            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.claro, mContext.getTheme()));
                else
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.claro));
                break;
            case 3:
            case 26:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vivo, mContext.getTheme()));
                else
                    holder.imgOperadora.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vivo));
                break;
            default:
                holder.imgOperadora.setVisibility(View.GONE);
                break;
        }
        try {

        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

        holder.txtProduto.setText(produto.getId() + "-" + produto.getNome());
        holder.txtQtd.setText("Qtd.: " + String.valueOf(produto.getQtde()));
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewProdutosVendHolder extends RecyclerView.ViewHolder {
        private TextView txtProduto, txtQtd;
        private ImageView imgOperadora;

        ViewProdutosVendHolder(View view) {
            super(view);
            txtQtd = (TextView) view.findViewById(R.id.txtQuantidadeResumo);
            txtProduto = (TextView) itemView.findViewById(R.id.txtProdutoResumo);
            imgOperadora = (ImageView) itemView.findViewById(R.id.imgOperadoraResumo);
        }
    }
}