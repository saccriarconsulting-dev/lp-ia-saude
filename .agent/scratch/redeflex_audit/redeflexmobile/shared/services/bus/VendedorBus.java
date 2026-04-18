package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDVendedor;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Vendedor;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;

public class VendedorBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getVendedores(Context pContext) {
        try {
            BDVendedor dbVendedor = new BDVendedor(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();

            String urlfinal = URLs.VENDEDORES_SUPERVISOR + "/idSupervisor?idSupervisor=" + String.valueOf(colaborador.getId());
            Vendedor[] array = Utilidades.getArrayObject(urlfinal, Vendedor[].class);
            if (array != null && array.length > 0) {
                dbVendedor.deleteAll();
                for (Vendedor vendedor : array) {
                    dbVendedor.addVendedor(vendedor);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
