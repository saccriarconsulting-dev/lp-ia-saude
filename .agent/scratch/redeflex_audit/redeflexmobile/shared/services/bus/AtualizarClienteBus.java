package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AtualizarClienteBus {

    public static void enviarClientesAlterados(Context context) {
        try {
            DBAtualizarCliente dbAtualizarCliente = new DBAtualizarCliente(context);
            List<AtualizaCliente> clientesAlterados = dbAtualizarCliente.obterTodosClientesAlterados();

            for (AtualizaCliente item : clientesAlterados) {
                URL url = new URL(URLs.CLIENTE_ALTERACAO);
                String json = Utilidades.getGson().toJson(item);
                String retorno = Utilidades.postRegistros(url, json);
                if (retorno != null && retorno.equals("1")) {
                    dbAtualizarCliente.atualizarSync(item.getId());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
