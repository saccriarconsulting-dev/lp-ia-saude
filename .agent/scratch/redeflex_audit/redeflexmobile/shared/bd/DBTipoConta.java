package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Filial;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Trigueiro 02/10/2020
 */
public class DBTipoConta {

    private final Context context;
    private final String TABLE_NAME = "TipoConta";
    private final String ID_COLUMN = "id";
    private final String DESCRIPTION_COLUMN = "descricao";
    private final String ACTIVE_COLUMN = "ativo";
    private final String WHERE_ID = ID_COLUMN + " = ? ";

    public DBTipoConta(Context context) {
        this.context = context;
    }

    public int add(@NotNull TipoConta tipoConta) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, tipoConta.getId());
        values.put(DESCRIPTION_COLUMN, tipoConta.getDescription());
        values.put(ACTIVE_COLUMN, tipoConta.isActive());
        try {
            if (!Util_DB.isCadastrado(context, TABLE_NAME, ID_COLUMN, String.valueOf(tipoConta.getId()))) {
                return (int) SimpleDbHelper.INSTANCE.open(context).insert(TABLE_NAME, null, values);
            } else {
                SimpleDbHelper.INSTANCE.open(context).update(
                        TABLE_NAME,
                        values,
                        WHERE_ID,
                        new String[]{String.valueOf(tipoConta.getId())});
                return tipoConta.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<TipoConta> getAll(@Nullable String where, @Nullable String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + ID_COLUMN);
        sb.appendLine(", " + DESCRIPTION_COLUMN);
        sb.appendLine(", " + ACTIVE_COLUMN);
        sb.appendLine("FROM [" + TABLE_NAME + "]");
        sb.appendLine("WHERE 1 = 1 ");
        if (where != null)
            sb.append(where);
        sb.append(" ORDER BY " + DESCRIPTION_COLUMN);

        return moverCursorDb(sb, fields);
    }

    public List<TipoConta> getAllActive() {
        return getAll("AND " + ACTIVE_COLUMN + " = ?", new String[]{"1"});
    }

    public TipoConta getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID, new String[]{String.valueOf(id)});
    }

    private ArrayList<TipoConta> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<TipoConta> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    TipoConta tipoConta = new TipoConta();
                    tipoConta.setId(cursor.getInt(0));
                    tipoConta.setDescription(cursor.getString(1));
                    tipoConta.setActive(cursor.getInt(2) == 1);
                    list.add(tipoConta);
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
