package com.axys.redeflexmobile.shared.services.bus.clientinfo;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.BaseBus;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author lucasmarciano on 01/07/20
 */
public class FlagsBankBus extends BaseBus {

    public static void get(int load, Context context) {
        try {
            DBFlagsBank db = new DBFlagsBank(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String url = URLs.URL_FLAGS + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + load;
            FlagsBank[] array = Utilidades.getArrayObject(url, FlagsBank[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (FlagsBank flagsBank : array) {
                    if (flagsBank.isActive()) db.insert(flagsBank);
                    else db.deleteById(flagsBank.getId());

                    idList.add(flagsBank.getId());
                }
                setSync(URLs.URL_FLAGS, idList, colaborador.getId());
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
