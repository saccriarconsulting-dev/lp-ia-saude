package com.axys.redeflexmobile.shared.services.googlemapsapi;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 31/01/19.
 */

public interface GoogleMapsApiManager {

    Single<List<GoogleMapsApi>> getRoute(LatLng start, LatLng end);
}
