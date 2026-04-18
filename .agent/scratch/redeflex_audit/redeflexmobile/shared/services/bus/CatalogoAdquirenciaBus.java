package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.axys.redeflexmobile.shared.bd.DBCatalogoAdquirencia;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCatalog;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import timber.log.Timber;

/**
 * @author Rogério Massa on 28/01/19.
 */

public class CatalogoAdquirenciaBus {

    private CatalogoAdquirenciaBus() {
    }

    public static void getCatalogs(Context context) {
        try {
            DBCatalogoAdquirencia dbCatalogoAdquirencia = new DBCatalogoAdquirencia(context);
            VisitProspectCatalog[] array = Utilidades.getArrayObjectSemTimeout(URLs.CATALOGO_RFMA, VisitProspectCatalog[].class);
            if (array == null || array.length == 0) return;

            for (VisitProspectCatalog item : array) {

                if (item.getImage() == null) {
                    continue;
                }

                Bitmap bImage = Utilidades.decodeBase64(item.getImage());
                if (bImage == null) {
                    try {
                        byte[] data = Base64.decode(item.getImage(), Base64.DEFAULT);
                        bImage = Utilidades.decodeBase64(new String(data, "UTF-8"));
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }

                if (bImage != null) {
                    String localFinal = Utilidades.getFilename(context);
                    try {
                        FileOutputStream fos = new FileOutputStream(localFinal);
                        bImage.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    item.setImage(localFinal);
                    dbCatalogoAdquirencia.salvar(item);
                }
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}
