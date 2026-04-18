package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSenhaCliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.GenericResponse;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.SenhaCliente;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by joao.viana on 19/05/2017.
 */

public class SenhaClienteBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int tipoCarga, Context context) {
        try {
            DBSenhaCliente dbSenhaCliente = new DBSenhaCliente(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.SENHA_CLIENTE + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + tipoCarga;
            SenhaCliente[] array = Utilidades.getArrayObject(urlfinal, SenhaCliente[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (SenhaCliente item : array) {
                    dbSenhaCliente.addSenha(item);
                    idList.add(Integer.parseInt(item.getId()));
                }
                setSync(URLs.SENHA_CLIENTE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static RequestModel solicitaSenha(ReenviaSenhaCliente senhaCliente) {
        try {
            String json = Utilidades.getGsonInstance().toJson(senhaCliente);
            URL url = new URL(URLs.REENVIA_SENHA_CLIENTE);
            return Utilidades.postRequest(url, json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Single<GenericResponse> obterSenhaSgv(ReenviaSenhaCliente senhaCliente) {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }


        });
    }
//
//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String idServer, String idVendedor) {
//        try {
//            String urlfinal = URLs.SENHA_CLIENTE + "?idServer=" + idServer + "&idVendedor=" + idVendedor;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}