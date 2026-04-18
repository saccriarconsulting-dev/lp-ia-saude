package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.ComprovanteSkyTa;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

public class DBComprovanteSkyTa {

    public static String SQL = Tabela.gerarSql();

    private Context context;

    public DBComprovanteSkyTa(Context context) {
        this.context = context;
    }

    public void salvar(ComprovanteSkyTa comprovanteSkyTa) {
        ContentValues values = Tabela.converter(comprovanteSkyTa);
        SimpleDbHelper.INSTANCE.open(context)
                .insert(Tabela.TABELA, null, values);
    }

    public List<ComprovanteSkyTa> obterTodosComprovantes() {
        List<ComprovanteSkyTa> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE
                .open(context)
                .query(
                        Tabela.TABELA,
                        Tabela.COLUNAS,
                        Tabela.FILTRO_PENDENTE,
                        new String[]{String.valueOf(ComprovanteSkyTa.NAO_SINCRONIZADO)},
                        null,
                        null,
                        null
                )) {
            while (cursor.moveToNext()) {
                ComprovanteSkyTa comprovanteSkyTa = Tabela.converter(cursor);
                lista.add(comprovanteSkyTa);
            }
        }

        return lista;
    }

    public void atualizarSync(ComprovanteSkyTa comprovanteSkyTa) {
        ContentValues values = Tabela.converter(comprovanteSkyTa);
        SimpleDbHelper.INSTANCE
                .open(context)
                .update(
                        Tabela.TABELA,
                        values,
                        Tabela.FILTRO_PADRAO,
                        new String[]{String.valueOf(comprovanteSkyTa.getId())}
                );
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE
                .open(context)
                .delete(Tabela.TABELA, null, null);
    }

    private static class Tabela {
        static final String TABELA = "ComprovanteSkyTa";
        static final String COLUNA_ID = "id";
        static final String COLUNA_COLABORADOR = "idColaborador";
        static final String COLUNA_CLIENTE = "idCliente";
        static final String COLUNA_TIPO = "tipo";
        static final String COLUNA_ANEXO = "anexo";
        static final String COLUNA_SYNC = "sync";
        static final String COLUNA_CAMPANHA = "idCampanha";
        static final String COLUNA_DATA = "data";
        static final String COLUNA_IDMATERIAL = "idMaterial";
        static final String COLUNA_INTERNO = "interno";
        static final String[] COLUNAS = new String[]{
                COLUNA_ID,
                COLUNA_COLABORADOR,
                COLUNA_CLIENTE,
                COLUNA_TIPO,
                COLUNA_ANEXO,
                COLUNA_SYNC,
                COLUNA_CAMPANHA,
                COLUNA_DATA,
                COLUNA_IDMATERIAL,
                COLUNA_INTERNO
        };
        static final String FILTRO_PADRAO = "id = ?";
        static final String FILTRO_PENDENTE = "sync = ?";

        static String gerarSql() {
            return String.format(
                    "CREATE TABLE IF NOT EXISTS [%s] (" +
                            "[%s] INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER," +
                            "[%s] TEXT," +
                            "[%s] TINYINT DEFAULT 0," +
                            "[%s] INTEGER," +
                            "[%s] INTEGER" +
                            ");",
                    TABELA,
                    COLUNA_ID,
                    COLUNA_COLABORADOR,
                    COLUNA_CLIENTE,
                    COLUNA_TIPO,
                    COLUNA_ANEXO,
                    COLUNA_SYNC,
                    COLUNA_IDMATERIAL,
                    COLUNA_INTERNO
            );
        }

        static ComprovanteSkyTa converter(Cursor cursor) {
            ComprovanteSkyTa comprovanteSkyTa = new ComprovanteSkyTa();
            comprovanteSkyTa.setId(getInt(cursor, COLUNA_ID));
            comprovanteSkyTa.setIdColaborador(getInt(cursor, COLUNA_COLABORADOR));
            comprovanteSkyTa.setIdCliente(getInt(cursor, COLUNA_CLIENTE));
            comprovanteSkyTa.setTipo(getInt(cursor, COLUNA_TIPO));
            comprovanteSkyTa.setAnexo(getString(cursor, COLUNA_ANEXO));
            comprovanteSkyTa.setSync(getInt(cursor, COLUNA_SYNC));
            comprovanteSkyTa.setIdCampanha(getInt(cursor, COLUNA_CAMPANHA));
            comprovanteSkyTa.setData(Util_IO.stringToDate(getString(cursor, COLUNA_DATA), "yyyy-MM-dd HH:mm:ss"));
            comprovanteSkyTa.setIdMaterial(getInt(cursor, COLUNA_IDMATERIAL));
            comprovanteSkyTa.setInterno(getInt(cursor, COLUNA_INTERNO));
            return comprovanteSkyTa;
        }

        static ContentValues converter(ComprovanteSkyTa comprovanteSkyTa) {
            ContentValues values = new ContentValues();
            values.put(COLUNA_COLABORADOR, comprovanteSkyTa.getIdColaborador());
            values.put(COLUNA_CLIENTE, comprovanteSkyTa.getIdCliente());
            values.put(COLUNA_TIPO, comprovanteSkyTa.getTipo());
            values.put(COLUNA_ANEXO, comprovanteSkyTa.getAnexo());
            values.put(COLUNA_SYNC, comprovanteSkyTa.getSync());
            values.put(COLUNA_CAMPANHA, comprovanteSkyTa.getIdCampanha());
            values.put(COLUNA_IDMATERIAL, comprovanteSkyTa.getIdMaterial());
            values.put(COLUNA_INTERNO, comprovanteSkyTa.getInterno());
            return values;
        }

        private static String getString(Cursor cursor, String coluna) {
            return cursor.getString(cursor.getColumnIndex(coluna));
        }

        private static int getInt(Cursor cursor, String coluna) {
            return cursor.getInt(cursor.getColumnIndex(coluna));
        }
    }
}
