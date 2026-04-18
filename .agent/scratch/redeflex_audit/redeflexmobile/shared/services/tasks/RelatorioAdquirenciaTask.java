package com.axys.redeflexmobile.shared.services.tasks;

import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.models.Adquirencia;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.services.bus.AdquirenciaBus;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class RelatorioAdquirenciaTask extends AsyncTask<String, Void, RequestModelGeneric<Adquirencia>> {

    private OnTaskAction onTaskAction;

    public RelatorioAdquirenciaTask(OnTaskAction onTaskAction) {
        this.onTaskAction = onTaskAction;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected RequestModelGeneric<Adquirencia> doInBackground(String... id) {
        if (id.length == 0) {
            throw new IllegalArgumentException("params == null");
        }
        RequestModel dados = AdquirenciaBus.obterDadosAdquirencia(id[0]);

        return converterDados(dados);
    }

    @Override
    protected void onPostExecute(RequestModelGeneric<Adquirencia> adquirencia) {
        super.onPostExecute(adquirencia);
        onTaskAction.taskAction(adquirencia);
    }

    private RequestModelGeneric<Adquirencia> converterDados(RequestModel dados) {
        RequestModelGeneric<Adquirencia> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.models = Collections.emptyList();
        requestModelGeneric.exception = dados.error;

        Gson gson = Utilidades.getGson();
        try {
            requestModelGeneric.models = gson.fromJson(dados.result, new TypeToken<List<Adquirencia>>() {
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
        void taskAction(RequestModelGeneric<Adquirencia> adquirencia);
    }
}
