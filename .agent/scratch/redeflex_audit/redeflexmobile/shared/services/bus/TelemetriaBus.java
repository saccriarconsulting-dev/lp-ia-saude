package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBTelemetria;
import com.axys.redeflexmobile.shared.models.Telemetria;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 26/05/2017.
 */

public class TelemetriaBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarLocal(Context pContext) {
        try {
            DBTelemetria dbTelemetria = new DBTelemetria(pContext);
            ArrayList<Telemetria> list = dbTelemetria.getNova();
            for (Telemetria telemetria : list) {
                try {
                    URL url = new URL(URLs.NOVA_TELEMETRIA);
                    JSONObject main = new JSONObject();
                    try {
                        main.put("idVendedor", telemetria.getIdVendedor());
                        main.put("modeloAparelho", Util_IO.trataString(telemetria.getModeloAparelho()));
                        main.put("versaoOs", Util_IO.trataString(telemetria.getVersaoOs()));
                        main.put("versaoApp", Util_IO.trataString(telemetria.getVersaoApp()));
                        main.put("imei", Util_IO.trataString(telemetria.getImei()));
                        main.put("bateria", telemetria.getBateria());
                        main.put("tipoInternet", Util_IO.trataString(telemetria.getTipoInternet()));
                        main.put("latitude", telemetria.getLatitude());
                        main.put("longitude", telemetria.getLongitude());
                        main.put("precisao", telemetria.getPrecisao());
                        main.put("dataGps", Util_IO.dateTimeToString(telemetria.getDataGps(), Config.FormatDateTimeStringBanco));
                        main.put("qtdVisitaPend", telemetria.getQtdVisitaPend());
                        main.put("qtdVendaPend", telemetria.getQtdVendaPend());
                        main.put("qtdVendaItensPend", telemetria.getQtdVendaItensPend());
                        main.put("qtdCodBarraPend", telemetria.getQtdCodBarraPend());
                        main.put("qtdCadCliPend", telemetria.getQtdCadCliPend());
                        main.put("qtdDocCliPend", telemetria.getQtdDocCliPend());
                        main.put("qtdLocCliPend", telemetria.getQtdLocCliPend());
                        main.put("qtdRemessaPend", telemetria.getQtdRemessaPend());
                        main.put("qtdItemRemessaPend", telemetria.getQtdItemRemessaPend());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    String response = Utilidades.postRegistros(url, main.toString());
                    if (response != null && response.equals("1"))
                        dbTelemetria.delete(telemetria.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}