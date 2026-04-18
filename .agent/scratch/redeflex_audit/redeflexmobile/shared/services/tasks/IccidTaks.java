package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.IccidBus;

/**
 * Created by joao.viana on 09/02/2017.
 */

public class IccidTaks extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public IccidTaks(Context pContext, int pTipoOperacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacao;
    }

    @Override
    protected String doInBackground(java.lang.String... params) {
        try {
            IccidBus.getIccid(this.mTipoOperacao, this.mContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(java.lang.String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }
}