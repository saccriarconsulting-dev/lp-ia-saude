package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPOS;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.UltimaDataTransacaoPOS;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Rogério Massa on 06/11/18.
 */

public class POSBus extends BaseBus {

    public static <T> Stream<List<T>> chunked(T[] array, int size) {
        return IntStream.range(0, (array.length + size - 1) / size)
                .mapToObj(i -> Arrays.asList(
                        Arrays.copyOfRange(array, i * size, Math.min(array.length, (i + 1) * size))
                ));
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getUltimaTransacaoPOS(int pTipoCarga, Context pContext) {
        try {
            Colaborador colaborador = new DBColaborador(pContext).get();
            DBPOS dbpos = new DBPOS(pContext);

            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.ULTIMA_DATA_TRANSACAO_POS, colaborador.getId(), pTipoCarga);

            UltimaDataTransacaoPOS[] itens = Utilidades.getArrayObject(urlfinal, UltimaDataTransacaoPOS[].class);
            if (itens != null && itens.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (UltimaDataTransacaoPOS item : itens) {
                    dbpos.addUltimaDataTransacaoPOS(item);
                    idList.add(item.getId());
                }
                setSync(URLs.ULTIMA_DATA_TRANSACAO_POS, idList, colaborador.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getInformacoesGeraisPOS(int pTipoCarga, Context pContext) {
        try {
            Colaborador colaborador = new DBColaborador(pContext).get();
            DBPOS dbpos = new DBPOS(pContext);


            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.INFORMACOES_GERAIS_POS, colaborador.getId(), pTipoCarga);

            InformacaoGeralPOS[] itens = Utilidades.getArrayObject(urlfinal, InformacaoGeralPOS[].class);

            assert itens != null;
            ExecutorService pool = Executors.newFixedThreadPool(4);
            chunked(itens, 100).forEachIndexed((index, chunk) -> {
                try {
                    pool.submit(() -> {
                        try {
                            JSONArray array = new JSONArray();
                            chunk.forEach(item -> {
                                dbpos.addInformacaoGeralPOS(item);
                                array.put(item.getId());
                            });
                            if (array.length() > 0) {
                                setSyncAsJson(URLs.INFORMACOES_GERAIS_POS, array, colaborador.getId());
                            }
                        } catch (Exception ex) {
                            FirebaseCrashlytics.getInstance().recordException(ex);
                        }
                    });
                } catch (Exception ex) {
                    FirebaseCrashlytics.getInstance().recordException(ex);
                }
            });
            dbpos.removerInformacoesGeralPOSInativa();
        } catch (Throwable ex) {
            FirebaseCrashlytics.getInstance().recordException(ex);
            ex.printStackTrace();
        }
    }
}
