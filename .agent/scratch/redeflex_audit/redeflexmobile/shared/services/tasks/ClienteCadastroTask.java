package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.ClienteCadastroBus;

/**
 * Created by joao.viana on 09/09/2016.
 */
public class ClienteCadastroTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int iTipoOperacao = 1;

    public ClienteCadastroTask(Context _context, int _tipoOperacao) {
        mContext = _context;
        iTipoOperacao = _tipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ClienteCadastroBus.enviarCadastroCliente(mContext);
            ClienteCadastroBus.enviarAnexos(mContext);
            ClienteCadastroBus.getRetorno(mContext, iTipoOperacao);
            ClienteCadastroBus.getRetornoDoc(mContext);
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