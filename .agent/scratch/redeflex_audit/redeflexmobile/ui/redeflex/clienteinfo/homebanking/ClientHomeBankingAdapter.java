package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

/**
 * @author lucasmarciano on 01/07/20
 */
public class ClientHomeBankingAdapter extends BaseAdapter<ClientHomeBanking, ClientHomeBankingViewHolder> {

    @Override
    public int getLayoutItem() {
        return R.layout.item_home_bank;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new ClientHomeBankingViewHolder(view);
    }
}
