package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.ComprovanteSkyTaBusImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;

import java.lang.ref.WeakReference;

public class ComprovanteSkyTaTask extends AsyncTask<String, Void, String> {

    private WeakReference<Context> context;

    public ComprovanteSkyTaTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        if (context.get() == null) {
            return StringUtils.EMPTY_STRING;
        }
        try {
            ComprovanteSkyTaBusImpl.enviar(context.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return StringUtils.EMPTY_STRING;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }
}
