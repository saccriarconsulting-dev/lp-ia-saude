package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.network.ApiClient;
import com.axys.redeflexmobile.shared.network.Result;
import com.axys.redeflexmobile.shared.network.api.ApiService;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import timber.log.Timber;

public class SugestaoVendaBus extends BaseBus {

    private static final String SUGESTAO_VENDA_BUS = "SugestaoVendaBus";

    public static <T> Stream<List<T>> chunked(T[] array, int size) {
        return IntStream.range(0, (array.length + size - 1) / size)
                .mapToObj(i -> Arrays.asList(
                        Arrays.copyOfRange(array, i * size, Math.min(array.length, (i + 1) * size))
                ));
    }


    // TODO: migrate to retrofit & chunk
    public static void obterSugestaoVenda(String tipoCarga, Context context) {
        try {
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();
            DBSugestaoVenda dbSugestaoVenda = new DBSugestaoVenda(context);

            ApiClient client = ApiClient.getInstance(context);
            ApiService service = client.createService(ApiService.class);
            Result<SugestaoVenda[]> result = client.executeCall(service.getSugestaoVendaRaw(colaborador.getId(), tipoCarga));

            SugestaoVenda[] sugestaoVendas;
            if (result.isSuccess() && result.getData() != null) {
                sugestaoVendas = result.getData();
            } else {
                sugestaoVendas = new SugestaoVenda[0];
            }

            ExecutorService pool = Executors.newFixedThreadPool(4);
            chunked(sugestaoVendas, 100).forEachIndexed((index, chunk) -> {
                try {
                    pool.submit(() -> {
                        try {
                            List<Integer> ids = new ArrayList<>();
                            chunk.forEach(sugestaoVenda -> {
                                dbSugestaoVenda.salvar(sugestaoVenda);
                                ids.add((int) sugestaoVenda.getId());
                            });
                            if (!ids.isEmpty()) {
                                setSyncAsJsonV2(client, service, URLs.SUGESTAO_VENDA, ids, colaborador.getId());
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
        } catch (Exception e) {
            Log.e("log", e.getMessage(), e);
            Timber.tag(SUGESTAO_VENDA_BUS).e(e);
        }
    }

    private static RequestModelGeneric<SugestaoVenda> converterDados(RequestModel dados) {
        RequestModelGeneric<SugestaoVenda> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.models = new ArrayList<>();
        requestModelGeneric.exception = dados.error;

        Gson gson = Utilidades.getGson();
        try {
            requestModelGeneric.models = gson.fromJson(dados.result, new TypeToken<List<SugestaoVenda>>() {
            }.getType());
            return requestModelGeneric;
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        } catch (JsonParseException e) {
            Timber.e(e);
        } catch (NullPointerException e) {
            Timber.e(e);
        }

        return requestModelGeneric;
    }
}
