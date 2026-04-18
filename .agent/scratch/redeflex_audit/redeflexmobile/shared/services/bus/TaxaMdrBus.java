package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.network.ApiClient;
import com.axys.redeflexmobile.shared.network.Result;
import com.axys.redeflexmobile.shared.network.api.ApiService;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import timber.log.Timber;

/**
 * @author Rogério Massa on 25/03/19.
 */

public class TaxaMdrBus extends BaseBus {

    public static <T> Stream<List<T>> chunked(T[] array, int size) {
        return IntStream.range(0, (array.length + size - 1) / size)
                .mapToObj(i -> Arrays.asList(
                        Arrays.copyOfRange(array, i * size, Math.min(array.length, (i + 1) * size))
                ));
    }

    public static void getTaxas(int pTipoCarga, Context pContext) {
        try {
            DBTaxaMdr dbTaxaMdr = new DBTaxaMdr(pContext);
            Colaborador collaborator = new DBColaborador(pContext).get();


            ApiClient client = ApiClient.getInstance(pContext);
            ApiService service = client.createService(ApiService.class);
            Call<TaxaMdr[]> call = service.getTaxas(collaborator.getId(), pTipoCarga);

            Result<TaxaMdr[]> result = ApiClient.getInstance(pContext).executeCall(call);
            TaxaMdr[] list;
            if (result.isSuccess() && result.getData() != null) {
                list = result.getData();
            } else {
                list = new TaxaMdr[0];
            }

            ExecutorService pool = Executors.newFixedThreadPool(4);

            FirebaseCrashlytics.getInstance().recordException(new Exception("size mdr: " + list.length));
            chunked(list, 100).forEachIndexed((index, chunk) -> {
                try {
                    pool.submit(() -> {
                        try {
                            List<Integer> array = new ArrayList<>();
                            chunk.forEach(taxMdr -> {
                                dbTaxaMdr.salvar(taxMdr);
                                array.add(taxMdr.getId());
                            });
                            if (!array.isEmpty()) {
                                setSyncAsJsonV2(client, service, URLs.TAXA_MDR, array, collaborator.getId());
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
            FirebaseCrashlytics.getInstance().recordException(ex);
            Timber.e(ex);
        }
    }
}
