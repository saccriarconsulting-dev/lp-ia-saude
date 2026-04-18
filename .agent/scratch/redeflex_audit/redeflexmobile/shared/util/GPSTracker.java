package com.axys.redeflexmobile.shared.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.axys.redeflexmobile.R;

import java.util.Date;
import java.util.List;

/**
 * Created by joao.viana on 21/10/2016.
 */

public class GPSTracker implements LocationListener, IGPSTracker {

    public static final double REDEFLEX_LATITUDE = -15.6092514;
    public static final double REDEFLEX_LONGITUDE = -56.0969861;
    public static final double START_LOCATION = 0.0;
    private static final String FLAG_POINT_FIRST = "point A";
    private static final String FLAG_POINT_SECOND = "point B";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    private final Context mContext;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;
    public boolean isMockLocationON = false;
    public Location location;
    protected LocationManager locationManager;
    private boolean canGetLocation = false;
    private double latitude;
    private double longitude;
    private double precisao;
    private Date dataGps;
    private LocationProvider locationProvider;

    public GPSTracker(Context pContext) {
        this.mContext = pContext;
        this.locationProvider = new LocationProvider(pContext);
        getLocation();
    }

    public static boolean isGPSActive(Context context) {
        if (isGPSEnabled(context)) {
            if (isGPSPermissionGranted(context)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isGPSEnabled(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    public static boolean isGPSPermissionGranted(Context context) {
        return context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void showAlertGPS() {
        Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "O GPS não está " +
                "habilitado. Para continuar você deve habitila-lo!");
        alerta.show();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v("isGPSEnabled", "=" + isGPSEnabled);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            precisao = location.getAccuracy();
                            dataGps = new Date(location.getTime());
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            precisao = location.getAccuracy();
                            dataGps = new Date(location.getTime());
                        }
                    }
                }
            }
            //verifica localização falsa
            isMockLocationON = false;
            if (android.os.Build.VERSION.SDK_INT >= 18 && location != null)
                isMockLocationON = location.isFromMockProvider();
            else
                isMockLocationON = !Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if (locationProvider.getLatitude() != START_LOCATION) {
            latitude = locationProvider.getLatitude();
        } else if (locationProvider.getLatitude() == START_LOCATION && location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (locationProvider.getLongitude() != START_LOCATION) {
            longitude = locationProvider.getLongitude();
        } else if (locationProvider.getLongitude() == START_LOCATION && location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public double getPrecisao() {
        if (locationProvider.getAccuracy() != START_LOCATION) {
            precisao = locationProvider.getAccuracy();
        } else if (locationProvider.getAccuracy() == START_LOCATION && location != null) {
            precisao = location.getAccuracy();
        }
        return precisao;
    }

    public Date getDataGps() {
        if (location != null) {
            dataGps = new Date(location.getTime());
        }
        return dataGps;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "O GPS não está " +
                "habilitado. Para continuar você deve habitila-lo!");
        alerta.showConfirm((dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
        }, "Configuração", (dialog, which) -> dialog.cancel(), "Cancelar");
    }

    public void showMockAlert() {
        Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "Existem aplicativos externos modificando sua localização, Verifique");
        alerta.show();
    }

    @Override
    public float getDistanceToMyLocation(Double latitude, Double longitude) {
        Location locationA = new Location(FLAG_POINT_FIRST);
        locationA.setLatitude(this.latitude);
        locationA.setLongitude(this.longitude);

        Location locationB = new Location(FLAG_POINT_SECOND);
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);

        return locationA.distanceTo(locationB);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        getLatitude();
        getLongitude();
        getPrecisao();
        getDataGps();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // unused
    }

    @Override
    public void onProviderEnabled(String provider) {
        // unused
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // unused
    }

    public boolean areThereMockPermissionApps() {
        int count = 0;
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;
                ApplicationInfo informacoes;
                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i].equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(mContext.getPackageName())
                                && !applicationInfo.packageName.toLowerCase().contains(Build.MANUFACTURER.toLowerCase())
                                && (Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("1"))) {

                            try {
                                informacoes = pm.getApplicationInfo(applicationInfo.packageName, 0);
                            } catch (final PackageManager.NameNotFoundException e) {
                                informacoes = null;
                                e.printStackTrace();
                            }

                            String name = applicationInfo.packageName;
                            if (informacoes != null)
                                name = String.valueOf(pm.getApplicationLabel(informacoes));
                            Alerta alerta = new Alerta(mContext, "Verifique o aplicativo", name);
                            alerta.show();
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        return count > 0;
    }

    @Override
    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    @Override
    public boolean isMockLocationON() {
        return isMockLocationON;
    }
}