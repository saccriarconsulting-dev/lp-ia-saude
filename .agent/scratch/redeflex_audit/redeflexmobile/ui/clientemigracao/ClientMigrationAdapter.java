package com.axys.redeflexmobile.ui.clientemigracao;

import android.view.View;

import com.axys.redeflexmobile.R;

import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationViewHolder.ClientMigrationAdapterListener;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationViewHolder.ClientMigrationViewHolderListener;

/**
 * @author Lucas Marciano on 23/03/2020
 */
public class ClientMigrationAdapter extends BaseAdapter<ClientMigrationResponse, ClientMigrationViewHolder>
        implements ClientMigrationAdapterListener {

    private final ClientMigrationViewHolderListener viewListener;

    ClientMigrationAdapter(ClientMigrationViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.fragment_main_migration_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new ClientMigrationViewHolder(view, viewListener);
    }
}
