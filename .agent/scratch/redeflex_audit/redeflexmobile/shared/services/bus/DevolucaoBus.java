package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBDevolucao;
import com.axys.redeflexmobile.shared.models.DevolucaoEnvio;
import com.axys.redeflexmobile.shared.models.DevolucaoICCID;
import com.axys.redeflexmobile.shared.models.DevolucaoItens;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 01/09/2017.
 */

public class DevolucaoBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarDevolucao(Context mContext) {
        DBDevolucao dbDevolucao = new DBDevolucao(mContext);
        ArrayList<DevolucaoEnvio> list = dbDevolucao.getPendenteSync();

        for (DevolucaoEnvio devolucao : list) {
            try {
                URL url = new URL(URLs.DEVOLUCAO);
                JSONObject main = new JSONObject();
                JSONArray listDevolucao, listIccid;
                try {
                    main.put("idAppMobile", devolucao.getId());
                    main.put("idVendedor", devolucao.getIdVendedor());
                    main.put("idCliente", devolucao.getIdCliente());
                    main.put("dataHora", Util_IO.dateTimeToString(devolucao.getData(), Config.FormatDateTimeStringBanco));
                    main.put("situacao", devolucao.getSituacao());
                    main.put("tipo", devolucao.getTipo());
                    listDevolucao = new JSONArray();
                    if (devolucao.getListItens() != null && devolucao.getListItens().size() > 0) {
                        JSONObject item, iccid;
                        for (DevolucaoItens devItem : devolucao.getListItens()) {
                            item = new JSONObject();
                            item.put("idAppMobile", devItem.getId());
                            item.put("idProduto", devItem.getIdProduto());
                            item.put("quantidade", devItem.getQuantidade());
                            listIccid = new JSONArray();
                            if (devItem.getListICCID() != null && devItem.getListICCID().size() > 0) {
                                for (DevolucaoICCID devIccid : devItem.getListICCID()) {
                                    iccid = new JSONObject();
                                    iccid.put("idAppMobile", devIccid.getId());
                                    iccid.put("codigoBarra", devIccid.getIccidEntrada());
                                    iccid.put("codigoBarraFinal", devIccid.getIccidSaida());
                                    listIccid.put(iccid);
                                }
                                item.put("listIccid", listIccid);
                            } else
                                item.put("listIccid", "null");
                            listDevolucao.put(item);
                        }
                        main.put("listDevolucao", listDevolucao);
                    } else
                        main.put("listDevolucao", "null");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String response = Utilidades.postRegistros(url, main.toString());
                if (response != null && !response.equals("-1"))
                    dbDevolucao.atualizaSync(devolucao.getId(), response);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}