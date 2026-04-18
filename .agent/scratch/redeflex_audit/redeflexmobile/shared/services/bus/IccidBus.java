package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 09/02/2017.
 */

public class IccidBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getIccid(int tipoCarga, Context context) {
        try {
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            DBIccid dbIccid = new DBIccid(context);
            String urlfinal = URLs.IDENTIFICAR_CHIP + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            Iccid[] array = Utilidades.getArrayObject(urlfinal, Iccid[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Iccid item : array) {
                    dbIccid.addIccid(item);
                    idList.add(Integer.parseInt(item.getId()));
                }
                setSync(URLs.IDENTIFICAR_CHIP, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String pIdServer, String pIdVendedor) {
//        try {
//            String urlfinal = URLs.IDENTIFICAR_CHIP + "?idServer=" + pIdServer + "&idVendedor=" + pIdVendedor;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}