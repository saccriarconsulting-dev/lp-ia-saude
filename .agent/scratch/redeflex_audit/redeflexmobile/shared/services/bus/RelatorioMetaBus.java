package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRelatorioMeta;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RelatorioMeta;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;

/**
 * Created by Desenvolvimento on 01/06/2016.
 */
public class RelatorioMetaBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getRelatorioMeta(Context pContext, int pTipoCarga) {
        try {
            DBRelatorioMeta dbRelatorioMeta = new DBRelatorioMeta(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.RELATORIO_META + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            RelatorioMeta[] array = Utilidades.getArrayObject(urlfinal, RelatorioMeta[].class);
            if (array != null) {
                for (RelatorioMeta relatorio : array) {
                    if (relatorio.getAtivo().equals("S"))
                        dbRelatorioMeta.addRelatorio(relatorio);
                    else
                        dbRelatorioMeta.delete(relatorio.getIdIndicador(), relatorio.getIdOperadora());
                    setSync(colaborador.getId(), relatorio.getIdIndicador(), relatorio.getIdOperadora());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static void setSync(int idVendedor, int idIndicador, int idOperadora) {
        try {
            String urlfinal = URLs.RELATORIO_META + "?idVendedor=" + idVendedor
                    + "&idIndicador=" + idIndicador + "&idOperadora=" + idOperadora;
            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}