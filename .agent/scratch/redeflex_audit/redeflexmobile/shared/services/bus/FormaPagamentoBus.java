package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 19/09/2016.
 */
public class FormaPagamentoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getFormaPagamentos(int pTipoCarga, Context pContext) {
        try {
            DBFormaPagamento dbFormaPagamento = new DBFormaPagamento(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            if (pTipoCarga == 1 && !dbFormaPagamento.existeFormaPagamento())
                pTipoCarga = 0;
            String urlfinal = URLs.FORMA_PAGAMENTO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            FormaPagamento[] array = Utilidades.getArrayObject(urlfinal, FormaPagamento[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (FormaPagamento item : array) {
                    dbFormaPagamento.addFormaPagamento(item);
                    idList.add(item.getId());
                }
                setSync(URLs.FORMA_PAGAMENTO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int pIdVendedor, int pIdFormaPagamento) {
//        try {
//            String urlfinal = URLs.FORMA_PAGAMENTO + "?idVendedor=" + String.valueOf(pIdVendedor) + "&idFormaPagamento=" + String.valueOf(pIdFormaPagamento);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
}