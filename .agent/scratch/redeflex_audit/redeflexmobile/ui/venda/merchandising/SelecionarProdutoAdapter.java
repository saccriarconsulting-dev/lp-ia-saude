package com.axys.redeflexmobile.ui.venda.merchandising;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.venda.ProdutoMerchandising;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.List;

public class SelecionarProdutoAdapter extends RecyclerView.Adapter<SelecionarProdutoAdapter.ViewHolderProduto> {

    private Context mContext;
    private List<ProdutoMerchandising> mLista;
    private ISelecionarProdutoAdapterListener adapterListener;

    public SelecionarProdutoAdapter(Context mContext, List<ProdutoMerchandising> mLista, ISelecionarProdutoAdapterListener adapterListener) {
        this.mContext = mContext;
        this.mLista = mLista;
        this.adapterListener = adapterListener;
    }

    @Override
    public ViewHolderProduto onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_selecionar_produto, parent, false);
        return new ViewHolderProduto(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderProduto holder, int position) {
        ProdutoMerchandising objeto = mLista.get(position);
        holder.tvNomeProduto.setText(objeto.getNome());
        holder.tvDataProduto.setText(
                objeto.getData() == null
                        ? "Indeterminado"
                        : Util_IO.dateToStringBr(objeto.getData())
        );

        holder.tvDataProduto.setOnClickListener(v -> adapterListener.onClickCalendario(objeto, holder.tvDataProduto));
        holder.llExcluirProduto.setOnClickListener(v -> adapterListener.onClickExcluir(objeto));
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    protected class ViewHolderProduto extends RecyclerView.ViewHolder {
        private TextView tvNomeProduto;
        private TextView tvDataProduto;
        private LinearLayout llExcluirProduto;

        ViewHolderProduto(View itemView) {
            super(itemView);

            tvNomeProduto = (TextView) itemView.findViewById(R.id.produto_tv_nome);
            tvDataProduto = (TextView) itemView.findViewById(R.id.produto_tv_data);
            llExcluirProduto = (LinearLayout) itemView.findViewById(R.id.produto_ll_excluir);
        }
    }

    public interface ISelecionarProdutoAdapterListener {
        void onClickExcluir(ProdutoMerchandising produto);

        void onClickCalendario(ProdutoMerchandising produto, TextView data);
    }
}
