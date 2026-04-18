package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.CartaoPonto;
import com.axys.redeflexmobile.shared.models.CartaoPontoRequest;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBCartaoPonto {

    public static final String SQL = Tabela.criarTabela();
    private Context context;

    public DBCartaoPonto(Context context) {
        this.context = context;
    }

    private static double getDouble(Cursor cursor, String column) {
        return cursor.getDouble(cursor.getColumnIndex(column));
    }

    private static String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    private static int getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    public List<CartaoPonto> obterRegistroHorarioPush(LocalDateTime horaInicio, LocalDateTime horaFim) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + Tabela.ID);
        sb.appendLine(", " + Tabela.HORARIO);
        sb.appendLine(", " + Tabela.LATITUDE);
        sb.appendLine(", " + Tabela.LONGITUDE);
        sb.appendLine(", " + Tabela.PRECISAO);
        sb.appendLine(", " + Tabela.SYNC);
        sb.appendLine("FROM " + Tabela.TABELA + " ");
        sb.appendLine("WHERE " + Tabela.HORARIO + " BETWEEN datetime('" + horaInicio + "') " +
                "AND datetime('" + horaFim + "')");

        List<CartaoPonto> cartoesPonto = new ArrayList<>();

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null)) {
            CartaoPonto cartaoPonto;
            while (cursor.moveToNext()) {
                cartaoPonto = new CartaoPonto();
                cartaoPonto.setId(cursor.getInt(cursor.getColumnIndex(Tabela.ID)));
                cartaoPonto.setHorario(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndex(Tabela.HORARIO)), Config.FormatDateTimeStringBanco));
                cartaoPonto.setLatitude(cursor.getDouble(cursor.getColumnIndex(Tabela.LATITUDE)));
                cartaoPonto.setLongitude(cursor.getDouble(cursor.getColumnIndex(Tabela.LONGITUDE)));
                cartaoPonto.setPrecisao(cursor.getDouble(cursor.getColumnIndex(Tabela.PRECISAO)));
                cartaoPonto.setSync(cursor.getInt(cursor.getColumnIndex(Tabela.SYNC)) == 1);
                cartoesPonto.add(cartaoPonto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cartoesPonto;
    }

    public List<CartaoPonto> obterRegistroDia() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + Tabela.ID);
        sb.appendLine(", " + Tabela.HORARIO);
        sb.appendLine(", " + Tabela.LATITUDE);
        sb.appendLine(", " + Tabela.LONGITUDE);
        sb.appendLine(", " + Tabela.PRECISAO);
        sb.appendLine(", " + Tabela.SYNC);
        sb.appendLine("FROM " + Tabela.TABELA + " ");
        sb.appendLine("WHERE date(" + Tabela.HORARIO + ") = date('now', 'localtime')");

        Cursor cursor = null;
        List<CartaoPonto> lista = new ArrayList<>();

        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null);
            while (cursor.moveToNext()) {
                CartaoPonto cartaoPonto = new CartaoPonto();
                cartaoPonto.setId(cursor.getInt(cursor.getColumnIndex(Tabela.ID)));
                cartaoPonto.setHorario(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndex(Tabela.HORARIO)), Config.FormatDateTimeStringBanco));
                cartaoPonto.setLatitude(cursor.getDouble(cursor.getColumnIndex(Tabela.LATITUDE)));
                cartaoPonto.setLongitude(cursor.getDouble(cursor.getColumnIndex(Tabela.LONGITUDE)));
                cartaoPonto.setPrecisao(cursor.getDouble(cursor.getColumnIndex(Tabela.PRECISAO)));
                cartaoPonto.setSync(cursor.getInt(cursor.getColumnIndex(Tabela.SYNC)) == 1);
                lista.add(cartaoPonto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return lista;
    }

    public List<CartaoPontoRequest> obterTodosRegistros() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + Tabela.ID);
        sb.appendLine(", " + Tabela.HORARIO);
        sb.appendLine(", " + Tabela.LATITUDE);
        sb.appendLine(", " + Tabela.LONGITUDE);
        sb.appendLine(", " + Tabela.PRECISAO);
        sb.appendLine(" FROM " + Tabela.TABELA);
        sb.appendLine(" WHERE " + Tabela.SYNC + " = 0");

        Cursor cursor = null;
        List<CartaoPontoRequest> lista = new ArrayList<>();

        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null);
            CartaoPontoRequest cartaoPontoRequest;
            if (cursor.moveToFirst()) {
                do {
                    cartaoPontoRequest = new CartaoPontoRequest();
                    cartaoPontoRequest.setIdMobile(getInt(cursor, Tabela.ID));
                    cartaoPontoRequest.setData(
                            Utilidades.converterDataBancoParaFormatoJson(
                                    Util_IO.stringToDate(
                                            getString(cursor, Tabela.HORARIO),
                                            Config.FormatDateTimeStringBanco)
                            )
                    );
                    cartaoPontoRequest.setLatitude(getDouble(cursor, Tabela.LATITUDE));
                    cartaoPontoRequest.setLongitude(getDouble(cursor, Tabela.LONGITUDE));
                    cartaoPontoRequest.setPrecisao(getDouble(cursor, Tabela.PRECISAO));
                    cartaoPontoRequest.setIdVendedor(new DBColaborador(context).get().getId());
                    lista.add(cartaoPontoRequest);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return lista;
    }

    public void registrarPonto(CartaoPonto cartaoPonto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tabela.HORARIO, Util_IO.dateTimeToString(cartaoPonto.getHorario(), Config.FormatDateTimeStringBanco));
        contentValues.put(Tabela.LATITUDE, cartaoPonto.getLatitude());
        contentValues.put(Tabela.LONGITUDE, cartaoPonto.getLongitude());
        contentValues.put(Tabela.PRECISAO, cartaoPonto.getPrecisao());
        contentValues.put(Tabela.SYNC, 0);

        SimpleDbHelper.INSTANCE.open(context).insert(Tabela.TABELA, null, contentValues);
    }

    public void apagarRegistroComMais60Dias() {
        SimpleDbHelper.INSTANCE.open(context).delete(Tabela.TABELA,
                "date(" + Tabela.HORARIO + ", 'localtime', '+60 day') < date('now', 'localtime') " +
                        "AND " + Tabela.SYNC + " = 1",
                null);
    }

    public void atualizaSync(String pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(context).update(Tabela.TABELA, values, "[id]=?", new String[]{pId});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(Tabela.TABELA, null, null);
    }

    private static class Tabela {

        static final String TABELA = "CartaoPonto";
        static final String ID = "id";
        static final String HORARIO = "horario";
        static final String LATITUDE = "latitude";
        static final String LONGITUDE = "longitude";
        static final String PRECISAO = "precisao";
        static final String SYNC = "sync";

        static String criarTabela() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "[%s] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                            "[%s] DATE,\n" +
                            "[%s] DECIMAL(20, 16),\n" +
                            "[%s] DECIMAL(20, 16),\n" +
                            "[%s] DECIMAL(20, 16),\n" +
                            "[%s] INTEGER DEFAULT 0)",
                    TABELA,
                    ID,
                    HORARIO,
                    LATITUDE,
                    LONGITUDE,
                    PRECISAO,
                    SYNC
            );
        }
    }
}
