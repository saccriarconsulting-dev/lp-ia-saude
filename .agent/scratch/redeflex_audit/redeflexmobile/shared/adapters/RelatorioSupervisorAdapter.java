package com.axys.redeflexmobile.shared.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.RelatorioSupervisorProduto;
import com.axys.redeflexmobile.shared.models.Vendedor;

import java.util.ArrayList;
import java.util.List;

public class RelatorioSupervisorAdapter extends RecyclerView.Adapter<RelatorioSupervisorAdapter.RelatorioViewHolder> {

    private List<Vendedor> vendedores;
    private RelatorioViewHolder relatorioViewHolder;
    private OnItemTouch onItemTouch;

    public RelatorioSupervisorAdapter(OnItemTouch onItemTouch) {
        vendedores = new ArrayList<>();
        this.onItemTouch = onItemTouch;
    }

    @Override
    public RelatorioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(
                R.layout.item_relatorio_supervisor_vendedor,
                parent,
                false
        );
        relatorioViewHolder = new RelatorioViewHolder(itemView, onItemTouch);
        return relatorioViewHolder;
    }

    @Override
    public void onBindViewHolder(RelatorioViewHolder holder, int position) {
        holder.bind(vendedores.get(holder.getAdapterPosition()), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return vendedores.size();
    }

    public void setVendedores(List<Vendedor> vendedores) {
        this.vendedores = vendedores;

        notifyDataSetChanged();
    }

    public void setProdutos(Vendedor vendedor, int position) {
        relatorioViewHolder.popularListaResumo(vendedor);
    }

    public interface OnItemTouch {
        void onTouch(Vendedor vendedor, int position);
    }

    static class RelatorioViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivSeta;
        private TextView tvNome;
        private LinearLayout llConteudo;
        private LinearLayout llItens;
        private LinearLayout llHeader;
        private TextView tvVazio;
        private View view;
        private ProgressBar pbCarregando;
        private OnItemTouch onItemTouch;

        RelatorioViewHolder(View itemView, OnItemTouch onItemTouch) {
            super(itemView);
            this.view = itemView;
            this.onItemTouch = onItemTouch;

            ivSeta = itemView.findViewById(R.id.relatorio_supervisor_vendedor_iv_seta);
            tvNome = itemView.findViewById(R.id.relatorio_supervisor_vendedor_tv_nome);
            llConteudo = itemView.findViewById(R.id.relatorio_supervisor_vendedor_ll_container);
            llItens = itemView.findViewById(R.id.relatorio_supervisor_vendedor_ll_produtos);
            pbCarregando = itemView.findViewById(R.id.relatorio_supervisor_vendedor_pb_carregando);
            tvVazio = itemView.findViewById(R.id.relatorio_supervisor_vendedor_tv_vazio);
            llHeader = itemView.findViewById(R.id.relatorio_supervisor_vendedor_ll_resumo);
        }

        void bind(Vendedor vendedor, int position) {
            llItens.removeAllViews();
            llItens.addView(tvVazio);
            llConteudo.setVisibility(View.VISIBLE);
            llHeader.setVisibility(View.GONE);
            tvVazio.setVisibility(View.GONE);
            pbCarregando.setVisibility(View.GONE);
            ivSeta.animate().rotation(0).start();
            ivSeta.setVisibility(View.VISIBLE);
            llItens.setVisibility(View.GONE);
            llConteudo.setOnClickListener(view -> {
                vendedor.selecionado = !vendedor.selecionado;
                if (!vendedor.atualizado) {
                    mostrarCarregando();
                }
                onItemTouch.onTouch(vendedor, position);
            });

            tvNome.setText(vendedor.vendedor);
            if (vendedor.selecionado) {
                popularListaResumo(vendedor);
                llHeader.setVisibility(View.VISIBLE);
                llItens.setVisibility(View.VISIBLE);
                ivSeta.animate().rotation(180).setDuration(50).start();
                if (vendedor.produtos == null || vendedor.produtos.isEmpty()) {
                    llHeader.setVisibility(View.GONE);
                    tvVazio.setVisibility(View.VISIBLE);
                    tvVazio.setText(
                            vendedor.idVendedor == -1
                                    ? R.string.relatorio_supervisor_vendedor_tv_vazio_resumo
                                    : R.string.relatorio_supervisor_vendedor_tv_vazio
                    );
                }
            }

            if (vendedor.selecionado && vendedor.idVendedor == -1) {
                llConteudo.setVisibility(View.GONE);
                popularListaResumo(vendedor);
                llHeader.setVisibility(View.VISIBLE);
                llItens.setVisibility(View.VISIBLE);
            }
        }

        void mostrarCarregando() {
            pbCarregando.setVisibility(View.VISIBLE);
            ivSeta.setVisibility(View.INVISIBLE);
        }

        void esconderCarregando() {
            pbCarregando.setVisibility(View.GONE);
            ivSeta.setVisibility(View.VISIBLE);
        }

        void popularListaResumo(Vendedor vendedor) {
            if (vendedor.produtos == null || vendedor.produtos.isEmpty()) {
                return;
            }
            List<LinearLayout> views = new ArrayList<>();
            List<RelatorioSupervisorProduto> produtos = vendedor.produtos;
            llItens.removeAllViews();
            for (RelatorioSupervisorProduto produto : produtos) {
                LinearLayout llLista = (LinearLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.item_relatorio_supervisor_produto, null, false);
                ImageView logo = llLista.findViewById(R.id.relatorio_supervisor_produto_iv_logo);
                TextView nome = llLista.findViewById(R.id.relatorio_supervisor_produto_tv_produto);
                TextView quantidade = llLista.findViewById(R.id.relatorio_supervisor_produto_tv_quantidade);

                nome.setText(produto.produto);
                quantidade.setText(String.valueOf(produto.qtde));
                logo.setImageResource(R.drawable.ic_redeflex);
                if (produto.idOperadora == 1) {
                    logo.setImageResource(R.drawable.ic_oi_wrapped);
                }
                if (produto.idOperadora == 2) {
                    logo.setImageResource(R.drawable.ic_claro_wrapped);
                }
                if (produto.idOperadora == 3) {
                    logo.setImageResource(R.drawable.ic_vivo_wrapped);
                }
                if (produto.idOperadora == 4) {
                    logo.setImageResource(R.drawable.ic_tim_wrapped);
                }

                views.add(llLista);
            }

            for (View temp : views) {
                llItens.addView(temp);
            }

            esconderCarregando();
        }
    }
}
