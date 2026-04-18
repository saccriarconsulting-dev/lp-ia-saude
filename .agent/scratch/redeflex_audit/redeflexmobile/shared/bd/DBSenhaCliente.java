package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SenhaCliente;
import com.axys.redeflexmobile.shared.util.Util_DB;

import java.util.ArrayList;

/**
 * Created by joao.viana on 19/05/2017.
 */

public class DBSenhaCliente {
    private Context mContext;
    final private String mTabela = "SenhaCliente";

    public DBSenhaCliente(Context _context) {
        this.mContext = _context;
    }

    public void addSenha(SenhaCliente item) throws Exception {
        if (item.isIncluir()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("idCliente", item.getIdCliente());
            contentValues.put("senha", item.getSenha());
            if (!Util_DB.isCadastrado(mContext, mTabela, "id", item.getId())) {
                contentValues.put("id", item.getId());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, contentValues);
            } else
                SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, contentValues, "[id]=?", new String[]{item.getId()});
        } else
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{item.getId()});
    }

    public void deleteSenhasByIdCliente(String idCliente) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[idCliente]=? ", new String[]{idCliente});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<SenhaCliente> getSenhasByClienteId(String pIdCliente) {
        ArrayList<SenhaCliente> list = new ArrayList<>();
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT id ");
        sbSQL.append(",idCliente ");
        sbSQL.append(",senha ");
        sbSQL.append("FROM [SenhaCliente] ");
        sbSQL.append("WHERE idCliente = ? ");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{pIdCliente});
            SenhaCliente obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new SenhaCliente();
                    obj.setId(cursor.getString(0));
                    obj.setIdCliente(cursor.getString(1));
                    obj.setSenha(cursor.getString(2));
                    obj.setOperador("");
                    list.add(obj);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }
}