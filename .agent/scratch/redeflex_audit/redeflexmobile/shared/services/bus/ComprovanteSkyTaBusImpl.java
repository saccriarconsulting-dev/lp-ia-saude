package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBComprovanteSkyTa;
import com.axys.redeflexmobile.shared.models.ComprovanteSkyTa;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import timber.log.Timber;

public class ComprovanteSkyTaBusImpl extends BaseBus
        implements ComprovanteSkyTaBus {

    private Context context;

    public ComprovanteSkyTaBusImpl(Context context) {
        this.context = context;
    }

    public static void enviar(Context context) {
        DBComprovanteSkyTa dbComprovanteSkyTa = new DBComprovanteSkyTa(context);
        List<ComprovanteSkyTa> lista = dbComprovanteSkyTa.obterTodosComprovantes();
        Stream.ofNullable(lista)
                .filter(value -> value.getSync() == ComprovanteSkyTa.NAO_SINCRONIZADO)
                .forEach(value -> {
                    try {
                        if(value.getAnexo() != null && !value.getAnexo().trim().isEmpty()) {
                            converterImagem(value);
                        }

                        URL url = new URL(URLs.ENVIO_SKY_TA);
                        String json = Utilidades.getJsonFromClass(value);
                        String response = Utilidades.postRegistros(url, json);
                        if (response != null && !response.equals("-1")) {
                            value.setSync(1);
                            dbComprovanteSkyTa.atualizarSync(value);
                        }
                    } catch (IOException e) {
                        Timber.e(e);
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                });
    }

    private static void converterImagem(ComprovanteSkyTa comprovanteSkyTa) {
        Bitmap bitmap;
        if (comprovanteSkyTa.getAnexo().contains("/Files/Compressed"))
            bitmap = BitmapFactory.decodeFile(comprovanteSkyTa.getAnexo());
        else
            bitmap = Utilidades.decodeBase64(comprovanteSkyTa.getAnexo());

        comprovanteSkyTa.setAnexo(
                Utilidades.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)
        );
    }

    @Override
    public void sincronizar() {
        ComprovanteSkyTaBusImpl.enviar(context);
    }
}
