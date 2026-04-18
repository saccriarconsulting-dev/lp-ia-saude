package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.LocalizacaoClienteBus;

/**
 * Created by joao.viana on 25/10/2016.
 */

public class LocalizacaoClienteTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public LocalizacaoClienteTask(Context pContext) {
        this.mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            LocalizacaoClienteBus.enviarLocalizacao(this.mContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }
}