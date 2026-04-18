package com.axys.redeflexmobile.shared.services.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.bd.DBRemessa;
import com.axys.redeflexmobile.shared.bd.DBTelemetria;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.models.ClienteCadastroAnexo;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCodigoBarra;
import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.models.Remessa;
import com.axys.redeflexmobile.shared.models.RemessaItem;
import com.axys.redeflexmobile.shared.models.Telemetria;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.services.bus.TelemetriaBus;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 21/10/2016.
 */

public class LocalizacaoTask extends AsyncTask<String, Void, Integer> {
    private Context mContext;
    private Telemetria telemetria;

    public LocalizacaoTask(Context pContext) {
        mContext = pContext;
    }

    protected Integer doInBackground(String... params) {
        try {
            capturarLocal();
            TelemetriaBus.enviarLocal(mContext);
            return 0;
        } catch (Exception er) {
            er.printStackTrace();
            return -1;
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void capturarLocal() {
        GPSTracker gps = new GPSTracker(mContext);
        telemetria = new Telemetria();

        Colaborador colaborador = new DBColaborador(mContext).get();
        telemetria.setIdVendedor(colaborador.getId());

        telemetria.setPrecisao(gps.getPrecisao());
        telemetria.setLongitude(gps.getLongitude());
        telemetria.setLatitude(gps.getLatitude());
        telemetria.setVersaoApp(BuildConfig.VERSION_NAME);

        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer))
                telemetria.setModeloAparelho(Util_IO.toTitleCase(model));
            telemetria.setModeloAparelho(Util_IO.toTitleCase(manufacturer) + " " + model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            batteryLevel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            telemetria.setImei(Utilidades.retornaIMEI(mContext));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo inf = cm.getActiveNetworkInfo();

            telemetria.setTipoInternet((inf.getTypeName() == null ? "" : inf.getTypeName()) + "-" + (inf.getExtraInfo() == null ? "" : inf.getExtraInfo()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            verificaPendencia();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        telemetria.setVersaoOs(String.valueOf(Build.VERSION.SDK_INT));
        try {
            new DBTelemetria(mContext).addNova(telemetria);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                try {
                    context.unregisterReceiver(this);
                    int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
                    int level = -1;
                    if (rawlevel >= 0 && scale > 0)
                        level = (rawlevel * 100) / scale;

                    if (level == 0)
                        level = intent.getIntExtra("level", 0);

                    telemetria.setBateria(level);
                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    private void verificaPendencia() {
        try {
            DBVenda dbvenda = new DBVenda(mContext);
            ArrayList<Venda> venda = dbvenda.getVendasPendentes();
            ArrayList<ItemVendaCodigoBarra> codbarra = dbvenda.getItemVendaCodigoBarraPendentes();
            ArrayList<ItemVenda> itens = dbvenda.getItemVendaPendentes();

            ArrayList<Visita> visita = new DBVisita(mContext).getPendentes();
            ArrayList<LocalizacaoCliente> localCli = new DBLocalizacaoCliente(mContext).getLocalizacaoPendente();

            DBRemessa dbRemessa = new DBRemessa(mContext);
            ArrayList<Remessa> remessa = dbRemessa.getRemessa(true);
            ArrayList<RemessaItem> remessaItems = dbRemessa.getRemessaItem(null);

            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(mContext);
            ArrayList<ClienteCadastroAnexo> anexos = dbClienteCadastro.getAnexosPendentes();
            List<CustomerRegister> cadastros = dbClienteCadastro.getClientesPendentes();

            telemetria.setQtdCodBarraPend(codbarra.size());
            telemetria.setQtdVendaItensPend(itens.size());
            telemetria.setQtdVendaPend(venda.size());
            telemetria.setQtdVisitaPend(visita.size());
            telemetria.setQtdRemessaPend(remessa.size());
            telemetria.setQtdItemRemessaPend(remessaItems.size());
            telemetria.setQtdLocCliPend(localCli.size());
            telemetria.setQtdCadCliPend(cadastros.size());
            telemetria.setQtdDocCliPend(anexos.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}