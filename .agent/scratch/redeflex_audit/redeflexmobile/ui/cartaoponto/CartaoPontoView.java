package com.axys.redeflexmobile.ui.cartaoponto;

import com.axys.redeflexmobile.shared.models.CartaoPonto;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

public interface CartaoPontoView extends BaseView {

    void preencherRegistros(List<CartaoPonto> cartoesPonto);

    void verificarListaVazia();

    void exibirComprovante(CartaoPonto cartaoPonto, Colaborador colaborador);

    void pararServico();

    void mostrarErroColaborador();
}
