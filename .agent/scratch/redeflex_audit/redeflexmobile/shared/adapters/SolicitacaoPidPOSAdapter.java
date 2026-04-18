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
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.DBTipoMaquina;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidPos;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SolicitacaoPidPOSAdapter extends RecyclerView.Adapter<SolicitacaoPidPOSAdapter.ViewSolicitacaoPidPOSHolder>{
    private Context mContext;
    private ArrayList<SolicitacaoPidPos> mLista;
    private SolicitacaoPidPOSAdapter.excluir listener;

    public SolicitacaoPidPOSAdapter(ArrayList<SolicitacaoPidPos> pLista, Context pContext, SolicitacaoPidPOSAdapter.excluir listener) {
        mContext = pContext;
        mLista = pLista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SolicitacaoPidPOSAdapter.ViewSolicitacaoPidPOSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_solicitacaopid_pos, parent, false);
        return new SolicitacaoPidPOSAdapter.ViewSolicitacaoPidPOSHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitacaoPidPOSAdapter.ViewSolicitacaoPidPOSHolder holder, int position) {
        SolicitacaoPidPos solicitacaoPidPOS = mLista.get(position);

        holder.tvModelo.setText(new DBModeloPOS(mContext).getById(solicitacaoPidPOS.getTipoMaquinaId()).getDescricao());
        holder.tvConexao.setText(new DBModeloPOS(mContext).obterTipoConexao(solicitacaoPidPOS.getTipoConexaoId(), solicitacaoPidPOS.getTipoMaquinaId()).getDescription());
        holder.tvOperadora.setText(String.valueOf(new DBOperadora(mContext).getByIdOperadora(solicitacaoPidPOS.getIdOperadora()).getDescricao()));

        // Defina o formato para moeda brasileira
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatoMoeda.format(solicitacaoPidPOS.getValorAluguel());
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

    public class ViewSolicitacaoPidPOSHolder extends RecyclerView.ViewHolder {
        TextView tvModelo, tvConexao, tvOperadora, tvValor;
        ImageButton btnDelete;
        public ViewSolicitacaoPidPOSHolder(@NonNull View itemView) {
            super(itemView);

            tvModelo = itemView.findViewById(R.id.item_pid_tvModeloPOS);
            tvConexao = itemView.findViewById(R.id.item_pid_tvConexao);
            tvOperadora = itemView.findViewById(R.id.item_pid_tvOperadora);
            tvValor = itemView.findViewById(R.id.item_pid_tvValor);
            btnDelete = itemView.findViewById(R.id.item_pid_btndelete);
        }
    }

    public interface excluir {
        void onExcluirClick(int pos);
    }
}
