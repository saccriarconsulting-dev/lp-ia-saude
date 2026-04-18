package com.axys.redeflexmobile.shared.services.tasks;

import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.models.Vendedor;
import com.axys.redeflexmobile.shared.services.bus.RelatorioSupervisorBus;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class RelatorioSupervisorVendedorTask extends AsyncTask<String, Void, RequestModelGeneric<Vendedor>> {

    private OnTaskAction onTaskAction;

    public RelatorioSupervisorVendedorTask(OnTaskAction onTaskAction) {
        this.onTaskAction = onTaskAction;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected RequestModelGeneric<Vendedor> doInBackground(String... strings) {
        if (strings.length == 0) {
            throw new IllegalArgumentException("params == null");
        }
        RequestModel resultado = RelatorioSupervisorBus.obterVendedores(strings[0]);
        return converterDados(resultado);
    }

    @Override
    protected void onPostExecute(RequestModelGeneric<Vendedor> vendedores) {
        super.onPostExecute(vendedores);
        onTaskAction.taskAction(vendedores);
    }

    private RequestModelGeneric<Vendedor> converterDados(RequestModel dados) {
        RequestModelGeneric<Vendedor> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.exception = dados.error;
        Gson gson = Utilidades.getGson();
        try {
            List<Vendedor> vendedores = gson.fromJson(dados.result, new TypeToken<List<Vendedor>>() {
            }.getType());
            vendedores.add(0, new Vendedor(-1, "RESUMO", 0));
            requestModelGeneric.models = vendedores;

            return requestModelGeneric;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        requestModelGeneric.models = Collections.emptyList();
        return requestModelGeneric;
    }

    public interface OnTaskAction {
        void taskAction(RequestModelGeneric<Vendedor> vendedores);
    }
}
