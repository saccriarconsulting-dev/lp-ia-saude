package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBMensagem;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Mensagem;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 03/06/2016.
 */
public class MensagemBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getMensagens(Context context) {
        try {
            DBMensagem dbMensagem = new DBMensagem(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.MENSAGEM + "?idVendedor=" + String.valueOf(colaborador.getId());
            Mensagem[] array = Utilidades.getArrayObject(urlfinal, Mensagem[].class);
            if (array != null) {
                for (Mensagem item : array) {
                    dbMensagem.add(item);
                    setSync(colaborador.getId(), item.getId());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static void setSync(int idVendedor, int id) {
        try {
            String urlfinal = URLs.MENSAGEM + "?idVendedor=" + String.valueOf(idVendedor) + "&id=" + id;
            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void setSyncVisualizacao(Context pContext) {
        try {
            Colaborador colaborador = new DBColaborador(pContext).get();
            DBMensagem dbMensagem = new DBMensagem(pContext);
            ArrayList<Mensagem> lista = dbMensagem.getVisualizadasNaoSync();
            String urlfinal;
            if (lista != null && lista.size() > 0) {
                for (Mensagem item : lista) {
                    urlfinal = URLs.MENSAGEM + "/" + String.valueOf(item.getId())
                            + "?idVendedor=" + String.valueOf(colaborador.getId())
                            + "&dataVisualizacao=" + Util_IO.dateTimeToString(item.getDataVisualizacao(), "yyyy-MM-dd'T'HH:mm");
                    Integer result = Utilidades.getObject(urlfinal, Integer.class);
                    if (result != null && result == 1)
                        dbMensagem.setSyncVisualizacao(item.getId());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
