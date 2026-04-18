package com.axys.redeflexmobile.ui.cartaoponto;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CartaoPonto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Gasparoto on 03/05/2019.
 */

public class CartaoPontoAdapter extends RecyclerView.Adapter<CartaoPontoViewHolder> {

    private Context context;
    private List<CartaoPonto> cartoesPonto;

    CartaoPontoAdapter(Context context) {
        this.context = context;
        this.cartoesPonto = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartaoPontoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CartaoPontoViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_registro_ponto, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartaoPontoViewHolder viewHolder, int position) {
        viewHolder.bind(cartoesPonto.get(position));
    }

    @Override
    public int getItemCount() {
        return cartoesPonto.size();
    }

    void setCartoesPonto(List<CartaoPonto> cartoesPonto) {
        this.cartoesPonto = cartoesPonto;
        notifyDataSetChanged();
    }
}
