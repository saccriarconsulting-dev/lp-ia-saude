package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RotaMobile;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 28/06/2016.
 */
public class RotaBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getRota(int pTipoCarga, Context pContext) {
        try {
            DBRota dbRota = new DBRota(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.ROTA + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            RotaMobile[] array = Utilidades.getArrayObject(urlfinal, RotaMobile[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (RotaMobile item : array) {
                    dbRota.addRotaMobile(item);
                    idList.add(item.getId());
                }
                setSync(URLs.ROTA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int pIdVendedor, String pIdRota) {
//        try {
//            String urlfinal = URLs.ROTA + "?idVendedor=" + String.valueOf(pIdVendedor) + "&idRota=" + pIdRota;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}