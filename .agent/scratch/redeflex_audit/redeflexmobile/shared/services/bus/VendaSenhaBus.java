package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSenhaVenda;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.SenhaVenda;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 26/07/2016.
 */
public class VendaSenhaBus extends BaseBus {
    public static void syncSenha(Context context) {
        DBSenhaVenda dbSenhaVenda = new DBSenhaVenda(context);
        ArrayList<SenhaVenda> list = dbSenhaVenda.getSenha(true);
        if (list != null && list.size() > 0) {
            for (SenhaVenda pSenhaVenda : list) {
                try {
                    URL url = new URL(URLs.VENDA_SENHA_SENHA);
                    JSONObject main = new JSONObject();
                    try {
                        main.put("idVendedor", pSenhaVenda.getIdVendedor());
                        main.put("senha", pSenhaVenda.getSenha());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    String response = Utilidades.postRegistros(url, main.toString());
                    if (response != null && !response.equals("-1"))
                        dbSenhaVenda.updateSync(pSenhaVenda.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getSenha(int tipoCarga, Context context) {
        try {
            DBSenhaVenda dbSenhaVenda = new DBSenhaVenda(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.VENDA_SENHA + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(tipoCarga);
            SenhaVenda[] array = Utilidades.getArrayObject(urlfinal, SenhaVenda[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (SenhaVenda item : array) {
                    dbSenhaVenda.addSenha(item);
                    idList.add(item.getId());
                }
                setSync(URLs.VENDA_SENHA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    public static void setSync(int idServer) {
//        try {
//            String urlfinal = URLs.VENDA_SENHA + "?idServer=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}