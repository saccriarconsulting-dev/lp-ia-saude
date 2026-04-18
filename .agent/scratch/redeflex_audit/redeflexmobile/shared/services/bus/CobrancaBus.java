package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCobranca;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Cobranca;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 14/07/2016.
 */
public class CobrancaBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getCobranca(int tipoCarga, Context context) {
        try {
            DBCobranca dbCobranca = new DBCobranca(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.COBRANCA + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            Cobranca[] array = Utilidades.getArrayObject(urlfinal, Cobranca[].class);
            if (array != null) {
                ArrayList<Long> idList = new ArrayList<>();
                for (Cobranca obj : array) {
                    dbCobranca.addCobranca(obj);
                    idList.add(obj.getId());
                }
                setSyncLong(URLs.COBRANCA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(Long idServer) {
//        try {
//            String urlfinal = URLs.COBRANCA + "?idServer=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}