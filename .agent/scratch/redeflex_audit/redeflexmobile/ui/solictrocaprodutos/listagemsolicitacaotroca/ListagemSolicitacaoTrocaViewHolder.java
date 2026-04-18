package com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumStatusTrocaProduto;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoExibicao;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaAdapter.ListagemSolicitacaoTrocaAdapterListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Rogério Massa on 09/10/18.
 */

class ListagemSolicitacaoTrocaViewHolder {

    private static LinearLayout.LayoutParams obterTamanhoView(Context context, boolean aberto) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, aberto ? WRAP_CONTENT : 0);
        int margin = (int) context.getResources().getDimension(R.dimen.spacing_normal);
        layoutParams.setMargins(margin, 0, margin, 0);
        return layoutParams;
    }

    interface ListagemSolicitacaoTrocaViewHolderListener {
        boolean estaAberto(String dataValidador);

        void abrirFecharSessao(String dataValidador);

        boolean podeRotacionar();

        void bloquearRotacionar();
    }

    static class ListagemSolicitacaoTrocaViewHolderData extends RecyclerView.ViewHolder {

        @BindView(R.id.solicitacao_troca_item_data_ll_container) RelativeLayout rlContainer;
        @BindView(R.id.solicitacao_troca_item_data_iv_seta) ImageView ivSeta;
        @BindView(R.id.solicitaca_troca_item_data_tv_data) TextView tvData;

        private ListagemSolicitacaoTrocaViewHolderListener adapterListener;

        ListagemSolicitacaoTrocaViewHolderData(@NonNull View itemView,
                                               ListagemSolicitacaoTrocaViewHolderListener adapterListener) {
            super(itemView);
            this.adapterListener = adapterListener;
            ButterKnife.bind(this, itemView);
        }

        void bindData(ListagemSolicitacaoExibicao objeto) {
            tvData.setText("");
            if (objeto == null) {
                return;
            }
            tvData.setText(Util_IO.dateToStringBr(objeto.dataSolicitacao));
            rlContainer.setOnClickListener(view -> adapterListener.abrirFecharSessao(objeto.dataValidador));
            this.alterarSeta(adapterListener.estaAberto(objeto.dataValidador));
        }

        private void alterarSeta(boolean aberto) {
            int rotation = aberto ? 180 : 0;

            if (!adapterListener.podeRotacionar()) {
                ivSeta.setRotation(rotation);
                return;
            }
            ivSeta.setRotation(rotation);
            adapterListener.bloquearRotacionar();
        }
    }

    static class ListagemSolicitacaoTrocaViewHolderCliente extends RecyclerView.ViewHolder {

        @BindView(R.id.solicitacao_troca_item_cliente_ll_container) LinearLayout llContainer;
        @BindView(R.id.solicitacao_troca_item_cliente_tv_cliente) TextView tvCliente;

        private ListagemSolicitacaoTrocaViewHolderListener adapterListener;

        ListagemSolicitacaoTrocaViewHolderCliente(@NonNull View itemView,
                                                  ListagemSolicitacaoTrocaViewHolderListener adapterListener) {
            super(itemView);
            this.adapterListener = adapterListener;
            ButterKnife.bind(this, itemView);
        }

        void bindCliente(ListagemSolicitacaoExibicao objeto) {
            tvCliente.setText("");
            if (objeto == null) {
                return;
            } else if (!adapterListener.estaAberto(objeto.dataValidador)) {
                llContainer.setLayoutParams(obterTamanhoView(llContainer.getContext(), false));
                return;
            }

            llContainer.setLayoutParams(obterTamanhoView(llContainer.getContext(), true));
            llContainer.setVisibility(View.VISIBLE);
            tvCliente.setText(objeto.clienteNome);
        }
    }

    static class ListagemSolicitacaoTrocaViewHolderProduto extends RecyclerView.ViewHolder {

        @BindView(R.id.solicitacao_troca_item_ll_container) LinearLayout llContainer;
        @BindView(R.id.solicitacao_troca_item_tv_produto) TextView tvProduto;
        @BindView(R.id.solicitacao_troca_item_tv_quantidade) TextView tvQuantidade;
        @BindView(R.id.solicitacao_troca_item_tv_status) TextView tvStatus;
        @BindView(R.id.solicitacao_troca_item_ll_aprovado) LinearLayout llAprovado;
        @BindView(R.id.solicitacao_troca_item_iv_cancelar) ImageView ivCancelar;
        @BindView(R.id.solicitacao_troca_item_iv_aprovar) ImageView ivAprovar;

        private ListagemSolicitacaoTrocaAdapterListener listener;
        private ListagemSolicitacaoTrocaViewHolderListener adapterListener;

        ListagemSolicitacaoTrocaViewHolderProduto(@NonNull View itemView,
                                                  @NonNull ListagemSolicitacaoTrocaAdapterListener listener,
                                                  ListagemSolicitacaoTrocaViewHolderListener adapterListener) {
            super(itemView);
            this.listener = listener;
            this.adapterListener = adapterListener;
            ButterKnife.bind(this, itemView);
        }

        void bindProduto(ListagemSolicitacaoExibicao objeto) {
            this.limparCampos();

            if (objeto == null) {
                return;
            } else if (!adapterListener.estaAberto(objeto.dataValidador)) {
                llContainer.setLayoutParams(obterTamanhoView(llContainer.getContext(), false));
                return;
            }

            llContainer.setLayoutParams(obterTamanhoView(llContainer.getContext(), true));
            tvProduto.setText(objeto.produtoNome);
            tvQuantidade.setText(String.valueOf(objeto.produtoQuantidade));
            this.configurarStatus(objeto);
        }

        private void configurarStatus(ListagemSolicitacaoExibicao objeto) {
            tvStatus.setVisibility(View.GONE);
            llAprovado.setVisibility(View.GONE);

            if (objeto.produtoStatusId == EnumStatusTrocaProduto.ANALISE.valor) {
                prepararAnalise();
            } else if (objeto.produtoStatusId == EnumStatusTrocaProduto.REPROVADO.valor) {
                prepararReprovado();
            } else if (objeto.produtoStatusId == EnumStatusTrocaProduto.APROVADO.valor) {
                prepararAprovado(objeto);
            } else if (objeto.produtoStatusId == EnumStatusTrocaProduto.CANCELADO.valor) {
                prepararCancelado(objeto);
            } else if (objeto.produtoStatusId == EnumStatusTrocaProduto.CONCLUIDO.valor) {
                prepararConcluido(objeto);
            }
        }

        private void prepararAnalise() {
            tvStatus.setVisibility(View.VISIBLE);
            SpannableString span = new SpannableString("EM ANÁLISE");
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, span.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            tvStatus.setText(span);
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(),
                    R.color.solicitacaoTrocaStatusAnalise));
        }

        private void prepararReprovado() {
            tvStatus.setVisibility(View.VISIBLE);
            SpannableString span = new SpannableString("REPROVADO");
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, span.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            tvStatus.setText(span);
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(),
                    R.color.solicitacaoTrocaStatusReprovado));
        }

        private void prepararAprovado(ListagemSolicitacaoExibicao objeto) {
            llAprovado.setVisibility(View.VISIBLE);
            ivCancelar.setOnClickListener(view -> listener.cancelarSolicitacao(objeto));
            ivAprovar.setOnClickListener(view -> listener.confirmarSolicitacao(objeto));
        }

        private void prepararCancelado(ListagemSolicitacaoExibicao objeto) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(obterTextoStatus(EnumStatusTrocaProduto.CANCELADO,
                    Util_IO.dateToStringBr(objeto.dataAlteracao),
                    Util_IO.timeToString(objeto.dataAlteracao)));
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(),
                    R.color.solicitacaoTrocaStatusCancelado));
        }

        private void prepararConcluido(ListagemSolicitacaoExibicao objeto) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(obterTextoStatus(EnumStatusTrocaProduto.CONCLUIDO,
                    String.valueOf(objeto.produtoQuantidadeTrocada),
                    String.valueOf(objeto.produtoQuantidade)));
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(),
                    R.color.solicitacaoTrocaStatusAprovado));
        }

        private SpannableStringBuilder obterTextoStatus(EnumStatusTrocaProduto status, String texto1, String texto2) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            if (status == EnumStatusTrocaProduto.CONCLUIDO) {
                SpannableString span1 = new SpannableString("TROCADO");
                span1.setSpan(new StyleSpan(Typeface.BOLD), 0, span1.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(span1);
                builder.append(String.format("\n %s de %s", texto1, texto2));
                return builder;
            }
            int sizeSmall = tvStatus.getContext().getResources().getDimensionPixelSize(R.dimen.text_small);
            SpannableString span1 = new SpannableString("CANCELADO");
            span1.setSpan(new StyleSpan(Typeface.BOLD), 0, span1.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(span1);
            SpannableString span2 = new SpannableString(String.format("\n %s às %s", texto1, texto2));
            span2.setSpan(new AbsoluteSizeSpan(sizeSmall), 0, span2.length(), SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(span2);
            return builder;
        }

        private void limparCampos() {
            tvProduto.setText("");
            tvQuantidade.setText("");
            tvStatus.setText("");
            tvStatus.setVisibility(View.GONE);
            llAprovado.setVisibility(View.GONE);
        }
    }
}
