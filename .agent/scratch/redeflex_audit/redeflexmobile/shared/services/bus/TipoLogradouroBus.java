package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTipoLogradouro;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.TipoLogradouro;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class TipoLogradouroBus extends BaseBus {
    public static void get(int tipoCarga, Context context) {
        try {
            DBTipoLogradouro dbTipoLogradouro = new DBTipoLogradouro(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_TIPO_LOGRADOURO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(tipoCarga);
            TipoLogradouro[] array = Utilidades.getArrayObject(urlfinal, TipoLogradouro[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (TipoLogradouro tipoLogradouro : array) {
                    dbTipoLogradouro.addTipoLogradouro(tipoLogradouro);
                    idList.add(tipoLogradouro.getId());
                }
                setSync(URLs.URL_TIPO_LOGRADOURO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
