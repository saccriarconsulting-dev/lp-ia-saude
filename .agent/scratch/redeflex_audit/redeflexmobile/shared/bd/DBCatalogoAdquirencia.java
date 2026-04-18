package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCatalog;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Rogério Massa on 28/01/19.
 */

public class DBCatalogoAdquirencia {

    static final String SQL = DBCatalogoAdquirencia.Tabela.criarTabela();
    private Context context;

    public DBCatalogoAdquirencia(Context context) {
        this.context = context;
    }

    public void salvar(VisitProspectCatalog reason) {
        if (pegarPorId(reason.getId()) != null) {
            atualizar(reason);
            return;
        }

        ContentValues contentValues = DBCatalogoAdquirencia.Tabela.converter(reason);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        DBCatalogoAdquirencia.Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(VisitProspectCatalog reason) {
        ContentValues contentValues = DBCatalogoAdquirencia.Tabela.converter(reason);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        DBCatalogoAdquirencia.Tabela.NOME,
                        contentValues,
                        DBCatalogoAdquirencia.Tabela.filtroPadrao(),
                        new String[]{String.valueOf(reason.getId())}
                );
    }

    public VisitProspectCatalog pegarPorId(int id) {
        VisitProspectCatalog reason = null;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            DBCatalogoAdquirencia.Tabela.NOME,
                            DBCatalogoAdquirencia.Tabela.COLUNAS,
                            DBCatalogoAdquirencia.Tabela.filtroPadrao(),
                            new String[]{String.valueOf(id)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                reason = DBCatalogoAdquirencia.Tabela.converter(cursor);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return reason;
    }

    public List<VisitProspectCatalog> pegarTodos() {
        List<VisitProspectCatalog> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            DBCatalogoAdquirencia.Tabela.NOME,
                            DBCatalogoAdquirencia.Tabela.COLUNAS,
                            String.format("[%s] = ?", DBCatalogoAdquirencia.Tabela.COLUNA_ATIVO),
                            new String[]{"1"},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                lista.add(DBCatalogoAdquirencia.Tabela.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBCatalogoAdquirencia.Tabela.NOME, null, null);
    }

    private static class Tabela {

        static final String NOME = "VisitaAdquirenciaCatalogo";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_IMAGEM = "imagem";
        static final String COLUNA_DATA = "data";
        static final String COLUNA_ATIVO = "ativo";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_IMAGEM,
                COLUNA_DATA,
                COLUNA_ATIVO
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER," +
                            "[%s] TEXT," +
                            "[%s] DATETIME," +
                            "[%s] INTEGER" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_IMAGEM,
                    COLUNA_DATA,
                    COLUNA_ATIVO
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(VisitProspectCatalog reason) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, reason.getId());
            values.put(COLUNA_IMAGEM, reason.getImage());
            values.put(COLUNA_DATA, Util_IO.dateTimeToString(reason.getDate(), Config.FormatDateTimeStringBanco));
            values.put(COLUNA_ATIVO, Util_IO.booleanToNumber(reason.getActivate()));
            return values;
        }

        private static VisitProspectCatalog converter(Cursor cursor) {
            return new VisitProspectCatalog(
                    getInt(cursor, COLUNA_ID_APP),
                    getString(cursor, COLUNA_IMAGEM),
                    Util_IO.stringToDate(getString(cursor, COLUNA_DATA), Config.FormatDateTimeStringBanco),
                    Util_IO.numberToBoolean(getInt(cursor, COLUNA_ATIVO))
            );
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }
    }
}
