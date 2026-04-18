package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBFaixaSalarial;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.FaixaSalarial;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class FaixaSalarialBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int tipoCarga, Context context) {
        try {
            DBFaixaSalarial dbFaixaSalarial = new DBFaixaSalarial(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.FAIXA_SALARIAL + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(tipoCarga);
            FaixaSalarial[] array = Utilidades.getArrayObject(urlfinal, FaixaSalarial[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (FaixaSalarial faixaSalarial : array) {
                    dbFaixaSalarial.addFaixaSalarial(faixaSalarial);
                    idList.add(faixaSalarial.getId());
                }
                setSync(URLs.FAIXA_SALARIAL, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
