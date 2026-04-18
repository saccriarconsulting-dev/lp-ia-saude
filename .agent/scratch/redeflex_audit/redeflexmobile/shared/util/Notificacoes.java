package com.axys.redeflexmobile.shared.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Mensagem;
import com.axys.redeflexmobile.shared.models.OrdemServico;
import com.axys.redeflexmobile.shared.models.Remessa;
import com.axys.redeflexmobile.ui.clientevalidacao.ClienteActivity;
import com.axys.redeflexmobile.ui.redeflex.ChamadoActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.MensagemActivity;
import com.axys.redeflexmobile.ui.redeflex.OSActivity;
import com.axys.redeflexmobile.ui.redeflex.RemessaDetalheActivity;
import com.axys.redeflexmobile.ui.redeflex.SyncActivity;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.axys.redeflexmobile.ui.register.list.RegisterListActivity;

import java.util.Random;

import static android.app.PendingIntent.getActivity;
import static android.graphics.BitmapFactory.decodeResource;
import static com.axys.redeflexmobile.ui.redeflex.SyncActivity.ARGS_TIPO_OPERACAO;

/**
 * Created by joao.viana on 07/12/2016.
 */

public class Notificacoes {

    private static final String DEFAULT_FLAG = "default";
    private static final CharSequence NAME = "Notification";
    private static final String CHANNEL_ID = "1";
    private static final int NOTIFICATION_MAXIMUM_PRIORITY = 2;

    public static void RemessaAnexo(Context contexto, String codigo, String title, String Mensagem) {
        Intent detalhes = new Intent(contexto, RemessaDetalheActivity.class);
        detalhes.putExtra(Config.CodigoRemessa, codigo);
        detalhes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent =
                getActivity(contexto, Integer.parseInt(codigo), detalhes, PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(contexto, title, Mensagem, soundUri, resultPendingIntent, null, null);
    }

    public static void Cliente(Context contexto, String title, String Mensagem, Cliente cliente) {
        Intent detalhes = new Intent(contexto, ClienteActivity.class);
        try {
            detalhes.putExtra(Config.Latitude, cliente.getLatitude());
            detalhes.putExtra(Config.Longitude, cliente.getLongitude());
        } catch (Exception ex) {
            detalhes.putExtra(Config.Latitude, "0.0");
            detalhes.putExtra(Config.Longitude, "0.0");
            ex.printStackTrace();
        }

        detalhes.putExtra(Config.CodigoCliente, cliente.getCodigoIntraFlex());
        detalhes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = getActivity(contexto, Integer.parseInt(cliente.getId()), detalhes, PendingIntent.FLAG_MUTABLE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(contexto, title, Mensagem, soundUri, resultPendingIntent, null, null);
    }

    public static void Util(Context context, String mensagem) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(context, context.getResources().getString(R.string.app_name), mensagem, soundUri, null, null, null);
    }

    public static void OrdemServico(String title, OrdemServico item, Context contexto) {
        Intent detalhes = new Intent(contexto, OSActivity.class);
        detalhes.putExtra(Config.CodigoItemOS, item.getId());
        detalhes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent =
                getActivity(contexto, item.getId(), detalhes, PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(contexto, title, "OS N°: " + String.valueOf(item.getId())
                + " - " + item.getDescricaoTipo(), soundUri, resultPendingIntent, null, null);
    }

    public static void Chamado(Context contexto, int codigo, String title, String mensagem) {
        Intent detalhes = new Intent(contexto, ChamadoActivity.class);
        detalhes.putExtra(Config.CodigoChamado, String.valueOf(codigo));
        detalhes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent =
                getActivity(contexto, codigo, detalhes, PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(contexto, title, mensagem, soundUri, resultPendingIntent, null, null);
    }

    public static void Remessa(Context pContext, Remessa item, String title) {
        Intent detalhes = new Intent(pContext, RemessaDetalheActivity.class);
        detalhes.putExtra(Config.CodigoRemessa, item.getId());
        detalhes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (item.getSituacao() != 3 && Util_IO.isNullOrEmpty(item.getDataconfirmacao() == null ? "" : item.getDataconfirmacao().toString())) {
            PendingIntent resultPendingIntent =
                    getActivity(pContext, Integer.parseInt(item.getId()), detalhes, PendingIntent.FLAG_MUTABLE);

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            showNotification(pContext, title, "Remessa" + item.getNumero(), soundUri,
                    resultPendingIntent, null, null);
        }
    }

    public static void Mensagem(String title, Mensagem item, Context contexto) {
        Intent i = new Intent(contexto, MensagemActivity.class);
        i.putExtra(Config.CodigoMensagem, item.getId());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent =
                getActivity(contexto, item.getId(), i, PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(contexto, title, item.getTexto(), soundUri, resultPendingIntent,
                null, null);
    }

    public static void Cadastro(String pTitle, int pCodigo, Context pContext, String pMensagem,
                                boolean isError) {
        Intent intent = isError ?
                RegisterCustomerActivity.getIntent(pContext, String.valueOf(pCodigo))
                : RegisterListActivity.getIntent(pContext);

        if (pMensagem == null)
            pMensagem = "Alteração de Cadastro";

        PendingIntent resultPendingIntent = getActivity(pContext, pCodigo, intent, PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(pContext, pTitle, pMensagem, soundUri, resultPendingIntent, null, null);
    }

    public static void cancelarNotificacao(Context pContext, int pCodigo) {
        NotificationManager notificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(pCodigo);
    }

    public static void sincronizacaoPendente(Context pContext, String mensagem) {
        Intent intent = new Intent(pContext, SyncActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_TIPO_OPERACAO, 1);
        intent.putExtras(bundle);

        PendingIntent resultPendingIntent = getActivity(pContext,
                1,
                intent,
                PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        showNotification(pContext, pContext.getResources().getString(R.string.app_name), mensagem, soundUri, resultPendingIntent, null, null);
    }

    public static void chatBotNotification(Context context, String mensagem) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        showNotification(context, context.getResources().getString(R.string.chat_notification_title),
                mensagem, soundUri, null, R.drawable.ic_chat, context.getResources().getString(R.string.chat_notification_title));
    }

    private static void showNotification(Context context, String title, String mensagem,
                                         Uri soundUri, PendingIntent pendingIntent, Integer iconResource, String summary) {
        int numeroAlto = 999999;
        int numeroBaixo = 000001;

        int randomId = new Random().nextInt(numeroAlto - numeroBaixo);

        int smallIcon = R.mipmap.ic_icone_new;
        if (iconResource != null) {
            smallIcon = iconResource;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(context, DEFAULT_FLAG)
                .setSmallIcon(smallIcon)
                .setLargeIcon(decodeResource(context.getResources(), R.mipmap.ic_icone_new))
                .setContentTitle(title)
                .setContentText(mensagem)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setGroup(String.valueOf(randomId))
                .setPriority(NOTIFICATION_MAXIMUM_PRIORITY);

        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        String summaryText = context.getString(R.string.app_name);
        if (summary != null) {
            summaryText = summary;
        }

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(title);
        bigText.bigText(mensagem);
        bigText.setSummaryText(summaryText);
        notificationBuilder.setStyle(bigText);

        NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder.setChannelId(CHANNEL_ID);
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, NAME, NotificationManager.IMPORTANCE_HIGH));
            }

            int hash = (title + mensagem).hashCode();
            notificationManager.notify(hash, notificationBuilder.build());
        }
    }
}