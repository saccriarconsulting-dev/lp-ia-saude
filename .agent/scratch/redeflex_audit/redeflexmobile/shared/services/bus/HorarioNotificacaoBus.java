package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBHorarioNotificacao;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.HorarioNotificacao;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Denis Gasparoto on 07/05/2019.
 */

public class HorarioNotificacaoBus extends BaseBus {

    public static void getHorariosNotificacao(int tipoCarga, Context context) {
        try {
            DBHorarioNotificacao dbHorarioNotificacao = new DBHorarioNotificacao(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();

            String url = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.JORNADA,
                    colaborador.getId(),
                    tipoCarga);

            HorarioNotificacao[] horariosNotificacao = Utilidades.getArrayObject(url,
                    HorarioNotificacao[].class);

            if (horariosNotificacao == null || horariosNotificacao.length == 0) {
                return;
            }

            ArrayList<Integer> idList = new ArrayList<>();
            for (HorarioNotificacao horarioNotificacao : horariosNotificacao) {
                dbHorarioNotificacao.salvarHorariosNotificacao(horarioNotificacao);
                idList.add(horarioNotificacao.getIdServer());
            }
            setSync(URLs.JORNADA, idList, colaborador.getId());


        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
