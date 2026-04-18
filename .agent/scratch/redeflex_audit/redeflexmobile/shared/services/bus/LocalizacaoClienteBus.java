package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 25/10/2016.
 */

public class LocalizacaoClienteBus {
    public static void enviarLocalizacao(Context pContext) {
        DBLocalizacaoCliente dbLocalizacaoCliente = new DBLocalizacaoCliente(pContext);
        DBCliente dbCliente = new DBCliente(pContext);
        ArrayList<LocalizacaoCliente> lista = dbLocalizacaoCliente.getLocalizacaoPendente();
        for (LocalizacaoCliente pLocalizacaoCliente : lista) {
            try {
                Bitmap bImagem = BitmapFactory.decodeFile(pLocalizacaoCliente.getLocalArquivo());
                if (bImagem == null) {
                    dbCliente.updateAtualizaLocal(pLocalizacaoCliente.getIdCliente(), "S");
                    dbLocalizacaoCliente.deleteById(pLocalizacaoCliente.getId());
                } else {
                    URL url = new URL(URLs.LOCALIZACAO_CLIENTE);
                    JSONObject main = new JSONObject();
                    try {
                        main.put("idVendedor", pLocalizacaoCliente.getIdVendedor());
                        main.put("latitude", pLocalizacaoCliente.getLatitude());
                        main.put("longitude", pLocalizacaoCliente.getLongitude());
                        main.put("precisao", pLocalizacaoCliente.getPrecisao());
                        main.put("idCliente", pLocalizacaoCliente.getIdCliente());
                        main.put("data", Util_IO.dateTimeToString(pLocalizacaoCliente.getData(), Config.FormatDateTimeStringBanco));
                        main.put("arquivo", Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));
                        main.put("tipoId", pLocalizacaoCliente.getTipoId());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (OutOfMemoryError ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    String response = Utilidades.postRegistros(url, main.toString());
                    if (response != null && response.equals("1"))
                        dbLocalizacaoCliente.updateSync(pLocalizacaoCliente.getId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}