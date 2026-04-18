package com.axys.redeflexmobile.shared.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axys.redeflexmobile.shared.services.bus.OsBus;
import com.axys.redeflexmobile.shared.util.Utilidades;

/**
 * Created by Desenvolvimento on 12/05/2016.
 */
public class AlarmNotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utilidades.verificarHorarioComercial(context, false)) {
            OsBus.agendamentoAutomatico(context);
            OsBus.notificacoesPendentesAgendar(context);
            OsBus.notificacoesPendentesATender(context);
        }
    }
}