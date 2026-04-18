package com.axys.redeflexmobile.shared.services.googlemapsapi;

import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Rogério Massa on 31/01/19.
 */

public class GoogleMapsApiUrlSigner {

    private static final String URL_STRING = "https://maps.googleapis.com/maps/api/directions/json" +
            "?origin=%s" +
            "&destination=%s" +
            "&mode=driving" +
            "&language=pt_BR" +
            "&alternatives=true" +
            "&client=%s";

    private static final String KEY_STRING = "TFoKGmBixYJwl60P5wZA9qiBAYQ=";
    private static final String CLIENT_STRING = "gme-redeflex";
    private static final String LAT_LNG_FORMAT = "%s,%s";
    private static final String MODE_DRIVING = "driving";
    private static final String LANGUAGE = "pt_BR";

    private LatLng latLongStart;
    private LatLng latLongEnd;
    private String mode;
    private String language;

    public GoogleMapsApiUrlSigner(LatLng latLongStart, LatLng latLongEnd) {
        this.latLongStart = latLongStart;
        this.latLongEnd = latLongEnd;
        this.mode = MODE_DRIVING;
        this.language = LANGUAGE;
    }

    public String getOrigin() {
        return String.format(LAT_LNG_FORMAT, latLongStart.latitude, latLongStart.longitude);
    }

    public String getDestination() {
        return String.format(LAT_LNG_FORMAT, latLongEnd.latitude, latLongEnd.longitude);
    }

    public String getMode() {
        return mode;
    }

    public String getLanguage() {
        return language;
    }

    public String getClientKey() {
        return CLIENT_STRING;
    }

    public String getSecretKey() {

        String signature = EMPTY_STRING;

        try {

            String keyString = KEY_STRING.replace('-', '+');
            keyString = keyString.replace('_', '/');
            byte[] key = Base64.decode(keyString, Base64.DEFAULT);

            URL url = new URL(String.format(URL_STRING, getOrigin(), getDestination(), CLIENT_STRING));
            String resource = url.getPath() + '?' + url.getQuery();
            SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(sha1Key);
            byte[] sigBytes = mac.doFinal(resource.getBytes());

            signature = Base64.encodeToString(sigBytes, Base64.DEFAULT);
            signature = signature.replace('+', '-');
            signature = signature.replace('/', '_');

        } catch (MalformedURLException e) {
            Timber.e(e);
            return signature;
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        } catch (InvalidKeyException e) {
            Timber.e(e);
        }

        return signature.substring(0, 28);
    }
}
