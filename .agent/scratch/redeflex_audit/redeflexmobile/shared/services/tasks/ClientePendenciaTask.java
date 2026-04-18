package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.services.bus.PendenciaBus;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * @author Diego Fernando.
 * @since 3/20/20
 */
public class ClientePendenciaTask extends AsyncTask<String, Void, String> {

    private static final int TIPO_OPERACAO = 1;
    private final WeakReference<Context> context;

    public ClientePendenciaTask(final Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        if (context.get() == null) {
            return null;
        }

        try {
            PendenciaBus.getPendencias(TIPO_OPERACAO, context.get());
            PendenciaBus.getPendenciasCliente(TIPO_OPERACAO, context.get());
            PendenciaBus.getPendenciasMotivo(TIPO_OPERACAO, context.get());
        } catch (Exception e) {
            Timber.w(e);
        }

        return null;
    }
}
