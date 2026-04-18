package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.services.bus.ConsignadoBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBoletoBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBus;

/**
 * Created by Desenvolvimento on 07/07/2016.
 */
public class VendaSyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public VendaSyncTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            VendaBus.enviarVendasMobile(mContext);
            VendaBus.enviarMobileCodigoBarras(mContext);
            VendaBoletoBus.getBoletosGerados(mContext);
            ConsignadoBus.enviarConsignado(mContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}