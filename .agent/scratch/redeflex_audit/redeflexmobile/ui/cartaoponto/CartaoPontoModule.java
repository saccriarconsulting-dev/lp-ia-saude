package com.axys.redeflexmobile.ui.cartaoponto;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBColaborador;

import dagger.Module;
import dagger.Provides;

@Module
public class CartaoPontoModule {

    @Provides
    CartaoPontoPresenter providePresenter(CartaoPontoActivity view,
                                          DBColaborador dbColaborador,
                                          DBCartaoPonto dbCartaoPonto) {
        return new CartaoPontoPresenterImpl(view, dbColaborador, dbCartaoPonto);
    }

    @Provides
    CartaoPontoAdapter provideAdapter(Context context) {
        return new CartaoPontoAdapter(context);
    }
}
