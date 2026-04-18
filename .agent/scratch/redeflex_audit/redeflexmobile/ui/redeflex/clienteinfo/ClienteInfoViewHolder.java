package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoFragmentType;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author Rogério Massa on 07/11/18.
 */

class ClienteInfoViewHolder {

    interface ClienteInfoViewHolderListener {
        boolean estaAberto(String validador);

        void abrirFechar(String validador);
    }

    static class ClienteInfoViewHolderHeader extends RecyclerView.ViewHolder {

        private static final int EMPTY_INT = 0;

        @BindView(R.id.cliente_info_fragment_header_tv_titulo) TextView tvPrimeiroCampo;
        @BindView(R.id.cliente_info_fragment_header_ll_container) LinearLayout llContainer;
        @BindView(R.id.cliente_info_fragment_header_view_top_divider) View viewTopDivider;
        @BindView(R.id.cliente_info_fragment_header_view_divider) View viewDivider;
        @BindView(R.id.cliente_info_fragment_header_view_shadown) View viewShadown;

        private ClienteInfoViewHolderListener adapterListener;
        private ClienteInfoFragmentType tipoFragmento;

        ClienteInfoViewHolderHeader(@NonNull View itemView,
                                    ClienteInfoViewHolderListener adapterListener,
                                    ClienteInfoFragmentType tipoFragmento) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.adapterListener = adapterListener;
            this.tipoFragmento = tipoFragmento;
        }

        void bind(ClienteInfoItem item) {
            if (item == null) return;
            tvPrimeiroCampo.setText(item.primeiroCampo);
            llContainer.setOnClickListener((view) -> adapterListener.abrirFechar(item.validador));

            viewTopDivider.setVisibility(getAdapterPosition() == EMPTY_INT ? VISIBLE : GONE);

            if (tipoFragmento == ClienteInfoFragmentType.ULTIMA_VENDA) {
                configurarUltimaVenda(item);
            } else {
                configurarPrecoDiferenciado();
            }
            if (TextUtils.isEmpty(item.validador)) {
                configurarMockVenda();
            }
        }

        private void configurarMockVenda() {
            tvPrimeiroCampo.setVisibility(View.INVISIBLE);
            llContainer.setVisibility(View.INVISIBLE);
            viewTopDivider.setVisibility(View.INVISIBLE);
            viewDivider.setVisibility(View.INVISIBLE);
            viewShadown.setVisibility(View.INVISIBLE);
            llContainer.setOnClickListener(null);
        }

        private void configurarPrecoDiferenciado() {
            llContainer.setBackgroundColor(ContextCompat.getColor(llContainer.getContext(),
                    R.color.cliente_info_grey_backgound));
            viewDivider.setVisibility(GONE);
            viewShadown.setVisibility(GONE);
        }

        private void configurarUltimaVenda(ClienteInfoItem item) {
            llContainer.setBackgroundColor(ContextCompat.getColor(llContainer.getContext(),
                    R.color.branco));
            if (adapterListener.estaAberto(item.validador)) {
                viewDivider.setVisibility(GONE);
                viewShadown.setVisibility(VISIBLE);
            } else {
                viewDivider.setVisibility(VISIBLE);
                viewShadown.setVisibility(GONE);
            }
        }
    }

    static class ClienteInfoViewHolderItem extends RecyclerView.ViewHolder {

        @BindView(R.id.cliente_info_fragment_item_tv_primeiro) TextView tvPrimeiroCampo;
        @BindView(R.id.cliente_info_fragment_item_tv_segundo) TextView tvSegundoCampo;
        @BindView(R.id.cliente_info_fragment_item_tv_terceiro) TextView tvTerceiroCampo;
        @BindView(R.id.cliente_info_fragment_item_tv_quarto) TextView tvQuartoCampo;
        @BindView(R.id.cliente_info_fragment_item_tv_quinto) TextView tvQuintoCampo;
        @BindView(R.id.cliente_info_fragment_item_ll_container) LinearLayout llContainer;

        private ClienteInfoViewHolderListener adapterListener;
        private Context context;

        ClienteInfoViewHolderItem(@NonNull View itemView,
                                  ClienteInfoViewHolderListener adapterListener) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            this.adapterListener = adapterListener;
        }

        private static LayoutParams obterTamanhoView(boolean aberto) {
            return new LayoutParams(MATCH_PARENT, aberto ? WRAP_CONTENT : 0);
        }

        void bind(ClienteInfoItem item) {
            if (item == null) {
                return;
            } else if (!adapterListener.estaAberto(item.validador)) {
                llContainer.setLayoutParams(obterTamanhoView(false));
                return;
            }

            tvPrimeiroCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoItem));
            tvSegundoCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoItem));
            tvTerceiroCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoItem));

            if (item.tipo == ClienteInfoFragment.ClienteInfoItemType.HEADER_ITEM) {
                tvPrimeiroCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoHeader));
                tvSegundoCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoHeader));
                tvTerceiroCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoHeader));
                tvQuartoCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoHeader));
                tvQuintoCampo.setTextColor(ContextCompat.getColor(context, R.color.clienteInfoHeader));

                if (TextUtils.isEmpty(item.quartoCampo))
                {
                    tvQuartoCampo.setVisibility(GONE);
                    tvQuintoCampo.setVisibility(GONE);
                }
            }

            llContainer.setLayoutParams(obterTamanhoView(true));
            tvPrimeiroCampo.setText(item.primeiroCampo);
            tvSegundoCampo.setText(item.segundoCampo);
            tvTerceiroCampo.setText(item.terceiroCampo);
            if (TextUtils.isEmpty(item.quartoCampo)) {
                tvQuartoCampo.setVisibility(GONE);
                tvQuintoCampo.setVisibility(GONE);
            }
            else {
                tvQuartoCampo.setText(item.quartoCampo);
                tvQuintoCampo.setText(item.quintoCampo);
            }
        }
    }
}
