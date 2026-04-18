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
import com.axys.redeflexmobile.shared.enums.EnumSituacaoPrecoDif;
import com.axys.redeflexmobile.shared.enums.EnumStatusPrecoDif;
import com.axys.redeflexmobile.shared.enums.EnumStatusSolicitacao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemSolPD_DetalheAdapter extends RecyclerView.Adapter<ItemSolPD_DetalheAdapter.ViewHolder> {

    private List<SolicitacaoPrecoDiferenciadoDetalhe> items;
    private Context context;
    private SolicitacaoPrecoDiferenciado solicitacao;

    public ItemSolPD_DetalheAdapter(SolicitacaoPrecoDiferenciado solicitacao, List<SolicitacaoPrecoDiferenciadoDetalhe> items, Context context) {
        this.solicitacao = solicitacao;
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemSolPD_DetalheAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultar_sol_preco_det, parent, false);
        return new ItemSolPD_DetalheAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSolPD_DetalheAdapter.ViewHolder holder, int position) {
        SolicitacaoPrecoDiferenciadoDetalhe item = items.get(position);

        // Carrega os Dados do Produto
        Produto produto = new DBEstoque(context).getProdutoById(item.getItemCode());
        if (produto == null)
            holder.tvProduto.setText(item.getItemCode() + " - Produto não encontrado");
        else
            holder.tvProduto.setText(item.getItemCode() + " - " + produto.getNome().trim());

        // Carrega os Dados do Cliente
        Cliente cliente = new DBCliente(context).getById(String.valueOf(item.getIdCliente()));
        if (cliente == null)
            holder.tvCliente.setText(item.getCodigoClienteSGV() + " - Cliente não encontrado");
        else
            holder.tvCliente.setText(item.getCodigoClienteSGV() + " - " + cliente.getNomeFantasia().trim());

        if (item.getStatusId() > 0)
            holder.tvStatus.setText("Status: " + EnumStatusPrecoDif.getPorId(item.getStatusId()).descricao);
        else if (solicitacao.getSync()==1)
            holder.tvStatus.setText("Status: Solicitação em análise");
        else
            holder.tvStatus.setText("Status: Pendente de Sincronização");

        holder.tvDataInicial.setText("Data Inicial: " + Util_IO.dateToStringBr(solicitacao.getDataInicial()));
        holder.tvDataFinal.setText("Data Final: " + Util_IO.dateToStringBr(solicitacao.getDataFinal()));
        holder.tvQuantidade.setText("Quantidade: " + item.getQuantidade());

        // Defina o formato para moeda brasileira
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatoMoeda.format(item.getPrecoVenda());
        holder.tvPrecoVenda.setText("Preço Venda: " + valorFormatado);

        valorFormatado = formatoMoeda.format(item.getPrecoDiferenciado());
        holder.tvPrecoDif.setText("Preço Diferenciado: " + valorFormatado);

        double vDesconto = item.getPrecoVenda() - item.getPrecoDiferenciado();
        valorFormatado = formatoMoeda.format(vDesconto);
        holder.tvDesconto.setText("Desconto: " + valorFormatado);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduto, tvCliente, tvStatus, tvDataInicial, tvDataFinal, tvQuantidade;
        TextView tvPrecoVenda, tvPrecoDif, tvDesconto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduto = itemView.findViewById(R.id.item_PDDetalhe_tvProduto);
            tvCliente = itemView.findViewById(R.id.item_PDDetalhe_tvCliente);
            tvStatus = itemView.findViewById(R.id.item_PDDetalhe_tvStatus);
            tvDataInicial = itemView.findViewById(R.id.item_PDDetalhe_tvDataInicial);
            tvDataFinal = itemView.findViewById(R.id.item_PDDetalhe_tvDataFinal);
            tvQuantidade = itemView.findViewById(R.id.item_PDDetalhe_tvQuantidade);
            tvPrecoVenda = itemView.findViewById(R.id.item_PDDetalhe_tvPrecoVenda);
            tvPrecoDif = itemView.findViewById(R.id.item_PDDetalhe_tvPrecoDif);
            tvDesconto = itemView.findViewById(R.id.item_PDDetalhe_tvDesconto);
        }
    }
}
