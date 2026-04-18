package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.EstoqueBus;
import com.axys.redeflexmobile.shared.services.bus.IccidBus;

/**
 * Created by Desenvolvimento on 24/04/2016.
 */
public class EstoqueSyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public EstoqueSyncTask(Context pContext, int pTipoOperacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            EstoqueBus.getProdutos(mTipoOperacao, mContext);
            EstoqueBus.enviarAuditoria(mContext);
            EstoqueBus.enviarAuditoriaCliente(mContext);
            EstoqueBus.getEstrutura(mTipoOperacao, mContext);
            IccidBus.getIccid(mTipoOperacao, mContext);
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