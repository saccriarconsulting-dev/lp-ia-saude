package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class ClienteInfoAdapterDiff extends DiffUtil.Callback {

    private List<ClienteInfoFragment.ClienteInfoItem> listaNova;
    private List<ClienteInfoFragment.ClienteInfoItem> listaAntiga;

    ClienteInfoAdapterDiff(List<ClienteInfoFragment.ClienteInfoItem> listaNova,
                           List<ClienteInfoFragment.ClienteInfoItem> listaAntiga) {
        this.listaNova = listaNova;
        this.listaAntiga = listaAntiga;
    }

    @Override
    public int getOldListSize() {
        return listaAntiga.size();
    }

    @Override
    public int getNewListSize() {
        return listaNova.size();
    }

    @Override
    public boolean areItemsTheSame(int older, int newer) {
        return listaAntiga.get(older).primeiroCampo.equals(listaNova.get(older).primeiroCampo)
                && listaAntiga.get(older).segundoCampo.equals(listaNova.get(older).segundoCampo)
                && listaAntiga.get(older).terceiroCampo.equals(listaNova.get(older).terceiroCampo)
                && listaAntiga.get(older).tipo.equals(listaNova.get(older).tipo)
                && listaAntiga.get(older).validador.equals(listaNova.get(older).validador);
    }

    @Override
    public boolean areContentsTheSame(int older, int newer) {
        return listaAntiga.get(older).equals(listaNova.get(newer));
    }
}
