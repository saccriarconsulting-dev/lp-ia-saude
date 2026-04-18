package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.RemessaBus;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class RemessaTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public RemessaTask(Context _context, int _tipoOperacao) {
        this.mContext = _context;
        this.mTipoOperacao = _tipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            RemessaBus.getRemessa(this.mTipoOperacao, this.mContext);
            RemessaBus.enviarConfirmacaoRemessa(this.mContext);
            RemessaBus.enviarAnexo(this.mContext);
            RemessaBus.getAnexo(this.mTipoOperacao, this.mContext);
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