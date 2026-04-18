package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDIccidOperadora;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.IccidOperadora;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class IccidOperadoraBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getIccidOperadora(int tipoCarga, Context context) {
        try {
            BDIccidOperadora bdIccidOperadora = new BDIccidOperadora(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_ICCIOPERADORA + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            IccidOperadora[] array = Utilidades.getArrayObject(urlfinal, IccidOperadora[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (IccidOperadora item : array) {
                    int idSync = item.getId();
                    item.setId(item.getIccidOperadoraId());

                    bdIccidOperadora.addIccidOperadora(item);
                    idList.add(idSync);
                }
                setSync(URLs.URL_ICCIOPERADORA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
