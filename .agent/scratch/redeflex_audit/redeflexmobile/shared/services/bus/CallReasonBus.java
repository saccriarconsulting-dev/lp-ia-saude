package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBCallReason;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.CallReason;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Interacoes;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author lucasmarciano on 16/07/20
 */
public class CallReasonBus extends BaseBus {

    public static void get(int load, Context context) {
        try {
            DBCallReason db = new DBCallReason(context);
            DBChamados dbChamados = new DBChamados(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String url = URLs.URL_CALL_RESPONSE + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + load;
            CallReason[] array = Utilidades.getArrayObject(url, CallReason[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (CallReason callReason : array) {
                    if (callReason.isActive()) {
                        db.add(callReason);
                    } else {
                        deleteInactive(db, callReason, dbChamados);
                    }
                    idList.add(callReason.getId());
                }
                setSync(URLs.URL_CALL_RESPONSE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Timber.e(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
        }
    }

    private static void deleteInactive(DBCallReason dbCallReason, CallReason callReason, DBChamados dbChamados) {
        List<Interacoes> interactionsList = dbChamados.selectAllCallsByReasonId(callReason.getId());
        if (interactionsList.size() <= 0) {
            dbCallReason.deleteById(callReason.getId());
        } else {
            dbCallReason.add(callReason);
        }
    }
}
