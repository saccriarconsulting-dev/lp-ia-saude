package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import android.location.Location;

public class ClienteInfoPosListLocalizacaoImpl implements ClienteInfoPosListLocalizacao {

    public boolean estaProximo(final double colaboradorDistancia, final int clienteCerca,
                               final Location gps, final Location cliente) {
        float distance = Math.round(gps.distanceTo(cliente));
        if (gps.getAccuracy() == 0) distance = 0;

        if (cliente.getLatitude() == 0 || cliente.getLongitude() == 0) return false;

        if (colaboradorDistancia <= 0 && clienteCerca <= 0) return false;

        if (gps.getAccuracy() <= 0) return false;

        if (clienteCerca > 0) {
            return !(distance > (clienteCerca + gps.getAccuracy()));
        } else {
            return !(distance > (colaboradorDistancia + gps.getAccuracy()));
        }
    }
}
