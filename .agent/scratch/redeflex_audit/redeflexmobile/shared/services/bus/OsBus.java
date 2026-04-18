package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.OS;
import com.axys.redeflexmobile.shared.models.OrdemServico;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 12/05/2016.
 */
public class OsBus extends BaseBus {
    public static void agendamentoAutomatico(Context context) {
        try {
            DBOs dbOs = new DBOs(context);
            dbOs.setAgendamentoAutomatico();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void notificacoesPendentesAgendar(Context context) {
        try {
            DBOs dbOs = new DBOs(context);
            ArrayList<OS> list = dbOs.getPendenteAgendar();
            if (list != null) {
                if (list.size() > 5)
                    Notificacoes.Util(context, "Existem " + String.valueOf(list.size()) + " OS aguardando agendamento, Verifique!");
                else
                    for (OS item : list)
                        Notificacoes.OrdemServico("Agende o atendimento da OS!", item, context);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void notificacoesPendentesATender(Context context) {
        try {
            DBOs dbOs = new DBOs(context);
            ArrayList<OS> list = dbOs.getPendenteAtenderAtrasadas();
            if (list != null) {
                if (list.size() > 5)
                    Notificacoes.Util(context, "Existem " + String.valueOf(list.size()) + " OS aguardando atendimento, Verifique!");
                else
                    for (OS item : list)
                        Notificacoes.OrdemServico("Atendimento de OS pendente!", item, context);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getOS(Context context, int tipoCarga) {
        try {
            DBColaborador dbColaborador = new DBColaborador(context);
            DBOs dbOs = new DBOs(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.OS + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(tipoCarga);
            OrdemServico[] array = Utilidades.getArrayObject(urlfinal, OrdemServico[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (OrdemServico item : array) {
                    if (Util_IO.isNullOrEmpty(item.getDataCancelamento()))
                        dbOs.add(item);
                    else
                        dbOs.delete(item.getId());

                    idList.add(item.getId());
                }
                setSync(URLs.OS, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String idServer) {
//        try {
//            String urlfinal = URLs.OS + "?idServer=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}