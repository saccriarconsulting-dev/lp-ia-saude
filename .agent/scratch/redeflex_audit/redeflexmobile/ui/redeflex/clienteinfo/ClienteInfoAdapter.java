package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoFragmentType;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoItem;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoViewHolder.ClienteInfoViewHolderHeader;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoViewHolder.ClienteInfoViewHolderItem;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoViewHolder.ClienteInfoViewHolderListener;

import java.util.ArrayList;
import java.util.List;

import static com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoFragment.ClienteInfoItemType.HEADER;

/**
 * @author Rogério Massa on 07/11/18.
 */

public class ClienteInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ClienteInfoViewHolderListener {

    private static final int VIEW_HEADER = 9;

    private Context context;
    private List<ClienteInfoItem> lista;
    private ClienteInfoFragmentType tipoFragmento;
    private List<String> abertos;

    ClienteInfoAdapter(Context context, ClienteInfoFragmentType tipoFragmento) {
        this.context = context;
        this.tipoFragmento = tipoFragmento;
        this.lista = new ArrayList<>();
        this.abertos = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_HEADER) {
            return new ClienteInfoViewHolderHeader(LayoutInflater.from(context)
                    .inflate(R.layout.activity_cliente_info_fragment_header, viewGroup, false),
                    this, tipoFragmento);
        }
        return new ClienteInfoViewHolderItem(LayoutInflater.from(context)
                .inflate(R.layout.activity_cliente_info_fragment_item, viewGroup, false),
                this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ClienteInfoViewHolderHeader) {
            ClienteInfoViewHolderHeader holder = (ClienteInfoViewHolderHeader) viewHolder;
            holder.bind(lista.get(position));
        }

        if (viewHolder instanceof ClienteInfoViewHolderItem) {
            ClienteInfoViewHolderItem holder = (ClienteInfoViewHolderItem) viewHolder;
            holder.bind(lista.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (lista == null) {
            return 0;
        }
        return lista.size();
    }

    @Override
    public int getItemViewType(int position) {
        ClienteInfoItem item = lista.get(position);
        if (item != null && item.tipo == HEADER) {
            return VIEW_HEADER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public boolean estaAberto(String validador) {
        return tipoFragmento == ClienteInfoFragmentType.PRECO_DIFERENCIADO
                || abertos != null && abertos.contains(validador);
    }

    @Override
    public void abrirFechar(String validador) {
        if (abertos.contains(validador)) {
            abertos.remove(validador);
        } else {
            abertos.add(validador);
        }

        Stream.of(lista).forEachIndexed((index, item) -> {
            if (item.validador.equals(validador)) {
                notifyItemChanged(index);
            }
        });
    }

    public void setLista(List<ClienteInfoItem> lista) {
        if (lista == null) {
            return;
        }

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ClienteInfoAdapterDiff(
                this.lista,
                lista
        ));

        this.lista.clear();
        this.lista.addAll(lista);

        diffResult.dispatchUpdatesTo(this);
    }
}
