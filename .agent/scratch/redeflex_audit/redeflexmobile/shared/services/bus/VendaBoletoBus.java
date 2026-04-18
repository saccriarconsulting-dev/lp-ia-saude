package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RetornoItemVenda;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 27/07/2016.
 */
public class VendaBoletoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getBoletosGerados(Context pContext) {
        try {
            DBVenda dbVenda = new DBVenda(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            String urlfinal = URLs.VENDA_MOBILE + "?idVendedor=" + String.valueOf(colaborador.getId());
            RetornoItemVenda[] array = Utilidades.getArrayObject(urlfinal, RetornoItemVenda[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (RetornoItemVenda item : array) {
                    dbVenda.updateCobrancaItemVenda(item.getIdAppVenda());
                    idList.add(Integer.parseInt(item.getIdServer()));
                }
                setSync(URLs.VENDA_MOBILE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String pIdServer) {
//        try {
//            String urlfinal = URLs.VENDA_MOBILE + "?idServer=" + pIdServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}