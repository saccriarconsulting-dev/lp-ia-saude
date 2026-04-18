package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rogério Massa on 08/10/18.
 */

public class InformarCodigoBarraViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.informar_codigo_barra_item_tv_antigo) TextView tvAntigo;
    @BindView(R.id.informar_codigo_barra_item_tv_novo) TextView tvNovo;

    InformarCodigoBarraViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(SolicitacaoTrocaCodBarras solicitacaoTrocaCodBarras) {
        if (solicitacaoTrocaCodBarras == null) {
            return;
        }
        tvAntigo.setText(obterCodigoBarra("Antigo: ", solicitacaoTrocaCodBarras.getIccidDe()));
        tvNovo.setText(obterCodigoBarra("Novo: ", solicitacaoTrocaCodBarras.getIccidPara()));
    }

    private SpannableStringBuilder obterCodigoBarra(String label, String codigoBarra) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString labelString = new SpannableString(label);
        labelString.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(labelString);
        builder.append(codigoBarra != null ? codigoBarra : "");
        return builder;
    }
}
