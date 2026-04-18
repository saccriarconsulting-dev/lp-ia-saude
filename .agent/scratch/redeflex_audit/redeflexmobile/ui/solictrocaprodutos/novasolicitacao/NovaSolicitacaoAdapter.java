package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosAdapter.SelecionarProdutosAdapterListener;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosViewHolder.SelecionarProdutosViewHolderHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 02/10/2018.
 */

public class NovaSolicitacaoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FIRST_POSITION = 0;
    private static final int HEADER_POSITION = 1;

    private Context context;
    private List<SolicitacaoTrocaDetalhes> lista;
    private SelecionarProdutosAdapterListener callback;

    NovaSolicitacaoAdapter(Context context, SelecionarProdutosAdapterListener callback) {
        this.context = context;
        this.initializeList();
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER_POSITION) {
            return new SelecionarProdutosViewHolderHeader(LayoutInflater.from(context).inflate(
                    R.layout.activity_selecionar_produtos_troca_list_header, viewGroup, false));
        }
        return new NovaSolicitacaoViewHolder(LayoutInflater.from(context).inflate(
                R.layout.activity_nova_solicitacao_troca_item, viewGroup, false), callback);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NovaSolicitacaoViewHolder) {
            NovaSolicitacaoViewHolder viewHolder = (NovaSolicitacaoViewHolder) holder;
            viewHolder.bind(lista.get(holder.getAdapterPosition()));
        } else if (holder instanceof SelecionarProdutosViewHolderHeader) {
            SelecionarProdutosViewHolderHeader viewHolder = (SelecionarProdutosViewHolderHeader) holder;
            viewHolder.removeDivider();
        }
    }

    @Override
    public int getItemCount() {
        return this.lista.size() > HEADER_POSITION ? this.lista.size() : FIRST_POSITION;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == FIRST_POSITION) {
            return HEADER_POSITION;
        }
        return super.getItemViewType(position);
    }

    private void initializeList() {
        ArrayList<SolicitacaoTrocaDetalhes> produtos = new ArrayList<>();
        produtos.add(new SolicitacaoTrocaDetalhes());
        this.lista = produtos;
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    void inserirProdutos(List<SolicitacaoTrocaDetalhes> lista) {
        if (lista != null && !lista.isEmpty()) {
            List<SolicitacaoTrocaDetalhes> itens = new ArrayList<>();
            itens.add(new SolicitacaoTrocaDetalhes());
            itens.addAll(lista);

            this.lista = itens;
            notifyDataSetChanged();
            return;
        }
        this.initializeList();
    }

    void inserirProduto(SolicitacaoTrocaDetalhes produto) {
        final Integer[] posicao = {null};
        Stream.of(this.lista).forEachIndexed((index, produto1) -> {
            if (produto1.getProdutoCodigo().equals(produto.getProdutoCodigo())) {
                posicao[0] = index;
            }
        });

        if (posicao[0] != null) {
            this.lista.set(posicao[0], produto);
            notifyItemChanged(posicao[0]);
        } else {
            this.lista.add(produto);
            notifyItemInserted(this.lista.size() - 1);
        }
    }

    void removerProduto(SolicitacaoTrocaDetalhes produto) {
        Stream.of(this.lista).forEachIndexed((index, produto1) -> {
            if (produto1.getProdutoCodigo().equals(produto.getProdutoCodigo())) {
                NovaSolicitacaoAdapter.this.lista.remove(index);
                notifyItemRemoved(index);
            }
        });
    }
}
