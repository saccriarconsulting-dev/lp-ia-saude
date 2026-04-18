package com.axys.redeflexmobile.ui.component.customspinner;

import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.ui.base.BaseAdapter.IBaseViewHolderClickListener;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Bruno Pimentel on 23/11/18.
 */
public class CustomSpinnerDialogViewHolder extends BaseViewHolder<ICustomSpinnerDialogModel> {

    @BindView(android.R.id.text1) TextView tvItem;

    private IBaseViewHolderClickListener<ICustomSpinnerDialogModel> clickListener;
    private View view;

    public CustomSpinnerDialogViewHolder(View itemView, IBaseViewHolderClickListener<ICustomSpinnerDialogModel> clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        this.view = itemView;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(ICustomSpinnerDialogModel customSpinnerDialogModel, int position) {
        super.bind(customSpinnerDialogModel, position);
        tvItem.setText(customSpinnerDialogModel.getDescriptionValue());
        view.setOnClickListener(view1 -> clickListener.onClickListener(customSpinnerDialogModel, view1));
    }
}
