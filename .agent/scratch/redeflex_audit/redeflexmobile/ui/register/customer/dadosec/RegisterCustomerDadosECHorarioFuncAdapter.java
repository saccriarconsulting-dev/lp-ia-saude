package com.axys.redeflexmobile.ui.register.customer.dadosec;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RegisterCustomerDadosECHorarioFuncAdapter extends Adapter<BaseViewHolder> {

    private RegisterCustomerDadosECHorarioFuncViewHolder.RegisterCustomerDadosECHorarioFuncViewHolderListener callback;
    private List<CustomerRegisterHorarioFunc> list;

    RegisterCustomerDadosECHorarioFuncAdapter(RegisterCustomerDadosECHorarioFuncViewHolder.RegisterCustomerDadosECHorarioFuncViewHolderListener callback)
    {
        this.callback = callback;
        this.list = new ArrayList<>();
    }

    public List<CustomerRegisterHorarioFunc> getList()
    {
        return list;
    }

    public void setList(List<CustomerRegisterHorarioFunc> list) {
        if (list == null) list = new ArrayList<>();
        this.list = new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RegisterCustomerDadosECHorarioFuncViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linha_horariofunc, parent, false),
                callback);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof RegisterCustomerDadosECHorarioFuncViewHolder) {
            RegisterCustomerDadosECHorarioFuncViewHolder holder = (RegisterCustomerDadosECHorarioFuncViewHolder) baseViewHolder;
            holder.bind(list.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
