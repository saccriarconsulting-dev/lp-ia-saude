package com.axys.redeflexmobile.shared.services.broadcast;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.MensagemTask;
import com.axys.redeflexmobile.shared.services.tasks.OsSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.PermissaoSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.RemessaTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Desenvolvimento on 12/02/2016.
 */

public class GcmMessageHandler extends IntentService {
    String mes;
    private Handler handler;

    private static final String TAG = GcmMessageHandler.class.getSimpleName();

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String msg = extras.getString("message");
            Log.i(TAG, "Mensagem: " + ((msg == null) ? "Vazio" : msg));
            if (msg != null) {
                if (msg.equals("OS")) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new OsSyncTask(getApplicationContext(), 1).execute();
                        }
                    });
                } else if (msg.equals("MSG")) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new MensagemTask(getApplicationContext()).execute();
                        }
                    });
                } else if (msg.equals("PERM")) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new PermissaoSyncTask(getApplicationContext(), 1).execute();
                        }
                    });
                } else if (msg.equals("REMESSA")) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new RemessaTask(getApplicationContext(), 1).execute();
                        }
                    });
                } else if (msg.equals("CLIENTE")) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            new ClienteSyncTask(getApplicationContext(), 1).execute();
                        }
                    });
                }
            }
            GcmBroadcastReceiver.completeWakefulIntent(intent);

        } catch (Exception erro) {
            mes = erro.toString();
            showToast();
        }
    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                Log.e(TAG, "Erro: " + mes);
            }
        });
    }
}