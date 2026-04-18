package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SolicitacaoPidRedeAdapter extends RecyclerView.Adapter<SolicitacaoPidRedeAdapter.ViewSolicitacaoPidRedeHolder>{
    private Context mContext;
    private ArrayList<SolicitacaoPidRede> mLista;
    private SolicitacaoPidRedeAdapter.excluir listener;

    public SolicitacaoPidRedeAdapter(ArrayList<SolicitacaoPidRede> pLista, Context pContext, SolicitacaoPidRedeAdapter.excluir listener) {
        mContext = pContext;
        mLista = pLista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SolicitacaoPidRedeAdapter.ViewSolicitacaoPidRedeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_solicitacaopid_rede, parent, false);
        return new SolicitacaoPidRedeAdapter.ViewSolicitacaoPidRedeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitacaoPidRedeAdapter.ViewSolicitacaoPidRedeHolder holder, int position) {
        SolicitacaoPidRede solicitacaoPidRede = mLista.get(position);

        holder.tvCnpjCpf.setText(solicitacaoPidRede.getCpfCnpj());
        holder.tvMcc.setText(solicitacaoPidRede.getMcc());
        holder.tvTpv.setText(String.valueOf(solicitacaoPidRede.getTpvPorcentagem()));

        // Defina o formato para moeda brasileira
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatoMoeda.format(solicitacaoPidRede.getTpvTotal());
        holder.tvValor.setText(valorFormatado);

        // Excluir Item
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onExcluirClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public class ViewSolicitacaoPidRedeHolder extends RecyclerView.ViewHolder {
        TextView tvCnpjCpf, tvMcc, tvTpv, tvValor;
        ImageButton btnDelete;
        public ViewSolicitacaoPidRedeHolder(@NonNull View itemView) {
            super(itemView);

            tvCnpjCpf = itemView.findViewById(R.id.item_pid_tvCnpjCpf);
            tvMcc = itemView.findViewById(R.id.item_pid_tvMcc);
            tvTpv = itemView.findViewById(R.id.item_pid_tvTpv);
            tvValor = itemView.findViewById(R.id.item_pid_tvValor);
            btnDelete = itemView.findViewById(R.id.item_pid_btndelete);
        }
    }

    public interface excluir {
        void onExcluirClick(int pos);
    }
}
