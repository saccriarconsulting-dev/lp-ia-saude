package com.axys.redeflexmobile.shared.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axys.redeflexmobile.shared.util.Utilidades;

/**
 * Created by Desenvolvimento on 12/05/2016.
 */
public class MyBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Utilidades.ativaInativaAlarmes(context, true);
        }
    }
}