package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 08/10/18.
 */

public class InformarCodigoBarraAdapter extends RecyclerView.Adapter<InformarCodigoBarraViewHolder> {

    private Context context;
    private List<SolicitacaoTrocaCodBarras> produtos;

    InformarCodigoBarraAdapter(Context context) {
        this.context = context;
        this.produtos = new ArrayList<>();
    }

    public void setProdutos(List<SolicitacaoTrocaCodBarras> produtos) {
        this.produtos = produtos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InformarCodigoBarraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InformarCodigoBarraViewHolder(LayoutInflater.from(context).inflate(
                R.layout.activity_informar_codigo_barra_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InformarCodigoBarraViewHolder holder, int position) {
        holder.bind(produtos.get(position));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
