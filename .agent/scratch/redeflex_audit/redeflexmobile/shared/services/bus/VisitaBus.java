package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 26/07/2016.
 */
public class VisitaBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarPendentes(Context pContext) {
        try {
            DBVisita dbVisita = new DBVisita(pContext);
            ArrayList<Visita> list = dbVisita.getPendentes();
            URL url = new URL(URLs.VISITA);
            for (Visita pVisita : list) {
                JSONObject main = new JSONObject();
                try {
                    main.put("id", pVisita.getIdServer());
                    main.put("idAppMobile", pVisita.getId());
                    main.put("idVendedor", String.valueOf(pVisita.getIdVendedor()));
                    main.put("dataInicio", Util_IO.dateTimeToString(pVisita.getDataInicio(), Config.FormatDateTimeStringBanco));
                    main.put("dataFim", Util_IO.dateTimeToString(pVisita.getDataFim(), Config.FormatDateTimeStringBanco));
                    main.put("idMotivo", pVisita.getIdMotivo());
                    main.put("idCliente", pVisita.getIdCliente());
                    main.put("latitude", pVisita.getLatitude());
                    main.put("longitude", pVisita.getLongitude());
                    main.put("precisao", pVisita.getPrecisao());
                    main.put("versaoApp", pVisita.getVersaoApp());
                    main.put("distancia", pVisita.getDistancia());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String response = Utilidades.postRegistros(url, main.toString());
                if (response != null && !response.equals("-1"))
                    dbVisita.updateIdServer(response, pVisita.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}