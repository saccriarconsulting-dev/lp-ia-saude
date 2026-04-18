package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Rogério Massa on 24/01/19.
 */

public class DBPerguntasQualidade {

    static final String SQL = Tabela.criarTabela();
    private Context context;

    public DBPerguntasQualidade(Context context) {
        this.context = context;
    }

    public void salvar(VisitProspectQualityQuestion reason) {
        if (pegarPorId(reason.getId()) != null) {
            atualizar(reason);
            return;
        }

        ContentValues contentValues = Tabela.converter(reason);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(
                        Tabela.NOME,
                        null,
                        contentValues
                );
    }

    private void atualizar(VisitProspectQualityQuestion reason) {
        ContentValues contentValues = Tabela.converter(reason);
        SimpleDbHelper.INSTANCE.open(context)
                .update(
                        Tabela.NOME,
                        contentValues,
                        Tabela.filtroPadrao(),
                        new String[]{String.valueOf(reason.getId())}
                );
    }

    public VisitProspectQualityQuestion pegarPorId(int id) {
        VisitProspectQualityQuestion reason = null;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            Tabela.NOME,
                            Tabela.COLUNAS,
                            Tabela.filtroPadrao(),
                            new String[]{String.valueOf(id)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                reason = Tabela.converter(cursor);
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

    public List<VisitProspectQualityQuestion> pegarTodas() {
        List<VisitProspectQualityQuestion> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context)
                    .query(
                            Tabela.NOME,
                            Tabela.COLUNAS,
                            String.format("[%s] = ?", Tabela.COLUNA_ATIVO),
                            new String[]{String.valueOf(1)},
                            null,
                            null,
                            null
                    );

            while (cursor.moveToNext()) {
                lista.add(Tabela.converter(cursor));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public void deletaTudo() {
        SimpleDbHelper.INSTANCE.open(context)
                .delete(DBPerguntasQualidade.Tabela.NOME, null, null);
    }

    private static class Tabela {

        static final String NOME = "PerguntasQualidade";

        static final String COLUNA_ID_APP = "id";
        static final String COLUNA_TIPO = "tipo";
        static final String COLUNA_DESCRICAO = "descricao";
        static final String COLUNA_ATIVO = "ativo";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID_APP,
                COLUNA_TIPO,
                COLUNA_DESCRICAO,
                COLUNA_ATIVO
        };

        private static String criarTabela() {
            return String.format(
                    "CREATE TABLE [%s] (" +
                            "[%s] INTEGER PRIMARY KEY ," +
                            "[%s] INTEGER," +
                            "[%s] VARCHAR(50)," +
                            "[%s] VARCHAR(1)" +
                            ")",
                    NOME,
                    COLUNA_ID_APP,
                    COLUNA_TIPO,
                    COLUNA_DESCRICAO,
                    COLUNA_ATIVO
            );
        }

        private static String filtroPadrao() {
            return String.format("[%s] = ?", COLUNA_ID_APP);
        }

        private static ContentValues converter(VisitProspectQualityQuestion reason) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_ID_APP, reason.getId());
            values.put(COLUNA_TIPO, reason.getType());
            values.put(COLUNA_DESCRICAO, reason.getDescription());
            values.put(COLUNA_ATIVO, Util_IO.booleanToNumber(reason.isActivate()));
            return values;
        }

        private static VisitProspectQualityQuestion converter(Cursor cursor) {
            return new VisitProspectQualityQuestion(
                    getInt(cursor, COLUNA_ID_APP),
                    getInt(cursor, COLUNA_TIPO),
                    getString(cursor, COLUNA_DESCRICAO),
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
