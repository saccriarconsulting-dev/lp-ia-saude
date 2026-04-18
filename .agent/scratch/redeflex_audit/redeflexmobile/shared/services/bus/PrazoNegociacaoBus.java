package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPrazoNegociacao;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Rogério Massa on 2019-09-12.
 */

public class PrazoNegociacaoBus extends BaseBus {

    public static void getPrazos(int tipoCarga, Context context) {

        try {
            DBPrazoNegociacao dbPrazoNegociacao = new DBPrazoNegociacao(context);
            Colaborador colaborador = new DBColaborador(context).get();
            String urlFinal = URLs.PRAZO_NEGOCIACAO +
                    "?idVendedor=" + colaborador.getId() +
                    "&tipoCarga=" + tipoCarga;
            PrazoNegociacao[] array = Utilidades.getArrayObject(urlFinal, PrazoNegociacao[].class);

            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (PrazoNegociacao item : array) {
                    dbPrazoNegociacao.salvar(item);
                    idList.add(item.getId());
                }
                setSync(URLs.PRAZO_NEGOCIACAO, idList, colaborador.getId());
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}
