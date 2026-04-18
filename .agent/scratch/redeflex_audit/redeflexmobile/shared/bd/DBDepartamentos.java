package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Departamento;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/03/2017.
 */

public class DBDepartamentos {
    private static final String mTabela = "Departamentos";
    private Context mContext;

    public DBDepartamentos(Context pContext) {
        this.mContext = pContext;
    }

    public void addDepartamento(Departamento pDepartamento) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", pDepartamento.getId());
        values.put("descricao", pDepartamento.getDescricao());
        values.put("ativo", Util_IO.booleanToNumber(pDepartamento.isAtivo()));
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pDepartamento.getId()))) {
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pDepartamento.getId())});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<Departamento> getDepartamentos() {
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT id ");
        sbSQL.append(",descricao ");
        sbSQL.append(",ativo ");
        sbSQL.append("FROM [Departamentos] ");
        sbSQL.append("WHERE ativo = 1");
        Cursor cursor = null;
        ArrayList<Departamento> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
            Departamento obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new Departamento();
                    obj.setId(cursor.getInt(0));
                    obj.setDescricao(cursor.getString(1));
                    obj.setAtivo(Util_IO.numberToBoolean(cursor.getInt(2)));
                    lista.add(obj);
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