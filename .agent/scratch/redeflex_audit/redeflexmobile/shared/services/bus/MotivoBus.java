package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBMotivo;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Motivo;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 28/06/2016.
 */
public class MotivoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getMotivos(int pTipoCarga, Context pContext) {
        try {
            DBMotivo dbMotivo = new DBMotivo(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            String urlfinal = URLs.MOTIVO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Motivo[] array = Utilidades.getArrayObject(urlfinal, Motivo[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Motivo item : array) {
                    dbMotivo.addMotivo(item);
                    idList.add(item.getId());
                }
                setSync(URLs.MOTIVO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int pIdVendedor, int pIdMotivo) {
//        try {
//            String urlfinal = URLs.MOTIVO + "?idVendedor=" + String.valueOf(pIdVendedor) + "&idMotivo=" + String.valueOf(pIdMotivo);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}