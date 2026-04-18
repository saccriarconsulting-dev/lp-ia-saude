package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem.SelecionarProdutosBipagemAdapter.SelecionarProdutosBipagemAdapterListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rogério Massa on 15/10/18.
 */

public class SelecionarProdutosBipagemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtProduto) TextView tvProduto;
    @BindView(R.id.btnExcluir) ImageButton ibDeletar;
    @BindView(R.id.viewDivider) View viewDivider;

    private SelecionarProdutosBipagemAdapterListener viewListener;
    private SelecionarProdutosBipagemViewHolderListener adapterListener;

    SelecionarProdutosBipagemViewHolder(@NonNull View itemView,
                                        SelecionarProdutosBipagemAdapterListener viewListener,
                                        SelecionarProdutosBipagemViewHolderListener adapterListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.viewListener = viewListener;
        this.adapterListener = adapterListener;
    }

    public void bind(SolicitacaoTrocaCodBarras solicitacaoTrocaCodBarras, int position) {
        if (solicitacaoTrocaCodBarras == null) {
            return;
        }
        tvProduto.setText(solicitacaoTrocaCodBarras.getIccidDe());
        ibDeletar.setOnClickListener((view) -> viewListener.deletarItem(solicitacaoTrocaCodBarras));
        viewDivider.setVisibility(adapterListener.validaUltimo(position) ? View.GONE : View.VISIBLE);
    }

    interface SelecionarProdutosBipagemViewHolderListener {
        boolean validaUltimo(int position);
    }
}
