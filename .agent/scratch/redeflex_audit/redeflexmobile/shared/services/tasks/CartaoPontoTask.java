package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.services.bus.CartaoPontoBus;
import com.axys.redeflexmobile.shared.util.StringUtils;

import java.lang.ref.WeakReference;

/**
 * @author Denis Gasparoto on 02/05/2019.
 */

public class CartaoPontoTask extends AsyncTask<String, Void, String> {

    private WeakReference<Context> context;

    public CartaoPontoTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        if (context.get() == null) {
            return StringUtils.EMPTY_STRING;
        }

        try {
            CartaoPontoBus.enviarDadosCartaoPonto(context.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return StringUtils.EMPTY_STRING;
    }
}