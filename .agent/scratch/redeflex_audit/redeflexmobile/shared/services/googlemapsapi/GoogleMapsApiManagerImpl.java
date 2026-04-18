package com.axys.redeflexmobile.shared.services.googlemapsapi;

import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApi.GoogleMapsApiItem;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;

/**
 * @author Rogério Massa on 31/01/19.
 */

public class GoogleMapsApiManagerImpl implements GoogleMapsApiManager {

    private GoogleMapsApiService googleMapsApiService;

    public GoogleMapsApiManagerImpl(GoogleMapsApiService googleMapsApiService) {
        this.googleMapsApiService = googleMapsApiService;
    }

    @Override
    public Single<List<GoogleMapsApi>> getRoute(LatLng start, LatLng end) {
        GoogleMapsApiUrlSigner signer = new GoogleMapsApiUrlSigner(start, end);
        return googleMapsApiService.getRoutes(signer.getOrigin(),
                signer.getDestination(),
                signer.getMode(),
                signer.getLanguage(),
                true,
                signer.getClientKey(),
                signer.getSecretKey())
                .flatMap(responseBody -> {
                    List<GoogleMapsApi> responseList = new ArrayList<>();
                    String result = responseBody.string();
                    if (StringUtils.isEmpty(result)) {
                        return Single.just(responseList);
                    }
                    final JSONObject json = new JSONObject(result);
                    return Single.just(parseData(json));
                });
    }

    private List<GoogleMapsApi> parseData(JSONObject jObject) {

        List<GoogleMapsApi> routes = new ArrayList<>();
        Gson gson = new Gson();

        try {

            JSONArray JARoutes = jObject.getJSONArray("routes");
            for (int i = 0; i < JARoutes.length(); i++) {

                GoogleMapsApi mapObject = new GoogleMapsApi();

                JSONArray JALegs = ((JSONObject) JARoutes.get(i)).getJSONArray("legs");
                for (int j = 0; j < JALegs.length(); j++) {

                    mapObject.distance = gson.fromJson(((JSONObject) JALegs.get(j)).getJSONObject("distance").toString(), GoogleMapsApiItem.class);
                    mapObject.duration = gson.fromJson(((JSONObject) JALegs.get(j)).getJSONObject("duration").toString(), GoogleMapsApiItem.class);
                    mapObject.latLng = new ArrayList<>();

                    JSONArray JASteps = ((JSONObject) JALegs.get(j)).getJSONArray("steps");
                    for (int k = 0; k < JASteps.length(); k++) {
                        String polyline = (String) ((JSONObject) ((JSONObject) JASteps.get(k)).get("polyline")).get("points");
                        mapObject.latLng.addAll(decodePoly(polyline));
                    }
                    routes.add(mapObject);
                }
            }
        } catch (JSONException e) {
            Timber.e(e);
        } catch (Exception e) {
            Timber.e(e);
        }
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
