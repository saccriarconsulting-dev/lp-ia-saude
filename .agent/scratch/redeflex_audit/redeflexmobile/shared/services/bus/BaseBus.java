package com.axys.redeflexmobile.shared.services.bus;

import android.util.Log;

import com.axys.redeflexmobile.shared.network.ApiClient;
import com.axys.redeflexmobile.shared.network.api.ApiService;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diego.lobo on 15/03/2018.
 */

public class BaseBus {

    protected static void setSyncAsJsonV2(ApiClient client, ApiService service, String pUrl, List<Integer> ids, Integer pIdVendedor) {
        try {
            Map<String, Object> payload = new HashMap<>(2);
            payload.put("idVendedor", pIdVendedor);
            payload.put("ids", ids);
            client.executeCall(service.sync(pUrl, payload));
        } catch (Throwable ex) {
            FirebaseCrashlytics.getInstance().recordException(ex);
            Log.i("log", ex.getMessage(), ex);
        }
    }

    //    @SuppressWarnings("TryWithIdenticalCatches")
    protected static void setSyncAsJson(String pUrl, JSONArray jsonArray, Integer pIdVendedor) {
        try {
            JSONObject main = new JSONObject();
            main.put("idVendedor", pIdVendedor);
            main.put("ids", jsonArray);
            Utilidades.postRegistros(new URL(pUrl), main.toString());
        } catch (Throwable ex) {
            FirebaseCrashlytics.getInstance().recordException(ex);
            Log.i("log", ex.getMessage(), ex);
        }
    }

    //    @SuppressWarnings("TryWithIdenticalCatches")
    protected static void setSync(String pUrl, List<Integer> idList, Integer pIdVendedor) {
        try {
            JSONObject main = new JSONObject();
            main.put("idVendedor", pIdVendedor);
            JSONArray jsonArray = new JSONArray();

            if (idList.size() > 0) {
                for (Integer id : idList) {
                    jsonArray.put(id);
                }

                main.put("ids", jsonArray);
                Utilidades.postRegistros(new URL(pUrl), main.toString());
            }
        } catch (Throwable ex) {
            FirebaseCrashlytics.getInstance().recordException(ex);
            Log.i("log", ex.getMessage(), ex);
        }
    }

    protected static void setSyncLong(String pUrl, ArrayList<Long> idList, Integer pIdVendedor) {
        try {
            JSONObject main = new JSONObject();
            main.put("idVendedor", pIdVendedor);
            JSONArray jsonArray = new JSONArray();

            if (idList.size() > 0) {
                for (Long id : idList) {
                    jsonArray.put(id);
                }

                main.put("ids", jsonArray);
                Utilidades.postRegistros(new URL(pUrl), main.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static void setSyncString(String pUrl, ArrayList<String> idList, Integer pIdVendedor) {
        try {
            JSONObject main = new JSONObject();
            main.put("idVendedor", pIdVendedor);
            JSONArray jsonArray = new JSONArray();

            if (idList.size() > 0) {
                for (String id : idList) {
                    jsonArray.put(id);
                }

                main.put("ids", jsonArray);
                Utilidades.postRegistros(new URL(pUrl), main.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
