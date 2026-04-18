package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosListAdapter.ClienteInfoPosListItem;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist.ClienteInfoPosListAdapter.ClienteInfoPosListItemTipo.HEADER;

/**
 * @author Rogério Massa on 07/12/18.
 */

class ClienteInfoPosListViewHolder extends RecyclerView.ViewHolder {

    public static final int DELAY_FOR_TRIGGER_EVENT = 1800;
    private final CompositeDisposable compositeDisposable;
    @BindView(R.id.cliente_info_pos_list_item_tv_descricao) TextView tvDescricao;
    @BindView(R.id.cliente_info_pos_list_item_tv_dias) TextView tvDias;
    @BindView(R.id.cliente_info_pos_list_item_ll_content) LinearLayout llContent;
    @BindView(R.id.cliente_info_pos_list_item_iv_seta) ImageView ivSeta;
    private Context context;
    private OnPOSClickListener posClickListener;

    ClienteInfoPosListViewHolder(@NonNull View itemView, CompositeDisposable compositeDisposable, OnPOSClickListener posClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
        this.compositeDisposable = compositeDisposable;
        this.posClickListener = posClickListener;
    }

    void bind(ClienteInfoPosListItem item) {
        if (item == null) {
            return;
        }

        if (item.tipo == HEADER) {
            tvDescricao.setText(configurarLabel(item.posDescricao));
            tvDias.setText(configurarLabel(item.posModelo));
            tvDias.setGravity(Gravity.CENTER);
            ivSeta.setVisibility(View.INVISIBLE);
            return;
        }

        ivSeta.setVisibility(View.VISIBLE);
        tvDescricao.setText(item.posModelo);
        String valorDia = item.dias > 0
                ? context.getResources().getQuantityString(R.plurals.dias, item.dias, item.dias)
                : context.getString(R.string.cliente_info_pos_dias_hoje);
        tvDias.setText(valorDia);

        Disposable disposable = RxView.clicks(llContent)
                .throttleFirst(DELAY_FOR_TRIGGER_EVENT, TimeUnit.MILLISECONDS)
                .subscribe(v -> posClickListener.onClick(item), Timber::e);
        compositeDisposable.add(disposable);
    }

    private SpannableStringBuilder configurarLabel(String label) {
        SpannableStringBuilder builder = new SpannableStringBuilder(label);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.preto)),
                0, builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                0, builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    public interface OnPOSClickListener {
        void onClick(ClienteInfoPosListItem clienteInfoPosListItem);
    }
}
