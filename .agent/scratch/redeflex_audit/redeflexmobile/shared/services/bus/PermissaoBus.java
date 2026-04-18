package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPermissao;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Permissao;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 23/06/2016.
 */
public class PermissaoBus extends BaseBus {
    public static void getPermissoes(int tipoCarga, Context context) {
        try {
            DBPermissao dbPermissao = new DBPermissao(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.PERMISSAO + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + tipoCarga;
            Permissao[] array = Utilidades.getArrayObject(urlfinal, Permissao[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Permissao item : array) {
                    if (item.getAtivo().equalsIgnoreCase("N"))
                        dbPermissao.deletePermissaoById(item.getId());
                    else
                        dbPermissao.addPermissao(item);
                    idList.add(Integer.parseInt(item.getId()));
                }
                setSync(URLs.PERMISSAO, idList, colaborador.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int idVendedor, String idMenu) {
//        try {
//            String urlfinal = URLs.PERMISSAO + "?idVendedor=" + String.valueOf(idVendedor) + "&idMenu=" + idMenu;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
