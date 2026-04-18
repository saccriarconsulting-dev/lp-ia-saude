package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBMotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.services.bus.BaseBus.setSync;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class MotiveMigrationSubBus {
    public static void get(int pTipoCarga, Context pContext) {
        try {
            DBMotiveMigrationSub db = new DBMotiveMigrationSub(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_MOTIVO_MIGRACAO_SUB_GET + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;
            MotiveMigrationSub[] array = Utilidades.getArrayObject(urlfinal, MotiveMigrationSub[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (MotiveMigrationSub item : array) {
                    if (item.isAtivo()) {
                        db.add(item);
                    } else {
                        db.deleteById(String.valueOf(item.getId()));
                    }
                    idList.add(item.getId());
                }
                setSync(URLs.URL_MOTIVO_MIGRACAO_SUB_GET, idList, colaborador.getId());
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
