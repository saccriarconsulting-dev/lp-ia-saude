package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 29/08/2016.
 */
public class SegmentoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getSegmento(int pTipoCarga, Context pContext) {
        try {
            DBSegmento dbSegmento = new DBSegmento(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            if (pTipoCarga == 1 && !dbSegmento.existeSegmento())
                pTipoCarga = 0;
            String urlfinal = URLs.SEGMENTO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Segmento[] array = Utilidades.getArrayObject(urlfinal, Segmento[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Segmento segmento : array) {
                    dbSegmento.addSegmento(segmento);
                    idList.add(Integer.parseInt(segmento.getId()));
                }
                setSync(URLs.SEGMENTO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String idServer) {
//        try {
//            String urlfinal = URLs.SEGMENTO + "?idServer=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}