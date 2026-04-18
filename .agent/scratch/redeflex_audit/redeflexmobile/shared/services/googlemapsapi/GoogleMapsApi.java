package com.axys.redeflexmobile.shared.services.googlemapsapi;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * @author Rogério Massa on 01/02/19.
 */

public class GoogleMapsApi {

    public GoogleMapsApiItem distance;
    public GoogleMapsApiItem duration;
    public List<LatLng> latLng;

    public static class GoogleMapsApiItem {
        public String text;
        public int value;
    }
}
