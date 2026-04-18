package com.axys.redeflexmobile.shared.services.tasks;

import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.services.bus.SenhaMastersBus;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SenhaMastersTask extends AsyncTask<String, Void, RequestModelGeneric<String>> {

    private OnTaskAction onTaskAction;

    public SenhaMastersTask(OnTaskAction onTaskAction) {
        this.onTaskAction = onTaskAction;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected RequestModelGeneric<String> doInBackground(String... strings) {
        if (strings.length == 0) {
            throw new IllegalArgumentException("params == null");
        }
        RequestModel requestModel = SenhaMastersBus.obterSenhaMasters(strings[0]);
        return converterDados(requestModel);

    }

    private RequestModelGeneric<String> converterDados(RequestModel dados) {
        RequestModelGeneric<String> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.exception = dados.error;

        try {
            List<String> senha = new ArrayList<>();
            senha.add(dados.result);
            requestModelGeneric.models = senha;

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

    @Override
    protected void onPostExecute(RequestModelGeneric<String> senhaMasters) {
        super.onPostExecute(senhaMasters);
        onTaskAction.taskAction(senhaMasters);
    }

    public interface OnTaskAction {
        void taskAction(RequestModelGeneric<String> senha);
    }
}
