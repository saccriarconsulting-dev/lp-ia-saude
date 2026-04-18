package com.axys.redeflexmobile.shared.services;

import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Created by joao.viana on 20/12/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        Timber.e(newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}