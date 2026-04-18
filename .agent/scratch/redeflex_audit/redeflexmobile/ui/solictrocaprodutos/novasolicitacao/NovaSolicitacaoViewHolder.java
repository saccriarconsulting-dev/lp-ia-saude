package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosAdapter.SelecionarProdutosAdapterListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rogério Massa on 02/10/2018.
 */

class NovaSolicitacaoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.nova_solicitacao_item_tv_produto) TextView tvProduto;
    @BindView(R.id.nova_solicitacao_item_tv_quantidade) TextView tvQuantidade;
    @BindView(R.id.nova_solicitacao_item_iv_deletar) ImageView ivDeletar;

    private SelecionarProdutosAdapterListener fragmentCallback;

    NovaSolicitacaoViewHolder(@NonNull View itemView,
                              SelecionarProdutosAdapterListener fragmentCallback) {
        super(itemView);
        this.fragmentCallback = fragmentCallback;
        ButterKnife.bind(this, itemView);
    }

    void bind(SolicitacaoTrocaDetalhes produto) {
        if (produto == null) {
            return;
        }

        tvProduto.setText(produto.getProdutoNome());
        tvQuantidade.setText(String.valueOf(produto.getQtde()));
        ivDeletar.setOnClickListener(view -> fragmentCallback.removerProduto(produto));
    }
}