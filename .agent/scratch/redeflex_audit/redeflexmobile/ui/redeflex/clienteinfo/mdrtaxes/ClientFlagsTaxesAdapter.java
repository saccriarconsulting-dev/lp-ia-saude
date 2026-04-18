package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import java.util.List;

/**
 * @author lucasmarciano on 30/07/20
 */
class ClientFlagsTaxesAdapter extends RecyclerView.Adapter<ClientFlagsTaxesViewHolder> {

    private final List<FlagsBank> list;
    private final ClientFlagsTaxesViewHolder.ClickListener clickListener;
    private int rowIndex = 0;

    public ClientFlagsTaxesAdapter(List<FlagsBank> flagList, ClientFlagsTaxesViewHolder.ClickListener clickListener) {
        this.list = flagList;
        this.clickListener = clickListener;
    }

    @Override
    public @NonNull ClientFlagsTaxesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_commercial_tax_type_flag,
                parent,
                false
        );
        return new ClientFlagsTaxesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientFlagsTaxesViewHolder holder, int index) {
        holder.bind(list.get(index), rowIndex == index);
        holder.setViewHolderClickListener(v -> {
            rowIndex = holder.getAdapterPosition();
            clickListener.clickEvent(list.get(index));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
