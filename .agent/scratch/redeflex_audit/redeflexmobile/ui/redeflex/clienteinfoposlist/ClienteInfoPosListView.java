package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import android.location.Location;

import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

public interface ClienteInfoPosListView extends BaseActivityView {

    void apresentarClienteNaoEncontrado();

    void apresentarErroTroca(String titulo, String mensagem);

    void trocaEfetuada();

    void apresentarErroRemocao(String titulo, String mensagem);

    void remocaoEfetuada();

    void popularAdapter(List<ClienteInfoPosListAdapter.ClienteInfoPosListItem> lista);

    boolean estaProximo(double colaboradorDistancia, int clienteDistancia, Location cliente);

    void apresentarErroInternet(boolean tentouRemover);

    void fechar();
}
