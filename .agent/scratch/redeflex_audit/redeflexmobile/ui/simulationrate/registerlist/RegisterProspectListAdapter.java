package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListViewHolder.RegisterListAdapterListener;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListViewHolder.RegisterListViewHolderListener;

/**
 * @author Lucas Marciano on 30/04/2020
 */
public class RegisterProspectListAdapter extends BaseAdapter<ProspectingClientAcquisition, RegisterProspectListViewHolder>
        implements RegisterListAdapterListener {

    private final RegisterListViewHolderListener viewListener;

    RegisterProspectListAdapter(RegisterListViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.item_list_register_prospecting_client_acquisition;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new RegisterProspectListViewHolder(view, viewListener);
    }
}
