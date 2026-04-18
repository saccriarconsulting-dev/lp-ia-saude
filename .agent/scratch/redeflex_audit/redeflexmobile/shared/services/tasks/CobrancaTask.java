package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.CobrancaBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBoletoBus;

/**
 * Created by joao.viana on 14/07/2016.
 */
public class CobrancaTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public CobrancaTask(Context pContext, int pTipoOperacacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            CobrancaBus.getCobranca(mTipoOperacao, mContext);
            VendaBoletoBus.getBoletosGerados(mContext);
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