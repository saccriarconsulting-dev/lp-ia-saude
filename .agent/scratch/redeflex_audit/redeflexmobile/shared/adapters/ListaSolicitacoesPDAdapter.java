package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumSituacaoPrecoDif;
import com.axys.redeflexmobile.shared.models.SituacaoSolicitacao;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.InputText;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.SolicitacaoPrecoDifActivity;
import com.axys.redeflexmobile.ui.redeflex.SolicitacaoPrecoDifDetalheActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaSolicitacoesPDAdapter extends RecyclerView.Adapter<ListaSolicitacoesPDAdapter.ListaSolicitacoesPDHolder> {
    private final Context mContext;
    private final ArrayList<SolicitacaoPrecoDiferenciado> mLista; // ??

    public ListaSolicitacoesPDAdapter(Context pContext, ArrayList<SolicitacaoPrecoDiferenciado> pLista) {
        mContext = pContext;
        mLista = pLista;
    }

    @Override
    public ListaSolicitacoesPDHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_consultar_sol_preco_dif, parent, false);
        return new ListaSolicitacoesPDHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListaSolicitacoesPDHolder holder, int position) {
        SolicitacaoPrecoDiferenciado situacaoSolicitacao = mLista.get(position);

        if (situacaoSolicitacao.getIdServerSolicitacao() != 0)
            holder.idConsulta.setText("ID - " + situacaoSolicitacao.getIdServerSolicitacao());
        else
            holder.idConsulta.setText("ID - " + situacaoSolicitacao.getId());

        holder.dataSolicitacao.setText("Data da Solicitação: " + Util_IO.dateTimeToString(situacaoSolicitacao.getDataSolicitacao(), Config.FormatDateStringBr));
        holder.dataInicial.setText("Data Inicial: " + Util_IO.dateTimeToString(situacaoSolicitacao.getDataInicial(), Config.FormatDateStringBr));
        holder.dataFinal.setText("Data Inicial: " + Util_IO.dateTimeToString(situacaoSolicitacao.getDataFinal(), Config.FormatDateStringBr));

        // TODO tratar depois ambos casos para converter no nome
        holder.solicitante.setText("Solicitante: " +situacaoSolicitacao.getNomeSolicitante());

        if (situacaoSolicitacao.getSituacaoId() > 0)
            holder.situacao.setText("Situação: " + EnumSituacaoPrecoDif.getPorId(situacaoSolicitacao.getSituacaoId()).descricao);
        else if (situacaoSolicitacao.getSync() == 1)
            holder.situacao.setText("Situação: Solicitação Sincronizada");
        else
            holder.situacao.setText("Situação: Pendente de Sincronização");
        holder.botaoVizualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent fDetalhes = new Intent(mContext, SolicitacaoPrecoDifDetalheActivity.class);
                    fDetalhes.putExtra("SolicitacaoPD", (Serializable) situacaoSolicitacao);
                    ((Activity) mContext).startActivityForResult(fDetalhes, 0);
                } catch (Exception ex) {
                    Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "Erro: " + ex.getMessage());
                    alerta.show();
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ListaSolicitacoesPDHolder extends RecyclerView.ViewHolder {

        TextView idConsulta, situacao, dataSolicitacao, dataInicial, dataFinal, solicitante;
        TextView botaoVizualizar;

        ListaSolicitacoesPDHolder(View itemView) {
            super(itemView);
            idConsulta = itemView.findViewById(R.id.item_PD_tvIdSolicitacao);
            situacao = itemView.findViewById(R.id.item_PD_tvSituacao);
            dataSolicitacao = itemView.findViewById(R.id.item_PD_tvDataSolicitacao);
            dataInicial = itemView.findViewById(R.id.item_PD_tvDataInicial);
            dataFinal = itemView.findViewById(R.id.item_PD_tvDataFinal);
            solicitante = itemView.findViewById(R.id.item_PD_tvSolicitante);
            botaoVizualizar = itemView.findViewById(R.id.item_PD_tvVisualizar);
        }
    }
}