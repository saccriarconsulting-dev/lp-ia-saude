package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBFilial;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Filial;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 10/03/2017.
 */

public class FilialBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getFilial(int pTipoCarga, Context pContext) {
        try {
            DBFilial dbFilial = new DBFilial(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.FILIAL + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Filial[] array = Utilidades.getArrayObject(urlfinal, Filial[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Filial filial : array) {
                    dbFilial.addFilial(filial);
                    idList.add(filial.getId());
                }
                setSync(URLs.FILIAL, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int idServer, int idVendedor) {
//        try {
//            String urlfinal = URLs.FILIAL + "?idVendedor=" + String.valueOf(idVendedor) + "&idFilial=" + String.valueOf(idServer);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}