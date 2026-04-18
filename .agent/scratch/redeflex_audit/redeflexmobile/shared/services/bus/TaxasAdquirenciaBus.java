package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTaxasAdquirencia;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.TaxasAdquirencia;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class TaxasAdquirenciaBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getTaxas(int pTipoCarga, Context pContext) {
        try {
            DBTaxasAdquirencia dbTaxasAdquirencia = new DBTaxasAdquirencia(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.TAXASADQUIRENCIA + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            TaxasAdquirencia[] array = Utilidades.getArrayObject(urlfinal, TaxasAdquirencia[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (TaxasAdquirencia taxasAdquirencia : array) {
                    dbTaxasAdquirencia.addTaxas(taxasAdquirencia);
                    idList.add(Integer.parseInt(taxasAdquirencia.getId()));
                }
                setSync(URLs.TAXASADQUIRENCIA, idList, colaborador.getId());
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
//            String urlfinal = URLs.TAXASADQUIRENCIA + "?idTaxa=" + pIdServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}