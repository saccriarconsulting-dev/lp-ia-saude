package com.axys.redeflexmobile.ui.cartaoponto;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CartaoPonto;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Denis Gasparoto on 03/05/2019.
 */

public class CartaoPontoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_cartao_ponto_ic_fingerprint) ImageView ivFingerprint;
    @BindView(R.id.item_cartao_ponto_descricao) TextView tvDescricao;
    @BindView(R.id.item_cartao_ponto_horario) TextView tvHorario;

    CartaoPontoViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CartaoPonto cartaoPonto) {
        if (cartaoPonto == null) {
            return;
        }

        int position = getAdapterPosition();

        ivFingerprint.setBackground(ContextCompat.getDrawable(ivFingerprint.getContext(),
                R.drawable.ic_fingerprint));
        tvDescricao.setText(tvDescricao.getContext().getString(R.string.cartao_ponto_item_quantity,
                ++position));
        tvHorario.setText(Util_IO.dateTimeToString(cartaoPonto.getHorario(),
                Config.FormatHoraMinutoString));
    }
}
