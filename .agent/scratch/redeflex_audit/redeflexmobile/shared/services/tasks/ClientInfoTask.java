package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.services.bus.clientinfo.ClientHomeBankingBus;
import com.axys.redeflexmobile.shared.services.bus.clientinfo.ClientTaxMdrBus;
import com.axys.redeflexmobile.shared.services.bus.clientinfo.FlagsBankBus;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * @author lucasmarciano on 01/07/20
 */
public class ClientInfoTask extends AsyncTask<String, Void, String> {

    private static final int OPERATION_TYPE = 1;
    private final WeakReference<Context> context;

    public ClientInfoTask(final Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        if (context.get() == null) {
            return null;
        }

        try {
            ClientHomeBankingBus.get(OPERATION_TYPE, context.get());
            ClientTaxMdrBus.get(OPERATION_TYPE, context.get());
            FlagsBankBus.get(OPERATION_TYPE, context.get());
        } catch (Exception e) {
            Timber.w(e);
        }

        return null;
    }
}
