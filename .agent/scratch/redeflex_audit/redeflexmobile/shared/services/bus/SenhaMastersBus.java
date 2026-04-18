package com.axys.redeflexmobile.shared.services.bus;

import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;

/**
 * Created by Vitor Herrmann - Capptan on 07/08/2018.
 */

public class SenhaMastersBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static RequestModel obterSenhaMasters(String idColaborador) {
        try {
            return Utilidades.getRequest(URLs.SENHA_MASTERS + "/" + idColaborador);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
