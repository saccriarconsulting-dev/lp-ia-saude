package com.axys.redeflexmobile.shared.bd.clientinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * @author lucasmarciano on 01/07/20
 */
public class DBFlagsBank {
    private final Context context;
    private final String TABLE_NAME = "Bandeiras";
    private final String ID_COLUMN = "idBandeira";
    private final String WHERE_ID = ID_COLUMN + " = ? ";

    public DBFlagsBank(Context context) {
        this.context = context;
    }

    public void insert(FlagsBank flag) throws Exception {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, flag.getId());
        values.put("nomeBandeira", flag.getName());
        values.put("imagem", flag.getImage());
        values.put("ativo", flag.isActive());

        if (!Util_DB.isCadastrado(context, TABLE_NAME, ID_COLUMN, String.valueOf(flag.getId())))
            SimpleDbHelper.INSTANCE.open(context).insert(
                    TABLE_NAME,
                    null,
                    values);
        else
            SimpleDbHelper.INSTANCE.open(context).update(
                    TABLE_NAME,
                    values,
                    WHERE_ID,
                    new String[]{String.valueOf(flag.getId())});
    }

    public FlagsBank getById(int id) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND " + WHERE_ID);
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{String.valueOf(id)}));
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID, new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    public ArrayList<FlagsBank> getAll(String conditions, String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT idBandeira");
        sb.appendLine(",nomeBandeira");
        sb.appendLine(",imagem ");
        sb.appendLine(",ativo ");
        sb.appendLine("FROM " + TABLE_NAME);
        sb.appendLine(" WHERE ativo = 1 ");
        if (conditions != null)
            sb.append(conditions);

        return moverCursorDb(sb, fields);
    }

    private ArrayList<FlagsBank> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<FlagsBank> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    FlagsBank flag = new FlagsBank();
                    flag.setId(cursor.getInt(0));
                    flag.setName(cursor.getString(1));
                    flag.setImage(new String(cursor.getBlob(2)));
                    flag.setActive(cursor.getInt(3) == 1);
                    list.add(flag);
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
