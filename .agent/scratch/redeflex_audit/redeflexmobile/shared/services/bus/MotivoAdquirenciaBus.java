package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBMotivoAdquirencia;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Desenvolvimento on 28/06/2016.
 */
public class MotivoAdquirenciaBus extends BaseBus {

    public static void getMotivosAdquirencia(int tipoCarga, Context context) {
        try {
            DBMotivoAdquirencia dbMotivoAdquirencia = new DBMotivoAdquirencia(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.MOTIVO_ADQUIRENCIA,
                    String.valueOf(colaborador.getId()),
                    String.valueOf(tipoCarga));

            VisitProspectCancelReason[] array = Utilidades.getArrayObject(urlfinal,
                    VisitProspectCancelReason[].class);

            if (array != null && array.length > 0) {

                ArrayList<Integer> idList = new ArrayList<>();
                for (VisitProspectCancelReason item : array) {
                    dbMotivoAdquirencia.salvar(item);
                    idList.add(item.getId());
                }
                setSync(URLs.MOTIVO_ADQUIRENCIA, idList, colaborador.getId());
            }

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}