package com.axys.redeflexmobile.shared.util;

import java.util.Date;

/**
 * @author Rogério Massa on 2019-06-14.
 */

public interface IGPSTracker {

    double getPrecisao();

    double getLatitude();

    double getLongitude();

    Date getDataGps();

    boolean isGPSEnabled();

    boolean isMockLocationON();

    boolean areThereMockPermissionApps();

    void showSettingsAlert();

    void showMockAlert();

    float getDistanceToMyLocation(Double latitude, Double longitude);
}