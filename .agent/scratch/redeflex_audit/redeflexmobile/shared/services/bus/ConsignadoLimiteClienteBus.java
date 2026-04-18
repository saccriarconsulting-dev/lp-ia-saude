package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteCliente;
import com.axys.redeflexmobile.shared.bd.BDConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteCliente;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class ConsignadoLimiteClienteBus extends BaseBus{
    public static void getConsignadoLimiteCliente(int pTipoCarga, Context pContext) {
        try {
            BDConsignadoLimiteCliente dbConsignadoLimiteCliente = new BDConsignadoLimiteCliente(pContext);
            BDConsignadoLimiteProduto dbConsignadoLimiteProduto = new BDConsignadoLimiteProduto(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_CONSIGNADOLIMITECLIENTE + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;
            ConsignadoLimiteCliente[] array = Utilidades.getArrayObject(urlfinal, ConsignadoLimiteCliente[].class);

            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (ConsignadoLimiteCliente consignadoLimiteCliente : array) {
                    dbConsignadoLimiteCliente.addConsignadoLimiteCliente(consignadoLimiteCliente);

                    // Grava os dados dos Produtos a Consignar
                    dbConsignadoLimiteProduto.deleteByIdLimite(String.valueOf(consignadoLimiteCliente.getId()));
                    for (ConsignadoLimiteProduto consignadoLimiteProduto : consignadoLimiteCliente.getConsignadoLimiteProdutoList())
                    {
                        dbConsignadoLimiteProduto.addConsignadoLimiteProduto(consignadoLimiteProduto);
                    }
                    idList.add(consignadoLimiteCliente.getId());
                }
                setSync(URLs.URL_CONSIGNADOLIMITECLIENTE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
