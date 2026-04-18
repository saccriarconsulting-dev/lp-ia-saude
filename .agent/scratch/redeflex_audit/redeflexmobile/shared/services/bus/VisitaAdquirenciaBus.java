package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBVisitaAdquirencia;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQuality;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import timber.log.Timber;

import static android.graphics.Bitmap.CompressFormat.JPEG;

/**
 * @author Rogério Massa on 28/01/19.
 */

public class VisitaAdquirenciaBus {

    private VisitaAdquirenciaBus() {
    }

    public static void enviarVisitas(Context pContext) {
        try {
            DBVisitaAdquirencia dbVisitaAdquirencia = new DBVisitaAdquirencia(pContext);
            List<VisitProspect> list = dbVisitaAdquirencia.pegarTodasVisitasSync();
            URL url = new URL(URLs.VISITA_ADQUIRENCIA);

            for (VisitProspect visitProspect : list) {
                String response = Utilidades.postRegistros(url,
                        JsonUtils.getGsonInstance().toJson(visitProspect));
                if (response != null && !response.equals("-1")) {
                    dbVisitaAdquirencia.atualizarVisitaSincronizada(visitProspect.getId(),
                            Integer.parseInt(response));
                }
            }
        } catch (IOException ex) {
            Timber.e(ex);
        }
    }

    public static void enviarVisitasAnexo(Context pContext) {
        try {
            DBVisitaAdquirencia dbVisitaAdquirencia = new DBVisitaAdquirencia(pContext);
            List<VisitProspectAttachment> list = dbVisitaAdquirencia.pegarTodasVisitasAnexoSync();
            URL url = new URL(URLs.VISITA_ADQUIRENCIA_ANEXO);

            Stream.ofNullable(list)
                    .map(visitProspectAttachment -> {
                        String image = visitProspectAttachment.getImage();
                        if (image.contains("/Files/Compressed")) {
                            visitProspectAttachment.setImage(Utilidades.encodeToBase64(BitmapFactory
                                    .decodeFile(image), JPEG, 100));
                        } else if (StringUtils.isNotEmpty(image)) {
                            visitProspectAttachment.setImage(Utilidades.encodeToBase64(Utilidades
                                    .decodeBase64(image), JPEG, 100));
                        }
                        return visitProspectAttachment;
                    })
                    .forEach(visitProspectAttachment -> {
                        String response = Utilidades.postRegistros(url, JsonUtils.getGsonInstance().toJson(visitProspectAttachment));
                        if (response != null && !response.equals("-1")) {
                            dbVisitaAdquirencia.atualizarVisitaAnexoSincronizada(visitProspectAttachment.getId());
                        }
                    });

        } catch (IOException ex) {
            Timber.e(ex);
        }
    }

    public static void enviarVisitasQualidade(Context pContext) {
        try {
            DBVisitaAdquirencia dbVisitaAdquirencia = new DBVisitaAdquirencia(pContext);
            URL url = new URL(URLs.VISITA_ADQUIRENCIA_QUALIDADE);

            Stream.ofNullable(dbVisitaAdquirencia.pegarTodasVisitasQualidadeSync())
                    .map(VisitProspectQuality::toRequest)
                    .forEach(request -> {
                        String response = Utilidades.postRegistros(url,
                                JsonUtils.getGsonInstance().toJson(request));
                        if (response != null && !response.equals("-1")) {
                            dbVisitaAdquirencia.atualizarVisitaQualidadeSincronizada(request.id);
                        }
                    });
        } catch (IOException ex) {
            Timber.e(ex);
        }
    }
}
