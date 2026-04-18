package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.ChamadosBus;

/**
 * Created by joao.viana on 08/03/2017.
 */

public class ChamadosTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public ChamadosTask(Context pContext, int pTipoOperacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ChamadosBus.enviarChamados(mContext);
            ChamadosBus.getChamados(mTipoOperacao, mContext);
            ChamadosBus.enviarInteracoes(mContext);
            ChamadosBus.getInteracoes(mTipoOperacao, mContext);
            ChamadosBus.enviarAnexos(mContext);
            ChamadosBus.getAnexos(mTipoOperacao, mContext);
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