package com.axys.redeflexmobile.shared.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBHorarioNotificacao;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.HorarioNotificacao;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.cartaoponto.CartaoPontoActivity;
import com.axys.redeflexmobile.ui.login.LoginActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.SplashScreen;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

import static android.graphics.BitmapFactory.decodeResource;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.SEMI_COLON;

public class CartaoPontoService extends Service {

    public static final int ID_NOTIFICACAO = 1321;
    private static final String DEFAULT_FLAG = "default";
    private static final CharSequence NAME = "Notification";
    private static final String CHANNEL_ID = "1";
    private static final int SATURDAY = 7;
    private static final int NOTIFICATION_MAXIMUM_PRIORITY = 2;
    private static final int DATA_VALIDACAO_INICIO_HORA = 17;
    private static final int DATA_VALIDACAO_INICIO_MINUTO = 50;
    private static final int DATA_VALIDACAO_FIM_HORA = 18;
    private static final int DATA_VALIDACAO_FIM_MINUTO = 0;
    private static final int PUSH_TEN_MINUTE_RANGE = 10;
    public static Runnable runnable = null;
    public Handler handler = null;
    public Context context = this;
    private int TEMPO_TIMER = 1000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        DBColaborador dbColaborador = new DBColaborador(this);
        Colaborador colaborador = dbColaborador.get();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (deveEnviarNotificacao()) {
                    TEMPO_TIMER = 60000;
                    enviarNotificacao();
                }

                handler.postDelayed(this, TEMPO_TIMER);
            }
        };

        if (colaborador != null && !colaborador.isCartaoPonto()) {
            handler.removeCallbacks(runnable);
            return;
        }

        runnable.run();
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }

        super.onDestroy();
    }

    private boolean deveEnviarNotificacao() {
        DBHorarioNotificacao dbHorarioNotificacao = new DBHorarioNotificacao(this);
        DBColaborador dbColaborador = new DBColaborador(this);
        List<HorarioNotificacao> horariosNotificacao = dbHorarioNotificacao.getHorariosNotificacao();
        LocalDateTime horaAtual = LocalDateTime.now();
        LocalDate data = LocalDate.now();
        DayOfWeek dayOfWeek = data.getDayOfWeek();
        int diaDaSemanaAtual = (dayOfWeek.getValue() == SATURDAY
                ? EMPTY_INT : dayOfWeek.getValue()) + SINGLE_INT;
        Colaborador colaborador = dbColaborador.get();

        if (colaborador != null && !colaborador.isCartaoPonto()) {
            return false;
        }

        DBCartaoPonto dbCartaoPonto = new DBCartaoPonto(this);

        for (HorarioNotificacao horarioNotificacao : horariosNotificacao) {

            int diaDaSemana = horarioNotificacao.getDiaSemana();

            if (diaDaSemana == diaDaSemanaAtual
                    && StringUtils.isEmpty(horarioNotificacao.getHoraUm())
                    && StringUtils.isEmpty(horarioNotificacao.getHoraDois())
                    && StringUtils.isEmpty(horarioNotificacao.getHoraTres())
                    && StringUtils.isEmpty(horarioNotificacao.getHoraQuatro())) {

                LocalDateTime dataValidacaoInicio = LocalDateTime.of(horaAtual.getYear(),
                        horaAtual.getMonth(),
                        horaAtual.getDayOfMonth(),
                        DATA_VALIDACAO_INICIO_HORA, DATA_VALIDACAO_INICIO_MINUTO);

                LocalDateTime dataValidacaoFim = LocalDateTime.of(horaAtual.getYear(),
                        horaAtual.getMonth(),
                        horaAtual.getDayOfMonth(),
                        DATA_VALIDACAO_FIM_HORA, DATA_VALIDACAO_FIM_MINUTO);

                return horaAtual.compareTo(dataValidacaoInicio) >= EMPTY_INT
                        && horaAtual.compareTo(dataValidacaoFim) <= EMPTY_INT;
            }


            if (horarioNotificacao.getPush() != null
                    && !horarioNotificacao.getPush().isEmpty()
                    && diaDaSemana == diaDaSemanaAtual) {

                String[] pushList = horarioNotificacao.getPush().split(SEMI_COLON);
                for (String push : pushList) {

                    try {

                        LocalDateTime hour = DateUtils.getLocalDateTimeFromHour(horarioNotificacao.getHourFromType(push));
                        if (hour != null) {

                            LocalDateTime hourUpdated = hour.minus(PUSH_TEN_MINUTE_RANGE, ChronoUnit.MINUTES);
                            if (dbCartaoPonto.obterRegistroHorarioPush(hourUpdated, hour).isEmpty()
                                    && horaAtual.isAfter(hourUpdated)
                                    && horaAtual.isBefore(hour)) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
            }
        }
        return false;
    }

    private boolean appAberto() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        List<String> activities = new ArrayList<>();
        activities.add(SplashScreen.class.getName());
        activities.add(LoginActivity.class.getName());

        return !services.isEmpty() && services.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName())
                && !activities.contains(services.get(0).topActivity.getClassName());
    }

    private void enviarNotificacao() {
        int numeroAlto = 999999;
        int numeroBaixo = 1;

        int randomId = new Random().nextInt(numeroAlto - numeroBaixo);

        TaskStackBuilder builder = getBuilder();

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
            pendingIntent = builder.getPendingIntent(randomId,PendingIntent.FLAG_MUTABLE);
        else
            pendingIntent = builder.getPendingIntent(randomId,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(context, DEFAULT_FLAG)
                .setSmallIcon(R.mipmap.ic_icone_new)
                .setLargeIcon(decodeResource(context.getResources(), R.mipmap.ic_icone_new))
                .setContentTitle("Atenção")
                .setContentText("Você deve registrar seu ponto.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setGroup(String.valueOf(randomId))
                .setPriority(NOTIFICATION_MAXIMUM_PRIORITY)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle("Atenção");
        bigText.bigText("Você deve registrar seu ponto.");
        bigText.setSummaryText(context.getString(R.string.app_name));
        notificationBuilder.setStyle(bigText);

        NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder.setChannelId(CHANNEL_ID);
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,
                        NAME, NotificationManager.IMPORTANCE_HIGH));
            }

            notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(ID_NOTIFICACAO, notificationBuilder.build());
        }
    }

    private @NotNull TaskStackBuilder getBuilder() {
        TaskStackBuilder builder = TaskStackBuilder.create(context);

        if (appAberto()) {
            Utilidades.salvarStringSharedPreference(context, Config.AbrirCartaoPonto, "N");
            Intent targetIntent = new Intent(context, CartaoPontoActivity.class);
            builder.addNextIntentWithParentStack(targetIntent);

            return builder;
        }

        Utilidades.salvarStringSharedPreference(context, Config.AbrirCartaoPonto, "S");
        Intent targetIntent = new Intent(context, LoginActivity.class);
        builder.addNextIntentWithParentStack(targetIntent);

        return builder;
    }
}
