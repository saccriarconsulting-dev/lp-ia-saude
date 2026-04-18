package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RegisterMigrationPersonalHorarioFuncAdapter extends Adapter<BaseViewHolder> {

    private RegisterMigrationPersonalHorarioFuncViewHolder.RegisterMigrationPersonalHorarioFuncViewHolderListener callback;
    private List<CadastroMigracaoSubHorario> list;

    RegisterMigrationPersonalHorarioFuncAdapter(RegisterMigrationPersonalHorarioFuncViewHolder.RegisterMigrationPersonalHorarioFuncViewHolderListener callback)
    {
        this.callback = callback;
        this.list = new ArrayList<>();
    }

    public List<CadastroMigracaoSubHorario> getList()
    {
        return list;
    }

    public void setList(List<CadastroMigracaoSubHorario> list) {
        if (list == null) list = new ArrayList<>();
        this.list = new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RegisterMigrationPersonalHorarioFuncViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linha_horariofunc, parent, false),
                callback);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof RegisterMigrationPersonalHorarioFuncViewHolder) {
            RegisterMigrationPersonalHorarioFuncViewHolder holder = (RegisterMigrationPersonalHorarioFuncViewHolder) baseViewHolder;
            holder.bind(list.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
