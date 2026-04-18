package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

/**
 * Created by joao.viana on 15/12/2017.
 */

public class DBLimite {
    private Context mContext;
    private String mTabela = "LimiteCLiente";

    public DBLimite(Context pContext) {
        mContext = pContext;
    }

    public void addLimite(LimiteCliente pObject) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idCliente", pObject.getIdCliente());
        values.put("saldo", pObject.getSaldo());
        values.put("limite", pObject.getLimite());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", pObject.getId())) {
            values.put("id", pObject.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pObject.getId()});
    }

    private Util_IO.StringBuilder retornaQuery() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[idCliente]");
        sb.appendLine(",[saldo]");
        sb.appendLine(",[limite]");
        sb.appendLine("FROM [LimiteCLiente]");
        sb.appendLine("WHERE 1=1");
        return sb;
    }

    public LimiteCliente getById(int pId) {
        Util_IO.StringBuilder sb = retornaQuery();
        sb.appendLine("AND [id] = ?");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pId)});
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

    public LimiteCliente getByIdCliente(String pIdCliente) {
        Util_IO.StringBuilder sb = retornaQuery();
        sb.appendLine("AND [idcliente] = ?");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pIdCliente)});
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

    private LimiteCliente retornaObjectToCursor(Cursor pCursor) {
        try {
            LimiteCliente item = new LimiteCliente();
            item.setId(pCursor.getString(0));
            item.setIdCliente(pCursor.getString(1));
            item.setSaldo(pCursor.getDouble(2));
            item.setLimite(pCursor.getDouble(3));
            return item;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public void atualizaSaldo(double pValor, String pId) {
        String query = "UPDATE [LimiteCLiente] SET saldo = saldo - " + pValor + " WHERE id = " + pId;
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
    }
}