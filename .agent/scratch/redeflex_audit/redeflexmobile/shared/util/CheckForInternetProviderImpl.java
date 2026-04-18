package com.axys.redeflexmobile.shared.util;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.axys.redeflexmobile.BuildConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Single;

public class CheckForInternetProviderImpl implements CheckForInternetProvider {

    private final ConnectivityManager connectivityManager;

    public CheckForInternetProviderImpl(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public Single<Boolean> checkForInternet() {
        return Single.defer(() -> {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(
                    connectivityManager.getActiveNetwork()
            );
            if (networkCapabilities == null) return Single.just(false);

            boolean isConnected = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);

            if (!isConnected) {
                return Single.just(false);
            }

            // Verifica se a conexão tem acesso à internet
            /*return Single.fromCallable(() -> {
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection)
                            (new URL("https://jsonplaceholder.typicode.com/posts").openConnection());
                    urlConnection.setRequestProperty("User-Agent", "Android");
                    urlConnection.setRequestProperty("Connection", "close");
                    urlConnection.setConnectTimeout(1500);
                    urlConnection.connect();
                    return (urlConnection.getResponseCode() == 200);
                } catch (IOException e) {
                    return false;
                }
            });*/

            return Single.just(true);
        });
    }
}
