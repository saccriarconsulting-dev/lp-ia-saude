package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.PermissaoBus;

/**
 * Created by Desenvolvimento on 23/06/2016.
 */
public class PermissaoSyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public PermissaoSyncTask(Context pContext, int pTipoOperacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            PermissaoBus.getPermissoes(mTipoOperacao, mContext);
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