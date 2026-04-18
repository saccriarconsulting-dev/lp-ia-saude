package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Adquirencia;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vitor Herrmann on 03/01/19.
 */
public class AdquirenciaBus extends BaseBus {

    public static void obterRelatorioAdquirencia(Context context) {
        try {
            String url = String.format(
                    "%s?idVendedor=%s",
                    URLs.RELATORIO_ADQUIRENCIA,
                    new DBColaborador(context).get().getId());

            List<Adquirencia> adquirencias = Arrays.asList(Utilidades.getArrayObject(url, Adquirencia[].class));

            if (adquirencias != null && !adquirencias.isEmpty()) {
                DBAdquirencia dbAdquirencia = new DBAdquirencia(context);

                for (Adquirencia item : adquirencias) {
                    dbAdquirencia.addAdquirencia(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RequestModel obterDadosAdquirencia(String idColaborador) {
        String url = String.format(
                "%s?idVendedor=%s",
                URLs.RELATORIO_ADQUIRENCIA,
                idColaborador);
        try {
            return Utilidades.getRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

