package com.axys.redeflexmobile.shared.services.tasks;

import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.models.RelatorioSupervisorProduto;
import com.axys.redeflexmobile.shared.models.RelatorioSupervisorVendedor;
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

public class RelatorioSupervisorResumoTask extends AsyncTask<RelatorioSupervisorVendedor, Void, RequestModelGeneric<RelatorioSupervisorProduto>> {

    private OnTaskAction onTaskAction;
    private Vendedor vendedor;
    private int position;

    public RelatorioSupervisorResumoTask(OnTaskAction onTaskAction) {
        this.onTaskAction = onTaskAction;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected RequestModelGeneric<RelatorioSupervisorProduto> doInBackground(RelatorioSupervisorVendedor... relatorio) {
        if (relatorio.length == 0) {
            throw new IllegalArgumentException("params == null");
        }
        RequestModel dados = RelatorioSupervisorBus.obterResumoVenda(relatorio[0]);

        return converterDados(dados);
    }

    @Override
    protected void onPostExecute(RequestModelGeneric<RelatorioSupervisorProduto> produtos) {
        super.onPostExecute(produtos);
        onTaskAction.taskAction(produtos, vendedor, position);
    }

    public RelatorioSupervisorResumoTask setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;

        return this;
    }

    public RelatorioSupervisorResumoTask setPosition(int position) {
        this.position = position;

        return this;
    }

    private RequestModelGeneric<RelatorioSupervisorProduto> converterDados(RequestModel dados) {
        RequestModelGeneric<RelatorioSupervisorProduto> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.models = Collections.emptyList();
        requestModelGeneric.exception = dados.error;

        Gson gson = Utilidades.getGson();
        try {
            requestModelGeneric.models = gson.fromJson(dados.result, new TypeToken<List<RelatorioSupervisorProduto>>() {
            }.getType());
            return requestModelGeneric;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        return requestModelGeneric;
    }

    public interface OnTaskAction {
        void taskAction(RequestModelGeneric<RelatorioSupervisorProduto> produtos, Vendedor vendedor, int position);
    }
}
