package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.OS;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.OsBus;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OsSyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int tipoCarga = 1;

    public OsSyncTask(Context pContext, int pTipoCarga) {
        mContext = pContext;
        tipoCarga = pTipoCarga;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            OsBus.getOS(mContext, tipoCarga);
            setSyncVisualizacao();
            setSyncAgendamento();
            setSyncAtendimento();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setSyncVisualizacao() {
        try {
            DBOs dbOs = new DBOs(this.mContext);
            ArrayList<OS> list = dbOs.getVisualizadasNaoSync();
            String urlfinal;
            for (OS item : list) {
                urlfinal = URLs.OS + "/" + String.valueOf(item.getId())
                        + "?data=" + Util_IO.dateTimeToString(item.getDataVisualizacao(), "yyyy-MM-dd'T'HH:mm")
                        + "&campo=0";
                int result = Utilidades.getObject(urlfinal, int.class);
                if (result == 1)
                    dbOs.setSyncVisualizacao(item.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setSyncAgendamento() {
        try {
            DBOs dbOs = new DBOs(this.mContext);
            ArrayList<OS> list = dbOs.getAgendadasNaoSync();
            String urlfinal;
            for (OS item : list) {
                urlfinal = URLs.OS + "/" + String.valueOf(item.getId())
                        + "?data=" + Util_IO.dateTimeToString(item.getDataAgendamento(), "yyyy-MM-dd'T'HH:mm")
                        + "&campo=1";

                int result = Utilidades.getObject(urlfinal, int.class);
                if (result == 1)
                    dbOs.setSyncAgendamento(item.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setSyncAtendimento() {
        try {
            DBOs dbOs = new DBOs(mContext);
            ArrayList<OS> list = dbOs.getAtendidasNaoSync();
            Calendar calendar = Calendar.getInstance();
            String data;
            for (OS item : list) {
                try {
                    URL url = new URL(URLs.OS);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.connect();

                    JSONObject main = new JSONObject();
                    try {
                        main.put("id", String.valueOf(item.getId()));
                        main.put("dataAtendimento", Util_IO.dateTimeToString(item.getDataAtendimento(), "yyyy-MM-dd'T'HH:mm"));
                        main.put("observacao", Util_IO.trataString(item.getObsVendedor()));
                        main.put("versaoApp", BuildConfig.VERSION_NAME);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    data = main.toString();

                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(data);
                    wr.flush();
                    wr.close();

                    BufferedReader in;
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    if (response.toString().equals("1")) {
                        calendar.setTime(item.getDataAtendimento());
                        calendar.add(Calendar.DATE, 30);
                        Date dt = calendar.getTime();
                        if (dt.before(new Date()))
                            dbOs.delete(item.getId());
                        else
                            dbOs.setSyncAtendimento(item.getId());
                    }
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException ex) {
                    ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}