package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBBancos;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Bancos;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class BancosBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getBancos(int pTipoCarga, Context pContext) {
        try {
            DBBancos dbBancos = new DBBancos(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.BANCOS + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Bancos[] array = Utilidades.getArrayObject(urlfinal, Bancos[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Bancos bancos : array) {
                    dbBancos.addBanco(bancos);
                    idList.add(Integer.parseInt(bancos.getIdServer()));
                }
                setSync(URLs.BANCOS, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String pIdServer) {
//        try {
//            String urlfinal = URLs.BANCOS + "?idServer=" + pIdServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}