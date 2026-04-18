package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosAdapter.SelecionarProdutosAdapterListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.content.ContextCompat.getColor;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Rogério Massa on 02/10/2018.
 */

public class SelecionarProdutosViewHolder {

    public static class SelecionarProdutosViewHolderHeader extends RecyclerView.ViewHolder {

        @BindView(R.id.selecionar_produto_troca_list_header_divider) View divider;

        public SelecionarProdutosViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void removeDivider() {
            divider.setVisibility(View.GONE);
        }
    }

    static class SelecionarProdutosViewHolderEmpty extends RecyclerView.ViewHolder {

        SelecionarProdutosViewHolderEmpty(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SelecionarProdutosViewHolderItem extends RecyclerView.ViewHolder {

        @BindView(R.id.selecionar_produto_item_ll_cabecalho) LinearLayout llCabecalho;
        @BindView(R.id.selecionar_produto_item_iv_seta) ImageView ivSeta;
        @BindView(R.id.selecionar_produto_item_tv_produto) TextView tvProduto;
        @BindView(R.id.selecionar_produto_item_tv_quantidade) TextView tvQuantidade;
        @BindView(R.id.selecionar_produto_item_iv_deletar) ImageView ivDeletar;
        @BindView(R.id.selecionar_produto_item_ll_conteudo) LinearLayout llConteudo;

        private SelecionarProdutosAdapterListener fragmentCallback;
        private SelecionarProdutosViewHolderListener adapterCallback;

        SelecionarProdutosViewHolderItem(@NonNull View itemView,
                                         SelecionarProdutosAdapterListener fragmentCallback,
                                         SelecionarProdutosViewHolderListener adapterCallback) {
            super(itemView);
            this.fragmentCallback = fragmentCallback;
            this.adapterCallback = adapterCallback;
            ButterKnife.bind(this, itemView);
        }

        void bind(SolicitacaoTrocaDetalhes produto, int posicao) {
            if (produto == null) {
                return;
            }

            tvProduto.setText(produto.getProdutoNome());
            tvQuantidade.setText(String.valueOf(produto.getQtde()));

            ivDeletar.setOnClickListener(view -> fragmentCallback.removerProduto(produto));

            if (produto.getIccids() != null && !produto.getIccids().isEmpty()) {
                llCabecalho.setOnClickListener(view -> adapterCallback.abrir(posicao));
                this.preencherIccIds(produto.getIccids());
                this.expandir(posicao);
                return;
            }

            ivSeta.setVisibility(View.INVISIBLE);
            llConteudo.setVisibility(View.GONE);
        }

        private void preencherIccIds(List<SolicitacaoTrocaCodBarras> iccIds) {
            llConteudo.removeAllViews();
            if (iccIds == null || iccIds.isEmpty()) {
                return;
            }

            for (SolicitacaoTrocaCodBarras iccid : iccIds) {
                TextView textView = new TextView(llConteudo.getContext());
                textView.setText(iccid.getIccidDe());
                textView.setTextColor(getColor(llConteudo.getContext(), R.color.textoCinzaMedio));
                llConteudo.addView(textView);
            }
        }

        private void expandir(int posicao) {

            boolean estaAberto = adapterCallback.estaAberto(posicao);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT,
                    estaAberto ? WRAP_CONTENT : 0);
            llConteudo.setVisibility(View.VISIBLE);
            llConteudo.setLayoutParams(layoutParams);

            if (estaAberto) {
                this.alterarSeta(false);
            } else {
                this.alterarSeta(true);
            }
        }

        private void alterarSeta(boolean aberto) {
            int rotation = aberto ? 0 : 180;

            if (!adapterCallback.podeRotacionar()) {
                ivSeta.setRotation(rotation);
                return;
            }
            ivSeta.setRotation(rotation);
        }
    }

    public interface SelecionarProdutosViewHolderListener {
        void abrir(Integer position);

        boolean estaAberto(int position);

        boolean podeRotacionar();
    }
}
