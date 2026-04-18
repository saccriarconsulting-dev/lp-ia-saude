package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SenhaVenda;

import java.util.ArrayList;

/**
 * Created by joao.viana on 26/07/2016.
 */
public class DBSenhaVenda {
    private Context mContext;
    private final String mTabela = "SenhaVenda";

    public DBSenhaVenda(Context _context) {
        mContext = _context;
    }

    public void addSenha(SenhaVenda obj) {
        ArrayList<SenhaVenda> lista = getSenha(false);
        ContentValues values = new ContentValues();
        values.put("senha", obj.getSenha());
        if (obj.getId() != 0)
            values.put("sync", 1);

        if (lista.size() > 0)
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(lista.get(0).getId())});
        else
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public ArrayList<SenhaVenda> getSenha(boolean sync) {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("SELECT id ");
        sbSql.append(",senha ");
        sbSql.append(",(SELECT id FROM Colaborador) idVendedor ");
        sbSql.append("FROM [SenhaVenda] ");
        sbSql.append("WHERE 1 = 1 ");
        if (sync)
            sbSql.append("AND sync = 0 ");

        ArrayList<SenhaVenda> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            SenhaVenda obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new SenhaVenda();
                    obj.setId(cursor.getInt(0));
                    obj.setSenha(cursor.getString(1));
                    obj.setIdVendedor(cursor.getString(2));
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

    public void updateSync(int pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}