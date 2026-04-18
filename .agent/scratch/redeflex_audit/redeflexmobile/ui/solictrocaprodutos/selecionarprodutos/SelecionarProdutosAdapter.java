package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosViewHolder.SelecionarProdutosViewHolderEmpty;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosViewHolder.SelecionarProdutosViewHolderHeader;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosViewHolder.SelecionarProdutosViewHolderItem;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosViewHolder.SelecionarProdutosViewHolderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 02/10/2018.
 */

public class SelecionarProdutosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SelecionarProdutosViewHolderListener {

    private static final int HEADER_POSITION = 10;
    private static final int EMPTY_POSITION = 11;

    private Context context;
    private List<SolicitacaoTrocaDetalhes> lista;
    private SelecionarProdutosAdapterListener callback;
    private List<Integer> abertos;
    private boolean permitirRotacionar;

    SelecionarProdutosAdapter(Context context, SelecionarProdutosAdapterListener callback) {
        this.context = context;
        this.initializeList();
        this.callback = callback;
        this.abertos = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER_POSITION) {
            return new SelecionarProdutosViewHolderHeader(LayoutInflater.from(context).inflate(
                    R.layout.activity_selecionar_produtos_troca_list_header, viewGroup, false));
        } else if (viewType == EMPTY_POSITION) {
            return new SelecionarProdutosViewHolderEmpty(LayoutInflater.from(context).inflate(
                    R.layout.activity_selecionar_produtos_troca_list_empty, viewGroup, false));
        }
        return new SelecionarProdutosViewHolderItem(LayoutInflater.from(context).inflate(
                R.layout.activity_selecionar_produtos_troca_list_item, viewGroup, false),
                callback, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelecionarProdutosViewHolderItem) {
            SelecionarProdutosViewHolderItem viewHolder = (SelecionarProdutosViewHolderItem) holder;
            viewHolder.bind(lista.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    @Override
    public int getItemViewType(int position) {
        SolicitacaoTrocaDetalhes objeto = lista.get(position);
        if (position == 0 && objeto.getProdutoCodigo().equals("HEADER")) {
            return HEADER_POSITION;
        } else if (position == 1 && objeto.getProdutoCodigo().equals("EMPTY")) {
            return EMPTY_POSITION;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void abrir(Integer position) {
        this.permitirRotacionar = true;
        if (!abertos.contains(position)) {
            abertos.add(position);
        } else {
            abertos.remove(position);
        }
        notifyItemChanged(position);
    }

    @Override
    public boolean estaAberto(int position) {
        return abertos.contains(position);
    }

    @Override
    public boolean podeRotacionar() {
        return permitirRotacionar;
    }

    private void initializeList() {
        ArrayList<SolicitacaoTrocaDetalhes> produtos = new ArrayList<>();

        SolicitacaoTrocaDetalhes header = new SolicitacaoTrocaDetalhes();
        header.setProdutoCodigo("HEADER");
        produtos.add(header);

        SolicitacaoTrocaDetalhes empty = new SolicitacaoTrocaDetalhes();
        empty.setProdutoCodigo("EMPTY");
        produtos.add(empty);

        this.lista = produtos;
        notifyDataSetChanged();
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    void inserirProdutos(List<SolicitacaoTrocaDetalhes> lista) {
        if (lista == null || lista.isEmpty()) {
            this.initializeList();
            return;
        }

        List<SolicitacaoTrocaDetalhes> itens = new ArrayList<>();
        SolicitacaoTrocaDetalhes objeto = new SolicitacaoTrocaDetalhes();
        objeto.setProdutoCodigo("HEADER");
        itens.add(objeto);

        itens.addAll(lista);
        this.lista = itens;
        notifyDataSetChanged();
    }

    public interface SelecionarProdutosAdapterListener {
        void removerProduto(SolicitacaoTrocaDetalhes produto);
    }
}
