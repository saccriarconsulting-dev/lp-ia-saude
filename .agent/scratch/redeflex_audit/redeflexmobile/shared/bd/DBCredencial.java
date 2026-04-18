package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axys.redeflexmobile.shared.models.login.Login;
import com.axys.redeflexmobile.shared.util.StringUtils;

public class DBCredencial {

    public static final String GERAR_TABELA = Tabela.gerarTabela();

    private final Context context;

    public DBCredencial(Context context) {
        this.context = context;
    }

    public void adicionar(Login login) {
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        ContentValues values = Tabela.converter(login);

        deleteAll();
        db.insert(Tabela.TABELA, null, values);
    }

    public Login obter() {
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        try (Cursor cursor = db.query(Tabela.TABELA, Tabela.COLUNAS, null, null, null, null, null)) {
            Login login = null;
            while (cursor.moveToNext()) {
                login = Tabela.converter(cursor);
            }

            return login;
        }
    }

    public void deleteAll() {
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        db.delete(Tabela.TABELA, null, null);
    }

    private static class Tabela {
        static final String TABELA = "Credencial";

        static final String USUARIO = "usuario";
        static final String SENHA = "senha";
        static final String UUID = "uuid";

        static final String[] COLUNAS = new String[]{USUARIO, SENHA, UUID};

        static String gerarTabela() {
            return "CREATE TABLE IF NOT EXISTS [" + TABELA + "] (" +
                    "[" + USUARIO + "] TEXT, " +
                    "[" + SENHA + "] TEXT, " +
                    "[" + UUID + "] TEXT " +
                    ");";
        }

        static ContentValues converter(Login login) {
            final ContentValues values = new ContentValues();
            if (StringUtils.isNotEmpty(login.getUsername())) {
                values.put(USUARIO, login.getUsername());
            }
            if (StringUtils.isNotEmpty(login.getPassword())) {
                values.put(SENHA, login.getPassword());
            }
            if (StringUtils.isNotEmpty(login.getUuid())) {
                values.put(UUID, login.getUuid());
            }

            return values;
        }

        static Login converter(Cursor cursor) {
            String username = getString(cursor, USUARIO);
            String pass = getString(cursor, SENHA);
            String uuid = getString(cursor, UUID);

            return new Login(uuid, username, pass);
        }

        private static String getString(Cursor cursor, String column) {
            return cursor.getString(cursor.getColumnIndex(column));
        }
    }
}
