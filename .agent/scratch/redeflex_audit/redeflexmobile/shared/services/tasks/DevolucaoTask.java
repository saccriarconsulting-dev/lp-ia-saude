package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.DevolucaoBus;

/**
 * Created by joao.viana on 01/09/2017.
 */

public class DevolucaoTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public DevolucaoTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            DevolucaoBus.enviarDevolucao(mContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }
}