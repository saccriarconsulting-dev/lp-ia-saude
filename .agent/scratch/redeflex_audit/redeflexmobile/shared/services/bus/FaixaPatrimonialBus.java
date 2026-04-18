package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBFaixaPatrimonial;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.FaixaPatrimonial;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class FaixaPatrimonialBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int tipoCarga, Context context) {
        try {
            DBFaixaPatrimonial dbFaixaPatrimonial = new DBFaixaPatrimonial(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.FAIXA_PATRIMONIAL + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(tipoCarga);
            FaixaPatrimonial[] array = Utilidades.getArrayObject(urlfinal, FaixaPatrimonial[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (FaixaPatrimonial faixaPatrimonial : array) {
                    dbFaixaPatrimonial.addFaixaPatrimonial(faixaPatrimonial);
                    idList.add(faixaPatrimonial.getId());
                }
                setSync(URLs.FAIXA_PATRIMONIAL, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
