package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by joao.viana on 13/11/2017.
 */

public class ColaboradorBus {
    public static Colaborador getServer(Context pContext) throws Exception {
        Utilidades.IMEI = Utilidades.retornaIMEI(pContext);
        String urlfinal = URLs.COLABORADOR + "?imei=" + Utilidades.IMEI;
        Timber.d("IMEI %s", urlfinal);
        return Utilidades.getObject(urlfinal, Colaborador.class);
    }
// 358240051111110
    public static Date getDateServer() throws Exception {
        String urlFinal = URLs.DATA_HORA_SERVIDOR;
        Date dataServidor = Utilidades.getObject(urlFinal, Date.class);

        if (dataServidor != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataServidor);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            return calendar.getTime();
        }

        return null;
    }
}