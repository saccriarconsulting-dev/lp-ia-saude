package com.axys.redeflexmobile.ui.cliente.lista;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface ClienteListaPresenter extends BasePresenter<ClienteListaView> {

    void carregarClientes();

    void carregarClientesConsignado();

    void pesquisarClientes(String texto);

    void abrirInformacoes(Cliente cliente);
}
