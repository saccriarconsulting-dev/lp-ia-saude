package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBAudOpe;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.AudOpe;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.network.ApiClient;
import com.axys.redeflexmobile.shared.network.Result;
import com.axys.redeflexmobile.shared.network.api.ApiService;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import timber.log.Timber;

/**
 * Created by joao.viana on 11/12/2017.
 */

public class AudOpeBus extends BaseBus {

    public static <T> Stream<List<T>> chunked(T[] array, int size) {
        return IntStream.range(0, (array.length + size - 1) / size)
                .mapToObj(i -> Arrays.asList(
                        Arrays.copyOfRange(array, i * size, Math.min(array.length, (i + 1) * size))
                ));
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    public static void get(int pTipoCarga, Context pContext) {
        try {
            DBAudOpe dbAudOpe = new DBAudOpe(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();

            ApiClient client = ApiClient.getInstance(pContext);
            ApiService service = client.createService(ApiService.class);
            Call<AudOpe[]> call = service.getAudOperadora(colaborador.getId(), pTipoCarga);

            Result<AudOpe[]> result = client.executeCall(call);
            AudOpe[] list;
            if (result.isSuccess() && result.getData() != null) {
                list = result.getData();
            } else {
                list = new AudOpe[0];
            }

            ExecutorService pool = Executors.newFixedThreadPool(4);
            chunked(list, 50).forEachIndexed((index, chunk) -> {
                try {
                    pool.submit(() -> {
                        try {
                            List<Integer> ids = new ArrayList<>();
                            chunk.forEach(audOpe -> {
                                try {
                                    dbAudOpe.addAudOpe(audOpe);
                                    ids.add(audOpe.getCodigo());
                                } catch (Exception ex) {
                                    FirebaseCrashlytics.getInstance().recordException(ex);
                                }
                            });
                            if (!ids.isEmpty()) {
                                setSyncAsJsonV2(client, service, URLs.AUDITORIA_OPERADORA, ids, colaborador.getId());
                            }
                        } catch (Exception ex) {
                            Timber.e(ex);
                            FirebaseCrashlytics.getInstance().recordException(ex);
                        }
                    });
                } catch (Exception ex) {
                    Timber.e(ex);
                    FirebaseCrashlytics.getInstance().recordException(ex);
                }
            });
        } catch (Throwable ex) {
            Timber.e(ex);
            FirebaseCrashlytics.getInstance().recordException(ex);
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(int pIdServer, int pIdColaborador) {
//        try {
//            String urlfinal = URLs.AUDITORIA_OPERADORA + "?idVendedor=" + String.valueOf(pIdColaborador) + "&idAuditagemOperadora=" + String.valueOf(pIdServer);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}