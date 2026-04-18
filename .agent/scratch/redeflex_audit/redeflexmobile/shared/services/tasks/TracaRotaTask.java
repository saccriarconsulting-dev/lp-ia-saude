package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 17/08/2017.
 */

public class TracaRotaTask extends AsyncTask<String, Void, ArrayList<LatLng>> {
    private Context mContext;
    private GoogleMap mGoogleMap;

    public TracaRotaTask(Context pContext, GoogleMap pGoogleMap) {
        mContext = pContext;
        mGoogleMap = pGoogleMap;
    }

    protected ArrayList<LatLng> doInBackground(String... params) {
        try {
            String str_origin = "origin=" + params[0] + "," + params[1];
            String str_dest = "destination=" + params[2] + "," + params[3];
            String parameters = str_origin + "&" + str_dest + "&sensor=false&mode=driving";
            String urlfinal = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters;
            return consultar(urlfinal);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onPostExecute(ArrayList<LatLng> result) {
        super.onPostExecute(result);
        try {
            if (result != null) {
                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.addAll(result);
                lineOptions.width(12);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    lineOptions.color(mContext.getResources().getColor(R.color.colorPrimary, mContext.getTheme()));
                else
                    lineOptions.color(mContext.getResources().getColor(R.color.colorPrimary));
                lineOptions.geodesic(true);
                mGoogleMap.addPolyline(lineOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng point : result)
                    builder.include(point);

                LatLngBounds bounds = builder.build();
                int padding = 20;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mGoogleMap.moveCamera(cameraUpdate);
                mGoogleMap.animateCamera(cameraUpdate, 2000, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<LatLng> consultar(String urlfinal) {
        try {
            ArrayList<LatLng> listCaminho = null;
            String resultString = Utilidades.getRegistros(urlfinal);
            if (resultString != null) {
                JSONObject jObject = new JSONObject(resultString);
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;
                String distancia = null;
                try {
                    jRoutes = jObject.getJSONArray("routes");
                    // Traçando as Rotas
                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");

                        // Traçando os Caminhos
                        for (int j = 0; j < jLegs.length(); j++) {
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                            try {
                                distancia = (String) ((JSONObject) jLegs.get(j)).getJSONObject("duration").get("text");
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                                distancia = "";
                            }

                            //Traçando os passos
                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                ArrayList<LatLng> arrayList = decodePoly(polyline);
                                if (listCaminho == null)
                                    listCaminho = new ArrayList<>();
                                listCaminho.addAll(arrayList);
                            }
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return listCaminho;
            } else
                return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<>();
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

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}