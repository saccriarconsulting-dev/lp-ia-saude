package com.axys.redeflexmobile.shared.services.network.util;

import com.axys.redeflexmobile.ui.redeflex.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static Gson gsonInstance;

    public static Gson getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder()
                    .setExclusionStrategies(new JsonExcludeStrategy())
                    .setLenient()
                    .setDateFormat(Config.FormatDateTimeStringBancoJson)
                    .create();
        }
        return gsonInstance;
    }

    public static boolean isJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
