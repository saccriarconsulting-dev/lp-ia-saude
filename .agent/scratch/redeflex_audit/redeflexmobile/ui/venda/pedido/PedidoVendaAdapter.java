package com.axys.redeflexmobile.ui.venda.pedido;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Util_IO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class PedidoVendaAdapter extends RecyclerView.Adapter<PedidoVendaAdapter.BaseVendaViewHolder> {

    private static final int VIEW_TYPE_EMPTY = -2;
    private static final int MINIMO_EMPTY_LIST = 1;

    private List<ItemVendaCombo> produtos = new ArrayList<>();
    private GenericEventPedidoVendaAdapter<ItemVendaCombo> onTouchRemove;
    private EventoPedidoVendaAdapter onTouchRead;
    private GenericEventPedidoVendaAdapter<CodBarra> onTouchRemoveItem;

    @NotNull
    @Override
    public BaseVendaViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedido_venda_item_layout, parent, false);

        if (viewType == VIEW_TYPE_EMPTY) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pedido_venda_item_vazio_layout, parent, false);

            return new PedidoVendaVazioViewHolder(view);
        }

        return new PedidoVendaViewHolder(
                view,
                this,
                onTouchRemove,
                onTouchRead,
                onTouchRemoveItem
        );
    }

    @Override
    public void onBindViewHolder(@NotNull BaseVendaViewHolder holder, int position) {
        if (holder instanceof PedidoVendaViewHolder) {
            ((PedidoVendaViewHolder) holder).bind(produtos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (produtos.isEmpty()) {
            return MINIMO_EMPTY_LIST;
        }

        return produtos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (produtos.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        }

        return super.getItemViewType(position);
    }

    void setOnTouchRemove(GenericEventPedidoVendaAdapter<ItemVendaCombo> onTouchRemove) {
        this.onTouchRemove = onTouchRemove;
    }

    void setOnTouchRead(EventoPedidoVendaAdapter onTouchRead) {
        this.onTouchRead = onTouchRead;
    }

    void setOnTouchRemoveItem(GenericEventPedidoVendaAdapter<CodBarra> onTouchRemoveItem) {
        this.onTouchRemoveItem = onTouchRemoveItem;
    }

    public void addProdutos(ItemVendaCombo item) {
        produtos.add(item);
        notifyItemInserted(produtos.size());
    }

    public void removerProduto(int posicao) {
        produtos.remove(posicao);
        notifyItemRemoved(posicao);
    }

    public ItemVendaCombo obterProduto(int posicao) {
        if (!produtos.isEmpty() && produtos.size() > posicao) {
            return produtos.get(posicao);
        }

        return null;
    }

    public List<ItemVendaCombo> obterProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemVendaCombo> produtos) {
        this.produtos = produtos;

        notifyDataSetChanged();
    }

    abstract static class BaseVendaViewHolder extends RecyclerView.ViewHolder {

        BaseVendaViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class PedidoVendaVazioViewHolder extends BaseVendaViewHolder {
        public PedidoVendaVazioViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class PedidoVendaViewHolder extends BaseVendaViewHolder {

        private static final int NO_VALUES = 0;
        private static final int MAX_PATH = 10000;
        private static final String PROPERTY_NAME = "level";
        private static final int QUALQUER_ITEM = 0;
        private static final int NO_SPACING = 0;
        private static final int LEFT_SPACING = 20;

        @BindView(R.id.pedido_venda_item_ll_container) LinearLayout llContainer;
        @BindView(R.id.pedido_venda_item_tv_produto) AppCompatTextView tvProduto;
        @BindView(R.id.pedido_venda_item_tv_quantidade) AppCompatTextView tvQuantidade;
        @BindView(R.id.pedido_venda_item_tv_serial) AppCompatTextView tvSerial;
        @BindView(R.id.pedido_venda_item_tv_valor) AppCompatTextView tvValor;
        @BindView(R.id.pedido_venda_item_iv_remover) AppCompatImageView ivRemover;
        @BindView(R.id.pedido_venda_item_iv_iccid) AppCompatImageView ivIccid;
        @BindView(R.id.pedido_venda_item_ll_container_iccid) LinearLayout llContainerIccid;
        @BindView(R.id.pedido_venda_item_tv_situacao) AppCompatTextView tvSituacao;

        private final Context context;
        private final LinearLayout llCabecalhoProduto;
        private final PedidoVendaAdapter adapter;
        private final GenericEventPedidoVendaAdapter<ItemVendaCombo> onTouchRemove;
        private final EventoPedidoVendaAdapter onTouchRead;
        private final GenericEventPedidoVendaAdapter<CodBarra> onTouchRemoveItem;

        PedidoVendaViewHolder(View itemView, PedidoVendaAdapter adapter,
                              GenericEventPedidoVendaAdapter<ItemVendaCombo> onTouchRemove,
                              EventoPedidoVendaAdapter onTouchRead,
                              GenericEventPedidoVendaAdapter<CodBarra> onTouchRemoveItem) {
            super(itemView);
            this.onTouchRead = onTouchRead;
            this.onTouchRemoveItem = onTouchRemoveItem;
            ButterKnife.bind(this, itemView);

            this.adapter = adapter;
            this.onTouchRemove = onTouchRemove;
            context = itemView.getContext();
            llCabecalhoProduto = (LinearLayout) LayoutInflater.from(context)
                    .inflate(
                            R.layout.pedido_venda_item_produto_cabecalho_layout,
                            llContainer,
                            false
                    );
        }

        void bind(ItemVendaCombo produto) {
            montarLayoutPadrao(produto);
            apagarEventos();
            iniciarEventos(produto);
            mostrarIccid(produto);
            mostrarCombo(produto);
        }

        private void montarLayoutPadrao(@NonNull ItemVendaCombo produto) {
            ivIccid.setVisibility(View.INVISIBLE);
            tvSerial.setText("-");
            String valor = context.getString(
                    R.string.pedido_venda_tv_valor_unitario,
                    Util_IO.formatDoubleToDecimalNonDivider(produto.getValorUN())
            );
            tvProduto.setText(produto.getNomeProduto());
            tvQuantidade.setText(String.valueOf(produto.getQtde()));
            tvValor.setText(valor);
            llContainerIccid.setVisibility(
                    produto.isViewing() ? View.VISIBLE : View.GONE
            );

            ivIccid.setVisibility(
                    produto.getBipagem().equalsIgnoreCase(ItemVenda.LER_CODIGO_BARRA)
                            && !produto.isCombo()
                            ? View.VISIBLE
                            : View.INVISIBLE
            );

            if (produto.getBipagem().equalsIgnoreCase(ItemVenda.LER_CODIGO_BARRA)) {
                tvSerial.setText("0");
            }

            if (!produto.getCodigosList().isEmpty()) {
                tvSerial.setText(String.valueOf(produto.getQuantidadeSerial()));
            }
            mudarPosicaoSeta(produto);

            tvSituacao.setText(produto.getSituacaosugestaovenda());

            boolean qtdeComboValidator = produto.getQtdCombo() > 0 && produto.getCodigosList() != null
                    && produto.getCodigosList().size() == (produto.getQtdCombo() * produto.getQtde());
            if (qtdeComboValidator) {
                tvSerial.setTextColor(ContextCompat.getColor(context, R.color.botao_ok));
                ivIccid.setVisibility(View.INVISIBLE);
                return;
            }

            ivRemover.setVisibility(View.VISIBLE);
            mudarCorSerial(produto.getQtde(), produto.getQuantidadeSerial());
            esconderIccidPeloSerial(produto.getQtde(), produto.getQuantidadeSerial());
        }

        private void mudarCorSerial(int quantidade, int serial) {
            tvSerial.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            if (quantidade == serial) {
                tvSerial.setTextColor(ContextCompat.getColor(context, R.color.botao_ok));
            }
        }

        private void esconderIccidPeloSerial(int quantidade, int serial) {
            if (quantidade != serial) {
                return;
            }
            ivIccid.setVisibility(View.INVISIBLE);
        }

        private void mudarPosicaoSeta(@NonNull ItemVendaCombo produto) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_animated);
            if (produto.isViewing()) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_animated);
            }
            tvProduto.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
            ObjectAnimator.ofInt(drawable, PROPERTY_NAME, NO_VALUES, MAX_PATH).start();
        }

        private void iniciarEventos(@NonNull ItemVendaCombo produto) {
            if (produto.getBipagem().equalsIgnoreCase(ItemVenda.LER_CODIGO_BARRA)
                    && !produto.getCodigosList().isEmpty()) {
                tvProduto.setOnClickListener(v -> {
                    adapter.notifyItemChanged(getAdapterPosition());
                    mostrarContainer(produto);
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_animated);
                    if (produto.isViewing()) {
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_animated);
                    }
                    tvProduto.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            drawable,
                            null,
                            null,
                            null
                    );
                    ObjectAnimator.ofInt(drawable, PROPERTY_NAME, NO_VALUES, MAX_PATH).start();
                });
            }

            if (onTouchRemove != null) {
                ivRemover.setOnClickListener(
                        v -> onTouchRemove.onTouch(produto, getAdapterPosition())
                );
            }

            if (onTouchRead != null) {
                ivIccid.setOnClickListener(v -> onTouchRead.onTouch(
                        produto.getIdProduto(),
                        String.valueOf(produto.getId()),
                        produto.getQtde(),
                        produto.getQuantidadeSerial(),
                        produto.isCombo(),
                        produto.getIdProduto(),
                        0,
                        0
                ));
            }

        }

        private void apagarEventos() {
            tvProduto.setOnClickListener(null);
            ivRemover.setOnClickListener(null);
            ivIccid.setOnClickListener(null);
        }

        private void mostrarContainer(@NonNull ItemVendaCombo produto) {
            if (produto.getCodigosList().isEmpty()) {
                return;
            }

            produto.changeViewing();

            llContainerIccid.setVisibility(
                    produto.isViewing() ? View.VISIBLE : View.GONE
            );
        }

        private void mostrarCombo(@NonNull ItemVendaCombo produto) {
            if (produto.getCodigosList() == null || produto.getCodigosList().isEmpty()) {
                return;
            }

            if (!produto.isCombo()) {
                return;
            }

            limparConteudoIccid();
            Stream.of(produto.getCodigosList())
                    .groupBy(CodBarra::getIdProduto)
                    .forEach(entries -> montarLayoutItemCombo(entries, produto));
        }

        private void montarLayoutItemCombo(@NonNull Map.Entry<String, List<CodBarra>> entries,
                                           ItemVendaCombo produto) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.pedido_venda_item_layout, llContainer, false);

            AppCompatTextView tvProd = view.findViewById(R.id.pedido_venda_item_tv_produto);
            AppCompatTextView tvSerialCombo = view.findViewById(R.id.pedido_venda_item_tv_serial);
            AppCompatTextView tvQtd = view.findViewById(R.id.pedido_venda_item_tv_quantidade);
            AppCompatImageView ivCci = view.findViewById(R.id.pedido_venda_item_iv_iccid);
            AppCompatImageView ivRemove = view.findViewById(R.id.pedido_venda_item_iv_remover);
            AppCompatTextView tvSituacao = view.findViewById(R.id.pedido_venda_item_tv_situacao);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_animated);
            if (entries.getValue().get(QUALQUER_ITEM).isViewing()) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_animated);
            }

            ObjectAnimator.ofInt(drawable, PROPERTY_NAME, NO_VALUES, MAX_PATH).start();
            tvProd.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
            String nomeProduto = Stream.of(entries.getValue())
                    .filter(cod -> cod.getIdProduto().equalsIgnoreCase(entries.getKey()))
                    .map(CodBarra::getNomeProduto)
                    .findFirst()
                    .orElse(null);

            tvProd.setText(nomeProduto);
            tvProd.setPaddingRelative(LEFT_SPACING, NO_SPACING, NO_SPACING, NO_SPACING);
            if (entries.getValue().get(0).getCodBarraInicial() != null) {
                tvProd.setOnClickListener(v -> {
                    for (CodBarra temp : entries.getValue()) {
                        temp.changeViewing();
                    }
                    adapter.notifyItemChanged(getAdapterPosition());

                    Drawable draw = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_animated);
                    if (entries.getValue().get(0).isViewing()) {
                        draw = ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_animated);
                    }
                    tvProd.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            draw,
                            null,
                            null,
                            null
                    );
                    ObjectAnimator.ofInt(draw, PROPERTY_NAME, NO_VALUES, MAX_PATH).start();
                });
            }

            int quantidade = 0;
            int quantidadeCombo = entries.getValue().get(0).getQuantidadeTemporaria();
            try {
                for (CodBarra temp : entries.getValue()) {
                    quantidade += temp.getCodBarraInicial() == null
                            ? 0
                            : Integer.parseInt(temp.retornaQuantidade(UsoCodBarra.GERAL));
                }
            } catch (NumberFormatException | NullPointerException e) {
                Timber.e(e, "MostrarCombo");
            }
            tvQtd.setText(String.valueOf(quantidadeCombo));
            tvSerialCombo.setText(String.valueOf(quantidade));
            tvSituacao.setText(produto.getSituacaosugestaovenda());

            int finalQuantidade = quantidade;
            ivCci.setOnClickListener(v -> onTouchRead.onTouch(
                    entries.getKey(),
                    String.valueOf(produto.getId()),
                    entries.getValue().get(0).getQuantidadeTemporaria(),
                    finalQuantidade,
                    produto.isCombo(),
                    produto.getIdProduto(),
                    produto.getQtde(),
                    produto.getQuantidadeSerial()
            ));
            ivRemove.setVisibility(View.INVISIBLE);

            tvSerialCombo.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            if (quantidadeCombo == quantidade) {
                tvSerialCombo.setTextColor(ContextCompat.getColor(context, R.color.botao_ok));
                ivCci.setVisibility(View.INVISIBLE);
            }
            montarLayoutItemComboFilho(entries, view);
            llContainerIccid.addView(view);
        }

        private void montarLayoutItemComboFilho(@NonNull Map.Entry<String, List<CodBarra>> entries,
                                                @NonNull View view) {
            LinearLayout temp = view.findViewById(R.id.pedido_venda_item_ll_container_iccid);
            View cabecalho = LayoutInflater.from(context)
                    .inflate(
                            R.layout.pedido_venda_item_produto_cabecalho_layout,
                            llContainer,
                            false
                    );
            temp.addView(cabecalho);

            for (CodBarra item : entries.getValue()) {
                if (item.getCodBarraInicial() == null) {
                    continue;
                }
                View innerView = LayoutInflater.from(context)
                        .inflate(
                                R.layout.pedido_venda_item_produto_layout,
                                llContainerIccid,
                                false
                        );
                AppCompatTextView tvIccidInicial = innerView
                        .findViewById(R.id.pedido_venda_item_produto_tv_iccid_inicial);
                AppCompatTextView tvIccidFinal = innerView
                        .findViewById(R.id.pedido_venda_item_produto_tv_iccid_final);
                AppCompatTextView tvQt = innerView
                        .findViewById(R.id.pedido_venda_item_produto_tv_quantidade);
                AppCompatImageView ivRemoverItem = innerView
                        .findViewById(R.id.pedido_venda_item_produto_iv_remover);

                tvIccidFinal.setText(context.getString(R.string.pedido_venda_item_produto_tv_padrao));

                tvIccidInicial.setText(item.getCodBarraInicial());
                try {
                    tvQt.setText(item.retornaQuantidade(UsoCodBarra.GERAL));
                } catch (Exception e) {
                    tvQt.setText(item.getQuantidadeTemporaria());
                    Timber.e(e);
                }
                if (item.getCodBarraFinal() != null) {
                    tvIccidFinal.setText(item.getCodBarraFinal());
                }

                if (onTouchRemoveItem != null) {
                    ivRemoverItem.setOnClickListener(v -> onTouchRemoveItem.onTouch(
                            item,
                            getAdapterPosition()
                    ));
                }

                temp.setVisibility(item.isViewing() ? View.VISIBLE : View.GONE);
                temp.addView(innerView);
            }
        }

        private void mostrarIccid(@NonNull ItemVendaCombo produto) {
            if (produto.getCodigosList() == null || produto.getCodigosList().isEmpty()) {
                llContainerIccid.setVisibility(View.GONE);
                limparConteudoIccid();
                return;
            }

            if (produto.isCombo()) {
                return;
            }

            limparConteudoIccid();
            adicionarCabecalho();

            Stream.ofNullable(produto.getCodigosList())
                    .forEach(item -> {
                        View view = LayoutInflater.from(context)
                                .inflate(
                                        R.layout.pedido_venda_item_produto_layout,
                                        llContainerIccid,
                                        false
                                );
                        AppCompatTextView tvIccidInicial = view
                                .findViewById(R.id.pedido_venda_item_produto_tv_iccid_inicial);
                        AppCompatTextView tvIccidFinal = view
                                .findViewById(R.id.pedido_venda_item_produto_tv_iccid_final);
                        AppCompatTextView tvQtd = view
                                .findViewById(R.id.pedido_venda_item_produto_tv_quantidade);
                        AppCompatImageView ivRemoverItem = view
                                .findViewById(R.id.pedido_venda_item_produto_iv_remover);

                        tvIccidFinal.setText(context.getString(R.string.pedido_venda_item_produto_tv_padrao));

                        tvIccidInicial.setText(item.getCodBarraInicial());
                        try {
                            tvQtd.setText(item.retornaQuantidade(UsoCodBarra.GERAL));
                        } catch (Exception e) {
                            tvQtd.setText("0");
                            Timber.e(e);
                        }
                        if (item.getCodBarraFinal() != null) {
                            tvIccidFinal.setText(item.getCodBarraFinal());
                        }

                        ivRemoverItem.setVisibility(View.INVISIBLE);
                        if (produto.getQtdCombo() == 0) {
                            ivRemoverItem.setVisibility(View.VISIBLE);
                            if (onTouchRemoveItem != null) {
                                ivRemoverItem.setOnClickListener(v -> onTouchRemoveItem.onTouch(
                                        item,
                                        getAdapterPosition()
                                ));
                            }
                        }

                        llContainerIccid.addView(view);
                    });
        }

        private void limparConteudoIccid() {
            llContainerIccid.removeAllViews();
        }

        private void adicionarCabecalho() {
            llContainerIccid.addView(llCabecalhoProduto);
        }
    }
}
