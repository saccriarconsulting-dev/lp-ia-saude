package com.axys.redeflexmobile.shared.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumStatusPID;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;

import java.util.ArrayList;

public class SolicitacaoPidAdapter extends RecyclerView.Adapter<SolicitacaoPidAdapter.ViewSolicitacaoPidHolder> {
    private Context mContext;
    private ArrayList<SolicitacaoPid> mLista;

    public SolicitacaoPidAdapter(ArrayList<SolicitacaoPid> pLista, Context pContext) {
        mContext = pContext;
        mLista = pLista;
    }

    @NonNull
    @Override
    public SolicitacaoPidAdapter.ViewSolicitacaoPidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_solicitacaopid, parent, false);
        return new SolicitacaoPidAdapter.ViewSolicitacaoPidHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitacaoPidAdapter.ViewSolicitacaoPidHolder holder, int position) {
        SolicitacaoPid solicitacaoPid = mLista.get(position);
        if (!Util_IO.isNullOrEmpty(solicitacaoPid.getCodigoSGV())) {
            holder.tv_cliente.setText(solicitacaoPid.getCodigoSGV() + " - " + solicitacaoPid.getNomeFantasia());
            holder.tv_tipoCliente.setText("Renegociação");
        } else {
            holder.tv_cliente.setText(solicitacaoPid.getNomeFantasia());
            holder.tv_tipoCliente.setText("Negociação");
        }

        holder.tv_id.setText("Id: " + solicitacaoPid.getId());
        holder.tv_dataEmissao.setText("Data Pedido: " + Util_IO.dateToStringBr(solicitacaoPid.getDataCadastro()));

        if (solicitacaoPid.getCpfCnpj() != null && solicitacaoPid.getCpfCnpj().length() > 11)
            holder.tv_CpfCnpj.setText("CNPJ: " + StringUtils.maskCpfCnpj(solicitacaoPid.getCpfCnpj()));
        else
            holder.tv_CpfCnpj.setText("CPF: " + StringUtils.maskCpfCnpj(solicitacaoPid.getCpfCnpj()));

        holder.tv_dataAvaliacao.setText("Data Alteração: " + Util_IO.dateToStringBr(solicitacaoPid.getDataAvaliacao()));

        if (solicitacaoPid.getStatus() != null) {
            holder.tv_status.setText(EnumStatusPID.getPorId(solicitacaoPid.getStatus()).descricao);
            holder.ll_status.setBackgroundResource(EnumStatusPID.getPorId(solicitacaoPid.getStatus()).background);
        }

        if (solicitacaoPid.getSincronizado() == 1) {
            holder.tv_IdServer.setVisibility(View.VISIBLE);
            holder.tv_IdServer.setText("Id Server: " + solicitacaoPid.getId_Server());
            holder.tv_Integrado.setText("Integrado");
        } else {
            holder.tv_IdServer.setVisibility(View.GONE);
            holder.tv_Integrado.setText("Não Integrado");
        }

        // Status com os quais pódem ser gerados Propostas
        if (solicitacaoPid.getStatus().equals("APV") || solicitacaoPid.getStatus().equals("APS") || solicitacaoPid.getStatus().equals("APG") ||
                solicitacaoPid.getStatus().equals("APD") || solicitacaoPid.getStatus().equals("APT"))
            holder.btnGerarProposta.setVisibility(View.VISIBLE);
        else
            holder.btnGerarProposta.setVisibility(View.GONE);

        holder.btnGerarProposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterCustomerActivity.class);
                intent.putExtra("IDSolicitacaoPid", solicitacaoPid.getId_Server());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    public class ViewSolicitacaoPidHolder extends RecyclerView.ViewHolder {
        TextView tv_cliente, tv_id, tv_dataEmissao, tv_CpfCnpj, tv_dataAvaliacao, tv_status, tv_tipoCliente;
        TextView tv_IdServer, tv_Integrado, btnGerarProposta;
        LinearLayout ll_status;

        public ViewSolicitacaoPidHolder(@NonNull View itemView) {
            super(itemView);
            tv_cliente = itemView.findViewById(R.id.solPid_item_tv_cliente);
            tv_id = itemView.findViewById(R.id.solPid_item_tv_id);
            tv_dataEmissao = itemView.findViewById(R.id.solPid_item_tv_dataEmissao);
            tv_CpfCnpj = itemView.findViewById(R.id.solPid_item_tv_CpfCnpj);
            tv_dataAvaliacao = itemView.findViewById(R.id.solPid_item_tv_dataAvaliacao);
            tv_status = itemView.findViewById(R.id.solPid_item_tv_status);
            tv_tipoCliente = itemView.findViewById(R.id.solPid_item_tv_TipoCliente);
            tv_IdServer = itemView.findViewById(R.id.solPid_item_tv_IdServer);
            tv_Integrado = itemView.findViewById(R.id.solPid_item_tv_Integrado);
            btnGerarProposta = itemView.findViewById(R.id.solPid_item_tv_gerarProposta);
            ll_status = itemView.findViewById(R.id.solPid_item_ll_status);

        }
    }
}
