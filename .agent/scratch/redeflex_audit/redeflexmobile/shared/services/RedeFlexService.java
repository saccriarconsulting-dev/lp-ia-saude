package com.axys.redeflexmobile.shared.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.services.tasks.BaseDadosAppTask;
import com.axys.redeflexmobile.shared.services.tasks.CartaoPontoTask;
import com.axys.redeflexmobile.shared.services.tasks.ClientInfoTask;
import com.axys.redeflexmobile.shared.services.tasks.ClientePendenciaTask;
import com.axys.redeflexmobile.shared.services.tasks.ComandosTask;
import com.axys.redeflexmobile.shared.services.tasks.ComprovanteSkyTaTask;
import com.axys.redeflexmobile.shared.services.tasks.DeletaDadosTask;
import com.axys.redeflexmobile.shared.services.tasks.PosTask;
import com.axys.redeflexmobile.shared.services.tasks.ValidaCadastroTask;
import com.axys.redeflexmobile.shared.services.tasks.VendaSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.VisitaSyncTask;
import com.axys.redeflexmobile.shared.util.Utilidades;

import static android.graphics.BitmapFactory.decodeResource;

public class RedeFlexService extends Service {

    private static final String ANDROID_CHANNEL_ID = "service_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification.Builder builder = new Notification.Builder(this, ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_icone_new)
                    .setLargeIcon(decodeResource(getResources(), R.mipmap.ic_icone_new))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Executando serviço")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setAutoCancel(true);

            builder.setChannelId(ANDROID_CHANNEL_ID);
            NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH));

            Notification notification = builder.build();
            startForeground(1, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_icone_new)
                    .setLargeIcon(decodeResource(getResources(), R.mipmap.ic_icone_new))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Executando serviço")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (Utilidades.isConectado(this.getBaseContext())) {
            new PosTask(getApplicationContext()).execute();
            new VendaSyncTask(getApplicationContext()).execute();
            new VisitaSyncTask(getApplicationContext()).execute();
            new BaseDadosAppTask(getApplicationContext()).execute();
            new ComandosTask(getApplicationContext()).execute();
            new ValidaCadastroTask(getApplicationContext()).execute();
            new DeletaDadosTask(getApplicationContext()).execute();
            new ComprovanteSkyTaTask(getApplicationContext()).execute();
            new CartaoPontoTask(getApplicationContext()).execute();
            new ClientePendenciaTask(getApplicationContext()).execute();
            new ClientInfoTask(getApplicationContext()).execute();

            return START_STICKY;
        }

        return START_NOT_STICKY;
    }
}
