package com.axys.redeflexmobile.shared.services.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author diego on 30/09/17.
 */

public class NetworkReceiver extends BroadcastReceiver {

    private final PublishSubject<Boolean> internet = PublishSubject.create();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = isConnectionInternet(context);
            internet.onNext(isConnected);
        }
    }

    public Observable<Boolean> isConnected() {
        return internet;
    }

    private boolean isConnectionInternet(final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
}
