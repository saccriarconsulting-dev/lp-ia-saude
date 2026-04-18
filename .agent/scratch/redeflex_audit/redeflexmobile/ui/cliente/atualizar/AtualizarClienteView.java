package com.axys.redeflexmobile.ui.cliente.atualizar;

import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.mvp.BaseView;

public interface AtualizarClienteView extends BaseView {

    void popularTela(AtualizaCliente cliente, Segmento segmento);

    void popularTela(Cliente cliente, Segmento segmento);

    void mostrarErroNomeFantasia();

    void mostrarErroRazaoSocial();

    void mostrarErroNomeContato();

    void mostrarErroTipoLogradouro();

    void mostrarErroNomeLogradouro();

    void mostrarErroNumeroLogradouro();

    void mostrarErroCidade();

    void mostrarErroBairro();

    void mostrarErroEstado();

    void mostrarErroSegmento();

    void mostrarErroCep();

    void mostrarSucesso();

    void trocarLabelsPessoaFisica();

    void mostrarErroTelefoneCelular();

    void mostrarErroDDDTelefone();

    void mostrarErroTelefone();

    void mostrarErroDDDCelular();

    void mostrarErroCelular();

    void mostrarErroEmail();

    void exibirErroCep();

    void preencherCamposCep(Cep cep);
}
