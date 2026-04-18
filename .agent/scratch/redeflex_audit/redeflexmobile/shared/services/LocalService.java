package com.axys.redeflexmobile.shared.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.services.tasks.LocalizacaoTask;

public class LocalService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String ANDROID_CHANNEL_ID = "service_channel";
    private static final String ANDROID_CHANNEL_NAME = "RFM Services";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ensureNotificationChannel();

            Notification.Builder builder = new Notification.Builder(this, ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_icone_new)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Executando serviço")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setAutoCancel(true);

            startForeground(NOTIFICATION_ID, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_icone_new)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Executando serviço")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            startForeground(NOTIFICATION_ID, builder.build());
        }
    }

    private void ensureNotificationChannel() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        NotificationChannel channel = new NotificationChannel(
                ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        );
        nm.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        new LocalizacaoTask(getApplicationContext()).execute();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}