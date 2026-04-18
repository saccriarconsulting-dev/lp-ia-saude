package com.axys.redeflexmobile.ui.cliente.lista;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

public interface ClienteListaView extends BaseActivityView {

    void popularAdapter(List<Cliente> clientes);

    void abrirInformacoes(String codigo, String latitude, String longitude);

    void validarMensagemListaVazia(boolean apresentar);
}
