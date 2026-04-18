package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPerguntasQualidade;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Desenvolvimento on 28/06/2016.
 */
public class PerguntasQualidadeBus extends BaseBus {

    public static void getPerguntas(int tipoCarga, Context context) {
        try {
            DBPerguntasQualidade dbPerguntasQualidade = new DBPerguntasQualidade(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.PERGUNTAS_QUALIDADE,
                    String.valueOf(colaborador.getId()),
                    String.valueOf(tipoCarga));

            VisitProspectQualityQuestion[] array = Utilidades.getArrayObject(urlfinal,
                    VisitProspectQualityQuestion[].class);

            if (array != null && array.length > 0) {

                ArrayList<Integer> idList = new ArrayList<>();
                for (VisitProspectQualityQuestion item : array) {
                    dbPerguntasQualidade.salvar(item);
                    idList.add(item.getId());
                }
                setSync(URLs.PERGUNTAS_QUALIDADE_POST_SYNC, idList, colaborador.getId());
            }

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}