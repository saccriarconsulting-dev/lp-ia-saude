package com.axys.redeflexmobile.shared.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.PendenciasAdapter;
import com.axys.redeflexmobile.shared.bd.BDCadastroVendedorPOS;
import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.DBAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.bd.DBAudOpe;
import com.axys.redeflexmobile.shared.bd.DBAuditagemCliente;
import com.axys.redeflexmobile.shared.bd.DBBancos;
import com.axys.redeflexmobile.shared.bd.DBCallReason;
import com.axys.redeflexmobile.shared.bd.DBCanalSuporte;
import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBCatalogoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroEndereco;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroPOS;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroTaxa;
import com.axys.redeflexmobile.shared.bd.DBCnae;
import com.axys.redeflexmobile.shared.bd.DBCobranca;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBComprovanteSkyTa;
import com.axys.redeflexmobile.shared.bd.DBCredencial;
import com.axys.redeflexmobile.shared.bd.DBDataHoraServidor;
import com.axys.redeflexmobile.shared.bd.DBDepartamentos;
import com.axys.redeflexmobile.shared.bd.DBDevolucao;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBFilial;
import com.axys.redeflexmobile.shared.bd.DBFormaPagamento;
import com.axys.redeflexmobile.shared.bd.DBHorarioNotificacao;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.bd.DBIsencao;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.bd.DBMensagem;
import com.axys.redeflexmobile.shared.bd.DBMerchandising;
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.bd.DBMotiveMigrationSub;
import com.axys.redeflexmobile.shared.bd.DBMotivo;
import com.axys.redeflexmobile.shared.bd.DBMotivoAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBPOS;
import com.axys.redeflexmobile.shared.bd.DBPendencia;
import com.axys.redeflexmobile.shared.bd.DBPerguntasQualidade;
import com.axys.redeflexmobile.shared.bd.DBPermissao;
import com.axys.redeflexmobile.shared.bd.DBPistolagemTemp;
import com.axys.redeflexmobile.shared.bd.DBPrazoNegociacao;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBProjetoTrade;
import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSub;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.bd.DBRelatorioMeta;
import com.axys.redeflexmobile.shared.bd.DBRemessa;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirenciaAgendada;
import com.axys.redeflexmobile.shared.bd.DBSegmento;
import com.axys.redeflexmobile.shared.bd.DBSenhaCliente;
import com.axys.redeflexmobile.shared.bd.DBSenhaMasters;
import com.axys.redeflexmobile.shared.bd.DBSenhaVenda;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.bd.DBTaxaAdquirenciaPF;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.DBTaxasAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBTelemetria;
import com.axys.redeflexmobile.shared.bd.DBTipoMaquina;
import com.axys.redeflexmobile.shared.bd.DBTokenCliente;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.bd.DBVisitaAdquirencia;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientHomeBanking;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientTaxMdr;
import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.bd.registerrate.DBProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.bd.registerrate.DBRegistrationSimulationFee;
import com.axys.redeflexmobile.shared.models.CartaoPonto;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.DataHoraServidor;
import com.axys.redeflexmobile.shared.models.HorarioNotificacao;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.models.Pendencias;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.Rota;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.models.login.Login;
import com.axys.redeflexmobile.shared.services.AlarmLocalizacao;
import com.axys.redeflexmobile.shared.services.AlarmNotifyReceiver;
import com.axys.redeflexmobile.shared.services.AlarmSyncReceiver;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.ColaboradorBus;
import com.axys.redeflexmobile.shared.services.network.util.JsonExcludeStrategy;
import com.axys.redeflexmobile.shared.services.tasks.VendaSyncTask;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.axys.redeflexmobile.ui.dialog.ConfirmarSenhaDialog;
import com.axys.redeflexmobile.ui.dialog.MotivoDialog;
import com.axys.redeflexmobile.ui.redeflex.AtendimentoActivity;
import com.axys.redeflexmobile.ui.redeflex.AuditagemClienteActivity;
import com.axys.redeflexmobile.ui.redeflex.ChamadoActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.NovaRotaActivity;
import com.axys.redeflexmobile.ui.redeflex.RotaActivity;
import com.axys.redeflexmobile.ui.redeflex.RotaDiariaActivity;
import com.axys.redeflexmobile.ui.redeflex.SuporteActivity;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.axys.redeflexmobile.shared.util.ImageUtils.copyFile;

/**
 * Created by joao.viana on 21/09/2017.
 */
@SuppressLint("DefaultLocale")
public class Utilidades {

    public static final String GET_REQUEST_TAG = "GET REQUEST";
    public static final String PREFS_NAME = "sharedPreference";
    private static final String INTENT_TYPE = "image/*";
    public static String secretToken = "933421BB0EFD59C15F003EBAC073E0D2938635F91F42C5D95A956E54D15A00E5";
    public static String IMEI = "404";
    public static String idVendedor = "404";
    private static Gson gsonInstance;

    public static String retornaTempoAtendimento(Date pDataInicial) {
        try {
            Date dataAtual = new Date();

            long lStartTime = pDataInicial.getTime();
            long lEndTime = dataAtual.getTime();

            long diffInSeconds = (lEndTime - lStartTime) / 1000;

            long[] diff = new long[]{0, 0, 0, 0};
            /* segundos */
            diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
            /* minutos */
            diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
            /* horas */
            diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
            /* dias */
            diff[0] = diffInSeconds / 24;

            return String.format(
                    "%s %02d:%02d:%02d",
                    diff[0] > 0 ? diff[0] > 1 ? String.format("%d dias,", diff[0]) : String.format("%d dia,", diff[0]) : "",
                    diff[1],
                    diff[2],
                    diff[3]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String retornaHoraAtual() {
        try {
            Date date = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);

            return (String.format(
                    "%02d:%02d:%02d",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND)));

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String retornaDataAtualFormatada() {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        return String.format("%s de %s de %s",
                calendar.get(Calendar.DAY_OF_MONTH),
                obterMes(calendar.get(Calendar.MONTH)).toLowerCase(),
                calendar.get(Calendar.YEAR));
    }

    private static String obterMes(int month) {
        month += 1;

        if (month == 1) {
            return "Janeiro";
        } else if (month == 2) {
            return "Fevereiro";
        } else if (month == 3) {
            return "Março";
        } else if (month == 4) {
            return "Abril";
        } else if (month == 5) {
            return "Maio";
        } else if (month == 6) {
            return "Junho";
        } else if (month == 7) {
            return "Julho";
        } else if (month == 8) {
            return "Agosto";
        } else if (month == 9) {
            return "Setembro";
        } else if (month == 10) {
            return "Outubro";
        } else if (month == 11) {
            return "Novembro";
        } else if (month == 12) {
            return "Dezembro";
        }

        return null;
    }

    public static String retornaMesExtenso(int month) {
        if (month == 1) {
            return "Janeiro";
        } else if (month == 2) {
            return "Fevereiro";
        } else if (month == 3) {
            return "Março";
        } else if (month == 4) {
            return "Abril";
        } else if (month == 5) {
            return "Maio";
        } else if (month == 6) {
            return "Junho";
        } else if (month == 7) {
            return "Julho";
        } else if (month == 8) {
            return "Agosto";
        } else if (month == 9) {
            return "Setembro";
        } else if (month == 10) {
            return "Outubro";
        } else if (month == 11) {
            return "Novembro";
        } else if (month == 12) {
            return "Dezembro";
        }

        return null;
    }

    public static void ativaInativaAlarmes(Context pContext, boolean pAtivar) {
        try {
            AlarmManager am = (AlarmManager) (pContext.getSystemService(Context.ALARM_SERVICE));
            //service Redeflex
            am.cancel(PendingIntent.getBroadcast(pContext, 0, new Intent(pContext, AlarmSyncReceiver.class), PendingIntent.FLAG_MUTABLE));
            //service os
            am.cancel(PendingIntent.getBroadcast(pContext, 0, new Intent(pContext, AlarmNotifyReceiver.class), PendingIntent.FLAG_MUTABLE));
            //service localização
            am.cancel(PendingIntent.getBroadcast(pContext, 0, new Intent(pContext, AlarmLocalizacao.class), PendingIntent.FLAG_MUTABLE));

            if (pAtivar) {
                //service Redeflex
                am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HALF_HOUR / 2,
                        PendingIntent.getBroadcast(pContext, 0, new Intent(pContext, AlarmSyncReceiver.class), PendingIntent.FLAG_MUTABLE));
                //service os
                am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR * 3
                        , PendingIntent.getBroadcast(pContext, 0, new Intent(pContext, AlarmNotifyReceiver.class), PendingIntent.FLAG_MUTABLE));
                //service localização
                am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HALF_HOUR / 3,
                        PendingIntent.getBroadcast(pContext, 0, new Intent(pContext, AlarmLocalizacao.class), PendingIntent.FLAG_MUTABLE));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String retornaIMEI(Context pContext) throws Exception {
        // TODO: Forçar IMEI para o aplicativo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(pContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
                return tm.getDeviceId();
            } else {
                throw new Exception("Não encontrado o IMEI");
            }
        }

        SharedPreferenceEncrypted sharedPreferenceEncrypted = new SharedPreferenceEncryptedImpl(
                new CipherWrapperImpl(CipherTransformation.TRANSFORMATION_PADDING),
                new KeyStoreWrapperImpl(pContext),
                new DBCredencial(pContext)
        );
        Login login = sharedPreferenceEncrypted.getSimpleLogin();
        if (StringUtils.isEmpty(login.getUuid())) {
            throw new Exception("Não encontrado o IMEI");
        }

        return login.getUuid();
    }

    public static void deletarTudo(Context pContext) {
        try {
            new BDCadastroVendedorPOS(pContext).deleteAll();
            new DBAdquirencia(pContext).deleteAll();
            //new DBAtualizarCliente(pContext).deleteAll();
            new DBAuditagemCliente(pContext).deleteAll();
            new DBAudOpe(pContext).deleteAll();
            new DBBancos(pContext).deleteAll();
            new DBCallReason(pContext).deleteAll();
            new DBCanalSuporte(pContext).deleteAll();
            new DBCartaoPonto(pContext).deleteAll();
            new DBCatalogoAdquirencia(pContext).deletaTudo();
            new DBChamados(pContext).deleteAll();
            new DBCliente(pContext).deleteAll();
            new DBClienteCadastro(pContext).deleteAll();
            new DBClienteCadastroEndereco(pContext).deletaTudo();
            new DBClienteCadastroPOS(pContext).deleteAll();
            new DBClienteCadastroTaxa(pContext).deletaTudo();
            new DBCnae(pContext).deleteAll();
            new DBCobranca(pContext).deleteAll();
            new DBColaborador(pContext).deleteAll();
            new DBComprovanteSkyTa(pContext).deleteAll();
            new DBDataHoraServidor(pContext).delete();
            new DBDepartamentos(pContext).deleteAll();
            new DBDevolucao(pContext).deleteAll();
            new DBEstoque(pContext).deleteAll();
            new DBFilial(pContext).deleteAll();
            new DBFormaPagamento(pContext).deleteAll();
            new DBHorarioNotificacao(pContext).deleteAll();
            new DBIccid(pContext).deleteAll();
            new DBIsencao(pContext).deletaTudo();
            new DBLimite(pContext).deleteAll();
            new DBLocalizacaoCliente(pContext).deleteAll();
            new DBMensagem(pContext).deleteAll();
            new DBMerchandising(pContext).deleteAll();
            new DBModeloPOS(pContext).deletaTudo();
            new DBMotiveMigrationSub(pContext).deleteAll();
            new DBMotivo(pContext).deleteAll();
            new DBMotivoAdquirencia(pContext).deletaTudo();
            new DBOperadora(pContext).deleteAll();
            new DBOs(pContext).deleteAll();
            new DBPendencia(pContext).deleteAll();
            new DBPerguntasQualidade(pContext).deletaTudo();
            new DBPermissao(pContext).deleteAll();
            new DBPistolagemTemp(pContext).deleteAll();
            new DBPOS(pContext).deleteAll();
            new DBPrazoNegociacao(pContext).deletaTudo();
            new DBPreco(pContext).deleteAll();
            new DBProjetoTrade(pContext).deleteAll();
            new DBProspect(pContext).deletaTudo();
            new DBRegisterMigrationSub(pContext).deleteAll();
            new DBRegisterMigrationSubTax(pContext).deleteAll();
            new DBRelatorioMeta(pContext).deleteAll();
            new DBRemessa(pContext).deleteAll();
            new DBRota(pContext).deleteAll();
            new DBRotaAdquirencia(pContext).deletaTudo();
            new DBRotaAdquirenciaAgendada(pContext).deletaTudo();
            new DBSegmento(pContext).deleteAll();
            new DBSenhaCliente(pContext).deleteAll();
            new DBSenhaMasters(pContext).deleteAll();
            new DBSenhaVenda(pContext).deleteAll();
            new DBSolicitacaoMercadoria(pContext).deleteAll();
            new DBSolicitacaoTroca(pContext).deleteAll();
            new DBSugestaoVenda(pContext).deleteAll();
            new DBSuporte(pContext).deleteAll();
            new DBTaxaAdquirenciaPF(pContext).deleteAll();
            new DBTaxaMdr(pContext).deletaTudo();
            new DBTaxasAdquirencia(pContext).deleteAll();
            new DBTelemetria(pContext).deleteAll();
            new DBTipoMaquina(pContext).deleteAll();
            new DBTokenCliente(pContext).deleteAll();
            new DBVenda(pContext).deleteAll();
            new DBVisita(pContext).deleteAll();
            new DBVisitaAdquirencia(pContext).deletaTudo();

            // Prospect
            new DBProspectingClientAcquisition(pContext).deleteAll();
            new DBRegistrationSimulationFee(pContext).deleteAll();

            // Cliente Info
            new DBClientTaxMdr(pContext).deleteAll();
            new DBClientHomeBanking(pContext).deleteAll();
            new DBFlagsBank(pContext).deleteAll();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getDataServidorESalvaBanco(Context pContext) {
        new Thread(() -> {
            Date dataServidor;
            DBDataHoraServidor dbDataServidor = new DBDataHoraServidor(pContext);

            try {
                dataServidor = ColaboradorBus.getDateServer();
                if (dataServidor != null) {
                    dbDataServidor.add(new DataHoraServidor(dataServidor));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static <T> T getObject(String pUrl, Class<T> pObjectClass) throws IOException {
        try {
            String resultString = getRegistros(pUrl);
            if (resultString != null)
                return new GsonBuilder().setDateFormat(Config.FormatDateTimeStringBancoJson).create().fromJson(resultString, pObjectClass);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return null;
    }

    public static String getRegistros(String pUrl) throws IOException {
        return getRegistros(pUrl, true);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static String getRegistros(String pUrl, boolean pNewLine) throws IOException {
        URL url1 = new URL(pUrl);
        String resultString;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection = (HttpURLConnection) setTokenOnUrlHeader(urlConnection);
            try {
                int statusCode = urlConnection.getResponseCode(); // <- Aqui pega o status

                Timber.i("### pUrl: " + pUrl + " statusCode: " + statusCode);
                InputStream httpinputStream;
                if (statusCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                    httpinputStream = urlConnection.getErrorStream(); // 4xx ou 5xx
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpinputStream));
                    StringBuilder errorBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorBuilder.append(line).append("\n");
                    }
                    reader.close();
                    Timber.i("### errorBuilder: " + errorBuilder.toString() );
                    throw new IOException(errorBuilder.toString());
                } else {
                    httpinputStream = urlConnection.getInputStream(); // 2xx
                }
                InputStream inputStream = new BufferedInputStream(httpinputStream);
                resultString = convertStreamToString(inputStream, pNewLine);
                inputStream.close();
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                resultString = null;
            } catch (IOException ex) {
                ex.printStackTrace();
                resultString = null;
            } finally {
                urlConnection.disconnect();
            }
        } catch (SocketTimeoutException ex) {
            resultString = null;
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            resultString = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            resultString = null;
        }
        return resultString;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static String getRegistrosSemTimeout(String pUrl, boolean pNewLine) throws IOException {
        URL url1 = new URL(pUrl);
        String resultString;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) setTokenOnUrlHeader((HttpURLConnection) url1.openConnection());
            urlConnection.setConnectTimeout(9999999);
            urlConnection.setReadTimeout(9999999);
            try {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                resultString = convertStreamToString(inputStream, pNewLine);
                inputStream.close();
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                resultString = null;
            } catch (IOException ex) {
                ex.printStackTrace();
                resultString = null;
            } finally {
                urlConnection.disconnect();
            }
        } catch (SocketTimeoutException ex) {
            resultString = null;
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            resultString = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            resultString = null;
        }
        return resultString;
    }

    public static RequestModel getRequest(String url) throws IOException {
        RequestModel requestModel = new RequestModel();
        URL url1 = new URL(url);
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
            setTokenOnUrlHeader(urlConnection);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            try {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                requestModel.result = convertStreamToString(inputStream, true);
                inputStream.close();
            } catch (UnsupportedEncodingException ex) {
                Log.e(GET_REQUEST_TAG, url, ex);
                requestModel.result = null;
                requestModel.error = ex;
            } catch (IOException ex) {
                Log.e(GET_REQUEST_TAG, url, ex);
                requestModel.result = null;
                requestModel.error = ex;
            } finally {
                urlConnection.disconnect();
            }
        } catch (SocketTimeoutException ex) {
            Log.e(GET_REQUEST_TAG, url, ex);
            requestModel.result = null;
            requestModel.error = ex;
        } catch (IOException ex) {
            Log.e(GET_REQUEST_TAG, url, ex);
            requestModel.result = null;
            requestModel.error = ex;
        } catch (Exception ex) {
            Log.e(GET_REQUEST_TAG, url, ex);
            requestModel.result = null;
            requestModel.error = ex;
        }
        return requestModel;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static RequestModel postRequest(URL pUrl, String pJson) {
        Timber.tag("HTTP POST: ").d(pUrl.toString());
        RequestModel requestModel = new RequestModel();
        try {
            HttpURLConnection conn = (HttpURLConnection) pUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=ISO-8859-1");
            conn.setRequestProperty("Accept", "application/json");
            conn = (HttpURLConnection) setTokenOnUrlHeader(conn);
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(pJson);
            wr.flush();
            wr.close();

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("ISO-8859-1")));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Timber.tag("HTTP POST: ").d(response.toString());
            requestModel.result = response.toString();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            requestModel.result = null;
            requestModel.error = ex;
        } catch (IOException ex) {
            ex.printStackTrace();
            requestModel.result = null;
            requestModel.error = ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            requestModel.result = null;
            requestModel.error = ex;
        }
        return requestModel;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static String postRegistros(URL pUrl, String pJson) {
        String resultString;
        try {
            Timber.d("POST/URL: %s", pUrl.toString());
            Timber.d("POST/Json: %s", pJson);

            HttpURLConnection conn = (HttpURLConnection) pUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=ISO-8859-1");
            conn.setRequestProperty("Accept", "application/json");
            conn = (HttpURLConnection) setTokenOnUrlHeader(conn);
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(pJson);
            wr.flush();
            wr.close();

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("ISO-8859-1")));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            resultString = response.toString();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            resultString = null;
        } catch (IOException ex) {
            ex.printStackTrace();
            resultString = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            resultString = null;
        }
        Timber.d("POST/Response: %s", resultString);
        return resultString;
    }

    public static String putRegistros(String pUrl, String pJson) {
        try {
            URL url = new URL(pUrl);
            return putRegistros(url, pJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String putRegistros(URL pUrl, String pJson) {
        String resultString;
        try {
            Timber.d("PUT/URL: %s", pUrl.toString());
            Timber.d("PUT/Json: %s", pJson);
            HttpURLConnection conn = (HttpURLConnection) pUrl.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=ISO-8859-1");
            conn.setRequestProperty("Accept", "application/json");
            setTokenOnUrlHeader(conn);
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(pJson);
            wr.flush();
            wr.close();

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("ISO-8859-1")));

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            resultString = response.toString();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Timber.e(ex);
            resultString = null;
        } catch (IOException ex) {
            ex.printStackTrace();
            Timber.e(ex);
            resultString = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            Timber.e(ex);
            resultString = null;
        }
        Timber.d("PUT/Response: %s", resultString);
        return resultString;
    }

    public static <T> T[] getArrayObject(String pUrl, Class<T[]> pArrayObjectClass) throws IOException {
        try {
            Timber.d("GET/URL: %s", pUrl);
            String resultString = getRegistros(pUrl);
            if (resultString != null) {
                Timber.d("GET/Response: %s", resultString);
                return new GsonBuilder()
                        .setDateFormat(Config.FormatDateTimeStringBancoJson)
                        .create().fromJson(resultString, pArrayObjectClass);
            }
            Timber.e("GET/URL: Failed");
        } catch (Throwable ex) {
            FirebaseCrashlytics.getInstance().recordException(ex);
            throw ex;
        }
        return null;
    }

    public static <T> T[] getArrayObjectSemTimeout(String pUrl, Class<T[]> pArrayObjectClass) throws IOException {
        try {
            Timber.d("GET/URL: %s", pUrl);
            String resultString = getRegistrosSemTimeout(pUrl, true);
            if (resultString != null) {
                Timber.d("GET/Response: %s", resultString);
                return new GsonBuilder()
                        .setDateFormat(Config.FormatDateTimeStringBancoJson)
                        .create().fromJson(resultString, pArrayObjectClass);
            }
            Timber.e("GET/URL: Failed");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return null;
    }

    public static String convertStreamToString(InputStream pInputStream, boolean pNewLine) {
        Util_IO.StringBuilder stringBuilder = new Util_IO.StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (pNewLine)
                    stringBuilder.appendLine(line);
                else
                    stringBuilder.append(line);
            }
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                pInputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public static boolean verificarHorarioComercial(Context pContext, boolean pAppAberto) {
        if (pAppAberto) {
            try {
                Colaborador colaborador = new DBColaborador(pContext).get();
                if (!colaborador.checkIfBloqueiaHorario()) return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                Timber.e(ex);
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return false;
        }

        LocalTime agora = LocalTime.now();
        LocalTime initHour = LocalTime.of(7, 0);
        LocalTime endHour = LocalTime.of(day == 7 ? 12 : 18, 0);

        DBHorarioNotificacao dbHorarioNotificacao = new DBHorarioNotificacao(pContext);
        HorarioNotificacao horarios = dbHorarioNotificacao.getHorarioNotificacaoPorDiaSemana(day);
        if (horarios != null) {
            initHour = toLocalTime(horarios.getHoraUm(), 7);
            endHour = toLocalTime(horarios.getHoraQuatro(), day == 7 ? 12 : 18);
        }

        return agora.isAfter(initHour) && agora.isBefore(endHour);
    }

    private static LocalTime toLocalTime(String hora, int padrao) {
        if (StringUtils.isEmpty(hora)) return LocalTime.of(padrao, 0);

        String[] tempo = hora.split(":");

        return LocalTime.of(Integer.parseInt(tempo[0]), Integer.parseInt(tempo[1]));
    }

    public static void retornaMensagem(Context pContexto, String pTexto, boolean pLongo) {
        if (pLongo)
            Toast.makeText(pContexto, pTexto, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(pContexto, pTexto, Toast.LENGTH_SHORT).show();
    }

    public static String encodeToBase64(Bitmap pImage, Bitmap.CompressFormat pCompressFormat, int pQuality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        pImage.compress(pCompressFormat, pQuality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String pInput) {
        byte[] decodedBytes = Base64.decode(pInput, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static boolean pingHost() {
        try {
            Runtime runtime = Runtime.getRuntime();
            int timeout = 1;
            String host = "201.65.56.210";
            //timeout /= 1000;
            String cmd = "ping -c 1 -W " + timeout + " " + host;
            Process proc = runtime.exec(cmd);
            proc.waitFor();
            int exit = proc.exitValue();
            return exit == 0;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return false;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isConectado(Context pContext) {
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }

        return networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static int[] retornaDiaSemanaAtendimento(Colaborador pColaborador) throws Exception {
        int dayOfWeek, weekOfMonth;

        //Calendario
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        if (weekOfMonth == 5)
            weekOfMonth = 1;
        else if (weekOfMonth == 6)
            weekOfMonth = 2;

        if (pColaborador.getSemanaRota() > 0)
            weekOfMonth = pColaborador.getSemanaRota();

        return new int[]{dayOfWeek, weekOfMonth};
    }

    public static boolean setupTrustedCameraIntent(Context context, Intent intentOut) {
        String[] trustedCameraPackages = {
                "com.google.android.GoogleCamera",
                "com.sec.android.app.camera",
                "com.motorola.camera",
                "com.motorola.camera2",
                "com.android.camera",
                "com.oneplus.camera",
                "com.lge.camera",
                "com.huawei.camera",
                "com.oppo.camera",
                "com.asus.camera"
        };

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> cameraApps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo app : cameraApps) {
            String packageName = app.activityInfo.packageName;

            for (String trusted : trustedCameraPackages) {
                if (packageName.equals(trusted)) {
                    intent.setPackage(packageName);
                    // Copia os dados para o intent de saída (por referência, se for o mesmo objeto)
                    intentOut.setAction(intent.getAction());
                    intentOut.setPackage(intent.getPackage());
                    intentOut.setComponent(intent.getComponent());
                    return true;
                }
            }
        }

        return false;
    }


    public static void loadImagefromCamera(Context pContext, File pFile, boolean justCamera) throws Exception {
        if (!(pContext instanceof Activity)) {
            throw new IllegalArgumentException("Context must be an Activity to startActivityForResult.");
        }
        if (pFile == null) {
            throw new IllegalArgumentException("pFile is null.");
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri outputUri = FileProvider.getUriForFile(
                pContext,
                BuildConfig.APPLICATION_ID + ".provider",
                pFile
        );

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setClipData(ClipData.newRawUri("output", outputUri));

        if (justCamera) {
            setupTrustedCameraIntent(pContext, intent);
        }

        List<ResolveInfo> resInfoList = pContext.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            pContext.grantUriPermission(packageName, outputUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        ((Activity) pContext).startActivityForResult(intent, RequestCode.CaptureImagem);
    }

    public static void loadImagefromGallery(Context pContext) throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity) pContext).startActivityForResult(intent, RequestCode.GaleriaImagem);
    }

    public static File getImagemFromGallery(Intent data, Context context) throws Exception {
        if (context == null) {
            throw new IllegalArgumentException("Context não pode ser nulo.");
        }

        if (data == null) {
            throw new Exception("Nenhuma imagem foi selecionada.");
        }

        final Uri uri = data.getData();
        if (uri == null) {
            throw new Exception("Não foi possível obter a imagem selecionada.");
        }


        cleanupGalleryTempFiles(context.getCacheDir(), "IMG_", ".jpg",
                /*maxAgeMillis*/ TimeUnit.DAYS.toMillis(7),
                /*maxFiles*/ 100);

        final File tempFile = File.createTempFile("IMG_", ".jpg", context.getCacheDir());
        boolean success = false;

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);


            if (is == null) {
                throw new IOException("Não foi possível abrir a imagem selecionada. Tente novamente.");
            }

            try (InputStream input = is; OutputStream os = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[8 * 1024];
                int length;
                while ((length = input.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                success = true;
                return tempFile;
            }
        } catch (Exception e) {
            Timber.e(e, "Falha ao obter imagem da galeria. uri=%s", uri);
            throw e;
        } finally {
            if (!success) {
                try {
                    tempFile.delete();
                } catch (Exception ignored) {
                }
            }
        }
    }


    private static void cleanupGalleryTempFiles(File cacheDir,
                                                String prefix,
                                                String extension,
                                                long maxAgeMillis,
                                                int maxFiles) {
        if (cacheDir == null || !cacheDir.exists() || !cacheDir.isDirectory()) return;

        File[] files = cacheDir.listFiles((dir, name) ->
                name != null && name.startsWith(prefix) && name.endsWith(extension));

        if (files == null || files.length == 0) return;

        final long now = System.currentTimeMillis();


        for (File f : files) {
            try {
                if (f != null && f.isFile() && (now - f.lastModified()) > maxAgeMillis) {
                    f.delete();
                }
            } catch (Exception ignored) {
            }
        }


        files = cacheDir.listFiles((dir, name) ->
                name != null && name.startsWith(prefix) && name.endsWith(extension));
        if (files == null || files.length <= maxFiles) return;


        java.util.Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
        int toDelete = files.length - maxFiles;

        for (int i = 0; i < toDelete; i++) {
            try {
                files[i].delete();
            } catch (Exception ignored) {
            }
        }
    }

    public static void atualizarLocalizacaoCliente(final Context pContext, final File pFile, Boolean justCamera) {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name),
                "Para iniciar a visita é necessario tirar uma foto da fachada do ponto de venda para atualizar a localização");
        alerta.showConfirm((dialog, which) -> {
            try {
                loadImagefromCamera(pContext, pFile, justCamera);
            } catch (Exception ex) {
                Mensagens.mensagemErro(pContext, ex.getMessage(), false);
            }
        }, null);
    }

    public static File setImagem() {
        return new File(Environment.getExternalStorageDirectory() + "/DCIM/", "IMG_" + System.currentTimeMillis() + ".jpg");
    }

    public static String getFilePath(Context pContext) {
        File mediaStorageDir = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/"
                + pContext.getPackageName()
                + "/Files/Compressed");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
            mediaStorageDir.mkdirs();

        return (mediaStorageDir.getAbsolutePath() + "/");
    }

    public static String getFilename(Context pContext) {
        String mImageName = "IMG_" + System.currentTimeMillis() + ".jpg";
        return getFilePath(pContext) + mImageName;
    }

    public static void capturarLocal(GPSTracker pGpsTracker, String pIdCliente, String pLocalImagem, Context pContext) throws Exception {
        LocalizacaoCliente locCli = new LocalizacaoCliente();
        locCli.setIdCliente(pIdCliente);
        locCli.setLatitude(pGpsTracker.getLatitude());
        locCli.setLongitude(pGpsTracker.getLongitude());
        locCli.setPrecisao(pGpsTracker.getPrecisao());
        locCli.setLocalArquivo(pLocalImagem);
        new DBLocalizacaoCliente(pContext).addLocalizacao(locCli);
        new DBCliente(pContext).updateAtualizaLocal(pIdCliente, "N");
    }

    public static void iniciarAtendimento(GPSTracker pGpsTracker,
                                          Context pContext,
                                          double pLatitude,
                                          double pLongitude,
                                          String pCodigoCliente,
                                          ProjetoTrade pProjetoTrade,
                                          int origem) throws Exception {

        Colaborador colaborador = new DBColaborador(pContext).get();

        if (pGpsTracker == null) {
            pGpsTracker = new GPSTracker(pContext);
        }

        if (!pGpsTracker.isGPSEnabled) {
            pGpsTracker.showSettingsAlert();
            return;
        }

        if (pGpsTracker.isMockLocationON || pGpsTracker.areThereMockPermissionApps()) {
            pGpsTracker.showMockAlert();
            return;
        }

        // Implementado para que não tire a permissão da localização e mantenha
        // os dados antigos, e force o usuário a habilitar novamente
        if (!pGpsTracker.isGPSActive(pContext)) {
            pGpsTracker.showAlertGPS();
            return;
        }

        Location locationA = new Location("point A");
        locationA.setLatitude(pGpsTracker.getLatitude());
        locationA.setLongitude(pGpsTracker.getLongitude());

        Location locationB = new Location("point B");
        locationB.setLatitude(pLatitude);
        locationB.setLongitude(pLongitude);

        float distance = locationA.distanceTo(locationB);
        distance = pGpsTracker.getPrecisao() == 0 ? 0 : Math.round(distance);

        Log.i("log", "distance: " + distance);
        Cliente cliente = new DBCliente(pContext).getById(pCodigoCliente);
        if ((pLatitude != 0 && pLongitude != 0) && ((colaborador.getDistancia() > 0 || cliente.getCerca() > 0) && pGpsTracker.getPrecisao() > 0)) {
            if (cliente.getCerca() > 0) {
                if (distance > (cliente.getCerca() + pGpsTracker.getPrecisao())) {
                    Mensagens.naoEstaNasImediacoes(pContext);
                    return;
                }
            } else {
                if (distance > (colaborador.getDistancia() + pGpsTracker.getPrecisao())) {
                    Mensagens.naoEstaNasImediacoes(pContext);
                    return;
                }
            }
        }

        DBVisita dbVisita = new DBVisita(pContext);
        Visita visita = dbVisita.getVisitaAtiva();
        long id;
        if (visita == null) {
            id = dbVisita.novaVisita(
                    pCodigoCliente,
                    pGpsTracker.getLatitude(),
                    pGpsTracker.getLongitude(),
                    pGpsTracker.getPrecisao(),
                    distance,
                    (pProjetoTrade == null) ? null : pProjetoTrade.getId(),
                    origem
            );
        } else {
            id = visita.getId();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Config.CodigoVisita, (int) id);
        bundle.putString(Config.CodigoCliente, cliente.getId());
        if (colaborador.isNovoAtend())
            Utilidades.openNewActivity(pContext, VendaAberturaActivity.class, bundle, true);
        else
            Utilidades.openNewActivity(pContext, AtendimentoActivity.class, bundle, false);
    }


    private static boolean calculateMinorDistance(Cliente cliente, Colaborador colaborador) {
        if (cliente.getCerca() == 0) return false;
        return cliente.getLatitude() != 0 || colaborador.getDistancia() != 0;
    }

    public static void iniciarAtendimentoReconsigna(GPSTracker pGpsTracker,
                                                    Context pContext,
                                                    double pLatitude,
                                                    double pLongitude,
                                                    String pCodigoCliente,
                                                    ProjetoTrade pProjetoTrade,
                                                    int origem) throws Exception {

        Colaborador colaborador = new DBColaborador(pContext).get();

        if (pGpsTracker == null) {
            pGpsTracker = new GPSTracker(pContext);
        }

        if (!pGpsTracker.isGPSEnabled) {
            pGpsTracker.showSettingsAlert();
            return;
        }

        if (pGpsTracker.isMockLocationON || pGpsTracker.areThereMockPermissionApps()) {
            pGpsTracker.showMockAlert();
            return;
        }

        Location locationA = new Location("point A");
        locationA.setLatitude(pGpsTracker.getLatitude());
        locationA.setLongitude(pGpsTracker.getLongitude());

        Location locationB = new Location("point B");
        locationB.setLatitude(pLatitude);
        locationB.setLongitude(pLongitude);

        float distance = locationA.distanceTo(locationB);
        distance = pGpsTracker.getPrecisao() == 0 ? 0 : Math.round(distance);

        Cliente cliente = new DBCliente(pContext).getById(pCodigoCliente);
        if ((pLatitude != 0 && pLongitude != 0) && ((colaborador.getDistancia() > 0 || cliente.getCerca() > 0) && pGpsTracker.getPrecisao() > 0)) {
            if (cliente.getCerca() > 0) {
                if (distance > (cliente.getCerca() + pGpsTracker.getPrecisao())) {
                    Mensagens.naoEstaNasImediacoes(pContext);
                    return;
                }
            } else {
                if (distance > (colaborador.getDistancia() + pGpsTracker.getPrecisao())) {
                    Mensagens.naoEstaNasImediacoes(pContext);
                    return;
                }
            }
        }

        DBVisita dbVisita = new DBVisita(pContext);
        Visita visita = dbVisita.getVisitaAtiva();
        long id;
        if (visita == null) {
            id = dbVisita.novaVisita(
                    pCodigoCliente,
                    pGpsTracker.getLatitude(),
                    pGpsTracker.getLongitude(),
                    pGpsTracker.getPrecisao(),
                    distance,
                    (pProjetoTrade == null) ? null : pProjetoTrade.getId(),
                    origem
            );
        } else {
            id = visita.getId();
        }
    }

    public static void showKeyboard(EditText pEditText, Context pContext) {
        pEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) pContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void hideKeyboard(EditText pEditText, Context pContext) {
        pEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pEditText.getWindowToken(), 0);
    }

    public static void hideKeyboard(View pView, Context pContext) {
        InputMethodManager imm = (InputMethodManager) pContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pView.getWindowToken(), 0);
    }

    public static boolean deletaArquivo(String pLocalArquivo) {
        File file = new File(pLocalArquivo);
        return file.delete();
    }

    public static void setcorLayoutImagem(LinearLayout pLinearLayout, String pStatus, TextView pTextView) {
        try {
            if (pStatus.equals("3")) {
                pLinearLayout.setBackgroundColor(Color.parseColor("#EE0000"));
                pTextView.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                pLinearLayout.setBackgroundColor(Color.parseColor("#EEEEE0"));
                pTextView.setTextColor(Color.parseColor("#A52A2A"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void defineSpinner(SearchableSpinner pSearchableSpinner) {
        pSearchableSpinner.setTitle("Selecionar item");
        pSearchableSpinner.setPositiveButton("OK");
    }

    public static void listarPendencias(Context pContext) throws Exception {
        RelativeLayout layout = ((Activity) pContext).findViewById(R.id.rfpendencia);
        ArrayList<Pendencias> lista = new DBSuporte(pContext).getPendencias();
        PendenciasAdapter mAdapter = new PendenciasAdapter(pContext, lista);
        RecyclerView mRecyclerView = ((Activity) pContext).findViewById(R.id.lvRfPendencias);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(pContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        if (lista != null && lista.size() > 0)
            layout.setVisibility(View.VISIBLE);
        else
            layout.setVisibility(View.GONE);
    }

    public static void listarRotas(final Context pContext) throws Exception {
        Colaborador colaborador = new DBColaborador(pContext).get();
        int[] atendimento = Utilidades.retornaDiaSemanaAtendimento(colaborador);
        int dayOfWeek = atendimento[0];
        int weekOfMonth = atendimento[1];
        DBRota dbRota = new DBRota(pContext);
        Rota rota = dbRota.getRotaByDiaSemana(dayOfWeek, weekOfMonth);
        boolean bExisteRota = dbRota.existeRotaDia(dayOfWeek, weekOfMonth);

        TextView txtClienteRota = ((Activity) pContext).findViewById(R.id.txtCliente);
        TextView txtEnderecoRota = ((Activity) pContext).findViewById(R.id.txtEndereco);
        TextView txtTituloRota = ((Activity) pContext).findViewById(R.id.txtTituloRota);
        TextView txtCurvaChip = ((Activity) pContext).findViewById(R.id.txtCurvaChip);
        TextView txtCurvaRecarga = ((Activity) pContext).findViewById(R.id.txtCurvaRecarga);
        TextView btnVerRotas = ((Activity) pContext).findViewById(R.id.btnVerRotas);

        if (rota != null) {
            txtClienteRota.setText(rota.getCliente());
            txtEnderecoRota.setText(rota.getEndereco());
            txtClienteRota.setVisibility(View.VISIBLE);
            txtEnderecoRota.setVisibility(View.VISIBLE);
            txtTituloRota.setText("ROTA | Próximo Cliente");
            Cliente cliente = new DBCliente(pContext).getById(rota.getIdCliente());
            if (cliente != null && !Util_IO.isNullOrEmpty(cliente.getCurvaRecarga()))
                txtCurvaRecarga.setText("Curva Recarga: " + cliente.getCurvaRecarga().toUpperCase().replace("CURVA", ""));
            else
                txtCurvaRecarga.setVisibility(View.GONE);
            if (cliente != null && !Util_IO.isNullOrEmpty(cliente.getCurvaChip()))
                txtCurvaChip.setText("Curva Chip: " + cliente.getCurvaChip().toUpperCase().replace("CURVA", ""));
            else
                txtCurvaChip.setVisibility(View.GONE);
        } else {
            String mensagem = (bExisteRota) ? "Foram realizados todos os atendimentos de hoje!" : "Nenhuma rota disponível para atendimento hoje";
            txtTituloRota.setText(mensagem);
            txtClienteRota.setVisibility(View.GONE);
            txtEnderecoRota.setVisibility(View.GONE);
            txtCurvaChip.setVisibility(View.GONE);
            txtCurvaRecarga.setVisibility(View.GONE);
        }

        btnVerRotas.setOnClickListener((view) -> {
            Intent intent = new Intent(pContext, RotaDiariaActivity.class);
            pContext.startActivity(intent);
        });
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void addRegId(int pIdAdicionarRegId) {
        try {
            String regid = FirebaseInstanceId.getInstance().getToken();
            if (regid == null)
                regid = "null";
            String urlfinal = URLs.COLABORADOR + "?idColaborador=" + pIdAdicionarRegId + "&regId=" + regid;
            getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void removeRegId(int idRemoverRegId) {
        try {
            String urlfinal = URLs.COLABORADOR + "?idColaborador=" + idRemoverRegId + "&regId=" + "";
            getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void openNewApp(Context pContext, String pPackageName) throws Exception {
        Intent intent = pContext.getPackageManager().getLaunchIntentForPackage(pPackageName);
        if (intent == null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + pPackageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pContext.startActivity(intent);
    }

    public static void openRota(Context pContext, boolean pFechar) {
        Visita visita = new DBVisita(pContext).getVisitaAtiva();
        if (visita != null) {
            openAtendimento(pContext, pFechar, visita);
            return;
        }

        Intent intent;
        if (pContext instanceof RotaActivity) {
            intent = new Intent(pContext, NovaRotaActivity.class);
        } else {
            intent = new Intent(pContext, RotaActivity.class);
        }

        pContext.startActivity(intent);
        if (pFechar) {
            ((Activity) pContext).finish();
        }
    }

    public static void openAtendimento(Context pContext, boolean pFechar, Visita pVisita) {
        Cliente cliente = new DBCliente(pContext).getById(pVisita.getIdCliente());
        if (cliente == null) {
            final int motivoCancelamento = 15;
            new DBVisita(pContext).encerrarVisitaSemDataFim(pVisita.getId(),
                    motivoCancelamento,
                    true,
                    pVisita.getIdCliente());
            openRota(pContext, pFechar);
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(Config.CodigoVisita, pVisita.getId());
        bundle.putString(Config.CodigoCliente, cliente.getId());

        Colaborador colaborador = new DBColaborador(pContext).get();
        if (colaborador.isNovoAtend()) {
            Utilidades.openNewActivity(pContext, VendaAberturaActivity.class, bundle, pFechar);
        } else {
            Utilidades.openNewActivity(pContext, AtendimentoActivity.class, bundle, pFechar);
        }
    }

    public static void openAuditagemCliente(final Context pContext, final String pCodigoCliente, boolean pInicio) {
        String mensagem = "Deseja realizar a auditagem do estoque do Ponto de venda?";
        if (pInicio)
            mensagem = "Deve ser realizada a auditagem do estoque do Ponto de venda, Deseja continuar?";
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
        alerta.showConfirm((dialog, which) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, pCodigoCliente);
            Utilidades.openNewActivity(pContext, AuditagemClienteActivity.class, bundle, true);
        }, null);
    }

    public static void openSuporte(Context pContext) {
        Utilidades.openNewActivity(pContext, SuporteActivity.class, null, false);
    }

    public static void encerrarAtendimento(final Context pContext,
                                           final boolean pRemoveVenda,
                                           final Venda pVenda,
                                           final LimiteCliente pLimiteCliente,
                                           final Visita pVisita,
                                           final ArrayList<ItemVenda> pListaItens,
                                           final int pIdMotivo,
                                           final CountDownTimer pTimer) throws Exception {

        if (!Validacoes.validacaoDataAparelho(pContext)) {
            return;
        }

        final ProgressDialog mDialog = new ProgressDialog(pContext);
        mDialog.setMessage("Aguarde...");
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();

        new Thread(() -> ((Activity) pContext).runOnUiThread(() -> {
            try {
                finalizaVisita(pContext, pRemoveVenda, pVenda, pLimiteCliente, pVisita, pListaItens, pIdMotivo);
            } catch (Exception ex) {
                Mensagens.mensagemErro(pContext, ex.getMessage(), false);
                if (mDialog.isShowing()) mDialog.dismiss();
                return;
            }

            if (pTimer != null) pTimer.cancel();
            mDialog.dismiss();

            Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Visita encerrada.");
            alerta.show((dialog, which) -> {
                try {
                    new VendaSyncTask(pContext).execute();
                    if (pVisita.getOrigem() == Config.NovosCredenciadosValue) {
                        ((Activity) pContext).finish();
                    }

                    // Caso seja uma venda com Referente a uma consignação método de fechamento será diferente
                    if (pVenda != null && pVenda.getIdConsignadoRefer() > 0) {
                        ((Activity) pContext).setResult(Activity.RESULT_OK);
                        ((Activity) pContext).finish();
                    } else {
                        openRota(pContext, true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        })).start();
    }

    public static void finalizaVisita(Context pContext, boolean pRemoveVenda, Venda pVenda, LimiteCliente pLimiteCliente
            , Visita pVisita, ArrayList<ItemVenda> pListaItens, int pIdMotivo) throws Exception {
        DBVenda dbVenda = new DBVenda(pContext);
        String idCliente = "";
        if (pRemoveVenda) {
            DBEstoque dbEstoque = new DBEstoque(pContext);
            if (pVenda == null) {
                pVenda = dbVenda.getVendabyIdVisita(pVisita.getId());
            }

            if (pVenda != null) {
                idCliente = pVenda.getIdCliente();
                new DBPreco(pContext).removeIdVendaGeral(String.valueOf(pVenda.getId()));
            }

            if (pListaItens == null && pVenda != null) {
                pListaItens = dbVenda.getItensVendabyIdVenda(pVenda.getId());
            }

            dbEstoque.atualizaEstoqueVenda(pListaItens);

            if (pVenda != null) {
                dbEstoque.deletarPistolagensVenda(pVenda);
            }
        } else {
            if (pVenda == null) {
                pVenda = dbVenda.getVendabyIdVisita(pVisita.getId());
            }

            if (pVenda != null) {
                idCliente = pVenda.getIdCliente();
                double dValor = dbVenda.retornaValorTotalVenda(pVenda.getId());
                dbVenda.updateValorTotalVenda(dValor, pVenda.getId());
                new DBIccid(pContext).deletaIccid(pVenda.getId());
                if (pVenda.getIdFormaPagamento() == 2 && pLimiteCliente != null)
                    new DBLimite(pContext).atualizaSaldo(dValor, pLimiteCliente.getId());
            }
        }

        if (idCliente.equals("")) {
            idCliente = pVisita.getIdCliente();
        }

        new DBVisita(pContext).encerrarVisita(pVisita.getId(), pIdMotivo, pRemoveVenda, idCliente);
        new DBEstoque(pContext).deletarPistolagensFinalizadas();
        new DBAtualizarCliente(pContext).atualizarStatusConcluido(idCliente);
    }

    public static Preco getPreco(Produto pProduto, String pIdCliente, Context pContext) throws Exception {
        Preco preco = null;
        DBPreco dbPreco = new DBPreco(pContext);
        List<PrecoDiferenciado> listaPreco = dbPreco.getPrecoDiferenciadoCliente(pProduto.getId(), pIdCliente);
        if (listaPreco != null && listaPreco.size() > 0) {
            preco = new Preco();
            preco.setIdPreco(String.valueOf(listaPreco.get(0).getId()));
            preco.setValor(listaPreco.get(0).getValor());
        } else {
            listaPreco = dbPreco.getPrecoDiferenciado(pProduto.getId());
            if (listaPreco != null && listaPreco.size() > 0) {
                preco = new Preco();
                preco.setIdPreco(String.valueOf(listaPreco.get(0).getId()));
                preco.setValor(listaPreco.get(0).getValor());
            }
        }

        if (preco == null) {
            preco = new Preco();
            preco.setIdPreco("");
            preco.setValor(pProduto.getPrecovenda());
        }
        return preco;
    }

    public static String getValorRd(RadioGroup pRadioGroup) {
        try {
            return ((RadioButton) pRadioGroup.findViewById(pRadioGroup.getCheckedRadioButtonId())).getText().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static int getQtdBipadaICCID(Produto pProduto, ProdutoCombo pProdutoCombo, ArrayList<CodBarra> pItensCod, UsoCodBarra usoCodBarra) {
        int quantidadebipada;
        if (pProduto.getQtdCombo() > 0) {
            if (pProdutoCombo == null)
                pProdutoCombo = CodigoBarra.retornaCombo(pProduto.getQtdCombo(), pItensCod, usoCodBarra);
            quantidadebipada = pProdutoCombo.getQtdTotal();
        } else
            quantidadebipada = CodigoBarra.quantidadeBipada(pItensCod, usoCodBarra);
        return quantidadebipada;
    }

    public static void getCtrlC(Context pContext, String pTexto) {
        ClipboardManager clipboard = (ClipboardManager) pContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", pTexto);
        clipboard.setPrimaryClip(clip);
    }

    public static void addICCIDItensVenda(Context pContext, Produto pProduto, int pQuantidade, double pValor, Venda pVenda
            , PrecoDiferenciado pPrecoDiferenciado, ArrayList<CodBarra> pItensCod, ArrayList<ComboVenda> pItensCombo) throws Exception {
        pProduto.setQtde(pQuantidade);

        if (pItensCombo == null || pItensCombo.isEmpty()) {
            addICCIDItemVenda(pContext, pPrecoDiferenciado, pVenda, pProduto, pItensCod, pValor, true, null);
        } else {
            addICCIDItemVenda(pContext, pPrecoDiferenciado, pVenda, pProduto, pItensCod, pValor, false, pItensCombo);
            DBEstoque dbEstoque = new DBEstoque(pContext);

            for (ComboVenda comboVenda : pItensCombo) {
                dbEstoque.atualizaEstoque(comboVenda.getIdProduto(), true, comboVenda.getQuantidade());
            }
        }
    }

    public static void addICCIDItemVenda(Context pContext, PrecoDiferenciado pPrecoDiferenciado, Venda pVenda, Produto pProduto, ArrayList<CodBarra> pItensCod
            , double pValor, boolean pAtualizaEstoque, ArrayList<ComboVenda> pItensCombo) throws Exception {
        DBEstoque dbEstoque = new DBEstoque(pContext);
        DBVenda dbVenda = new DBVenda(pContext);

        if (pPrecoDiferenciado != null && pPrecoDiferenciado.getQtdPreco() > 0 && dbVenda.retornaQtdPrecoDiferenciado(String.valueOf(pPrecoDiferenciado.getId())) >= pPrecoDiferenciado.getQtdPreco()) {
            pPrecoDiferenciado = null;
            pValor = dbEstoque.getProdutoById(pProduto.getId()).getPrecovenda();
        }

        if (pPrecoDiferenciado != null) {
            DBPreco dbPreco = new DBPreco(pContext);
            long idVendaItem = dbVenda.addItemVenda(pVenda, pProduto, pItensCod, pValor, pItensCombo, String.valueOf(pPrecoDiferenciado.getId()));
            dbPreco.atualizaIdVenda(String.valueOf(pPrecoDiferenciado.getId()), String.valueOf(pVenda.getId()), String.valueOf(idVendaItem), pProduto.getQtde());
        } else {
            dbVenda.addItemVenda(pVenda, pProduto, pItensCod, pValor, pItensCombo, null);
        }

        if (pAtualizaEstoque) {
            dbEstoque.atualizaEstoque(pProduto.getId(), true, pProduto.getQtde());
        }
    }

    public static Visita getVisita(Context pContext) throws Exception {
        int codigoVisita = 0;
        DBVisita dbVisita = new DBVisita(pContext);
        Bundle bundle = ((Activity) pContext).getIntent().getExtras();
        if (bundle != null)
            codigoVisita = bundle.getInt(Config.CodigoVisita);

        if (codigoVisita == 0) {
            Mensagens.visitaNaoIniciada(pContext);
            return null;
        }

        Visita visita = dbVisita.getVisitabyId(codigoVisita);
        if (visita == null) {
            Mensagens.visitaNaoIniciada(pContext);
            return null;
        }

        return visita;
    }

    public static Venda getVenda(Context pContext, Visita pVisita) throws Exception {
        DBVenda dbVenda = new DBVenda(pContext);
        Venda venda = dbVenda.getVendabyIdVisita(pVisita.getId());
        if (venda == null) {
            long codigo = dbVenda.novaVenda(pVisita.getId(), pVisita.getIdCliente());
            venda = dbVenda.getVendabyId((int) codigo);
        }

        return venda;
    }

    public static Consignado getConsignado(Context pContext, Visita pVisita) throws Exception {
        BDConsignado bdConsignado = new BDConsignado(pContext);
        Consignado consignado = bdConsignado.getConsignadobyIdVisita(String.valueOf(pVisita.getId()));
        if (consignado == null) {
            long codigo = bdConsignado.novoConsignado(pVisita, new DBColaborador(pContext).get().getId(), 0);
            consignado = bdConsignado.getById(String.valueOf(codigo));
        } else {
            Log.d("Roni", "getConsignado: " + consignado.getId());
        }

        return consignado;
    }

    public static void openMotivo(final Context pContext, final Visita pVisita) throws Exception {
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                , "Deseja realmente encerrar o atendimento? Se houver informações de venda, elas serão perdidas!");
        alerta.showConfirm((dialog, which) -> {
            try {
                MotivoDialog motivoDialog = new MotivoDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(Config.CodigoVisita, pVisita.getId());
                motivoDialog.setArguments(bundle);
                motivoDialog.show(((FragmentActivity) pContext).getSupportFragmentManager(), "tag");
            } catch (Exception ex) {
                Mensagens.mensagemErro(pContext, ex.getMessage(), false);
            }
        }, null);
    }

    public static void enviaPedido(Context pContext, Venda pVenda, Visita pVisita, ArrayList<ItemVenda> pList) throws Exception {
        switch (pVenda.getIdFormaPagamento()) {
            case 0:
                throw new Exception("Forma de pagamento não selecionada, Verifique!");
            case 1:
                encerrarAtendimento(pContext, false, pVenda, null, pVisita, pList, 0, null);
                break;
            case 2:
                if (new DBCliente(pContext).getById(pVisita.getIdCliente()).getPedeSenha().equalsIgnoreCase("S"))
                    openSenha(pContext, pVenda);
                else
                    encerrarAtendimento(pContext, false, pVenda, null, pVisita, pList, 0, null);
                break;
            // Forma de Pagamento Pix
            case 6:
                encerrarAtendimento(pContext, false, pVenda, null, pVisita, pList, 0, null);
                break;
            // Forma de Pagamento Cartão crédito
            case 7:
                encerrarAtendimento(pContext, false, pVenda, null, pVisita, pList, 0, null);
                break;
            default:
                throw new Exception("Forma de pagamento não selecionada, Verifique!");
        }
    }

    public static void compartilharTela(Context pContext) throws Exception {
        View view = ((Activity) pContext).getWindow().getDecorView();

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        File fotoFinal = new File(getFilename(pContext));
        FileOutputStream outStream = new FileOutputStream(fotoFinal);
        returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();

        //Compartilhar
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(pContext, BuildConfig.APPLICATION_ID + ".provider", fotoFinal);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        pContext.startActivity(Intent.createChooser(shareIntent, "Compartilhar com"));
    }

    public static void compartilharTexto(Context pContext, String pTexto) throws Exception {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, pTexto);
        shareIntent.setType("text/plain");
        pContext.startActivity(Intent.createChooser(shareIntent, "Compartilhar com"));
    }

    public static void openSenha(Context pContext, Venda pVenda) throws Exception {
        try {
            ConfirmarSenhaDialog dialog = new ConfirmarSenhaDialog();
            dialog.myCompleteListenerSenha = null;
            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, pVenda.getIdCliente());
            args.putString(Config.CodigoVenda, String.valueOf(pVenda.getId()));
            dialog.setArguments(args);
            dialog.show(((FragmentActivity) pContext).getSupportFragmentManager(), "tag");
        } catch (Exception ex) {
            Mensagens.mensagemErro(pContext, ex.getMessage(), false);
        }
    }

    public static boolean verificaItensDuplicado(Context pContext, ArrayList<ItemVenda> pItemVendas, Produto pProduto) {
        if (pItemVendas != null && pItemVendas.size() > 0) {
            for (ItemVenda item : pItemVendas) {
                if (item.getIdProduto().equals(pProduto.getId())) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                            , "O produto " + pProduto.getNome() + " já foi inclúido, Verifique");
                    alerta.show();
                    return true;
                }
            }
        }

        return false;
    }

    public static void atualizaTotalVenda(DBVenda pDbVenda, Venda pVenda, TextView pTextView, boolean pExibeInfo) {
        String sValorTotal = "";
        if (pExibeInfo)
            sValorTotal = "Total R$ ";
        if (pVenda != null && pDbVenda != null)
            sValorTotal += Util_IO.formatDoubleToDecimalNonDivider(pDbVenda.retornaValorTotalVenda(pVenda.getId()));
        else
            sValorTotal += "0,00";
        pTextView.setText(sValorTotal);
    }

    public static void removerItemVenda(Context pContext, String pIdProduto, int pQuantidade, int pIdVendaItem) throws Exception {
        DBVenda dbVenda = new DBVenda(pContext);
        DBEstoque dbEstoque = new DBEstoque(pContext);
        ItemVenda itemVenda = dbVenda.getItemVendaById(pIdVendaItem);

        if (itemVenda.isCombo()) {
            dbVenda.removeEstoqueComboByIdVendaItem(itemVenda.getId());
        } else {
            dbEstoque.atualizaEstoque(pIdProduto, false, pQuantidade);
        }

        new DBPreco(pContext).removeIdVenda(String.valueOf(pIdVendaItem));
        dbVenda.deleteItemByIdItem(pIdVendaItem);
    }

    public static void compressImage(String imagePath, String filePath) {
        final float maxHeight = 1280.0f;
        final float maxWidth = 1280.0f;
        Bitmap scaledBitmap = null;
        Bitmap bmp = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            InputStream in = new FileInputStream(imagePath);
            bmp = BitmapFactory.decodeStream(in, null, options);
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            bmp = BitmapFactory.decodeFile(imagePath, options);
            ex.printStackTrace();
        }

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        // fallback: caso não tenha sido possivel converter a imagem, enviamos a mesma imagem
        if (bmp == null) {
            try {
                copyFile(imagePath, filePath);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            return;
        }

        try {
            scaledBitmap = Bitmap.createBitmap((actualWidth > 0) ? actualWidth : 1, (actualHeight > 0) ? actualHeight : 1, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if (bmp != null) {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options pOptions, int pReqWidth, int pReqHeight) {
        final int height = pOptions.outHeight;
        final int width = pOptions.outWidth;
        int inSampleSize = 1;

        if (height > pReqHeight || width > pReqWidth) {
            final int heightRatio = Math.round((float) height / (float) pReqHeight);
            final int widthRatio = Math.round((float) width / (float) pReqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = pReqWidth * pReqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static int getScreenOrientation(Context pContext) {
        WindowManager wm = (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            Point size = new Point();
            display.getSize(size);
            if (size.x < size.y)
                orientation = Configuration.ORIENTATION_PORTRAIT;
            else
                orientation = Configuration.ORIENTATION_LANDSCAPE;
        } else {
            if (display.getWidth() == display.getHeight())
                orientation = Configuration.ORIENTATION_SQUARE;
            else {
                if (display.getWidth() < display.getHeight()) {
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                } else {
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }
            }
        }

        return orientation;
    }

    public static String getDateName(Date pDate) {
        if (pDate == null)
            return "";
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(pDate);
        return Util_IO.dateToStringBr(currentDate.getTime()) + " - " + Util_IO.toTitleCase(currentDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
    }

    public static void setButtonCalendar(Context pContext, final EditText pEditText) throws Exception {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(Config.FormatDateStringBr, Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();
        pEditText.setInputType(InputType.TYPE_NULL);

        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(pContext, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            pEditText.setText(dateFormatter.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        pEditText.setOnClickListener((view) -> {
            fromDatePickerDialog.show();
        });
    }

    public static ArrayList<Preco> getListaPrecos(Context pContext, Produto pProduto, String pIdCliente) throws Exception {
        ArrayList<Preco> listaPreco = new ArrayList<>();
        Preco preco;

        if (pProduto != null) {
            DBPreco dbPreco = new DBPreco(pContext);
            ArrayList<PrecoDiferenciado> listaPrecoDiferenciado = dbPreco.getPrecoDiferenciado(pProduto.getId());
            ArrayList<PrecoDiferenciado> listaPrecoCliente = dbPreco.getPrecoDiferenciadoCliente(pProduto.getId(), pIdCliente);

            if (listaPrecoCliente != null && listaPrecoCliente.size() > 0) {
                for (PrecoDiferenciado precoDiferenciado : listaPrecoCliente) {
                    preco = new Preco();
                    preco.setIdPreco(String.valueOf(precoDiferenciado.getId()));
                    preco.setValor(precoDiferenciado.getValor());
                    preco.setQuantidade(precoDiferenciado.getQtdPreco());
                    listaPreco.add(preco);
                }
            }

            if (listaPrecoDiferenciado != null && listaPrecoDiferenciado.size() > 0) {
                for (PrecoDiferenciado precoDiferenciado : listaPrecoDiferenciado) {
                    preco = new Preco();
                    preco.setIdPreco(String.valueOf(precoDiferenciado.getId()));
                    preco.setValor(precoDiferenciado.getValor());
                    preco.setQuantidade(precoDiferenciado.getQtdPreco());
                    listaPreco.add(preco);
                }
            }

            preco = new Preco();
            preco.setIdPreco("");
            preco.setValor(pProduto.getPrecovenda());
            listaPreco.add(preco);

            return listaPreco;
        } else
            return null;
    }

    public static boolean isCameraAvailable(Context pContext) {
        return pContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public static String getJsonFromClass(Object pObject) {
        return new GsonBuilder().setDateFormat(Config.FormatDateTimeStringBancoJson).create().toJson(pObject);
    }

    public static <T> T getClassFromJson(String pJson, Class<T> pClass) {
        return new GsonBuilder().setDateFormat(Config.FormatDateTimeStringBancoJson).create()
                .fromJson(pJson, pClass);
    }

    public static void openChamadoCliente(final Context pContext, final Chamado pChamado) {
        String mensagem = "Para continuar, Você deve realizar o atendimento do chamado " +
                ((pChamado.getChamadoID() != null && pChamado.getChamadoID() > 0) ? String.valueOf(pChamado.getChamadoID()) : "");
        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
        alerta.show((dialog, which) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoChamado, String.valueOf(pChamado.getIdAppMobile()));
            openNewActivity(pContext, ChamadoActivity.class, bundle, false);
        });
    }

    public static void openTelaDiscagem(Context pContext, String pNumberPhone) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pNumberPhone));
            pContext.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void openNewActivity(Context pContext, Class<?> pActivity, Bundle pParametros, boolean pFechar) {
        Intent intent = new Intent(pContext, pActivity);
        if (pParametros != null)
            intent.putExtras(pParametros);
        pContext.startActivity(intent);
        if (pFechar)
            ((Activity) pContext).finish();
    }

    public static void openNewActivityForResult(Context pContext, Class<?> pActivity, int pRequestCode, Bundle pParametros) {
        Intent intent = new Intent(pContext, pActivity);
        if (pParametros != null)
            intent.putExtras(pParametros);
        ((Activity) pContext).startActivityForResult(intent, pRequestCode);
    }

    public static <T> T firstOrDefault(ArrayList<T> pLista) {
        return (pLista != null && pLista.size() > 0) ? pLista.get(0) : null;
    }

    public static URLConnection setTokenOnUrlHeader(HttpURLConnection pUrlConnection) {
        String token = IMEI + "." + secretToken + "." + idVendedor + "." + (pUrlConnection.getURL().toString().contains("Colaborador?imei=") ? 0 : 1);
        pUrlConnection.setRequestProperty("rFM", token);
        return pUrlConnection;
    }

    public static AsyncHttpClient setTokenOnAsyncHttpClientHeader(AsyncHttpClient pAsyncHttpClient) {
        String token = IMEI + "." + secretToken + "." + idVendedor + ".0";
        pAsyncHttpClient.addHeader("rFM", token);
        return pAsyncHttpClient;
    }

    public static Gson getGson() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder()
                    .setLenient()
                    .create();
        }

        return gsonInstance;
    }

    public static long diferencaDatas(Date dataAtual, Date dataBanco, TimeUnit timeUnit) {
        long diffInMillies = dataAtual.getTime() - dataBanco.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static Intent obterIntentCompartilhar(LinearLayout v) {
        v.findViewById(R.id.comprovante_compartilhar).setVisibility(View.INVISIBLE);
        String bitmapPath = MediaStore.Images.Media.insertImage(
                v.getContext().getContentResolver(),
                getBitmapFromView(v),
                "Compartilhar",
                null);
        v.findViewById(R.id.comprovante_compartilhar).setVisibility(View.VISIBLE);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(INTENT_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);

        return intent;
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static CartaoPonto obterCartaoPonto(Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);

        if (!gpsTracker.isGPSEnabled) {
            gpsTracker.showSettingsAlert();
            return null;
        }

        if (gpsTracker.isMockLocationON || gpsTracker.areThereMockPermissionApps()) {
            gpsTracker.showMockAlert();
            return null;
        }

        CartaoPonto cartaoPonto = new CartaoPonto();
        cartaoPonto.setLatitude(gpsTracker.getLatitude());
        cartaoPonto.setLongitude(gpsTracker.getLongitude());
        cartaoPonto.setPrecisao(gpsTracker.getPrecisao());
        cartaoPonto.setHorario(new Date());

        return cartaoPonto;
    }

    public static void salvarStringSharedPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String recuperarStringSharedPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static String converterDataParaGMTZero(Date data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Config.FormatDateTimeStringBancoJson);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(data);
    }

    public static String converterDataBancoParaFormatoJson(Date data) {
        return new SimpleDateFormat(Config.FormatDateTimeStringBancoJson).format(data);
    }

    public static Bitmap decodeImagem(String pArray) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            byte[] decodedBytes = Base64.decode(pArray, 0);
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, o2);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int daybetween(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
    }

    public static Gson getGsonInstance() {
        return new GsonBuilder()
                .setDateFormat(Config.FormatDateTimeStringBancoJson)
                .setExclusionStrategies(new JsonExcludeStrategy())
                .setLenient()
                .create();
    }

    public static void atualizarQuantidadeCombo(Context context, int quantidade, String idVendaItem) {
        DBVenda dbVenda = new DBVenda(context);
        dbVenda.atualizarQuantidadeCombo(quantidade, idVendaItem);
    }

    public static void addICCIDItensVenda(Context pContext, Produto pProduto, int pQuantidade, double pValor, Venda pVenda
            , PrecoDiferenciado pPrecoDiferenciado, ArrayList<CodBarra> pItensCod, ArrayList<ComboVenda> pItensCombo, long idItemVenda,
                                          boolean atualizaQuantidade) throws Exception {
        if (!atualizaQuantidade) {
            pProduto.setQtde(pQuantidade);
        }

        addICCIDItemVenda(pContext, pPrecoDiferenciado, pVenda, pProduto, pItensCod, pValor, true, null, idItemVenda, atualizaQuantidade);
    }

    public static void addICCIDItemVenda(Context pContext, PrecoDiferenciado pPrecoDiferenciado, Venda pVenda, Produto pProduto, ArrayList<CodBarra> pItensCod
            , double pValor, boolean pAtualizaEstoque, ArrayList<ComboVenda> pItensCombo, long idItemVenda, boolean atualizaQuantidade) throws Exception {
        DBEstoque dbEstoque = new DBEstoque(pContext);
        DBVenda dbVenda = new DBVenda(pContext);

        if (pPrecoDiferenciado != null && pPrecoDiferenciado.getQtdPreco() > 0 && dbVenda.retornaQtdPrecoDiferenciado(String.valueOf(pPrecoDiferenciado.getId())) >= pPrecoDiferenciado.getQtdPreco()) {
            pPrecoDiferenciado = null;
            pValor = dbEstoque.getProdutoById(pProduto.getId()).getPrecovenda();
        }

        if (pPrecoDiferenciado != null) {
            DBPreco dbPreco = new DBPreco(pContext);
            long idVendaItem = dbVenda.addItemVenda(pVenda, pProduto, pItensCod, pValor, pItensCombo, String.valueOf(pPrecoDiferenciado.getId()), idItemVenda, atualizaQuantidade);
            dbPreco.atualizaIdVenda(String.valueOf(pPrecoDiferenciado.getId()), String.valueOf(pVenda.getId()), String.valueOf(idVendaItem), pProduto.getQtde());
        } else {
            dbVenda.addItemVenda(pVenda, pProduto, pItensCod, pValor, pItensCombo, null, idItemVenda, atualizaQuantidade);
        }

        ItemVenda produtoCombo = dbVenda.getItemVendaById((int) idItemVenda);
        if (produtoCombo.isCombo()) {
            dbEstoque.atualizaEstoque(pProduto.getId(), true, pProduto.getQtde());
        }
    }

    public static boolean saveImageResized(Context context, File tempFile) {
        try {

            Bitmap bMap = new Compressor(context).compressToBitmap(tempFile);
            Bitmap out = getResizedBitmap(bMap, 1024);

            FileOutputStream fOut = new FileOutputStream(tempFile);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            bMap.recycle();
            out.recycle();

            return true;

        } catch (Exception e) {
            Timber.e(e);
            return false;
        }
    }

    private static Bitmap getResizedBitmap(Bitmap image, int imageSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = imageSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = imageSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Bitmap decodeFile(String pPath) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pPath, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(pPath, o2);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void copyText(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("documento", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copiado para área de transfefência", Toast.LENGTH_LONG).show();
    }

    // Roni
    public static void addICCIDItensConsignado(Context pContext, Produto pProduto, int pQuantidade, double pValor, Consignado pConsignado
            , PrecoDiferenciado pPrecoDiferenciado, ArrayList<CodBarra> pItensCod, ArrayList<ComboVenda> pItensCombo, long idItemConsignado,
                                               boolean atualizaQuantidade) throws Exception {
        if (!atualizaQuantidade) {
            pProduto.setQtde(pQuantidade);
        }
        addICCIDItemConsignado(pContext, pPrecoDiferenciado, pConsignado, pProduto, pItensCod, pValor, true, null, idItemConsignado, atualizaQuantidade);
    }

    //Roni
    public static void addICCIDItemConsignado(Context pContext, PrecoDiferenciado pPrecoDiferenciado, Consignado pConsignado, Produto pProduto, ArrayList<CodBarra> pItensCod
            , double pValor, boolean pAtualizaEstoque, ArrayList<ComboVenda> pItensCombo, long idItemConsignado, boolean atualizaQuantidade) throws Exception {

        DBEstoque dbEstoque = new DBEstoque(pContext);
        BDConsignadoItem bdConsignadoItem = new BDConsignadoItem(pContext);
        BDConsignadoItemCodBarra bdConsignadoItemCodBarra = new BDConsignadoItemCodBarra(pContext);
        pValor = dbEstoque.getProdutoById(pProduto.getId()).getPrecovenda();

        ConsignadoItem consignadoItem = new ConsignadoItem();
        consignadoItem.setIdConsignado(pConsignado.getId());
        consignadoItem.setIdProduto(pProduto.getId());
        consignadoItem.setNomeProduto(pProduto.getNome());
        consignadoItem.setValorUnit(pValor);
        consignadoItem.setValorTotalItem(pProduto.getQtde() * pValor);
        consignadoItem.setQtd(pProduto.getQtde());
        if (idItemConsignado != 0) {
            consignadoItem.setId((int) idItemConsignado);
            //bdConsignadoItem.addConsignadoItem(consignadoItem);

            //Apagar Codigos de Barras
            bdConsignadoItemCodBarra.deleteByIdConsignadoItem(String.valueOf(idItemConsignado));
        } else
            consignadoItem.setId((int) bdConsignadoItem.addConsignadoItem(consignadoItem));

        // Gravar os Codigos de Barras
        for (int aa = 0; aa < pItensCod.size(); aa++) {
            ConsignadoItemCodBarra consignadoItemCodBarra = new ConsignadoItemCodBarra();
            consignadoItemCodBarra.setIdConsignado(consignadoItem.getIdConsignado());
            consignadoItemCodBarra.setIdConsignadoItem(consignadoItem.getId());
            consignadoItemCodBarra.setCodigoBarraIni(pItensCod.get(aa).getCodBarraInicial());
            consignadoItemCodBarra.setCodigoBarraFim(pItensCod.get(aa).getCodBarraFinal());
            consignadoItemCodBarra.setQtd(Integer.parseInt(pItensCod.get(aa).retornaQuantidade(UsoCodBarra.GERAL)));
            // Grava Codigos de Barras
            bdConsignadoItemCodBarra.addConsignadoItemCodBarra(consignadoItemCodBarra);
        }
    }

    public static List<CodBarra> getCodBarraItensByVendidos(Context pContext) {
        ArrayList<CodBarra> listBarra = new ArrayList<>();
        Util_IO.StringBuilder selectQuery = new Util_IO.StringBuilder();
        selectQuery.appendLine("SELECT codigoBarra, IFNULL(codigoBarraFinal,'-1') as codigoBarraFinal,");
        selectQuery.appendLine("IFNULL(grupo, '0') as grupo");
        selectQuery.appendLine("FROM [ItemVendaCodigoBarra]");
        selectQuery.appendLine("JOIN Produto ON Produto.id = ItemVendaCodigoBarra.idProduto");
        selectQuery.appendLine("WHERE ItemVendaCodigoBarra.cancelado = 0");
        selectQuery.appendLine("union all");
        selectQuery.appendLine("SELECT t0.codigoBarraini as codigoBarra, IFNULL(t0.codigoBarraFim,'-1') as codigoBarraFinal,");
        selectQuery.appendLine("IFNULL(t2.grupo, '0') as grupo");
        selectQuery.appendLine("FROM [ConsignadoItemCodBarra] t0");
        selectQuery.appendLine("LEFT JOIN ConsignadoItem t1 on t1.id = t0.IdConsignadoItem");
        selectQuery.appendLine("JOIN Produto t2 ON t2.id = t1.idProduto");
        selectQuery.appendLine("WHERE t0.cancelado = 0");
        try (Cursor cursorCod = SimpleDbHelper.INSTANCE.open(pContext).rawQuery(selectQuery.toString(), new String[]{})) {
            if (cursorCod != null && cursorCod.getCount() > 0) {
                if (cursorCod.moveToFirst()) {
                    do {
                        CodBarra codBarra = new CodBarra();
                        codBarra.setCodBarraInicial(cursorCod.getString(0));
                        codBarra.setGrupoProduto(cursorCod.getInt(2));
                        if (cursorCod.getString(1).equals("-1") || Util_IO.isNullOrEmpty(cursorCod.getString(1)))
                            codBarra.setIndividual(true);
                        else {
                            codBarra.setIndividual(false);
                            codBarra.setCodBarraFinal(cursorCod.getString(1));
                        }
                        listBarra.add(codBarra);
                    } while (cursorCod.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listBarra;
    }

    public static void encerrarAtendimentoConsignacao(final Context pContext,
                                                      final Consignado pConsignado,
                                                      final Visita pVisita,
                                                      final int pIdMotivo,
                                                      final CountDownTimer pTimer) throws Exception {

        final ProgressDialog mDialog = new ProgressDialog(pContext);
        mDialog.setMessage("Aguarde...");
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();

        new Thread(() -> ((Activity) pContext).runOnUiThread(() -> {
            try {
                finalizaVisitaConsignacao(pContext, pConsignado, pVisita, pIdMotivo);
            } catch (Exception ex) {
                Mensagens.mensagemErro(pContext, ex.getMessage(), false);
                if (mDialog.isShowing()) mDialog.dismiss();
                return;
            }

            if (pTimer != null) pTimer.cancel();
            mDialog.dismiss();

            Toast.makeText(pContext, "Consignação realizada com Sucesso.", Toast.LENGTH_LONG).show();
            new VendaSyncTask(pContext).execute();
        })).start();
    }

    public static void finalizaVisitaConsignacao(Context pContext, Consignado pConsignado, Visita pVisita, int pIdMotivo) throws Exception {
        BDConsignado bdConsignado = new BDConsignado(pContext);

        if (pConsignado != null) {
            // Mudar o Status da Consignacao Anterior
            double pValor = bdConsignado.getByValorTotalItens(String.valueOf(pConsignado.getId()));

            if (pConsignado.getIdConsignadoRefer() > 0)
                bdConsignado.atualizaStatusConsignadoIdServer(String.valueOf(pConsignado.getIdConsignadoRefer()), "3");

            // Atualiza Nova consignação
            bdConsignado.atualizaStatusConsignado(String.valueOf(pConsignado.getId()), pValor, 0);
            if (pConsignado.getItens() == null || pConsignado.getItens().size() <= 0) {
                pConsignado.setItens(new BDConsignadoItem(pContext).getByIdCConsignado(String.valueOf(pConsignado.getId())));
            }

            for (ConsignadoItem item : pConsignado.getItens()) {
                new DBEstoque(pContext).atualizaEstoque(item.getIdProduto(), true, item.getQtd());
            }

            // Exclui os Codigos de Barras da tabela Iccid - OK
            new DBIccid(pContext).deletaIccidConsignado(pConsignado.getId());
        }

        // Encerra Visita
        new DBVisita(pContext).encerrarVisita(pVisita.getId(), pIdMotivo);

        // Filtro Utilizado [finalizado]=1 AND date([data]) <= date('now','localtime')
        new DBEstoque(pContext).deletarPistolagensFinalizadas();

        // Ver o que faz esta função
        new DBAtualizarCliente(pContext).atualizarStatusConcluido(String.valueOf(pConsignado.getIdCliente()));
    }

    public static void obterPermissoesStorage(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
