package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCnae;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Cnae;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class CnaeBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getCnae(int pTipoCarga, Context pContext) {
        try {
            DBCnae dbCnae = new DBCnae(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            if (pTipoCarga == 1 && !dbCnae.existeCnae())
                pTipoCarga = 0;
            String urlfinal = URLs.CNAE + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Cnae[] array = Utilidades.getArrayObject(urlfinal, Cnae[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Cnae cnae : array) {
                    dbCnae.addCnae(cnae);
                    idList.add(Integer.parseInt(cnae.getId()));
                }
                setSync(URLs.CNAE, idList, colaborador.getId());
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
//            String urlfinal = URLs.CNAE + "?idCnae=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}