package com.axys.redeflexmobile.ui.adquirencia.routes.list;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.ui.adquirencia.routes.list.RoutesProspectListViewHolder.RoutesProspectListViewHolderListener;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

/**
 * @author Rogério Massa on 29/10/18.
 */

public class RoutesProspectListAdapter extends BaseAdapter<RoutesProspect, RoutesProspectListViewHolder> {

    private RoutesProspectListViewHolderListener viewListener;

    RoutesProspectListAdapter(RoutesProspectListViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.activity_routes_prospect_list_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new RoutesProspectListViewHolder(view, this, viewListener);
    }
}
