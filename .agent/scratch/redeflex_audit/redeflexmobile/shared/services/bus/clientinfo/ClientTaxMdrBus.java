package com.axys.redeflexmobile.shared.services.bus.clientinfo;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientTaxMdr;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;
import com.axys.redeflexmobile.shared.network.ApiClient;
import com.axys.redeflexmobile.shared.network.Result;
import com.axys.redeflexmobile.shared.network.api.ApiService;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.BaseBus;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientTaxMdrBus extends BaseBus {

    public static void get(int tipoCarga, Context context) {
        try {
            DBClientTaxMdr dbClientTaxMdr = new DBClientTaxMdr(context);
            Colaborador colaborador = new DBColaborador(context).get();

            ApiClient apiClient = ApiClient.getInstance(context);
            ApiService apiService = apiClient.createService(ApiService.class);
            Result<ClientTaxMdr[]> result = apiClient.executeCall(apiService.fetchClientTaxMdr(colaborador.getId(), tipoCarga));
            ClientTaxMdr[] clientTaxes;

            if (result.isSuccess() && result.getData() != null) {
                clientTaxes = result.getData();
            } else {
                clientTaxes = new ClientTaxMdr[0];
            }

            List<Integer> idList = new ArrayList<>();
            for (ClientTaxMdr clientTaxMdr : clientTaxes) {
                if (clientTaxMdr.isActive()) dbClientTaxMdr.insert(clientTaxMdr);
                else dbClientTaxMdr.deleteById(clientTaxMdr.getId());

                idList.add(clientTaxMdr.getId());
            }

            setSyncAsJsonV2(apiClient, apiService, URLs.URL_CLIENT_MDR_TAX, idList, colaborador.getId());
        } catch (IOException ex) {
            ex.printStackTrace();
            Timber.e(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
        }
    }
}
