package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.Operadora;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import timber.log.Timber;

/**
 * @author Bruno Pimentel on 10/09/2019
 */
public class OperadoraBus extends BaseBus {

    public static void buscarOperadoras(final int tipoCarga,
                                        final Context context) {
        try {
            final DBOperadora dbOperadora = new DBOperadora(context);
            final DBColaborador dbColaborador = new DBColaborador(context);

            final Integer colaboradorId = dbColaborador.get().getId();

            final String url = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.OPERADORA,
                    colaboradorId,
                    tipoCarga);

            final Operadora[] operadoras = Utilidades.getArrayObject(url, Operadora[].class);

            if (operadoras != null && operadoras.length > 0) {
                final List<Operadora> operadorasMapeadas = Stream.of(operadoras).map(operadora -> {
                    operadora.setImagem(
                            pegarEnderecoDeImagemSalva(
                                    operadora.getImagem(),
                                    context
                            ));
                    return operadora;
                }).toList();

                Stream.of(operadorasMapeadas)
                        .forEach(dbOperadora::insereOperadora);

                setSync(URLs.OPERADORA,
                        Stream.of(operadorasMapeadas).map(Operadora::getIdOperadora).toList(),
                        colaboradorId);
            }

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    private static String pegarEnderecoDeImagemSalva(final String imagem,
                                                     final Context context) {
        Bitmap bitmap = Utilidades.decodeBase64(imagem);

        if (bitmap == null) {
            try {
                byte[] data = Base64.decode(imagem, Base64.DEFAULT);
                bitmap = Utilidades.decodeBase64(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Timber.e(e);
            }
        }

        if (bitmap != null) {
            final String localFinal = Utilidades.getFilename(context);
            try (FileOutputStream outputStream = new FileOutputStream(localFinal)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            } catch (FileNotFoundException ex) {
                Timber.e(ex);
            } catch (IOException ex) {
                Timber.e(ex);
            }

            return localFinal;
        }

        return null;
    }
}
