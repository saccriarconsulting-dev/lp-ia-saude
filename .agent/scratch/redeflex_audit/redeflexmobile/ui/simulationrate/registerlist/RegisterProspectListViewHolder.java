package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import android.annotation.SuppressLint;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Lucas Marciano on 30/04/2020
 */
@SuppressLint("NonConstantResourceId")
public class RegisterProspectListViewHolder extends BaseViewHolder<ProspectingClientAcquisition> {

    private final RegisterListViewHolderListener callback;

    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_fantasy_name) TextView tvFantasyName;
    @BindView(R.id.tv_cnpj_number) TextView tvCnpjNumber;
    @BindView(R.id.cvContainer) CardView cvContainer;

    RegisterProspectListViewHolder(View itemView, RegisterListViewHolderListener callback) {
        super(itemView);
        this.callback = callback;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(ProspectingClientAcquisition prospect, int position) {
        super.bind(prospect, position);
        tvName.setText(prospect.getCompleteName());
        tvFantasyName.setText(prospect.getFantasyName());
        tvCnpjNumber.setText(StringUtils.maskCpfCnpj(prospect.getCpfCnpjNumber()));

        cvContainer.setOnClickListener(v -> callback.clickEvent(prospect));
    }

    interface RegisterListViewHolderListener {
        void clickEvent(ProspectingClientAcquisition prospect);
    }

    interface RegisterListAdapterListener {
    }
}
