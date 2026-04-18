package com.axys.redeflexmobile.ui.register.list;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.register.list.RegisterListViewHolder.RegisterListAdapterListener;
import com.axys.redeflexmobile.ui.register.list.RegisterListViewHolder.RegisterListViewHolderListener;

/**
 * @author Rogério Massa on 21/11/18.
 */

public class RegisterListAdapter extends BaseAdapter<RegisterListItem, RegisterListViewHolder>
        implements RegisterListAdapterListener {

    private RegisterListViewHolderListener viewListener;
    private Integer opened;

    RegisterListAdapter(RegisterListViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.fragment_main_register_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new RegisterListViewHolder(view, this, viewListener, this);
    }

    @Override
    public boolean isOpened(int position) {
        return opened != null && opened.equals(position);
    }

    @Override
    public void expandReason(int position) {
        if (opened == null) {
            opened = position;
            notifyItemChanged(position);
        } else if (opened.equals(position)) {
            opened = null;
            notifyItemChanged(position);
        } else {
            int lastPosition = opened;
            opened = position;
            notifyItemChanged(lastPosition);
            notifyItemChanged(position);
        }

    }
}
