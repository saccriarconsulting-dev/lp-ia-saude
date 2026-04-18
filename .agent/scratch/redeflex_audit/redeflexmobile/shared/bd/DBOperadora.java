package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pimentel on 10/09/2019
 */
public class DBOperadora {

    public static final String UPDATE_70_SQL_CRIAR_TABELA = Tabela.SQL_CRIAR_TABELA;

    private final Context context;

    public DBOperadora(final Context context) {
        this.context = context;
    }

    public void insereOperadora(final Operadora operadora) {
        final ContentValues values = new ContentValues();
        values.put(Tabela.COLUNA_ID, operadora.getId());
        values.put(Tabela.COLUNA_ID_OPERADORA, operadora.getIdOperadora());
        values.put(Tabela.COLUNA_DESCRICAO, operadora.getDescricao());
        values.put(Tabela.COLUNA_IMAGEM, operadora.getImagem());

        if (existeOperadora(operadora.getIdOperadora())) {
            SimpleDbHelper.INSTANCE.open(context)
                    .update(Tabela.NOME,
                            values,
                            "id = ?",
                            new String[]{String.valueOf(operadora.getIdOperadora())});
            return;
        }

        SimpleDbHelper.INSTANCE.open(context)
                .insert(Tabela.NOME,
                        null,
                        values);
    }

    private boolean existeOperadora(final int id) {
        final String query = "SELECT * " +
                "FROM [" + Tabela.NOME + "] " +
                "WHERE id = ?";

        try (final Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .rawQuery(query, new String[]{String.valueOf(id)})) {
            return cursor.moveToNext();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public List<Operadora> buscaOperadoras() {
        final String query = "SELECT * " +
                "FROM [" + Tabela.NOME + "] ";

        try (final Cursor cursor = SimpleDbHelper.INSTANCE.open(context)
                .rawQuery(query, null)) {
            final List<Operadora> operadoras = new ArrayList<>();
            while (cursor.moveToNext()) {
                operadoras.add(new Operadora(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(2)
                ));
            }
            return operadoras;
        }
    }

    public Operadora getByIdOperadora(int pIdOperadora) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT * FROM OPERADORA WHERE idOperadora = " + pIdOperadora);
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaObjectToCursor(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private Operadora retornaObjectToCursor(Cursor pCursor) {
        try {
            Operadora item = new Operadora(pCursor.getInt(0), pCursor.getInt(1), pCursor.getString(2), pCursor.getString(3));
            return item;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(Tabela.NOME, null, null);
    }

    private static class Tabela {
        private static final String NOME = "Operadora";

        private static final String COLUNA_ID = "id";
        private static final String COLUNA_ID_OPERADORA = "idOperadora";
        private static final String COLUNA_DESCRICAO = "descricao";
        private static final String COLUNA_IMAGEM = "imagem";

        private static final String SQL_CRIAR_TABELA =
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "[%s] INTEGER, " +
                                "[%s] INTEGER, " +
                                "[%s] VARCHAR(100)," +
                                "[%s] VARCHAR(200))",
                        NOME,
                        COLUNA_ID,
                        COLUNA_ID_OPERADORA,
                        COLUNA_DESCRICAO,
                        COLUNA_IMAGEM
                );
    }
}
