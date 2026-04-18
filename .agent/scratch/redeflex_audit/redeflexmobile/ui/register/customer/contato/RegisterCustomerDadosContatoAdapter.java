package com.axys.redeflexmobile.ui.register.customer.contato;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RegisterCustomerDadosContatoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private RegisterCustomerDadosContatoViewHolder.RegisterCustomerDadosContatoViewHolderListener callback;
    private List<CustomerRegisterContato> list;

    RegisterCustomerDadosContatoAdapter(RegisterCustomerDadosContatoViewHolder.RegisterCustomerDadosContatoViewHolderListener callback)
    {
        this.callback = callback;
        this.list = new ArrayList<>();
    }

    public List<CustomerRegisterContato> getList()
    {
        return list;
    }

    public void setList(List<CustomerRegisterContato> list) {
        if (list == null) list = new ArrayList<>();
        this.list = new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RegisterCustomerDadosContatoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linha_contato, parent, false),
                callback);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof RegisterCustomerDadosContatoViewHolder) {
            RegisterCustomerDadosContatoViewHolder holder = (RegisterCustomerDadosContatoViewHolder) baseViewHolder;
            holder.bind(list.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
