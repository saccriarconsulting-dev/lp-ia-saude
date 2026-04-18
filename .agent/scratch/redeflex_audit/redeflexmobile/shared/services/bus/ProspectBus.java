package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Bruno Pimentel on 22/01/19.
 */
public class ProspectBus extends BaseBus {

    public static void getProspect(int tipoCarga, Context context) {
        try {
            DBProspect dbProspect = new DBProspect(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();

            String url = String.format(
                    "%s?idVendedor=%s&tipoCarga=%s",
                    URLs.PROSPECT,
                    colaborador.getId(),
                    tipoCarga);

            RouteClientProspect[] array = Utilidades.getArrayObject(url, RouteClientProspect[].class);
            if (array == null || array.length == 0) {
                return;
            }

            ArrayList<Integer> idList = new ArrayList<>();
            for (RouteClientProspect item : array) {
                dbProspect.salvar(item);
                idList.add(item.getId());
            }

            setSync(URLs.PROSPECT_POST_SYNC, idList, colaborador.getId());

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}
