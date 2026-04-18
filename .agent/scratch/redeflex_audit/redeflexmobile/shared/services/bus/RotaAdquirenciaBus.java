package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Diego Fernando on 2019-01-07.
 */
public class RotaAdquirenciaBus extends BaseBus {

    public static void getRota(int tipoCarga, Context context) {
        try {
            DBRotaAdquirencia dbRota = new DBRotaAdquirencia(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();

            String url = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.ROTA_ADQUIRENCIA,
                    colaborador.getId(),
                    tipoCarga);

            RoutesProspect[] array = Utilidades.getArrayObject(url, RoutesProspect[].class);
            if (array == null || array.length == 0) return;

            ArrayList<Integer> idList = new ArrayList<>();
            for (RoutesProspect item : array) {
                dbRota.salvar(item);
                idList.add(item.getId());
            }
            setSync(URLs.ROTA_ADQUIRENCIA, idList, colaborador.getId());

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}
