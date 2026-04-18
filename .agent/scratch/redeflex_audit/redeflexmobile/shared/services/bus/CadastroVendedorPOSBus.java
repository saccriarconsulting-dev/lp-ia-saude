package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDCadastroVendedorPOS;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.CadastroVendedorPOS;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;


/**
 * Created by diego.lobo on 27/02/2018.
 */

public class CadastroVendedorPOSBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getVendedorPOS(int pTipoCarga, Context pContext) {
        try {
            BDCadastroVendedorPOS dbPOS = new BDCadastroVendedorPOS(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();

            if (pTipoCarga == 1 && !dbPOS.existeVendedorPOS())
                pTipoCarga = 0;

            String urlfinal = URLs.CADASTRO_VENDEDOR_POS + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            CadastroVendedorPOS[] array = Utilidades.getArrayObject(urlfinal, CadastroVendedorPOS[].class);
            if (array != null) {
                for (CadastroVendedorPOS vPOS : array) {
                    dbPOS.addPOS(vPOS);
                    setSync(vPOS.getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static void setSync(Integer idServer) {
        try {
            String urlfinal = URLs.CADASTRO_VENDEDOR_POS + "?idVendedor=" + idServer;
            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}