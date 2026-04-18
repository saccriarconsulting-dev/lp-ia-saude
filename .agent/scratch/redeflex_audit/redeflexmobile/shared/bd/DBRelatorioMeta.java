package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.RelatorioMeta;
import com.axys.redeflexmobile.shared.models.painelRelatorioMeta;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 23/05/2016.
 */
public class DBRelatorioMeta {
    private Context mContext;
    private final String TABELA = "RelatorioMeta";

    public DBRelatorioMeta(Context _context) {
        this.mContext = _context;
    }

    private boolean isCadastrado(Integer idIndicador, Integer idOperadora) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(TABELA, new String[]{"idIndicador"}
                , "idIndicador=? AND idOperadora=?",
                new String[]{String.valueOf(idIndicador), String.valueOf(idOperadora)}, null, null, null, null);
        try {
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void addRelatorio(RelatorioMeta obj) {
        ContentValues values = new ContentValues();
        values.put("idIndicador", obj.getIdIndicador());
        values.put("indicador", obj.getIndicador());
        values.put("idOperadora", obj.getIdOperadora());
        values.put("operadora", obj.getOperadora());
        values.put("meta", obj.getMeta());
        values.put("realizado", obj.getRealizado());
        values.put("tendencia", obj.getTendencia());
        if (!isCadastrado(obj.getIdIndicador(), obj.getIdOperadora()))
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);
        else {
            SimpleDbHelper.INSTANCE.open(mContext).update(TABELA, values,
                    "idIndicador=? AND idOperadora=?", new String[]{String.valueOf(obj.getIdIndicador()), String.valueOf(obj.getIdOperadora())});
        }
    }

    public ArrayList<painelRelatorioMeta> getPainel() {
        String selectQuery = "SELECT idIndicador,indicador, sum(tendencia) as tendencia FROM [RelatorioMeta] GROUP BY idIndicador,indicador";
        ArrayList<painelRelatorioMeta> lista = new ArrayList<>();
        Cursor cursor = null, cursor2 = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            painelRelatorioMeta obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new painelRelatorioMeta();
                    obj.setIndicador(true);
                    obj.setTexto(cursor.getString(1));
                    lista.add(obj);

                    obj = new painelRelatorioMeta();
                    obj.setIndicador(false);
                    obj.setIsHeader(true);
                    obj.setTendencia(cursor.getDouble(2));
                    lista.add(obj);

                    selectQuery = "SELECT idOperadora,meta,realizado,operadora,tendencia" +
                            " FROM [RelatorioMeta] WHERE idIndicador = " + cursor.getString(0) + " ORDER BY idOperadora desc";

                    cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            obj = new painelRelatorioMeta();
                            obj.setIndicador(false);
                            obj.setIsHeader(false);
                            obj.setIdoperadora(cursor2.getInt(0));
                            obj.setMeta(cursor2.getInt(1));
                            obj.setRealizado(cursor2.getInt(2));
                            obj.setTexto(cursor2.getString(3));
                            obj.setTendencia(cursor2.getDouble(4));
                            lista.add(obj);
                        } while (cursor2.moveToNext());
                    }
                    cursor2.close();
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
        return lista;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
    }

    public void delete(Integer idIndicador, Integer idOperadora) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, "idIndicador=? AND idOperadora=?",
                new String[]{String.valueOf(idIndicador), String.valueOf(idOperadora)});
    }

    public ArrayList<RelatorioMeta> getOperadoras() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT idOperadora, operadora ");
        sb.append("FROM [RelatorioMeta] ");
        sb.append("GROUP BY idOperadora, operadora ");
        sb.append("ORDER BY 2 ");

        ArrayList<RelatorioMeta> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            RelatorioMeta relatorioMeta;
            if (cursor.moveToFirst()) {
                do {
                    relatorioMeta = new RelatorioMeta();
                    relatorioMeta.setIdOperadora(cursor.getInt(0));
                    relatorioMeta.setOperadora(cursor.getString(1));
                    lista.add(relatorioMeta);
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

    public ArrayList<RelatorioMeta> getMetas(int pOperadora) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT meta, realizado, indicador, idOperadora, idIndicador,tendencia ");
        sb.append("FROM [RelatorioMeta] ");
        sb.append("WHERE idOperadora = " + pOperadora);
        sb.append(" ORDER BY idIndicador");

        ArrayList<RelatorioMeta> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            RelatorioMeta relatorioMeta;
            if (cursor.moveToFirst()) {
                do {
                    relatorioMeta = new RelatorioMeta();
                    relatorioMeta.setIdOperadora(cursor.getInt(3));
                    relatorioMeta.setMeta(cursor.getInt(0));
                    relatorioMeta.setRealizado(cursor.getInt(1));
                    relatorioMeta.setIndicador(cursor.getString(2));
                    relatorioMeta.setIdIndicador(cursor.getInt(4));
                    relatorioMeta.setTendencia(cursor.getDouble(5));
                    lista.add(relatorioMeta);
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
}