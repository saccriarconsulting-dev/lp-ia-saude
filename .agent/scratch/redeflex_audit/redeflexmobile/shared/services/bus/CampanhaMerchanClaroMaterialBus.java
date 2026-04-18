package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.BDCampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.CampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CampanhaMerchanClaroMaterialBus extends BaseBus {
    private static final String CAMPANHAMERCHANCLAROMATERIAL_BUS = "CampanhaMerchanClaroMaterialBus";

    public static void obterCampanhaMerchanClaroMaterial(int load, Context context) {
        try {
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String url = String.format(
                    "%s?idVendedor=%s&tipoCarga=%s",
                    URLs.URL_CAMPANHAMERCHANCLAROMATERIAL,
                    colaborador.getId(),
                    load
            );
            RequestModel requestModel = Utilidades.getRequest(url);
            RequestModelGeneric<CampanhaMerchanClaroMaterial> requestModelGeneric = converterDados(requestModel);

            if(requestModelGeneric.models != null && !requestModelGeneric.models.isEmpty()) {
                ArrayList<Integer> ids = new ArrayList<>();
                BDCampanhaMerchanClaroMaterial dbMaterial = new BDCampanhaMerchanClaroMaterial(context);
                for (CampanhaMerchanClaroMaterial material : requestModelGeneric.models) {
                    dbMaterial.addCampanhaMerchanClaroMaterial(material);
                    ids.add(Integer.parseInt(material.getId()));
                }

                setSync(URLs.URL_CAMPANHAMERCHANCLAROMATERIAL, ids, colaborador.getId());
            }
        } catch (JsonSyntaxException e) {
            Timber.tag(CAMPANHAMERCHANCLAROMATERIAL_BUS).e(e);
        } catch (JsonParseException e) {
            Timber.tag(CAMPANHAMERCHANCLAROMATERIAL_BUS).e(e);
        } catch (NullPointerException e) {
            Timber.tag(CAMPANHAMERCHANCLAROMATERIAL_BUS).e(e);
        } catch (IOException e) {
            Timber.tag(CAMPANHAMERCHANCLAROMATERIAL_BUS).e(e);
        } catch (Exception e) {
            Timber.tag(CAMPANHAMERCHANCLAROMATERIAL_BUS).e(e);
        }
    }

    private static RequestModelGeneric<CampanhaMerchanClaroMaterial> converterDados(RequestModel dados) {
        RequestModelGeneric<CampanhaMerchanClaroMaterial> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.models = new ArrayList<>();
        requestModelGeneric.exception = dados.error;

        Gson gson = Utilidades.getGson();
        try {
            requestModelGeneric.models = gson.fromJson(dados.result, new TypeToken<List<CampanhaMerchanClaroMaterial>>() {
            }.getType());
            return requestModelGeneric;
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        } catch (JsonParseException e) {
            Timber.e(e);
        } catch (NullPointerException e) {
            Timber.e(e);
        }

        return requestModelGeneric;
    }

}
