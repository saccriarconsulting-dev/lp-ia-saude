package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 27/04/2017.
 */

public class LimiteBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int tipoCarga, Context context) {
        try {
            DBLimite dbLimite = new DBLimite(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.LIMITE_CLIENTE + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            LimiteCliente[] array = Utilidades.getArrayObject(urlfinal, LimiteCliente[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (LimiteCliente item : array) {
                    dbLimite.addLimite(item);
                    idList.add(Integer.parseInt(item.getId()));
                }
                setSync(URLs.LIMITE_CLIENTE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}