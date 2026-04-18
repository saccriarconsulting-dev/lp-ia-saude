package com.axys.redeflexmobile.shared.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.axys.redeflexmobile.worker.SyncWorker;

/**
 * Created by Desenvolvimento on 12/05/2016.
 */
public class AlarmSyncReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SyncWorker.start(context, RedeFlexService.class);
    }
}