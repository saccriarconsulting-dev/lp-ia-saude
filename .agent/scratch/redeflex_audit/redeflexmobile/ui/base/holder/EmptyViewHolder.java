package com.axys.redeflexmobile.ui.base.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject.ICON_RESOURCE_ZERO;


/**
 * @author Vitor Otero on 18/10/17.
 */

public class EmptyViewHolder extends BaseViewHolder<EmptyListObject> {

    @BindView(R.id.cpt_empty_list_iv) ImageView ivIcon;
    @BindView(R.id.cpt_empty_list_title) TextView tvTitle;
    @BindView(R.id.cpt_empty_list_text) TextView tvMessage;
    @BindView(R.id.cpt_empty_list_button) Button btnTryAgain;

    public EmptyViewHolder(View itemView, EmptyListObject emptyListObject) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        bind(emptyListObject);
    }

    @Override
    public void bind(EmptyListObject emptyListObject) {
        if (emptyListObject == null) {
            return;
        }
        tvTitle.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.GONE);

        if (emptyListObject.getIcon() != ICON_RESOURCE_ZERO) {
            ivIcon.setImageResource(emptyListObject.getIcon());
        }

        if (emptyListObject.getTitle() != null && !emptyListObject.getTitle().isEmpty()) {
            tvTitle.setText(emptyListObject.getTitle());
            tvTitle.setVisibility(View.VISIBLE);
        }

        if (emptyListObject.getMessage() != null && !emptyListObject.getMessage().isEmpty()) {
            tvMessage.setText(emptyListObject.getMessage());
            tvMessage.setVisibility(View.VISIBLE);
        }

        if (emptyListObject.getButtonText() != null) {
            btnTryAgain.setText(emptyListObject.getButtonText());
            btnTryAgain.setVisibility(View.VISIBLE);
        }

        if (emptyListObject.getButtonCallback() != null) {
            btnTryAgain.setOnClickListener(emptyListObject.getButtonCallback());
            btnTryAgain.setVisibility(View.VISIBLE);
        }
    }
}