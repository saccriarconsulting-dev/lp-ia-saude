package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface ClienteInfoPosPresenter extends BasePresenter<ClienteInfoPosListView> {

    void setClienteId(String idCliente);


    void carregarDados();

    InformacaoGeralPOS pegarPos(ClienteInfoPosListAdapter.ClienteInfoPosListItem item);

    void removerPos(int id);

    void trocarPos(InformacaoGeralPOS pos);

    void removerPos(InformacaoGeralPOS pos);

    boolean estaProximo();
}
