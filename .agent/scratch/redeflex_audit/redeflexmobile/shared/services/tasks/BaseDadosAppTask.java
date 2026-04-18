package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.CompressFile;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Desenvolvimento on 03/05/2016.
 */
public class BaseDadosAppTask extends AsyncTask<String, Void, Integer> {
    public static final String DATABASE_NAME = Config.PROJECT_NAME + ".db";
    private static final String PATH_DATABASE = Environment.getExternalStorageDirectory()
            + File.separator + "RDF" + File.separator;
    private static final String DESTINO_ZIP = Environment.getExternalStorageDirectory()
            + File.separator + "RDF_ZIP";
    private static final String ZIP_NAME = "RDF.zip";

    private DBColaborador dbColaborador;

    public BaseDadosAppTask(Context pContext) {
        dbColaborador = new DBColaborador(pContext);
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            Colaborador colaborador = dbColaborador.get();
            if (colaborador.getEnviarBase() == 1)
                return colaborador.getId();
            else
                return 0;
        } catch (Exception er) {
            er.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (result > 0) {
            try {
                CompressFile compressFile = new CompressFile(PATH_DATABASE, DESTINO_ZIP, ZIP_NAME);
                compressFile.compress();
                File file = compressFile.getCompressedFile();

                RequestParams params = new RequestParams();
                try {
                    params.put("file", file);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

                String url = URLs.BASE_DADOS + "?idVendedor=" + result;

                AsyncHttpClient client = new AsyncHttpClient();
                client = Utilidades.setTokenOnAsyncHttpClientHeader(client);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });

                dbColaborador.setEnviarBase(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}