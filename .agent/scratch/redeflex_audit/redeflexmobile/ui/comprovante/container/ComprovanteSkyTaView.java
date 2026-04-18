package com.axys.redeflexmobile.ui.comprovante.container;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

public interface ComprovanteSkyTaView extends BaseView {

    void popularAdapter(List<Cliente> clientes);

    void salvoSucesso();

    void mostrarErroCliente();

    void mostrarErroImagem();

    void mostrarErroTipo();

    void mostrarErroCampanha();

    void mostrarErroMaterial();
}
