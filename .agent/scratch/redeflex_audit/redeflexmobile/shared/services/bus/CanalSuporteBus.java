package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.axys.redeflexmobile.shared.bd.DBCanalSuporte;
import com.axys.redeflexmobile.shared.models.CanalSuporte;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 10/07/2017.
 */

public class CanalSuporteBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarSolicitacao(Context pContext) throws Exception {
        DBCanalSuporte dbCanalSuporte = new DBCanalSuporte(pContext);
        ArrayList<CanalSuporte> list = dbCanalSuporte.getPendente();
        if (list != null && list.size() > 0) {
            for (CanalSuporte pSuporte : list) {
                try {
                    Bitmap bImagem = null;
                    if (!Util_IO.isNullOrEmpty(pSuporte.getLocalArquivo()))
                        bImagem = BitmapFactory.decodeFile(pSuporte.getLocalArquivo());

                    URL url = new URL(URLs.SUPORTE);
                    JSONObject main = new JSONObject();
                    try {
                        main.put("idVendedor", pSuporte.getIdVendedor());
                        main.put("problema", Util_IO.trataString(pSuporte.getProblema()));
                        if (bImagem != null)
                            main.put("arquivo", Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));
                        else
                            main.put("arquivo", "");
                        main.put("data", Util_IO.dateTimeToString(pSuporte.getDataHora(), Config.FormatDateTimeStringBanco));
                        main.put("versao", pSuporte.getVersaoApp());
                        main.put("descricao", Util_IO.trataString(pSuporte.getDescricao()));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    String response = Utilidades.postRegistros(url, main.toString());
                    if (response != null && response.equals("1"))
                        dbCanalSuporte.deleteById(pSuporte.getCodigo());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } catch (OutOfMemoryError ex) {
                    throw new Exception("Memória do celular está cheia", ex);
                }
            }
        }
    }
}