package com.axys.redeflexmobile.ui.component.customspinner;

import android.view.View;

import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

/**
 * @author Bruno Pimentel on 23/11/18.
 */
public class CustomSpinnerDialogAdapter extends BaseAdapter<ICustomSpinnerDialogModel, CustomSpinnerDialogViewHolder> {

    public CustomSpinnerDialogAdapter(IBaseViewHolderClickListener<ICustomSpinnerDialogModel> clickListener) {
        super(clickListener);
    }

    @Override
    public int getLayoutItem() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new CustomSpinnerDialogViewHolder(view, clickListener);
    }
}
