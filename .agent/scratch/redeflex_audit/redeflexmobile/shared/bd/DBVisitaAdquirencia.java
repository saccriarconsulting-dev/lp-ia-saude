package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQuality;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Rogério Massa on 22/01/19.
 */

public class DBVisitaAdquirencia {

    static final String SQL_VISITA = TabelaVisitaAdquirencia.criarTabela();
    static final String SQL_VISITA_ANEXO = TabelaVisitaAdquirenciaAnexo.criarTabela();
    static final String SQL_VISITA_QUALIDADE = TabelaVisitaAdquirenciaQualidade.criarTabela();
    private Context context;

    public DBVisitaAdquirencia(Context context) {
        this.context = context;
    }

    //region VISITA
    public long salvarVisita(VisitProspect visitProspect) {
        ContentValues contentValues = TabelaVisitaAdquirencia.converter(visitProspect);
        return SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        TabelaVisitaAdquirencia.NOME,
                        null,
                        contentValues
                );
    }

    public List<VisitProspect> pegarTodasVisitasSync() {
        List<VisitProspect> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            TabelaVisitaAdquirencia.NOME,
                            TabelaVisitaAdquirencia.COLUNAS,
                            String.format("[%s] isnull", TabelaVisitaAdquirencia.COLUNA_ID_SERVER),
                            null,
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                lista.add(TabelaVisitaAdquirencia.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }

    public void atualizarVisitaSincronizada(int id, int idServer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TabelaVisitaAdquirencia.COLUNA_ID_SERVER, idServer);

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        TabelaVisitaAdquirencia.NOME,
                        contentValues,
                        TabelaVisitaAdquirencia.filtroPadrao(),
                        new String[]{String.valueOf(id)}
                );
    }

    private void deletarVisitas() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBVisitaAdquirencia.TabelaVisitaAdquirencia.NOME, null, null);
    }
    //endregion

    //region VISITA ANEXO
    public long salvarVisitaAnexo(VisitProspectAttachment visitProspectAttachment) {
        ContentValues contentValues = TabelaVisitaAdquirenciaAnexo.converter(visitProspectAttachment);
        return SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        TabelaVisitaAdquirenciaAnexo.NOME,
                        null,
                        contentValues
                );
    }

    public List<VisitProspectAttachment> pegarTodasVisitasAnexoSync() {
        List<VisitProspectAttachment> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            TabelaVisitaAdquirenciaAnexo.NOME,
                            TabelaVisitaAdquirenciaAnexo.COLUNAS,
                            String.format("[%s] = ?", TabelaVisitaAdquirenciaAnexo.COLUNA_SYNC),
                            new String[]{"0"},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                lista.add(TabelaVisitaAdquirenciaAnexo.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }

    public void atualizarVisitaAnexoSincronizada(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TabelaVisitaAdquirenciaAnexo.COLUNA_SYNC, 1);

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        TabelaVisitaAdquirenciaAnexo.NOME,
                        contentValues,
                        TabelaVisitaAdquirenciaAnexo.filtroPadrao(),
                        new String[]{String.valueOf(id)}
                );
    }

    private void deletarVisitasAnexo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBVisitaAdquirencia.TabelaVisitaAdquirenciaAnexo.NOME, null, null);
    }
    //endregion

    //region VISITA QUALIDADE
    public void salvarVisitaQualidade(VisitProspectQuality visitProspectQuality) {
        ContentValues contentValues = TabelaVisitaAdquirenciaQualidade.converter(visitProspectQuality);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        TabelaVisitaAdquirenciaQualidade.NOME,
                        null,
                        contentValues
                );
    }

    public List<VisitProspectQuality> pegarTodasVisitasQualidadeSync() {
        List<VisitProspectQuality> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            TabelaVisitaAdquirenciaQualidade.NOME,
                            TabelaVisitaAdquirenciaQualidade.COLUNAS,
                            String.format("[%s] = ?", TabelaVisitaAdquirenciaQualidade.COLUNA_SYNC),
                            new String[]{String.valueOf(0)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                lista.add(TabelaVisitaAdquirenciaQualidade.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }

    public void atualizarVisitaQualidadeSincronizada(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TabelaVisitaAdquirenciaQualidade.COLUNA_SYNC, 1);

        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        TabelaVisitaAdquirenciaQualidade.NOME,
                        contentValues,
                        TabelaVisitaAdquirenciaQualidade.filtroPadrao(),
                        new String[]{String.valueOf(id)}
                );
    }

    private void deletarVisitasQualidade() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBVisitaAdquirencia.TabelaVisitaAdquirenciaQualidade.NOME, null, null);
    }
    //endregion

    //region TABELAS
    private static class TabelaVisitaAdquirencia {

        static final String NOME = "VisitaAdquirencia";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_ID_SERVER = "id_server";
        static final String COLUNA_ID_VENDEDOR = "id_vendedor";
        static final String COLUNA_DATA_INICIO = "data_inicio";
        static final String COLUNA_DATA_FIM = "data_fim";
        static final String COLUNA_CANCELAMENTO_ID_MOTIVO = "id_motivo";
        static final String COLUNA_ID_CLIENTE_SGV = "id_cliente_sgv";
        static final String COLUNA_ID_CLIENTE = "id_cliente";
        static final String COLUNA_ID_PROSPECT = "id_prospect";
        static final String COLUNA_LATITUDE = "latitude";
        static final String COLUNA_LONGITUDE = "longitude";
        static final String COLUNA_PRECISAO = "precisao";
        static final String COLUNA_VERSAO_APP = "versao_app";
        static final String COLUNA_DISTANCIA = "distancia";
        static final String COLUNA_OBSERVACAO = "observacao";
        static final String COLUNA_CANCELAMENTO_OBSERVACAO = "observacao_cancelamento";
        static final String COLUNA_ID_ROTA = "id_rota";
        static final String COLUNA_ID_ROTA_AGENDADA = "id_rota_agendada";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_ID_SERVER,
                COLUNA_ID_VENDEDOR,
                COLUNA_DATA_INICIO,
                COLUNA_DATA_FIM,
                COLUNA_CANCELAMENTO_ID_MOTIVO,
                COLUNA_CANCELAMENTO_OBSERVACAO,
                COLUNA_ID_CLIENTE_SGV,
                COLUNA_ID_CLIENTE,
                COLUNA_ID_PROSPECT,
                COLUNA_LATITUDE,
                COLUNA_LONGITUDE,
                COLUNA_PRECISAO,
                COLUNA_VERSAO_APP,
                COLUNA_DISTANCIA,
                COLUNA_OBSERVACAO,
                COLUNA_ID_ROTA,
                COLUNA_ID_ROTA_AGENDADA
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] DATETIME," +
                            "[%s] DATETIME," +
                            "[%s] INTEGER," +
                            "[%s] VARCHAR(20)," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] VARCHAR(50)," +
                            "[%s] INTEGER," +
                            "[%s] VARCHAR(500)," +
                            "[%s] VARCHAR(500)," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_ID_SERVER,
                    COLUNA_ID_VENDEDOR,
                    COLUNA_DATA_INICIO,
                    COLUNA_DATA_FIM,
                    COLUNA_CANCELAMENTO_ID_MOTIVO,
                    COLUNA_ID_CLIENTE_SGV,
                    COLUNA_ID_CLIENTE,
                    COLUNA_ID_PROSPECT,
                    COLUNA_LATITUDE,
                    COLUNA_LONGITUDE,
                    COLUNA_PRECISAO,
                    COLUNA_VERSAO_APP,
                    COLUNA_DISTANCIA,
                    COLUNA_OBSERVACAO,
                    COLUNA_CANCELAMENTO_OBSERVACAO,
                    COLUNA_ID_ROTA,
                    COLUNA_ID_ROTA_AGENDADA
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(VisitProspect visitProspect) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, visitProspect.getId());
            values.put(COLUNA_ID_SERVER, visitProspect.getIdServer());
            values.put(COLUNA_ID_VENDEDOR, visitProspect.getIdSalesman());
            values.put(COLUNA_DATA_INICIO, Util_IO.dateTimeToString(visitProspect.getDateStart(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_DATA_FIM, Util_IO.dateTimeToString(visitProspect.getDateFinish(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_CANCELAMENTO_ID_MOTIVO, visitProspect.getCancelIdReason());
            values.put(COLUNA_ID_CLIENTE_SGV, visitProspect.getIdCustomerSgv());
            values.put(COLUNA_ID_CLIENTE, visitProspect.getIdCustomer());
            values.put(COLUNA_ID_PROSPECT, visitProspect.getIdProspect());
            values.put(COLUNA_LATITUDE, visitProspect.getLatitude());
            values.put(COLUNA_LONGITUDE, visitProspect.getLongitude());
            values.put(COLUNA_PRECISAO, visitProspect.getPrecision());
            values.put(COLUNA_VERSAO_APP, visitProspect.getVersionApp());
            values.put(COLUNA_DISTANCIA, visitProspect.getDistance());
            values.put(COLUNA_OBSERVACAO, visitProspect.getObservation());
            values.put(COLUNA_CANCELAMENTO_OBSERVACAO, visitProspect.getCancelObservation());
            values.put(COLUNA_ID_ROTA, visitProspect.getIdRoute());
            values.put(COLUNA_ID_ROTA_AGENDADA, visitProspect.getIdRouteScheduled());
            return values;
        }

        private static VisitProspect converter(Cursor cursor) {
            return new VisitProspect(
                    getInt(cursor, COLUNA_ID_APP),
                    getInt(cursor, COLUNA_ID_SERVER),
                    getInt(cursor, COLUNA_ID_VENDEDOR),
                    Util_IO.stringToDate(getString(cursor, COLUNA_DATA_INICIO), Config.FormatDateTimeStringBanco),
                    Util_IO.stringToDate(getString(cursor, COLUNA_DATA_FIM), Config.FormatDateTimeStringBanco),
                    getInt(cursor, COLUNA_CANCELAMENTO_ID_MOTIVO),
                    getString(cursor, COLUNA_ID_CLIENTE_SGV),
                    getInt(cursor, COLUNA_ID_CLIENTE),
                    getInt(cursor, COLUNA_ID_PROSPECT),
                    getInt(cursor, COLUNA_LATITUDE),
                    getInt(cursor, COLUNA_LONGITUDE),
                    getInt(cursor, COLUNA_PRECISAO),
                    getString(cursor, COLUNA_VERSAO_APP),
                    getInt(cursor, COLUNA_DISTANCIA),
                    getString(cursor, COLUNA_OBSERVACAO),
                    getString(cursor, COLUNA_CANCELAMENTO_OBSERVACAO),
                    getInt(cursor, COLUNA_ID_ROTA),
                    getInt(cursor, COLUNA_ID_ROTA_AGENDADA)
            );
        }
    }

    private static class TabelaVisitaAdquirenciaAnexo {

        static final String NOME = "VisitaAdqurenciaAnexo";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_ID_VENDEDOR = "idVendedor";
        static final String COLUNA_ID_VISITA = "idVisita";
        static final String COLUNA_ID_CLIENTE = "idCliente";
        static final String COLUNA_ID_PROSPECT = "idProspect";
        static final String COLUNA_FOTO = "foto";
        static final String COLUNA_DATA = "data";
        static final String COLUNA_LATITUDE = "latitude";
        static final String COLUNA_LONGITUDE = "longitude";
        static final String COLUNA_PRECISAO = "precisao";
        static final String COLUNA_SYNC = "sync";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_ID_VENDEDOR,
                COLUNA_ID_VISITA,
                COLUNA_ID_CLIENTE,
                COLUNA_ID_PROSPECT,
                COLUNA_FOTO,
                COLUNA_DATA,
                COLUNA_LATITUDE,
                COLUNA_LONGITUDE,
                COLUNA_PRECISAO,
                COLUNA_SYNC
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] TEXT," +
                            "[%s] DATETIME," +
                            "[%s] DECIMAL(20, 16)," +
                            "[%s] DECIMAL(20, 16)," +
                            "[%s] DECIMAL(20, 16)," +
                            "[%s] INTEGER DEFAULT 0" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_ID_VENDEDOR,
                    COLUNA_ID_VISITA,
                    COLUNA_ID_CLIENTE,
                    COLUNA_ID_PROSPECT,
                    COLUNA_FOTO,
                    COLUNA_DATA,
                    COLUNA_LATITUDE,
                    COLUNA_LONGITUDE,
                    COLUNA_PRECISAO,
                    COLUNA_SYNC
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(VisitProspectAttachment visitProspect) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, visitProspect.getId());
            values.put(COLUNA_ID_VENDEDOR, visitProspect.getIdSalesman());
            values.put(COLUNA_ID_VISITA, visitProspect.getIdVisit());
            values.put(COLUNA_ID_CLIENTE, visitProspect.getIdCustomer());
            values.put(COLUNA_ID_PROSPECT, visitProspect.getIdProspect());
            values.put(COLUNA_FOTO, visitProspect.getImage());
            values.put(COLUNA_DATA, Util_IO.dateTimeToString(visitProspect.getDate(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_LATITUDE, visitProspect.getLatitude());
            values.put(COLUNA_LONGITUDE, visitProspect.getLongitude());
            values.put(COLUNA_PRECISAO, visitProspect.getPrecision());
            return values;
        }

        private static VisitProspectAttachment converter(Cursor cursor) {
            return new VisitProspectAttachment(
                    getInt(cursor, COLUNA_ID_APP),
                    getInt(cursor, COLUNA_ID_VENDEDOR),
                    getLong(cursor, COLUNA_ID_VISITA),
                    getInt(cursor, COLUNA_ID_CLIENTE),
                    getInt(cursor, COLUNA_ID_PROSPECT),
                    getString(cursor, COLUNA_FOTO),
                    Util_IO.stringToDate(getString(cursor, COLUNA_DATA), Config.FormatDateTimeStringBanco),
                    getDouble(cursor, COLUNA_LATITUDE),
                    getDouble(cursor, COLUNA_LONGITUDE),
                    getDouble(cursor, COLUNA_PRECISAO)
            );
        }
    }

    private static class TabelaVisitaAdquirenciaQualidade {

        static final String NOME = "VisitaAdquirenciaQualidade";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_ID_VISITA = "id_visita";
        static final String COLUNA_VALIDADOS = "validados";
        static final String COLUNA_NAO_VALIDADOS = "nao_validados";
        static final String COLUNA_SYNC = "sync";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_ID_VISITA,
                COLUNA_VALIDADOS,
                COLUNA_NAO_VALIDADOS,
                COLUNA_SYNC
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "[%s] INTEGER," +
                            "[%s] TEXT," +
                            "[%s] TEXT," +
                            "[%s] INTEGER DEFAULT 0" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_ID_VISITA,
                    COLUNA_VALIDADOS,
                    COLUNA_NAO_VALIDADOS,
                    COLUNA_SYNC
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(VisitProspectQuality visitProspect) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, visitProspect.getId());
            values.put(COLUNA_ID_VISITA, visitProspect.getIdVisit());
            values.put(COLUNA_VALIDADOS, visitProspect.getAnswers());
            values.put(COLUNA_NAO_VALIDADOS, visitProspect.getQuestions());
            return values;
        }

        private static VisitProspectQuality converter(Cursor cursor) {
            return new VisitProspectQuality(
                    getInt(cursor, COLUNA_ID_APP),
                    getLong(cursor, COLUNA_ID_VISITA),
                    getString(cursor, COLUNA_VALIDADOS),
                    getString(cursor, COLUNA_NAO_VALIDADOS));
        }
    }
    //endregion

    public void deletaTudo() {
        deletarVisitas();
        deletarVisitasAnexo();
        deletarVisitasQualidade();
    }

    private static long getLong(Cursor cursor, String coluna) {
        return cursor.getLong(cursor.getColumnIndex(coluna));
    }

    private static double getDouble(Cursor cursor, String coluna) {
        return cursor.getDouble(cursor.getColumnIndex(coluna));
    }

    private static int getInt(Cursor cursor, String coluna) {
        return cursor.getInt(cursor.getColumnIndex(coluna));
    }

    private static String getString(Cursor cursor, String coluna) {
        return cursor.getString(cursor.getColumnIndex(coluna));
    }
}
