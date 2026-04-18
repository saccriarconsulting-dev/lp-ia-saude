package com.axys.redeflexmobile.shared.services.bus.clientinfo;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientHomeBanking;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.BaseBus;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientHomeBankingBus extends BaseBus {

    public static void get(int load, Context context) {
        try {
            DBClientHomeBanking dbClientTaxMdr = new DBClientHomeBanking(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String url = URLs.URL_CLIENT_DOMICILIO_BANCARIO + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + load;
            ClientHomeBanking[] array = Utilidades.getArrayObject(url, ClientHomeBanking[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (ClientHomeBanking clientHomeBanking : array) {
                    if (clientHomeBanking.isActive()) dbClientTaxMdr.insert(clientHomeBanking);
                    else dbClientTaxMdr.deleteById(clientHomeBanking.getId());

                    idList.add(clientHomeBanking.getId());
                }
                setSync(URLs.URL_CLIENT_DOMICILIO_BANCARIO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Timber.e(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
        }
    }
}
