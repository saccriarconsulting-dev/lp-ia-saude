package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class ConsignadoBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarConsignado(Context pContext) {
        BDConsignado bdConsignado = new BDConsignado(pContext);

        ArrayList<Consignado> list = bdConsignado.getConsignadoPendente();
        for (Consignado pConsignado : list) {

            try {
                URL url = new URL(URLs.URL_CONSIGNADO);
                String json = Utilidades.getJsonFromClass(pConsignado);
                String response = Utilidades.putRegistros(url, json);
                if (response != null) {
                    Consignado returConsignado = Utilidades.getClassFromJson(response, Consignado.class);
                    if (returConsignado != null)
                        bdConsignado.updateSyncConsignado(returConsignado);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // Roni
    public static void getConsignado(int tipoCarga, Context context) {
        try {
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            BDConsignado dbConsignado = new BDConsignado(context);
            BDConsignadoItem dbConsignadoItem = new BDConsignadoItem(context);
            BDConsignadoItemCodBarra dbConsignadoItemCodBarra = new BDConsignadoItemCodBarra(context);

            String urlfinal = URLs.URL_CONSIGNADO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            Consignado[] array = Utilidades.getArrayObject(urlfinal, Consignado[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Consignado item : array) {
                    item.setId(0);
                    item.setDataSync(new Date());
                    long idConsignado = dbConsignado.addConsignado(item);
                    for (ConsignadoItem itemConsignado : item.getItens()) {
                        itemConsignado.setId(0);
                        itemConsignado.setIdConsignado((int) idConsignado);
                        long idItemConsignado = dbConsignadoItem.addConsignadoItem(itemConsignado);
                        for (ConsignadoItemCodBarra codBarraConsignado : itemConsignado.getListaCodigoBarra())
                        {
                            codBarraConsignado.setId(0);
                            codBarraConsignado.setIdConsignado((int) idConsignado);
                            codBarraConsignado.setIdConsignadoItem((int) idItemConsignado);
                            dbConsignadoItemCodBarra.addConsignadoItemCodBarra(codBarraConsignado);
                        }
                    }
                    idList.add(item.getIdServer());
                }
                setSync(URLs.URL_CONSIGNADO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
