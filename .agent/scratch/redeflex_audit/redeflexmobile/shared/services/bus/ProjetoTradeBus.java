package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBProjetoTrade;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;
import com.axys.redeflexmobile.shared.models.ProjetoTradeItens;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joao.viana on 05/10/2016.
 */

public class ProjetoTradeBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getProjeto(int tipoCarga, Context context) {
        try {
            DBProjetoTrade dbProjetoTrade = new DBProjetoTrade(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.PROJETO_TRADE + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            ProjetoTrade[] array = Utilidades.getArrayObject(urlfinal, ProjetoTrade[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (ProjetoTrade item : array) {
                    dbProjetoTrade.addProjeto(item);
                    idList.add(Integer.parseInt(item.getId()));
                }
                setSync(URLs.PROJETO_TRADE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSyncProjeto(String idServer) {
//        try {
//            String urlfinal = URLs.PROJETO_TRADE + "?id=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getProjetoItens(int tipoCarga, Context context) {
        try {
            DBProjetoTrade dbProjetoTrade = new DBProjetoTrade(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.PROJETO_TRADE_ITENS + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            ProjetoTradeItens[] array = Utilidades.getArrayObject(urlfinal, ProjetoTradeItens[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (ProjetoTradeItens item : array) {
                    dbProjetoTrade.addItens(item);
                    idList.add(Integer.parseInt(item.getId()));
                }
                setSync(URLs.PROJETO_TRADE_ITENS, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSyncItens(String idServer) {
//        try {
//            String urlfinal = URLs.PROJETO_TRADE_ITENS + "?id=" + idServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}