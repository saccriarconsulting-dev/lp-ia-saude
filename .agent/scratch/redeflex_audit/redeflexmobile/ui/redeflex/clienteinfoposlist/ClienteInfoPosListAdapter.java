package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 07/12/18.
 */

public class ClienteInfoPosListAdapter extends RecyclerView.Adapter<ClienteInfoPosListViewHolder> {

    private List<ClienteInfoPosListItem> lista;
    private ClienteInfoPosListViewHolder.OnPOSClickListener posClickListener;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ClienteInfoPosListAdapter(ClienteInfoPosListViewHolder.OnPOSClickListener posClickListener) {
        this.lista = new ArrayList<>();
        this.posClickListener = posClickListener;
    }

    public void setLista(List<ClienteInfoPosListItem> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClienteInfoPosListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ClienteInfoPosListViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.activity_cliente_info_pos_list_item, viewGroup, false),
                compositeDisposable,
                posClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteInfoPosListViewHolder clienteInfoPosListViewHolder, int i) {
        clienteInfoPosListViewHolder.bind(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        compositeDisposable.dispose();
    }

    enum ClienteInfoPosListItemTipo {
        HEADER, ITEM
    }

    static class ClienteInfoPosListItem {

        int posId;
        String posDescricao;
        String posModelo;
        int dias;
        ClienteInfoPosListItemTipo tipo;

        ClienteInfoPosListItem(String label1, String label2) {
            this.posDescricao = label1;
            this.posModelo = label2;
            this.tipo = ClienteInfoPosListItemTipo.HEADER;
        }

        ClienteInfoPosListItem(int posId, String posDescricao, String posModelo, int dias) {
            this.posId = posId;
            this.posDescricao = posDescricao;
            this.posModelo = posModelo;
            this.dias = dias;
            this.tipo = ClienteInfoPosListItemTipo.ITEM;
        }
    }
}
