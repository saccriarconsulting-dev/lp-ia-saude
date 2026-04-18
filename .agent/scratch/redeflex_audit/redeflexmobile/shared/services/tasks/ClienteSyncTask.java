package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.ClienteBus;
import com.axys.redeflexmobile.shared.services.bus.SenhaClienteBus;
import com.axys.redeflexmobile.shared.services.bus.TokenClienteBus;

/**
 * Created by Desenvolvimento on 27/06/2016.
 */
public class ClienteSyncTask extends AsyncTask<String, Void, String> {
    public static final int CARGA_PARCIAL = 1;
    private Context mContext;
    private int mTipoOperacao = 1;

    public ClienteSyncTask(Context pContext, int pTipoOperacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ClienteBus.enviarAtualizacao(mContext);
            ClienteBus.getClientes(mTipoOperacao, mContext);
            SenhaClienteBus.get(mTipoOperacao, mContext);
            TokenClienteBus.get(mTipoOperacao, mContext);
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