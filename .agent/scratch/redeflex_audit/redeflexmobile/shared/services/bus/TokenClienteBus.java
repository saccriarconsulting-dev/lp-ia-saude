package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTokenCliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.TokenCliente;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 21/09/2017.
 */

public class TokenClienteBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int pTipoCarga, Context pContext) {
        try {
            DBTokenCliente dbTokenCliente = new DBTokenCliente(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            String urlfinal = URLs.TOKEN + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            TokenCliente[] array = Utilidades.getArrayObject(urlfinal, TokenCliente[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (TokenCliente tokenCliente : array) {
                    dbTokenCliente.addToken(tokenCliente);
                    idList.add(Integer.parseInt(tokenCliente.getId()));
                }
                setSync(URLs.TOKEN, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String pIdVendedor, String pIdToken) {
//        try {
//            String urlfinal = URLs.TOKEN + "?idVendedor=" + pIdVendedor + "&idToken=" + pIdToken;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}