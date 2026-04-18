package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTaxaAdquirenciaPF;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.models.TaxasAdquirencia;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class TaxasAdquirenciaPFBus extends BaseBus {

    @SuppressWarnings("TryWithIdenticalCatches")
    public static RequestModel getTaxas(int tipoCarga, Context pContext) {
        RequestModel result = new RequestModel();
        try {
            DBTaxaAdquirenciaPF dbTaxasAdquirencia = new DBTaxaAdquirenciaPF(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();

            String url = String.format(
                    "%s?idVendedor=%s&tipoCarga=%s",
                    URLs.TAXASADQUIRENCIAPF,
                    String.valueOf(colaborador.getId()),
                    String.valueOf(tipoCarga)
            );

            result = Utilidades.getRequest(url);
            RequestModelGeneric<TaxasAdquirencia> generic = converterDados(result);
            List<TaxasAdquirencia> lista = generic.models;
            if (lista == null || lista.isEmpty()) {
                return result;
            }

            ArrayList<Integer> idList = new ArrayList<>();
            for (TaxasAdquirencia taxasAdquirencia : lista) {
                dbTaxasAdquirencia.addTaxas(taxasAdquirencia);
                idList.add(Integer.parseInt(taxasAdquirencia.getId()));
            }

            setSync(URLs.TAXASADQUIRENCIAPF, idList, colaborador.getId());
        } catch (IOException ex) {
            ex.printStackTrace();
            result.error = ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            result.error = ex;
        }

        return result;
    }

    private static RequestModelGeneric<TaxasAdquirencia> converterDados(RequestModel dados) {
        RequestModelGeneric<TaxasAdquirencia> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.models = Collections.emptyList();
        requestModelGeneric.exception = dados.error;

        Gson gson = Utilidades.getGson();
        try {
            requestModelGeneric.models = gson.fromJson(dados.result, new TypeToken<List<TaxasAdquirencia>>() {
            }.getType());
            return requestModelGeneric;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return requestModelGeneric;
    }
}