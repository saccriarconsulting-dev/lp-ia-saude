package com.axys.redeflexmobile.ui.cartaoponto;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.CartaoPonto;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class CartaoPontoPresenterImpl extends BasePresenterImpl<CartaoPontoView>
        implements CartaoPontoPresenter {

    private final DBColaborador dbColaborador;
    private final DBCartaoPonto dbCartaoPonto;

    protected CartaoPontoPresenterImpl(CartaoPontoView view,
                                       DBColaborador dbColaborador,
                                       DBCartaoPonto dbCartaoPonto) {
        super(view);

        this.dbColaborador = dbColaborador;
        this.dbCartaoPonto = dbCartaoPonto;
    }

    @Override
    public String obterNomeColaborador() {
        Colaborador colaborador = dbColaborador.get();
        if (colaborador == null) {
            getView().mostrarErroColaborador();
            return "";
        }

        return dbColaborador.get().getNome();
    }

    @Override
    public void obterPontosDoDia() {
        List<CartaoPonto> pontos = dbCartaoPonto.obterRegistroDia();

        if (pontos.isEmpty()) {
            getView().verificarListaVazia();
            return;
        }

        getView().preencherRegistros(pontos);
    }

    @Override
    public void registrarPonto(Context context) {
        CartaoPonto cartaoPonto = Utilidades.obterCartaoPonto(context);
        if (cartaoPonto == null) {
            cartaoPonto = new CartaoPonto();
            cartaoPonto.setHorario(new Date());
        }

        dbCartaoPonto.registrarPonto(cartaoPonto);

        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataValidacao = LocalDateTime.of(dataAtual.getYear(), dataAtual.getMonth(),
                dataAtual.getDayOfMonth(), 17, 50);

        if (dataAtual.compareTo(dataValidacao) >= 0) {
            getView().pararServico();
        }

        getView().exibirComprovante(cartaoPonto, dbColaborador.get());
    }
}
