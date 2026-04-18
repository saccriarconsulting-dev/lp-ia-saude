package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.models.CartaoPontoRequest;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CartaoPontoBus extends BaseBus {

    public static void enviarDadosCartaoPonto(Context context) {
        DBCartaoPonto dbCartaoPonto = new DBCartaoPonto(context);
        List<CartaoPontoRequest> requestList = dbCartaoPonto.obterTodosRegistros();

        try {
            URL url = new URL(URLs.REGISTRO_PONTO);

            for (CartaoPontoRequest item : requestList) {
                String retorno = Utilidades.postRegistros(url, Utilidades.getJsonFromClass(item));

                if (retorno != null && !retorno.equals("-1")) {
                    dbCartaoPonto.atualizaSync(String.valueOf(item.getIdMobile()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
