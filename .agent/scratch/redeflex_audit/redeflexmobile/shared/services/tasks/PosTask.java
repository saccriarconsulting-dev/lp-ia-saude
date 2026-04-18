package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.POSBus;

import java.lang.ref.WeakReference;

public class PosTask extends AsyncTask<String, Void, String> {

    private static final int CARGA_PARCIAL = 1;
    private static final String RETORNO_SEM_VALOR = "";
    private WeakReference<Context> context;

    public PosTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        if (context.get() == null) {
            return RETORNO_SEM_VALOR;
        }

        POSBus.getInformacoesGeraisPOS(CARGA_PARCIAL, context.get());

        return RETORNO_SEM_VALOR;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SimpleDbHelper.INSTANCE.close();
    }

}
