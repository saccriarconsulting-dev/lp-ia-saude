package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaViewHolder.ListagemSolicitacaoTrocaViewHolderCliente;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaViewHolder.ListagemSolicitacaoTrocaViewHolderData;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaViewHolder.ListagemSolicitacaoTrocaViewHolderListener;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaViewHolder.ListagemSolicitacaoTrocaViewHolderProduto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoVisualizacaoTipo.CLIENTE;
import static com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoVisualizacaoTipo.DATA;

public class ListagemSolicitacaoTrocaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ListagemSolicitacaoTrocaViewHolderListener {

    private Context context;
    private List<ListagemSolicitacaoExibicao> solicitacoes;
    private ListagemSolicitacaoTrocaAdapterListener listener;
    private List<String> abertos;
    private boolean podeRotacionar;

    ListagemSolicitacaoTrocaAdapter(Context context, ListagemSolicitacaoTrocaAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.solicitacoes = new ArrayList<>();
        this.abertos = new ArrayList<>();
    }

    void setSolicitacoes(List<ListagemSolicitacaoExibicao> solicitacoes) {
        this.solicitacoes = solicitacoes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == DATA.valor) {
            return new ListagemSolicitacaoTrocaViewHolderData(LayoutInflater.from(context).inflate(
                    R.layout.activity_solicitacao_troca_item_data, viewGroup, false), this);
        } else if (viewType == CLIENTE.valor) {
            return new ListagemSolicitacaoTrocaViewHolderCliente(LayoutInflater.from(context).inflate(
                    R.layout.activity_solicitacao_troca_item_cliente, viewGroup, false), this);
        }
        return new ListagemSolicitacaoTrocaViewHolderProduto(LayoutInflater.from(context).inflate(
                R.layout.activity_solicitacao_troca_item_produto, viewGroup, false), listener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ListagemSolicitacaoTrocaViewHolderData) {
            ListagemSolicitacaoTrocaViewHolderData holder = (ListagemSolicitacaoTrocaViewHolderData) viewHolder;
            holder.bindData(solicitacoes.get(i));
        } else if (viewHolder instanceof ListagemSolicitacaoTrocaViewHolderCliente) {
            ListagemSolicitacaoTrocaViewHolderCliente holder = (ListagemSolicitacaoTrocaViewHolderCliente) viewHolder;
            holder.bindCliente(solicitacoes.get(i));
        } else {
            ListagemSolicitacaoTrocaViewHolderProduto holder = (ListagemSolicitacaoTrocaViewHolderProduto) viewHolder;
            holder.bindProduto(solicitacoes.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return solicitacoes.size();
    }

    @Override
    public long getItemId(int position) {
        return solicitacoes.get(position).solicitacaoId;
    }

    @Override
    public int getItemViewType(int position) {
        ListagemSolicitacaoExibicao item = solicitacoes.get(position);
        if (item.tipo == DATA) {
            return DATA.valor;
        } else if (item.tipo == CLIENTE) {
            return CLIENTE.valor;
        }
        return super.getItemViewType(position);
    }

    @Override
    public boolean estaAberto(String dataValidador) {
        return abertos != null && abertos.contains(dataValidador);
    }

    @Override
    public void abrirFecharSessao(String dataValidador) {
        this.podeRotacionar = true;

        if (abertos.contains(dataValidador)) {
            abertos.remove(dataValidador);
        } else {
            abertos.add(dataValidador);
        }

        Stream.of(solicitacoes).forEachIndexed((index, listagemSolicitacaoExibicao) -> {
            if (listagemSolicitacaoExibicao.dataValidador.equals(dataValidador)) {
                notifyItemChanged(index);
            }
        });
    }

    @Override
    public boolean podeRotacionar() {
        return podeRotacionar;
    }

    @Override
    public void bloquearRotacionar() {
        this.podeRotacionar = false;
    }

    static class ListagemSolicitacaoExibicao {

        int solicitacaoId;
        String clienteId;
        String clienteNome;
        String produtoId;
        String produtoNome;
        int produtoQuantidade;
        int produtoQuantidadeTrocada;
        int produtoStatusId;
        Date dataSolicitacao;
        Date dataAlteracao;
        String dataValidador;
        ListagemSolicitacaoVisualizacaoTipo tipo;

        ListagemSolicitacaoExibicao(String dataValidador, Date dataSolicitacao) {
            this.dataValidador = dataValidador;
            this.dataSolicitacao = dataSolicitacao;
            this.tipo = ListagemSolicitacaoVisualizacaoTipo.DATA;
        }

        ListagemSolicitacaoExibicao(int solicitacaoId, String dataValidador, String clienteNome) {
            this.solicitacaoId = solicitacaoId;
            this.dataValidador = dataValidador;
            this.clienteNome = clienteNome;
            this.tipo = ListagemSolicitacaoVisualizacaoTipo.CLIENTE;
        }

        ListagemSolicitacaoExibicao(int solicitacaoId,
                                    String clienteId,
                                    String clienteNome,
                                    String produtoId,
                                    String produtoNome,
                                    int produtoQuantidade,
                                    int produtoQuantidadeTrocada,
                                    int produtoStatusId,
                                    Date dataSolicitacao,
                                    Date dataAlteracao,
                                    String dataValidador) {
            this.solicitacaoId = solicitacaoId;
            this.clienteId = clienteId;
            this.clienteNome = clienteNome;
            this.produtoId = produtoId;
            this.produtoNome = produtoNome;
            this.produtoQuantidade = produtoQuantidade;
            this.produtoQuantidadeTrocada = produtoQuantidadeTrocada;
            this.produtoStatusId = produtoStatusId;
            this.dataSolicitacao = dataSolicitacao;
            this.dataAlteracao = dataAlteracao;
            this.dataValidador = dataValidador;
            this.tipo = ListagemSolicitacaoVisualizacaoTipo.PRODUTO;
        }
    }

    enum ListagemSolicitacaoVisualizacaoTipo {
        DATA(1), CLIENTE(2), PRODUTO(3);

        public int valor;

        ListagemSolicitacaoVisualizacaoTipo(int valor) {
            this.valor = valor;
        }
    }

    interface ListagemSolicitacaoTrocaAdapterListener {
        void cancelarSolicitacao(ListagemSolicitacaoExibicao solicitacao);

        void confirmarSolicitacao(ListagemSolicitacaoExibicao solicitacao);
    }
}
