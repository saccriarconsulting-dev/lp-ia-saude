package com.axys.redeflexmobile.shared.services;

import android.util.Log;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.MensagemTask;
import com.axys.redeflexmobile.shared.services.tasks.OsSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.PermissaoSyncTask;
import com.axys.redeflexmobile.shared.services.tasks.RemessaTask;
import com.axys.redeflexmobile.shared.services.tasks.RotaSyncTask;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Created by joao.viana on 20/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String CONTENT = "content";
    private static final String TYPE = "type";
    private static final String TEXT_PLAIN = "text/plain";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        DeviceUtils.saveStringInSharedPreferences(this, DeviceUtils.PREFS_FCM_TOKEN, s);
        Timber.e(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            Log.i(TAG, remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getBody().toUpperCase().contains("OS")) {
                new Thread() {
                    public void run() {
                        new OsSyncTask(getApplicationContext(), 1).execute();
                    }
                }.start();
            } else if (remoteMessage.getNotification().getBody().toUpperCase().contains("MENSAGEM")) {
                new Thread() {
                    public void run() {
                        new MensagemTask(getApplicationContext()).execute();
                    }
                }.start();
            } else if (remoteMessage.getNotification().getBody().toUpperCase().contains("ROTA")) {
                new Thread() {
                    public void run() {
                        new RotaSyncTask(getApplicationContext(), 1).execute();
                    }
                }.start();
            } else if (remoteMessage.getNotification().getBody().toUpperCase().contains("REMESSA")) {
                new Thread() {
                    public void run() {
                        new RemessaTask(getApplicationContext(), 1).execute();
                    }
                }.start();
            } else if (remoteMessage.getNotification().getBody().toUpperCase().contains("CLIENTE")) {
                new Thread() {
                    public void run() {
                        new ClienteSyncTask(getApplicationContext(), 1).execute();
                    }
                }.start();
            } else if (remoteMessage.getNotification().getBody().toUpperCase().contains("PERMISSAO")) {
                new Thread() {
                    public void run() {
                        new PermissaoSyncTask(getApplicationContext(), 1).execute();
                    }
                }.start();
            } else if (remoteMessage.getData().size() > 0) {
                if (remoteMessage.getData().containsKey(TYPE)) {
                    if (remoteMessage.getData().get(TYPE).equals(TEXT_PLAIN)) {
                        if (remoteMessage.getData().containsKey(CONTENT)) {
                            Notificacoes.chatBotNotification(getApplicationContext(), remoteMessage.getData().get(CONTENT));
                        }
                    } else {
                        Notificacoes.chatBotNotification(getApplicationContext(), getString(R.string.chat_notification_text));
                    }
                }
            } else {
                Notificacoes.Util(getApplicationContext(), remoteMessage.getNotification().getBody());
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
    }
}