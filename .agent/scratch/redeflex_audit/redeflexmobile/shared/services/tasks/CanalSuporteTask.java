package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.CanalSuporteBus;

/**
 * Created by joao.viana on 10/07/2017.
 */

public class CanalSuporteTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public CanalSuporteTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            CanalSuporteBus.enviarSolicitacao(mContext);
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