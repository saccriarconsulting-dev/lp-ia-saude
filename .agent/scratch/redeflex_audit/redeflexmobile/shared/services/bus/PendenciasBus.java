package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.models.ErroSync;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 17/11/2017.
 */

public class PendenciasBus {
    public static void enviarPendencias(Context pContext) {
        try {
            DBSuporte dbSuporte = new DBSuporte(pContext);
            ArrayList<ErroSync> list = dbSuporte.getErroSync();
            URL url = new URL(URLs.UTIL);
            for (ErroSync pErroSync : list) {
                String jSon = Utilidades.getJsonFromClass(pErroSync);
                String response = Utilidades.postRegistros(url, jSon);
                if (response != null && response.equals("1"))
                    dbSuporte.deletarErroSync(pErroSync.getSyncPendenciasId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
