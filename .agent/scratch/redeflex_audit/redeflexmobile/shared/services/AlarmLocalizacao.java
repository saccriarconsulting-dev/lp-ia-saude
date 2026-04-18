package com.axys.redeflexmobile.shared.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.worker.SyncWorker;

public class AlarmLocalizacao extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utilidades.verificarHorarioComercial(context, false)) {
            SyncWorker.start(context, LocalService.class);
        }
    }
}