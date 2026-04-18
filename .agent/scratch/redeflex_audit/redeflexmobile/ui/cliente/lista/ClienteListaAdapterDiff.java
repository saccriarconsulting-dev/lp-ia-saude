package com.axys.redeflexmobile.ui.cliente.lista;

import androidx.recyclerview.widget.DiffUtil;

import com.axys.redeflexmobile.shared.models.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteListaAdapterDiff extends DiffUtil.Callback {

    private List<Cliente> clientesAntigo = new ArrayList<>();
    private List<Cliente> clientesNovos = new ArrayList<>();

    public ClienteListaAdapterDiff(List<Cliente> clientesAntigo, List<Cliente> clientesNovos) {
        this.clientesAntigo = clientesAntigo;
        this.clientesNovos = clientesNovos;
    }

    @Override
    public int getOldListSize() {
        return clientesAntigo.size();
    }

    @Override
    public int getNewListSize() {
        return clientesNovos.size();
    }

    @Override
    public boolean areItemsTheSame(int older, int newer) {
        return clientesAntigo.get(older).getId().equals(clientesNovos.get(newer).getId());
    }

    @Override
    public boolean areContentsTheSame(int older, int newer) {
        return clientesAntigo.get(older).equals(clientesNovos.get(newer));
    }
}
