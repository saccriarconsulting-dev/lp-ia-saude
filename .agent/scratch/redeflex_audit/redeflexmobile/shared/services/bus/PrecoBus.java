package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.RetornoVenda;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 18/08/2016.
 */
public class PrecoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getPrecoDiferenciado(int mTipoCarga, Context mContext) {
        try {
            DBPreco dbPreco = new DBPreco(mContext);
            DBColaborador dbColaborador = new DBColaborador(mContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.PRECO_DIFERENCIADO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + mTipoCarga;
            PrecoDiferenciado[] array = Utilidades.getArrayObject(urlfinal, PrecoDiferenciado[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (PrecoDiferenciado obj : array) {
                    dbPreco.addPreco(obj);
                    idList.add(obj.getId());
                }
                setSync(URLs.PRECO_DIFERENCIADO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSyncDiferenciado(String idServer) {
//        try {
//            String urlfinal = URLs.PRECO_DIFERENCIADO + "?idServer=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void atualizaIdVendaServer(Context context) {
        try {
            DBPreco dbPreco = new DBPreco(context);
            ArrayList<RetornoVenda> list = dbPreco.getPrecoPendenteSync();
            for (RetornoVenda pRetornoVenda : list) {
                String urlfinal = URLs.PRECO_CLIENTE + "?idServer=" + String.valueOf(pRetornoVenda.getIdPreco()) + "&idVenda=" + String.valueOf(pRetornoVenda.getIdVenda())
                        + "&idVendaItem=" + String.valueOf(pRetornoVenda.getIdVendaItem()) + "&quantidade=" + String.valueOf(pRetornoVenda.getQuantidade());
                int retorno = Utilidades.getObject(urlfinal, int.class);
                if (retorno == 1)
                    dbPreco.atualizaSync(String.valueOf(pRetornoVenda.getId()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}