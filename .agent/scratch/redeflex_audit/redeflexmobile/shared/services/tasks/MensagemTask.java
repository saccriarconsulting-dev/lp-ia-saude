package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.MensagemBus;

/**
 * Created by Desenvolvimento on 03/06/2016.
 */
public class MensagemTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public MensagemTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            MensagemBus.getMensagens(mContext);
            MensagemBus.setSyncVisualizacao(mContext);
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