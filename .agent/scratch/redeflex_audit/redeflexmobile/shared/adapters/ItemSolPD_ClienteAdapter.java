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
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemSolPD_ClienteAdapter extends RecyclerView.Adapter<ItemSolPD_ClienteAdapter.ViewHolder> {
    private List<SolicitacaoPrecoDiferenciadoDetalhe> items;
    private Context context;

    private ItemSolPD_ClienteAdapter.excluir listener;

    public ItemSolPD_ClienteAdapter(List<SolicitacaoPrecoDiferenciadoDetalhe> items, Context context, ItemSolPD_ClienteAdapter.excluir listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemSolPD_ClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solpd_cliente, parent, false);
        return new ItemSolPD_ClienteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSolPD_ClienteAdapter.ViewHolder holder, int position) {
        int pos = position;
        SolicitacaoPrecoDiferenciadoDetalhe item = items.get(position);
        holder.tvCodigoCliente.setText(item.getCodigoClienteSGV());

        // Carrega os Dados do Cliente
        Cliente cliente = new DBCliente(context).getById(String.valueOf(item.getIdCliente()));
        holder.tvCodigoCliente.setText(item.getCodigoClienteSGV());
        if (cliente == null)
            holder.tvNomeFantasia.setText("Cliente não encontrado");
        else
            holder.tvNomeFantasia.setText(cliente.getNomeFantasia());

        holder.itemsolPD_tv_Qtd.setText(String.valueOf(item.getQuantidade()));

        // Defina o formato para moeda brasileira
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatoMoeda.format(item.getPrecoVenda());
        holder.itemsolPD_tv_PrecoVenda.setText(valorFormatado);

        valorFormatado = formatoMoeda.format(item.getPrecoDiferenciado());
        holder.itemsolPD_tv_PrecoDif.setText(valorFormatado);

        double vDesconto = item.getPrecoVenda() - item.getPrecoDiferenciado();
        valorFormatado = formatoMoeda.format(vDesconto);
        holder.itemsolPD_tv_Desconto.setText(valorFormatado);

        Produto produto = new DBEstoque(context).getProdutoById(item.getItemCode());
        if (produto == null)
            holder.tvProduto.setText("Produto: ");
        else
            holder.tvProduto.setText("Produto: " + produto.getId() + " - " + produto.getDescricao());


        // Excluir Item
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onExcluirClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigoCliente, tvNomeFantasia, tvProduto;
        TextView itemsolPD_tv_Qtd, itemsolPD_tv_PrecoVenda, itemsolPD_tv_PrecoDif, itemsolPD_tv_Desconto;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoCliente = itemView.findViewById(R.id.itemsolPD_tv_CodigoCliente);
            tvNomeFantasia = itemView.findViewById(R.id.itemsolPD_tv_NomeFantasia);
            btnDelete = itemView.findViewById(R.id.itemsolPD_btn_delete);
            tvProduto = itemView.findViewById(R.id.itemsolPD_tv_Produto);
            itemsolPD_tv_Qtd = itemView.findViewById(R.id.itemsolPD_tvQtd);
            itemsolPD_tv_PrecoVenda = itemView.findViewById(R.id.itemsolPD_tvPrecoVenda);
            itemsolPD_tv_PrecoDif = itemView.findViewById(R.id.itemsolPD_tvPrecoDif);
            itemsolPD_tv_Desconto = itemView.findViewById(R.id.itemsolPD_tvDesconto);
        }
    }

    public interface excluir {
        void onExcluirClick(int pos);
    }
}
