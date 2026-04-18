package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComandoExecutado;
import com.axys.redeflexmobile.shared.models.ComandoExecutar;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 13/02/2017.
 */

public class ComandosBus {
    public static void getComandos(Context mContext) {
        try {
            DBColaborador dbColaborador = new DBColaborador(mContext);
            Colaborador colaborador = dbColaborador.get();
            DBSuporte dbSuporte = new DBSuporte(mContext);
            String urlfinal = URLs.COMANDO + "?idVendedor=" + String.valueOf(colaborador.getId());
            try {
                ComandoExecutar[] array = Utilidades.getArrayObject(urlfinal, ComandoExecutar[].class);
                if (array != null) {
                    for (ComandoExecutar item : array)
                        dbSuporte.insertComandoExecutar(item);
                    dbSuporte.executarComandos();
                    ArrayList<ComandoExecutado> cmds = dbSuporte.getComandosExecutados();
                    for (ComandoExecutado cmd : cmds) {
                        URL url = new URL(URLs.COMANDO);
                        JSONObject main = new JSONObject();
                        try {
                            main.put("id", cmd.getId());
                            main.put("mensagem", cmd.getMensagem());
                            main.put("dataExecucao", Util_IO.dateTimeToString(cmd.getDataExecucao(), Config.FormatDateTimeStringBanco));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            continue;
                        }

                        String response = Utilidades.postRegistros(url, main.toString());
                        if (response != null && !response.equals("-1"))
                            dbSuporte.deleteComandoExecutar(cmd.getId());
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}