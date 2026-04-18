package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBDepartamentos;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Departamento;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 10/03/2017.
 */

public class DepartamentoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getDepartamentos(int tipoCarga, Context context) {
        try {
            DBDepartamentos dbDepartamentos = new DBDepartamentos(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.DEPARTAMENTO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            Departamento[] array = Utilidades.getArrayObject(urlfinal, Departamento[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Departamento departamento : array) {
                    dbDepartamentos.addDepartamento(departamento);
                    idList.add(departamento.getId());
                }
                setSync(URLs.DEPARTAMENTO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int idServer, int idVendedor) {
//        try {
//            String urlfinal = URLs.DEPARTAMENTO + "?idVendedor=" + String.valueOf(idVendedor) + "&idDepartamento=" + String.valueOf(idServer);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}