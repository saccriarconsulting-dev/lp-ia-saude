package com.axys.redeflexmobile.ui.dialog.cliente;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface ClienteInfoPresenter extends BasePresenter<ClienteInfoView> {

    void iniciar(final String clienteJson, final String senhaJson);

    void solicitarSenhaSgv(final boolean telefonePrincipalSelecionado,
                           final boolean telefoneSelecionado,
                           final boolean celularSelecionado,
                           final boolean celularOpcionalSelecionado,
                           final boolean emailSelecionado);
}
