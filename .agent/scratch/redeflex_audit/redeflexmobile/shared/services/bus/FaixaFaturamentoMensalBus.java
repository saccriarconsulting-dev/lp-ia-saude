package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBFaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.FaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class FaixaFaturamentoMensalBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int tipoCarga, Context context) {
        try {
            DBFaixaDeFaturamentoMensal dbFaixaDeFaturamentoMensal = new DBFaixaDeFaturamentoMensal(context);
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.FAIXA_FATURAMENTOMENSAL + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(tipoCarga);
            FaixaDeFaturamentoMensal[] array = Utilidades.getArrayObject(urlfinal, FaixaDeFaturamentoMensal[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (FaixaDeFaturamentoMensal faixaDeFaturamentoMensal : array) {
                    dbFaixaDeFaturamentoMensal.addFaixaDeFaturamentoMensal(faixaDeFaturamentoMensal);
                    idList.add(faixaDeFaturamentoMensal.getId());
                }
                setSync(URLs.FAIXA_FATURAMENTOMENSAL, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
