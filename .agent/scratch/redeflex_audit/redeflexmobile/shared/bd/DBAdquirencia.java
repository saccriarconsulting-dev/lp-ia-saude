package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Adquirencia;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitor Herrmann on 03/01/19.
 */
public class DBAdquirencia {

    private static final String TABELA = "RelatorioAdquirencia";
    private static final String COLUNA_INTEIRO = "[%s] INTEGER,\n";
    static final String CRIAR_TABELA = DBAdquirencia.Tabela.criarTabela();
    private Context mContext;

    public DBAdquirencia(Context mContext) {
        this.mContext = mContext;
    }

    private boolean isCadastrado(long idIndicador) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(TABELA, new String[]{String.valueOf(idIndicador)},
                Tabela.ID_INDICADOR + "=?",
                new String[]{String.valueOf(idIndicador)}, null, null, null, null)) {
            return (cursor != null && cursor.getCount() > 0);
        }
    }

    public void addAdquirencia(Adquirencia adquirencia) {
        ContentValues values = new ContentValues();
        values.put(Tabela.ID_INDICADOR, adquirencia.getIdIndicador());
        values.put(Tabela.INDICADOR, adquirencia.getIndicador());
        values.put(Tabela.META, adquirencia.getMeta());
        values.put(Tabela.REALIZADO, adquirencia.getRealizado());
        values.put(Tabela.FALTA, adquirencia.getFalta());
        values.put(Tabela.ATIVO, adquirencia.getAtivo());

        if (!isCadastrado(adquirencia.getIdIndicador())) {
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).update(TABELA, values,
                    Tabela.ID_INDICADOR + "=?",
                    new String[]{String.valueOf(adquirencia.getIdIndicador())});
        }
    }

    public List<Adquirencia> getAdquirencias() {
        Cursor cursor = null;
        List<Adquirencia> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT * FROM " + TABELA, null);
            Adquirencia adquirencia;
            if (cursor.moveToFirst()) {
                do {
                    adquirencia = new Adquirencia();
                    adquirencia.setIdIndicador(cursor.getInt(cursor.getColumnIndex(Tabela.ID_INDICADOR)));
                    adquirencia.setIndicador(cursor.getString(cursor.getColumnIndex(Tabela.INDICADOR)));
                    adquirencia.setMeta(cursor.getInt(cursor.getColumnIndex(Tabela.META)));
                    adquirencia.setRealizado(cursor.getInt(cursor.getColumnIndex(Tabela.REALIZADO)));
                    adquirencia.setFalta(cursor.getInt(cursor.getColumnIndex(Tabela.FALTA)));
                    adquirencia.setAtivo(cursor.getString(cursor.getColumnIndex(Tabela.ATIVO)));

                    lista.add(adquirencia);
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

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
    }

    public static class Tabela {
        static final String ID_INDICADOR = "idIndicador";
        static final String INDICADOR = "indicador";
        static final String META = "meta";
        static final String REALIZADO = "realizado";
        static final String FALTA = "falta";
        static final String ATIVO = "ativo";

        static String criarTabela() {
            return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                            COLUNA_INTEIRO +
                            "[%s] VARCHAR(100),\n" +
                            COLUNA_INTEIRO +
                            COLUNA_INTEIRO +
                            COLUNA_INTEIRO +
                            "[%s] VARCHAR(1))",
                    TABELA,
                    ID_INDICADOR,
                    INDICADOR,
                    META,
                    REALIZADO,
                    FALTA,
                    ATIVO);
        }
    }
}