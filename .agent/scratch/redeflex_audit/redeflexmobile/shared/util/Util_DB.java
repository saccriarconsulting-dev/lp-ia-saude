package com.axys.redeflexmobile.shared.util;

import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joao.viana on 10/10/2017.
 */

public class Util_DB {
    public static boolean isCadastrado(Context pContext, String pTabela, String pCampo, String pValor) throws Exception {
        return isCadastrado(pContext, pTabela, new String[]{pCampo}, new String[]{pValor});
    }

    public static boolean isCadastrado(Context pContext, String pTabela, String[] pCampos, String[] pValores) throws Exception {
        Util_IO.StringBuilder sbCondicao = new Util_IO.StringBuilder();
        for (int i = 0; i < pCampos.length; i++) {
            sbCondicao.append(String.format("%1s = ?", pCampos[i]));
            if (i < pCampos.length - 1)
                sbCondicao.append(" AND ");
        }

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(pContext).query(pTabela, pCampos, sbCondicao.toString(), pValores, null, null, null, null)) {
            return (cursor != null && cursor.getCount() > 0);
        }
    }

    public static <T> ArrayList<T> RetornaLista(Context pContext, Class<T> pClass, String sql, String[] parametros) throws Exception {
        ArrayList<T> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(pContext).rawQuery(sql, parametros)) {
            if (cursor.moveToFirst()) {
                do {
                    T objeto = pClass.newInstance();
                    for (Field campo : pClass.getDeclaredFields()) {
                        int index = cursor.getColumnIndex(campo.getName());
                        if (index > -1) {
                            campo.setAccessible(true);
                            if (campo.getType().equals(String.class))
                                campo.set(objeto, cursor.getString(index));
                            else if (campo.getType().equals(Date.class))
                                campo.set(objeto, Util_IO.stringToDate(cursor.getString(index), Config.FormatDateTimeStringBanco));
                            else if (campo.getType().equals(boolean.class))
                                campo.set(objeto, Util_IO.stringToBoolean(cursor.getString(index)));
                            else if (campo.getType().equals(double.class)) {
                                campo.set(objeto, cursor.getDouble(index));
                            } else
                                campo.set(objeto, getValorCursor(cursor, index));
                        }
                    }
                    lista.add(objeto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Erro ao consultar");
        }
        return lista;
    }

    private static Object getValorCursor(Cursor pCursor, int pIndex) {
        switch (pCursor.getType(pIndex)) {
            case Cursor.FIELD_TYPE_STRING:
                return pCursor.getString(pIndex);
            case Cursor.FIELD_TYPE_INTEGER:
                return pCursor.getInt(pIndex);
            case Cursor.FIELD_TYPE_FLOAT:
                return pCursor.getDouble(pIndex);
            case Cursor.FIELD_TYPE_BLOB:
                return pCursor.getBlob(pIndex);
            default:
                return null;
        }
    }
}
