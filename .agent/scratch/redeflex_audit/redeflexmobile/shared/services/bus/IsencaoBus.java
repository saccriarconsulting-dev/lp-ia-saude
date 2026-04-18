package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBIsencao;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterExemption;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.services.bus.BaseBus.setSync;

/**
 * @author Rogério Massa on 06/02/19.
 */

public class IsencaoBus {

    public static void getPrazoIsencao(int tipoCarga, Context context) {

        try {
            DBIsencao dbIsencao = new DBIsencao(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String url = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.ISENCAO, colaborador.getId(), tipoCarga);

            ArrayList<Integer> idList = new ArrayList<>();
            CustomerRegisterExemption[] list = Utilidades.getArrayObject(url, CustomerRegisterExemption[].class);

            Stream.ofNullable(list).forEach(customerRegisterExemption -> {
                dbIsencao.salvar(customerRegisterExemption);
                idList.add(customerRegisterExemption.getId());
            });

            if (!idList.isEmpty()) {
                setSync(URLs.ISENCAO, idList, colaborador.getId());
            }

        } catch (Exception ex) {
            Timber.e(ex);
        }
    }
}
