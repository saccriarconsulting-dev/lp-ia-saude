package com.axys.redeflexmobile.ui.redeflex.clienteinfoposlist;

import android.location.Location;

public interface ClienteInfoPosListLocalizacao {

    boolean estaProximo(final double colaboradorDistancia, final int clienteCerca,
                        final Location gps, final Location cliente);
}
