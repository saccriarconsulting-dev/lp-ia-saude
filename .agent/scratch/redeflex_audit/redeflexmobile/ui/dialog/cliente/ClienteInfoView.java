package com.axys.redeflexmobile.ui.dialog.cliente;

import com.axys.redeflexmobile.shared.mvp.BaseView;

public interface ClienteInfoView extends BaseView {
    void mostrarErro(String message);

    void mostrarErro(String title, String description);

    void mostrarSucesso(String title, String description);

    void apresentarMensagemSemDados();

    void apresentarTelefonePrincipal(final String telefone);

    void apresentarTelefone(final String telefone);

    void apresentarCelular(final String celular);

    void apresentarCelularOpcional(final String celular);

    void apresentarEmail(final String email);
}
