package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemViewHolder.SelecionarProdutosBipagemViewHolderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 15/10/18.
 */

public class SelecionarProdutosBipagemAdapter extends RecyclerView.Adapter<SelecionarProdutosBipagemViewHolder>
        implements SelecionarProdutosBipagemViewHolderListener {

    private Context context;
    private SelecionarProdutosBipagemAdapterListener listener;
    private List<SolicitacaoTrocaCodBarras> lista;

    SelecionarProdutosBipagemAdapter(Context context, SelecionarProdutosBipagemAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.lista = new ArrayList<>();
    }

    @NonNull
    @Override
    public SelecionarProdutosBipagemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SelecionarProdutosBipagemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.activity_selecionar_produtos_bipagem_item, viewGroup, false), listener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecionarProdutosBipagemViewHolder selecionarProdutosBipagemViewHolder, int i) {
        selecionarProdutosBipagemViewHolder.bind(lista.get(i), i);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void carregarListagem(List<SolicitacaoTrocaCodBarras> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @Override
    public boolean validaUltimo(int position) {
        return position == lista.size() - 1;
    }

    interface SelecionarProdutosBipagemAdapterListener {
        void deletarItem(SolicitacaoTrocaCodBarras codBarras);
    }
}
