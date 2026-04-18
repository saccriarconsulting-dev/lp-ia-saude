package com.axys.redeflexmobile.ui.cartaoponto;

import android.content.Context;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface CartaoPontoPresenter extends BasePresenter<CartaoPontoView> {

    String obterNomeColaborador();

    void obterPontosDoDia();

    void registrarPonto(Context context);
}
